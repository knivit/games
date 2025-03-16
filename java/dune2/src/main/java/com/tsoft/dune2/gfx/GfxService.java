package com.tsoft.dune2.gfx;

import java.util.Arrays;

import static com.tsoft.dune2.gfx.Screen.SCREEN_0;
import static com.tsoft.dune2.gfx.Screen.SCREEN_ACTIVE;
import static com.tsoft.dune2.house.HouseType.HOUSE_MAX;
import static com.tsoft.dune2.opendune.OpenDuneService.g_dune2_enhanced;
import static com.tsoft.dune2.sprites.SpritesService.*;
import static com.tsoft.dune2.utils.CFunc.ucmp;
import static com.tsoft.dune2.video.VideoWin32Service.Video_GetFrameBuffer;
import static com.tsoft.dune2.video.VideoWin32Service.Video_SetPalette;
import static org.lwjgl.system.libc.LibCString.memcpy;
import static org.lwjgl.system.libc.LibCString.memset;

public class GfxService {

    public static final int SCREEN_WIDTH  = 320; /*!< Width of the screen in pixels. */
    public static final int SCREEN_HEIGHT = 200;  /*!< Height of the screen in pixels. */

    public static byte[] g_paletteActive = new byte[256 * 3];
    public static byte[] g_palette1 = null;
    static byte[] g_palette2 = null;
    static byte[] g_paletteMapping1 = null;
    public static byte[] g_paletteMapping2 = null;

    static int s_tileSpacing = 0;	/* bytes to skip between each line. == SCREEN_WIDTH - 2*s_tileWidth */
    static int s_tileHeight = 0;	/* "icon" sprites height (lines) */
    static int s_tileWidth = 0; 	/* "icon" sprites width in bytes. each bytes contains 2 pixels. 4 MSB = left, 4 LSB = right */
    static int  s_tileMode = 0;
    static int  s_tileByteSize = 0;	/* size in byte of one sprite pixel data = s_tileHeight * s_tileWidth / 2 */

    /* SCREEN_0 = 320x200 = 64000 = 0xFA00   The main screen buffer, 0xA0000 Video RAM in DOS Dune 2
     * SCREEN_1 = 64506 = 0xFBFA
     * SCREEN_2 = 320x200 = 64000 = 0xFA00
     * SCREEN_3 = 64781 = 0xFD0D    * NEVER ACTIVE * only used for game credits and intro */
    static final int GFX_SCREEN_BUFFER_COUNT = 4;
    static int[] s_screenBufferSize = new int[] { 0xFA00, 0xFBF4, 0xFA00, 0xFD0D/*, 0xA044*/ };
    static byte[][] s_screenBuffer = new byte[][] { new byte[0], new byte[0], new byte[0], new byte[0] };

    static boolean s_screen0_is_dirty = false;
    static DirtyArea s_screen0_dirty_area = new DirtyArea(0, 0, 0, 0);
    static long[] g_dirty_blocks = new long[200];

    static int s_screenActiveID = SCREEN_0;

    /**
     * Returns the size of a screenbuffer.
     * @param screenID The screenID to get the size of.
     * @return Some size value.
     */
    public static int GFX_Screen_GetSize_ByIndex(int screenID) {
        if (screenID == SCREEN_ACTIVE) {
            screenID = s_screenActiveID;
        }
        assert(screenID >= 0 && screenID < GFX_SCREEN_BUFFER_COUNT);
        return s_screenBufferSize[screenID];
    }

    /**
     * Get the pointer to a screenbuffer.
     * @param screenID The screenbuffer to get.
     * @return A pointer to the screenbuffer.
     */
    public static byte[] GFX_Screen_Get_ByIndex(int screenID) {
        if (screenID == SCREEN_ACTIVE) {
            screenID = s_screenActiveID;
        }
        assert(screenID >= 0 && screenID < GFX_SCREEN_BUFFER_COUNT);
        return s_screenBuffer[screenID];
    }

    /**
     * Change the current active screen to the new value.
     * @param screenID The new screen to get active.
     * @return Old screenID that was currently active.
     */
    public static int GFX_Screen_SetActive(int screenID) {
        int oldScreen = s_screenActiveID;
        if (screenID != SCREEN_ACTIVE) {
            s_screenActiveID = screenID;
        }
        return oldScreen;
    }

