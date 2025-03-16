package com.tsoft.dune2.input;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.tsoft.dune2.file.FileService.*;
import static com.tsoft.dune2.gfx.GfxService.SCREEN_HEIGHT;
import static com.tsoft.dune2.gfx.GfxService.SCREEN_WIDTH;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.input.InputFlagsEnum.INPUT_FLAG_KEY_RELEASE;
import static com.tsoft.dune2.input.InputFlagsEnum.INPUT_FLAG_MOUSE_EMUL;
import static com.tsoft.dune2.input.InputMouseMode.*;
import static com.tsoft.dune2.input.InputService.*;
import static com.tsoft.dune2.tools.ToolsService.Tools_RandomLCG_Seed;
import static com.tsoft.dune2.tools.ToolsService.Tools_Random_Seed;
import static com.tsoft.dune2.video.VideoWin32Service.Video_Mouse_SetRegion;
import static java.lang.Math.abs;

public class MouseService {

    public static int g_mouseLock;          /*!< Lock for when handling mouse movement. */

    public static int g_mouseX;             /*!< Current X position of the mouse. */
    public static int g_mouseY;             /*!< Current Y position of the mouse. */
    public static int g_mousePrevX;         /*!< Previous X position of the mouse. */
    public static int g_mousePrevY;         /*!< Previous Y position of the mouse. */

    public static int g_prevButtonState;    /*!< Previous mouse button state. */
    public static int g_mouseClickX;        /*!< X position of last mouse click. */
    public static int g_mouseClickY;        /*!< Y position of last mouse click. */

    public static int g_regionFlags;        /*!< Flags: 0x4000 - Mouse still inside region, 0x8000 - Region check. 0x00FF - Countdown to showing. */
    public static int g_mouseRegionLeft;    /*!< Region mouse can be in - left position. */
    public static int g_mouseRegionRight;   /*!< Region mouse can be in - right position. */
    public static int g_mouseRegionTop;     /*!< Region mouse can be in - top position. */
    public static int g_mouseRegionBottom;  /*!< Region mouse can be in - bottom position. */

    public static int g_regionMinX;         /*!< Region - minimum value for X position. */
    public static int g_regionMinY;         /*!< Region - minimum value for Y position. */
    public static int g_regionMaxX;         /*!< Region - maximum value for X position. */
    public static int g_regionMaxY;         /*!< Region - maximum value for Y position. */

    public static int g_mouseDisabled;       /*!< Mouse disabled flag */
    public static int g_mouseHiddenDepth;
    public static int g_mouseFileID;

    public static boolean g_mouseNoRecordedValue; /*!< used in INPUT_MOUSE_MODE_PLAY */
    public static int g_mouseInputValue;
    public static int g_mouseRecordedTimer;
    public static int g_mouseRecordedX;
    public static int g_mouseRecordedY;

    public static int g_mouseMode;
    public static int g_inputFlags;

    /**
     * Initialize the mouse driver.
     */
    public static void Mouse_Init() {
        g_mouseX = SCREEN_WIDTH / 2;
        g_mouseY = SCREEN_HEIGHT / 2;
        g_mouseHiddenDepth = 1;
        g_regionFlags = 0;
        g_mouseRegionRight = SCREEN_WIDTH - 1;
        g_mouseRegionBottom = SCREEN_HEIGHT - 1;

        g_mouseDisabled = 1;
        g_mouseFileID = FILE_INVALID;

        Video_Mouse_SetPosition(g_mouseX, g_mouseY);
    }

    /**
     * Handle the new mouse event.
     */
    public static void Mouse_EventHandler(int mousePosX, int mousePosY, boolean mouseButtonLeft, boolean mouseButtonRight) {
        int newButtonState = (mouseButtonLeft ? 0x1 : 0x0) | (mouseButtonRight ? 0x2 : 0x0);

        if (g_mouseDisabled == 0) {
            if (g_mouseMode == INPUT_MOUSE_MODE_NORMAL && (g_inputFlags & INPUT_FLAG_MOUSE_EMUL) == 0) {
                Input_HandleInput(Mouse_CheckButtons(newButtonState));
            }

            if (g_mouseMode != INPUT_MOUSE_MODE_PLAY && g_mouseLock == 0) {
                Mouse_HandleMovement(newButtonState, mousePosX, mousePosY);
            }
        }
    }

