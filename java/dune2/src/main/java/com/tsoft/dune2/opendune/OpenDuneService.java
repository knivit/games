package com.tsoft.dune2.opendune;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tsoft.dune2.config.DuneCfg;
import com.tsoft.dune2.gobject.GObjectInfo;
import com.tsoft.dune2.gui.widget.Widget;
import com.tsoft.dune2.gui.widget.WidgetInfo;
import com.tsoft.dune2.gui.widget.WidgetProperties;
import com.tsoft.dune2.house.House;
import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.unit.Unit;
import com.tsoft.dune2.unit.UnitInfo;
import com.tsoft.dune2.video.VideoScaleFilter;

import java.util.Arrays;

import static com.tsoft.dune2.animation.AnimationService.Animation_Init;
import static com.tsoft.dune2.audio.SoundService.Sound_Output_Feedback;
import static com.tsoft.dune2.config.ConfigService.*;
import static com.tsoft.dune2.explosion.ExplosionService.Explosion_Init;
import static com.tsoft.dune2.file.FileService.*;
import static com.tsoft.dune2.gfx.GfxService.*;
import static com.tsoft.dune2.gfx.Screen.*;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.gui.SelectionType.*;
import static com.tsoft.dune2.gui.font.FontService.*;
import static com.tsoft.dune2.gui.mentat.MentatService.*;
import static com.tsoft.dune2.gui.widget.WidgetDrawService.GUI_Widget_ActionPanel_Draw;
import static com.tsoft.dune2.gui.widget.WidgetService.*;
import static com.tsoft.dune2.house.HouseService.*;
import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;
import static com.tsoft.dune2.house.HouseType.HOUSE_MERCENARY;
import static com.tsoft.dune2.ini.IniFileService.IniFile_GetInteger;
import static com.tsoft.dune2.ini.IniFileService.IniFile_GetString;
import static com.tsoft.dune2.input.InputFlagsEnum.*;
import static com.tsoft.dune2.input.InputMouseMode.INPUT_MOUSE_MODE_NORMAL;
import static com.tsoft.dune2.input.InputService.*;
import static com.tsoft.dune2.input.MouseService.*;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.opendune.GameMode.*;
import static com.tsoft.dune2.pool.PoolHouseService.*;
import static com.tsoft.dune2.pool.PoolStructureService.*;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Init;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Recount;
import static com.tsoft.dune2.pool.PoolUnitService.*;
import static com.tsoft.dune2.scenario.ScenarioService.Scenario_Load;
import static com.tsoft.dune2.scenario.ScenarioService.g_scenario;
import static com.tsoft.dune2.script.ScriptService.*;
import static com.tsoft.dune2.sprites.SpritesService.*;
import static com.tsoft.dune2.strings.StringService.*;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureService.*;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_READY;
import static com.tsoft.dune2.structure.StructureType.*;
import static com.tsoft.dune2.team.TeamService.GameLoop_Team;
import static com.tsoft.dune2.tile.TileService.Tile_Center;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;
import static com.tsoft.dune2.timer.TimerService.*;
import static com.tsoft.dune2.timer.TimerType.TIMER_GAME;
import static com.tsoft.dune2.timer.TimerType.TIMER_GUI;
import static com.tsoft.dune2.tools.ToolsService.Tools_RandomLCG_Range;
import static com.tsoft.dune2.tools.ToolsService.Tools_RandomLCG_Seed;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.UNIT_INVALID;
import static com.tsoft.dune2.unit.UnitType.UNIT_MAX;
import static com.tsoft.dune2.video.VideoScaleFilter.FILTER_NEAREST_NEIGHBOR;
import static com.tsoft.dune2.video.VideoWin32Service.*;
import static java.lang.Long.max;

public class OpenDuneService extends Game {

