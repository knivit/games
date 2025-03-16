package com.tsoft.dune2.audio;

import com.tsoft.dune2.tile.Tile32;

import static com.tsoft.dune2.audio.Sound.*;
import static com.tsoft.dune2.config.ConfigService.*;
import static com.tsoft.dune2.file.FileService.File_Exists;
import static com.tsoft.dune2.file.FileService.File_Exists_GetSize;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;
import static com.tsoft.dune2.house.HouseType.HOUSE_MAX;
import static com.tsoft.dune2.strings.Language.*;
import static com.tsoft.dune2.strings.StringService.String_Get_ByIndex;
import static com.tsoft.dune2.table.TableHouseInfo.g_table_houseInfo;
import static com.tsoft.dune2.tile.TileService.Tile_GetDistancePacked;
import static com.tsoft.dune2.tile.TileService.Tile_PackTile;
import static com.tsoft.dune2.timer.TimerService.Timer_Sleep;

public class SoundService {

    static int[] g_voiceData = new int[NUM_VOICES];           /* Preloaded Voices sound data */
    static int[] g_voiceDataSize = new int[NUM_VOICES];       /* Preloaded Voices sound data size in byte */
    static int s_currentMusic = 0;                            /* Currently loaded music file. */
    static int[] s_spokenWords = new int[NUM_SPEECH_PARTS];   /* Buffer with speech to play. */
    static int s_currentVoicePriority;                        /* Priority of the currently playing Speech */

    static void Driver_Music_Play(int index, int volume) {
        Driver music = g_driverMusic;
        MSBuffer *musicBuffer = g_bufferMusic;

        if (index < 0 || index > 120 || g_gameConfig.music == 0) return;

        if (music.index == 0xFFFF) return;

        if (musicBuffer.index != 0xFFFF) {
            MPU_Stop(musicBuffer.index);
            MPU_ClearData(musicBuffer.index);
            musicBuffer.index = 0xFFFF;
        }

        musicBuffer.index = MPU_SetData(music.content, index, musicBuffer.buffer);

        MPU_Play(musicBuffer.index);
        MPU_SetVolume(musicBuffer.index, ((volume & 0xFF) * 90) / 256, 0);
    }

    static void Driver_Music_LoadFile(String musicName) {
        Driver music = g_driverMusic;
        Driver sound = g_driverSound;

        Driver_Music_Stop();

        if (music.index == 0xFFFF) return;

        if (music.content == sound.content) {
            music.content         = null;
            music.filename[0]     = '\0';
            music.contentMalloced = false;
        } else {
            Driver_UnloadFile(music);
        }

        if (sound.filename != null && musicName != NULL && strcasecmp(Drivers_GenerateFilename(musicName, music), sound.filename) == 0) {
            g_driverMusic.content         = g_driverSound.content;
            memcpy(g_driverMusic.filename, g_driverSound.filename, sizeof(g_driverMusic.filename));
            g_driverMusic.contentMalloced = g_driverSound.contentMalloced;

            return;
        }

        Driver_LoadFile(musicName, music);
    }

    static int currentMusicID = 0;

    /**
     * Plays a music.
     * @param index The index of the music to play.
     */
    public static void Music_Play(int musicID) {
        if (musicID == 0xFFFF || musicID >= 38 || musicID == currentMusicID) return;

        currentMusicID = musicID;

        if (g_table_musics[musicID].string != s_currentMusic) {
            s_currentMusic = g_table_musics[musicID].string;

            Driver_Music_Stop();
            Driver_Voice_Play(NULL, 0xFF);
            Driver_Music_LoadFile(NULL);
            Driver_Sound_LoadFile(NULL);
            Driver_Music_LoadFile(s_currentMusic);
            Driver_Sound_LoadFile(s_currentMusic);
        }

        Driver_Music_Play(g_table_musics[musicID].index, 0xFF);
    }

    /**
     * Initialises the MT-32.
     * @param index The index of the music to play.
     */
    public static void Music_InitMT32() {
        int left = 0;

        Driver_Music_LoadFile("DUNEINIT");

        Driver_Music_Play(0, 0xFF);

        GUI_DrawText(String_Get_ByIndex(15), 0, 0, 15, 12); /* "Initializing the MT-32" */

        while (Driver_Music_IsPlaying()) {
            Timer_Sleep(60);

            left += 6;
            GUI_DrawText(".", left, 10, 15, 12);
        }
    }

