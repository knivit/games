package com.tsoft.dune2.script;

import static com.tsoft.dune2.script.ScriptService.*;

public class Script {

    public static int STACK_PEEK(ScriptEngine script, int position) {
        return Script_Stack_Peek(script, position);
    }

    public static void STACK_PUSH(ScriptEngine script, int value) {
        Script_Stack_Push(script, value);
    }

    public static int STACK_POP(ScriptEngine script) {
        return Script_Stack_Pop(script);
    }
}