    public static SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        GameManager.instance.dispose();
    }
    
    static String window_caption = "OpenDUNE - v0.9";

    public static boolean g_dune2_enhanced = true;             /* If false, the game acts exactly like the original Dune2, including bugs. */
    public static boolean g_starPortEnforceUnitLimit = false;  /* If true, one cannot circumvent unit cap using starport */
    static boolean g_unpackSHPonLoad = true;	               /* If true, Format80 encoded sprites from SHP files will be decoded on load. set to false to save memory */

    static long g_hintsShown1 = 0;          /* A bit-array to indicate which hints has been show already (0-31). */
    static long g_hintsShown2 = 0;          /* A bit-array to indicate which hints has been show already (32-63). */
    static int g_gameMode = GM_MENU;
    public static int g_campaignID = 0;
    public static int g_scenarioID = 1;
    public static int g_activeAction = 0xFFFF;              /* Action the controlled unit will do. */
    public static long g_tickScenarioStart = 0;             /* The tick the scenario started in. */
    static long s_tickGameTimeout = 0;                      /* The tick the game will timeout. */

    public static boolean g_debugGame = false;              /* When true, you can control the AI. */
    public static boolean g_debugScenario = false;          /* When true, you can review the scenario. There is no fog. The game is not running (no unit-movement, no structure-building, etc). You can click on individual tiles. */
    public static boolean g_debugSkipDialogs = false;       /* When non-zero, you immediately go to house selection, and skip all intros. */

    public static byte[] g_readBuffer = null;
    public static long g_readBufferSize = 0;

    static boolean  s_debugForceWin = false;                /*!< When true, you immediately win the level. */

    static int s_enableLog = 0;                             /*!< 0 = off, 1 = record game, 2 = playback game (stored in 'dune.log'). */

    public static int g_validateStrictIfZero = 0;           /*!< 0 = strict validation, basically: no-cheat-mode. */
    public static boolean g_running = true;                 /*!< true if game needs to keep running; false to stop the game. */
    public static int g_selectionType = 0;
    static int g_selectionTypeNew = 0;
    public static boolean g_viewport_forceRedraw = false;   /*!< Force a full redraw of the screen. */
    public static boolean g_viewport_fadein = false;        /*!< Fade in the screen. */

    public static int g_musicInBattle = 0;                  /*!< 0 = no battle, 1 = fight is going on, -1 = music of fight is going on is active. */

    /**
     * Check if a level is finished, based on the values in WinFlags.
     *
     * @return True if and only if the level has come to an end.
     */
    static boolean GameLoop_IsLevelFinished() {
        boolean finish = false;

        if (s_debugForceWin) return true;

        /* You have to play at least 7200 ticks before you can win the game */
        if (g_timerGame - g_tickScenarioStart < 7200) return false;

        /* Check for structure counts hitting zero */
        if ((g_scenario.winFlags & 0x3) != 0) {
            int countStructureEnemy = 0;
            int countStructureFriendly = 0;

            PoolFindStruct find = new PoolFindStruct();
            find.houseID = HOUSE_INVALID;
            find.type = 0xFFFF;
            find.index = 0xFFFF;

            /* Calculate how many structures are left on the map */
            while (true) {
                Structure s = Structure_Find(find);
                if (s == null) break;

                if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;
                if (s.o.type == STRUCTURE_TURRET) continue;
                if (s.o.type == STRUCTURE_ROCKET_TURRET) continue;

                if (s.o.houseID == g_playerHouseID) {
                    countStructureFriendly++;
                } else {
                    countStructureEnemy++;
                }
            }

            if ((g_scenario.winFlags & 0x1) != 0 && countStructureEnemy == 0) {
                finish = true;
            }
            if ((g_scenario.winFlags & 0x2) != 0 && countStructureFriendly == 0) {
                finish = true;
            }
        }

        /* Check for reaching spice quota */
        if ((g_scenario.winFlags & 0x4) != 0 && g_playerCredits != 0xFFFF) {
            if (g_playerCredits >= g_playerHouse.creditsQuota) {
                finish = true;
            }
        }

        /* Check for reaching timeout */
        if ((g_scenario.winFlags & 0x8) != 0) {
            /* XXX -- This code was with '<' instead of '>=', which makes
             *  no sense. As it is unused, who knows what the intentions
             *  were. This at least makes it sensible. */
            if (g_timerGame >= s_tickGameTimeout) {
                finish = true;
            }
        }

        return finish;
    }

    /**
     * Check if a level is won, based on the values in LoseFlags.
     *
     * @return True if and only if the level has been won by the human.
     */
    static boolean GameLoop_IsLevelWon() {
        boolean win = false;

        if (s_debugForceWin) return true;

        /* Check for structure counts hitting zero */
        if ((g_scenario.loseFlags & 0x3) != 0) {
            PoolFindStruct find = new PoolFindStruct();
            int countStructureEnemy = 0;
            int countStructureFriendly = 0;

            find.houseID = HOUSE_INVALID;
            find.type = 0xFFFF;
            find.index = 0xFFFF;

            /* Calculate how many structures are left on the map */
            while (true) {
                Structure s = Structure_Find(find);
                if (s == null) break;

                if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;
                if (s.o.type == STRUCTURE_TURRET) continue;
                if (s.o.type == STRUCTURE_ROCKET_TURRET) continue;

                if (s.o.houseID == g_playerHouseID) {
                    countStructureFriendly++;
                } else {
                    countStructureEnemy++;
                }
            }

            win = true;
            if ((g_scenario.loseFlags & 0x1) != 0) {
                win = win && (countStructureEnemy == 0);
            }
            if ((g_scenario.loseFlags & 0x2) != 0) {
                win = win && (countStructureFriendly != 0);
            }
        }

        /* Check for reaching spice quota */
        if (!win && (g_scenario.loseFlags & 0x4) != 0 && g_playerCredits != 0xFFFF) {
            win = (g_playerCredits >= g_playerHouse.creditsQuota);
        }

        /* Check for reaching timeout */
        if (!win && (g_scenario.loseFlags & 0x8) != 0) {
            win = (g_timerGame < s_tickGameTimeout);
        }

        return win;
    }

    public static void GameLoop_Uninit() {
        while (g_widgetLinkedListHead != null) {
            Widget w = g_widgetLinkedListHead;
            g_widgetLinkedListHead = w.next;

            free(w);
        }

        Script_ClearInfo(g_scriptStructure);
        Script_ClearInfo(g_scriptTeam);

        free(g_readBuffer); g_readBuffer = null;

        free(g_palette1); g_palette1 = null;
        free(g_palette2); g_palette2 = null;
        free(g_paletteMapping1); g_paletteMapping1 = null;
        free(g_paletteMapping2); g_paletteMapping2 = null;
    }

    /**
     * Checks if the level comes to an end. If so, it shows all end-level stuff,
     *  and prepares for the next level.
     */
    static void GameLoop_LevelEnd() {
        long levelEndTimer = 0;

        if (levelEndTimer >= g_timerGame && !s_debugForceWin) return;

        if (GameLoop_IsLevelFinished()) {
            Music_Play(0);

            g_cursorSpriteID = 0;

            Sprites_SetMouseSprite(0, 0, g_sprites[0]);

            Sound_Output_Feedback(0xFFFE);

            GUI_ChangeSelectionType(SELECTIONTYPE_MENTAT);

            if (GameLoop_IsLevelWon()) {
                Sound_Output_Feedback(40);

                GUI_DisplayModalMessage(String_Get_ByIndex(STR_YOU_HAVE_SUCCESSFULLY_COMPLETED_YOUR_MISSION), 0xFFFF);

                GUI_Mentat_ShowWin();

                Sprites_UnloadTiles();

                g_campaignID++;

                GUI_EndStats_Show(g_scenario.killedAllied, g_scenario.killedEnemy, g_scenario.destroyedAllied, g_scenario.destroyedEnemy, g_scenario.harvestedAllied, g_scenario.harvestedEnemy, g_scenario.score, g_playerHouseID);

                if (g_campaignID == 9) {
                    GUI_Mouse_Hide_Safe();

                    GUI_SetPaletteAnimated(g_palette2, 15);
                    GUI_ClearScreen(SCREEN_0);
                    GameLoop_GameEndAnimation();
                    PrepareEnd();
                    System.exit(0);
                }

                GUI_Mouse_Hide_Safe();
                GameLoop_LevelEndAnimation();
                GUI_Mouse_Show_Safe();

                File_ReadBlockFile("IBM.PAL", g_palette1, 256 * 3);

                g_scenarioID = GUI_StrategicMap_Show(g_campaignID, true);

                GUI_SetPaletteAnimated(g_palette2, 15);

                if (g_campaignID == 1 || g_campaignID == 7) {
                    if (!GUI_Security_Show()) {
                        PrepareEnd();
                        System.exit(0);
                    }
                }
            } else {
                Sound_Output_Feedback(41);

                GUI_DisplayModalMessage(String_Get_ByIndex(STR_YOU_HAVE_FAILED_YOUR_MISSION), 0xFFFF);

                GUI_Mentat_ShowLose();

                Sprites_UnloadTiles();

                g_scenarioID = GUI_StrategicMap_Show(g_campaignID, false);
            }

            g_playerHouse.flags.doneFullScaleAttack = false;

            Sprites_LoadTiles();

            g_gameMode = GM_RESTART;
            s_debugForceWin = false;
        }

        levelEndTimer = g_timerGame + 300;
    }

    static void GameLoop_DrawMenu(String[][] strings) {
        WidgetProperties props;
        int left;
        int top;
        int i;

        props = g_widgetProperties[21];
        top = g_curWidgetYBase + props.yBase;
        left = (g_curWidgetXBase + props.xBase) << 3;

        GUI_Mouse_Hide_Safe();

        for (i = 0; i < props.height; i++) {
            int pos = top + g_fontCurrent.height * i;

            if (i == props.fgColourBlink) {
                GUI_DrawText_Wrapper(strings[i], left, pos, props.fgColourSelected, 0, 0x22);
            } else {
                GUI_DrawText_Wrapper(strings[i], left, pos, props.fgColourNormal, 0, 0x22);
            }
        }

        GUI_Mouse_Show_Safe();

        Input_History_Clear();
    }

    static void GameLoop_DrawText2(String string, int left, int top, int fgColourNormal, int fgColourSelected, int bgColour) {
        for (int i = 0; i < 3; i++) {
            GUI_Mouse_Hide_Safe();

            GUI_DrawText_Wrapper(string, left, top, fgColourSelected, bgColour, 0x22);
            Timer_Sleep(2);

            GUI_DrawText_Wrapper(string, left, top, fgColourNormal, bgColour, 0x22);
            GUI_Mouse_Show_Safe();
            Timer_Sleep(2);
        }
    }

    static boolean GameLoop_IsInRange(int x, int y, int minX, int minY, int maxX, int maxY) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    static int GameLoop_HandleEvents(String[][] strings) {
        int last;
        int result;
        int key;
        int top;
        int left;
        int minX;
        int maxX;
        int minY;
        int maxY;
        int lineHeight;
        int fgColourNormal;
        int fgColourSelected;
        int old;
        WidgetProperties props;
        int current;

        props = g_widgetProperties[21];

        last = props.height - 1;
        old = props.fgColourBlink % (last + 1);
        current = old;

        result = 0xFFFF;

        top = g_curWidgetYBase + props.yBase;
        left = (g_curWidgetXBase + props.xBase) << 3;

        lineHeight = g_fontCurrent.height;

        minX = (g_curWidgetXBase << 3) + (g_fontCurrent.maxWidth * props.xBase);
        minY = g_curWidgetYBase + props.yBase;
        maxX = minX + (g_fontCurrent.maxWidth * props.width) - 1;
        maxY = minY + (props.height * lineHeight) - 1;

        fgColourNormal = props.fgColourNormal;
        fgColourSelected = props.fgColourSelected;

        key = 0;
        if (Input_IsInputAvailable() != 0) {
            key = Input_Wait() & 0x8FF;
        }

        if (g_mouseDisabled == 0) {
            int y = g_mouseY;

            if (GameLoop_IsInRange(g_mouseX, y, minX, minY, maxX, maxY)) {
                current = (y - minY) / lineHeight;
            }
        }

        switch (key) {
            case 0x60: /* NUMPAD 8 / ARROW UP */
                if (current-- == 0) current = last;
                break;

            case 0x62: /* NUMPAD 2 / ARROW DOWN */
                if (current++ == last) current = 0;
                break;

            case 0x5B: /* NUMPAD 7 / HOME */
            case 0x65: /* NUMPAD 9 / PAGE UP */
                current = 0;
                break;

            case 0x5D: /* NUMPAD 1 / END */
            case 0x67: /* NUMPAD 3 / PAGE DOWN */
                current = last;
                break;

            case 0x41: /* MOUSE LEFT BUTTON */
            case 0x42: /* MOUSE RIGHT BUTTON */
                if (GameLoop_IsInRange(g_mouseClickX, g_mouseClickY, minX, minY, maxX, maxY)) {
                    current = (g_mouseClickY - minY) / lineHeight;
                    result = current;
                }
                break;

            case 0x2B: /* NUMPAD 5 / RETURN */
            case 0x3D: /* SPACE */
            case 0x61:
                result = current;
                break;

            default: {
                int i;

                for (i = 0; i < props.height; i++) {
                    char c1;
                    char c2;

                    if (strings[i] == null) continue;
                    c1 = toupper(*strings[i]);
                    c2 = toupper(Input_Keyboard_HandleKeys(key & 0xFF));

                    if (c1 == c2) {
                        result = i;
                        current = i;
                        break;
                    }
                }
            } break;
        }

        if (current != old) {
            GUI_Mouse_Hide_Safe();
            GUI_DrawText_Wrapper(strings[old], left, top + (old * lineHeight), fgColourNormal, 0, 0x22);
            GUI_DrawText_Wrapper(strings[current], left, top + (current * lineHeight), fgColourSelected, 0, 0x22);
            GUI_Mouse_Show_Safe();
        }

        props.fgColourBlink = current;

        if (result == 0xFFFF) return 0xFFFF;

        GUI_Mouse_Hide_Safe();
        GameLoop_DrawText2(strings[result], left, top + (current * lineHeight), fgColourNormal, fgColourSelected, 0);
        GUI_Mouse_Show_Safe();

        return result;
    }

    static void Window_WidgetClick_Create() {
        WidgetInfo wi;

        for (wi = g_table_gameWidgetInfo; wi.index >= 0; wi++) {
            Widget w;

            w = GUI_Widget_Allocate(wi.index, wi.shortcut, wi.offsetX, wi.offsetY, wi.spriteID, wi.stringID);

            if (wi.spriteID < 0) {
                w.width  = wi.width;
                w.height = wi.height;
            }

            w.clickProc = wi.clickProc;
            w.flags.requiresClick = (wi.flags & 0x0001) ? true : false;
            w.flags.notused1 = (wi.flags & 0x0002) ? true : false;
            w.flags.clickAsHover = (wi.flags & 0x0004) ? true : false;
            w.flags.invisible = (wi.flags & 0x0008) ? true : false;
            w.flags.greyWhenInvisible = (wi.flags & 0x0010) ? true : false;
            w.flags.noClickCascade = (wi.flags & 0x0020) ? true : false;
            w.flags.loseSelect = (wi.flags & 0x0040) ? true : false;
            w.flags.notused2 = (wi.flags & 0x0080) ? true : false;
            w.flags.buttonFilterLeft = (wi.flags >> 8) & 0x0f;
            w.flags.buttonFilterRight = (wi.flags >> 12) & 0x0f;

            g_widgetLinkedListHead = GUI_Widget_Insert(g_widgetLinkedListHead, w);
        }
    }

    static void ReadProfileIni(String filename) {
        char *source;
        char *key;
        char *keys;
        char buffer[120];
        int locsi;

        if (filename == null) return;
        if (!File_Exists(filename)) return;

        source = GFX_Screen_Get_ByIndex(SCREEN_1);

        memset(source, 0, 32000);
        File_ReadBlockFile(filename, source, GFX_Screen_GetSize_ByIndex(SCREEN_1));

        keys = source + strlen(source) + 5000;
	    *keys = '\0';

        Ini_GetString("construct", null, keys, keys, 2000, source);

        for (key = keys; *key != '\0'; key += strlen(key) + 1) {
            GObjectInfo oi = null;
            int count;
            int type;
            int buildCredits;
            int buildTime;
            int fogUncoverRadius;
            int availableCampaign;
            int sortPriority;
            int priorityBuild;
            int priorityTarget;
            int hitpoints;

            type = Unit_StringToType(key);
            if (type != UNIT_INVALID) {
                oi = g_table_unitInfo[type].o;
            } else {
                type = Structure_StringToType(key);
                if (type != STRUCTURE_INVALID) oi = g_table_structureInfo[type].o;
            }

            if (oi == null) continue;

            Ini_GetString("construct", key, buffer, buffer, 120, source);
            count = sscanf(buffer, "%hu,%hu,%hu,%hu,%hu,%hu,%hu,%hu", &buildCredits, &buildTime, &hitpoints, &fogUncoverRadius, &availableCampaign, &priorityBuild, &priorityTarget, &sortPriority);
            oi.buildCredits      = buildCredits;
            oi.buildTime         = buildTime;
            oi.hitpoints         = hitpoints;
            oi.fogUncoverRadius  = fogUncoverRadius;
            oi.availableCampaign = availableCampaign;
            oi.priorityBuild     = priorityBuild;
            oi.priorityTarget    = priorityTarget;
            if (count <= 7) continue;
            oi.sortPriority = (int)sortPriority;
        }

        if (g_debugGame) {
            for (locsi = 0; locsi < UNIT_MAX; locsi++) {
                GObjectInfo oi = g_table_unitInfo[locsi].o;

                sprintf(buffer, "%*s%4d,%4d,%4d,%4d,%4d,%4d,%4d,%4d",
                    15 - (int)strlen(oi.name), "", oi.buildCredits, oi.buildTime, oi.hitpoints, oi.fogUncoverRadius,
                    oi.availableCampaign, oi.priorityBuild, oi.priorityTarget, oi.sortPriority);

                Ini_SetString("construct", oi.name, buffer, source);
            }

            for (locsi = 0; locsi < STRUCTURE_MAX; locsi++) {
                GObjectInfo oi = g_table_structureInfo[locsi].o;

                sprintf(buffer, "%*s%4d,%4d,%4d,%4d,%4d,%4d,%4d,%4d",
                    15 - (int)strlen(oi.name), "", oi.buildCredits, oi.buildTime, oi.hitpoints, oi.fogUncoverRadius,
                    oi.availableCampaign, oi.priorityBuild, oi.priorityTarget, oi.sortPriority);

                Ini_SetString("construct", oi.name, buffer, source);
            }
        }

	    *keys = '\0';

        Ini_GetString("combat", null, keys, keys, 2000, source);

        for (key = keys; *key != '\0'; key += strlen(key) + 1) {
            int damage;
            int movingSpeedFactor;
            int fireDelay;
            int fireDistance;

            Ini_GetString("combat", key, buffer, buffer, 120, source);
            String_Trim(buffer);
            if (sscanf(buffer, "%hu,%hu,%hu,%hu", &fireDistance, &damage, &fireDelay, &movingSpeedFactor) < 4) continue;

            for (locsi = 0; locsi < UNIT_MAX; locsi++) {
                UnitInfo ui = g_table_unitInfo[locsi];

                if (strcasecmp(ui.o.name, key) != 0) continue;

                ui.damage            = damage;
                ui.movingSpeedFactor = movingSpeedFactor;
                ui.fireDelay         = fireDelay;
                ui.fireDistance      = fireDistance;
                break;
            }
        }

        if (!g_debugGame) return;

        for (locsi = 0; locsi < UNIT_MAX; locsi++) {
		    UnitInfo ui = g_table_unitInfo[locsi];

            sprintf(buffer, "%*s%4d,%4d,%4d,%4d", 15 - (int)strlen(ui.o.name), "", ui.fireDistance, ui.damage, ui.fireDelay, ui.movingSpeedFactor);
            Ini_SetString("combat", ui.o.name, buffer, source);
        }
    }

    /**
     * Intro menu.
     */
    static void GameLoop_GameIntroAnimationMenu() {
        int[][] mainMenuStrings = new int[][] {
            {STR_PLAY_A_GAME, STR_REPLAY_INTRODUCTION, STR_EXIT_GAME, STR_NULL,         STR_NULL,         STR_NULL}, /* Neither HOF nor save. */
            {STR_PLAY_A_GAME, STR_REPLAY_INTRODUCTION, STR_LOAD_GAME, STR_EXIT_GAME,    STR_NULL,         STR_NULL}, /* Has a save game. */
            {STR_PLAY_A_GAME, STR_REPLAY_INTRODUCTION, STR_EXIT_GAME, STR_HALL_OF_FAME, STR_NULL,         STR_NULL}, /* Has a HOF. */
            {STR_PLAY_A_GAME, STR_REPLAY_INTRODUCTION, STR_LOAD_GAME, STR_EXIT_GAME,    STR_HALL_OF_FAME, STR_NULL}  /* Has a HOF and a save game. */
        };

        boolean loadGame = false;
        boolean drawMenu = true;
        int stringID = STR_REPLAY_INTRODUCTION;
        int maxWidth;
        boolean hasSave = false;
        boolean hasFame = false;
        String[] strings = new String[6];
        int index = 0xFFFF;

        if (index == 0xFFFF) {
            hasSave = File_Exists_Personal("_save000.dat");
            hasFame = File_Exists_Personal("SAVEFAME.DAT");
            index = (hasFame ? 2 : 0) + (hasSave ? 1 : 0);
        }

        if (!g_canSkipIntro) {
            if (hasSave) g_canSkipIntro = true;
        }

        switch (stringID) {
            case STR_REPLAY_INTRODUCTION:
                Music_Play(0);

                free(g_readBuffer);
                g_readBufferSize = (g_enableVoices == 0) ? 12000 : 28000;
                g_readBuffer = calloc(1, g_readBufferSize);

                GUI_Mouse_Hide_Safe();

                Driver_Music_FadeOut();

                GameLoop_GameIntroAnimation();

                Sound_Output_Feedback(0xFFFE);

                File_ReadBlockFile("IBM.PAL", g_palette1, 256 * 3);

                if (!g_canSkipIntro) {
                    File_Create_Personal("ONETIME.DAT");
                    g_canSkipIntro = true;
                }

                Music_Play(0);

                free(g_readBuffer);
                g_readBufferSize = (g_enableVoices == 0) ? 12000 : 20000;
                g_readBuffer = calloc(1, g_readBufferSize);

                GUI_Mouse_Show_Safe();

                Music_Play(28);

                drawMenu = true;
                break;

            case STR_EXIT_GAME:
                g_running = false;
                return;

            case STR_HALL_OF_FAME:
                GUI_HallOfFame_Show(0xFFFF);

                GFX_SetPalette(g_palette2);

                hasFame = File_Exists_Personal("SAVEFAME.DAT");
                drawMenu = true;
                break;

            case STR_LOAD_GAME:
                GUI_Mouse_Hide_Safe();
                GUI_SetPaletteAnimated(g_palette2, 30);
                GUI_ClearScreen(SCREEN_0);
                GUI_Mouse_Show_Safe();

                GFX_SetPalette(g_palette1);

                if (GUI_Widget_SaveLoad_Click(false)) {
                    loadGame = true;
                    if (g_gameMode == GM_RESTART) break;
                    g_gameMode = GM_NORMAL;
                } else {
                    GFX_SetPalette(g_palette2);

                    drawMenu = true;
                }
                break;

            default: break;
        }

        if (drawMenu) {
            int i;

            g_widgetProperties[21].height = 0;

            for (i = 0; i < 6; i++) {
                strings[i] = null;

                if (mainMenuStrings[index][i] == 0) {
                    if (g_widgetProperties[21].height == 0) g_widgetProperties[21].height = i;
                    continue;
                }

                strings[i] = String_Get_ByIndex(mainMenuStrings[index][i]);
            }

            GUI_DrawText_Wrapper(null, 0, 0, 0, 0, 0x22);

            maxWidth = 0;

            for (i = 0; i < g_widgetProperties[21].height; i++) {
                if (Font_GetStringWidth(strings[i]) <= maxWidth) continue;
                maxWidth = Font_GetStringWidth(strings[i]);
            }

            maxWidth += 7;

            g_widgetProperties[21].width  = maxWidth >> 3;
            g_widgetProperties[13].width  = g_widgetProperties[21].width + 2;
            g_widgetProperties[13].xBase  = 19 - (maxWidth >> 4);
            g_widgetProperties[13].yBase  = 160 - ((g_widgetProperties[21].height * g_fontCurrent.height) >> 1);
            g_widgetProperties[13].height = (g_widgetProperties[21].height * g_fontCurrent.height) + 11;

            if (File_Exists("TITLE.CPS")) Sprites_LoadImage("TITLE.CPS", SCREEN_1, null);
            else Sprites_LoadImage(String_GenerateFilename("TITLE"), SCREEN_1, null);

            GUI_Mouse_Hide_Safe();

            GUI_ClearScreen(SCREEN_0);

            GUI_Screen_Copy(0, 0, 0, 0, SCREEN_WIDTH / 8, SCREEN_HEIGHT, SCREEN_1, SCREEN_0);

            GUI_SetPaletteAnimated(g_palette1, 30);

            GUI_DrawText_Wrapper("V1.07", 319, 192, 133, 0, 0x231, 0x39);
            GUI_DrawText_Wrapper(null, 0, 0, 0, 0, 0x22);

            Widget_SetCurrentWidget(13);

            GUI_Widget_DrawBorder(13, 2, 1);

            GameLoop_DrawMenu(strings);

            GUI_Mouse_Show_Safe();

            drawMenu = false;
        }

        if (loadGame) return;

        stringID = GameLoop_HandleEvents(strings);

        if (stringID != 0xFFFF) stringID = mainMenuStrings[index][stringID];

        GUI_PaletteAnimate();

        if (stringID == STR_PLAY_A_GAME) g_gameMode = GM_PICKHOUSE;
    }

    static void InGame_Numpad_Move(int key) {
        if (key == 0) return;

        switch (key) {
            case 0x0010: /* TAB */
                Map_SelectNext(true);
                return;

            case 0x0110: /* SHIFT TAB */
                Map_SelectNext(false);
                return;

            case 0x005C: /* NUMPAD 4 / ARROW LEFT */
            case 0x045C:
            case 0x055C:
                Map_MoveDirection(6);
                return;

            case 0x0066: /* NUMPAD 6 / ARROW RIGHT */
            case 0x0466:
            case 0x0566:
                Map_MoveDirection(2);
                return;

            case 0x0060: /* NUMPAD 8 / ARROW UP */
            case 0x0460:
            case 0x0560:
                Map_MoveDirection(0);
                return;

            case 0x0062: /* NUMPAD 2 / ARROW DOWN */
            case 0x0462:
            case 0x0562:
                Map_MoveDirection(4);
                return;

            case 0x005B: /* NUMPAD 7 / HOME */
            case 0x045B:
            case 0x055B:
                Map_MoveDirection(7);
                return;

            case 0x005D: /* NUMPAD 1 / END */
            case 0x045D:
            case 0x055D:
                Map_MoveDirection(5);
                return;

            case 0x0065: /* NUMPAD 9 / PAGE UP */
            case 0x0465:
            case 0x0565:
                Map_MoveDirection(1);
                return;

            case 0x0067: /* NUMPAD 3 / PAGE DOWN */
            case 0x0467:
            case 0x0567:
                Map_MoveDirection(3);
                return;

            default: return;
        }
    }

    /**
     * Main game loop.
     */
    static void GameLoop_Main() {
        static long l_timerNext = 0;
        static long l_timerUnitStatus = 0;
        static int  l_selectionState = -2;

        int key;

        String_Init();
        Sprites_Init();

        if (IniFile_GetInteger("mt32midi", 0) != 0) Music_InitMT32();

        Input_Flags_SetBits(INPUT_FLAG_KEY_REPEAT | INPUT_FLAG_UNKNOWN_0010 | INPUT_FLAG_UNKNOWN_0200 |
            INPUT_FLAG_KBD_MOUSE_CLK);
        Input_Flags_ClearBits(INPUT_FLAG_KEY_RELEASE | INPUT_FLAG_UNKNOWN_0400 | INPUT_FLAG_UNKNOWN_0100 |
            INPUT_FLAG_UNKNOWN_0080 | INPUT_FLAG_UNKNOWN_0040 | INPUT_FLAG_UNKNOWN_0020 |
            INPUT_FLAG_UNKNOWN_0008 | INPUT_FLAG_UNKNOWN_0004 | INPUT_FLAG_NO_TRANSLATE);

        Timer_SetTimer(TIMER_GAME, true);
        Timer_SetTimer(TIMER_GUI, true);

        g_campaignID = 0;
        g_scenarioID = 1;
        g_playerHouseID = HOUSE_INVALID;
        g_selectionType = SELECTIONTYPE_MENTAT;
        g_selectionTypeNew = SELECTIONTYPE_MENTAT;

        if (g_palette1) Warning("g_palette1\n");
        else g_palette1 = calloc(1, 256 * 3);
        if (g_palette2) Warning("g_palette2\n");
        else g_palette2 = calloc(1, 256 * 3);

        g_readBufferSize = 12000;
        g_readBuffer = calloc(1, g_readBufferSize);

        ReadProfileIni("PROFILE.INI");

        free(g_readBuffer); g_readBuffer = null;

        File_ReadBlockFile("IBM.PAL", g_palette1, 256 * 3);

        GUI_ClearScreen(SCREEN_0);

        Video_SetPalette(g_palette1, 0, 256);

        GFX_SetPalette(g_palette1);
        GFX_SetPalette(g_palette2);

        g_paletteMapping1 = malloc(256);
        g_paletteMapping2 = malloc(256);

        GUI_Palette_CreateMapping(g_palette1, g_paletteMapping1, 0xC, 0x55);
        g_paletteMapping1[0xFF] = 0xFF;
        g_paletteMapping1[0xDF] = 0xDF;
        g_paletteMapping1[0xEF] = 0xEF;

        GUI_Palette_CreateMapping(g_palette1, g_paletteMapping2, 0xF, 0x55);
        g_paletteMapping2[0xFF] = 0xFF;
        g_paletteMapping2[0xDF] = 0xDF;
        g_paletteMapping2[0xEF] = 0xEF;

        Script_LoadFromFile("TEAM.EMC", g_scriptTeam, g_scriptFunctionsTeam, null);
        Script_LoadFromFile("BUILD.EMC", g_scriptStructure, g_scriptFunctionsStructure, null);

        GUI_Palette_CreateRemap(HOUSE_MERCENARY);

        g_cursorSpriteID = 0;

        Sprites_SetMouseSprite(0, 0, g_sprites[0]);

        while (g_mouseHiddenDepth > 1) {
            GUI_Mouse_Show_Safe();
        }

        Window_WidgetClick_Create();
        GameOptions_Load();
        Unit_Init();
        Team_Init();
        House_Init();
        Structure_Init();

        GUI_Mouse_Show_Safe();

        g_canSkipIntro = File_Exists_Personal("ONETIME.DAT");

        for (;; sleepIdle()) {
            if (g_gameMode == GM_MENU) {
                GameLoop_GameIntroAnimationMenu();

                if (!g_running) break;
                if (g_gameMode == GM_MENU) continue;

                GUI_Mouse_Hide_Safe();

                GUI_DrawFilledRectangle(g_curWidgetXBase << 3, g_curWidgetYBase, (g_curWidgetXBase + g_curWidgetWidth) << 3, g_curWidgetYBase + g_curWidgetHeight, 12);

                Input_History_Clear();

                if (s_enableLog != 0) Mouse_SetMouseMode((int)s_enableLog, "DUNE.LOG");

                GFX_SetPalette(g_palette1);

                GUI_Mouse_Show_Safe();
            }

            if (g_gameMode == GM_PICKHOUSE) {
                Music_Play(28);

                g_playerHouseID = HOUSE_MERCENARY;
                g_playerHouseID = GUI_PickHouse();

                GUI_Mouse_Hide_Safe();

                GFX_ClearBlock(SCREEN_0);

                Sprites_LoadTiles();

                GUI_Palette_CreateRemap(g_playerHouseID);

                Voice_LoadVoices(g_playerHouseID);

                GUI_Mouse_Show_Safe();

                g_gameMode = GM_RESTART;
                g_scenarioID = 1;
                g_campaignID = 0;
                g_strategicRegionBits = 0;
            }

            if (g_selectionTypeNew != g_selectionType) {
                GUI_ChangeSelectionType(g_selectionTypeNew);
            }

            GUI_PaletteAnimate();

            if (g_gameMode == GM_RESTART) {
                GUI_ChangeSelectionType(SELECTIONTYPE_MENTAT);

                Game_LoadScenario(g_playerHouseID, g_scenarioID);
                if (!g_debugScenario && !g_debugSkipDialogs) {
                    GUI_Mentat_ShowBriefing();
                } else {
                    Debug("Skipping GUI_Mentat_ShowBriefing()\n");
                }

                g_gameMode = GM_NORMAL;

                GUI_ChangeSelectionType(g_debugScenario ? SELECTIONTYPE_DEBUG : SELECTIONTYPE_STRUCTURE);

                Music_Play(Tools_RandomLCG_Range(0, 8) + 8);
                l_timerNext = g_timerGUI + 300;
            }

            if (l_selectionState != g_selectionState) {
                Map_SetSelectionObjectPosition(0xFFFF);
                Map_SetSelectionObjectPosition(g_selectionRectanglePosition);
                l_selectionState = g_selectionState;
            }

            if (!Driver_Voice_IsPlaying() && !Sound_StartSpeech()) {
                if (g_gameConfig.music == 0) {
                    Music_Play(2);

                    g_musicInBattle = 0;
                } else if (g_musicInBattle > 0) {
                    Music_Play(Tools_RandomLCG_Range(0, 5) + 17);
                    l_timerNext = g_timerGUI + 300;
                    g_musicInBattle = -1;
                } else {
                    g_musicInBattle = 0;
                    if (g_enableSoundMusic != 0 && g_timerGUI > l_timerNext) {
                        if (!Driver_Music_IsPlaying()) {
                            Music_Play(Tools_RandomLCG_Range(0, 8) + 8);
                            l_timerNext = g_timerGUI + 300;
                        }
                    }
                }
            }

            GFX_Screen_SetActive(SCREEN_0);

            key = GUI_Widget_HandleEvents(g_widgetLinkedListHead);

            if (g_selectionType == SELECTIONTYPE_TARGET || g_selectionType == SELECTIONTYPE_PLACE || g_selectionType == SELECTIONTYPE_UNIT || g_selectionType == SELECTIONTYPE_STRUCTURE) {
                if (g_unitSelected != null) {
                    if (l_timerUnitStatus < g_timerGame) {
                        Unit_DisplayStatusText(g_unitSelected);
                        l_timerUnitStatus = g_timerGame + 300;
                    }

                    if (g_selectionType != SELECTIONTYPE_TARGET) {
                        g_selectionPosition = Tile_PackTile(Tile_Center(g_unitSelected.o.position));
                    }
                }

                GUI_Widget_ActionPanel_Draw(false);

                InGame_Numpad_Move(key);

                GUI_DrawCredits(g_playerHouseID, 0);

                GameLoop_Team();
                GameLoop_Unit();
                GameLoop_Structure();
                GameLoop_House();

                GUI_DrawScreen(SCREEN_0);
            }

            GUI_DisplayText(null, 0);

            if (g_running && !g_debugScenario) {
                GameLoop_LevelEnd();
            }

            if (!g_running) break;
        }

        GUI_Mouse_Hide_Safe();

        if (s_enableLog != 0) Mouse_SetMouseMode(INPUT_MOUSE_MODE_NORMAL, "DUNE.LOG");

        GUI_Mouse_Hide_Safe();

        Widget_SetCurrentWidget(0);

        GFX_Screen_SetActive(SCREEN_1);

        GFX_ClearScreen(SCREEN_1);

        GUI_Screen_FadeIn(g_curWidgetXBase, g_curWidgetYBase, g_curWidgetXBase, g_curWidgetYBase, g_curWidgetWidth, g_curWidgetHeight, SCREEN_1, SCREEN_0);
    }

    /**
     * Initialize Timer, Video, Mouse, GFX, Fonts, Random number generator
     * and current Widget
     */
    static boolean OpenDune_Init(int screen_magnification, VideoScaleFilter filter, int frame_rate) {
        if (!Font_Init()) {
            Error(
                "--------------------------\n" +
                "ERROR LOADING DATA FILE\n" +
                "\n" +
                "Did you copy the Dune2 1.07eu data files into the data directory ?\n" +
                "\n"
            );

            return false;
        }

        Timer_Init();

        if (!Video_Init(screen_magnification, filter)) return false;

        Mouse_Init();

        /* Add the general tickers */
        Timer_Add(Timer_Tick, 1000000 / 60, false);
        Timer_Add(Video_Tick, 1000000 / frame_rate, true);

        g_mouseDisabled = -1;

        GFX_Init();
        GFX_ClearScreen(SCREEN_ACTIVE);

        Font_Select(g_fontNew8p);

        g_palette_998A = calloc(256 * 3, sizeof(int));

        memset(&g_palette_998A[45], 63, 3);	/* Set color 15 to WHITE */

        GFX_SetPalette(g_palette_998A);

        Tools_RandomLCG_Seed((unsigned)time(null));

        Widget_SetCurrentWidget(0);

        return true;
    }

    /**
     * Print a IBM 437 encoded string to the system console,
     * performing conversion to the system charset.
     *
     * @param str IBM 437 code page encoded character string
     */
    static void PrintToConsole(String str) {
        int[] cp437toLatin1 = new int[] {
            0xC7, 0xFC, 0xE9, 0xE2, 0xE4, 0xE0, 0xE5, 0xE7,	/* 0x80 - 0x87 */
            0xEA, 0xEB, 0xE8, 0xEF, 0xEE, 0xEC, 0xC4, 0xC5,	/* 0x88 - 0x8f */
            0xC9, 0xE6, 0xC6, 0xF4, 0xF6, 0xF2, 0xFB, 0xF9,	/* 0x90 - 0x97 */
            0xFF, 0xD6, 0xDC, 0xA2, 0xA3, 0xA5				/* 0x98 - 0x9d */
        };

        boolean utf8_output;
        String LANG = System.getenv("LANG");

        utf8_output = "UTF-8".equals(LANG);
        for (int i = 0; i < str.length(); i ++) {
            int c = str.charAt(i);	/* IBM 437 code page character */

            if ((c & 0x80) != 0) {
                if ((c & 0x7f) < cp437toLatin1.length) {
                    c = cp437toLatin1[c & 0x7f];
                }
                if (utf8_output) {
                    System.out.print(0xc0 | (c >> 6));
                    System.out.print(0x80 | (c & 0x3f));
                } else {
                    System.out.print(c);
                }
            } else {
                System.out.print(c);
            }
        }
        System.out.print("\n");
    }

    public static int main(String[] argv) {
        boolean commit_dune_cfg = false;
        VideoScaleFilter scale_filter = FILTER_NEAREST_NEIGHBOR;
        int scaling_factor = 2;
        int frame_rate = 60;
        char[] filter_text = new char[64];

        FreeConsole();
        CrashLog_Init();

        /* Load opendune.ini file */
        Load_IniFile();

        /* set globals according to opendune.ini */
        g_dune2_enhanced = (IniFile_GetInteger("dune2_enhanced", 1) != 0) ? true : false;
        g_debugGame = (IniFile_GetInteger("debug_game", 0) != 0) ? true : false;
        g_debugScenario = (IniFile_GetInteger("debug_scenario", 0) != 0) ? true : false;
        g_debugSkipDialogs = (IniFile_GetInteger("debug_skip_dialogs", 0) != 0) ? true : false;
        s_enableLog = (int)IniFile_GetInteger("debug_log_game", 0);
        g_starPortEnforceUnitLimit = (IniFile_GetInteger("startport_unit_cap", 0) != 0) ? true : false;

        Debug("Globals :\n");
        Debug("  g_dune2_enhanced = %d\n", (int)g_dune2_enhanced);
        Debug("  g_debugGame = %d\n", (int)g_debugGame);
        Debug("  g_debugScenario = %d\n", (int)g_debugScenario);
        Debug("  g_debugSkipDialogs = %d\n", (int)g_debugSkipDialogs);
        Debug("  s_enableLog = %d\n", (int)s_enableLog);
        Debug("  g_starPortEnforceUnitLimit = %d\n", (int)g_starPortEnforceUnitLimit);

        if (!File_Init()) {
            return 1;
        }

        /* Loading config from dune.cfg */
        DuneCfg g_config = Config_Read("dune.cfg");
        if (g_config == null) {
            g_config = Config_Default();
            commit_dune_cfg = true;
        }

        /* reading config from opendune.ini which prevail over dune.cfg */
        SetLanguage_From_IniFile(g_config);

        /* Writing config to dune.cfg */
        if (commit_dune_cfg && !Config_Write("dune.cfg", g_config)) {
            Error("Error writing to dune.cfg file.\n");
            return 1;
        }

        Input_Init();

        /* if no mouse is detected, we should Input_Flags_SetBits(INPUT_FLAG_MOUSE_EMUL) */

        Drivers_All_Init();

        scaling_factor = IniFile_GetInteger("scalefactor", 2);
        if (IniFile_GetString("scalefilter", null, filter_text, sizeof(filter_text)) != null) {
            if (strcasecmp(filter_text, "nearest") == 0) {
                scale_filter = FILTER_NEAREST_NEIGHBOR;
            } else if (strcasecmp(filter_text, "scale2x") == 0) {
                scale_filter = FILTER_SCALE2X;
            } else if (strcasecmp(filter_text, "hqx") == 0) {
                scale_filter = FILTER_HQX;
            } else {
                Error("unrecognized scalefilter value '%s'\n", filter_text);
            }
        }

        frame_rate = IniFile_GetInteger("framerate", 60);

        if (!OpenDune_Init(scaling_factor, scale_filter, frame_rate)) exit(1);

        g_mouseDisabled = 0;

        GameLoop_Main();

        PrintToConsole(String_Get_ByIndex(STR_THANK_YOU_FOR_PLAYING_DUNE_II));

        PrepareEnd();
        Free_IniFile();

        return 0;
    }

    /**
     * Prepare the map (after loading scenario or savegame). Does some basic
     *  sanity-check and corrects stuff all over the place.
     */
    public static void Game_Prepare() {
        int i;

        g_validateStrictIfZero++;

        int oldSelectionType = g_selectionType;
        g_selectionType = SELECTIONTYPE_MENTAT;

        Structure_Recount();
        Unit_Recount();
        Team_Recount();

        int tOff = 0;
        for (i = 0; i < 64 * 64; i++, tOff++) {
            Unit u = Unit_Get_ByPackedTile(i);
            Structure s = Structure_Get_ByPackedTile(i);

            Tile t = g_map[tOff];
            if (u == null || !u.o.flags.used) t.hasUnit = false;
            if (s == null || !s.o.flags.used) t.hasStructure = false;
            if (t.isUnveiled) Map_UnveilTile(i, g_playerHouseID);
        }

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = HOUSE_INVALID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;

            if (u.o.flags.isNotOnMap) continue;

            Unit_RemoveFog(u);
            Unit_UpdateMap(1, u);
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;
            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;

            if (s.o.flags.isNotOnMap) continue;

            Structure_RemoveFog(s);

            if (s.o.type == STRUCTURE_STARPORT && s.o.linkedID != 0xFF) {
                Unit u = Unit_Get_ByIndex(s.o.linkedID);

                if (!u.o.flags.used || !u.o.flags.isNotOnMap) {
                    s.o.linkedID = 0xFF;
                    s.countDown = 0;
                } else {
                    Structure_SetState(s, STRUCTURE_STATE_READY);
                }
            }

            Script_Load(s.o.script, s.o.type);

            if (s.o.type == STRUCTURE_PALACE) {
                House_Get_ByIndex(s.o.houseID).palacePosition = s.o.position;
            }

            if ((House_Get_ByIndex(s.o.houseID).palacePosition.x != 0) || (House_Get_ByIndex(s.o.houseID).palacePosition.y != 0)) continue;
            House_Get_ByIndex(s.o.houseID).palacePosition = s.o.position;
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            House h = House_Find(find);
            if (h == null) break;

            h.structuresBuilt = Structure_GetStructuresBuilt(h);
            House_UpdateCreditsStorage(h.index);
            House_CalculatePowerAndCredit(h);
        }

        GUI_Palette_CreateRemap(g_playerHouseID);

        Map_SetSelection(g_selectionPosition);

        if (g_structureActiveType != 0xFFFF) {
            Map_SetSelectionSize(g_table_structureInfo[g_structureActiveType].layout);
        } else {
            Structure s = Structure_Get_ByPackedTile(g_selectionPosition);

            if (s != null) Map_SetSelectionSize(g_table_structureInfo[s.o.type].layout);
        }

        Voice_LoadVoices(g_playerHouseID);

        g_tickHousePowerMaintenance = max(g_timerGame + 70, g_tickHousePowerMaintenance);
        g_viewport_forceRedraw = true;
        g_playerCredits = 0xFFFF;

        g_selectionType = oldSelectionType;
        g_validateStrictIfZero--;
    }

    /**
     * Initialize a game, by setting most variables to zero, cleaning the map, etc
     *  etc.
     */
    static void Game_Init() {
        Unit_Init();
        Structure_Init();
        Team_Init();
        House_Init();

        Animation_Init();
        Explosion_Init();
        Arrays.fill(g_map, null);

        Arrays.fill(g_displayedViewport, 0);
        Arrays.fill(g_displayedMinimap, 0);
        Arrays.fill(g_changedTilesMap, 0));
        Arrays.fill(g_dirtyViewport, 0);
        Arrays.fill(g_dirtyMinimap, 0);

        Arrays.fill(g_mapTileID, 0);
        Arrays.fill(g_starportAvailable, 0);

        Sound_Output_Feedback(0xFFFE);

        g_playerCreditsNoSilo = 0;
        g_houseMissileCountdown = 0;
        g_selectionState = 0; /* Invalid. */
        g_structureActivePosition = 0;

        g_unitHouseMissile = null;
        g_unitActive = null;
        g_structureActive = null;

        g_activeAction = 0xFFFF;
        g_structureActiveType = 0xFFFF;

        GUI_DisplayText(null, -1);

        sleepIdle();	/* let the game a chance to update screen, etc. */
    }

    /**
     * Load a scenario in a safe way, and prepare the game.
     * @param houseID The House which is going to play the game.
     * @param scenarioID The Scenario to load.
     */
    static void Game_LoadScenario(int houseID, int scenarioID) {
        Sound_Output_Feedback(0xFFFE);

        Game_Init();

        g_validateStrictIfZero++;

        if (!Scenario_Load(scenarioID, houseID)) {
            GUI_DisplayModalMessage("No more scenarios!", 0xFFFF);

            PrepareEnd();
            System.exit(0);
        }

        Game_Prepare();

        if (scenarioID < 5) {
            g_hintsShown1 = 0;
            g_hintsShown2 = 0;
        }

        g_validateStrictIfZero--;
    }

    /**
     * Close down facilities used by the program. Always called just before the
     *  program terminates.
     */
    public static void PrepareEnd() {
        free(g_palette_998A);
        g_palette_998A = null;

        GameLoop_Uninit();

        String_Uninit();
        Sprites_Uninit();
        Font_Uninit();
        Voice_UnloadVoices();

        Drivers_All_Uninit();

        if (g_mouseFileID != 0xFF) Mouse_SetMouseMode(INPUT_MOUSE_MODE_NORMAL, null);

        File_Uninit();
        Timer_Uninit();
        GFX_Uninit();
        Video_Uninit();
    }
}