    /**
     * Play a voice. Volume is based on distance to position.
     * @param voiceID Which voice to play.
     * @param position Which position to play it on.
     */
    public static void Voice_PlayAtTile(int voiceID, Tile32 position) {
        if (voiceID < 0 || voiceID >= 120) return;
        if (g_gameConfig.sounds == 0) return;

        int volume = 255;
        if (position.x != 0 || position.y != 0) {
            volume = Tile_GetDistancePacked(g_minimapPosition, Tile_PackTile(position));
            if (volume > 64) volume = 64;

            volume = 255 - (volume * 255 / 80);
        }

        int index = g_table_voiceMapping[voiceID];

        if (g_enableVoices && index != 0xFFFF && g_voiceData[index] != null && g_table_voices[index].priority >= s_currentVoicePriority) {
            s_currentVoicePriority = g_table_voices[index].priority;
            memmove(g_readBuffer, g_voiceData[index], g_voiceDataSize[index]);

            Driver_Voice_Play(g_readBuffer, s_currentVoicePriority);
        } else {
            Driver_Sound_Play(voiceID, volume);
        }
    }

    /**
     * Play a voice.
     * @param voiceID The voice to play.
     */
    public static void Voice_Play(int voiceID) {
        Tile32 tile = new Tile32();
        tile.x = 0;
        tile.y = 0;
        Voice_PlayAtTile(voiceID, tile);
    }

    /**
     * Free a voice
     */
    public static void Voice_UnloadVoice(int voice) {
        if (g_voiceData[voice] != null) {
            free(g_voiceData[voice]);
            g_voiceData[voice] = null;
        }
    }

    static int currentVoiceSet = 0xFFFE;

    /**
     * Load voices.
     * voiceSet 0xFFFE is for Game Intro.
     * voiceSet 0xFFFF is for Game End.
     * @param voiceSet Voice set to load : either a HouseID, or special values 0xFFFE or 0xFFFF.
     */
    public static void Voice_LoadVoices(int voiceSet) {
        int prefixChar = ' ';

        if (!g_enableVoices) return;

        for (int voice = 0; voice < NUM_VOICES; voice++) {
            /* unload if necessary */
            switch (g_table_voices[voice].string.charAt(0)) {
                case '%':
                    if (g_config.language != LANGUAGE_ENGLISH || currentVoiceSet == voiceSet) {
                        if (voiceSet != 0xFFFF && voiceSet != 0xFFFE) break;
                    }

                    Voice_UnloadVoice(voice);
                    break;

                case '+':
                    if (voiceSet != 0xFFFF && voiceSet != 0xFFFE) break;

                    Voice_UnloadVoice(voice);
                    break;

                case '-':
                    if (voiceSet == 0xFFFF) break;

                    Voice_UnloadVoice(voice);
                    break;

                case '/':
                    if (voiceSet != 0xFFFE) break;

                    Voice_UnloadVoice(voice);
                    break;

                case '?':
                    if (voiceSet == 0xFFFF) break;

                    /* Theses are not supposed to be preloaded. check anyway */
                    Voice_UnloadVoice(voice);
                    break;

                default:
                    break;
            }
        }

        if (currentVoiceSet == voiceSet) return;

        for (int voice = 0; voice < NUM_VOICES; voice++) {
            char filename[16];
		    String str = g_table_voices[voice].string;
            sleepIdle();	/* let a chance to update screen, etc. */
            switch (str.charAt(0)) {
                case '%':
                    if (g_voiceData[voice] != null ||
                        currentVoiceSet == voiceSet || voiceSet == 0xFFFF || voiceSet == 0xFFFE) break;

                    switch (g_config.language) {
                        case LANGUAGE_FRENCH: prefixChar = 'F'; break;
                        case LANGUAGE_GERMAN: prefixChar = 'G'; break;
                        default: prefixChar = g_table_houseInfo[voiceSet].prefixChar;
                    }
                    snprintf(filename, sizeof(filename), str, prefixChar);

                    g_voiceData[voice] = Sound_LoadVoc(filename, &g_voiceDataSize[voice]);
                    break;

                case '+':
                    if (voiceSet == 0xFFFF || g_voiceData[voice] != NULL) break;

                    switch (g_config.language) {
                        case LANGUAGE_FRENCH:  prefixChar = 'F'; break;
                        case LANGUAGE_GERMAN:  prefixChar = 'G'; break;
                        default: prefixChar = 'Z'; break;
                    }
                    snprintf(filename, sizeof(filename), str + 1, prefixChar);

                    /* XXX - In the 1.07us datafiles, a few files are named differently:
                     *
                     *  moveout.voc
                     *  overout.voc
                     *  report1.voc
                     *  report2.voc
                     *  report3.voc
                     *
                     * They come without letter in front of them. To make things a bit
                     *  easier, just check if the file exists, then remove the first
                     *  letter and see if it works then.
                     */
                    if (!File_Exists(filename)) {
                        memmove(filename, filename + 1, strlen(filename));
                    }

                    g_voiceData[voice] = Sound_LoadVoc(filename, &g_voiceDataSize[voice]);
                    break;

                case '-':
                    if (voiceSet != 0xFFFF || g_voiceData[voice] != NULL) break;

                    g_voiceData[voice] = Sound_LoadVoc(str + 1, &g_voiceDataSize[voice]);
                    break;

                case '/':
                    if (voiceSet != 0xFFFE) break;

                    g_voiceData[voice] = Sound_LoadVoc(str + 1, &g_voiceDataSize[voice]);
                    break;

                case '?':
                    /* Do not preload */
                    break;

                default:
                    if (g_voiceData[voice] != NULL) break;

                    g_voiceData[voice] = Sound_LoadVoc(str, &g_voiceDataSize[voice]);
                    break;
            }
        }
        currentVoiceSet = voiceSet;
    }

