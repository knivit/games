package com.tsoft.dune2.gui.widget;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.gobject.GObjectInfo;
import com.tsoft.dune2.gui.FactoryWindowItem;
import com.tsoft.dune2.house.House;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.unit.ActionInfo;
import com.tsoft.dune2.unit.Unit;
import com.tsoft.dune2.unit.UnitInfo;

import static com.tsoft.dune2.gfx.Screen.SCREEN_0;
import static com.tsoft.dune2.gfx.Screen.SCREEN_1;
import static com.tsoft.dune2.gui.FactoryResult.FACTORY_BUY;
import static com.tsoft.dune2.gui.FactoryResult.FACTORY_RESUME;
import static com.tsoft.dune2.gui.GuiService.GUI_DrawFilledRectangle;
import static com.tsoft.dune2.gui.SelectionType.SELECTIONTYPE_PLACE;
import static com.tsoft.dune2.gui.SelectionType.SELECTIONTYPE_STRUCTURE;
import static com.tsoft.dune2.gui.widget.WidgetDrawService.GUI_Widget_Scrollbar_Draw;
import static com.tsoft.dune2.gui.widget.WidgetService.GUI_Widget_Scrollbar_CalculateScrollPosition;
import static com.tsoft.dune2.gui.widget.WidgetService.g_widgetProperties;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;
import static com.tsoft.dune2.input.InputService.Input_Test;
import static com.tsoft.dune2.input.MouseService.g_mouseX;
import static com.tsoft.dune2.input.MouseService.g_mouseY;
import static com.tsoft.dune2.opendune.OpenDuneService.g_dune2_enhanced;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Get_ByIndex;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureType.STRUCTURE_CONSTRUCTION_YARD;
import static com.tsoft.dune2.structure.StructureType.STRUCTURE_INVALID;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.MovementType.*;
import static com.tsoft.dune2.unit.UnitService.Unit_GetHouseID;
import static com.tsoft.dune2.unit.UnitService.Unit_SetAction;
import static com.tsoft.dune2.unit.UnitType.UNIT_HARVESTER;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class WidgetClickService {
    
    char g_savegameDesc[5][51];                                 /*!< Array of savegame descriptions for the SaveLoad window. */
    static int s_savegameIndexBase = 0;
    static int s_savegameCountOnDisk = 0;                    /*!< Amount of savegames on disk. */

    static String GenerateSavegameFilename(int number) {
        if (number >= 1000) {
            Warning("Savegame #%hu not supported\n", number);
            return null;
        }
        return String.format("_save%03hu.dat", number);
    }

    /**
     * Handles scrolling of a scrollbar.
     *
     * @param scrollbar The scrollbar.
     * @param scroll The amount of scrolling.
     */
    static void GUI_Widget_Scrollbar_Scroll(WidgetScrollbar scrollbar, int scroll) {
        scrollbar.scrollPosition += scroll;

        if ((int)scrollbar.scrollPosition >= scrollbar.scrollMax - scrollbar.scrollPageSize) {
            scrollbar.scrollPosition = scrollbar.scrollMax - scrollbar.scrollPageSize;
        }

        if ((int)scrollbar.scrollPosition <= 0) scrollbar.scrollPosition = 0;

        GUI_Widget_Scrollbar_CalculatePosition(scrollbar);

        GUI_Widget_Scrollbar_Draw(scrollbar.parent);
    }

    /**
     * Handles Click event for a sprite/text button.
     *
     * @param w The widget.
     * @return False, always.
     */
    boolean GUI_Widget_SpriteTextButton_Click(Widget w) {
        Structure s;

        VARIABLE_NOT_USED(w);

        s = Structure_Get_ByPackedTile(g_selectionPosition);

        switch (g_productionStringID) {
            default: break;

            case STR_PLACE_IT:
                if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
                    Structure ns;

                    ns = Structure_Get_ByIndex(s.o.linkedID);
                    g_structureActive = ns;
                    g_structureActiveType = s.objectType;
                    g_selectionState = Structure_IsValidBuildLocation(g_selectionRectanglePosition, g_structureActiveType);
                    g_structureActivePosition = g_selectionPosition;
                    s.o.linkedID = STRUCTURE_INVALID;

                    GUI_ChangeSelectionType(SELECTIONTYPE_PLACE);
                }
                break;

            case STR_ON_HOLD:
                s.o.flags.repairing = false;
                s.o.flags.onHold    = false;
                s.o.flags.upgrading = false;
                break;

            case STR_BUILD_IT:
                Structure_BuildObject(s, s.objectType);
                break;

            case STR_LAUNCH:
            case STR_FREMEN:
            case STR_SABOTEUR:
                Structure_ActivateSpecial(s);
                break;

            case STR_D_DONE:
                s.o.flags.onHold = true;
                break;
        }
        return false;
    }

    /**
     * Handles Click event for scrollbar up button.
     *
     * @param w The widget.
     * @return False, always.
     */
    public static boolean GUI_Widget_Scrollbar_ArrowUp_Click(Widget w) {
        GUI_Widget_Scrollbar_Scroll(w.data, -1);

        return false;
    }

    /**
     * Handles Click event for scrollbar down button.
     *
     * @param w The widget.
     * @return False, always.
     */
    public static boolean GUI_Widget_Scrollbar_ArrowDown_Click(Widget w) {
        GUI_Widget_Scrollbar_Scroll(w.data, 1);

        return false;
    }

    /**
     * Handles Click event for scrollbar button.
     *
     * @param w The widget.
     * @return False, always.
     */
    static boolean GUI_Widget_Scrollbar_Click(Widget w) {
        WidgetScrollbar scrollbar;
        int positionX, positionY;

        scrollbar = w.data;

        positionX = w.offsetX;
        if (w.offsetX < 0) positionX += g_widgetProperties[w.parentID].width << 3;
        positionX += g_widgetProperties[w.parentID].xBase << 3;

        positionY = w.offsetY;
        if (w.offsetY < 0) positionY += g_widgetProperties[w.parentID].height;
        positionY += g_widgetProperties[w.parentID].yBase;

        if ((w.state.buttonState & 0x44) != 0) {
            scrollbar.pressed = 0;
            GUI_Widget_Scrollbar_Draw(w);
        }

        if ((w.state.buttonState & 0x11) != 0) {
            int positionCurrent;
            int positionBegin;
            int positionEnd;

            scrollbar.pressed = 0;

            if (w.width > w.height) {
                positionCurrent = g_mouseX;
                positionBegin = positionX + scrollbar.position + 1;
            } else {
                positionCurrent = g_mouseY;
                positionBegin = positionY + scrollbar.position + 1;
            }

            positionEnd = positionBegin + scrollbar.size;

            if (positionCurrent <= positionEnd && positionCurrent >= positionBegin) {
                scrollbar.pressed = 1;
                scrollbar.pressedPosition = positionCurrent - positionBegin;
            } else {
                GUI_Widget_Scrollbar_Scroll(scrollbar, (positionCurrent < positionBegin ? -scrollbar.scrollPageSize : scrollbar.scrollPageSize));
            }
        }

        if ((w.state.buttonState & 0x22) != 0 && scrollbar.pressed != 0) {
            int position, size;

            if (w.width > w.height) {
                size = w.width - 2 - scrollbar.size;
                position = g_mouseX - scrollbar.pressedPosition - positionX - 1;
            } else {
                size = w.height - 2 - scrollbar.size;
                position = g_mouseY - scrollbar.pressedPosition - positionY - 1;
            }

            if (position < 0) {
                position = 0;
            } else if (position > size) {
                position = size;
            }

            if (scrollbar.position != position) {
                scrollbar.position = position;
                scrollbar.dirty = 1;
            }

            GUI_Widget_Scrollbar_CalculateScrollPosition(scrollbar);

            if (scrollbar.dirty != 0) GUI_Widget_Scrollbar_Draw(w);
        }

        return false;
    }

    /**
     * Handles Click event for unit commands button.
     *
     * @param w The widget.
     * @return True, always.
     */
    static boolean GUI_Widget_TextButton_Click(Widget w) {
	    UnitInfo ui;
	    ActionInfo ai;
	    int *actions;
        int action;
        Unit u;
        int *found;
        int unitAction;

        u = g_unitSelected;
        ui = g_table_unitInfo[u.o.type];

        actions = ui.o.actionsPlayer;
        if (Unit_GetHouseID(u) != g_playerHouseID && u.o.type != UNIT_HARVESTER) {
            actions = g_table_actionsAI;
        }

        action = actions[w.index - 8];
        if (g_dune2_enhanced) {
            if (Input_Test(0x2c) || Input_Test(0x39)) {	/* LSHIFT or RSHIFT is pressed */
                if (action == ACTION_GUARD) action = ACTION_AREA_GUARD;   /* AREA GUARD instead of GUARD */
                else if (action == ACTION_ATTACK) action = ACTION_AMBUSH; /* AMBUSH instead of ATTACK */
            }
            Debug("GUI_Widget_TextButton_Click(%p index=%d) action=%d\n", w, w.index, action);
        }

        unitAction = u.nextActionID;
        if (unitAction == ACTION_INVALID) {
            unitAction = u.actionID;
        }

        if (u.deviated != 0) {
            Unit_Deviation_Decrease(u, 5);
            if (u.deviated == 0) {
                GUI_Widget_MakeNormal(w, false);
                return true;
            }
        }

        GUI_Widget_MakeSelected(w, false);

        ai = g_table_actionInfo[action];

        if (ai.selectionType != g_selectionType) {
            g_unitActive = g_unitSelected;
            g_activeAction = action;
            GUI_ChangeSelectionType(ai.selectionType);

            return true;
        }

        Object_Script_Variable4_Clear(u.o);
        u.targetAttack = 0;
        u.targetMove = 0;
        u.route[0] = 0xFF;

        Unit_SetAction(u, action);

        if (ui.movementType == MOVEMENT_FOOT) Sound_StartSound(ai.soundID);

        if (unitAction == action) return true;

        found = memchr(actions, unitAction, 4);
        if (found == null) return true;

        GUI_Widget_MakeNormal(GUI_Widget_Get_ByIndex(g_widgetLinkedListHead, (int)(found - actions + 8)), false);

        return true;
    }

    /**
     * Handles Click event for current selection name.
     *
     * @return False, always.
     */
    boolean GUI_Widget_Name_Click(Widget w) {
        GObject o;
        int packed;

        VARIABLE_NOT_USED(w);

        o = Object_GetByPackedTile(g_selectionPosition);

        if (o == null) return false;

        packed = Tile_PackTile(o.position);

        Map_SetViewportPosition(packed);
        Map_SetSelection(packed);

        return false;
    }

    /**
     * Handles Click event for "Cancel" button.
     *
     * @return True, always.
     */
    boolean GUI_Widget_Cancel_Click(Widget w) {
        VARIABLE_NOT_USED(w);

        if (g_structureActiveType != 0xFFFF) {
            Structure s  = Structure_Get_ByPackedTile(g_structureActivePosition);
            Structure s2 = g_structureActive;

            assert(s2 != null);

            if (s != null) {
                s.o.linkedID = s2.o.index & 0xFF;
            } else {
                Structure_Free(s2);
            }

            g_structureActive = null;
            g_structureActiveType = 0xFFFF;

            GUI_ChangeSelectionType(SELECTIONTYPE_STRUCTURE);

            g_selectionState = 0; /* Invalid. */
        }

        if (g_unitActive == null) return true;

        g_unitActive = null;
        g_activeAction = 0xFFFF;
        g_cursorSpriteID = 0;

        Sprites_SetMouseSprite(0, 0, g_sprites[0]);

        GUI_ChangeSelectionType(SELECTIONTYPE_UNIT);

        return true;
    }

    /**
     * Handles Click event for current selection picture.
     *
     * @return False, always.
     */
    boolean GUI_Widget_Picture_Click(Widget w) {
        Structure s;

        VARIABLE_NOT_USED(w);

        if (g_unitSelected != null) {
            Unit_DisplayStatusText(g_unitSelected);

            return false;
        }

        s = Structure_Get_ByPackedTile(g_selectionPosition);

        if (s == null || !g_table_structureInfo[s.o.type].o.flags.factory) return false;

        Structure_BuildObject(s, 0xFFFF);

        return false;
    }

    /**
     * Handles Click event for "Repair/Upgrade" button.
     *
     * @param w The widget.
     * @return False, always.
     */
    boolean GUI_Widget_RepairUpgrade_Click(Widget w) {
        Structure s;

        s = Structure_Get_ByPackedTile(g_selectionPosition);

        if (Structure_SetRepairingState(s, -1, w)) return false;
        Structure_SetUpgradingState(s, -1, w);

        return false;
    }

    static void GUI_Widget_Undraw(Widget w, int colour) {
        int offsetX;
        int offsetY;
        int width;
        int height;

        if (w == null) return;

        offsetX = w.offsetX + (g_widgetProperties[w.parentID].xBase << 3);
        offsetY = w.offsetY + g_widgetProperties[w.parentID].yBase;
        width = w.width;
        height = w.height;

        if (GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Hide_InRegion(offsetX, offsetY, offsetX + width, offsetY + height);
        }

        GUI_DrawFilledRectangle(offsetX, offsetY, offsetX + width, offsetY + height, colour);

        if (GFX_Screen_IsActive(SCREEN_0)) {
            GUI_Mouse_Show_InRegion();
        }
    }

    static void GUI_Window_Create(WindowDesc desc) {
        int i;

        if (desc == null) return;

        g_widgetLinkedListTail = null;

        GFX_Screen_SetActive(SCREEN_1);

        Widget_SetCurrentWidget(desc.index);

        GUI_Widget_DrawBorder(g_curWidgetIndex, 2, true);

        if (GUI_String_Get_ByIndex(desc.stringID) != null) {
            GUI_DrawText_Wrapper(GUI_String_Get_ByIndex(desc.stringID), (g_curWidgetXBase << 3) + (g_curWidgetWidth << 2), g_curWidgetYBase + 6 + ((desc == &g_yesNoWindowDesc) ? 2 : 0), 238, 0, 0x122);
        }

        if (GUI_String_Get_ByIndex(desc.widgets[0].stringID) == null) {
            GUI_DrawText_Wrapper(String_Get_ByIndex(STR_THERE_ARE_NO_SAVED_GAMES_TO_LOAD), (g_curWidgetXBase + 2) << 3, g_curWidgetYBase + 42, 232, 0, 0x22);
        }

        for (i = 0; i < desc.widgetCount; i++) {
            Widget w = g_table_windowWidgets[i];

            if (GUI_String_Get_ByIndex(desc.widgets[i].stringID) == null) continue;

            w.next      = null;
            w.offsetX   = desc.widgets[i].offsetX;
            w.offsetY   = desc.widgets[i].offsetY;
            w.width     = desc.widgets[i].width;
            w.height    = desc.widgets[i].height;
            w.shortcut  = 0;
            w.shortcut2 = 0;

            if (desc != g_savegameNameWindowDesc) {
                if (desc.widgets[i].labelStringId != STR_null) {
                    w.shortcut = GUI_Widget_GetShortcut(*GUI_String_Get_ByIndex(desc.widgets[i].labelStringId));
                } else {
                    w.shortcut = GUI_Widget_GetShortcut(*GUI_String_Get_ByIndex(desc.widgets[i].stringID));
                }
            }

            w.shortcut2 = desc.widgets[i].shortcut2;
            if (w.shortcut == 0x1B) {
                w.shortcut2 = 0x13;
            }

            w.stringID = desc.widgets[i].stringID;
            w.drawModeNormal   = DRAW_MODE_CUSTOM_PROC;
            w.drawModeSelected = DRAW_MODE_CUSTOM_PROC;
            w.drawModeDown     = DRAW_MODE_CUSTOM_PROC;
            w.drawParameterNormal.proc   = GUI_Widget_TextButton_Draw;
            w.drawParameterSelected.proc = GUI_Widget_TextButton_Draw;
            w.drawParameterDown.proc     = GUI_Widget_TextButton_Draw;
            w.parentID = desc.index;
            memset(&w.state, 0, sizeof(w.state));

            g_widgetLinkedListTail = GUI_Widget_Link(g_widgetLinkedListTail, w);

            GUI_Widget_MakeVisible(w);
            GUI_Widget_MakeNormal(w, false);
            GUI_Widget_Draw(w);

            if (desc.widgets[i].labelStringId == STR_null) continue;

            if (g_config.language == LANGUAGE_FRENCH) {
                GUI_DrawText_Wrapper(GUI_String_Get_ByIndex(desc.widgets[i].labelStringId), (g_widgetProperties[w.parentID].xBase << 3) + 40, w.offsetY + g_widgetProperties[w.parentID].yBase + 3, 232, 0, 0x22);
            } else {
                GUI_DrawText_Wrapper(GUI_String_Get_ByIndex(desc.widgets[i].labelStringId), w.offsetX + (g_widgetProperties[w.parentID].xBase << 3) - 10, w.offsetY + g_widgetProperties[w.parentID].yBase + 3, 232, 0, 0x222);
            }
        }

        if (s_savegameCountOnDisk >= 5 && desc.addArrows) {
            Widget w = g_table_windowWidgets[7];

            w.drawParameterNormal.sprite   = g_sprites[59];
            w.drawParameterSelected.sprite = g_sprites[60];
            w.drawParameterDown.sprite     = g_sprites[60];
            w.next             = null;
            w.parentID         = desc.index;

            GUI_Widget_MakeNormal(w, false);
            GUI_Widget_MakeInvisible(w);
            GUI_Widget_Undraw(w, 233);

            g_widgetLinkedListTail = GUI_Widget_Link(g_widgetLinkedListTail, w);

            w = g_table_windowWidgets[8];

            w.drawParameterNormal.sprite   = g_sprites[61];
            w.drawParameterSelected.sprite = g_sprites[62];
            w.drawParameterDown.sprite     = g_sprites[62];
            w.next             = null;
            w.parentID         = desc.index;

            GUI_Widget_MakeNormal(w, false);
            GUI_Widget_MakeInvisible(w);
            GUI_Widget_Undraw(w, 233);

            g_widgetLinkedListTail = GUI_Widget_Link(g_widgetLinkedListTail, w);
        }

        GUI_Mouse_Hide_Safe();

        Widget_SetCurrentWidget(desc.index);

        GUI_Screen_Copy(g_curWidgetXBase, g_curWidgetYBase, g_curWidgetXBase, g_curWidgetYBase, g_curWidgetWidth, g_curWidgetHeight, SCREEN_1, SCREEN_0);

        GUI_Mouse_Show_Safe();

        GFX_Screen_SetActive(SCREEN_0);
    }

    static void GUI_Window_BackupScreen(WindowDesc desc) {
        Widget_SetCurrentWidget(desc.index);

        GUI_Mouse_Hide_Safe();
        GFX_CopyToBuffer(g_curWidgetXBase * 8, g_curWidgetYBase, g_curWidgetWidth * 8, g_curWidgetHeight, GFX_Screen_Get_ByIndex(SCREEN_2));
        GUI_Mouse_Show_Safe();
    }

    static void GUI_Window_RestoreScreen(WindowDesc desc) {
        Widget_SetCurrentWidget(desc.index);

        GUI_Mouse_Hide_Safe();
        GFX_CopyFromBuffer(g_curWidgetXBase * 8, g_curWidgetYBase, g_curWidgetWidth * 8, g_curWidgetHeight, GFX_Screen_Get_ByIndex(SCREEN_2));
        GUI_Mouse_Show_Safe();
    }

    /**
     * Handles Click event for "Game controls" button.
     *
     * @param w The widget.
     */
    static void GUI_Widget_GameControls_Click(Widget w) {
        WindowDesc desc = g_gameControlWindowDesc;
        boolean loop;

        GUI_Window_BackupScreen(desc);

        GUI_Window_Create(desc);

        for (loop = true; loop; sleepIdle()) {
            Widget w2 = g_widgetLinkedListTail;
            int key = GUI_Widget_HandleEvents(w2);

            if ((key & 0x8000) != 0) {
                w = GUI_Widget_Get_ByIndex(w2, key & 0x7FFF);

                switch ((key & 0x7FFF) - 0x1E) {
                    case 0:
                        g_gameConfig.music ^= 0x1;
                        if (g_gameConfig.music == 0) Driver_Music_Stop();
                        break;

                    case 1:
                        g_gameConfig.sounds ^= 0x1;
                        if (g_gameConfig.sounds == 0) Driver_Sound_Stop();
                        break;

                    case 2:
                        if (++g_gameConfig.gameSpeed >= 5) g_gameConfig.gameSpeed = 0;
                        break;

                    case 3:
                        g_gameConfig.hints ^= 0x1;
                        break;

                    case 4:
                        g_gameConfig.autoScroll ^= 0x1;
                        break;

                    case 5:
                        loop = false;
                        break;

                    default: break;
                }

                GUI_Widget_MakeNormal(w, false);

                GUI_Widget_Draw(w);
            }

            GUI_PaletteAnimate();
        }

        GUI_Window_RestoreScreen(desc);
    }

    /* shade everything except colors 231 to 238 */
    static void ShadeScreen() {
        int i;

        memmove(g_palette_998A, g_palette1, 256 * 3);

        for (i = 0; i < 231 * 3; i++) g_palette1[i] = g_palette1[i] / 2;
        for (i = 239 * 3; i < 256 * 3; i++) g_palette1[i] = g_palette1[i] / 2;

        GFX_SetPalette(g_palette_998A);
    }

    static void UnshadeScreen() {
        memmove(g_palette1, g_palette_998A, 256 * 3);

        GFX_SetPalette(g_palette1);
    }

    static boolean GUI_YesNo(int stringID) {
        WindowDesc desc = g_yesNoWindowDesc;
        boolean loop;
        boolean ret = false;

        desc.stringID = stringID;

        GUI_Window_BackupScreen(desc);

        GUI_Window_Create(desc);

        for (loop = true; loop; sleepIdle()) {
            int key = GUI_Widget_HandleEvents(g_widgetLinkedListTail);

            if ((key & 0x8000) != 0) {
                switch (key & 0x7FFF) {
                    case 0x1E: ret = true; break;
                    case 0x1F: ret = false; break;
                    default: break;
                }
                loop = false;
            }

            GUI_PaletteAnimate();
        }

        GUI_Window_RestoreScreen(desc);

        return ret;
    }
    /**
     * Handles Click event for "Options" button.
     *
     * @param w The widget.
     * @return False, always.
     */
    public static boolean GUI_Widget_Options_Click(Widget w) {
        WindowDesc desc = g_optionsWindowDesc;
        int cursor = g_cursorSpriteID;
        boolean loop;

        g_cursorSpriteID = 0;

        Sprites_SetMouseSprite(0, 0, g_sprites[0]);

        Sprites_UnloadTiles();

        memmove(g_palette_998A, g_paletteActive, 256 * 3);

        Driver_Voice_Play(null, 0xFF);

        Timer_SetTimer(TIMER_GAME, false);

        GUI_DrawText_Wrapper(null, 0, 0, 0, 0, 0x22);

        ShadeScreen();

        GUI_Window_BackupScreen(desc);

        GUI_Window_Create(desc);

        for (loop = true; loop; sleepIdle()) {
            Widget w2 = g_widgetLinkedListTail;
            int key = GUI_Widget_HandleEvents(w2);

            if ((key & 0x8000) != 0) {
                w = GUI_Widget_Get_ByIndex(w2, key);

                GUI_Window_RestoreScreen(desc);

                switch ((key & 0x7FFF) - 0x1E) {
                    case 0:
                        if (GUI_Widget_SaveLoad_Click(false)) loop = false;
                        break;

                    case 1:
                        if (GUI_Widget_SaveLoad_Click(true)) loop = false;
                        break;

                    case 2:
                        GUI_Widget_GameControls_Click(w);
                        break;

                    case 3:
                        /* "Are you sure you wish to restart?" */
                        if (!GUI_YesNo(STR_ARE_YOU_SURE_YOU_WISH_TO_RESTART)) break;

                        loop = false;
                        g_gameMode = GM_RESTART;
                        break;

                    case 4:
                        /* "Are you sure you wish to pick a new house?" */
                        if (!GUI_YesNo(STR_ARE_YOU_SURE_YOU_WISH_TO_PICK_A_NEW_HOUSE)) break;

                        loop = false;
                        Driver_Music_FadeOut();
                        g_gameMode = GM_PICKHOUSE;
                        break;

                    case 5:
                        loop = false;
                        break;

                    case 6:
                        /* "Are you sure you want to quit playing?" */
                        loop = !GUI_YesNo(STR_ARE_YOU_SURE_YOU_WANT_TO_QUIT_PLAYING);
                        g_running = loop;

                        Sound_Output_Feedback(0xFFFE);

                        while (Driver_Voice_IsPlaying()) sleepIdle();
                        break;

                    default: break;
                }

                if (g_running && loop) {
                    GUI_Window_BackupScreen(desc);

                    GUI_Window_Create(desc);
                }
            }

            GUI_PaletteAnimate();
        }

        g_textDisplayNeedsUpdate = true;

        Sprites_LoadTiles();
        GUI_DrawInterfaceAndRadar(SCREEN_0);

        UnshadeScreen();

        GUI_Widget_MakeSelected(w, false);

        Timer_SetTimer(TIMER_GAME, true);

        GameOptions_Save();

        Structure_Recount();
        Unit_Recount();

        g_cursorSpriteID = cursor;

        Sprites_SetMouseSprite(0, 0, g_sprites[cursor]);

        return false;
    }

    static int GetSavegameCount() {
        int i;

        for (i = 0;; i++) {
            if (!File_Exists_Personal(GenerateSavegameFilename(i))) return i;
        }
    }

    static void FillSavegameDesc(boolean save) {
        int i;

        for (i = 0; i < 5; i++) {
            char *desc = g_savegameDesc[i];
            char *filename;
            int fileId;

		    *desc = '\0';

            if (s_savegameIndexBase - i < 0) continue;

            if (s_savegameIndexBase - i == s_savegameCountOnDisk) {
                if (!save) continue;

                strncpy(desc, String_Get_ByIndex(STR_EMPTY_SLOT_), 50);
                continue;
            }

            filename = GenerateSavegameFilename(s_savegameIndexBase - i);

            fileId = ChunkFile_Open_Personal(filename);
            if (fileId == FILE_INVALID) continue;
            ChunkFile_Read(fileId, HTOBE32(CC_NAME), desc, 50);
            ChunkFile_Close(fileId);
            continue;
        }
    }

    /**
     * Handles Click event for savegame button.
     *
     * @param index The index of the clicked button.
     * @return True if a game has been saved, False otherwise.
     */
    static boolean GUI_Widget_Savegame_Click(int index) {
        WindowDesc desc = g_savegameNameWindowDesc;
        boolean loop;
        char *saveDesc = g_savegameDesc[index];
        boolean widgetPaint;
        boolean ret;

        if (*saveDesc == '[') *saveDesc = 0;

        GUI_Window_BackupScreen(desc);

        GUI_Window_Create(desc);

        ret = false;
        widgetPaint = true;

        if (*saveDesc == '[') index = s_savegameCountOnDisk;

        GFX_Screen_SetActive(SCREEN_0);

        Widget_SetCurrentWidget(15);

        GUI_Mouse_Hide_Safe();
        GUI_DrawBorder((g_curWidgetXBase << 3) - 1, g_curWidgetYBase - 1, (g_curWidgetWidth << 3) + 2, g_curWidgetHeight + 2, 4, false);
        GUI_Mouse_Show_Safe();

        for (loop = true; loop; sleepIdle()) {
            int eventKey;
            Widget w = g_widgetLinkedListTail;

            GUI_DrawText_Wrapper(null, 0, 0, 232, 235, 0x22);

            eventKey = GUI_EditBox(saveDesc, 50, 15, g_widgetLinkedListTail, null, widgetPaint);
            widgetPaint = false;

            if ((eventKey & 0x8000) == 0) continue;

            GUI_Widget_MakeNormal(GUI_Widget_Get_ByIndex(w, eventKey & 0x7FFF), false);

            switch (eventKey & 0x7FFF) {
                case 0x1E:	/* RETURN / Save Button */
                    if (*saveDesc == 0) break;

                    SaveGame_SaveFile(GenerateSavegameFilename(s_savegameIndexBase - index), saveDesc);
                    loop = false;
                    ret = true;
                    break;

                case 0x1F:	/* ESCAPE / Cancel Button */
                    loop = false;
                    ret = false;
                    FillSavegameDesc(true);
                    break;

                default: break;
            }
        }

        GUI_Window_RestoreScreen(desc);

        return ret;
    }

    static void UpdateArrows(boolean save, boolean force) {
        int previousIndex = 0;
        Widget w;

        if (!force && s_savegameIndexBase == previousIndex) return;

        previousIndex = s_savegameIndexBase;

        w = g_table_windowWidgets[8];
        if (s_savegameIndexBase >= 5) {
            GUI_Widget_MakeVisible(w);
        } else {
            GUI_Widget_MakeInvisible(w);
            GUI_Widget_Undraw(w, 233);
        }

        w = g_table_windowWidgets[7];
        if (s_savegameCountOnDisk - (save ? 0 : 1) > s_savegameIndexBase) {
            GUI_Widget_MakeVisible(w);
        } else {
            GUI_Widget_MakeInvisible(w);
            GUI_Widget_Undraw(w, 233);
        }
    }

    /**
     * Handles Click event for "Save Game" or "Load Game" button.
     *
     * @param save Wether to save or load.
     * @return True if a game has been saved or loaded, False otherwise.
     */
    boolean GUI_Widget_SaveLoad_Click(boolean save) {
        WindowDesc desc = g_saveLoadWindowDesc;
        boolean loop;

        s_savegameCountOnDisk = GetSavegameCount();

        s_savegameIndexBase = max(0, s_savegameCountOnDisk - (save ? 0 : 1));

        FillSavegameDesc(save);

        desc.stringID = save ? STR_SELECT_A_POSITION_TO_SAVE_TO : STR_SELECT_A_SAVED_GAME_TO_LOAD;

        GUI_Window_BackupScreen(desc);

        GUI_Window_Create(desc);

        UpdateArrows(save, true);

        for (loop = true; loop; sleepIdle()) {
            Widget w = g_widgetLinkedListTail;
            int key = GUI_Widget_HandleEvents(w);

            UpdateArrows(save, false);

            if ((key & 0x8000) != 0) {
                Widget w2;

                key &= 0x7FFF;
                w2 = GUI_Widget_Get_ByIndex(w, key);

                switch (key) {
                    case 0x25:
                        s_savegameIndexBase = min(s_savegameCountOnDisk - (save ? 0 : 1), s_savegameIndexBase + 1);

                        FillSavegameDesc(save);

                        GUI_Widget_DrawAll(w);
                        break;

                    case 0x26:
                        s_savegameIndexBase = max(0, s_savegameIndexBase - 1);

                        FillSavegameDesc(save);

                        GUI_Widget_DrawAll(w);
                        break;

                    case 0x23:
                        loop = false;
                        break;

                    default: {
                        GUI_Window_RestoreScreen(desc);

                        key -= 0x1E;

                        if (!save) {
                            return SaveGame_LoadFile(GenerateSavegameFilename(s_savegameIndexBase - key));
                        }

                        if (GUI_Widget_Savegame_Click(key)) return true;

                        GUI_Window_BackupScreen(desc);

                        UpdateArrows(save, true);

                        GUI_Window_Create(desc);

                        UpdateArrows(save, true);
                    } break;
                }

                GUI_Widget_MakeNormal(w2, false);
            }

            GUI_PaletteAnimate();
        }

        GUI_Window_RestoreScreen(desc);

        return false;
    }

    /**
     * Handles Click event for "Clear List" button.
     *
     * @param w The widget.
     * @return True, always.
     */
    boolean GUI_Widget_HOF_ClearList_Click(Widget w) {
        /* "Are you sure you want to clear the high scores?" */
        if (GUI_YesNo(STR_ARE_YOU_SURE_YOU_WANT_TO_CLEAR_THE_HIGH_SCORES)) {
            HallOfFameStruct *data = w.data;

            memset(data, 0, 128);

            if (File_Exists_Personal("SAVEFAME.DAT")) File_Delete_Personal("SAVEFAME.DAT");

            GUI_HallOfFame_DrawData(data, true);

            g_doQuitHOF = true;
        }

        GUI_Widget_MakeNormal(w, false);

        return true;
    }

    /**
     * Handles Click event for "Resume Game" button.
     *
     * @return True, always.
     */
    boolean GUI_Widget_HOF_Resume_Click(Widget w) {
        VARIABLE_NOT_USED(w);

        g_doQuitHOF = true;

        return true;
    }

    /**
     * Handles Click event for the list in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_List_Click(Widget w) {
        GUI_FactoryWindow_B495_0F30();

        g_factoryWindowSelected = w.index - 46;

        GUI_FactoryWindow_DrawDetails();

        GUI_FactoryWindow_UpdateSelection(true);

        return true;
    }

    /**
     * Handles Click event for the "Resume Game" button in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_ResumeGame_Click(Widget w) {
        g_factoryWindowResult = FACTORY_RESUME;

        if (g_factoryWindowStarport) {
            uint8 i = 0;
            House h = g_playerHouse;
            while (g_factoryWindowOrdered != 0) {
                if (g_factoryWindowItems[i].amount != 0) {
                    h.credits += g_factoryWindowItems[i].amount * g_factoryWindowItems[i].credits;
                    g_factoryWindowOrdered -= g_factoryWindowItems[i].amount;
                    g_factoryWindowItems[i].amount = 0;
                }

                i++;

                GUI_DrawCredits(g_playerHouseID, 0);
            }
        }

        if (w != null) GUI_Widget_MakeNormal(w, false);

        return true;
    }

    /**
     * Handles Click event for the "Upgrade" button in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_Upgrade_Click(Widget w) {
        GUI_Widget_MakeNormal(w, false);

        g_factoryWindowResult = FACTORY_UPGRADE;

        return true;
    }

    static void GUI_FactoryWindow_ScrollList(int step) {
        int i;
        int y = 32;

        GUI_FactoryWindow_B495_0F30();

        GUI_Mouse_Hide_Safe();

        for (i = 0; i < 32; i++) {
            y += step;
            GFX_Screen_Copy2(72, y, 72, 16, 32, 136, SCREEN_1, SCREEN_0, false);
        }

        GUI_Mouse_Show_Safe();

        GUI_FactoryWindow_PrepareScrollList();

        GUI_FactoryWindow_UpdateSelection(true);
    }

    static void GUI_FactoryWindow_FailScrollList(int step) {
        int i;
        int y = 32;

        GUI_FactoryWindow_B495_0F30();

        GUI_Mouse_Hide_Safe();

        GUI_FactoryWindow_B495_0F30();

        for (i = 0; i < 6; i++) {
            y += step;
            GFX_Screen_Copy2(72, y, 72, 16, 32, 136, SCREEN_1, SCREEN_0, false);
        }

        for (i = 0; i < 6; i++) {
            y -= step;
            GFX_Screen_Copy2(72, y, 72, 16, 32, 136, SCREEN_1, SCREEN_0, false);
        }

        GUI_Mouse_Show_Safe();

        GUI_FactoryWindow_UpdateSelection(true);
    }

    /**
     * Handles Click event for the "Down" button in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_Down_Click(Widget w) {
        boolean drawDetails = false;

        if (g_factoryWindowSelected < 3 && (g_factoryWindowSelected + 1) < g_factoryWindowTotal) {
            g_timerTimeout = 10;
            GUI_FactoryWindow_B495_0F30();
            g_factoryWindowSelected++;

            GUI_FactoryWindow_UpdateSelection(true);

            drawDetails = true;
        } else {
            if (g_factoryWindowBase + 4 < g_factoryWindowTotal) {
                g_timerTimeout = 10;
                g_factoryWindowBase++;
                drawDetails = true;

                GUI_FactoryWindow_ScrollList(1);

                GUI_FactoryWindow_UpdateSelection(true);
            } else {
                GUI_FactoryWindow_DrawDetails();

                GUI_FactoryWindow_FailScrollList(1);
            }
        }

        for (; g_timerTimeout != 0; sleepIdle()) {
            GUI_FactoryWindow_UpdateSelection(false);
        }

        if (drawDetails) GUI_FactoryWindow_DrawDetails();

        GUI_Widget_MakeNormal(w, false);

        return true;
    }

    /**
     * Handles Click event for the "Up" button in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_Up_Click(Widget w) {
        boolean drawDetails = false;

        if (g_factoryWindowSelected != 0) {
            g_timerTimeout = 10;
            GUI_FactoryWindow_B495_0F30();
            g_factoryWindowSelected--;

            GUI_FactoryWindow_UpdateSelection(true);

            drawDetails = true;
        } else {
            if (g_factoryWindowBase != 0) {
                g_timerTimeout = 10;
                g_factoryWindowBase--;
                drawDetails = true;

                GUI_FactoryWindow_ScrollList(-1);

                GUI_FactoryWindow_UpdateSelection(true);
            } else {
                GUI_FactoryWindow_DrawDetails();

                GUI_FactoryWindow_FailScrollList(-1);
            }
        }

        for (; g_timerTimeout != 0; sleepIdle()) {
            GUI_FactoryWindow_UpdateSelection(false);
        }

        if (drawDetails) GUI_FactoryWindow_DrawDetails();

        GUI_Widget_MakeNormal(w, false);

        return true;
    }

    static void GUI_Purchase_ShowInvoice() {
        Widget w = g_widgetInvoiceTail;
        int oldScreenID;
        int y = 48;
        int total = 0;
        int x;
        char textBuffer[12];

        oldScreenID = GFX_Screen_SetActive(SCREEN_1);

        GUI_DrawFilledRectangle(128, 48, 311, 159, 20);

        GUI_DrawText_Wrapper(String_Get_ByIndex(STR_ITEM_NAME_QTY_TOTAL), 128, y, 12, 0, 0x11);

        y += 7;

        GUI_DrawLine(129, y, 310, y, 12);

        y += 2;

        if (g_factoryWindowOrdered != 0) {
            int i;

            for (i = 0; i < g_factoryWindowTotal; i++) {
                ObjectInfo *oi;
                int amount;

                if (g_factoryWindowItems[i].amount == 0) continue;

                amount = g_factoryWindowItems[i].amount * g_factoryWindowItems[i].credits;
                total += amount;

                snprintf(textBuffer, sizeof(textBuffer), "%02d %5d", g_factoryWindowItems[i].amount, amount);

                oi = g_factoryWindowItems[i].objectInfo;
                GUI_DrawText_Wrapper(String_Get_ByIndex(oi.stringID_full), 128, y, 8, 0, 0x11);

                GUI_DrawText_Monospace(textBuffer, 311 - (short)strlen(textBuffer) * 6, y, 15, 0, 6);

                y += 8;
            }
        } else {
            GUI_DrawText_Wrapper(String_Get_ByIndex(STR_NO_UNITS_ON_ORDER), 220, 99, 6, 0, 0x112);
        }

        GUI_DrawLine(129, 148, 310, 148, 12);
        GUI_DrawLine(129, 150, 310, 150, 12);

        snprintf(textBuffer, sizeof(textBuffer), "%d", total);

        x = 311 - (short)strlen(textBuffer) * 6;

        /* "Total Cost :" */
        GUI_DrawText_Wrapper(GUI_String_Get_ByIndex(STR_TOTAL_COST_), x - 3, 152, 11, 0, 0x211);
        GUI_DrawText_Monospace(textBuffer, x, 152, 11, 0, 6);

        GUI_Mouse_Hide_Safe();
        GUI_Screen_Copy(16, 48, 16, 48, 23, 112, SCREEN_1, SCREEN_0);
        GUI_Mouse_Show_Safe();

        GFX_Screen_SetActive(SCREEN_0);

        GUI_FactoryWindow_DrawCaption(String_Get_ByIndex(STR_INVOICE_OF_UNITS_ON_ORDER));

        Input_History_Clear();

        for (; GUI_Widget_HandleEvents(w) == 0; sleepIdle()) {
            GUI_DrawCredits(g_playerHouseID, 0);

            GUI_FactoryWindow_UpdateSelection(false);

            GUI_PaletteAnimate();
        }

        GFX_Screen_SetActive(oldScreenID);

        w = GUI_Widget_Get_ByIndex(w, 10);

        if (w != null && Mouse_InsideRegion(w.offsetX, w.offsetY, w.offsetX + w.width, w.offsetY + w.height) != 0) {
            while (Input_Test(0x41) != 0 || Input_Test(0x42) != 0) sleepIdle();
            Input_History_Clear();
        }

        if (g_factoryWindowResult == FACTORY_CONTINUE) GUI_FactoryWindow_DrawDetails();
    }

    /**
     * Handles Click event for the "Invoice" button in starport window.
     *
     * @return True, always.
     */
    boolean GUI_Purchase_Invoice_Click(Widget w) {
        GUI_Widget_MakeInvisible(w);
        GUI_Purchase_ShowInvoice();
        GUI_Widget_MakeVisible(w);
        GUI_Widget_MakeNormal(w, false);
        return true;
    }

    /**
     * Handles Click event for the "Build this" button in production window.
     *
     * @return True, always.
     */
    boolean GUI_Production_BuildThis_Click(Widget w) {
        if (g_factoryWindowStarport) {
            if (g_factoryWindowOrdered == 0) {
                GUI_Widget_MakeInvisible(w);
                GUI_Purchase_ShowInvoice();
                GUI_Widget_MakeVisible(w);
            } else {
                g_factoryWindowResult = FACTORY_BUY;
            }
        } else {
            FactoryWindowItem item;
            GObjectInfo oi;

            item = GUI_FactoryWindow_GetItem(g_factoryWindowSelected);
            oi = item.objectInfo;

            if (oi.available > 0) {
                item.amount = 1;
                g_factoryWindowResult = FACTORY_BUY;
            }
        }

        GUI_Widget_MakeNormal(w, false);

        return true;
    }

    /**
     * Handles Click event for the "+" button in starport window.
     *
     * @return True, always.
     */
    boolean GUI_Purchase_Plus_Click(Widget w) {
        FactoryWindowItem item = GUI_FactoryWindow_GetItem(g_factoryWindowSelected);
        GObjectInfo oi = item.objectInfo;
        House h = g_playerHouse;
        boolean canCreateMore = true;
        int type = item.objectType;

        GUI_Widget_MakeNormal(w, false);

        if (g_table_unitInfo[type].movementType != MOVEMENT_WINGER && g_table_unitInfo[type].movementType != MOVEMENT_SLITHER) {
            if (g_starPortEnforceUnitLimit && h.unitCount >= h.unitCountMax) canCreateMore = false;
        }

        if (item.amount < oi.available && item.credits <= h.credits && canCreateMore) {
            item.amount++;

            GUI_FactoryWindow_UpdateDetails(item);

            g_factoryWindowOrdered++;

            h.credits -= item.credits;

            GUI_FactoryWindow_DrawCaption(null);
        }

        return true;
    }

    /**
     * Handles Click event for the "-" button in startport window.
     *
     * @return True, always.
     */
    boolean GUI_Purchase_Minus_Click(Widget w) {
        FactoryWindowItem item;
        House h = g_playerHouse;

        GUI_Widget_MakeNormal(w, false);

        item = GUI_FactoryWindow_GetItem(g_factoryWindowSelected);

        if (item.amount != 0) {
            item.amount--;

            GUI_FactoryWindow_UpdateDetails(item);

            g_factoryWindowOrdered--;

            h.credits += item.credits;

            GUI_FactoryWindow_DrawCaption(null);
        }

        return true;
    }
}
