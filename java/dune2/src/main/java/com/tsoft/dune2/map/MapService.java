package com.tsoft.dune2.map;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.house.House;
import com.tsoft.dune2.opendune.XYPosition;
import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;
import com.tsoft.dune2.unit.UnitInfo;

import java.util.Arrays;

import static com.tsoft.dune2.audio.SoundService.Sound_Output_Feedback;
import static com.tsoft.dune2.explosion.ExplosionService.Explosion_Start;
import static com.tsoft.dune2.explosion.ExplosionType.*;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_SetActive;
import static com.tsoft.dune2.gfx.Screen.SCREEN_0;
import static com.tsoft.dune2.gfx.Screen.SCREEN_1;
import static com.tsoft.dune2.gobject.GObjectService.Object_GetByPackedTile;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.gui.SelectionType.*;
import static com.tsoft.dune2.gui.ViewportService.GUI_Widget_Viewport_DrawTile;
import static com.tsoft.dune2.house.HouseService.House_AreAllied;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;
import static com.tsoft.dune2.house.HouseType.*;
import static com.tsoft.dune2.map.LandscapeType.*;
import static com.tsoft.dune2.opendune.OpenDuneService.*;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Find;
import static com.tsoft.dune2.pool.PoolUnitService.UNIT_INDEX_INVALID;
import static com.tsoft.dune2.pool.PoolUnitService.Unit_Find;
import static com.tsoft.dune2.scenario.ScenarioService.g_scenario;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_FOG_OF_WAR;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_LANDSCAPE;
import static com.tsoft.dune2.sprites.SpritesService.*;
import static com.tsoft.dune2.structure.StructureService.*;
import static com.tsoft.dune2.table.TableLandscapeInfo.g_table_landscapeInfo;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.structure.StructureLayout.STRUCTURE_LAYOUT_1x1;
import static com.tsoft.dune2.structure.StructureType.*;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutSize;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.table.TileDiff.g_table_mapDiff;
import static com.tsoft.dune2.table.TileDiff.g_table_tilediff;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_STAGING;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.ToolsService.*;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.MovementType.MOVEMENT_FOOT;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.*;

public class MapService {

    public static int[] g_mapTileID = new int[64 * 64];
    public static Tile[] g_map = new Tile[64 * 64];                /* All map data. */

    public static int[][] g_functions= new int[][] {
        new int[] {0, 1, 0},
        new int[] {2, 3, 0},
        new int[] {0, 1, 0}
    };

    public static int[] g_dirtyMinimap = new int[512];             /* Dirty tiles of the minimap (must be rendered again). */
    public static int[] g_displayedMinimap = new int[512];         /* Displayed part of the minimap. */
    public static int[] g_dirtyViewport = new int[512];            /* Dirty tiles of the viewport (must be rendered again). */
    static int[] g_displayedViewport = new int[512];               /* Displayed part of the viewport. */

    public static int g_changedTilesCount;                         /* Number of changed tiles in #_changedTiles. */
    public static int[] g_changedTiles = new int[200];             /* Array of positions of changed tiles. */
    public static int[] g_changedTilesMap = new int[512];          /* Bit array of changed tiles, in order not to loose changes. */

    static boolean s_debugNoExplosionDamage = false;               /* When non-zero, explosions do no damage to their surrounding. */

    public static int g_dirtyViewportCount = 0;
    public static boolean g_selectionRectangleNeedRepaint = false;

    /**
     * Map definitions.
     * Map sizes: [0] is 62x62, [1] is 32x32, [2] is 21x21.
     */
    public static MapInfo[] g_mapInfos = new MapInfo[] {
        new MapInfo( 1,  1, 62, 62),
        new MapInfo(16, 16, 32, 32),
        new MapInfo(21, 21, 21, 21)
    };

    /**
     * Move the viewport position in the given direction.
     *
     * @param direction The direction to move in.
     * @return The new viewport position.
     */
    public static int Map_MoveDirection(int direction) {
        XYPosition[] mapScrollOffset = new XYPosition[] {
            new XYPosition(0, -1), new XYPosition(1, -1), new XYPosition(1,  0), new XYPosition(1,  1),
            new XYPosition(0,  1), new XYPosition(-1,  1), new XYPosition(-1,  0), new XYPosition(-1, -1)
        };

        int x = Tile_GetPackedX(g_minimapPosition) + mapScrollOffset[direction].x;
        int y = Tile_GetPackedY(g_minimapPosition) + mapScrollOffset[direction].y;

        MapInfo mapInfo = g_mapInfos[g_scenario.mapScale];

        x = Math.max(x, mapInfo.minX);
        y = Math.max(y, mapInfo.minY);

        x = Math.min(x, mapInfo.minX + mapInfo.sizeX - 15);
        y = Math.min(y, mapInfo.minY + mapInfo.sizeY - 10);

        g_viewportPosition = Tile_PackXY(x, y);
        return g_viewportPosition;
    }

    /**
     * Sets the selection on given packed tile.
     *
     * @param packed The packed tile to set the selection on.
     */
    public static void Map_SetSelection(int packed) {
        if (g_selectionType == SELECTIONTYPE_TARGET) return;

        if (g_selectionType == SELECTIONTYPE_PLACE) {
            g_selectionState = Structure_IsValidBuildLocation(packed, g_structureActiveType);
            g_selectionPosition = packed;
            return;
        }

        if ((packed != 0xFFFF && g_map[packed].overlayTileID != g_veiledTileID) || g_debugScenario) {
            Structure s = Structure_Get_ByPackedTile(packed);
            if (s != null) {
			    StructureInfo si = g_table_structureInfo[s.o.type];
                if (s.o.houseID == g_playerHouseID && g_selectionType != SELECTIONTYPE_MENTAT) {
                    GUI_DisplayHint(si.o.hintStringID, si.o.spriteID);
                }

                packed = Tile_PackTile(s.o.position);

                Map_SetSelectionSize(si.layout);

                Structure_UpdateMap(s);
            } else {
                Map_SetSelectionSize(STRUCTURE_LAYOUT_1x1);
            }

            if (g_selectionType != SELECTIONTYPE_TARGET) {
                Unit u = Unit_Get_ByPackedTile(packed);
                if (u != null) {
                    if (u.o.type != UNIT_CARRYALL) {
                        Unit_Select(u);
                    }
                } else {
                    if (g_unitSelected != null) {
                        Unit_Select(null);
                    }
                }
            }

            g_selectionPosition = packed;
            return;
        }

        Map_SetSelectionSize(STRUCTURE_LAYOUT_1x1);
        g_selectionPosition = packed;
    }

    /**
     * Sets the selection size for the given layout.
     *
     * @param layout The layout to determine selection size from.
     * @return The previous layout.
     */
    public static int Map_SetSelectionSize(int layout) {
        int oldLayout = 0;
        if ((layout & 0x80) != 0) return oldLayout;

        int oldPosition = Map_SetSelectionObjectPosition(0xFFFF);

        g_selectionWidth  = g_table_structure_layoutSize[layout].width;
        g_selectionHeight = g_table_structure_layoutSize[layout].height;

        Map_SetSelectionObjectPosition(oldPosition);

        return oldLayout;
    }

