package com.tsoft.dune2.script;

/**
 * A ScriptInfo as stored in the memory.
 */
public class ScriptInfo {

    public int text;                      /*!< Pointer to TEXT section of the scripts. */
    public int start;                     /*!< Pointer to the begin of the scripts. */
    public int[] offsets;                 /*!< Pointer to an array of offsets of where to start with a script for a typeID. */
    public int offsetsCount;              /*!< Number of words in offsets array. */
    public int startCount;                /*!< Number of words in start. */
    public ScriptFunction[] functions;    /*!< Pointer to an array of functions pointers which scripts with this scriptInfo can call. */
    public int isAllocated;               /*!< Memory has been allocated on load. */
}