    /**
     * Set the region in which the mouse can move.
     * @note This limits the mouse movement in the hardware.
     *
     * @param left The left side of the region.
     * @param top The top side of the region.
     * @param right The right side of the region.
     * @param bottom The bottom side of the region.
     */
    public static void Mouse_SetRegion(int left, int top, int right, int bottom) {
        if (left > right) {
            int temp = left;
            left = right;
            right = temp;
        }
        if (top > bottom) {
            int temp = top;
            top = bottom;
            bottom = temp;
        }

        left   = clamp(left,   0, SCREEN_WIDTH - 1);
        right  = clamp(right,  0, SCREEN_WIDTH - 1);
        top    = clamp(top,    0, SCREEN_HEIGHT - 1);
        bottom = clamp(bottom, 0, SCREEN_HEIGHT - 1);

        g_mouseRegionLeft   = left;
        g_mouseRegionRight  = right;
        g_mouseRegionTop    = top;
        g_mouseRegionBottom = bottom;

        Video_Mouse_SetRegion(left, right, top, bottom);
    }

    /**
     * Test whether the mouse cursor is at the border or inside the given rectangle.
     * @param left Left edge.
     * @param top  Top edge.
     * @param right Right edge.
     * @param bottom Bottom edge.
     * @return Mouse is at the border or inside the rectangle.
     */
    static int Mouse_InsideRegion(int left, int top, int right, int bottom) {
        int mx, my;
        int inside;

        while (g_mouseLock != 0) sleepIdle();
        g_mouseLock++;

        mx = g_mouseX;
        my = g_mouseY;

        inside = (mx < left || mx > right || my < top || my > bottom) ? 0 : 1;

        g_mouseLock--;
        return inside;
    }

    public static void Mouse_SetMouseMode(int mouseMode, String filename) {
        switch (mouseMode) {
            default: break;

            case INPUT_MOUSE_MODE_NORMAL:
                g_mouseMode = mouseMode;
                if (g_mouseFileID != FILE_INVALID) {
                    Input_Flags_ClearBits(INPUT_FLAG_KEY_RELEASE);
                    File_Close(g_mouseFileID);
                    g_mouseFileID = FILE_INVALID;
                }
                g_mouseNoRecordedValue = true;
                break;

            case INPUT_MOUSE_MODE_RECORD:
                if (g_mouseFileID != FILE_INVALID) break;

                File_Delete_Personal(filename);
                File_Create_Personal(filename);

                Tools_RandomLCG_Seed(0x1234);
                Tools_Random_Seed(0x12344321);

                g_mouseFileID = File_Open_Personal(filename, FILE_MODE_READ_WRITE);

                g_mouseMode = mouseMode;

                Input_Flags_SetBits(INPUT_FLAG_KEY_RELEASE);

                Input_HandleInput(0x2D);
                break;

            case INPUT_MOUSE_MODE_PLAY:
                if (g_mouseFileID == FILE_INVALID) {
                    g_mouseFileID = File_Open_Personal(filename, FILE_MODE_READ);
                    if (g_mouseFileID == FILE_INVALID) {
                        Error("Cannot open '%s', replay log is impossible.\n", filename);
                        return;
                    }

                    Tools_RandomLCG_Seed(0x1234);
                    Tools_Random_Seed(0x12344321);
                }

                g_mouseNoRecordedValue = true;

                File_Read(g_mouseFileID, &g_mouseInputValue, 2);
                if (File_Read(g_mouseFileID, &g_mouseRecordedTimer, 2) != 2) break;;

            if ((g_mouseInputValue >= 0x41 && g_mouseInputValue <= 0x44) || g_mouseInputValue == 0x2D) {
                /* 0x2D == '-' 0x41 == 'A' [...] 0x44 == 'D' */
                File_Read(g_mouseFileID, &g_mouseRecordedX, 2);
                if (File_Read(g_mouseFileID, &g_mouseRecordedY, 2) == 2) {
                    g_mouseX = g_mouseRecordedX;
                    g_mouseY = g_mouseRecordedY;
                    g_prevButtonState = 0;

                    GUI_Mouse_Hide_Safe();
                    GUI_Mouse_Show_Safe();

                    g_mouseNoRecordedValue = false;
                    break;
                }
                g_mouseNoRecordedValue = true;
                break;
            }
            g_mouseNoRecordedValue = false;
            break;
        }

        g_timerInput = 0;
        g_mouseMode = mouseMode;
    }