    static void Map_InvalidateSelection(int packed, boolean enable) {
        if (packed == 0xFFFF) return;

        for (int y = 0; y < g_selectionHeight; y++) {
            for (int x = 0; x < g_selectionWidth; x++) {
                int curPacked = packed + Tile_PackXY(x, y);

                Map_Update(curPacked, 0, false);

                if (enable) {
                    BitArray_Set(g_displayedViewport, curPacked);
                } else {
                    BitArray_Clear(g_displayedViewport, curPacked);
                }
            }
        }
    }

    private static int selectionPosition = 0xFFFF;

    /**
     * Sets the selection object to the given position.
     *
     * @param packed The position to set.
     * @return The previous position.
     */
    public static int Map_SetSelectionObjectPosition(int packed) {
        int oldPacked = selectionPosition;

        if (packed == oldPacked) return oldPacked;

        Map_InvalidateSelection(oldPacked, false);

        if (packed != 0xFFFF) Map_InvalidateSelection(packed, true);

        selectionPosition = packed;

        return oldPacked;
    }

    /* Border tiles of the viewport relative to the top-left. */
    private static int[] viewportBorder = new int[] {
        0x0000, 0x0001, 0x0002, 0x0003, 0x0004, 0x0005, 0x0006, 0x0007, 0x0008, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x000E,
        0x0040, 0x004E,
        0x0080, 0x008E,
        0x00C0, 0x00CE,
        0x0100, 0x010E,
        0x0140, 0x014E,
        0x0180, 0x018E,
        0x01C0, 0x01CE,
        0x0200, 0x020E,
        0x0240, 0x0241, 0x0242, 0x0243, 0x0244, 0x0245, 0x0246, 0x0247, 0x0248, 0x0249, 0x024A, 0x024B, 0x024C, 0x024D, 0x024E,
        0xFFFF
    };

    private static int minimapPreviousPosition = 0;

    /**
     * Update the minimap position.
     *
     * @param packed The new position.
     * @param forceUpdate If true force the update even if the position didn't change.
     */
    public static void Map_UpdateMinimapPosition(int packed, boolean forceUpdate) {
        if (packed != 0xFFFF && packed == minimapPreviousPosition && !forceUpdate) return;
        if (g_selectionType == SELECTIONTYPE_MENTAT) return;

        int oldScreenID = GFX_Screen_SetActive(SCREEN_1);

        boolean cleared = false;

        if (minimapPreviousPosition != 0xFFFF && minimapPreviousPosition != packed) {
            cleared = true;

            for (int mOff = 0; viewportBorder[mOff] != 0xFFFF; mOff ++) {
                int curPacked = minimapPreviousPosition + viewportBorder[mOff];
                BitArray_Clear(g_displayedMinimap, curPacked);

                GUI_Widget_Viewport_DrawTile(curPacked);
            }
        }

        if (packed != 0xFFFF && (packed != minimapPreviousPosition || forceUpdate)) {
            int mapScale = g_scenario.mapScale;
            MapInfo mapInfo = g_mapInfos[mapScale];

            int left = (Tile_GetPackedX(packed) - mapInfo.minX) * (mapScale + 1) + 256;
            int right = left + mapScale * 15 + 14;
            int top = (Tile_GetPackedY(packed) - mapInfo.minY) * (mapScale + 1) + 136;
            int bottom = top + mapScale * 10 + 9;

            GUI_DrawWiredRectangle(left, top, right, bottom, (byte)15);

            for (int mOff = 0; viewportBorder[mOff] != 0xFFFF; mOff ++) {
                int curPacked = packed + viewportBorder[mOff];
                BitArray_Set(g_displayedMinimap, curPacked);
            }
        }

        if (cleared && oldScreenID == SCREEN_0) {
            GUI_Mouse_Hide_Safe();
            GUI_Screen_Copy(32, 136, 32, 136, 8, 64, SCREEN_1, SCREEN_0);
            GUI_Mouse_Show_Safe();
        }

        GFX_Screen_SetActive(oldScreenID);

        minimapPreviousPosition = packed;
    }

    /**
     * Checks if the given position is inside the map.
     *
     * @param position The tile (packed) to check.
     * @return True if the position is valid.
     */
    public static boolean Map_IsValidPosition(int position) {
        if ((position & 0xC000) != 0) return false;

        int x = Tile_GetPackedX(position);
        int y = Tile_GetPackedY(position);

        MapInfo mapInfo = g_mapInfos[g_scenario.mapScale];

        return (mapInfo.minX <= x && x < (mapInfo.minX + mapInfo.sizeX) && mapInfo.minY <= y && y < (mapInfo.minY + mapInfo.sizeY));
    }

    /**
     * Check if a position is unveiled (the fog is removed).
     *
     * @param position For which position to check.
     * @return True if and only if the position is unveiled.
     */
    public static boolean Map_IsPositionUnveiled(int position) {
        if (g_debugScenario) return true;

        Tile t = g_map[position];

        if (!t.isUnveiled) return false;
        if (!Tile_IsUnveiled(t.overlayTileID)) return false;

        return true;
    }

    /**
     * Check if a position is in the viewport.
     *
     * @param position For which position to check.
     * @return True if and only if the position is in the viewport.
     */
    public static boolean Map_IsPositionInViewport(Tile32 position, Tile32 xy) {
        int x = (position.x >> 4) - (Tile_GetPackedX(g_viewportPosition) << 4);
        int y = (position.y >> 4) - (Tile_GetPackedY(g_viewportPosition) << 4);

        xy.x = x;
        xy.y = y;

        return x >= -16 && x <= 256 && y >= -16 && y <= 176;
    }

    static boolean Map_UpdateWall(int packed) {
        if (Map_GetLandscapeType(packed) != LST_WALL) return false;

        Tile t = g_map[packed];

        t.groundTileID = g_mapTileID[packed] & 0x1FF;

        if (Map_IsPositionUnveiled(packed)) t.overlayTileID = g_wallTileID;

        Structure_ConnectWall(packed, true);
        Map_Update(packed, 0, false);

        return true;
    }

