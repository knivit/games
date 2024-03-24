package com.tsoft.dune2.table;

import com.tsoft.dune2.unit.ActionInfo;

import static com.tsoft.dune2.gui.SelectionType.SELECTIONTYPE_TARGET;
import static com.tsoft.dune2.gui.SelectionType.SELECTIONTYPE_UNIT;
import static com.tsoft.dune2.strings.Strings.*;

public class TableActionInfo {

    public static final ActionInfo[] g_table_actionInfo = new ActionInfo[] {
        new ActionInfo( /* 0 */
            /* stringID      */ STR_ATTACK,
            /* name          */ "Attack",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_TARGET,
            /* soundID       */ 21
        ),

        new ActionInfo( /* 1 */
            /* stringID      */ STR_MOVE,
            /* name          */ "Move",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_TARGET,
            /* soundID       */ 22
        ),

        new ActionInfo( /* 2 */
            /* stringID      */ STR_RETREAT,
            /* name          */ "Retreat",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 21
        ),

        new ActionInfo( /* 3 */
            /* stringID      */ STR_GUARD,
            /* name          */ "Guard",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 21
        ),

        new ActionInfo( /* 4 */
            /* stringID      */ STR_AREA_GUARD,
            /* name          */ "Area Guard",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 5 */
            /* stringID      */ STR_HARVEST,
            /* name          */ "Harvest",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_TARGET,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 6 */
            /* stringID      */ STR_RETURN,
            /* name          */ "Return",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 21
        ),

        new ActionInfo( /* 7 */
            /* stringID      */ STR_STOP2,
            /* name          */ "Stop",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 21
        ),

        new ActionInfo( /* 8 */
            /* stringID      */ STR_AMBUSH,
            /* name          */ "Ambush",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 9 */
            /* stringID      */ STR_SABOTAGE,
            /* name          */ "Sabotage",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 10 */
            /* stringID      */ STR_DIE,
            /* name          */ "Die",
            /* switchType    */ 1,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 0xFFFF
        ),

        new ActionInfo( /* 11 */
            /* stringID      */ STR_HUNT,
            /* name          */ "Hunt",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 12 */
            /* stringID      */ STR_DEPLOY,
            /* name          */ "Deploy",
            /* switchType    */ 0,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        ),

        new ActionInfo( /* 13 */
            /* stringID      */ STR_DESTRUCT,
            /* name          */ "Destruct",
            /* switchType    */ 1,
            /* selectionType */ SELECTIONTYPE_UNIT,
            /* soundID       */ 20
        )
    };
}