    /**
     * Compare mouse button state with previous value, and report changes.
     * @param newButtonState New button state.
     * @return \c 0x2D if no change, \c 0x41 for change in first button state,
     *     \c 0x42 for change in second button state, bit 11 means 'button released'.
     */
    private static int Mouse_CheckButtons(int newButtonState) {
        int change;
        int result;

        newButtonState &= 0xFF;

        result = 0x2D;
        change = newButtonState ^ g_prevButtonState;
        if (change == 0) return result;

        g_prevButtonState = newButtonState & 0xFF;

        if ((change & 0x2) != 0) {
            result = 0x42;
            if ((newButtonState & 0x2) == 0) {
                result |= 0x800;	/* RELEASE */
            }
        }

        if ((change & 0x1) != 0) {
            result = 0x41;
            if ((newButtonState & 0x1) == 0) {
                result |= 0x800;	/* RELEASE */
            }
        }

        return result;
    }

    /**
     * If the mouse has moved, update its coordinates, and update the region flags.
     * @param mouseX New mouse X coordinate.
     * @param mouseY New mouse Y coordinate.
     */
    public static void Mouse_CheckMovement(int mouseX, int mouseY) {
        if (g_mouseHiddenDepth == 0 && (g_mousePrevX != mouseX || g_mousePrevY != mouseY)) {
            if ((g_regionFlags & 0xC000) != 0xC000) {
                GUI_Mouse_Hide();

                if ((g_regionFlags & 0x8000) == 0) {
                    GUI_Mouse_Show();
                    g_mousePrevX = mouseX;
                    g_mousePrevY = mouseY;
                    g_mouseLock = 0;
                    return;
                }
            }

            if (mouseX >= g_regionMinX && mouseX <= g_regionMaxX &&
                mouseY >= g_regionMinY && mouseY <= g_regionMaxY) {
                g_regionFlags |= 0x4000;
            } else {
                GUI_Mouse_Show();
            }
        }

        g_mousePrevX = mouseX;
        g_mousePrevY = mouseY;
        g_mouseLock = 0;
    }

    /**
     * Handle movement of the mouse.
     * @param newButtonState State of the mouse buttons.
     * @param mouseX Horizontal position of the mouse cursor.
     * @param mouseY Vertical position of the mouse cursor.
     */
    private static void Mouse_HandleMovement(int newButtonState, int mouseX, int mouseY) {
        g_mouseLock = 0x1;

        g_mouseX = mouseX;
        g_mouseY = mouseY;
        if (g_mouseMode != INPUT_MOUSE_MODE_PLAY && g_mouseMode != INPUT_MOUSE_MODE_NORMAL && (g_inputFlags & INPUT_FLAG_MOUSE_EMUL) == 0) {
            Input_HandleInput(Mouse_CheckButtons(newButtonState));
        }

        Mouse_CheckMovement(mouseX, mouseY);
    }

    /**
     * Perform handling of mouse movement iff the mouse position changed.
     * @param newButtonState New button state.
     */
    public static void Mouse_HandleMovementIfMoved(int newButtonState) {
        if (abs(g_mouseX - g_mousePrevX) >= 1 ||
            abs(g_mouseY - g_mousePrevY) >= 1) {
            Mouse_HandleMovement(newButtonState, g_mouseX, g_mouseY);
        }
    }
}
