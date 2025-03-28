package com.tsoft.dune2.house;

import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.audio.DriverService.Driver_Voice_IsPlaying;
import static com.tsoft.dune2.audio.SoundService.Sound_Output_Feedback;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_GetSize_ByIndex;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_Get_ByIndex;
import static com.tsoft.dune2.gfx.Screen.SCREEN_0;
import static com.tsoft.dune2.gfx.Screen.SCREEN_1;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.gui.ViewportService.GUI_Widget_Viewport_RedrawMap;
import static com.tsoft.dune2.house.HouseType.*;
import static com.tsoft.dune2.map.MapService.Map_FindLocationTile;
import static com.tsoft.dune2.opendune.OpenDuneService.*;
import static com.tsoft.dune2.pool.PoolHouseService.House_Find;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Find;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolUnitService.*;
import static com.tsoft.dune2.scenario.ScenarioService.g_scenario;
import static com.tsoft.dune2.strings.StringService.String_Get_ByIndex;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureService.Structure_CalculateHitpointsMax;
import static com.tsoft.dune2.structure.StructureService.g_structureIndex;
import static com.tsoft.dune2.structure.StructureType.*;
import static com.tsoft.dune2.table.TableHouseInfo.g_table_houseInfo;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.tile.TileService.Tile_UnpackTile;
import static com.tsoft.dune2.timer.TimerService.Timer_Sleep;
import static com.tsoft.dune2.timer.TimerService.g_timerGame;
import static com.tsoft.dune2.tools.IndexType.IT_STRUCTURE;
import static com.tsoft.dune2.tools.IndexType.IT_TILE;
import static com.tsoft.dune2.tools.ToolsService.*;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.*;
import static com.tsoft.dune2.wsa.WsaService.*;

public class HouseService {

    public static House g_playerHouse;
    public static int g_playerHouseID = HOUSE_INVALID;
    public static int g_houseMissileCountdown = 0;
    public static int g_playerCreditsNoSilo = 0;
    public static int g_playerCredits = 0;                /* Credits shown to player as 'current'. */
    public static long g_tickHousePowerMaintenance = 0;

    static long s_tickHouseHouse = 0;
    static long s_tickHouseStarport = 0;
    static long s_tickHouseReinforcement = 0;
    static long s_tickHouseMissileCountdown = 0;
    static long s_tickHouseStarportAvailability = 0;

