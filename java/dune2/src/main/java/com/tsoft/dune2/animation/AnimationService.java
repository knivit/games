package com.tsoft.dune2.animation;

import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.tile.Tile32;

import java.util.Arrays;

import static com.tsoft.dune2.animation.AnimationCommand.*;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_BASE_DEFENSE_TURRET;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_BASE_ROCKET_TURRET;
import static com.tsoft.dune2.sprites.SpritesService.g_iconMap;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutTileCount;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutTiles;
import static com.tsoft.dune2.structure.StructureService.Structure_Get_ByPackedTile;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;
import static com.tsoft.dune2.timer.TimerService.g_timerGUI;
import static com.tsoft.dune2.tools.ToolsService.Tools_Random_256;

public class AnimationService {

    public static final int ANIMATION_MAX = 112;

    static Animation[] g_animations = new Animation[ANIMATION_MAX];
    static long s_animationTimer; /*!< Timer for animations. */

    /**
     * Stop with this Animation.
     * @param animation The Animation to stop.
     * @param parameter Not used.
     */
    static void Animation_Func_Stop(Animation animation, int parameter) {
	    int[] layout = g_table_structure_layoutTiles[animation.tileLayout];
        int layoutTileCount = g_table_structure_layoutTileCount[animation.tileLayout];
        int packed = Tile_PackTile(animation.tile);
        int i;

        g_map[packed].hasAnimation = false;
        animation.commands = null;

        for (i = 0; i < layoutTileCount; i++) {
            int position = packed + layout[i];
            Tile t = g_map[position];

            if (animation.tileLayout != 0) {
                t.groundTileID = g_mapTileID[position];
            }

            if (Map_IsPositionUnveiled(position)) {
                t.overlayTileID = 0;
            }

            Map_Update(position, 0, false);
        }
    }

    /**
     * Abort this Animation.
     * @param animation The Animation to abort.
     * @param parameter Not used.
     */
    static void Animation_Func_Abort(Animation animation, int parameter) {
        int packed = Tile_PackTile(animation.tile);

        g_map[packed].hasAnimation = false;
        animation.commands = null;

        Map_Update(packed, 0, false);
    }

    /**
     * Pause the animation for a few ticks.
     * @param animation The Animation to pause.
     * @param parameter How many ticks it should pause.
     * @note Delays are randomly delayed with [0..3] ticks.
     */
    static void Animation_Func_Pause(Animation animation, int parameter) {
        assert(parameter >= 0);

        animation.tickNext = g_timerGUI + parameter + (Tools_Random_256() % 4);
    }

    /**
     * Set the overlay sprite of the tile.
     * @param animation The Animation for which we change the overlay sprite.
     * @param parameter The TileID to which the overlay sprite is set.
     */
    static void Animation_Func_SetOverlayTile(Animation animation, int parameter) {
        int packed = Tile_PackTile(animation.tile);
        Tile t = g_map[packed];
        assert(parameter >= 0);

        if (!Map_IsPositionUnveiled(packed)) return;

        t.overlayTileID = g_iconMap[g_iconMap[animation.iconGroup] + parameter];
        t.houseID = animation.houseID;

        Map_Update(packed, 0, false);
    }

    /**
     * Rewind the animation.
     * @param animation The Animation to rewind.
     * @param parameter Not used.
     */
    static void Animation_Func_Rewind(Animation animation, int parameter) {
        animation.current = 0;
    }

    /**
     * Set the ground sprite of the tile.
     * @param animation The Animation for which we change the ground sprite.
     * @param parameter The offset in the iconGroup to which the ground sprite is set.
     */
    static void Animation_Func_SetGroundTile(Animation animation, int parameter) {
        int[] specialMap = new int[1];
        byte[] iconMap;
	    int[] layout = g_table_structure_layoutTiles[animation.tileLayout];
        int layoutTileCount = g_table_structure_layoutTileCount[animation.tileLayout];
        int packed = Tile_PackTile(animation.tile);
        int i;

        iconMap = g_iconMap[g_iconMap[animation.iconGroup] + layoutTileCount * parameter];

        /* Some special case for turrets */
        if ((parameter > 1) && (animation.iconGroup == ICM_ICONGROUP_BASE_DEFENSE_TURRET || animation.iconGroup == ICM_ICONGROUP_BASE_ROCKET_TURRET)) {
            Structure s = Structure_Get_ByPackedTile(packed);
            assert(s != null);
            assert(layoutTileCount == 1);

            specialMap[0] = s.rotationSpriteDiff + g_iconMap[g_iconMap[animation.iconGroup]] + 2;
            iconMap = specialMap;
        }

        for (i = 0; i < layoutTileCount; i++) {
            int position = packed + (*layout++);
            int tileID = *iconMap++;
            Tile t = g_map[position];

            if (t.groundTileID == tileID) continue;
            t.groundTileID = tileID;
            t.houseID = animation.houseID;

            if (Map_IsPositionUnveiled(position)) {
                t.overlayTileID = 0;
            }

            Map_Update(position, 0, false);

            Map_MarkTileDirty(position);
        }
    }

