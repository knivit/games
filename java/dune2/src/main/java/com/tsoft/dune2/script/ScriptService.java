package com.tsoft.dune2.script;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.file.FileService.*;
import static com.tsoft.dune2.os.EndianService.BETOH16;
import static com.tsoft.dune2.os.EndianService.HTOBE32;
import static com.tsoft.dune2.script.Script.*;
import static com.tsoft.dune2.script.ScriptCommand.*;
import static com.tsoft.dune2.script.ScriptStructureService.*;

public class ScriptService {

    public static final int SCRIPT_UNIT_OPCODES_PER_TICK = 50;         /* The amount of opcodes a unit can execute per tick. */
    public static final int SCRIPT_FUNCTIONS_COUNT = 64;               /* There are never more than 64 functions for a script category. */

    public static GObject g_scriptCurrentObject;
    public static Structure g_scriptCurrentStructure;
    public static Unit g_scriptCurrentUnit;
    public static Team g_scriptCurrentTeam;

    static ScriptInfo s_scriptStructure;
    static ScriptInfo s_scriptTeam;
    static ScriptInfo s_scriptUnit;
    public static ScriptInfo g_scriptStructure = s_scriptStructure;
    public static ScriptInfo g_scriptTeam = s_scriptTeam;
    public static ScriptInfo g_scriptUnit = s_scriptUnit;

    /**
     * Converted script functions for Structures.
     */
    static ScriptFunction[] g_scriptFunctionsStructure = new ScriptFunction[] {
        /* 00 */ General::Script_General_Delay,
        /* 01 */ General::Script_General_NoOperation,
        /* 02 */ ScriptStructureService::Script_Structure_Unknown0A81,
        /* 03 */ ScriptStructureService::Script_Structure_FindUnitByType,
        /* 04 */ ScriptStructureService::Script_Structure_SetState,
        /* 05 */ General::Script_General_DisplayText,
        /* 06 */ ScriptStructureService::Script_Structure_Unknown11B9,
        /* 07 */ ScriptStructureService::Script_Structure_Unknown0C5A,
        /* 08 */ ScriptStructureService::Script_Structure_FindTargetUnit,
        /* 09 */ ScriptStructureService::Script_Structure_RotateTurret,
        /* 0A */ ScriptStructureService::Script_Structure_GetDirection,
        /* 0B */ ScriptStructureService::Script_Structure_Fire,
        /* 0C */ General::Script_General_NoOperation,
        /* 0D */ ScriptStructureService::Script_Structure_GetState,
        /* 0E */ ScriptStructureService::Script_Structure_VoicePlay,
        /* 0F */ ScriptStructureService::Script_Structure_RemoveFogAroundTile,
        /* 10 */ General::Script_General_NoOperation,
        /* 11 */ General::Script_General_NoOperation,
        /* 12 */ General::Script_General_NoOperation,
        /* 13 */ General::Script_General_NoOperation,
        /* 14 */ General::Script_General_NoOperation,
        /* 15 */ ScriptStructureService::Script_Structure_RefineSpice,
        /* 16 */ ScriptStructureService::Script_Structure_Explode,
        /* 17 */ ScriptStructureService::Script_Structure_Destroy,
        /* 18 */ General::Script_General_NoOperation,
    };

