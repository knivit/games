package com.tsoft.dune2.scenario;

import com.tsoft.dune2.house.House;
import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import static com.tsoft.dune2.file.FileService.File_ReadWholeFile;
import static com.tsoft.dune2.gui.GuiService.g_minimapPosition;
import static com.tsoft.dune2.gui.GuiService.g_selectionRectanglePosition;
import static com.tsoft.dune2.house.HouseService.House_StringToType;
import static com.tsoft.dune2.house.HouseService.g_playerHouse;
import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;
import static com.tsoft.dune2.house.HouseType.HOUSE_MAX;
import static com.tsoft.dune2.ini.IniService.Ini_GetInteger;
import static com.tsoft.dune2.ini.IniService.Ini_GetString;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.opendune.OpenDuneService.*;
import static com.tsoft.dune2.pool.PoolHouseService.House_Allocate;
import static com.tsoft.dune2.pool.PoolHouseService.House_Find;
import static com.tsoft.dune2.pool.PoolStructureService.STRUCTURE_INDEX_INVALID;
import static com.tsoft.dune2.pool.PoolUnitService.UNIT_INDEX_INVALID;
import static com.tsoft.dune2.pool.PoolUnitService.Unit_Free;
import static com.tsoft.dune2.sprites.SpritesService.*;
import static com.tsoft.dune2.structure.StructureService.*;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_IDLE;
import static com.tsoft.dune2.structure.StructureType.STRUCTURE_INVALID;
import static com.tsoft.dune2.table.TableHouseInfo.g_table_houseInfo;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_INVALID;
import static com.tsoft.dune2.team.TeamService.Team_Create;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;
import static com.tsoft.dune2.tile.TileService.Tile_PackXY;
import static com.tsoft.dune2.unit.ActionType.ACTION_INVALID;
import static com.tsoft.dune2.unit.MovementType.MOVEMENT_INVALID;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.UNIT_INVALID;
import static com.tsoft.dune2.utils.CFunc.atoi;

public class ScenarioService {

    public static Scenario g_scenario;
    
    public static byte[] s_scenarioBuffer = null;

    static void Scenario_Load_General() {
        g_scenario.winFlags          = Ini_GetInteger("BASIC", "WinFlags",    0,                            s_scenarioBuffer);
        g_scenario.loseFlags         = Ini_GetInteger("BASIC", "LoseFlags",   0,                            s_scenarioBuffer);
        g_scenario.mapSeed           = Ini_GetInteger("MAP",   "Seed",        0,                            s_scenarioBuffer);
        g_scenario.timeOut           = Ini_GetInteger("BASIC", "TimeOut",     0,                            s_scenarioBuffer);
        g_minimapPosition            = Ini_GetInteger("BASIC", "TacticalPos", g_minimapPosition,            s_scenarioBuffer);
        g_selectionRectanglePosition = Ini_GetInteger("BASIC", "CursorPos",   g_selectionRectanglePosition, s_scenarioBuffer);
        g_scenario.mapScale          = Ini_GetInteger("BASIC", "MapScale",    0,                            s_scenarioBuffer);

        Ini_GetString("BASIC", "BriefPicture", "HARVEST.WSA",  g_scenario.pictureBriefing, 14, s_scenarioBuffer);
        Ini_GetString("BASIC", "WinPicture",   "WIN1.WSA",     g_scenario.pictureWin,      14, s_scenarioBuffer);
        Ini_GetString("BASIC", "LosePicture",  "LOSTBILD.WSA", g_scenario.pictureLose,     14, s_scenarioBuffer);

        g_viewportPosition  = g_minimapPosition;
        g_selectionPosition = g_selectionRectanglePosition;
    }

    static void Scenario_Load_House(int houseID) {
	    String houseName = g_table_houseInfo[houseID].name;
        String houseType;
        char buf[128];
        char *b;
        House h;

        /* Get the type of the House (CPU / Human) */
        Ini_GetString(houseName, "Brain", "NONE", buf, 127, s_scenarioBuffer);
        for (b = buf; *b != '\0'; b++) if (*b >= 'a' && *b <= 'z') *b += 'A' - 'a';
        houseType = strstr("HUMAN$CPU", buf);
        if (houseType == null) return;

        /* Create the house */
        h = House_Allocate(houseID);

        h.credits      = Ini_GetInteger(houseName, "Credits",  0, s_scenarioBuffer);
        h.creditsQuota = Ini_GetInteger(houseName, "Quota",    0, s_scenarioBuffer);
        h.unitCountMax = Ini_GetInteger(houseName, "MaxUnit", 39, s_scenarioBuffer);

        /* For 'Brain = Human' we have to set a few additional things */
        if (*houseType != 'H') return;

        h.flags.human = true;

        g_playerHouseID       = houseID;
        g_playerHouse         = h;
        g_playerCreditsNoSilo = h.credits;
    }

