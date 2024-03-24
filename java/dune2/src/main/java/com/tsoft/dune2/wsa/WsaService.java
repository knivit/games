package com.tsoft.dune2.wsa;

import static com.tsoft.dune2.file.FileService.*;
import static com.tsoft.dune2.gfx.GfxService.*;
import static com.tsoft.dune2.gui.widget.WidgetService.g_widgetProperties;
import static com.tsoft.dune2.utils.CFunc.READ_LE_long;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class WsaService {

    /**
     * Get the amount of frames a WSA has.
     */
    public static int WSA_GetFrameCount(byte[] wsa) {
        WSAHeader header = WSAHeader.from(wsa);

        if (header == null) return 0;
        return header.frames;
    }

    /**
     * Get the offset in the fileContent which stores the animation data for a
     *  given frame.
     * @param header The header of the WSA.
     * @param frame The frame of animation.
     * @return The offset for the animation from the beginning of the fileContent.
     */
    static long WSA_GetFrameOffset_FromMemory(WSAHeader header, int frame) {
        long lengthAnimation = 0;
        long animationFrame;
        long animation0;

        animationFrame = READ_LE_long(header.fileContent, frame * 4);

        if (animationFrame == 0) return 0;

        animation0 = READ_LE_long(header.fileContent, 0);
        if (animation0 != 0) {
            lengthAnimation = READ_LE_long(header.fileContent, 4) - animation0;
        }

        return animationFrame - lengthAnimation - header.lengthHeader;
    }

    /**
     * Get the offset in the file which stores the animation data for a given
     *  frame.
     * @param fileno The fileno of an opened WSA.
     * @param frame The frame of animation.
     * @return The offset for the animation from the beginning of the file.
     */
    static long WSA_GetFrameOffset_FromDisk(int fileno, int frame, int lengthHeader) {
        long offset;

        File_Seek(fileno, frame * 4 + lengthHeader, 0);
        offset = File_Read_LE32(fileno);

        return offset;
    }

    /**
     * Go to the next frame in the animation.
     * @param wsa WSA pointer.
     * @param frame Frame number to go to.
     * @param dst Destination buffer to write the animation to.
     * @return 1 on success, 0 on failure.
     */
    static int WSA_GotoNextFrame(byte[] wsa, int frame, byte[] dst) {
        WSAHeader header = WSAHeader.from(wsa);
        int lengthPalette;
        byte[] buffer;

        lengthPalette = (header.flags.hasPalette) ? 0x300 : 0;

        buffer = header.buffer;

        if (header.flags.dataInMemory) {
            long positionStart;
            long positionEnd;
            long length;
            byte[] positionFrame;

            positionStart = WSA_GetFrameOffset_FromMemory(header, frame);
            positionEnd = WSA_GetFrameOffset_FromMemory(header, frame + 1);
            length = positionEnd - positionStart;

            positionFrame = header.fileContent + positionStart;
            buffer += header.bufferLength - length;

            memmove(buffer, positionFrame, length);
        } else if (header.flags.dataOnDisk) {
            int fileno;
            long positionStart;
            long positionEnd;
            long length;
            long res;

            fileno = File_Open(header.filename, FILE_MODE_READ);

            positionStart = WSA_GetFrameOffset_FromDisk(fileno, frame, header.lengthHeader);
            positionEnd = WSA_GetFrameOffset_FromDisk(fileno, frame + 1, header.lengthHeader);
            length = positionEnd - positionStart;

            if (positionStart == 0 || positionEnd == 0 || length == 0) {
                File_Close(fileno);
                return 0;
            }

            buffer += header.bufferLength - length;

            File_Seek(fileno, positionStart + lengthPalette, 0);
            res = File_Read(fileno, buffer, length);
            File_Close(fileno);

            if (res != length) return 0;
        }

        Format80_Decode(header.buffer, buffer, header.bufferLength);

        if (header.flags.displayInBuffer) {
            Format40_Decode(dst, header.buffer);
        } else {
            Format40_Decode_XorToScreen(dst, header.buffer, header.width);
        }

        return 1;
    }

    /**
     * Load a WSA file.
     * @param filename Name of the file.
     * @param wsa Data buffer for the WSA.
     * @param wsaSize Current size of buffer.
     * @param reserveDisplayFrame True if we need to reserve the display frame.
     * @return Address of loaded WSA file, or null.
     */
    public static byte[] WSA_LoadFile(String filename, byte[] wsa, long wsaSize, boolean reserveDisplayFrame) {
        WSAFlags flags;
        WSAFileHeader fileheader = new WSAFileHeader();
        WSAHeader header;
        long bufferSizeMinimal;
        long bufferSizeOptimal;
        int lengthHeader = 10;
        int lengthOffsets;
        int fileno;
        int lengthPalette;
        int lengthFirstFrame;
        long lengthFileContent;
        int displaySize;
        byte[] buffer;

        fileno = File_Open(filename, FILE_MODE_READ);
        fileheader.frames = File_Read_LE16(fileno);
        fileheader.width = File_Read_LE16(fileno);
        fileheader.height = File_Read_LE16(fileno);
        fileheader.requiredBufferSize = File_Read_LE16(fileno);
        fileheader.hasPalette = File_Read_LE16(fileno);		/* has palette */
        Debug("%s : %u %ux%u %u %x\n", filename, fileheader.frames, fileheader.width, fileheader.height, fileheader.requiredBufferSize, fileheader.hasPalette);
        fileheader.firstFrameOffset = (int)File_Read_LE32(fileno);	/* Offset of 1st frame */
        fileheader.secondFrameOffset = (int)File_Read_LE32(fileno);	/* Offset of 2nd frame (end of 1st frame) */
        if (fileheader.firstFrameOffset != (long)lengthHeader + 8 + 4 * fileheader.frames
            && fileheader.secondFrameOffset != (long)lengthHeader + 8 + 4 * fileheader.frames) {
            /* Old format from Dune v1.0 */
            lengthHeader = 8;
            fileheader.hasPalette = 0;
            File_Seek(fileno, -10, 1);
            fileheader.firstFrameOffset = (int)File_Read_LE32(fileno);
            fileheader.secondFrameOffset = (int)File_Read_LE32(fileno);
        }
        Debug("               %08x %08x\n", fileheader.firstFrameOffset, fileheader.secondFrameOffset);

        lengthPalette = 0;
        if (fileheader.hasPalette != 0) {
            flags.hasPalette = true;

            lengthPalette = 0x300;	/* length of a 256 color RGB palette */
        }

        lengthFileContent = File_Seek(fileno, 0, 2);

        lengthFirstFrame = 0;
        if (fileheader.firstFrameOffset != 0) {
            lengthFirstFrame = fileheader.secondFrameOffset - fileheader.firstFrameOffset;
        } else {
            flags.hasNoFirstFrame = true;	/* is the continuation of another WSA */
        }

        lengthFileContent -= lengthPalette + lengthFirstFrame + lengthHeader;

        displaySize = 0;
        if (reserveDisplayFrame) {
            flags.displayInBuffer = true;
            displaySize = fileheader.width * fileheader.height;
        }

        bufferSizeMinimal = displaySize + fileheader.requiredBufferSize - 33 + sizeof(WSAHeader);
        bufferSizeOptimal = bufferSizeMinimal + lengthFileContent;

        if (wsaSize > 1 && wsaSize < bufferSizeMinimal) {
            File_Close(fileno);

            return null;
        }
        if (wsaSize == 0) wsaSize = bufferSizeOptimal;
        if (wsaSize == 1) wsaSize = bufferSizeMinimal;

        if (wsa == null) {
            if (wsaSize == 0) {
                wsaSize = bufferSizeOptimal;
            } else if (wsaSize == 1) {
                wsaSize = bufferSizeMinimal;
            } else if (wsaSize >= bufferSizeOptimal) {
                wsaSize = bufferSizeOptimal;
            } else {
                wsaSize = bufferSizeMinimal;
            }

            wsa = calloc(1, wsaSize);
            flags.malloced = true;
        } else {
            flags.notmalloced = true;
        }

        header = WSAHeader.from(wsa);
        buffer = (uint8 *)wsa + sizeof(WSAHeader);

        header.flags = flags;
        header.lengthHeader = lengthHeader;

        if (reserveDisplayFrame) {
            memset(buffer, 0, displaySize);
        }

        buffer += displaySize;

        if ((fileheader.frames & 0x8000) != 0) {
            fileheader.frames &= 0x7FFF;
        }

        header.frameCurrent = fileheader.frames;
        header.frames       = fileheader.frames;
        header.width        = fileheader.width;
        header.height       = fileheader.height;
        header.bufferLength = fileheader.requiredBufferSize + 33 - sizeof(WSAHeader);
        header.buffer       = buffer;
        strncpy(header.filename, filename, sizeof(header.filename) - 1);
        header.filename[sizeof(header.filename) - 1] = '\0';

        lengthOffsets = (fileheader.frames + 2) * 4;

        if (wsaSize >= bufferSizeOptimal) {
            header.fileContent = buffer + header.bufferLength;

            File_Seek(fileno, lengthHeader, 0);
            File_Read(fileno, header.fileContent, lengthOffsets);
            File_Seek(fileno, lengthFirstFrame + lengthPalette, 1);
            File_Read(fileno, header.fileContent + lengthOffsets, lengthFileContent - lengthOffsets);

            header.flags.dataInMemory = true;
            if (WSA_GetFrameOffset_FromMemory(header, header.frames + 1) == 0) header.flags.noAnimation = true;
        } else {
            header.flags.dataOnDisk = true;
            if (WSA_GetFrameOffset_FromDisk(fileno, header.frames + 1, header.lengthHeader) == 0) header.flags.noAnimation = true;
        }

        {
            byte[] b;
            b = buffer + header.bufferLength - lengthFirstFrame;

            File_Seek(fileno, lengthHeader + lengthOffsets + lengthPalette, 0);
            File_Read(fileno, b, lengthFirstFrame);
            File_Close(fileno);

            Format80_Decode(buffer, b, header.bufferLength);
        }
        return wsa;
    }

    /**
     * Unload the WSA.
     * @param wsa The pointer to the WSA.
     */
    public static void WSA_Unload(byte[] wsa) {
        WSAHeader header = WSAHeader.from(wsa);

        if (wsa == null) return;
        if (!header.flags.malloced) return;

        free(wsa);
    }

    /**
     * Draw a frame on the buffer.
     * @param x The X-position to start drawing.
     * @param y The Y-position to start drawing.
     * @param width The width of the image.
     * @param height The height of the image.
     * @param windowID The windowID.
     * @param screenID the screen to write to
     * @param src The source for the frame.
     */
    static void WSA_DrawFrame(int x, int y, int width, int height, int windowID, byte[] src, int screenID) {
        int left;
        int right;
        int top;
        int bottom;
        int skipBefore;
        int skipAfter;
        byte[] dst;

        dst = GFX_Screen_Get_ByIndex(screenID);

        left   = g_widgetProperties[windowID].xBase << 3;
        right  = left + (g_widgetProperties[windowID].width << 3);
        top    = g_widgetProperties[windowID].yBase;
        bottom = top + g_widgetProperties[windowID].height;

        int srcOff = 0;
        if (y - top < 0) {
            if (y - top + height <= 0) return;
            height += y - top;
            srcOff += (top - y) * width;
            y += top - y;
        }

        if (bottom - y <= 0) return;
        height = min(bottom - y, height);

        skipBefore = 0;
        if (x - left < 0) {
            skipBefore = left - x;
            x += skipBefore;
            width -= skipBefore;
        }

        skipAfter = 0;
        if (right - x <= 0) return;
        if (right - x < width) {
            skipAfter = width - right + x;
            width = right - x;
        }

        int dstOff = y * SCREEN_WIDTH + x;

        while (height-- != 0) {
            srcOff += skipBefore;
            System.arraycopy(src, srcOff, dst, dstOff, width);
            srcOff += width + skipAfter;
            dstOff += SCREEN_WIDTH;
        }
    }

    /**
     * Display a frame.
     * @param wsa The pointer to the WSA.
     * @param frameNext The next frame to display.
     * @param posX The X-position of the WSA.
     * @param posY The Y-position of the WSA.
     * @param screenID The screenID to draw on.
     * @return False on failure, true on success.
     */
    public static boolean WSA_DisplayFrame(byte[] wsa, int frameNext, int posX, int posY, int screenID) {
        WSAHeader header = WSAHeader.from(wsa);
        byte[] dst;

        int i;
        int frame;
        int frameDiff;
        int direction;
        int frameCount;

        if (wsa == null) return false;
        if (frameNext >= header.frames) return false;

        int dstOff = 0;
        if (header.flags.displayInBuffer) {
            dst = wsa;
            dstOff = WSAHeader.sizeof();
        } else {
            dst = GFX_Screen_Get_ByIndex(screenID);
            dstOff += posX + posY * SCREEN_WIDTH;
        }

        if (header.frameCurrent == header.frames) {
            if (!header.flags.hasNoFirstFrame) {
                if (!header.flags.displayInBuffer) {
                    Format40_Decode_ToScreen(dst, header.buffer, header.width);
                } else {
                    Format40_Decode(dst, header.buffer);
                }
            }

            header.frameCurrent = 0;
        }

        frameDiff = abs(header.frameCurrent - frameNext);
        direction = 1;

        if (frameNext > header.frameCurrent) {
            frameCount = header.frames - frameNext + header.frameCurrent;

            if (frameCount < frameDiff && !header.flags.noAnimation) {
                direction = -1;
            } else {
                frameCount = frameDiff;
            }
        } else {
            frameCount = header.frames - header.frameCurrent + frameNext;

            if (frameCount < frameDiff && !header.flags.noAnimation) {
            } else {
                direction = -1;
                frameCount = frameDiff;
            }
        }

        frame = header.frameCurrent;
        if (direction > 0) {
            for (i = 0; i < frameCount; i++) {
                frame += direction;

                WSA_GotoNextFrame(wsa, frame, dst);

                if (frame == header.frames) frame = 0;
            }
        } else {
            for (i = 0; i < frameCount; i++) {
                if (frame == 0) frame = header.frames;

                WSA_GotoNextFrame(wsa, frame, dst);

                frame += direction;
            }
        }

        header.frameCurrent = frameNext;

        if (header.flags.displayInBuffer) {
            WSA_DrawFrame(posX, posY, header.width, header.height, 0, dst, screenID);
        }

        GFX_Screen_SetDirty(screenID, posX, posY, posX + header.width, posY + header.height);
        return true;
    }
}
