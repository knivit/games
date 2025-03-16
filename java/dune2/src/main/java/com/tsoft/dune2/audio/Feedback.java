package com.tsoft.dune2.audio;

public class Feedback {

    public int[] voiceId;           /* English spoken text. */
    public int messageId;           /* Message to display in the viewport when audio is disabled. */
    public int soundId;             /* Sound. */

    public Feedback(int[] voiceId, int messageId, int soundId) {
        this.voiceId = voiceId;
        this.messageId = messageId;
        this.soundId = soundId;
    }
}
