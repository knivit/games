package com.tsoft.dune2.explosion;

import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.tile.Tile32;

import static com.tsoft.dune2.explosion.ExplosionCommand.*;
import static com.tsoft.dune2.explosion.ExplosionType.EXPLOSION_MAX;
import static com.tsoft.dune2.explosion.ExplosionType.EXPLOSION_SPICE_BLOOM_TREMOR;
import static com.tsoft.dune2.map.LandscapeType.*;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;

public class ExplosionService {

    /**
     * Update the tile a Explosion is on.
     * @param type Are we introducing (0) or updating (2) the tile.
     * @param e The Explosion in question.
     */
    static void Explosion_Update(int type, Explosion e) {
        if (e == null) return;

        if (type == 1 && e.isDirty) return;

        e.isDirty = (type != 0);

        Map_UpdateAround(24, e.position, null, g_functions[2][type]);
    }

    /**
     * Handle damage to a tile, removing spice, removing concrete, stuff like that.
     * @param e The Explosion to handle damage on.
     * @param parameter Unused parameter.
     */
    static void Explosion_Func_TileDamage(Explosion e, int parameter) {
        int[] craterIconMapIndex = new int[] { -1, 2, 1 };

        int packed;
        int type;
        Tile t;
        int iconMapIndex;
        int overlayTileID;
	    int *iconMap;

        VARIABLE_NOT_USED(parameter);

        packed = Tile_PackTile(e.position);

        if (!Map_IsPositionUnveiled(packed)) return;

        type = Map_GetLandscapeType(packed);

        if (type == LST_STRUCTURE || type == LST_DESTROYED_WALL) return;

        t = g_map[packed];

        if (type == LST_CONCRETE_SLAB) {
            t.groundTileID = g_mapTileID[packed];
            Map_Update(packed, 0, false);
        }

        if (g_table_landscapeInfo[type].craterType == 0) return;

        /* You cannot damage veiled tiles */
        overlayTileID = t.overlayTileID;
        if (!Tile_IsUnveiled(overlayTileID)) return;

        iconMapIndex = craterIconMapIndex[g_table_landscapeInfo[type].craterType];
        iconMap = g_iconMap[g_iconMap[iconMapIndex]];

        if (iconMap[0] <= overlayTileID && overlayTileID <= iconMap[10]) {
            /* There already is a crater; make it bigger */
            overlayTileID -= iconMap[0];
            if (overlayTileID < 4) overlayTileID += 2;
        } else {
            /* Randomly pick 1 of the 2 possible craters */
            overlayTileID = Tools_Random_256() & 1;
        }

        /* Reduce spice if there is any */
        Map_ChangeSpiceAmount(packed, -1);

        /* Boom a bloom if there is one */
        if (t.groundTileID == g_bloomTileID) {
            Map_Bloom_ExplodeSpice(packed, g_playerHouseID);
            return;
        }

        /* Update the tile with the crater */
        t.overlayTileID = overlayTileID + iconMap[0];
        Map_Update(packed, 0, false);
    }

    /**
     * Play a voice for a Explosion.
     * @param e The Explosion to play the voice on.
     * @param voiceID The voice to play.
     */
    static void Explosion_Func_PlayVoice(Explosion e, int voiceID) {
        Voice_PlayAtTile(voiceID, e.position);
    }

    /**
     * Shake the screen.
     * @param e The Explosion.
     * @param parameter Unused parameter.
     */
    static void Explosion_Func_ScreenShake(Explosion e, int parameter) {
        int i;
        Debug("Explosion_Func_ScreenShake(%p, %d)\n", e, parameter);

        for(i = 0; i < 2; i++) {
            msleep(30);
            Video_SetOffset(320);

            msleep(30);
            Video_SetOffset(0);
        }
    }

    /**
     * Check if there is a bloom at the location, and make it explode if needed.
     * @param e The Explosion to perform the explosion on.
     * @param parameter Unused parameter.
     */
    static void Explosion_Func_BloomExplosion(Explosion e, int parameter) {
        int packed;

        VARIABLE_NOT_USED(parameter);

        packed = Tile_PackTile(e.position);

        if (g_map[packed].groundTileID != g_bloomTileID) return;

        Map_Bloom_ExplodeSpice(packed, g_playerHouseID);
    }

    /**
     * Set the animation of a Explosion.
     * @param e The Explosion to change.
     * @param animationMapID The animation map to use.
     */
    static void Explosion_Func_SetAnimation(Explosion e, int animationMapID) {
        int packed;

        packed = Tile_PackTile(e.position);

        if (Structure_Get_ByPackedTile(packed) != null) return;

        animationMapID += Tools_Random_256() & 0x1;
        animationMapID += g_table_landscapeInfo[Map_GetLandscapeType(packed)].isSand ? 0 : 2;

        assert(animationMapID < 16);
        Animation_Start(g_table_animation_map[animationMapID], e.position, 0, e.houseID, 3);
    }

    /**
     * Set position at the left of a row.
     * @param e The Explosion to change.
     * @param row Row number.
     */
    static void Explosion_Func_MoveYPosition(Explosion e, int row) {
        e.position.y += row;
    }

