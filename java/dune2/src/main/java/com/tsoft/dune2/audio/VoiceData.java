package com.tsoft.dune2.audio;

/** Information about sound files. */
public class VoiceData {

    public String string;      /* Pointer to a string. */
    public int priority;       /* priority */

    public VoiceData(String string, int priority) {
        this.string = string;
        this.priority = priority;
    }
}
