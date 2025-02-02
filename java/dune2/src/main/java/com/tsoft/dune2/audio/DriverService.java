package com.tsoft.dune2.audio;

public class DriverService {

    private static Driver g_driverVoice;

    public static boolean Driver_Voice_IsPlaying() {
        if (g_driverVoice.index == 0xFFFF) return false;
        return false;//DSP_GetStatus() == 2;
    }

}