    /**
     * Checks if the screen is active.
     * @param screenID The screen to check for being active
     * @return true or false.
     */
    public static boolean GFX_Screen_IsActive(int screenID) {
        if (screenID == SCREEN_ACTIVE) return true;
        return (screenID == s_screenActiveID);
    }

    public static void GFX_Screen_SetDirty(int screenID, int left, int top, int right, int bottom) {
        if (screenID == SCREEN_ACTIVE) screenID = s_screenActiveID;
        if (screenID != SCREEN_0) return;
        s_screen0_is_dirty = true;
        if (left < s_screen0_dirty_area.left) s_screen0_dirty_area.left = left;
        if (top < s_screen0_dirty_area.top) s_screen0_dirty_area.top = top;
        if (right > s_screen0_dirty_area.right) s_screen0_dirty_area.right = right;
        if (bottom > s_screen0_dirty_area.bottom) s_screen0_dirty_area.bottom = bottom;

        long mask = (1 << ((right + 15) >> 4)) - 1;
        mask -= (1 << (left >> 4)) - 1;
        for (int y = top; y < bottom; y++) {
            g_dirty_blocks[y] |= mask;
        }
    }

    public static void GFX_Screen_SetClean(int screenID) {
        if (screenID == SCREEN_ACTIVE) screenID = s_screenActiveID;
        if (screenID != SCREEN_0) return;
        s_screen0_is_dirty = false;
        s_screen0_dirty_area.left = 0xffff;
        s_screen0_dirty_area.top = 0xffff;
        s_screen0_dirty_area.right = 0;
        s_screen0_dirty_area.bottom = 0;

        memset(g_dirty_blocks, 0);
    }

    public static boolean GFX_Screen_IsDirty(int screenID) {
        if(screenID == SCREEN_ACTIVE) screenID = s_screenActiveID;
        if(screenID != SCREEN_0) return true;
        return s_screen0_is_dirty;
    }

    static DirtyArea GFX_Screen_GetDirtyArea(int screenID) {
        if (screenID == SCREEN_ACTIVE) screenID = s_screenActiveID;
        if (screenID != SCREEN_0) return null;
        return s_screen0_dirty_area;
    }

    /**
     * Initialize the GFX system.
     */
    public static void GFX_Init() {
        /* init g_paletteActive with invalid values so first GFX_SetPalette() will be ok */
        Arrays.fill(g_paletteActive, (byte)0xff);

        for (int i = 1; i < GFX_SCREEN_BUFFER_COUNT; i++) {
            int size = GFX_Screen_GetSize_ByIndex(i);
            s_screenBuffer[i] = new byte[size];
        }

        /* special case for SCREEN_0 which is the MCGA frame buffer */
        s_screenBuffer[0] = Video_GetFrameBuffer(GFX_Screen_GetSize_ByIndex(0));

        s_screenActiveID = SCREEN_0;
    }

    /**
     * Uninitialize the GFX system.
     */
    public static void GFX_Uninit() {
        for (int i = 0; i < GFX_SCREEN_BUFFER_COUNT; i++) {
            s_screenBuffer[i] = null;
        }
    }