    /**
     * Forward the current Animation with the given amount of steps.
     * @param animation The Animation to forward.
     * @param parameter With what value you want to forward the Animation.
     * @note Forwarding with 1 is just the next instruction, making this command a NOP.
     */
    static void Animation_Func_Forward(Animation animation, int parameter) {
        animation.current += parameter - 1;
    }

    /**
     * Set the IconGroup of the Animation.
     * @param animation The Animation to change.
     * @param parameter To what value IconGroup should change.
     */
    static void Animation_Func_SetIconGroup(Animation animation, int parameter) {
        assert(parameter >= 0);

        animation.iconGroup = (int)parameter;
    }

    /**
     * Play a Voice on the tile of animation.
     * @param animation The Animation which gives the position the voice plays at.
     * @param parameter The VoiceID to play.
     */
    static void Animation_Func_PlayVoice(Animation animation, int parameter) {
        Voice_PlayAtTile(parameter, animation.tile);
    }

    static void Animation_Init() {
        Arrays.fill(g_animations, null);
    }

    /**
     * Start an Animation.
     * @param commands List of commands for the Animation.
     * @param tile The tile to do the Animation on.
     * @param tileLayout The layout of tiles for the Animation.
     * @param houseID The house of the item being Animation.
     * @param iconGroup In which IconGroup the sprites of the Animation belongs.
     */
    public static void Animation_Start(AnimationCommandStruct[] commands, Tile32 tile, int tileLayout, int houseID, int iconGroup) {
        Animation[] animation = g_animations;
        int packed = Tile_PackTile(tile);
        Tile t;
        int i;

        t = g_map[packed];
        Animation_Stop_ByTile(packed);

        for (i = 0; i < ANIMATION_MAX; i++) {
            if (animation[i].commands != null) continue;

            animation[i].tickNext   = g_timerGUI;
            animation[i].tileLayout = tileLayout;
            animation[i].houseID    = houseID;
            animation[i].current    = 0;
            animation[i].iconGroup  = iconGroup;
            animation[i].commands   = commands;
            animation[i].tile       = tile;

            s_animationTimer = 0;

            t.houseID = houseID;
            t.hasAnimation = true;
            return;
        }
    }

    /**
     * Stop an Animation on a tile, if any.
     * @param packed The tile to check for animation on.
     */
    public static void Animation_Stop_ByTile(int packed) {
        Animation[] animation = g_animations;
        Tile t = g_map[packed];
        int i;

        if (!t.hasAnimation) return;

        for (i = 0; i < ANIMATION_MAX; i++) {
            if (animation[i].commands == null) continue;
            if (Tile_PackTile(animation[i].tile) != packed) continue;

            Animation_Func_Stop(animation[i], 0);
            return;
        }
    }

    /**
     * Check all Animations if they need changing.
     */
    public static void Animation_Tick() {
        Animation[] animation = g_animations;
        int i;

        if (s_animationTimer > g_timerGUI) return;
        s_animationTimer += 10000;

        for (i = 0; i < ANIMATION_MAX; i++) {
            if (animation[i].commands == null) continue;

            if (animation[i].tickNext <= g_timerGUI) {
			    AnimationCommandStruct[] commands = animation[i].commands + animation[i].current;
                int parameter = commands.parameter;
                assert((parameter & 0x0800) == 0 || (parameter & 0xF000) != 0); /* Validate if the compiler sign-extends correctly */

                animation[i].current++;

                switch (commands.command) {
                    case ANIMATION_STOP:
                    default:                           Animation_Func_Stop(animation[i], parameter); break;

                    case ANIMATION_ABORT:              Animation_Func_Abort(animation[i], parameter); break;
                    case ANIMATION_SET_OVERLAY_TILE:   Animation_Func_SetOverlayTile(animation[i], parameter); break;
                    case ANIMATION_PAUSE:              Animation_Func_Pause(animation[i], parameter); break;
                    case ANIMATION_REWIND:             Animation_Func_Rewind(animation[i], parameter); break;
                    case ANIMATION_PLAY_VOICE:         Animation_Func_PlayVoice(animation[i], parameter); break;
                    case ANIMATION_SET_GROUND_TILE:    Animation_Func_SetGroundTile(animation[i], parameter); break;
                    case ANIMATION_FORWARD:            Animation_Func_Forward(animation[i], parameter); break;
                    case ANIMATION_SET_ICONGROUP:      Animation_Func_SetIconGroup(animation[i], parameter); break;
                }

                if (animation[i].commands == null) continue;
            }

            if (animation[i].tickNext < s_animationTimer) s_animationTimer = animation[i].tickNext;
        }
    }
}
