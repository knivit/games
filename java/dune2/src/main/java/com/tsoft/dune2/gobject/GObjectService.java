package com.tsoft.dune2.gobject;

import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.tile.Tile32;

import static com.tsoft.dune2.map.MapService.g_map;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Get_ByIndex;
import static com.tsoft.dune2.structure.StructureService.Structure_SetState;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutEdgeTiles;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_BUSY;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_IDLE;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.ToolsService.Tools_Index_GetStructure;

public class GObjectService {

    /**
     * Link two variable4 values to each other, and clean up existing values if
     *  needed.
     * @param encodedFrom From where the link goes.
     * @param encodedTo To where the link goes.
     */
    public static void Object_Script_Variable4_Link(int encodedFrom, int encodedTo) {
        GObject objectFrom;
        GObject objectTo;

        if (!Tools_Index_IsValid(encodedFrom)) return;
        if (!Tools_Index_IsValid(encodedTo)) return;

        objectFrom = Tools_Index_GetObject(encodedFrom);
        objectTo = Tools_Index_GetObject(encodedTo);

        if (objectFrom == null) return;
        if (objectTo == null) return;

        if (objectFrom.script.variables[4] != objectTo.script.variables[4]) {
            Object_Script_Variable4_Clear(objectFrom);
            Object_Script_Variable4_Clear(objectTo);
        }

        if (objectFrom.script.variables[4] != 0) return;

        Object_Script_Variable4_Set(objectFrom, encodedTo);
        Object_Script_Variable4_Set(objectTo, encodedFrom);
    }

    /**
     * Set in a safe way the new value for variable4.
     * @param o The Object to set variable4 for.
     * @param encoded The encoded index to set it to.
     */
    public static void Object_Script_Variable4_Set(GObject o, int encoded) {
        StructureInfo si;
        Structure s;

        if (o == null) return;

        o.script.variables[4] = encoded;

        if (o.flags.isUnit) return;

        si = g_table_structureInfo[o.type];
        if (!si.o.flags.busyStateIsIncoming) return;

        s = (Structure)o;
        if (Structure_GetLinkedUnit(s) != null) return;

        Structure_SetState(s, (encoded == 0) ? STRUCTURE_STATE_IDLE : STRUCTURE_STATE_BUSY);
    }

    /**
     * Clear variable4 in a safe (and recursive) way from an object.
     * @param object The Oject to clear variable4 of.
     */
    public static void Object_Script_Variable4_Clear(GObject object) {
        GObject objectVariable;
        int encoded = object.script.variables[4];

        if (encoded == 0) return;

        objectVariable = Tools_Index_GetObject(encoded);

        Object_Script_Variable4_Set(object, 0);
        Object_Script_Variable4_Set(objectVariable, 0);
    }

    /**
     * Get the object on the given packed tile.
     * @param packed The packed tile to get the object from.
     * @return The object.
     */
    public static GObject Object_GetByPackedTile(int packed) {
        Tile t;

        if (Tile_IsOutOfMap(packed)) return null;

        t = g_map[packed];
        if (t.hasUnit) return Unit_Get_ByIndex(t.index - 1).o;
        if (t.hasStructure) return Structure_Get_ByIndex(t.index - 1).o;
        return null;
    }

    /**
     * Gets the distance from the given object to the given encoded index.
     * @param o The object.
     * @param encoded The encoded index.
     * @return The distance.
     */
    public static int Object_GetDistanceToEncoded(GObject o, int encoded) {
        Structure s;
        Tile32 position;

        s = Tools_Index_GetStructure(encoded);

        if (s != null) {
            int packed;

            position = s.o.position;
            packed = Tile_PackTile(position);

            /* ENHANCEMENT -- Originally this was o.type, where 'o' refers to a unit. */
            packed += g_table_structure_layoutEdgeTiles[g_table_structureInfo[s.o.type].layout][(Orientation_Orientation256ToOrientation8(Tile_GetDirection(o.position, position)) + 4) & 7];

            position = Tile_UnpackTile(packed);
        } else {
            position = Tools_Index_GetTile(encoded);
        }

        return Tile_GetDistance(o.position, position);
    }
}