    /**
     * Draw a tile on the screen.
     * @param tileID The tile to draw.
     * @param x The x-coordinate to draw the sprite.
     * @param y The y-coordinate to draw the sprite.
     * @param houseID The house the sprite belongs (for recolouring).
     */
    public static void GFX_DrawTile(int tileID, int x, int y, int houseID) {
        int i, j;
        byte[] icon_palette;
        byte[] local_palette = new byte[16];

        assert(houseID < HOUSE_MAX);

        if (s_tileMode == 4) return;

        icon_palette = g_iconRPAL + (g_iconRTBL[tileID] << 4);

        if (houseID != 0) {
            /* Remap colors for the right house */
            for (i = 0; i < 16; i++) {
                byte colour = icon_palette[i];

                /* ENHANCEMENT -- Dune2 recolours too many colours, causing clear graphical glitches in the IX building */
                if ((colour & 0xF0) == 0x90) {
                    if (ucmp(colour, (byte)0x96) <= 0 || !g_dune2_enhanced) colour += houseID << 4;
                }
                local_palette[i] = colour;
            }
            icon_palette = local_palette;
        }

        byte[] wptr = GFX_Screen_GetActive();
        int wptrPos = y * SCREEN_WIDTH + x;
        byte[] rptr = g_tilesPixels + (tileID * s_tileByteSize);
        int rptrPos = 0;

        /* tiles with transparent pixels : [1 : 33] U [108 : 122] and 124
         * palettes 1 to 18 and 22 and 24 */
        /*if (tileID <= 33 || (tileID >= 108 && tileID <= 124)) {*/
        /* We've found that all "transparent" icons/tiles have 0 (transparent) as color 0 */
        if (icon_palette[0] == 0) {
            for (j = 0; j < s_tileHeight; j++) {
                for (i = 0; i < s_tileWidth; i++) {
                    byte left  = icon_palette[rptr[rptrPos] >> 4];
                    byte right = icon_palette[rptr[rptrPos] & 0xF];
                    rptrPos++;

                    if (left != 0) wptr[wptrPos] = left;
                    wptrPos++;
                    if (right != 0) wptr[wptrPos] = right;
                    wptr++;
                }
                wptr += s_tileSpacing;
            }
        } else {
            for (j = 0; j < s_tileHeight; j++) {
                for (i = 0; i < s_tileWidth; i++) {
				*wptr++ = icon_palette[(*rptr) >> 4];
				*wptr++ = icon_palette[(*rptr) & 0xF];
                    rptr++;
                }
                wptr += s_tileSpacing;
            }
        }
    }

    /**
     * Initialize sprite information.
     *
     * @param widthSize Value between 0 and 2, indicating the width of the sprite. x8 to get actual width of sprite
     * @param heightSize Value between 0 and 2, indicating the width of the sprite. x8 to get actual width of sprite
     */
    public static void GFX_Init_TilesInfo(int widthSize, int heightSize) {
        /* NOTE : shouldn't it be (heightSize < 3 && widthSize < 3) ??? */
        if (widthSize == heightSize && widthSize < 3) {
            s_tileMode = widthSize & 2;

            s_tileWidth   = widthSize << 2;
            s_tileHeight  = heightSize << 3;
            s_tileSpacing = SCREEN_WIDTH - s_tileHeight;
            s_tileByteSize = s_tileWidth * s_tileHeight;
        } else {
            /* NOTE : is it dead code ? */
            /* default to 8x8 sprites */
            s_tileMode = 4;
            s_tileByteSize = 8*4;

            s_tileWidth   = 4;
            s_tileHeight  = 8;
            s_tileSpacing = 312;
        }
    }

    /**
     * Put a pixel on the screen.
     * @param x The X-coordinate on the screen.
     * @param y The Y-coordinate on the screen.
     * @param colour The colour of the pixel to put on the screen.
     */
    public static void GFX_PutPixel(int x, int y, byte colour) {
        if (y >= SCREEN_HEIGHT) return;
        if (x >= SCREEN_WIDTH) return;

	    GFX_Screen_GetActive()[y * SCREEN_WIDTH + x] = colour;
    }

