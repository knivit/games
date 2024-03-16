package com.tsoft.dune2.tools;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.config.ConfigService.g_gameConfig;
import static com.tsoft.dune2.pool.PoolStructureService.STRUCTURE_INDEX_MAX_HARD;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolUnitService.UNIT_INDEX_MAX;
import static com.tsoft.dune2.pool.PoolUnitService.UNIT_INDEX_MAX;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutTileDiff;
import static com.tsoft.dune2.structure.StructureService.STRUCTURE_INDEX_MAX_HARD;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.IndexType.*;

public class ToolsService {

    private static long[] s_randomSeed = new long[4];
    private static long s_randomLCG;

    public static int Tools_AdjustToGameSpeed(int normal, int minimum, int maximum, boolean inverseSpeed) {
        int gameSpeed = g_gameConfig.gameSpeed;

        if (gameSpeed == 2) return normal;
        if (gameSpeed > 4) return normal;

        if (maximum > normal * 2) maximum = normal * 2;
        if (minimum < normal / 2) minimum = normal / 2;

        if (inverseSpeed) gameSpeed = 4 - gameSpeed;

        switch (gameSpeed) {
            case 0: return minimum;
            case 1: return normal - (normal - minimum) / 2;
            case 3: return normal + (maximum - normal) / 2;
            case 4: return maximum;
        }

        /* Never reached, but avoids compiler errors */
        return normal;
    }

    /**
     * Get the type of the given encoded index.
     *
     * @param encoded The encoded index to get the type of.
     * @return The type
     */
    public static int Tools_Index_GetType(int encoded) {
        switch (encoded & 0xC000) {
            case 0x4000: return IT_UNIT;
            case 0x8000: return IT_STRUCTURE;
            case 0xC000: return IT_TILE;
            default:     return IT_NONE;
        }
    }

    /**
     * Decode the given encoded index.
     *
     * @param encoded The encoded index to decode.
     * @return The decoded index.
     */
    public static int Tools_Index_Decode(int encoded) {
        if (Tools_Index_GetType(encoded) == IT_TILE) {
            return Tile_PackXY((encoded >> 1) & 0x3F, (encoded >> 8) & 0x3F);
        }
        return encoded & 0x3FFF;
    }

    /**
     * Encode the given index.
     *
     * @param index The index to encode.
     * @param type The type of the encoded Index.
     * @return The encoded Index.
     */
    public static int Tools_Index_Encode(int index, int type) {
        switch (type) {
            case IT_TILE: {
                int ret;

                ret  = ((Tile_GetPackedX(index) << 1) + 1) << 0;
                ret |= ((Tile_GetPackedY(index) << 1) + 1) << 7;
                return ret | 0xC000;
            }
            case IT_UNIT: {
                if (index >= UNIT_INDEX_MAX || !Unit_Get_ByIndex(index).o.flags.s.allocated) return 0;
                return index | 0x4000;
            }
            case IT_STRUCTURE:  return index | 0x8000;
            default: return 0;
        }
    }

    /**
     * Check whether an encoded index is valid.
     *
     * @param encoded The encoded index to check for validity.
     * @return True if valid, false if not.
     */
    public static boolean Tools_Index_IsValid(int encoded) {
        int index;

        if (encoded == 0) return false;

        index = Tools_Index_Decode(encoded);

        switch (Tools_Index_GetType(encoded)) {
            case IT_UNIT:
                if (index >= UNIT_INDEX_MAX) return false;
                return Unit_Get_ByIndex(index).o.flags.s.used && Unit_Get_ByIndex(index).o.flags.s.allocated;

            case IT_STRUCTURE:
                if (index >= STRUCTURE_INDEX_MAX_HARD) return false;
                return Structure_Get_ByIndex(index).o.flags.s.used;

            case IT_TILE : return true;

            default: return false;
        }
    }

    /**
     * Gets the packed tile corresponding to the given encoded index.
     *
     * @param encoded The encoded index to get the packed tile of.
     * @return The packed tile.
     */
    public static int Tools_Index_GetPackedTile(int encoded) {
        int index;

        index = Tools_Index_Decode(encoded);

        switch (Tools_Index_GetType(encoded)) {
            case IT_TILE:      return index;
            case IT_UNIT:      return (index < UNIT_INDEX_MAX) ? Tile_PackTile(Unit_Get_ByIndex(index).o.position) : 0;
            case IT_STRUCTURE: return (index < STRUCTURE_INDEX_MAX_HARD) ? Tile_PackTile(Structure_Get_ByIndex(index).o.position) : 0;
            default:           return 0;
        }
    }