    /**
     * Converted script functions for Units.
    */
    public static ScriptFunction[] g_scriptFunctionsUnit = new ScriptFunction[] {
        /* 00 */ ScriptUnitService::Script_Unit_GetInfo,
        /* 01 */ ScriptUnitService::Script_Unit_SetAction,
        /* 02 */ General::Script_General_DisplayText,
        /* 03 */ General::Script_General_GetDistanceToTile,
        /* 04 */ ScriptUnitService::Script_Unit_StartAnimation,
        /* 05 */ ScriptUnitService::Script_Unit_SetDestination,
        /* 06 */ ScriptUnitService::Script_Unit_GetOrientation,
        /* 07 */ ScriptUnitService::Script_Unit_SetOrientation,
        /* 08 */ ScriptUnitService::Script_Unit_Fire,
        /* 09 */ ScriptUnitService::Script_Unit_MCVDeploy,
        /* 0A */ ScriptUnitService::Script_Unit_SetActionDefault,
        /* 0B */ ScriptUnitService::Script_Unit_Blink,
        /* 0C */ ScriptUnitService::Script_Unit_CalculateRoute,
        /* 0D */ General::Script_General_IsEnemy,
        /* 0E */ ScriptUnitService::Script_Unit_ExplosionSingle,
        /* 0F */ ScriptUnitService::Script_Unit_Die,
        /* 10 */ General::Script_General_Delay,
        /* 11 */ General::Script_General_IsFriendly,
        /* 12 */ ScriptUnitService::Script_Unit_ExplosionMultiple,
        /* 13 */ ScriptUnitService::Script_Unit_SetSprite,
        /* 14 */ ScriptUnitService::Script_Unit_TransportDeliver,
        /* 15 */ General::Script_General_NoOperation,
        /* 16 */ ScriptUnitService::Script_Unit_MoveToTarget,
        /* 17 */ General::Script_General_RandomRange,
        /* 18 */ General::Script_General_FindIdle,
        /* 19 */ ScriptUnitService::Script_Unit_SetDestinationDirect,
        /* 1A */ ScriptUnitService::Script_Unit_Stop,
        /* 1B */ ScriptUnitService::Script_Unit_SetSpeed,
        /* 1C */ ScriptUnitService::Script_Unit_FindBestTarget,
        /* 1D */ ScriptUnitService::Script_Unit_GetTargetPriority,
        /* 1E */ ScriptUnitService::Script_Unit_MoveToStructure,
        /* 1F */ ScriptUnitService::Script_Unit_IsInTransport,
        /* 20 */ ScriptUnitService::Script_Unit_GetAmount,
        /* 21 */ ScriptUnitService::Script_Unit_RandomSoldier,
        /* 22 */ ScriptUnitService::Script_Unit_Pickup,
        /* 23 */ ScriptUnitService::Script_Unit_CallUnitByType,
        /* 24 */ ScriptUnitService::Script_Unit_Unknown2552,
        /* 25 */ ScriptUnitService::Script_Unit_FindStructure,
        /* 26 */ General::Script_General_VoicePlay,
        /* 27 */ ScriptUnitService::Script_Unit_DisplayDestroyedText,
        /* 28 */ ScriptUnitService::Script_Unit_RemoveFog,
        /* 29 */ General::Script_General_SearchSpice,
        /* 2A */ ScriptUnitService::Script_Unit_Harvest,
        /* 2B */ General::Script_General_NoOperation,
        /* 2C */ General::Script_General_GetLinkedUnitType,
        /* 2D */ General::Script_General_GetIndexType,
        /* 2E */ General::Script_General_DecodeIndex,
        /* 2F */ ScriptUnitService::Script_Unit_IsValidDestination,
        /* 30 */ ScriptUnitService::Script_Unit_GetRandomTile,
        /* 31 */ ScriptUnitService::Script_Unit_IdleAction,
        /* 32 */ General::Script_General_UnitCount,
        /* 33 */ ScriptUnitService::Script_Unit_GoToClosestStructure,
        /* 34 */ General::Script_General_NoOperation,
        /* 35 */ General::Script_General_NoOperation,
        /* 36 */ ScriptUnitService::Script_Unit_Sandworm_GetBestTarget,
        /* 37 */ ScriptUnitService::Script_Unit_Unknown2BD5,
        /* 38 */ General::Script_General_GetOrientation,
        /* 39 */ General::Script_General_NoOperation,
        /* 3A */ ScriptUnitService::Script_Unit_SetTarget,
        /* 3B */ General::Script_General_Unknown0288,
        /* 3C */ General::Script_General_DelayRandom,
        /* 3D */ ScriptUnitService::Script_Unit_Rotate,
        /* 3E */ General::Script_General_GetDistanceToObject,
        /* 3F */ General::Script_General_NoOperation,
    };

