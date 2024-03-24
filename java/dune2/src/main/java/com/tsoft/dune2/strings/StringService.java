package com.tsoft.dune2.strings;

import static com.tsoft.dune2.config.ConfigService.g_config;
import static com.tsoft.dune2.file.FileService.File_ReadWholeFile;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.utils.CFunc.READ_LE_int;
import static com.tsoft.dune2.utils.CFunc.uint8;

public class StringService {
    
    
/* 941 strings and 38356 chars in English,
   833 and 39582 in French,
   899 and 42868 in German */
    static final int MAX_STRING_COUNT = 950;
    static final int MAX_CHARACTER_COUNT = 48*1024;
    static int * s_strings = NULL;
    static char * s_stringsBuffer = NULL;
    static int s_stringsCount = 0;

    static String[] g_languageSuffixes = new String[] { "ENG", "FRE", "GER", "ITA", "SPA" };

    /**
     * Decompress a string and
     * Translates 0x1B 0xXX occurences into extended ASCII values (0x7F + 0xXX).
     *
     * Characters values >= 0x80 (1AAAABBB) are unpacked to 2 characters
     * from the table. AAAA gives the 1st characted.
     * BBB the 2nd one (from a sub-table depending on AAAA)
     *
     * @param source The compressed string.
     * @param dest The decompressed and translated string.
     * @return The length of decompressed string.
     */
    public static int String_DecompressAndTranslate(String s, char[] dest, int destLen) {
        String couples =
            " etainosrlhcdupm" + 	/* 1st char */
            "tasio wb" + 	/* <SPACE>? */
            " rnsdalm" + 	/* e? */
            "h ieoras" + 	/* t? */
            "nrtlc sy" + 	/* a? */
            "nstcloer" + 	/* i? */
            " dtgesio" + 	/* n? */
            "nr ufmsw" + 	/* o? */
            " tep.ica" + 	/* s? */
            "e oiadur" + 	/* r? */
            " laeiyod" + 	/* l? */
            "eia otru" + 	/* h? */
            "etoakhlr" + 	/* c? */
            " eiu,.oa" + 	/* d? */
            "nsrctlai" + 	/* u? */
            "leoiratp" + 	/* p? */
            "eaoip bm";	/* m? */
        int count;

        for (count = 0; count < s.length(); count ++) {
            char c = s.charAt(count);
            if ((c & 0x80) != 0) {
                c &= 0x7F;
                dest[count++] = couples.charAt(c >> 3);	/* 1st char */
                c = couples.charAt(c + 16);	/* 2nd char */
            } else if (c == 0x1B) {
                count ++;
                c = new Character(0x7F + s.charAt(c));
            }
            dest[count++] = c;
            if (count >= destLen - 1) {
                Warning("String_Decompress() : truncating output !\n");
                break;
            }
        }
        dest[count] = '\0';
        return count;
    }

    /**
     * Appends ".(ENG|FRE|...)" to the given string.
     *
     * @param name The string to append extension to.
     * @return The new string.
     */
    static String String_GenerateFilename(String name) {
        assert(g_config.language < g_languageSuffixes.length);
        return name + "." + g_languageSuffixes[g_config.language];
    }

    /**
     * Returns a pointer to the string at given index in current string file.
     *
     * @param stringID The index of the string.
     * @return The pointer to the string.
     */
    public static String String_Get_ByIndex(int stringID) {
        return s_stringsBuffer + s_strings[stringID];
    }

    static void String_Load(String filename, boolean compressed, int start, int end) {
        byte[] buf;
        int count;
        int i;
        char[] buffer = new char[1024];

        buf = File_ReadWholeFile(String_GenerateFilename(filename));
        count = READ_LE_int(buf, 0) / 2;

        if (end == 0) end = start + count - 1;

        for (i = 0; i < count && s_stringsCount <= end; i++) {
            int len;
		    int srcOff = READ_LE_int(buf, i * 2);

            if (compressed) {
                len = String_DecompressAndTranslate(buf, srcOff, buffer, buffer.length);
            } else {
                strcpy(buffer, src);
            }
            String_Trim(buffer);

            len = strlen(buffer);
            if (len > 0 || s_strings[s_stringsCount] == 0) {
                memcpy(s_stringsBuffer + s_strings[s_stringsCount++], buffer, len + 1);
                s_strings[s_stringsCount] = s_strings[s_stringsCount - 1] + len + 1;
                /* s_strings[s_stringsCount] must point to the available space in buffer */
            }
        }
        free(buf);

        if (s_stringsCount == 335) {
            Warning("DUNE V1.0 message file detected\n");
            memmove(s_strings + 283, s_strings + 281, sizeof(s_strings[0]) * (335 - 281 + 1));
            s_stringsCount += 2;
            s_strings[s_stringsCount + 2] = s_strings[s_stringsCount];	/* available space */
            s_strings[s_stringsCount++] = s_strings[STR_NULL];
            s_strings[s_stringsCount++] = s_strings[STR_OLD_SAVE_GAME_FILE_IS_INCOMPATABLE_WITH_LATEST_VERSION];
        }

        /* EU version has one more string in DUNE langfile. */
        if (s_stringsCount == STR_LOAD_GAME) {
            s_strings[s_stringsCount + 1] = s_strings[s_stringsCount];	/* available space */
            s_strings[s_stringsCount++] = s_strings[STR_LOAD_A_GAME];
        }

        while (s_stringsCount <= end) {
            Warning("String_Load(%s) filling %hu 0x%hx\n", filename, s_stringsCount, s_strings[s_stringsCount]);
            s_stringsCount++;
            s_strings[s_stringsCount] = s_strings[s_stringsCount - 1];	/* available space */
        }

        Debug("String_Load(%s) done. str count = %hu, byte count = %hu\n",
            filename, s_stringsCount, s_strings[s_stringsCount]);
    }

    /**
     * Loads the language files in the memory, which is used after that with String_GetXXX_ByIndex().
     */
    static void String_Init() {
        s_stringsCount = 0;
        s_strings = malloc(sizeof(int) * MAX_STRING_COUNT);
        s_strings[s_stringsCount] = 0;	/* points to available space in buffer */
        s_stringsBuffer = malloc(MAX_CHARACTER_COUNT);
        String_Load("DUNE",     false,   1, 339);
        String_Load("MESSAGE",  false, 340, 367);
        String_Load("INTRO",    false, 368, 404);
        String_Load("TEXTH",    true,  405, 444);
        String_Load("TEXTA",    true,  445, 484);
        String_Load("TEXTO",    true,  485, 524);
        String_Load("PROTECT",  true,  525,   0);
    }

    /**
     * Unloads the language files in the memory.
     */
    public static void String_Uninit() {
        free(s_stringsBuffer);
        s_stringsBuffer = NULL;
        free(s_strings);
        s_strings = NULL;
    }

    /**
     * Go to the next string.
     * @param ptr Pointer to the current string.
     * @return Pointer to the next string.
     */
    static int String_NextString(byte[] ptr) {
        int ptrOff = uint8(ptr[0]);
        while (ptr[ptrOff] == 0) ptrOff++;
        return ptrOff;
    }

    /**
     * Go to the previous string.
     * @param ptr Pointer to the current string.
     * @return Pointer to the previous string.
     */
    static int String_PrevString(byte[] ptr) {
        do {
            ptr--;
        } while (*ptr == 0);
        ptr -= *ptr - 1;
        return ptr;
    }

    static void String_Trim(char *string) {
        char *s = string + strlen(string) - 1;
        while (s >= string && isspace((uint8)*s)) {
		*s = '\0';
            s--;
        }
    }
}