    /**
     * Gets the tile corresponding to the given encoded index.
     *
     * @param encoded The encoded index to get the tile of.
     * @return The tile.
     */
    public static Tile32 Tools_Index_GetTile(int encoded) {
        int index;
        Tile32 tile = new Tile32();

        index = Tools_Index_Decode(encoded);
        tile.x = 0;
        tile.y = 0;

        switch (Tools_Index_GetType(encoded)) {
            case IT_TILE: return Tile_UnpackTile(index);
            case IT_UNIT: return (index < UNIT_INDEX_MAX) ? Unit_Get_ByIndex(index).o.position : tile;
            case IT_STRUCTURE: {
			    StructureInfo si;
                Structure s;

                if (index >= STRUCTURE_INDEX_MAX_HARD) return tile;

                s = Structure_Get_ByIndex(index);
                si = g_table_structureInfo[s.o.type];

                return Tile_AddTileDiff(s.o.position, g_table_structure_layoutTileDiff[si.layout]);
            }
            default: return tile;
        }
    }

    /**
     * Gets the Unit corresponding to the given encoded index.
     *
     * @param encoded The encoded index to get the Unit of.
     * @return The Unit.
     */
    public static Unit Tools_Index_GetUnit(int encoded) {
        int index;

        if (Tools_Index_GetType(encoded) != IT_UNIT) return null;

        index = Tools_Index_Decode(encoded);
        return (index < UNIT_INDEX_MAX) ? Unit_Get_ByIndex(index) : null;
    }

    /**
     * Gets the Structure corresponding to the given encoded index.
     *
     * @param encoded The encoded index to get the Structure of.
     * @return The Structure.
     */
    public static Structure Tools_Index_GetStructure(int encoded) {
        int index;

        if (Tools_Index_GetType(encoded) != IT_STRUCTURE) return null;

        index = Tools_Index_Decode(encoded);
        return (index < STRUCTURE_INDEX_MAX_HARD) ? Structure_Get_ByIndex(index) : null;
    }

    /**
     * Gets the Object corresponding to the given encoded index.
     *
     * @param encoded The encoded index to get the Object of.
     * @return The Object.
     */
    public static GObject Tools_Index_GetObject(int encoded) {
        int index;

        switch (Tools_Index_GetType(encoded)) {
            case IT_UNIT:
                index = Tools_Index_Decode(encoded);
                return (index < UNIT_INDEX_MAX) ? Unit_Get_ByIndex(index).o : null;

            case IT_STRUCTURE:
                index = Tools_Index_Decode(encoded);
                return (index < STRUCTURE_INDEX_MAX_HARD) ? Structure_Get_ByIndex(index).o : null;

            default: return null;
        }
    }

    /**
     * Get a random value between 0 and 255.
     *
     * @return The random value.
     */
    public static int Tools_Random_256(void) {
        int val16;
        int val8;

        val16 = (s_randomSeed[1] << 8) | s_randomSeed[2];
        val8 = ((val16 ^ 0x8000) >> 15) & 1;
        val16 = (val16 << 1) | ((s_randomSeed[0] >> 1) & 1);
        val8 = (s_randomSeed[0] >> 2) - s_randomSeed[0] - val8;
        s_randomSeed[0] = (val8 << 7) | (s_randomSeed[0] >> 1);
        s_randomSeed[1] = val16 >> 8;
        s_randomSeed[2] = val16 & 0xFF;

        return s_randomSeed[0] ^ s_randomSeed[1];
    }

    /**
     * Set the seed for the Tools_Random_256().
     * @param seed The seed to set the randomizer to.
     */
    public static void Tools_Random_Seed(long seed) {
        s_randomSeed[0] = (seed >>  0) & 0xFF;
        s_randomSeed[1] = (seed >>  8) & 0xFF;
        s_randomSeed[2] = (seed >> 16) & 0xFF;
        s_randomSeed[3] = (seed >> 24) & 0xFF;
    }

    /**
     * Set the seed for the LCG randomizer.
     */
    public static void Tools_RandomLCG_Seed(int seed) {
        s_randomLCG = seed;
    }

    /**
     * Get a random value from the LCG.
     */
    public static long Tools_RandomLCG() {
        /* Borland C/C++ 'a' and 'b' value, bits 30..16, as used by Dune2 */
        s_randomLCG = 0x015A4E35 * s_randomLCG + 1;
        return (s_randomLCG >> 16) & 0x7FFF;
    }

    /**
     * Get a random value between the given values.
     *
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The random value.
     */
    public static int Tools_RandomLCG_Range(int min, int max) {
        int ret;

        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        do {
            int value = (int)Tools_RandomLCG() * (max - min + 1) / 0x8000 + min;
            ret = value;
        } while (ret > max);

        return ret;
    }

    /**
     * Test a bit in a bit array.
     * @param array Bit array.
     * @param index Index in the array.
     * @return value of the bit.
     */
    public static boolean BitArray_Test(int[] array, int index) {
        return (array[index >> 3] & (1 << (index & 7))) != 0;
    }

    /**
     * Set a bit in a bit array.
     * @param array Bit array.
     * @param index Index in the array.
     */
    public static void BitArray_Set(int[] array, int index) {
        array[index >> 3] |= (1 << (index & 7));
    }

    /**
     * Clear a bit in a bit array.
     * @param array Bit array.
     * @param index Index in the array.
     */
    public static void BitArray_Clear(int[] array, int index) {
        array[index >> 3] &= ~(1 << (index & 7));
    }
}
