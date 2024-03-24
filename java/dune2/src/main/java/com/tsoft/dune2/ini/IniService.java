package com.tsoft.dune2.ini;

import static com.tsoft.dune2.utils.CFunc.atoi;

public class IniService {

    public static String Ini_GetString(String category, String key, String defaultValue, String dest, int length, String source) {
        char *s;
        char buffer[1024];
        int catLength;
        char *current;
        char *ret;

        if (dest != null) {
		    *dest = '\0';
            /* Set the default value in case we jump out early */
            if (defaultValue != null) strncpy(dest, defaultValue, length);
            dest[length - 1] = '\0';
        }

        if (source == null) return null;

        sprintf(buffer, "[%s]", category);
        for (s = buffer; *s != '\0'; s++) *s = toupper(*s);
        catLength = (int)strlen(buffer);

        ret = source;

        for (current = source; current != null; current++) {
		const char *end;

            current = strchr(current, '[');
            if (current == null) break;

            if (strncasecmp(current, buffer, catLength) != 0) continue;
            if (current != source && *(current - 1) != '\r' && *(current - 1) != '\n') continue;

            current += catLength;
            while (isspace((uint8)*current)) current++;

            /* Find the end of this block */
            for (end = current; end != null; end++) {
                end = strchr(end, '[');
                if (end == null) break;

                if (*(end - 1) == '\r' || *(end - 1) == '\n') break;
            }

            /* If there is no other '[', take the last char of the file */
            if (end == null) end = current + strlen(current);

            if (key != null) {
                int keyLength = (int)strlen(key);

                ret = current;

                while (current < end) {
                    char *value;
                    char *lineEnd;

                    /* Check to see if there is nothing behind the key ('a' should not match 'aa') */
                    value = current + keyLength;
                    while (isspace((uint8)*value)) value++;

                    /* Now validate the size and if we match at all */
                    if (*value != '=' || strncasecmp(current, key, keyLength) != 0) {
                        /* Search for LF to support both CR/LF and LF line endings. */
                        current = strchr(current, '\n');
                        if (current == null) break;
                        while (isspace((uint8)*current)) current++;
                        if (current > end) break;

                        continue;
                    }

                    ret = current;

                    /* Get the value */
                    current = value + 1;

                    /* Find the end of the line */
                    lineEnd = strchr(current, '\n');
                    if (lineEnd == null) break;
                    while (isspace((uint8)*lineEnd)) lineEnd++;
                    if (lineEnd > end) break;

                    /* Copy the value */
                    if (dest != null) {
                        int len = (int)(lineEnd - current);
                        memcpy(dest, current, len);
					*(dest + len) = '\0';

                        String_Trim(dest);
                    }

                    return ret;
                }

                /* Failed to find the key. Return anyway. */
                return null;
            }

            ret = current;
            if (dest == null) return ret;

            /* Read all the keys from this section */
            while (true) {
                int len;
                char *lineEnd;

                lineEnd = strchr(current, '=');
                if (lineEnd == null || lineEnd > end) break;

                len = (int)(lineEnd - current);
                memcpy(dest, current, len);
			    *(dest + len) = '\0';

                String_Trim(dest);
                dest += strlen(dest) + 1;

                /* Find the next line, ignoring all \r\n */
                current = strchr(current, '\n');
                if (current == null) break;
                while (isspace((uint8)*current)) current++;
                if (current > end) break;
            }

		*dest++ = '\0';
            /* end the list with a zero element */
		*dest++ = '\0';

            return ret;
        }

        return null;
    }

    public static int Ini_GetInteger(String category, String key, int defaultValue, char *source) {
        char value[16];
        char buffer[16];

        sprintf(value, "%d", defaultValue);

        Ini_GetString(category, key, value, buffer, 15, source);
        return atoi(buffer);
    }

    static void Ini_SetString(String category, String key, String value, char *source) {
        char *s;
        char buffer[120];

        if (source == null || category == null) return;

        s = Ini_GetString(category, null, null, null, 0, source);
        if (s == null && key != null) {
            sprintf(buffer, "\r\n[%s]\r\n", category);
            strcat(source, buffer);
        }

        s = Ini_GetString(category, key, null, null, 0, source);
        if (s != null) {
            int count = (int)strcspn(s, "\r\n");
            if (count != 0) {
                /* Drop first line if not empty */
                size_t len = strlen(s + count + 1) + 1;
                memmove(s, s + count + 1, len);
            }
            if (*s == '\n') {
                /* Drop first line if empty */
                size_t len = strlen(s + 1) + 1;
                memmove(s, s + 1, len);
            }
        } else {
            s = Ini_GetString(category, null, null, null, 0, source);
        }

        if (value != null) {
            sprintf(buffer, "%s=%s\r\n", key, value);
            memmove(s + strlen(buffer), s, strlen(s) + 1);
            memcpy(s, buffer, strlen(buffer));
        }
    }
}
