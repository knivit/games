package com.tsoft.dune2.gui.widget;

import static com.tsoft.dune2.gfx.GfxService.GFX_DrawTile;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_IsActive;
import static com.tsoft.dune2.gfx.Screen.SCREEN_0;
import static com.tsoft.dune2.gfx.Screen.SCREEN_ACTIVE;
import static com.tsoft.dune2.gui.Gui.DRAWSPRITE_FLAG_REMAP;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.gui.widget.DrawMode.*;
import static com.tsoft.dune2.gui.widget.WidgetDrawService.GUI_Widget_Scrollbar_Draw;
import static com.tsoft.dune2.house.HouseType.HOUSE_HARKONNEN;
import static com.tsoft.dune2.input.MouseService.*;
import static com.tsoft.dune2.strings.Strings.STR_CANCEL;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class WidgetService {

    public static Widget g_widgetLinkedListHead = null;
    static Widget g_widgetLinkedListTail = null;
    public static Widget g_widgetInvoiceTail = null;
    public static Widget g_widgetMentatFirst = null;
    public static Widget g_widgetMentatTail = null;
    public static Widget g_widgetMentatScrollUp = null;
    public static Widget g_widgetMentatScrollDown = null;
    public static Widget g_widgetMentatScrollbar = null;

    /** Layout and other properties of the widgets. */
    public static WidgetProperties[] g_widgetProperties = new WidgetProperties[] {
        /* x   y   w    h   p4  norm sel */
        new WidgetProperties( 0,   0, 40, 200,  (byte)15,  (byte)12,  (byte)0), /*  0 */
        new WidgetProperties( 1,  75, 29,  70,  (byte)15,  (byte)15,  (byte)0), /*  1 */
        new WidgetProperties( 0,  40, 30, 160,  (byte)15,  (byte)20,  (byte)0), /*  2 */
        new WidgetProperties(32, 136,  8,  64,  (byte)15,  (byte)12,  (byte)0), /*  3 */
        new WidgetProperties(32,  44,  8,   9,  (byte)29, (byte)116,  (byte)0), /*  4 */
        new WidgetProperties(32,   4,  8,   9,  (byte)29, (byte)116,  (byte)0), /*  5 */
        new WidgetProperties(32,  42,  8,  82,  (byte)15,  (byte)20,  (byte)0), /*  6 */
        new WidgetProperties( 1,  21, 38,  14,  (byte)12, (byte)116,  (byte)0), /*  7 */
        new WidgetProperties(16,  48, 23, 112,  (byte)15, (byte)233,  (byte)0), /*  8 */
        new WidgetProperties( 2, 176, 36,  11,  (byte)15,  (byte)20,  (byte)0), /*  9 */
        new WidgetProperties( 0,  40, 40, 160,  (byte)29,  (byte)20,  (byte)0), /* 10 */
        new WidgetProperties(16,  48, 23, 112,  (byte)29,  (byte)20,  (byte)0), /* 11 */
        new WidgetProperties( 9,  80, 22, 112,  (byte)29, (byte)116,  (byte)0), /* 12 */
        new WidgetProperties(12, 140, 16,  42, (byte)236, (byte)233,  (byte)0), /* 13 */
        new WidgetProperties( 2,  89, 36,  60,   (byte)0,   (byte)0,  (byte)0), /* 14 */
        new WidgetProperties( 4, 110, 32,  12, (byte)232, (byte)235,  (byte)0), /* 15 */
        new WidgetProperties( 5,  48, 30, 134,   (byte)0,   (byte)0,  (byte)0), /* 16 */
        new WidgetProperties( 3,  36, 36, 148,   (byte)0,   (byte)0,  (byte)0), /* 17 */
        new WidgetProperties( 1,  72, 38,  52,   (byte)0,   (byte)0,  (byte)0), /* 18 */
        new WidgetProperties( 0,   0,  0,   0,   (byte)0,   (byte)0,  (byte)0), /* 19 */
        new WidgetProperties( 2,  24, 36, 152,  (byte)12,  (byte)12,  (byte)0), /* 20 */
        new WidgetProperties( 1,   6, 12,   3,   (byte)0,  (byte)15,  (byte)6)  /* 21 */
    };

    public static int g_curWidgetIndex;           /*!< Index of the currently selected widget in #g_widgetProperties. */
    public static int g_curWidgetXBase;           /*!< Horizontal base position of the currently selected widget. */
    public static int g_curWidgetYBase;           /*!< Vertical base position of the currently selected widget. */
    public static int g_curWidgetWidth;           /*!< Width of the currently selected widget. */
    public static int g_curWidgetHeight;          /*!< Height of the currently selected widget. */
    public static byte g_curWidgetFGColourBlink;  /*!< Blinking colour of the currently selected widget. */
    public static byte g_curWidgetFGColourNormal; /*!< Normal colour of the currently selected widget. */

    static boolean s_widgetReset; /*!< If true, the widgets will be redrawn. */

    static Widget GUI_Widget_GetNext(Widget w) {
        if (w.next == null) return null;
        return w.next;
    }

    /**
     * Find an existing Widget by the index number. It matches the first hit, and
     *  returns that widget to you.
     * @param w The first widget to start searching from.
     * @param index The index of the widget you are looking for.
     * @return The widget, or null if not found.
     */
    public static Widget GUI_Widget_Get_ByIndex(Widget w, int index) {
        if (index == 0) return w;

        while (w != null) {
            if (w.index == index) return w;
            w = GUI_Widget_GetNext(w);
        }

        return null;
    }

    /**
     * Draw a chess-pattern filled rectangle over the widget.
     *
     * @param w The widget to draw.
     * @param colour The colour of the chess pattern.
     */
    static void GUI_Widget_DrawBlocked(Widget w, int colour) {
        if (GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Hide_InRegion(w.offsetX, w.offsetY, w.offsetX + w.width, w.offsetY + w.height);
        }

        GUI_DrawSprite(SCREEN_ACTIVE, w.drawParameterNormal.sprite, w.offsetX, w.offsetY, w.parentID, 0);

        GUI_DrawBlockedRectangle(w.offsetX, w.offsetY, w.width, w.height, colour);

        if (GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Show_InRegion();
        }
    }

    /**
     * Make the widget invisible.
     * @param w The widget to make invisible.
     */
    static void GUI_Widget_MakeInvisible(Widget w) {
        if (w == null || w.flags.invisible) return;
        w.flags.invisible = true;

        GUI_Widget_Draw(w);
    }

    /**
     * Make the widget visible.
     * @param w The widget to make visible.
     */
    static void GUI_Widget_MakeVisible(Widget w) {
        if (w == null || !w.flags.invisible) return;
        w.flags.invisible = false;

        GUI_Widget_Draw(w);
    }

    /**
     * Draw a widget to the display.
     *
     * @param w The widget to draw.
     */
    public static void GUI_Widget_Draw(Widget w) {
        int positionLeft, positionRight;
        int positionTop, positionBottom;
        int offsetX, offsetY;
        int drawMode;
        int fgColour, bgColour;
        WidgetDrawParameter drawParam;

        if (w == null) return;

        if (w.flags.invisible) {
            if (!w.flags.greyWhenInvisible) return;

            GUI_Widget_DrawBlocked(w, 12);
            return;
        }

        if (!w.state.hover2) {
            if (!w.state.selected) {
                drawMode  = w.drawModeNormal;
                drawParam = w.drawParameterNormal;
                fgColour  = w.fgColourNormal;
                bgColour  = w.bgColourNormal;
            } else {
                drawMode  = w.drawModeSelected;
                drawParam = w.drawParameterSelected;
                fgColour  = w.fgColourSelected;
                bgColour  = w.bgColourSelected;

            }
        } else {
            drawMode  = w.drawModeDown;
            drawParam = w.drawParameterDown;
            fgColour  = w.fgColourDown;
            bgColour  = w.bgColourDown;
        }

        offsetX = w.offsetX;
        if (w.offsetX < 0) {
            offsetX = (g_widgetProperties[w.parentID].width << 3) + w.offsetX;
        }
        positionLeft = (g_widgetProperties[w.parentID].xBase << 3) + offsetX;
        positionRight = positionLeft + w.width - 1;

        offsetY = w.offsetY;
        if (w.offsetY < 0) {
            offsetY = g_widgetProperties[w.parentID].height + w.offsetY;
        }
        positionTop = g_widgetProperties[w.parentID].yBase + offsetY;
        positionBottom = positionTop + w.height - 1;

        assert(drawMode < DRAW_MODE_MAX);
        if (drawMode != DRAW_MODE_NONE && drawMode != DRAW_MODE_CUSTOM_PROC && GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Hide_InRegion(positionLeft, positionTop, positionRight, positionBottom);
        }

        switch (drawMode) {
            case DRAW_MODE_NONE: break;

            case DRAW_MODE_SPRITE: {
                GUI_DrawSprite(SCREEN_ACTIVE, drawParam.sprite, offsetX, offsetY, w.parentID, DRAWSPRITE_FLAG_REMAP | DRAWSPRITE_FLAG_WIDGETPOS, g_remap, 1);
            } break;

            case DRAW_MODE_TEXT: {
                GUI_DrawText(drawParam.text, positionLeft, positionTop, fgColour, bgColour);
            } break;

            case DRAW_MODE_UNKNOWN3: {
                GFX_DrawTile(drawParam.spriteID, positionLeft, positionTop, HOUSE_HARKONNEN);
            } break;

            case DRAW_MODE_CUSTOM_PROC: {
                if (drawParam.proc == null) return;
                drawParam.proc.accept(w);
            } break;

            case DRAW_MODE_WIRED_RECTANGLE: {
                GUI_DrawWiredRectangle(positionLeft, positionTop, positionRight, positionBottom, fgColour);
            } break;

            case DRAW_MODE_XORFILLED_RECTANGLE: {
                GUI_DrawXorFilledRectangle(positionLeft, positionTop, positionRight, positionBottom, fgColour);
            } break;
        }

        if (drawMode != DRAW_MODE_NONE && drawMode != DRAW_MODE_CUSTOM_PROC && GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Show_InRegion();
        }
    }

    /**
     * Check a widget for events like 'hover' or 'click'. Also check the keyboard
     *  buffer if there was any key which should active us.
     *
     * @param w The widget to handle events for. If the widget has a valid next
     *   pointer, those widgets are handled too.
     * @return The last key pressed, or 0 if the key pressed was handled (or if
     *   there was no key press).
     */
    public static int GUI_Widget_HandleEvents(Widget w) {
        Widget l_widget_selected     = null;
        Widget l_widget_last         = null;
        int  l_widget_button_state = 0x0;

        int mouseX, mouseY;
        int buttonState;
        int returnValue;
        int key;
        boolean fakeClick;

        /* Get the key from the buffer, if there was any key pressed */
        key = 0;
        if (Input_IsInputAvailable() != 0) {
            key = Input_Wait();
        }

        if (w == null) return key & 0x7FFF;

        /* First time this window is being drawn? */
        if (w != l_widget_last || s_widgetReset) {
            l_widget_last         = w;
            l_widget_selected     = null;
            l_widget_button_state = 0x0;
            s_widgetReset = false;

            /* Check for left click */
            if (Input_Test(0x41) != 0) l_widget_button_state |= 0x0200;

            /* Check for right click */
            if (Input_Test(0x42) != 0) l_widget_button_state |= 0x2000;

            /* Draw all the widgets */
            for (; w != null; w = GUI_Widget_GetNext(w)) {
                GUI_Widget_Draw(w);
            }
        }

        mouseX = g_mouseX;
        mouseY = g_mouseY;

        buttonState = 0;
        if (g_mouseDisabled == 0) {
            int buttonStateChange = 0;

            /* See if the key was a mouse button action */
            if ((key & 0x8000) != 0) {
                if ((key & 0x00FF) == 0xC7) buttonStateChange = 0x1000;
                if ((key & 0x00FF) == 0xC6) buttonStateChange = 0x0100;
            } else {
                if ((key & 0x00FF) == 0x42) buttonStateChange = 0x1000;
                if ((key & 0x00FF) == 0x41) buttonStateChange = 0x0100;
            }

            /* Mouse button up */
            if ((key & 0x0800) != 0) {
                buttonStateChange <<= 2;
            }

            if (buttonStateChange != 0) {
                mouseX = g_mouseClickX;
                mouseY = g_mouseClickY;
            }

            /* Disable when release, enable when click */
            l_widget_button_state &= ~((buttonStateChange & 0x4400) >> 1);
            l_widget_button_state |=   (buttonStateChange & 0x1100) << 1;

            buttonState |= buttonStateChange;
            buttonState |= l_widget_button_state;
            buttonState |= (l_widget_button_state << 2) ^ 0x8800;
        }

        w = l_widget_last;
        if (l_widget_selected != null) {
            w = l_widget_selected;

            if (w.flags.invisible) {
                l_widget_selected = null;
            }
        }

        returnValue = 0;
        for (; w != null; w = GUI_Widget_GetNext(w)) {
            int positionX, positionY;
            boolean triggerWidgetHover;
            boolean widgetHover;
            boolean widgetClick;

            if (w.flags.invisible) continue;

            /* Store the previous button state */
            w.state.selectedLast = w.state.selected;
            w.state.hover1Last = w.state.hover1;

            positionX = w.offsetX;
            if (w.offsetX < 0) positionX += g_widgetProperties[w.parentID].width << 3;
            positionX += g_widgetProperties[w.parentID].xBase << 3;

            positionY = w.offsetY;
            if (w.offsetY < 0) positionY += g_widgetProperties[w.parentID].height;
            positionY += g_widgetProperties[w.parentID].yBase;

            widgetHover = false;
            w.state.keySelected = false;

            /* Check if the mouse is inside the widget */
            if (positionX <= mouseX && mouseX <= positionX + w.width && positionY <= mouseY && mouseY <= positionY + w.height) {
                widgetHover = true;
            }

            /* Check if there was a keypress for the widget */
            if ((key & 0x7F) != 0 && ((key & 0x7F) == w.shortcut || (key & 0x7F) == w.shortcut2)) {
                widgetHover = true;
                w.state.keySelected = true;
                key = 0;

                buttonState = 0;
                if ((key & 0x7F) == w.shortcut2) buttonState = (w.flags.buttonFilterRight) << 12;
                if (buttonState == 0) buttonState = (w.flags.buttonFilterLeft) << 8;

                l_widget_selected = w;
            }

            /* Update the hover state */
            w.state.hover1 = false;
            w.state.hover2 = false;
            if (widgetHover) {
                /* Button pressed, and click is hover */
                if ((buttonState & 0x3300) != 0 && w.flags.clickAsHover && (w == l_widget_selected || l_widget_selected == null)) {
                    w.state.hover1 = true;
                    w.state.hover2 = true;

                    /* If we don't have a selected widget yet, this will be the one */
                    if (l_widget_selected == null) {
                        l_widget_selected = w;
                    }
                }
                /* No button pressed, and click not is hover */
                if ((buttonState & 0x8800) != 0 && !w.flags.clickAsHover) {
                    w.state.hover1 = true;
                    w.state.hover2 = true;
                }
            }

            /* Check if we should trigger the hover activation */
            triggerWidgetHover = widgetHover;
            if (l_widget_selected != null && l_widget_selected.flags.loseSelect) {
                triggerWidgetHover = (l_widget_selected == w) ? true : false;
            }

            widgetClick = false;
            if (triggerWidgetHover) {
                int buttonLeftFiltered;
                int buttonRightFiltered;

                /* We click this widget for the first time */
                if ((buttonState & 0x1100) != 0 && l_widget_selected == null) {
                    l_widget_selected = w;
                    key = 0;
                }

                buttonLeftFiltered = (buttonState >> 8) & w.flags.buttonFilterLeft;
                buttonRightFiltered = (buttonState >> 12) & w.flags.buttonFilterRight;

                /* Check if we want to consider this as click */
                if ((buttonLeftFiltered != 0 || buttonRightFiltered != 0) && (widgetHover || !w.flags.requiresClick)) {

                    if (((buttonLeftFiltered & 1) != 0) || ((buttonRightFiltered & 1) != 0)) {
                        /* Widget click */
                        w.state.selected = !w.state.selected;
                        returnValue = w.index | 0x8000;
                        widgetClick = true;

                        if (w.flags.clickAsHover) {
                            w.state.hover1 = true;
                            w.state.hover2 = true;
                        }
                        l_widget_selected = w;
                    } else if (((buttonLeftFiltered & 2) != 0) || ((buttonRightFiltered & 2) != 0)) {
                        /* Widget was already clicked */
                        if (!w.flags.clickAsHover) {
                            w.state.hover1 = true;
                            w.state.hover2 = true;
                        }
                        if (!w.flags.requiresClick) widgetClick = true;
                    } else if (((buttonLeftFiltered & 4) != 0) || ((buttonRightFiltered & 4) != 0)) {
                        /* Widget release */
                        if (!w.flags.requiresClick || (w.flags.requiresClick && w == l_widget_selected)) {
                            w.state.selected = !w.state.selected;
                            returnValue = w.index | 0x8000;
                            widgetClick = true;
                        }

                        if (!w.flags.clickAsHover) {
                            w.state.hover1 = false;
                            w.state.hover2 = false;
                        }
                    } else {
                        /* Widget was already released */
                        if (w.flags.clickAsHover) {
                            w.state.hover1 = true;
                            w.state.hover2 = true;
                        }
                        if (!w.flags.requiresClick) widgetClick = true;
                    }
                }
            }

            fakeClick = false;
            /* Check if we are hovering and have mouse button down */
            if (widgetHover && (buttonState & 0x2200) != 0) {
                w.state.hover1 = true;
                w.state.hover2 = true;

                if (!w.flags.clickAsHover && !w.state.selected) {
                    fakeClick = true;
                    w.state.selected = true;
                }
            }

            /* Check if we are not pressing a button */
            if ((buttonState & 0x8800) == 0x8800) {
                l_widget_selected = null;

                if (!widgetHover || w.flags.clickAsHover) {
                    w.state.hover1 = false;
                    w.state.hover2 = false;
                }
            }

            if (!widgetHover && l_widget_selected == w && !w.flags.loseSelect) {
                l_widget_selected = null;
            }

            /* When the state changed, redraw */
            if (w.state.selected != w.state.selectedLast || w.state.hover1 != w.state.hover1Last) {
                GUI_Widget_Draw(w);
            }

            /* Reset click state when we were faking it */
            if (fakeClick) {
                w.state.selected = false;
            }

            if (widgetClick) {
                w.state.buttonState = buttonState >> 8;

                /* If Click was successful, don't handle any other widgets */
                if (w.clickProc != null && w.clickProc.apply(w)) break;

                /* On click, don't handle any other widgets */
                if (w.flags.noClickCascade) break;
            }

            /* If we are selected and we lose selection on leave, don't try other widgets */
            if (w == l_widget_selected && w.flags.loseSelect) break;
        }

        if (returnValue != 0) return returnValue;
        return key & 0x7FFF;
    }

    /**
     * Get shortcut key for the given char.
     *
     * @param c The (ASCII) char to get the shortcut for.
     * @return The shortcut key. (Dune II key code)
     */
    public static int GUI_Widget_GetShortcut(int c) {
        /* This is for a US AT keyboard layout */
        int[] shortcuts = new int[] {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /*  0 -  7 */
            0x0f, 0x10, 0x00, 0x00, 0x00, 0x2b, 0x00, 0x00, /*  8 - 15 : Backspace, Tab, return */
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /* 16 - 23 */
            0x00, 0x00, 0x00, 0x6e, 0x00, 0x00, 0x00, 0x00, /* 24 - 31 : ESCAPE */
            0x3d, 0x02, 0x29, 0x04, 0x05, 0x06, 0x08, 0x29, /* 32 - 39 : SPACE !1 '" #3 $4 %5 &7 '" */
            0x0a, 0x0b, 0x64, 0x6a, 0x35, 0x0c, 0x36, 0x5f, /* 40 - 47 : (9 )0 KP* KP+ ,< -_ . / */
            0x0b, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, /* 48 - 55 : 0 1 2 3 4 5 6 7 */
            0x09, 0x0a, 0x28, 0x28, 0x35, 0x0d, 0x36, 0x41, /* 56 - 63 : 8 9 :; :; <, =+ >. ?/ */
            0x03, 0x1f, 0x32, 0x30, 0x21, 0x13, 0x22, 0x23, /* 64 - 71 : @2 A B C D E F G */
            0x24, 0x18, 0x25, 0x26, 0x27, 0x34, 0x33, 0x19, /* 72 - 79 : H I J K L M N O */
            0x1a, 0x11, 0x14, 0x20, 0x15, 0x17, 0x31, 0x12, /* 80 - 87 : P Q R S T U V W */
            0x2f, 0x16, 0x2e, 0x1b, 0x1d, 0x1c, 0x07, 0x0c, /* 88 - 95 : X Y Z [ \ ] ^6 _- */
            0x01, 0x1f, 0x32, 0x30, 0x21, 0x13, 0x22, 0x23, /* 96 -103 : ` a b c d e f g */
            0x24, 0x18, 0x25, 0x26, 0x27, 0x34, 0x33, 0x19, /*104 -111 : h i j k l m n o */
            0x1a, 0x11, 0x14, 0x20, 0x15, 0x17, 0x31, 0x12, /*112 -119 : p q r s t u v w */
            0x2f, 0x16, 0x2e, 0x1b, 0x1d, 0x1c, 0x01, 0x00, /*120 -127 : x y z { | } ~ */
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /*128 -135 */
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x79, /*136 -143 : F10 */
            0x78, 0x77, 0x76, 0x75, 0x74, 0x73, 0x72, 0x71, /*144 -151 : F9 F8 F7 F6 F5 F4 F3 F2 */
            0x70, 0x79, 0x78, 0x77, 0x76, 0x75, 0x74, 0x73, /*152 -159 : F1 F10 F9 F8 F7 F6 F5 F4 */
            0x72, 0x71, 0x70, 0x79, 0x78, 0x77, 0x76, 0x75, /*160 -167 : F3 F2 F1 F10 F9 F8 F7 F6 */
            0x74, 0x73, 0x72, 0x71, 0x70, 0x4c, 0x4b, 0x56, /*168 -175 : F5 F4 F3 F2 F1 DELETE INSERT RIGHT */
            0x54, 0x51, 0x00, 0x59, 0x61, 0x4f, 0x00, 0x55, /*176 -183 : DOWN END  KP/ KP5 END  PGUP */
            0x53, 0x50, 0x00, 0x00, 0x79, 0x78, 0x77, 0x76, /*184 -191 : UP HOME   F10 F9 F8 F7 */
            0x75, 0x74, 0x73, 0x72, 0x71, 0x70, 0x41, 0x42, /*192 -199 : F6 F5 F4 F3 F2 F1 LEFT_MOUSEB RIGHT_MOUSEB */
            0x43, 0x44, 0x45, 0x46, 0x47, 0x48              /*200 -205 : ??? ??? ??? ??? ??? ??? */
        };

        if (c < shortcuts.length) return shortcuts[c];
        else return 0;
    }

    /**
     * Allocates a widget.
     *
     * @param index The index for the allocated widget.
     * @param shortcut The shortcut for the allocated widget.
     * @param offsetX The x position for the allocated widget.
     * @param offsetY The y position for the allocated widget.
     * @param spriteID The sprite to draw on the allocated widget (0xFFFF for none).
     * @param stringID The string to print on the allocated widget.
     * @return The allocated widget.
     */
    public static Widget GUI_Widget_Allocate(int index, int shortcut, int offsetX, int offsetY, int spriteID, int stringID) {
        Widget w = new Widget();
        int  drawMode;
        WidgetDrawParameter drawParam1;
        WidgetDrawParameter drawParam2;

        w.index            = index;
        w.shortcut         = shortcut;
        w.shortcut2        = shortcut;
        w.parentID         = 0;
        w.fgColourSelected = 0xB;
        w.bgColourSelected = 0xC;
        w.fgColourNormal   = 0xF;
        w.bgColourNormal   = 0xC;
        w.stringID         = stringID;
        w.offsetX          = offsetX;
        w.offsetY          = offsetY;

        w.flags.requiresClick = true;
        w.flags.clickAsHover = true;
        w.flags.loseSelect = true;
        w.flags.buttonFilterLeft = 4;
        w.flags.buttonFilterRight = 4;

        switch ((int)spriteID + 4) {
            case 0:
                drawMode        = DRAW_MODE_CUSTOM_PROC;
                drawParam1.proc = GUI_Widget_SpriteButton_Draw;
                drawParam2.proc = GUI_Widget_SpriteButton_Draw;
                break;

            case 1:
                drawMode        = DRAW_MODE_CUSTOM_PROC;
                drawParam1.proc = GUI_Widget_SpriteTextButton_Draw;
                drawParam2.proc = GUI_Widget_SpriteTextButton_Draw;

                if (stringID == STR_NULL) break;

                if (String_Get_ByIndex(stringID) != null) w.shortcut = GUI_Widget_GetShortcut(*String_Get_ByIndex(stringID));
                if (stringID == STR_CANCEL) w.shortcut2 = 'n';
                break;

            case 2:
                drawMode        = DRAW_MODE_CUSTOM_PROC;
                drawParam1.proc = GUI_Widget_TextButton2_Draw;
                drawParam2.proc = GUI_Widget_TextButton2_Draw;
                break;

            case 3:
                drawMode            = DRAW_MODE_NONE;
                drawParam1.spriteID = 0;
                drawParam2.spriteID = 0;
                break;

            default:
                drawMode = DRAW_MODE_SPRITE;
                drawParam1.sprite = g_sprites[spriteID];
                drawParam2.sprite = g_sprites[spriteID + 1];

                if (drawParam1.sprite == null) break;

                w.width  = Sprite_GetWidth(drawParam1.sprite);
                w.height = Sprite_GetHeight(drawParam1.sprite);
                break;
        }

        w.drawModeSelected = drawMode;
        w.drawModeDown     = drawMode;
        w.drawModeNormal   = drawMode;
        w.drawParameterNormal   = drawParam1;
        w.drawParameterDown     = drawParam2;
        w.drawParameterSelected = (spriteID == 0x19) ? drawParam2 : drawParam1;

        return w;
    }

    static int GUI_Widget_Scrollbar_CalculateSize(WidgetScrollbar scrollbar) {
        Widget w;
        int size;

        w = scrollbar.parent;

        if (w == null) return 0;

        size = scrollbar.scrollPageSize * (max(w.width, w.height) - 2) / scrollbar.scrollMax;

        if (scrollbar.size != size) {
            scrollbar.size = size;
            scrollbar.dirty = 1;
        }

        return size;
    }

    /**
     * Allocate a #Widget and a #WidgetScrollbar.
     * @param index Index of the new widget.
     * @param parentID Parent ID of the new widget.
     * @param offsetX Horizontal offset of the new widget.
     * @param offsetY Vertical offset of the new widget.
     * @param width Width of the new widget.
     * @param height Height of the new widget.
     * @param drawProc Procedure for drawing.
     * @return Address of the new widget.
     */
    public static Widget GUI_Widget_Allocate_WithScrollbar(int index, int parentID, int offsetX, int offsetY, int width, int height, ScrollbarDrawProc drawProc) {
        Widget w = new Widget();
        WidgetScrollbar ws = new WidgetScrollbar();

        w.index    = index;
        w.parentID = parentID;
        w.offsetX  = offsetX;
        w.offsetY  = offsetY;
        w.width    = width;
        w.height   = height;

        w.fgColourSelected = 10;
        w.bgColourSelected = 12;

        w.fgColourNormal = 15;
        w.bgColourNormal = 12;

        w.flags.buttonFilterLeft = 7;
        w.flags.loseSelect = true;

        w.state.hover2Last = true;

        w.drawModeNormal   = DRAW_MODE_CUSTOM_PROC;
        w.drawModeSelected = DRAW_MODE_CUSTOM_PROC;

        w.drawParameterNormal.proc   = GUI_Widget_Scrollbar_Draw;
        w.drawParameterSelected.proc = GUI_Widget_Scrollbar_Draw;
        w.clickProc                  = GUI_Widget_Scrollbar_Click;

        w.data = ws;

        ws.parent = w;

        ws.scrollMax      = 1;
        ws.scrollPageSize = 1;
        ws.scrollPosition = 0;
        ws.pressed        = 0;
        ws.dirty          = 0;

        ws.drawProc = drawProc;

        GUI_Widget_Scrollbar_CalculateSize(ws);
        GUI_Widget_Scrollbar_CalculatePosition(ws);

        return w;
    }

    /**
     * Allocate a scroll button for the Mentat screen scroll bar.
     * @return Allocated widget.
     */
    public static Widget GUI_Widget_AllocateScrollBtn(int index, int parentID, int offsetX, int offsetY, Object sprite1, Object sprite2, Widget widget2, boolean isDown) {
        Widget w = new Widget();

        w.index    = index;
        w.parentID = parentID;
        w.offsetX  = offsetX;
        w.offsetY  = offsetY;

        w.drawModeNormal   = DRAW_MODE_SPRITE;
        w.drawModeDown     = DRAW_MODE_SPRITE;
        w.drawModeSelected = DRAW_MODE_SPRITE;

        w.width  = Sprite_GetWidth(sprite1) * 8;
        w.height = Sprite_GetHeight(sprite1);

        w.flags.requiresClick     = true;
        w.flags.clickAsHover      = true;
        w.flags.loseSelect        = true;
        w.flags.buttonFilterLeft  = 1;
        w.flags.buttonFilterRight = 1;

        w.drawParameterNormal.sprite   = sprite1;
        w.drawParameterSelected.sprite = sprite1;
        w.drawParameterDown.sprite     = sprite2;

        if (isDown) {
            w.clickProc = GUI_Widget_Scrollbar_ArrowDown_Click;
        } else {
            w.clickProc = GUI_Widget_Scrollbar_ArrowUp_Click;
        }

        w.data = widget2.data;
        return w;
    }

    /**
     * Make the Widget selected.
     *
     * @param w The widget to make selected.
     * @param clickProc Wether to execute the widget clickProc.
     */
    public static void GUI_Widget_MakeSelected(Widget w, boolean clickProc) {
        if (w == null || w.flags.invisible) return;

        w.state.selectedLast = w.state.selected;

        w.state.selected = true;

        GUI_Widget_Draw(w);

        if (!clickProc || w.clickProc == null) return;

        w.clickProc(w);
    }

    /**
     * Reset the Widget to a normal state (not selected, not clicked).
     *
     * @param w The widget to reset.
     * @param clickProc Wether to execute the widget clickProc.
     */
    public static void GUI_Widget_MakeNormal(Widget w, boolean clickProc) {
        if (w == null || w.flags.invisible) return;

        w.state.selectedLast = w.state.selected;
        w.state.hover1Last = w.state.hover2;

        w.state.selected = false;
        w.state.hover1 = false;
        w.state.hover2 = false;;

        GUI_Widget_Draw(w);

        if (!clickProc || w.clickProc == null) return;

        w.clickProc.accept(w);
    }

    /**
     * Link a widget to another widget, where the new widget is linked at the end
     *  of the list of the first widget.
     * @param w1 Widget to which the other widget is added.
     * @param w2 Widget which is added to the first widget (at the end of his chain).
     * @return The first widget of the chain.
     */
    public static Widget GUI_Widget_Link(Widget w1, Widget w2) {
        Widget first = w1;

        s_widgetReset = true;

        if (w2 == null) return w1;
        w2.next = null;
        if (w1 == null) return w2;

        while (w1.next != null) w1 = w1.next;

        w1.next = w2;
        return first;
    }

    /**
     * Get scrollbar position.
     * @param w Widget.
     * @return Scrollbar position, or \c 0xFFFF if no widget supplied.
     */
    static int GUI_Get_Scrollbar_Position(Widget w) {
        WidgetScrollbar ws;

        if (w == null) return 0xFFFF;

        ws = (WidgetScrollbar)w.data;
        return ws.scrollPosition;
    }

    static int GUI_Widget_Scrollbar_Init(Widget w, int scrollMax, int scrollPageSize, int scrollPosition) {
        int position;
        WidgetScrollbar scrollbar;

        if (w == null) return 0xFFFF;

        position = GUI_Get_Scrollbar_Position(w);
        scrollbar = (WidgetScrollbar)w.data;

        if (scrollMax > 0) scrollbar.scrollMax = scrollMax;
        if (scrollPageSize >= 0) scrollbar.scrollPageSize = min(scrollPageSize, scrollbar.scrollMax);
        if (scrollPosition >= 0) scrollbar.scrollPosition = min(scrollPosition, scrollbar.scrollMax - scrollbar.scrollPageSize);

        GUI_Widget_Scrollbar_CalculateSize(scrollbar);
        GUI_Widget_Scrollbar_CalculatePosition(scrollbar);
        GUI_Widget_Scrollbar_Draw(w);

        if (scrollbar.drawProc != null) scrollbar.drawProc.accept(w);

        return position;
    }

    static int GUI_Widget_Scrollbar_CalculatePosition(WidgetScrollbar scrollbar) {
        Widget w;
        int position;

        w = scrollbar.parent;
        if (w == null) return 0xFFFF;

        position = scrollbar.scrollMax - scrollbar.scrollPageSize;

        if (position != 0) position = scrollbar.scrollPosition * (max(w.width, w.height) - 2 - scrollbar.size) / position;

        if (scrollbar.position != position) {
            scrollbar.position = position;
            scrollbar.dirty = 1;
        }

        return position;
    }

    static int GUI_Widget_Scrollbar_CalculateScrollPosition(WidgetScrollbar scrollbar) {
        Widget w;

        w = scrollbar.parent;
        if (w == null) return 0xFFFF;

        scrollbar.scrollPosition = scrollbar.position * (scrollbar.scrollMax - scrollbar.scrollPageSize) / (max(w.width, w.height) - 2 - scrollbar.size);

        return scrollbar.scrollPosition;
    }

    public static void GUI_Widget_Free_WithScrollbar(Widget w) {
        if (w == null) return;

        free(w.data);
        free(w);
    }

    /**
     * Insert a widget into a list of widgets.
     * @param w1 Widget to which the other widget is added.
     * @param w2 Widget which is added to the first widget (ordered by index).
     * @return The first widget of the chain.
     */
    static Widget GUI_Widget_Insert(Widget w1, Widget w2) {
        Widget first;
        Widget prev;

        if (w1 == null) return w2;
        if (w2 == null) return w1;

        if (w2.index <= w1.index) {
            w2.next = w1;
            return w2;
        }

        first = w1;
        prev = w1;

        while (w2.index > w1.index && w1.next != null) {
            prev = w1;
            w1 = w1.next;
        }

        if (w2.index > w1.index) {
            w1 = GUI_Widget_Link(first, w2);
        } else {
            prev.next = w2;
            w2.next = w1;
        }

        s_widgetReset = true;

        return first;
    }

    /**
     * Select a widget as current widget.
     * @param index %Widget number to select.
     * @return Index of the previous selected widget.
     */
    public static int Widget_SetCurrentWidget(int index) {
        int oldIndex = g_curWidgetIndex;
        g_curWidgetIndex = index;

        g_curWidgetXBase          = g_widgetProperties[index].xBase;
        g_curWidgetYBase          = g_widgetProperties[index].yBase;
        g_curWidgetWidth          = g_widgetProperties[index].width;
        g_curWidgetHeight         = g_widgetProperties[index].height;
        g_curWidgetFGColourBlink  = g_widgetProperties[index].fgColourBlink;
        g_curWidgetFGColourNormal = g_widgetProperties[index].fgColourNormal;

        return oldIndex;
    }

    /**
     * Select a widget as current widget and draw its exterior.
     * @param index %Widget number to select.
     * @return Index of the previous selected widget.
     */
    public static int Widget_SetAndPaintCurrentWidget(int index) {
        index = Widget_SetCurrentWidget(index);

        Widget_PaintCurrentWidget();

        return index;
    }

    /**
     * Draw the exterior of the currently selected widget.
     */
    public static void Widget_PaintCurrentWidget() {
        GUI_DrawFilledRectangle(g_curWidgetXBase << 3, g_curWidgetYBase, ((g_curWidgetXBase + g_curWidgetWidth) << 3) - 1, g_curWidgetYBase + g_curWidgetHeight - 1, g_curWidgetFGColourNormal);
    }
}