    /**
     * Make an explosion on the given position, of a certain type. All units in the
     *  neighbourhood get an amount of damage related to their distance to the
     *  explosion.
     * @param type The type of explosion.
     * @param position The position of the explosion.
     * @param hitpoints The amount of hitpoints to give people in the neighbourhoud.
     * @param unitOriginEncoded The unit that fired the bullet.
     */
    public static void Map_MakeExplosion(int type, Tile32 position, int hitpoints, int unitOriginEncoded) {
        int reactionDistance = (type == EXPLOSION_DEATH_HAND) ? 32 : 16;
        int positionPacked = Tile_PackTile(position);

        if (!s_debugNoExplosionDamage && hitpoints != 0) {
            PoolFindStruct find = new PoolFindStruct();
            find.houseID = HOUSE_INVALID;
            find.index = 0xFFFF;
            find.type = 0xFFFF;

            while (true) {
                Unit u = Unit_Find(find);
                if (u == null) break;

                UnitInfo ui = g_table_unitInfo[u.o.type];

                int distance = Tile_GetDistance(position, u.o.position) >> 4;
                if (distance >= reactionDistance) continue;

                if (!(u.o.type == UNIT_SANDWORM && type == EXPLOSION_SANDWORM_SWALLOW) && u.o.type != UNIT_FRIGATE) {
                    Unit_Damage(u, hitpoints >> (distance >> 2), 0);
                }

                if (u.o.houseID == g_playerHouseID) continue;

                Unit us = Tools_Index_GetUnit(unitOriginEncoded);
                if (us == null) continue;
                if (us == u) continue;
                if (House_AreAllied(Unit_GetHouseID(u), Unit_GetHouseID(us))) continue;

                Team t = Unit_GetTeam(u);
                if (t != null) {
				    UnitInfo targetInfo;
                    Unit target;

                    if (t.action == TEAM_ACTION_STAGING) {
                        Unit_RemoveFromTeam(u);
                        Unit_SetAction(u, ACTION_HUNT);
                        continue;
                    }

                    target = Tools_Index_GetUnit(t.target);
                    if (target == null) continue;

                    targetInfo = g_table_unitInfo[target.o.type];
                    if (targetInfo.bulletType == UNIT_INVALID) t.target = unitOriginEncoded;
                    continue;
                }

                if (u.o.type == UNIT_HARVESTER) {
				    UnitInfo uis = g_table_unitInfo[us.o.type];

                    if (uis.movementType == MOVEMENT_FOOT && u.targetMove == 0) {
                        if (u.actionID != ACTION_MOVE) Unit_SetAction(u, ACTION_MOVE);
                        u.targetMove = unitOriginEncoded;
                        continue;
                    }
                }

                if (ui.bulletType == UNIT_INVALID) continue;

                if (u.actionID == ACTION_GUARD && u.o.flags.byScenario) {
                    Unit_SetAction(u, ACTION_HUNT);
                }

                if (u.targetAttack != 0 && u.actionID != ACTION_HUNT) continue;

                Unit attack = Tools_Index_GetUnit(u.targetAttack);
                if (attack != null) {
                    int packed = Tile_PackTile(u.o.position);
                    if (Tile_GetDistancePacked(Tools_Index_GetPackedTile(u.targetAttack), packed) <= ui.fireDistance) continue;
                }

                Unit_SetTarget(u, unitOriginEncoded);
            }
        }

        if (!s_debugNoExplosionDamage && hitpoints != 0) {
            Structure s = Structure_Get_ByPackedTile(positionPacked);

            if (s != null) {
                if (type == EXPLOSION_IMPACT_LARGE) {
				    StructureInfo si = g_table_structureInfo[s.o.type];

                    if (si.o.hitpoints / 2 > s.o.hitpoints) {
                        type = EXPLOSION_SMOKE_PLUME;
                    }
                }

                Structure_HouseUnderAttack(s.o.houseID);
                Structure_Damage(s, hitpoints, 0);
            }
        }

        if (Map_GetLandscapeType(positionPacked) == LST_WALL && hitpoints != 0) {
            if ((g_table_structureInfo[STRUCTURE_WALL].o.hitpoints <= hitpoints) ||
                (Tools_Random_256() <= (hitpoints * 256 / g_table_structureInfo[STRUCTURE_WALL].o.hitpoints))) {
                Map_UpdateWall(positionPacked);
            }
        }

        Explosion_Start(type, position);
    }

    /**
     * Type of landscape for the landscape sprites.
     *
     * 0=normal sand, 1=partial rock, 5=mostly rock, 4=entirely rock,
     * 3=partial sand dunes, 2=entirely sand dunes, 7=partial mountain,
     * 6=entirely mountain, 8=spice, 9=thick spice
     * @see Map_GetLandscapeType
     */
    static int[] _landscapeSpriteMap = new int[] {
        0, 1, 1, 1, 5, 1, 5, 5, 5, 5, /* Sprites 127-136 */
        5, 5, 5, 5, 5, 5, 4, 3, 3, 3, /* Sprites 137-146 */
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, /* Sprites 147-156 */
        3, 3, 2, 7, 7, 7, 7, 7, 7, 7, /* Sprites 157-166 */
        7, 7, 7, 7, 7, 7, 7, 7, 6, 8, /* Sprites 167-176 */
        8, 8, 8, 8, 8, 8, 8, 8, 8, 8, /* Sprites 177-186 */
        8, 8, 8, 8, 8, 9, 9, 9, 9, 9, /* Sprites 187-196 */
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, /* Sprites 197-206 */
        9,                            /* Sprite  207 (bloom sprites 208 and 209 are already caught). */
    };

    /**
     * Get type of landscape of a tile.
     *
     * @param packed The packed tile to examine.
     * @return The type of landscape at the tile.
     */
    public static int Map_GetLandscapeType(int packed) {
        Tile t = g_map[packed];

        if (t.groundTileID == g_builtSlabTileID) return LST_CONCRETE_SLAB;

        if (t.groundTileID == g_bloomTileID || t.groundTileID == g_bloomTileID + 1) return LST_BLOOM_FIELD;

        if (t.groundTileID > g_wallTileID && (int)t.groundTileID < g_wallTileID + 75) return LST_WALL;

        if (t.overlayTileID == g_wallTileID) return LST_DESTROYED_WALL;

        if (Structure_Get_ByPackedTile(packed) != null) return LST_STRUCTURE;

        int spriteOffset = t.groundTileID - g_landscapeTileID; /* Offset in the landscape icon group. */
        if (spriteOffset < 0 || spriteOffset > 80) return LST_ENTIRELY_ROCK;

        return _landscapeSpriteMap[spriteOffset];
    }

    /**
     * Checks wether a packed tile is visible in the viewport.
     *
     * @param packed The packed tile.
     * @return True if the tile is visible.
     */
    static boolean Map_IsTileVisible(int packed) {
        int x = Tile_GetPackedX(packed);
        int y = Tile_GetPackedY(packed);
        int x2 = Tile_GetPackedX(g_minimapPosition);
        int y2 = Tile_GetPackedY(g_minimapPosition);

        return x >= x2 && x < x2 + 15 && y >= y2 && y < y2 + 10;
    }