    static void Scenario_Load_Houses() {
        House h;
        int houseID;

        for (houseID = 0; houseID < HOUSE_MAX; houseID++) {
            Scenario_Load_House(houseID);
        }

        h = g_playerHouse;
        /* In case there was no unitCountMax in the scenario, calculate
         *  it based on values used for the AI controlled houses. */
        if (h.unitCountMax == 0) {
            PoolFindStruct find = new PoolFindStruct();
            int max;
            House h2;

            find.houseID = HOUSE_INVALID;
            find.index   = 0xFFFF;
            find.type    = 0xFFFF;

            max = 80;
            while ((h2 = House_Find(find)) != null) {
                /* Skip the human controlled house */
                if (h2.flags.human) continue;
                max -= h2.unitCountMax;
            }

            h.unitCountMax = max;
        }
    }

    static void Scenario_Load_Unit(String key, String settings) {
        int houseType, unitType, actionType;
        int orientation;
        int hitpoints;
        Tile32 position;
        Unit u;
        byte[] split;

        /* The value should have 6 values separated by a ',' */
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* First value is the House type */
        houseType = House_StringToType(settings);
        if (houseType == HOUSE_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Second value is the Unit type */
        unitType = Unit_StringToType(settings);
        if (unitType == UNIT_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Third value is the Hitpoints in percent (in base 256) */
        hitpoints = atoi(settings);

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Fourth value is the position on the map */
        position = Tile_UnpackTile(atoi(settings));

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Fifth value is orientation */
        orientation = atoi(settings));

        /* Sixth value is the current state of the unit */
        settings = split + 1;
        actionType = Unit_ActionStringToType(settings);
        if (actionType == ACTION_INVALID) return;


        u = Unit_Allocate(UNIT_INDEX_INVALID, unitType, houseType);
        if (u == null) return;
        u.o.flags.byScenario = true;

        u.o.hitpoints   = hitpoints * g_table_unitInfo[unitType].o.hitpoints / 256;
        u.o.position    = position;
        u.orientation[0].current = orientation;
        u.actionID     = actionType;
        u.nextActionID = ACTION_INVALID;

        /* In case the above function failed and we are passed campaign 2, don't add the unit */
        if (!Map_IsValidPosition(Tile_PackTile(u.o.position)) && g_campaignID > 2) {
            Unit_Free(u);
            return;
        }

        /* XXX -- There is no way this is ever possible, as the beingBuilt flag is unset by Unit_Allocate() */
        if (!u.o.flagsisNotOnMap) Unit_SetAction(u, u.actionID);

        u.o.seenByHouses = 0x00;

        Unit_HouseUnitCount_Add(u, u.o.houseID);

        Unit_SetOrientation(u, u.orientation[0].current, true, 0);
        Unit_SetOrientation(u, u.orientation[0].current, true, 1);
        Unit_SetSpeed(u, 0);
    }

    static void Scenario_Load_Structure(String key, String settings) {
        int index, houseType, structureType;
        int hitpoints, position;
        byte[] split;

        /* 'GEN' marked keys are Slabs and Walls, where the number following indicates the position on the map */
        if (strncasecmp(key, "GEN", 3) == 0) {
            /* Position on the map is in the key */
            position = atoi(key + 3);

            /* The value should have two values separated by a ',' */
            split = strchr(settings, ',');
            if (split == null) return;
		    *split = '\0';
            /* First value is the House type */
            houseType = House_StringToType(settings);
            if (houseType == HOUSE_INVALID) return;

            /* Second value is the Structure type */
            settings = split + 1;
            structureType = Structure_StringToType(settings);
            if (structureType == STRUCTURE_INVALID) return;

            Structure_Create(STRUCTURE_INDEX_INVALID, structureType, houseType, position);
            return;
        }

        /* The key should start with 'ID', followed by the index */
        index = atoi(key + 2);

        /* The value should have four values separated by a ',' */
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* First value is the House type */
        houseType = House_StringToType(settings);
        if (houseType == HOUSE_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Second value is the Structure type */
        structureType = Structure_StringToType(settings);
        if (structureType == STRUCTURE_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Third value is the Hitpoints in percent (in base 256) */
        hitpoints = atoi(settings);
        /* ENHANCEMENT -- Dune2 ignores the % hitpoints read from the scenario */
        if (!g_dune2_enhanced) hitpoints = 256;
        else if(hitpoints > 256) hitpoints = 256;
        /* this is pointless to have more than 100% hitpoint, however ONE scenario
         * file has such "bug" : SCENH006.INI
         * ID001=Ordos,Const Yard,8421,936
         * ID000=Ordos,Light Fctry,14058,1064     */

        /* Fourth value is the position of the structure */
        settings = split + 1;
        position = atoi(settings);

        /* Ensure nothing is already on the tile */
        /* XXX -- DUNE2 BUG? -- This only checks the top-left corner? Not really a safety, is it? */
        if (Structure_Get_ByPackedTile(position) != null) return;

        {
            Structure s;

            s = Structure_Create(index, structureType, houseType, position);
            if (s == null) return;

            s.o.hitpoints = hitpoints * g_table_structureInfo[s.o.type].o.hitpoints / 256;
            s.o.flags.degrades = false;
            s.state = STRUCTURE_STATE_IDLE;
        }
    }

    static void Scenario_Load_Map(String key, String settings) {
        Tile t;
        int packed;
        int value;
        char *s;
        char posY[3];

        if (*key != 'C') return;

        memcpy(posY, key + 4, 2);
        posY[2] = '\0';

        packed = Tile_PackXY(atoi(posY), atoi(key + 6)) & 0xFFF;
        t = g_map[packed];

        s = strtok(settings, ",\r\n");
        value = atoi(s);
        t.houseID        = value & 0x07;
        t.isUnveiled     = (value & 0x08) != 0 ? true : false;
        t.hasUnit        = (value & 0x10) != 0 ? true : false;
        t.hasStructure   = (value & 0x20) != 0 ? true : false;
        t.hasAnimation   = (value & 0x40) != 0 ? true : false;
        t.hasExplosion = (value & 0x80) != 0 ? true : false;

        s = strtok(null, ",\r\n");
        t.groundTileID = atoi(s) & 0x01FF;
        if (g_mapTileID[packed] != t.groundTileID) g_mapTileID[packed] |= 0x8000;

        if (!t.isUnveiled) t.overlayTileID = g_veiledTileID;
    }

    static void Scenario_Load_Map_Bloom(int packed, Tile t) {
        t.groundTileID = g_bloomTileID;
        g_mapTileID[packed] |= 0x8000;
    }

    static void Scenario_Load_Map_Field(int packed, Tile t) {
        Map_Bloom_ExplodeSpice(packed, HOUSE_INVALID);

        /* Show where a field started in the preview mode by making it an odd looking sprite */
        if (g_debugScenario) {
            t.groundTileID = 0x01FF;
        }
    }

    static void Scenario_Load_Map_Special(int packed, Tile t) {
        t.groundTileID = g_bloomTileID + 1;
        g_mapTileID[packed] |= 0x8000;
    }

    static void Scenario_Load_Reinforcement(String key, String settings) {
        int index, houseType, unitType, locationID;
        int timeBetween;
        Tile32 position = new Tile32();
        boolean repeat;
        Unit u;
        String[] split;

        index = atoi(key);

        /* The value should have 4 values separated by a ',' */
        split = settings.split(",");
        if (split.length == 0) return;

        /* First value is the House type */
        houseType = House_StringToType(split[0]);
        if (houseType == HOUSE_INVALID) return;

        /* Find the next value in the ',' separated list */
        if (split.length == 1) return;

        /* Second value is the Unit type */
        unitType = Unit_StringToType(split[1]);
        if (unitType == UNIT_INVALID) return;

        /* Find the next value in the ',' separated list */
        if (split.length == 2) return;

        /* Third value is the location of the reinforcement */
        if ("NORTH".equalsIgnoreCase(split[2])) locationID = 0;
        else if ("EAST".equalsIgnoreCase(split[2])) locationID = 1;
        else if ("SOUTH".equalsIgnoreCase(split[2])) locationID = 2;
        else if ("WEST".equalsIgnoreCase(split[2])) locationID = 3;
        else if ("AIR".equalsIgnoreCase(split[2])) locationID = 4;
        else if ("VISIBLE".equalsIgnoreCase(split[2])) locationID = 5;
        else if ("ENEMYBASE".equalsIgnoreCase(split[2])) locationID = 6;
        else if ("HOMEBASE".equalsIgnoreCase(split[2])) locationID = 7;
        else return;

        /* Fourth value is the time between reinforcement */
        timeBetween = atoi(split[3]) * 6 + 1;
        repeat = split[3].endsWith("+");
        /* ENHANCEMENT -- Dune2 makes a mistake in reading the '+', causing repeat to be always false */
        if (!g_dune2_enhanced) repeat = false;

        position.x = 0xFFFF;
        position.y = 0xFFFF;
        u = Unit_Create(UNIT_INDEX_INVALID, unitType, houseType, position, 0);
        if (u == null) return;

        g_scenario.reinforcement[index].unitID      = u.o.index;
        g_scenario.reinforcement[index].locationID  = locationID;
        g_scenario.reinforcement[index].timeLeft    = timeBetween;
        g_scenario.reinforcement[index].timeBetween = timeBetween;
        g_scenario.reinforcement[index].repeat      = repeat ? 1 : 0;
    }

    static void Scenario_Load_Team(String key, String settings) {
        int houseType, teamActionType, movementType;
        int minMembers, maxMembers;
        char *split;

        /* The value should have 5 values separated by a ',' */
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* First value is the House type */
        houseType = House_StringToType(settings);
        if (houseType == HOUSE_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Second value is the teamAction type */
        teamActionType = Team_ActionStringToType(settings);
        if (teamActionType == TEAM_ACTION_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Third value is the movement type */
        movementType = Unit_MovementStringToType(settings);
        if (movementType == MOVEMENT_INVALID) return;

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Fourth value is minimum amount of members in team */
        minMembers = atoi(settings);

        /* Find the next value in the ',' separated list */
        settings = split + 1;
        split = strchr(settings, ',');
        if (split == null) return;
	    *split = '\0';

        /* Fifth value is maximum amount of members in team */
        maxMembers = atoi(settings);

        Team_Create(houseType, teamActionType, movementType, minMembers, maxMembers);
    }

    /**
     * Initialize a unit count of the starport.
     * @param key Unit type to set.
     * @param settings Count to set.
     */
    static void Scenario_Load_Choam(String key, String settings) {
        int unitType;

        unitType = Unit_StringToType(key);
        if (unitType == UNIT_INVALID) return;

        g_starportAvailable[unitType] = atoi(settings);
    }

    static void Scenario_Load_MapParts(String key, BiConsumer<Integer, Tile> ptr) {
        char *s;
        byte[] buf = new byte[128];

        Ini_GetString("MAP", key, "", buf, 127, s_scenarioBuffer);

        s = strtok(buf, ",\r\n");
        while (s != null) {
            int packed;
            Tile t;

            packed = atoi(s);
            t = g_map[packed];

            ptr.accept(packed, t);

            s = strtok(null, ",\r\n");
        }
    }

    static void Scenario_Load_Chunk(String category, BiConsumer<String, String> ptr) {
        char *buffer = g_readBuffer;

        Ini_GetString(category, null, null, g_readBuffer, g_readBufferSize, s_scenarioBuffer);
        while (true) {
            char buf[127];

            if (*buffer == '\0') break;

            Ini_GetString(category, buffer, null, buf, 127, s_scenarioBuffer);

            ptr.accept(buffer, buf);
            buffer += strlen(buffer) + 1;
        }
    }

    public static boolean Scenario_Load(int scenarioID, int houseID) {
        int i;

        if (houseID >= HOUSE_MAX) return false;

        g_scenarioID = scenarioID;

        /* Load scenario file */
        String filename = String.format("SCEN%c%03hu.INI", g_table_houseInfo[houseID].name.charAt(0), scenarioID);
        if (!Files.exists(Path.of(filename))) return false;
        s_scenarioBuffer = File_ReadWholeFile(filename);

        g_scenario = new Scenario();

        Scenario_Load_General();
        Sprites_LoadTiles();
        Map_CreateLandscape(g_scenario.mapSeed);

        for (i = 0; i < 16; i++) {
            g_scenario.reinforcement[i].unitID = UNIT_INDEX_INVALID;
        }

        Scenario_Load_Houses();

        Scenario_Load_Chunk("UNITS", Scenario_Load_Unit);
        Scenario_Load_Chunk("STRUCTURES", Scenario_Load_Structure);
        Scenario_Load_Chunk("MAP", Scenario_Load_Map);
        Scenario_Load_Chunk("REINFORCEMENTS", Scenario_Load_Reinforcement);
        Scenario_Load_Chunk("TEAMS", Scenario_Load_Team);
        Scenario_Load_Chunk("CHOAM", Scenario_Load_Choam);

        Scenario_Load_MapParts("Bloom", Scenario_Load_Map_Bloom);
        Scenario_Load_MapParts("Field", Scenario_Load_Map_Field);
        Scenario_Load_MapParts("Special", Scenario_Load_Map_Special);

        g_tickScenarioStart = g_timerGame;

        free(s_scenarioBuffer); s_scenarioBuffer = null;
        return true;
    }
}
