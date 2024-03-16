package com.tsoft.dune2.script;

/**
 * A ScriptEngine as stored in the memory.
 */
public class ScriptEngine {
    public int delay;                                           /*!< How many more ticks the script is suspended (or zero if not suspended). */
    public int script;                                          /*!< Pointer to current command in the script we are executing. */
    public ScriptInfo scriptInfo = new ScriptInfo();            /*!< Pointer to a struct with script information. */
    public int returnValue;                                     /*!< Return value from sub-routines. */
    public int framePointer;                                    /*!< Frame pointer. */
    public int stackPointer;                                    /*!< Stack pointer. */
    public int[] variables = new int[5];                        /*!< Variables to store values in (outside the stack). Accessed by all kinds of routines outside the scripts! */
    public int[] stack = new int[15];                           /*!< Stack of the script engine, starting at the end. */
    public int isSubroutine;                                    /*!< The script we are executing is a subroutine. */
}