    /**
     * Updates ??.
     *
     * @param packed The packed tile.
     * @param type The type of update.
     * @param ignoreInvisible Wether to ignore tile visibility check.
     */
    public static void Map_Update(int packed, int type, boolean ignoreInvisible) {
        int[] offsets = new int[] {
            -64, /* up */
            -63, /* up right */
            1,   /* right */
            65,  /* down rigth */
            64,  /* down */
            63,  /* down left */
            -1,  /* left */
            -65, /* up left */
            0
        };

        if (!ignoreInvisible && !Map_IsTileVisible(packed)) return;

        switch (type) {
            default:
            case 0: {
                int curPacked;

                if (BitArray_Test(g_dirtyMinimap, packed)) return;

                g_dirtyViewportCount++;

                for (int i = 0; i < 9; i++) {
                    curPacked = (packed + offsets[i]) & 0xFFF;
                    BitArray_Set(g_dirtyViewport, curPacked);
                    if (BitArray_Test(g_displayedViewport, curPacked)) g_selectionRectangleNeedRepaint = true;
                }

                BitArray_Set(g_dirtyMinimap, curPacked);
                return;
            }

            case 1:
            case 2:
            case 3:
                BitArray_Set(g_dirtyViewport, packed);
                return;
        }
    }

    /**
     * Make a deviator missile explosion on the given position, of a certain type. All units in the
     *  given radius may become deviated.
     * @param type The type of explosion.
     * @param position The position of the explosion.
     * @param radius The radius.
     * @param houseID House controlling the deviator.
     */
    public static void Map_DeviateArea(int type, Tile32 position, int radius, int houseID) {
        PoolFindStruct find = new PoolFindStruct();

        Explosion_Start(type, position);

        find.type    = 0xFFFF;
        find.index   = 0xFFFF;
        find.houseID = HOUSE_INVALID;

        while (true) {
            Unit u = Unit_Find(find);

            if (u == null) break;
            if (Tile_GetDistance(position, u.o.position) / 16 >= radius) continue;

            Unit_Deviate(u, 0, houseID);
        }
    }

    /**
     * Perform a bloom explosion, filling the area with spice.
     * @param packed Center position.
     * @param houseID %House causing the explosion.
     */
    public static void Map_Bloom_ExplodeSpice(int packed, int houseID) {
        if (g_validateStrictIfZero == 0) {
            Unit_Remove(Unit_Get_ByPackedTile(packed));
            g_map[packed].groundTileID = g_mapTileID[packed] & 0x1FF;
            Map_MakeExplosion(EXPLOSION_SPICE_BLOOM_TREMOR, Tile_UnpackTile(packed), 0, 0);
        }

        if (houseID == g_playerHouseID) {
            Sound_Output_Feedback(36);
        }

        Map_FillCircleWithSpice(packed, 5);
    }