    /**
     * Converted script functions for Teams.
     */
    static ScriptFunction[] g_scriptFunctionsTeam = new ScriptFunction[] {
        /* 00 */ General::Script_General_Delay,
        /* 01 */ ScriptTeamService::Script_Team_DisplayText,
        /* 02 */ ScriptTeamService::Script_Team_GetMembers,
        /* 03 */ ScriptTeamService::Script_Team_AddClosestUnit,
        /* 04 */ ScriptTeamService::Script_Team_GetAverageDistance,
        /* 05 */ ScriptTeamService::Script_Team_Unknown0543,
        /* 06 */ ScriptTeamService::Script_Team_FindBestTarget,
        /* 07 */ ScriptTeamService::Script_Team_Unknown0788,
        /* 08 */ ScriptTeamService::Script_Team_Load,
        /* 09 */ ScriptTeamService::Script_Team_Load2,
        /* 0A */ General::Script_General_DelayRandom,
        /* 0B */ General::Script_General_DisplayModalMessage,
        /* 0C */ ScriptTeamService::Script_Team_GetVariable6,
        /* 0D */ ScriptTeamService::Script_Team_GetTarget,
        /* 0E */ General::Script_General_NoOperation,
    };

    /**
     * Show a script error with additional information (Type, Index, ..).
     * @param error The error to show.
     */
    public static void Script_Error(String error, Object ... args) {
        String[] l_types = new String[] { "Unit", "Structure", "Team", "Unknown" };
	    String type;
        char[] buffer = new char[256];
        va_list va;

        if (g_scriptCurrentUnit      != null) type = l_types[0];
        if (g_scriptCurrentStructure != null) type = l_types[1];
        if (g_scriptCurrentTeam      != null) type = l_types[2];

        va_start(va, error);
        vsnprintf(buffer, sizeof(buffer), error, va);
        va_end(va);

        Error("[SCRIPT] %s; Type: %s; Index: %d; Type: %d;\n", buffer, type, g_scriptCurrentObject.index, g_scriptCurrentObject.type);
    }

    /**
     * Push a value on the stack.
     * @param value The value to push.
     * @note Use SCRIPT_PUSH(position) to use; do not use this function directly.
     */
    public static void Script_Stack_Push(ScriptEngine script, int value) {
        if (script.stackPointer == 0) {
            Script_Error("Stack Overflow at %s:%d", "filename", "lineno");
            script.script = null;
            return;
        }

        script.stack[--script.stackPointer] = value;
    }

    /**
     * Pop a value from the stack.
     * @return The value that was on the stack.
     * @note Use SCRIPT_POP(position) to use; do not use this function directly.
     */
    public static int Script_Stack_Pop(ScriptEngine script) {
        if (script.stackPointer >= 15) {
            Script_Error("Stack Overflow at %s:%d", "filename", "lineno");
            script.script = null;
            return 0;
        }

        return script.stack[script.stackPointer++];
    }

    /**
     * Peek a value from the stack.
     * @param position At which position you want to peek (1 = current, ..).
     * @return The value that was on the stack.
     * @note Use SCRIPT_PEEK(position) to use; do not use this function directly.
     */
    public static int Script_Stack_Peek(ScriptEngine script, int position) {
        assert(position > 0);

        if (script.stackPointer >= 16 - position) {
            Script_Error("Stack Overflow at %s:%d", "filename", "lineno");
            script.script = null;
            return 0;
        }

        return script.stack[script.stackPointer + position - 1];
    }

    /**
     * Reset a script engine. It forgets the correct script it was executing,
     *  and resets stack and frame pointer. It also loads in the scriptInfo given
     *  by the parameter.
     *
     * @param script The script engine to reset.
     * @param scriptInfo The scriptInfo to load in the script. Can be null.
     */
    public static void Script_Reset(ScriptEngine script, ScriptInfo scriptInfo) {
        if (script == null) return;
        if (scriptInfo == null) return;

        script.script = null;
        script.scriptInfo = scriptInfo;
        script.isSubroutine = 0;
        script.framePointer = 17;
        script.stackPointer = 15;
    }

    /**
     * Load a script in an engine. As script.scriptInfo already defines most
     *  of the information needed to load such script, all it needs is the type
     *  it needs to load the script for.
     *
     * @param script The script engine to load a script for.
     * @param typeID The typeID for which we want to load a script.
     */
    public static void Script_Load(ScriptEngine script, int typeID) {
        ScriptInfo scriptInfo;

        if (script == null) return;

        if (script.scriptInfo == null) return;
        scriptInfo = script.scriptInfo;

        Script_Reset(script, scriptInfo);

        script.script = scriptInfo.start + scriptInfo.offsets[typeID];
    }