    /**
     * Loop over all houses, preforming various of tasks.
     */
    public static void GameLoop_House() {
        boolean tickHouse = false;
        boolean tickPowerMaintenance = false;
        boolean tickStarport = false;
        boolean tickReinforcement = false;
        boolean tickMissileCountdown = false;
        boolean tickStarportAvailability = false;

        if (g_debugScenario) return;

        if (s_tickHouseHouse <= g_timerGame) {
            tickHouse = true;
            s_tickHouseHouse = g_timerGame + 900;
        }

        if (g_tickHousePowerMaintenance <= g_timerGame) {
            tickPowerMaintenance = true;
            g_tickHousePowerMaintenance = g_timerGame + 10800;
        }

        if (s_tickHouseStarport <= g_timerGame) {
            tickStarport = true;
            s_tickHouseStarport = g_timerGame + 180;
        }

        if (s_tickHouseReinforcement <= g_timerGame) {
            tickReinforcement = true;
            s_tickHouseReinforcement = g_timerGame + (g_debugGame ? 60 : 600);
        }

        if (s_tickHouseMissileCountdown <= g_timerGame) {
            tickMissileCountdown = true;
            s_tickHouseMissileCountdown = g_timerGame + 60;
        }

        if (s_tickHouseStarportAvailability <= g_timerGame) {
            tickStarportAvailability = true;
            s_tickHouseStarportAvailability = g_timerGame + 1800;
        }

        if (tickMissileCountdown && g_houseMissileCountdown != 0) {
            g_houseMissileCountdown--;
            Sound_Output_Feedback(g_houseMissileCountdown + 41);

            if (g_houseMissileCountdown == 0) {
                Unit_LaunchHouseMissile(Map_FindLocationTile(4, g_playerHouseID));
            }
        }

        if (tickStarportAvailability) {
            /* Pick a random unit to increase starport availability */
            int type = Tools_RandomLCG_Range(0, UNIT_MAX - 1);

            /* Increase how many of this unit is available via starport by one */
            if (g_starportAvailable[type] != 0 && g_starportAvailable[type] < 10) {
                if (g_starportAvailable[type] == -1) {
                    g_starportAvailable[type] = 1;
                } else {
                    g_starportAvailable[type]++;
                }
            }
        }

        if (tickReinforcement) {
            Unit nu = null;

            for (int i = 0; i < 16; i++) {
                if (g_scenario.reinforcement[i].unitID == UNIT_INDEX_INVALID) continue;
                if (g_scenario.reinforcement[i].timeLeft == 0) continue;
                if (--g_scenario.reinforcement[i].timeLeft != 0) continue;

                Unit u = Unit_Get_ByIndex(g_scenario.reinforcement[i].unitID);

                int locationID = g_scenario.reinforcement[i].locationID;
                boolean deployed   = false;

                if (locationID >= 4) {
                    if (nu == null) {
                        nu = Unit_Create(UNIT_INDEX_INVALID, UNIT_CARRYALL, u.o.houseID, Tile_UnpackTile(Map_FindLocationTile(Tools_Random_256() & 3, u.o.houseID)), 100);

                        if (nu != null) {
                            nu.o.flags.byScenario = true;
                            Unit_SetDestination(nu, Tools_Index_Encode(Map_FindLocationTile(locationID, u.o.houseID), IT_TILE));
                        }
                    }

                    if (nu != null) {
                        u.o.linkedID = nu.o.linkedID;
                        nu.o.linkedID = (int)u.o.index;
                        nu.o.flags.inTransport = true;
                        g_scenario.reinforcement[i].unitID = UNIT_INDEX_INVALID;
                        deployed = true;
                    } else {
                        /* Failed to create carry-all, try again in a short moment */
                        g_scenario.reinforcement[i].timeLeft = 1;
                    }
                } else {
                    deployed = Unit_SetPosition(u, Tile_UnpackTile(Map_FindLocationTile(locationID, u.o.houseID)));
                }

                if (deployed && g_scenario.reinforcement[i].repeat != 0) {
                    Tile32 tile = new Tile32();
                    tile.x = 0xFFFF;
                    tile.y = 0xFFFF;

                    g_validateStrictIfZero++;
                    u = Unit_Create(UNIT_INDEX_INVALID, u.o.type, u.o.houseID, tile, 0);
                    g_validateStrictIfZero--;

                    if (u != null) {
                        g_scenario.reinforcement[i].unitID = u.o.index;
                        g_scenario.reinforcement[i].timeLeft = g_scenario.reinforcement[i].timeBetween;
                    }
                }
            }
        }

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            House h = House_Find(find);
            if (h == null) break;

            if (tickHouse) {
                /* ENHANCEMENT -- Originally this code was outside the house loop, which seems very odd.
                 *  This problem is considered to be so bad, that the original code has been removed. */
                if (h.index != g_playerHouseID) {
                    if (h.creditsStorage < h.credits) {
                        h.credits = h.creditsStorage;
                    }
                } else {
                    int maxCredits = Math.max(h.creditsStorage, g_playerCreditsNoSilo);
                    if (h.credits > maxCredits) {
                        h.credits = maxCredits;

                        GUI_DisplayText(String_Get_ByIndex(STR_INSUFFICIENT_SPICE_STORAGE_AVAILABLE_SPICE_IS_LOST), 1);
                    }
                }

                if (h.index == g_playerHouseID) {
                    if (h.creditsStorage > g_playerCreditsNoSilo) {
                        g_playerCreditsNoSilo = 0;
                    }

                    if (g_playerCreditsNoSilo == 0 && g_campaignID > 1 && h.credits != 0) {
                        if (h.creditsStorage != 0 && ((h.credits * 256 / h.creditsStorage) > 200)) {
                            GUI_DisplayText(String_Get_ByIndex(STR_SPICE_STORAGE_CAPACITY_LOW_BUILD_SILOS), 0);
                        }
                    }

                    if (h.credits < 100 && g_playerCreditsNoSilo != 0) {
                        GUI_DisplayText(String_Get_ByIndex(STR_CREDITS_ARE_LOW_HARVEST_SPICE_FOR_MORE_CREDITS), 0);
                    }
                }
            }

            if (tickHouse) House_EnsureHarvesterAvailable(h.index);

            if (tickStarport && h.starportLinkedID != UNIT_INDEX_INVALID) {
                Unit u = null;

                h.starportTimeLeft--;
                if (h.starportTimeLeft < 0) h.starportTimeLeft = 0;

                if (h.starportTimeLeft == 0) {
                    Structure s = Structure_Get_ByIndex(g_structureIndex);
                    if (s.o.type == STRUCTURE_STARPORT && s.o.houseID == h.index) {
                        u = Unit_CreateWrapper(h.index, UNIT_FRIGATE, Tools_Index_Encode(s.o.index, IT_STRUCTURE));
                    } else {
                        PoolFindStruct find2 = new PoolFindStruct();
                        find2.houseID = h.index;
                        find2.index = 0xFFFF;
                        find2.type = STRUCTURE_STARPORT;

                        while (true) {
                            s = Structure_Find(find2);
                            if (s == null) break;
                            if (s.o.linkedID != 0xFF) continue;

                            u = Unit_CreateWrapper(h.index, UNIT_FRIGATE, Tools_Index_Encode(s.o.index, IT_STRUCTURE));
                            break;
                        }
                    }

                    if (u != null) {
                        u.o.linkedID = h.starportLinkedID;
                        h.starportLinkedID = UNIT_INDEX_INVALID;
                        u.o.flags.inTransport = true;

                        Sound_Output_Feedback(38);
                    }

                    h.starportTimeLeft = (u != null) ? g_table_houseInfo[h.index].starportDeliveryTime : 1;
                }
            }

            if (tickHouse) {
                House_CalculatePowerAndCredit(h);
                Structure_CalculateHitpointsMax(h);

                if (h.timerUnitAttack != 0) h.timerUnitAttack--;
                if (h.timerSandwormAttack != 0) h.timerSandwormAttack--;
                if (h.timerStructureAttack != 0) h.timerStructureAttack--;
                if (h.harvestersIncoming > 0 && Unit_CreateWrapper(h.index, UNIT_HARVESTER, 0) != null) {
                    h.harvestersIncoming--;
                }
            }

            if (tickPowerMaintenance) {
                int powerMaintenanceCost = (h.powerUsage / 32) + 1;
                h.credits -= Math.min(h.credits, powerMaintenanceCost);
            }
        }
    }

    /**
     * Convert the name of a house to the type value of that house, or
     *  HOUSE_INVALID if not found.
     */
    public static int House_StringToType(String name) {
        if (name == null) return HOUSE_INVALID;

        for (int index = 0; index < 6; index++) {
            if (g_table_houseInfo[index].name.equalsIgnoreCase(name)) {
                return index;
            }
        }

        return HOUSE_INVALID;
    }

    /**
     * Gives a harvester to the given house if it has a refinery and no harvesters.
     *
     * @param houseID The index of the house to give a harvester to.
     */
    static void House_EnsureHarvesterAvailable(int houseID) {
        PoolFindStruct find = new PoolFindStruct();
        find.houseID = houseID;
        find.type    = 0xFFFF;
        find.index   = 0xFFFF;

        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;

            /* ENHANCEMENT -- Dune2 checked the wrong type to skip. LinkedID is a structure for a Construction Yard */
            if (!g_dune2_enhanced && s.o.type == STRUCTURE_HEAVY_VEHICLE) continue;
            if (g_dune2_enhanced && s.o.type == STRUCTURE_CONSTRUCTION_YARD) continue;
            if (s.o.linkedID == UNIT_INVALID) continue;
            if (Unit_Get_ByIndex(s.o.linkedID).o.type == UNIT_HARVESTER) return;
        }

        find.houseID = houseID;
        find.type = UNIT_CARRYALL;
        find.index = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;
            if (u.o.linkedID == UNIT_INVALID) continue;
            if (Unit_Get_ByIndex(u.o.linkedID).o.type == UNIT_HARVESTER) return;
        }

        if (Unit_IsTypeOnMap(houseID, UNIT_HARVESTER)) return;

        find.houseID = houseID;
        find.type = STRUCTURE_REFINERY;
        find.index = 0xFFFF;

        Structure s = Structure_Find(find);
        if (s == null) return;

        if (Unit_CreateWrapper(houseID, UNIT_HARVESTER, Tools_Index_Encode(s.o.index, IT_STRUCTURE)) == null) return;

        if (houseID != g_playerHouseID) return;

        GUI_DisplayText(String_Get_ByIndex(STR_HARVESTER_IS_HEADING_TO_REFINERY), 0);
    }

    /**
     * Checks if two houses are allied.
     *
     * @param houseID1 The index of the first house.
     * @param houseID2 The index of the second house.
     * @return True if and only if the two houses are allies of eachother.
     */
    public static boolean House_AreAllied(int houseID1, int houseID2) {
        if (houseID1 == HOUSE_INVALID || houseID2 == HOUSE_INVALID) {
            return false;
        }

        if (houseID1 == houseID2) {
            return true;
        }

        if (houseID1 == HOUSE_FREMEN || houseID2 == HOUSE_FREMEN) {
            return (houseID1 == HOUSE_ATREIDES || houseID2 == HOUSE_ATREIDES);
        }

        return (houseID1 != g_playerHouseID && houseID2 != g_playerHouseID);
    }

    /**
     * Updates the radar state for the given house.
     * @param h The house.
     * @return True if and only if the radar has been activated.
     */
    public static boolean House_UpdateRadarState(House h) {
        if (h == null || h.index != g_playerHouseID) {
            return false;
        }

        boolean activate = h.flags.radarActivated;

        if (h.flags.radarActivated) {
            /* Deactivate radar */
            if ((h.structuresBuilt & (1 << STRUCTURE_OUTPOST)) == 0 || h.powerProduction < h.powerUsage) activate = false;
        } else {
            /* Activate radar */
            if ((h.structuresBuilt & (1 << STRUCTURE_OUTPOST)) != 0 && h.powerProduction >= h.powerUsage) activate = true;
        }

        if (h.flags.radarActivated == activate) {
            return false;
        }

        byte[] wsa = WSA_LoadFile("STATIC.WSA", GFX_Screen_Get_ByIndex(SCREEN_1), GFX_Screen_GetSize_ByIndex(SCREEN_1), true);
        int frameCount = WSA_GetFrameCount(wsa);

        g_textDisplayNeedsUpdate = true;

        GUI_Mouse_Hide_Safe();

        while (Driver_Voice_IsPlaying()) sleepIdle();

        Voice_Play(62);

        Sound_Output_Feedback(activate ? 28 : 29);

        frameCount = WSA_GetFrameCount(wsa);

        for (int frame = 0; frame < frameCount; frame++) {
            WSA_DisplayFrame(wsa, activate ? frameCount - frame : frame, 256, 136, SCREEN_0);
            GUI_PaletteAnimate();

            Timer_Sleep(3);
        }

        h.flags.radarActivated = activate;

        WSA_Unload(wsa);

        g_viewport_forceRedraw = true;

        GUI_Mouse_Show_Safe();

        GUI_Widget_Viewport_RedrawMap(SCREEN_0);

        return activate;
    }

    /**
     * Update the CreditsStorage by walking over all structures and checking what
     *  they can hold.
     * @param houseID The house to check the storage for.
     */
    public static void House_UpdateCreditsStorage(int houseID) {
        int oldValidateStrictIfZero = g_validateStrictIfZero;
        g_validateStrictIfZero = 0;

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = houseID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        int creditsStorage = 0;
        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;

            StructureInfo si = g_table_structureInfo[s.o.type];
            creditsStorage += si.creditsStorage;
        }

        if (creditsStorage > 32000) creditsStorage = 32000;

        House_Get_ByIndex(houseID).creditsStorage = creditsStorage;

        g_validateStrictIfZero = oldValidateStrictIfZero;
    }

    /**
     * Calculate the power usage and production, and the credits storage.
     *
     * @param h The house to calculate the numbers for.
     */
    public static void House_CalculatePowerAndCredit(House h) {
        if (h == null) return;

        h.powerUsage = 0;
        h.powerProduction = 0;
        h.creditsStorage = 0;

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = h.index;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;

            /* ENHANCEMENT -- Only count structures that are placed on the map, not ones we are building. */
            if (g_dune2_enhanced && s.o.flags.isNotOnMap) continue;

            StructureInfo si = g_table_structureInfo[s.o.type];

            h.creditsStorage += si.creditsStorage;

            /* Positive values means usage */
            if (si.powerUsage >= 0) {
                h.powerUsage += si.powerUsage;
                continue;
            }

            /* Negative value and full health means everything goes to production */
            if (s.o.hitpoints >= si.o.hitpoints) {
                h.powerProduction += -si.powerUsage;
                continue;
            }

            /* Negative value and partial health, calculate how much should go to production (capped at 50%) */
            /* ENHANCEMENT -- The 50% cap of Dune2 is silly and disagress with the GUI. If your hp is 10%, so should the production. */
            if (!g_dune2_enhanced && s.o.hitpoints <= si.o.hitpoints / 2) {
                h.powerProduction += (-si.powerUsage) / 2;
                continue;
            }
            h.powerProduction += (-si.powerUsage) * s.o.hitpoints / si.o.hitpoints;
        }

        /* Check if we are low on power */
        if (h.index == g_playerHouseID && h.powerUsage > h.powerProduction) {
            GUI_DisplayText(String_Get_ByIndex(STR_INSUFFICIENT_POWER_WINDTRAP_IS_NEEDED), 1);
        }

        /* If there are no buildings left, you lose your right on 'credits without storage' */
        if (h.index == g_playerHouseID && h.structuresBuilt == 0 && g_validateStrictIfZero == 0) {
            g_playerCreditsNoSilo = 0;
        }
    }

    static String House_GetWSAHouseFilename(int houseID) {
        String[] houseWSAFileNames = new String[] { "FHARK.WSA", "FARTR.WSA", "FORDOS.WSA" };

        if (houseID >= 3) return null;
        return houseWSAFileNames[houseID];
    }
}