    /**
     * Fill a circular area with spice.
     * @param packed Center position of the area.
     * @param radius Radius of the circle.
     */
    public static void Map_FillCircleWithSpice(int packed, int radius) {
        if (radius == 0) return;

        int x = Tile_GetPackedX(packed);
        int y = Tile_GetPackedY(packed);

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int curPacked = Tile_PackXY(x + j, y + i);
                int distance  = Tile_GetDistancePacked(packed, curPacked);

                if (distance > radius) continue;

                if (distance == radius && (Tools_Random_256() & 1) == 0) continue;

                if (Map_GetLandscapeType(curPacked) == LST_SPICE) continue;

                Map_ChangeSpiceAmount(curPacked, 1);

                if (g_debugScenario) {
                    Map_MarkTileDirty(curPacked);
                }
            }
        }

        Map_ChangeSpiceAmount(packed, 1);
    }

    /**
     * Fixes edges of spice / thick spice to show sand / normal spice for better looks.
     * @param packed Position to check and possible fix edges of.
     */
    static void Map_FixupSpiceEdges(int packed) {
        packed &= 0xFFF;
        int type = Map_GetLandscapeType(packed);
        int spriteID = 0;

        if (type == LST_SPICE || type == LST_THICK_SPICE) {
            for (int i = 0; i < 4; i++) {
			    int curPacked = packed + g_table_mapDiff[i];
                int curType;

                if (Tile_IsOutOfMap(curPacked)) {
                    if (type == LST_SPICE || type == LST_THICK_SPICE) spriteID |= (1 << i);
                    continue;
                }

                curType = Map_GetLandscapeType(curPacked);

                if (type == LST_SPICE) {
                    if (curType == LST_SPICE || curType == LST_THICK_SPICE) spriteID |= (1 << i);
                    continue;
                }

                if (curType == LST_THICK_SPICE) spriteID |= (1 << i);
            }

            spriteID += (type == LST_SPICE) ? 49 : 65;

            spriteID = g_iconMap[g_iconMap[ICM_ICONGROUP_LANDSCAPE] + spriteID] & 0x1FF;
            g_mapTileID[packed] = 0x8000 | spriteID;
            g_map[packed].groundTileID = spriteID;
        }

        Map_Update(packed, 0, false);
    }

    /**
     * Change amount of spice at a packed position.
     * @param packed Position in the world to modify.
     * @param dir Direction of change, > 0 means add spice, < 0 means remove spice.
     */
    public static void Map_ChangeSpiceAmount(int packed, int dir) {
        if (dir == 0) return;

        int type = Map_GetLandscapeType(packed);

        if (type == LST_THICK_SPICE && dir > 0) return;
        if (type != LST_SPICE && type != LST_THICK_SPICE && dir < 0) return;
        if (type != LST_NORMAL_SAND && type != LST_ENTIRELY_DUNE && type != LST_SPICE && dir > 0) return;

        if (dir > 0) {
            type = (type == LST_SPICE) ? LST_THICK_SPICE : LST_SPICE;
        } else {
            type = (type == LST_THICK_SPICE) ? LST_SPICE : LST_NORMAL_SAND;
        }

        int spriteID = 0;
        if (type == LST_SPICE) spriteID = 49;
        if (type == LST_THICK_SPICE) spriteID = 65;

        spriteID = g_iconMap[g_iconMap[ICM_ICONGROUP_LANDSCAPE] + spriteID] & 0x1FF;
        g_mapTileID[packed] = 0x8000 | spriteID;
        g_map[packed].groundTileID = spriteID;

        Map_FixupSpiceEdges(packed);
        Map_FixupSpiceEdges(packed + 1);
        Map_FixupSpiceEdges(packed - 1);
        Map_FixupSpiceEdges(packed - 64);
        Map_FixupSpiceEdges(packed + 64);
    }

    /**
     * Sets the viewport position.
     *
     * @param packed The packed position.
     */
    public static void Map_SetViewportPosition(int packed) {
        int x = Tile_GetPackedX(packed) - 7;
        int y = Tile_GetPackedY(packed) - 5;

        MapInfo mapInfo = g_mapInfos[g_scenario.mapScale];

        x = Math.max(mapInfo.minX, Math.min(mapInfo.minX + mapInfo.sizeX - 15, x));
        y = Math.max(mapInfo.minY, Math.min(mapInfo.minY + mapInfo.sizeY - 10, y));

        g_viewportPosition = Tile_PackXY(x, y);
    }

    /**
     * A unit drove over a special bloom, which can either give credits, a friendly
     *  Trike, an enemy Trike, or an enemy Infantry.
     * @param packed The tile where the bloom is on.
     * @param houseID The HouseID that is driving over the bloom.
     */
    public static void Map_Bloom_ExplodeSpecial(int packed, int houseID) {
        House h = House_Get_ByIndex(houseID);

        g_map[packed].groundTileID = g_landscapeTileID;
        g_mapTileID[packed] = 0x8000 | g_landscapeTileID;

        Map_Update(packed, 0, false);

        int enemyHouseID = houseID;

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        /* Find a house that belongs to the enemy */
        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;

            if (u.o.houseID == houseID) continue;

            enemyHouseID = u.o.houseID;
            break;
        }

        switch (Tools_Random_256() & 0x3) {
            case 0:
                h.credits += Tools_RandomLCG_Range(150, 400);
                break;

            case 1: {
                Tile32 position = Tile_UnpackTile(packed);

                position = Tile_MoveByRandom(position, 16, true);

                /* ENHANCEMENT -- Dune2 inverted houseID and typeID arguments. */
                Unit_Create(UNIT_INDEX_INVALID, UNIT_TRIKE, houseID, position, Tools_Random_256());
                break;
            }

            case 2: {
                Tile32 position = Tile_UnpackTile(packed);

                position = Tile_MoveByRandom(position, 16, true);

                /* ENHANCEMENT -- Dune2 inverted houseID and typeID arguments. */
                Unit u = Unit_Create(UNIT_INDEX_INVALID, UNIT_TRIKE, enemyHouseID, position, Tools_Random_256());

                if (u != null) Unit_SetAction(u, ACTION_HUNT);
                break;
            }

            case 3: {
                Tile32 position = Tile_UnpackTile(packed);

                position = Tile_MoveByRandom(position, 16, true);

                /* ENHANCEMENT -- Dune2 inverted houseID and typeID arguments. */
                Unit u = Unit_Create(UNIT_INDEX_INVALID, UNIT_INFANTRY, enemyHouseID, position, Tools_Random_256());

                if (u != null) Unit_SetAction(u, ACTION_HUNT);
                break;
            }

            default: break;
        }
    }

    /**
     * Find a tile close to a LocationID described position (North, Enemy Base, ..).
     *
     * @param locationID Value between 0 and 7 to indicate where the tile should be.
     * @param houseID The HouseID looking for a tile (to get an idea of Enemy Base).
     * @return The tile requested.
     */
    public static int Map_FindLocationTile(int locationID, int houseID) {
        int[] mapBase = new int[] {1, -2, -2};
	    MapInfo mapInfo = g_mapInfos[g_scenario.mapScale];
	    int mapOffset = mapBase[g_scenario.mapScale];

        int ret = 0;

        if (locationID == 6) { /* Enemy Base */
            PoolFindStruct find = new PoolFindStruct();
            find.houseID = HOUSE_INVALID;
            find.index = 0xFFFF;
            find.type = 0xFFFF;

            /* Find the house of an enemy */
            while (true) {
                Structure s = Structure_Find(find);
                if (s == null) break;
                if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;

                if (s.o.houseID == houseID) continue;

                houseID = s.o.houseID;
                break;
            }
        }

        while (ret == 0) {
            switch (locationID) {
                case 0: /* North */
                    ret = Tile_PackXY(mapInfo.minX + Tools_RandomLCG_Range(0, mapInfo.sizeX - 2), mapInfo.minY + mapOffset);
                    break;

                case 1: /* East */
                    ret = Tile_PackXY(mapInfo.minX + mapInfo.sizeX - mapOffset, mapInfo.minY + Tools_RandomLCG_Range(0, mapInfo.sizeY - 2));
                    break;

                case 2: /* South */
                    ret = Tile_PackXY(mapInfo.minX + Tools_RandomLCG_Range(0, mapInfo.sizeX - 2), mapInfo.minY + mapInfo.sizeY - mapOffset);
                    break;

                case 3: /* West */
                    ret = Tile_PackXY(mapInfo.minX + mapOffset, mapInfo.minY + Tools_RandomLCG_Range(0, mapInfo.sizeY - 2));
                    break;

                case 4: /* Air */
                    ret = Tile_PackXY(mapInfo.minX + Tools_RandomLCG_Range(0, mapInfo.sizeX), mapInfo.minY + Tools_RandomLCG_Range(0, mapInfo.sizeY));
                    if (houseID == g_playerHouseID && !Map_IsValidPosition(ret)) ret = 0;
                    break;

                case 5: /* Visible */
                    ret = Tile_PackXY(Tile_GetPackedX(g_minimapPosition) + Tools_RandomLCG_Range(0, 14), Tile_GetPackedY(g_minimapPosition) + Tools_RandomLCG_Range(0, 9));
                    if (houseID == g_playerHouseID && !Map_IsValidPosition(ret)) ret = 0;
                    break;

                case 6: /* Enemy Base */
                case 7: { /* Home Base */
                    PoolFindStruct find = new PoolFindStruct();
                    find.houseID = houseID;
                    find.index   = 0xFFFF;
                    find.type    = 0xFFFF;

                    Structure s = Structure_Find(find);

                    if (s != null) {
                        Tile32 unpacked = Tile_MoveByRandom(s.o.position, 120, true);
                        ret = Tile_PackTile(unpacked);
                    } else {
                        find.houseID = houseID;
                        find.index   = 0xFFFF;
                        find.type    = 0xFFFF;

                        Unit u = Unit_Find(find);

                        if (u != null) {
                            Tile32 unpacked = Tile_MoveByRandom(u.o.position, 120, true);
                            ret = Tile_PackTile(unpacked);
                        } else {
                            ret = Tile_PackXY(mapInfo.minX + Tools_RandomLCG_Range(0, mapInfo.sizeX), mapInfo.minY + Tools_RandomLCG_Range(0, mapInfo.sizeY));
                        }
                    }

                    if (houseID == g_playerHouseID && !Map_IsValidPosition(ret)) ret = 0;
                    break;
                }

                default: return 0;
            }

            ret &= 0xFFF;
            if (ret != 0 && Object_GetByPackedTile(ret) != null) ret = 0;
        }

        return ret;
    }

    /**
     * Around a position, run a certain function for all tiles within a certain radius.
     *
     * @note Radius is in a 1/4th of a tile unit.
     *
     * @param radius The radius of the to-update tiles.
     * @param position The position to go from.
     * @param unit The unit to update for (can be null if function < 2).
     * @param function The function to call.
     */
    public static void Map_UpdateAround(int radius, Tile32 position, Unit unit, int function) {
        int[] tileOffsets = new int[] {
            0x0080, 0x0088, 0x0090, 0x0098,
            0x00A0, 0x00A8, 0x00B0, 0x00B8,
            0x00C0, 0x00C8, 0x00D0, 0x00D8,
            0x00E0, 0x00E8, 0x00F0, 0x00F8,
            0x0100, 0x0180
        };

        Tile32 diff = new Tile32();
        int lastPacked;

        if (radius == 0 || (position.x == 0 && position.y == 0)) return;

        radius--;

        /* If radius is bigger or equal than 32, update all tiles in a 5x5 grid around the unit. */
        if (radius >= 32) {
            int x = Tile_GetPosX(position);
            int y = Tile_GetPosY(position);

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    int curPacked;

                    if (x + i < 0 || x + i >= 64 || y + j < 0 || y + j >= 64) continue;

                    curPacked = Tile_PackXY(x + i, y + j);
                    BitArray_Set(g_dirtyViewport, curPacked);
                    g_dirtyViewportCount++;

                    switch (function) {
                        case 0: Map_Update(curPacked, 0, false); break;
                        case 1: Map_Update(curPacked, 3, false); break;
                        case 2: Unit_RemoveFromTile(unit, curPacked); break;
                        case 3: Unit_AddToTile(unit, curPacked); break;
                        default: break;
                    }
                }
            }
            return;
        }

        radius = Math.max(radius, 15);
        position.x -= tileOffsets[radius - 15];
        position.y -= tileOffsets[radius - 15];

        diff.x = 0;
        diff.y = 0;
        lastPacked = 0;

        int i = 0;
        do {
            Tile32 curTile = Tile_AddTileDiff(position, diff);

            if (Tile_IsValid(curTile)) {
                int curPacked = Tile_PackTile(curTile);

                if (curPacked != lastPacked) {
                    BitArray_Set(g_dirtyViewport, curPacked);
                    g_dirtyViewportCount++;

                    switch (function) {
                        case 0: Map_Update(curPacked, 0, false); break;
                        case 1: Map_Update(curPacked, 3, false); break;
                        case 2: Unit_RemoveFromTile(unit, curPacked); break;
                        case 3: Unit_AddToTile(unit, curPacked); break;
                        default: break;
                    }

                    lastPacked = curPacked;
                }
            }

            if (i == 8) break;

            diff = g_table_tilediff[radius + 1][i++];
        } while ((diff.x != 0) || (diff.y != 0));
    }

    /**
     * Search for spice around a position. Thick spice is preferred if it is not too far away.
     * @param packed Center position.
     * @param radius Radius of the search.
     * @return Best position with spice, or 0 if no spice found.
     */
    public static int Map_SearchSpice(int packed, int radius) {
        int radius1 = radius + 1;
        int radius2 = radius + 1;
        int packed1 = packed;
        int packed2 = packed;

        boolean found = false;

        MapInfo mapInfo = g_mapInfos[g_scenario.mapScale];

        int xmin = Math.max(Tile_GetPackedX(packed) - radius, mapInfo.minX);
        int xmax = Math.min(Tile_GetPackedX(packed) + radius, mapInfo.minX + mapInfo.sizeX - 1);
        int ymin = Math.max(Tile_GetPackedY(packed) - radius, mapInfo.minY);
        int ymax = Math.min(Tile_GetPackedY(packed) + radius, mapInfo.minY + mapInfo.sizeY - 1);

        for (int y = ymin; y <= ymax; y++) {
            for (int x = xmin; x <= xmax; x++) {
                int curPacked = Tile_PackXY(x, y);

                if (!Map_IsValidPosition(curPacked)) continue;
                if (g_map[curPacked].hasStructure) continue;
                if (Unit_Get_ByPackedTile(curPacked) != null) continue;

                int type = Map_GetLandscapeType(curPacked);
                int distance = Tile_GetDistancePacked(curPacked, packed);

                if (type == LST_THICK_SPICE && distance < 4) {
                    found = true;

                    if (distance <= radius2) {
                        radius2 = distance;
                        packed2 = curPacked;
                    }
                }

                if (type == LST_SPICE) {
                    found = true;

                    if (distance <= radius1) {
                        radius1 = distance;
                        packed1 = curPacked;
                    }
                }
            }
        }

        if (!found) return 0;

        return (radius2 <= radius) ? packed2 : packed1;
    }

    static void Map_SelectNext(boolean getNext) {
        PoolFindStruct find = new PoolFindStruct();
        GObject selected = null;
        GObject previous = null;
        GObject next = null;
        GObject first = null;
        GObject last = null;
        boolean hasPrevious = false;
        boolean hasNext = false;

        if (g_unitSelected != null) {
            if (Map_IsTileVisible(Tile_PackTile(g_unitSelected.o.position))) selected = g_unitSelected.o;
        } else {
            Structure s = Structure_Get_ByPackedTile(g_selectionPosition);

            if (s != null && Map_IsTileVisible(Tile_PackTile(s.o.position))) selected = s.o;
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;

            if (!g_table_unitInfo[u.o.type].o.flags.tabSelectable) continue;

            if (!Map_IsTileVisible(Tile_PackTile(u.o.position))) continue;

            if ((u.o.seenByHouses & (1 << g_playerHouseID)) == 0) continue;

            if (first == null) first = u.o;
            last = u.o;
            if (selected == null) selected = u.o;

            if (selected == u.o) {
                hasPrevious = true;
                continue;
            }

            if (!hasPrevious) {
                previous = u.o;
                continue;
            }

            if (!hasNext) {
                next = u.o;
                hasNext = true;
            }
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;

            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;

            if (!Map_IsTileVisible(Tile_PackTile(s.o.position))) continue;

            if ((s.o.seenByHouses & (1 << g_playerHouseID)) == 0) continue;

            if (first == null) first = s.o;
            last = s.o;
            if (selected == null) selected = s.o;

            if (selected == s.o) {
                hasPrevious = true;
                continue;
            }

            if (!hasPrevious) {
                previous = s.o;
                continue;
            }

            if (!hasNext) {
                next = s.o;
                hasNext = true;
            }
        }

        if (previous == null) previous = last;
        if (next == null) next = first;
        if (previous == null) previous = next;
        if (next == null) next = previous;

        selected = getNext ? next : previous;

        if (selected == null) return;

        Map_SetSelection(Tile_PackTile(selected.position));
    }

    /**
     * After unveiling, check neighbour tiles. This function handles one neighbour.
     * @param packed The neighbour tile of an unveiled tile.
     */
    public static void Map_UnveilTile_Neighbour(int packed) {
        if (Tile_IsOutOfMap(packed)) return;

        Tile t = g_map[packed];

        int tileID = 15;
        if (t.isUnveiled) {
            if (g_validateStrictIfZero == 0 && Tile_IsUnveiled(t.overlayTileID)) return;

            tileID = 0;

            for (int i = 0; i < 4; i++) {
			    int neighbour = packed + g_table_mapDiff[i];

                if (Tile_IsOutOfMap(neighbour) || !g_map[neighbour].isUnveiled) {
                    tileID |= 1 << i;
                }
            }
        }

        if (tileID != 0) {
            if (tileID != 15) {
                Unit u = Unit_Get_ByPackedTile(packed);
                if (u != null) Unit_HouseUnitCount_Add(u, g_playerHouseID);
            }

            tileID = g_iconMap[g_iconMap[ICM_ICONGROUP_FOG_OF_WAR] + tileID];
        }

        t.overlayTileID = tileID;

        Map_Update(packed, 0, false);
    }

    /**
     * Unveil a tile for a House.
     * @param packed The tile to unveil.
     * @param houseID The house to unveil for.
     * @return True if tile was freshly unveiled.
     */
    public static boolean Map_UnveilTile(int packed, int houseID) {
        if (houseID != g_playerHouseID) return false;
        if (Tile_IsOutOfMap(packed)) return false;

        Tile t = g_map[packed];

        if (t.isUnveiled && Tile_IsUnveiled(t.overlayTileID)) return false;
        t.isUnveiled = true;

        Map_MarkTileDirty(packed);

        Unit u = Unit_Get_ByPackedTile(packed);
        if (u != null) Unit_HouseUnitCount_Add(u, houseID);

        Structure s = Structure_Get_ByPackedTile(packed);
        if (s != null) {
            s.o.seenByHouses |= 1 << houseID;
            if (houseID == HOUSE_ATREIDES) s.o.seenByHouses |= 1 << HOUSE_FREMEN;
        }

        Map_UnveilTile_Neighbour(packed);
        Map_UnveilTile_Neighbour(packed + 1);
        Map_UnveilTile_Neighbour(packed - 1);
        Map_UnveilTile_Neighbour(packed - 64);
        Map_UnveilTile_Neighbour(packed + 64);

        return true;
    }

    /**
     * Add spice on the given tile.
     * @param packed The tile.
     */
    static void Map_AddSpiceOnTile(int packed) {
        Tile t = g_map[packed];

        switch (t.groundTileID) {
            case LST_SPICE:
                t.groundTileID = LST_THICK_SPICE;
                Map_AddSpiceOnTile(packed);
                return;

            case LST_THICK_SPICE: {
                for (int j = -1; j <= 1; j++) {
                    for (int i = -1; i <= 1; i++) {
                        Tile t2;
                        int packed2 = Tile_PackXY(Tile_GetPackedX(packed) + i, Tile_GetPackedY(packed) + j);

                        if (Tile_IsOutOfMap(packed2)) continue;

                        t2 = g_map[packed2];

                        if (!g_table_landscapeInfo[t2.groundTileID].canBecomeSpice) {
                            t.groundTileID = LST_SPICE;
                            continue;
                        }

                        if (t2.groundTileID != LST_THICK_SPICE) t2.groundTileID = LST_SPICE;
                    }
                }
                return;
            }

            default:
                if (g_table_landscapeInfo[t.groundTileID].canBecomeSpice) t.groundTileID = LST_SPICE;
                return;
        }
    }

    private static final int[][][] _offsetTable = new int[][][] {
        new int[][] {
            new int[] {0, 0, 4, 0}, {4, 0, 4, 4}, {0, 0, 0, 4}, {0, 4, 4, 4}, {0, 0, 0, 2},
            new int[] {0, 2, 0, 4}, {0, 0, 2, 0}, {2, 0, 4, 0}, {4, 0, 4, 2}, {4, 2, 4, 4},
            new int[] {0, 4, 2, 4}, {2, 4, 4, 4}, {0, 0, 4, 4}, {2, 0, 2, 2}, {0, 0, 2, 2},
            new int[] {4, 0, 2, 2}, {0, 2, 2, 2}, {2, 2, 4, 2}, {2, 2, 0, 4}, {2, 2, 4, 4},
            new int[] {2, 2, 2, 4},
        },
        new int[][] {
            new int[] {0, 0, 4, 0}, {4, 0, 4, 4}, {0, 0, 0, 4}, {0, 4, 4, 4}, {0, 0, 0, 2},
            new int[] {0, 2, 0, 4}, {0, 0, 2, 0}, {2, 0, 4, 0}, {4, 0, 4, 2}, {4, 2, 4, 4},
            new int[] {0, 4, 2, 4}, {2, 4, 4, 4}, {4, 0, 0, 4}, {2, 0, 2, 2}, {0, 0, 2, 2},
            new int[] {4, 0, 2, 2}, {0, 2, 2, 2}, {2, 2, 4, 2}, {2, 2, 0, 4}, {2, 2, 4, 4},
            new int[] {2, 2, 2, 4},
        },
    };

    /**
     * Creates the landscape using the given seed.
     * @param seed The seed.
     */
    public static void Map_CreateLandscape(long seed) {
        int[] around = new int[] {0, -1, 1, -16, 16, -17, 17, -15, 15, -2, 2, -32, 32, -4, 4, -64, 64, -30, 30, -34, 34};

        int[] memory = new int[273];
        int[] currentRow = new int[64];
        int[] previousRow = new int[64];
        int spriteID2;
        byte[] iconMap;

        Tools_Random_Seed(seed);

        /* Place random data on a 4x4 grid. */
        for (int i = 0; i < 272; i++) {
            memory[i] = Tools_Random_256() & 0xF;
            if (memory[i] > 0xA) memory[i] = 0xA;
        }

        int n = (Tools_Random_256() & 0xF) + 1;
        while (n-- != 0) {
            int base = Tools_Random_256();

            for (int j = 0; j < around.length; j++) {
                int index = Math.min(Math.max(0, base + around[j]), 272);

                memory[index] = (memory[index] + (Tools_Random_256() & 0xF)) & 0xF;
            }
        }

        n = (Tools_Random_256() & 0x3) + 1;
        while (n-- != 0) {
            int base = Tools_Random_256();

            for (int j = 0; j < around.length; j++) {
                int index = Math.min(Math.max(0, base + around[j]), 272);

                memory[index] = Tools_Random_256() & 0x3;
            }
        }

        for (int j = 0; j < 16; j++) {
            for (int i = 0; i < 16; i++) {
                g_map[Tile_PackXY(i * 4, j * 4)].groundTileID = memory[j * 16 + i];
            }
        }

        /* Average around the 4x4 grid. */
        for (int j = 0; j < 16; j++) {
            for (int i = 0; i < 16; i++) {
                for (int k = 0; k < 21; k++) {
				    int[] offsets = _offsetTable[(i + 1) % 2][k];

                    int packed1 = Tile_PackXY(i * 4 + offsets[0], j * 4 + offsets[1]);
                    int packed2 = Tile_PackXY(i * 4 + offsets[2], j * 4 + offsets[3]);
                    int packed = (packed1 + packed2) / 2;

                    if (Tile_IsOutOfMap(packed)) continue;

                    packed1 = Tile_PackXY((i * 4 + offsets[0]) & 0x3F, j * 4 + offsets[1]);
                    packed2 = Tile_PackXY((i * 4 + offsets[2]) & 0x3F, j * 4 + offsets[3]);
                    assert(packed1 < 64 * 64);

                    /* ENHANCEMENT -- use groundTileID=0 when out-of-bounds to generate the original maps. */
                    int sprite2;
                    if (packed2 < 64 * 64) {
                        sprite2 = g_map[packed2].groundTileID;
                    } else {
                        sprite2 = 0;
                    }

                    g_map[packed].groundTileID = (g_map[packed1].groundTileID + sprite2 + 1) / 2;
                }
            }
        }

        Arrays.fill(currentRow, 0);

        /* Average each tile with its neighbours. */
        for (int j = 0; j < 64; j++) {
            System.arraycopy(currentRow, 0, previousRow, 0, currentRow.length);

            int off = j * 64;
            for (int i = 0; i < 64; i++) {
                currentRow[i] = g_map[off + i].groundTileID;
            }

            for (int i = 0; i < 64; i++) {
                int[] neighbours = new int[9];
                int total = 0;

                neighbours[0] = (i == 0  || j == 0) ? currentRow[i] : previousRow[i - 1];
                neighbours[1] = (j == 0)  ? currentRow[i] : previousRow[i];
                neighbours[2] = (i == 63 || j == 0) ? currentRow[i] : previousRow[i + 1];
                neighbours[3] = (i == 0) ? currentRow[i] : currentRow[i - 1];
                neighbours[4] = currentRow[i];
                neighbours[5] = (i == 63) ? currentRow[i] : currentRow[i + 1];
                neighbours[6] = (i == 0  || j == 63) ? currentRow[i] : g_map[off + i + 63].groundTileID;
                neighbours[7] = (j == 63) ? currentRow[i] : g_map[off + i + 64].groundTileID;
                neighbours[8] = (i == 63 || j == 63) ? currentRow[i] : g_map[off + i + 65].groundTileID;

                for (int k = 0; k < 9; k++) {
                    total += neighbours[k];
                }

                g_map[off + i].groundTileID = total / 9;
            }
        }

        /* Filter each tile to determine its final type. */
        int spriteID1 = Tools_Random_256() & 0xF;
        if (spriteID1 < 0x8) spriteID1 = 0x8;
        if (spriteID1 > 0xC) spriteID1 = 0xC;

        spriteID2 = (Tools_Random_256() & 0x3) - 1;
        if (spriteID2 > spriteID1 - 3) spriteID2 = spriteID1 - 3;

        for (int i = 0; i < 4096; i++) {
            int spriteID = g_map[i].groundTileID;

            if (spriteID > spriteID1 + 4) {
                spriteID = LST_ENTIRELY_MOUNTAIN;
            } else if (spriteID >= spriteID1) {
                spriteID = LST_ENTIRELY_ROCK;
            } else if (spriteID <= spriteID2) {
                spriteID = LST_ENTIRELY_DUNE;
            } else {
                spriteID = LST_NORMAL_SAND;
            }

            g_map[i].groundTileID = spriteID;
        }

        /* Add some spice. */
        n = Tools_Random_256() & 0x2F;
        while (n-- != 0) {
            int packed;

            while (true) {
                packed = Tools_Random_256() & 0x3F;
                packed = Tile_PackXY(Tools_Random_256() & 0x3F, packed);

                if (g_table_landscapeInfo[g_map[packed].groundTileID].canBecomeSpice) break;
            }

            Tile32 tile = Tile_UnpackTile(packed);

            int m = Tools_Random_256() & 0x1F;
            while (m-- != 0) {
                while (true) {
                    Tile32 unpacked = Tile_MoveByRandom(tile, Tools_Random_256() & 0x3F, true);
                    packed = Tile_PackTile(unpacked);

                    if (!Tile_IsOutOfMap(packed)) break;
                }

                Map_AddSpiceOnTile(packed);
            }
        }

        /* Make everything smoother and use the right sprite indexes. */
        for (int j = 0; j < 64; j++) {
            System.arraycopy(currentRow, 0, previousRow, 0, currentRow.length);

            int off = j * 64;
            for (int i = 0; i < 64; i++) {
                currentRow[i] = g_map[off + i].groundTileID;
            }

            for (int i = 0; i < 64; i++) {
                int current = g_map[off + i].groundTileID;
                int up = (j == 0)  ? current : previousRow[i];
                int right = (i == 63) ? current : currentRow[i + 1];
                int down = (j == 63) ? current : g_map[off + i + 64].groundTileID;
                int left = (i == 0)  ? current : currentRow[i - 1];
                int spriteID = 0;

                if (up == current) spriteID |= 1;
                if (right == current) spriteID |= 2;
                if (down == current) spriteID |= 4;
                if (left == current) spriteID |= 8;

                switch (current) {
                    case LST_NORMAL_SAND:
                        spriteID = 0;
                        break;
                    case LST_ENTIRELY_ROCK:
                        if (up == LST_ENTIRELY_MOUNTAIN) spriteID |= 1;
                        if (right == LST_ENTIRELY_MOUNTAIN) spriteID |= 2;
                        if (down == LST_ENTIRELY_MOUNTAIN) spriteID |= 4;
                        if (left == LST_ENTIRELY_MOUNTAIN) spriteID |= 8;
                        spriteID++;
                        break;
                    case LST_ENTIRELY_DUNE:
                        spriteID += 17;
                        break;
                    case LST_ENTIRELY_MOUNTAIN:
                        spriteID += 33;
                        break;
                    case LST_SPICE:
                        if (up == LST_THICK_SPICE) spriteID |= 1;
                        if (right == LST_THICK_SPICE) spriteID |= 2;
                        if (down == LST_THICK_SPICE) spriteID |= 4;
                        if (left == LST_THICK_SPICE) spriteID |= 8;
                        spriteID += 49;
                        break;
                    case LST_THICK_SPICE:
                        spriteID += 65;
                        break;
                    default: break;
                }

                g_map[off + i].groundTileID = spriteID;
            }
        }

        /* Finalise the tiles with the real sprites. */
        iconMap = g_iconMap[g_iconMap[ICM_ICONGROUP_LANDSCAPE]];

        for (int i = 0; i < 4096; i++) {
            Tile t = g_map[i];

            t.groundTileID = iconMap[t.groundTileID];
            t.overlayTileID = g_veiledTileID;
            t.houseID = HOUSE_HARKONNEN;
            t.isUnveiled = false;
            t.hasUnit = false;
            t.hasStructure = false;
            t.hasAnimation = false;
            t.hasExplosion = false;
            t.index = 0;
        }

        for (int i = 0; i < 4096; i++) {
            g_mapTileID[i] = g_map[i].groundTileID;
        }
    }

    /**
     * Mark a specific tile as dirty, so it gets a redrawn next time.
     *
     * @param packed The tile to mark as dirty.
     */
    public static void Map_MarkTileDirty(int packed) {
        if (BitArray_Test(g_displayedMinimap, packed) && g_scenario.mapScale + 1 == 0) {
            return;
        }

        BitArray_Set(g_changedTilesMap, packed);
        if (g_changedTilesCount < g_changedTiles.length) g_changedTiles[g_changedTilesCount++] = packed;
    }
}
