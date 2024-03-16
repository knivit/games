package com.tsoft.dune2.table;

import com.tsoft.dune2.animation.AnimationCommandStruct;

import static com.tsoft.dune2.animation.AnimationCommand.*;

public class TableAnimation {
    
    public static AnimationCommandStruct[][] g_table_animation_unitMove = new AnimationCommandStruct[][] {
        { /* 0 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 0),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 1 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 1),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 2 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 2),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 6),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_STOP              , 0)
        },
        { /* 3 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 3),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 7),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_STOP              , 0)
        },
        { /* 4 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 0),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 4),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_STOP              , 0)
        },
        { /* 5 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 1),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 5),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_STOP              , 0)
        },
        { /* 6 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 2),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE  , 6),
            new AnimationCommandStruct(ANIMATION_PAUSE             , 600),
            new AnimationCommandStruct(ANIMATION_STOP              , 0)
        },
        { /* 7 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        }
    };

    public static AnimationCommandStruct[][] g_table_animation_unitScript1 = new AnimationCommandStruct[][]{
        { /* 0 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 0),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 1),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 1 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 0),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 2 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PLAY_VOICE, 35),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 3 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PLAY_VOICE, 35),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        }
    };

    public static AnimationCommandStruct[][] g_table_animation_unitScript2 = new AnimationCommandStruct[][]{
        { /* 0 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 1 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 2 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PLAY_VOICE, 35),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 3 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PLAY_VOICE, 35),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        }
    };

    public static AnimationCommandStruct[][] g_table_animation_map = new AnimationCommandStruct[][]{
        { /* 0 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 1),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 1 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 1),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 2 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 0),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 3 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 0),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 4 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 5 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 6 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 7 */
            new AnimationCommandStruct(ANIMATION_SET_OVERLAY_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 600),
            new AnimationCommandStruct(ANIMATION_STOP, 0)
        },
        { /* 8 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 9 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 10 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 11 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 12 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 13 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 14 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        },
        { /* 15 */
            new AnimationCommandStruct(ANIMATION_STOP, 0),
        }
    };

    public static AnimationCommandStruct[][] g_table_animation_structure = new AnimationCommandStruct[][]{
        { /* 0 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 1),
            new AnimationCommandStruct(ANIMATION_PAUSE, 300),
            new AnimationCommandStruct(ANIMATION_ABORT, 0)
        },
        { /* 1 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 0),
            new AnimationCommandStruct(ANIMATION_ABORT, 0)
        },
        { /* 2 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 300),
            new AnimationCommandStruct(ANIMATION_ABORT, 0)
        },
        { /* 3 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 4 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 5 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 6 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 7 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 8),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 9),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 8 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 9 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 10 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 11 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 12 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 13 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 14 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 15 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 16 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 4),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 17 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 18 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 7),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 19 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 8),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 9),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 20 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 21 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 22 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 23 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 8),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 9),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 24 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 25 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 5),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 6),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 9),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 8),
            new AnimationCommandStruct(ANIMATION_PAUSE, 60),
            new AnimationCommandStruct(ANIMATION_FORWARD, -4)
        },
        { /* 26 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 27 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        },
        { /* 28 */
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 2),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_SET_GROUND_TILE, 3),
            new AnimationCommandStruct(ANIMATION_PAUSE, 30),
            new AnimationCommandStruct(ANIMATION_REWIND, 0)
        }
    };
}