    /**
     * Copy information from one screenbuffer to the other.
     * @param xSrc The X-coordinate on the source.
     * @param ySrc The Y-coordinate on the source.
     * @param xDst The X-coordinate on the destination.
     * @param yDst The Y-coordinate on the destination.
     * @param width The width.
     * @param height The height.
     * @param screenSrc The ID of the source screen.
     * @param screenDst The ID of the destination screen.
     * @param skipnull Wether to skip pixel colour 0.
     */
    public static void GFX_Screen_Copy2(int xSrc, int ySrc, int xDst, int yDst, int width, int height, int screenSrc, int screenDst, boolean skipnull) {
        if (xSrc >= SCREEN_WIDTH) return;
        if (xSrc < 0) {
            xDst += xSrc;
            width += xSrc;
            xSrc = 0;
        }

        if (ySrc >= SCREEN_HEIGHT) return;
        if (ySrc < 0) {
            yDst += ySrc;
            height += ySrc;
            ySrc = 0;
        }

        if (xDst >= SCREEN_WIDTH) return;
        if (xDst < 0) {
            xSrc += xDst;
            width += xDst;
            xDst = 0;
        }

        if (yDst >= SCREEN_HEIGHT) return;
        if (yDst < 0) {
            ySrc += yDst;
            height += yDst;
            yDst = 0;
        }

        if (SCREEN_WIDTH - xSrc - width < 0) width = SCREEN_WIDTH - xSrc;
        if (SCREEN_HEIGHT - ySrc - height < 0) height = SCREEN_HEIGHT - ySrc;
        if (SCREEN_WIDTH - xDst - width < 0) width = SCREEN_WIDTH - xDst;
        if (SCREEN_HEIGHT - yDst - height < 0) height = SCREEN_HEIGHT - yDst;

        if (xSrc < 0 || xSrc >= SCREEN_WIDTH) return;
        if (xDst < 0 || xDst >= SCREEN_WIDTH) return;
        if (ySrc < 0 || ySrc >= SCREEN_HEIGHT) return;
        if (yDst < 0 || yDst >= SCREEN_HEIGHT) return;
        if (width < 0 || width >= SCREEN_WIDTH) return;
        if (height < 0 || height >= SCREEN_HEIGHT) return;

        GFX_Screen_SetDirty(screenDst, xDst, yDst, xDst + width, yDst + height);

        byte[] src = GFX_Screen_Get_ByIndex(screenSrc);
        byte[] dst = GFX_Screen_Get_ByIndex(screenDst);

        int srcPos = xSrc + ySrc * SCREEN_WIDTH;
        int dstPos = xDst + yDst * SCREEN_WIDTH;

        while (height-- != 0) {
            if (skipnull) {
                int i;
                for (i = 0; i < width; i++) {
                    if (src[i] != 0) dst[i] = src[i];
                }
            } else {
                System.arraycopy(src, srcPos, dst, dstPos, width);
            }
            dstPos += SCREEN_WIDTH;
            srcPos += SCREEN_WIDTH;
        }
    }

    /**
     * Copy information from one screenbuffer to the other.
     * @param xSrc The X-coordinate on the source.
     * @param ySrc The Y-coordinate on the source.
     * @param xDst The X-coordinate on the destination.
     * @param yDst The Y-coordinate on the destination.
     * @param width The width.
     * @param height The height.
     * @param screenSrc The ID of the source screen.
     * @param screenDst The ID of the destination screen.
     */
    public static void GFX_Screen_Copy(int xSrc, int ySrc, int xDst, int yDst, int width, int height, int screenSrc, int screenDst) {
        if (xSrc >= SCREEN_WIDTH) return;
        if (xSrc < 0) xSrc = 0;

        if (ySrc >= SCREEN_HEIGHT) return;
        if (ySrc < 0) ySrc = 0;

        if (xDst >= SCREEN_WIDTH) return;
        if (xDst < 0) xDst = 0;

        if ((yDst + height) > SCREEN_HEIGHT) {
            height = SCREEN_HEIGHT - 1 - yDst;
        }
        if (height <= 0) return;

        if (yDst >= SCREEN_HEIGHT) return;
        if (yDst < 0) yDst = 0;

        if (width <= 0 || width > SCREEN_WIDTH) return;

        byte[] src = GFX_Screen_Get_ByIndex(screenSrc);
        byte[] dst = GFX_Screen_Get_ByIndex(screenDst);

        int srcPos = xSrc + ySrc * SCREEN_WIDTH;
        int dstPos = xDst + yDst * SCREEN_WIDTH;

        GFX_Screen_SetDirty(screenDst, xDst, yDst, xDst + width, yDst + height);

        if (width == SCREEN_WIDTH) {
            System.arraycopy(src, srcPos, dst, dstPos, height * SCREEN_WIDTH);
        } else {
            while (height-- != 0) {
                System.arraycopy(src, srcPos, dst, dstPos, width);
                dstPos += SCREEN_WIDTH;
                srcPos += SCREEN_WIDTH;
            }
        }
    }