    /**
     * Check if a script is loaded in an engine. If returning true it means that
     *  the engine is actively executing a script.
     *
     * @param script The script engine to check on.
     * @return Returns true if and only if the script engine is actively executing a script.
     */
    public static boolean Script_IsLoaded(ScriptEngine script) {
        if (script == null) return false;
        if (script.script == null) return false;
        if (script.scriptInfo == null) return false;

        return true;
    }

    /**
     * Run the next opcode of a script.
     *
     * @param script The script engine to run.
     * @return Returns false if and only if there was an scripting error, like
     *   invalid opcode.
     */
    public static boolean Script_Run(ScriptEngine script) {
        if (!Script_IsLoaded(script)) return false;

        ScriptInfo scriptInfo = script.scriptInfo;

        int current = BETOH16(script.script);
        script.script++;

        int opcode = (current >> 8) & 0x1F;
        int parameter = 0;

        if ((current & 0x8000) != 0) {
            /* When this flag is set, the instruction is a GOTO with a 13bit address */
            opcode = 0;
            parameter = current & 0x7FFF;
        } else if ((current & 0x4000) != 0) {
            /* When this flag is set, the parameter is part of the instruction */
            parameter = (current & 0xFF);
        } else if ((current & 0x2000) != 0) {
            /* When this flag is set, the parameter is in the next opcode */
            parameter = BETOH16(script.script);
            script.script++;
        }

        switch (opcode) {
            case SCRIPT_JUMP: {
                script.script = scriptInfo.start + parameter;
                return true;
            }

            case SCRIPT_SETRETURNVALUE: {
                script.returnValue = parameter;
                return true;
            }

            case SCRIPT_PUSH_RETURN_OR_LOCATION: {
                if (parameter == 0) { /* PUSH RETURNVALUE */
                    STACK_PUSH(script, script.returnValue);
                    return true;
                }

                if (parameter == 1) { /* PUSH NEXT LOCATION + FRAMEPOINTER */
                    long location;
                    location = (script.script - scriptInfo.start) + 1;

                    STACK_PUSH(script, location);
                    STACK_PUSH(script, script.framePointer);
                    script.framePointer = script.stackPointer + 2;

                    return true;
                }

                Script_Error("Unknown parameter %d for opcode 2", parameter);
                script.script = null;
                return false;
            }

            case SCRIPT_PUSH: case SCRIPT_PUSH2: {
                STACK_PUSH(script, parameter);
                return true;
            }

            case SCRIPT_PUSH_VARIABLE: {
                STACK_PUSH(script, script.variables[parameter]);
                return true;
            }

            case SCRIPT_PUSH_LOCAL_VARIABLE: {
                if (script.framePointer - parameter - 2 >= 15) {
                    Script_Error("Stack Overflow at %s:%d");
                    script.script = null;
                    return false;
                }

                STACK_PUSH(script, script.stack[script.framePointer - parameter - 2]);
                return true;
            }

            case SCRIPT_PUSH_PARAMETER: {
                if (script.framePointer + parameter - 1 >= 15) {
                    Script_Error("Stack Overflow at %s:%d");
                    script.script = null;
                    return false;
                }

                STACK_PUSH(script, script.stack[script.framePointer + parameter - 1]);
                return true;
            }

            case SCRIPT_POP_RETURN_OR_LOCATION: {
                if (parameter == 0) { /* POP RETURNVALUE */
                    script.returnValue = STACK_POP(script);
                    return true;
                }
                if (parameter == 1) { /* POP FRAMEPOINTER + LOCATION */
                    STACK_PEEK(script, 2);
                    if (script.script == null) return false;

                    script.framePointer = STACK_POP(script);
                    script.script = scriptInfo.start + STACK_POP(script);
                    return true;
                }

                Script_Error("Unknown parameter %d for opcode 8", parameter);
                script.script = null;
                return false;
            }

            case SCRIPT_POP_VARIABLE: {
                script.variables[parameter] = STACK_POP(script);
                return true;
            }

            case SCRIPT_POP_LOCAL_VARIABLE: {
                if (script.framePointer - parameter - 2 >= 15) {
                    Script_Error("Stack Overflow at %s:%d");
                    script.script = null;
                    return false;
                }

                script.stack[script.framePointer - parameter - 2] = STACK_POP(script);
                return true;
            }

            case SCRIPT_POP_PARAMETER: {
                if (script.framePointer + parameter - 1 >= 15) {
                    Script_Error("Stack Overflow at %s:%d");
                    script.script = null;
                    return false;
                }

                script.stack[script.framePointer + parameter - 1] = STACK_POP(script);
                return true;
            }

            case SCRIPT_STACK_REWIND: {
                script.stackPointer += parameter;
                return true;
            }

            case SCRIPT_STACK_FORWARD: {
                script.stackPointer -= parameter;
                return true;
            }

            case SCRIPT_FUNCTION: {
                parameter &= 0xFF;

                if (parameter >= SCRIPT_FUNCTIONS_COUNT || scriptInfo.functions[parameter] == null) {
                    Script_Error("Unknown function %d for opcode 14", parameter);
                    return false;
                }

                script.returnValue = scriptInfo.functions[parameter](script);
                return true;
            }

            case SCRIPT_JUMP_NE: {
                STACK_PEEK(script, 1);
                if (script.script == null) return false;

                if (STACK_POP(script) != 0) return true;

                script.script = scriptInfo.start + (parameter & 0x7FFF);
                return true;
            }

            case SCRIPT_UNARY: {
                if (parameter == 0) { /* STACK = !STACK */
                    STACK_PUSH(script, (STACK_POP(script) == 0) ? 1 : 0);
                    return true;
                }
                if (parameter == 1) { /* STACK = -STACK */
                    STACK_PUSH(script, -STACK_POP(script));
                    return true;
                }
                if (parameter == 2) { /* STACK = ~STACK */
                    STACK_PUSH(script, ~STACK_POP(script));
                    return true;
                }

                Script_Error("Unknown parameter %d for opcode 16", parameter);
                script.script = null;
                return false;
            }

            case SCRIPT_BINARY: {
                int right = STACK_POP(script);
                int left  = STACK_POP(script);

                switch (parameter) {
                    case 0:  STACK_PUSH(script, (left != 0 && right != 0) ? 1 : 0); break; /* left && right */
                    case 1:  STACK_PUSH(script, (left != 0 || right != 0) ? 1 : 0); break; /* left || right */
                    case 2:  STACK_PUSH(script, (left == right) ? 1 : 0); break; /* left == right */
                    case 3:  STACK_PUSH(script, (left != right) ? 1 : 0); break; /* left != right */
                    case 4:  STACK_PUSH(script, (left <  right) ? 1 : 0); break; /* left <  right */
                    case 5:  STACK_PUSH(script, (left <= right) ? 1 : 0); break; /* left <= right */
                    case 6:  STACK_PUSH(script, (left >  right) ? 1 : 0); break; /* left >  right */
                    case 7:  STACK_PUSH(script, (left >= right) ? 1 : 0); break; /* left >= right */
                    case 8:  STACK_PUSH(script, left +  right); break; /* left +  right */
                    case 9:  STACK_PUSH(script, left -  right); break; /* left -  right */
                    case 10: STACK_PUSH(script, left *  right); break; /* left *  right */
                    case 11: STACK_PUSH(script, left /  right); break; /* left /  right */
                    case 12: STACK_PUSH(script, left >> right); break; /* left >> right */
                    case 13: STACK_PUSH(script, left << right); break; /* left << right */
                    case 14: STACK_PUSH(script, left &  right); break; /* left &  right */
                    case 15: STACK_PUSH(script, left |  right); break; /* left |  right */
                    case 16: STACK_PUSH(script, left %  right); break; /* left %  right */
                    case 17: STACK_PUSH(script, left ^  right); break; /* left ^  right */

                    default:
                        Script_Error("Unknown parameter %d for opcode 17", parameter);
                        script.script = null;
                        return false;
                }

                return true;
            }
            case SCRIPT_RETURN: {
                STACK_PEEK(script, 2);
                if (script.script == null) return false;

                script.returnValue = STACK_POP(script);
                script.script = scriptInfo.start + STACK_POP(script);

                script.isSubroutine = 0;
                return true;
            }

            default:
                Script_Error("Unknown opcode %d", opcode);
                script.script = null;
                return false;
        }
    }

