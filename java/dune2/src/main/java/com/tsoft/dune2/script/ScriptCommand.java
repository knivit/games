package com.tsoft.dune2.script;

/**
 * The valid types for command in ScriptInfo->start array.
 */
public class ScriptCommand {

    public static final int SCRIPT_JUMP                    = 0;   /* Jump to the instruction given by the parameter. */
    public static final int SCRIPT_SETRETURNVALUE          = 1;   /* Set the return value to the value given by the parameter. */
    public static final int SCRIPT_PUSH_RETURN_OR_LOCATION = 2;   /* Push the return value (parameter = 0) or the location + framepointer (parameter = 1) on the stack. */
    public static final int SCRIPT_PUSH                    = 3;   /* Push a value given by the parameter on the stack. */
    public static final int SCRIPT_PUSH2                   = 4;   /* Identical to SCRIPT_PUSH. */
    public static final int SCRIPT_PUSH_VARIABLE           = 5;   /* Push a variable on the stack. */
    public static final int SCRIPT_PUSH_LOCAL_VARIABLE     = 6;   /* Push a local variable on the stack. */
    public static final int SCRIPT_PUSH_PARAMETER          = 7;   /* Push a parameter on the stack. */
    public static final int SCRIPT_POP_RETURN_OR_LOCATION  = 8;   /* Pop the return value (parameter = 0) or the location + framepointer (parameter = 1) from the stack. */
    public static final int SCRIPT_POP_VARIABLE            = 9;   /* Pop a variable from the stack. */
    public static final int SCRIPT_POP_LOCAL_VARIABLE      = 10;  /* Pop a local variable from the stack. */
    public static final int SCRIPT_POP_PARAMETER           = 11;  /* Pop a paramter from the stack. */
    public static final int SCRIPT_STACK_REWIND            = 12;  /* Add a value given by the parameter to the stackpointer. */
    public static final int SCRIPT_STACK_FORWARD           = 13;  /* Substract a value given by the parameter to the stackpointer. */
    public static final int SCRIPT_FUNCTION                = 14;  /* Execute a function by its ID given by the parameter. */
    public static final int SCRIPT_JUMP_NE                 = 15;  /* Jump to the instruction given by the parameter if the last entry on the stack is non-zero. */
    public static final int SCRIPT_UNARY                   = 16;  /* Perform unary operations. */
    public static final int SCRIPT_BINARY                  = 17;  /* Perform binary operations. */
    public static final int SCRIPT_RETURN                  = 18;  /* Return from a subroutine. */
}