    /**
     * Clears the screen.
     */
    public static void GFX_ClearScreen(int screenID) {
        Arrays.fill(GFX_Screen_Get_ByIndex(screenID), 0, SCREEN_WIDTH * SCREEN_HEIGHT, (byte)0);
        GFX_Screen_SetDirty(screenID, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Clears the given memory block.
     * @param index The memory block.
     */
    public static void GFX_ClearBlock(int index) {
        Arrays.fill(GFX_Screen_Get_ByIndex(index), 0, GFX_Screen_GetSize_ByIndex(index), (byte)0);
        GFX_Screen_SetDirty(index, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Set a new palette for the screen.
     * @param palette The palette in RGB order.
     */
    public static void GFX_SetPalette(byte[] palette) {
        int from, to;

        for (from = 0; from < 256; from++) {
            if (palette[from*3] != g_paletteActive[from*3] ||
                palette[from*3+1] != g_paletteActive[from*3+1] ||
                palette[from*3+2] != g_paletteActive[from*3+2]) break;
        }

        if (from >= 256) {
            Warning("Useless GFX_SetPalette() call\n");
            return;
        }

        for (to = 255; to > from; to--) {
            if (palette[to*3] != g_paletteActive[to*3] ||
                palette[to*3+1] != g_paletteActive[to*3+1] ||
                palette[to*3+2] != g_paletteActive[to*3+2]) break;
        }
        Video_SetPalette(palette + 3 * from, from, to - from + 1);

        memcpy(g_paletteActive + 3 * from, palette + 3 * from, (to - from + 1) * 3);
    }

    /**
     * Get a pixel on the screen.
     * @param x The X-coordinate on the screen.
     * @param y The Y-coordinate on the screen.
     * @return The colour of the pixel.
     */
    public static byte GFX_GetPixel(int x, int y) {
        if (y >= SCREEN_HEIGHT) return 0;
        if (x >= SCREEN_WIDTH) return 0;

        return GFX_Screen_GetActive()[y * SCREEN_WIDTH + x];
    }

    public static int GFX_GetSize(int width, int height) {
        if (width < 1) width = 1;
        if (width > SCREEN_WIDTH) width = SCREEN_WIDTH;
        if (height < 1) height = 1;
        if (height > SCREEN_HEIGHT) height = SCREEN_HEIGHT;

        return width * height;
    }

    /**
     * Copy information from a buffer to the screen.
     * @param left The X-coordinate on the screen.
     * @param top The Y-coordinate on the screen.
     * @param width The width.
     * @param height The height.
     * @param buffer The buffer to copy from.
     */
    public static void GFX_CopyFromBuffer(int left, int top, int width, int height, byte[] buffer) {
        if (width == 0) return;
        if (height == 0) return;

        if (left < 0) left = 0;
        if (left >= SCREEN_WIDTH) left = SCREEN_WIDTH - 1;

        if (top < 0) top = 0;
        if (top >= SCREEN_HEIGHT) top = SCREEN_HEIGHT - 1;

        if (width  > SCREEN_WIDTH - left) width  = SCREEN_WIDTH - left;
        if (height > SCREEN_HEIGHT - top) height = SCREEN_HEIGHT - top;

        byte[] screen = GFX_Screen_Get_ByIndex(SCREEN_0);
        int dstPos = top * SCREEN_WIDTH + left;

        GFX_Screen_SetDirty(SCREEN_0, left, top, left + width, top + height);

        int srcPos = 0;
        while (height-- != 0) {
            System.arraycopy(buffer, srcPos, screen, dstPos, width);
            dstPos += SCREEN_WIDTH;
            srcPos += width;
        }
    }

    /**
     * Copy information from the screen to a buffer.
     * @param left The X-coordinate on the screen.
     * @param top The Y-coordinate on the screen.
     * @param width The width.
     * @param height The height.
     * @param buffer The buffer to copy to.
     */
    public static void GFX_CopyToBuffer(int left, int top, int width, int height, byte[] buffer) {
        if (width == 0) return;
        if (height == 0) return;

        if (left < 0) left = 0;
        if (left >= SCREEN_WIDTH) left = SCREEN_WIDTH - 1;

        if (top < 0) top = 0;
        if (top >= SCREEN_HEIGHT) top = SCREEN_HEIGHT - 1;

        if (width  > SCREEN_WIDTH - left) width  = SCREEN_WIDTH - left;
        if (height > SCREEN_HEIGHT - top) height = SCREEN_HEIGHT - top;

        byte[] screen = GFX_Screen_Get_ByIndex(SCREEN_0);
        int srcPos = top * SCREEN_WIDTH + left;
        int dstPos = 0;

        while (height-- != 0) {
            System.arraycopy(screen, srcPos, buffer, dstPos, width);
            srcPos += SCREEN_WIDTH;
            dstPos += width;
        }
    }

    public static byte[] GFX_Screen_GetActive() {
        return GFX_Screen_Get_ByIndex(SCREEN_ACTIVE);
    }
}