    /**
     * Load a script in an engine without removing the previously loaded script.
     *
     * @param script The script engine to run.
     * @param typeID The typeID for which we want to load a script.
     */
    public static void Script_LoadAsSubroutine(ScriptEngine script, int typeID) {
        ScriptInfo scriptInfo;

        if (!Script_IsLoaded(script)) return;
        if (script.isSubroutine != 0) return;

        scriptInfo = script.scriptInfo;
        script.isSubroutine = 1;

        STACK_PUSH(script, (script.script - scriptInfo.start));
        STACK_PUSH(script, script.returnValue);

        script.script = scriptInfo.start + scriptInfo.offsets[typeID];
    }

    /**
     * Clears the given scriptInfo.
     *
     * @param scriptInfo The scriptInfo to clear.
     */
    public static void Script_ClearInfo(ScriptInfo scriptInfo) {
        if (scriptInfo == null) return;

        if (scriptInfo.isAllocated != 0) {
            free(scriptInfo.text);
            free(scriptInfo.offsets);
            free(scriptInfo.start);
        }

        scriptInfo.text = null;
        scriptInfo.offsets = null;
        scriptInfo.start = null;
    }

    /**
     * Clears the given scriptInfo.
     *
     * @param filename The name of the file to load.
     * @param scriptInfo The scriptInfo to load in the script.
     * @param functions Pointer to the functions to call via script.
     * @param data Pointer to preallocated space to load data.
     */
    public static int Script_LoadFromFile(String filename, ScriptInfo scriptInfo, ScriptFunction[] functions, byte[] data) {
        long total = 0;
        long length = 0;
        int index;
        int i;

        if (scriptInfo == null) return 0;
        if (filename == null) return 0;

        Script_ClearInfo(scriptInfo);

        scriptInfo.isAllocated = (data == null);

        scriptInfo.functions = functions;

        if (!File_Exists(filename)) return 0;

        index = ChunkFile_Open(filename);

        length = ChunkFile_Seek(index, HTOBE32(CC_TEXT));
        total += length;

        if (length != 0) {
            if (data != null) {
                scriptInfo.text = (uint16 *)data;
                data += length;
            } else {
                scriptInfo.text = calloc(1, length);
            }

            ChunkFile_Read(index, HTOBE32(CC_TEXT), scriptInfo.text, length);
        }

        length = ChunkFile_Seek(index, HTOBE32(CC_ORDR));
        total += length;

        if (length == 0) {
            Script_ClearInfo(scriptInfo);
            ChunkFile_Close(index);
            return 0;
        }

        if (data != null) {
            scriptInfo.offsets = (uint16 *)data;
            data += length;
        } else {
            scriptInfo.offsets = calloc(1, length);
        }

        scriptInfo.offsetsCount = (length >> 1) & 0xFFFF;
        ChunkFile_Read(index, HTOBE32(CC_ORDR), scriptInfo.offsets, length);

        for(i = 0; i < (int16)((length >> 1) & 0xFFFF); i++) {
            scriptInfo.offsets[i] = BETOH16(scriptInfo.offsets[i]);
        }

        length = ChunkFile_Seek(index, HTOBE32(CC_DATA));
        total += length;

        if (length == 0) {
            Script_ClearInfo(scriptInfo);
            ChunkFile_Close(index);
            return 0;
        }

        if (data != null) {
            scriptInfo.start = (uint16 *)data;
            data += length;
        } else {
            scriptInfo.start = calloc(1, length);
        }

        scriptInfo.startCount = (length >> 1) & 0xFFFF;
        ChunkFile_Read(index, HTOBE32(CC_DATA), scriptInfo.start, length);

        ChunkFile_Close(index);

        return total & 0xFFFF;
    }
}
