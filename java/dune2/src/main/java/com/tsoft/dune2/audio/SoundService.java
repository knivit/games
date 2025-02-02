package com.tsoft.dune2.audio;

import static com.tsoft.dune2.config.ConfigService.*;
import static com.tsoft.dune2.gui.GuiService.g_viewportMessageCounter;
import static com.tsoft.dune2.strings.Language.LANGUAGE_ENGLISH;
import static com.tsoft.dune2.strings.StringService.String_Get_ByIndex;

public class SoundService {

    static int[] g_voiceData = new int[NUM_VOICES];           /*!< Preloaded Voices sound data */
    static int[] g_voiceDataSize = new int[NUM_VOICES];       /*!< Preloaded Voices sound data size in byte */
    static int s_currentMusic = 0;                            /*!< Currently loaded music file. */
    static int[] s_spokenWords = new int[NUM_SPEECH_PARTS];   /*!< Buffer with speech to play. */
    static int s_currentVoicePriority;                        /*!< Priority of the currently playing Speech */

    /**
     * Output feedback about events of the game.
     * @param index Feedback to provide (\c 0xFFFF means do nothing, \c 0xFFFE means stop, otherwise a feedback code).
     * @note If sound is disabled, the main viewport is used to display a message.
     */
    public static void Sound_Output_Feedback(int index) {
        if (index == 0xFFFF) return;

//        if (index == 0xFFFE) {
//            /* Clear spoken audio. */
//            for (int i = 0; i < s_spokenWords.length; i++) {
//                s_spokenWords[i] = 0xFFFF;
//            }
//
//            Driver_Voice_Stop();
//
//            g_viewportMessageText = null;
//            if ((g_viewportMessageCounter & 1) != 0) {
//                g_viewport_forceRedraw = true;
//                g_viewportMessageCounter = 0;
//            }
//            s_currentVoicePriority = 0;
//
//            return;
//        }
//
//        if (g_enableVoices == 0 || g_gameConfig.sounds == 0) {
//            Driver_Sound_Play(g_feedback[index].soundId, 0xFF);
//
//            g_viewportMessageText = String_Get_ByIndex(g_feedback[index].messageId);
//
//            if ((g_viewportMessageCounter & 1) != 0) {
//                g_viewport_forceRedraw = true;
//            }
//
//            g_viewportMessageCounter = 4;
//
//            return;
//        }
//
//        /* If nothing is being said currently, load new words. */
//        if (s_spokenWords[0] == 0xFFFF) {
//            for (int i = 0; i < s_spokenWords.length; i++) {
//                s_spokenWords[i] = (g_config.language == LANGUAGE_ENGLISH) ? g_feedback[index].voiceId[i] : g_translatedVoice[index][i];
//            }
//        }
//
//        Sound_StartSpeech();
    }


    /**
     * Start playing a sound sample.
     * @param index Sample to play.
     */
    public static void Sound_StartSound(int index) {
        if (index == 0xFFFF || g_gameConfig.sounds == 0 || g_table_voices[index].priority < s_currentVoicePriority) return;

        s_currentVoicePriority = g_table_voices[index].priority;

        if (g_voiceData[index] != NULL) {
            Driver_Voice_Play(g_voiceData[index], 0xFF);
        } else {
            char filenameBuffer[16];
		const char *filename;

            filename = g_table_voices[index].string;
            if (filename[0] == '?') {
                snprintf(filenameBuffer, sizeof(filenameBuffer), filename + 1, g_playerHouseID < HOUSE_MAX ? g_table_houseInfo[g_playerHouseID].prefixChar : ' ');

                Driver_Voice_LoadFile(filenameBuffer, g_readBuffer, g_readBufferSize);

                Driver_Voice_Play(g_readBuffer, 0xFF);
            }
        }
    }
}