    /**
     * Unload voices.
     */
    static void Voice_UnloadVoices() {
        for (int voice = 0; voice < NUM_VOICES; voice++) {
            Voice_UnloadVoice(voice);
        }
    }

    /**
     * Start playing a sound sample.
     * @param index Sample to play.
     */
    static void Sound_StartSound(int index) {
        if (index == 0xFFFF || g_gameConfig.sounds == 0 || g_table_voices[index].priority < s_currentVoicePriority) return;

        s_currentVoicePriority = g_table_voices[index].priority;

        if (g_voiceData[index] != null) {
            Driver_Voice_Play(g_voiceData[index], 0xFF);
        } else {
            char filenameBuffer[16];
            String filename = g_table_voices[index].string;
            if (filename[0] == '?') {
                snprintf(filenameBuffer, sizeof(filenameBuffer), filename + 1, g_playerHouseID < HOUSE_MAX ? g_table_houseInfo[g_playerHouseID].prefixChar : ' ');

                Driver_Voice_LoadFile(filenameBuffer, g_readBuffer, g_readBufferSize);

                Driver_Voice_Play(g_readBuffer, 0xFF);
            }
        }
    }

    /**
     * Output feedback about events of the game.
     * @param index Feedback to provide (\c 0xFFFF means do nothing, \c 0xFFFE means stop, otherwise a feedback code).
     * @note If sound is disabled, the main viewport is used to display a message.
     */
    public static void Sound_Output_Feedback(int index) {
        if (index == 0xFFFF) return;

        if (index == 0xFFFE) {
            /* Clear spoken audio. */
            for (int i = 0; i < lengthof(s_spokenWords); i++) {
                s_spokenWords[i] = 0xFFFF;
            }

            Driver_Voice_Stop();

            g_viewportMessageText = null;
            if ((g_viewportMessageCounter & 1) != 0) {
                g_viewport_forceRedraw = true;
                g_viewportMessageCounter = 0;
            }
            s_currentVoicePriority = 0;

            return;
        }

        if (g_enableVoices == 0 || g_gameConfig.sounds == 0) {
            Driver_Sound_Play(g_feedback[index].soundId, 0xFF);

            g_viewportMessageText = String_Get_ByIndex(g_feedback[index].messageId);

            if ((g_viewportMessageCounter & 1) != 0) {
                g_viewport_forceRedraw = true;
            }

            g_viewportMessageCounter = 4;

            return;
        }

        /* If nothing is being said currently, load new words. */
        if (s_spokenWords[0] == 0xFFFF) {
            for (int i = 0; i < lengthof(s_spokenWords); i++) {
                s_spokenWords[i] = (g_config.language == LANGUAGE_ENGLISH) ? g_feedback[index].voiceId[i] : g_translatedVoice[index][i];
            }
        }

        Sound_StartSpeech();
    }

    /**
     * Start speech.
     * Start a new speech fragment if possible.
     * @return Sound is produced.
     */
    static boolean Sound_StartSpeech() {
        if (g_gameConfig.sounds == 0) return false;

        if (Driver_Voice_IsPlaying()) return true;

        s_currentVoicePriority = 0;

        if (s_spokenWords[0] == 0xFFFF) return false;

        Sound_StartSound(s_spokenWords[0]);
        /* Move speech parts one place. */
        memmove(&s_spokenWords[0], &s_spokenWords[1], sizeof(s_spokenWords) - sizeof(s_spokenWords[0]));
        s_spokenWords[lengthof(s_spokenWords) - 1] = 0xFFFF;

        return true;
    }

    /**
     * Load a voice file to a malloc'd buffer.
     * @param filename The name of the file to load.
     * @return Where the file is loaded.
     */
    static boolean Sound_LoadVoc(String filename, int *retFileSize) {
        int fileSize;
        void *res;

        if (filename == null) return false;
        if (!File_Exists_GetSize(filename, &fileSize)) return false;

        fileSize += 1;
        fileSize &= 0xFFFFFFFE;

	    *retFileSize = fileSize;
        res = malloc(fileSize);
        Driver_Voice_LoadFile(filename, res, fileSize);

        return true;
    }
}