    /**
     * Stop performing an explosion.
     * @param e The Explosion to end.
     * @param parameter Unused parameter.
     */
    static void Explosion_Func_Stop(Explosion e, int parameter) {
        VARIABLE_NOT_USED(parameter);

        g_map[Tile_PackTile(e.position)].hasExplosion = false;

        Explosion_Update(0, e);

        e.commands = null;
    }

    /**
     * Set timeout for next the activity of \a e.
     * @param e The Explosion to change.
     * @param value The new timeout value.
     */
    static void Explosion_Func_SetTimeout(Explosion e, int value) {
        e.timeOut = g_timerGUI + value;
    }

    /**
     * Set timeout for next the activity of \a e to a random value up to \a value.
     * @param e The Explosion to change.
     * @param value The maximum amount of timeout.
     */
    static void Explosion_Func_SetRandomTimeout(Explosion e, int value) {
        e.timeOut = g_timerGUI + Tools_RandomLCG_Range(0, value);
    }

    /**
     * Set the SpriteID of the Explosion.
     * @param e The Explosion to change.
     * @param spriteID The new SpriteID for the Explosion.
     */
    static void Explosion_Func_SetSpriteID(Explosion e, int spriteID) {
        e.spriteID = spriteID;

        Explosion_Update(2, e);
    }

    /**
     * Stop any Explosion at position \a packed.
     * @param packed A packed position where no activities should take place (any more).
     */
    static void Explosion_StopAtPosition(int packed) {
        Tile t;
        int i;

        t = g_map[packed];

        if (!t.hasExplosion) return;

        for (i = 0; i < EXPLOSION_MAX; i++) {
            Explosion e;

            e = g_explosions[i];

            if (e.commands == null || Tile_PackTile(e.position) != packed) continue;

            Explosion_Func_Stop(e, 0);
        }
    }

    static void Explosion_Init() {
        memset(g_explosions, 0, EXPLOSION_MAX * sizeof(Explosion));
    }

    /**
     * Start a Explosion on a tile.
     * @param explosionType Type of Explosion.
     * @param position The position to use for init.
     */
    public static void Explosion_Start(int explosionType, Tile32 position) {
	    ExplosionCommandStruct[] commands;
        int packed;
        int i;

        if (explosionType > EXPLOSION_SPICE_BLOOM_TREMOR) return;
        commands = g_table_explosion[explosionType];

        packed = Tile_PackTile(position);

        Explosion_StopAtPosition(packed);

        for (i = 0; i < EXPLOSION_MAX; i++) {
            Explosion e;

            e = g_explosions[i];

            if (e.commands != null) continue;

            e.commands = commands;
            e.current  = 0;
            e.spriteID = 0;
            e.position = position;
            e.isDirty  = false;
            e.timeOut  = g_timerGUI;
            s_explosionTimer = 0;
            g_map[packed].hasExplosion = true;

            break;
        }
    }

    /**
     * Timer tick for explosions.
     */
    static void Explosion_Tick() {
        int i;

        if (s_explosionTimer > g_timerGUI) return;
        s_explosionTimer += 10000;

        for (i = 0; i < EXPLOSION_MAX; i++) {
            Explosion e;

            e = g_explosions[i];

            if (e.commands == null) continue;

            if (e.timeOut <= g_timerGUI) {
                int parameter = e.commands[e.current].parameter;
                int command   = e.commands[e.current].command;

                e.current++;

                switch (command) {
                    default:
                    case EXPLOSION_STOP:               Explosion_Func_Stop(e, parameter); break;

                    case EXPLOSION_SET_SPRITE:         Explosion_Func_SetSpriteID(e, parameter); break;
                    case EXPLOSION_SET_TIMEOUT:        Explosion_Func_SetTimeout(e, parameter); break;
                    case EXPLOSION_SET_RANDOM_TIMEOUT: Explosion_Func_SetRandomTimeout(e, parameter); break;
                    case EXPLOSION_MOVE_Y_POSITION:    Explosion_Func_MoveYPosition(e, parameter); break;
                    case EXPLOSION_TILE_DAMAGE:        Explosion_Func_TileDamage(e, parameter); break;
                    case EXPLOSION_PLAY_VOICE:         Explosion_Func_PlayVoice(e, parameter); break;
                    case EXPLOSION_SCREEN_SHAKE:       Explosion_Func_ScreenShake(e, parameter); break;
                    case EXPLOSION_SET_ANIMATION:      Explosion_Func_SetAnimation(e, parameter); break;
                    case EXPLOSION_BLOOM_EXPLOSION:    Explosion_Func_BloomExplosion(e, parameter); break;
                }
            }

            if (e.commands == null || e.timeOut > s_explosionTimer) continue;

            s_explosionTimer = e.timeOut;
        }
    }

    public static Explosion Explosion_Get_ByIndex(int i) {
        assert(0 <= i && i < EXPLOSION_MAX);

        return g_explosions[i];
    }
}
