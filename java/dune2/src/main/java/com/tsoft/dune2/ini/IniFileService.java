package com.tsoft.dune2.ini;

import com.tsoft.dune2.config.DuneCfg;

import static com.tsoft.dune2.ini.IniService.Ini_GetString;
import static com.tsoft.dune2.strings.Language.*;

public class IniFileService {

    static char * g_openduneini = NULL;

    /**
     * Find and read the opendune.ini file
     *
     * @return True if and only if opendune.ini file was found and read.
     */
    boolean Load_IniFile() {
        FILE *f = NULL;
        long fileSize;
	/* look for opendune.ini in the following locations :
	   1) %APPDATA%/OpenDUNE (win32)
	      ~/Library/Application Support/OpenDUNE (Mac OS X)
	      ~/.config/opendune (Linux)
	      B_USER_SETTINGS_DIRECTORY (Haiku)
	   2) B_SYSTEM_SETTINGS_DIRECTORY (Haiku)
	      current directory
	   3) data/ dir
	   4) parent of bundle dir (Mac OS X)
	*/
#if defined(_WIN32)
        TCHAR path[MAX_PATH];
        if (SHGetFolderPath( NULL, CSIDL_APPDATA/*CSIDL_COMMON_APPDATA*/, NULL, 0, path ) != S_OK) {
            Warning("Cannot find AppData directory.\n");
        } else {
            PathAppend(path, TEXT("OpenDUNE\\opendune.ini"));
            f = fopen(path, "rb");
        }
#elif defined(__HAIKU__)  /* _WIN32 */
        char path[B_PATH_NAME_LENGTH];
        char buffer[B_PATH_NAME_LENGTH];
        if (find_directory(B_USER_SETTINGS_DIRECTORY, 0, true, buffer, sizeof(buffer)) == B_OK) {
            snprintf(path, sizeof(path), "%s/opendune/opendune.ini", buffer);
            f = fopen(path, "rb");
        }
        if (f == NULL) {
            Warning("%s not found; using default one...\n", path);
            if (find_directory(B_SYSTEM_SETTINGS_DIRECTORY, 0, true, buffer, sizeof(buffer)) == B_OK) {
                snprintf(path, sizeof(path), "%s/opendune.ini", buffer);
                f = fopen(path, "rb");
            }
        }
#elif !defined(TOS) && !defined(DOS)  /* __HAIKU__ */
        char path[PATH_MAX];
        char * homeDir;
        homeDir = getenv("HOME");
        if (homeDir != NULL) {
#if defined(__APPLE__)
            snprintf(path, sizeof(path), "%s/Library/Application Support/OpenDUNE/opendune.ini", homeDir);
#else /* __APPLE__ */
            snprintf(path, sizeof(path), "%s/.config/opendune/opendune.ini", homeDir);
#endif /* __APPLE__ */
                f = fopen(path, "rb");
        }
#endif /* not TOS, not _WIN32 */
        if (f == NULL) {
            /* current directory */
            f = fopen("opendune.ini", "rb");
        }
        if (f == NULL) {
            f = fopen("data/opendune.ini", "rb");
        }
#ifdef OSX
        if (f == NULL) {
            CFBundleRef mainBundle = CFBundleGetMainBundle();
            if (mainBundle != NULL) {
                CFURLRef bundleURL = CFBundleCopyBundleURL(mainBundle);
                if (bundleURL != NULL) {
                    size_t len;
                    CFStringRef bundleDir = CFURLCopyFileSystemPath(bundleURL, kCFURLPOSIXPathStyle);
                    CFStringGetFileSystemRepresentation(bundleDir, path, sizeof(path));
                    len = strlen(path);
                    strncpy(path + len, "/../opendune.ini", sizeof(path) - len);
                    Debug("trying to open %s\n", path);
                    f = fopen(path, "rb");
                    CFRelease(bundleDir);
                    CFRelease(bundleURL);
                }
            }
        }
#endif /* OSX */
        if (f == NULL) {
            Warning("opendune.ini file not found.\n");
            return false;
        }
        if (fseek(f, 0, SEEK_END) < 0) {
            Error("Cannot get opendune.ini file size.\n");
            fclose(f);
            return false;
        }
        fileSize = ftell(f);
        if (fileSize < 0) {
            Error("Cannot get opendune.ini file size.\n");
            fclose(f);
            return false;
        }
        rewind(f);
        g_openduneini = malloc(fileSize + 1);
        if (g_openduneini == NULL) {
            Error("Cannot allocate %ld bytes\n", fileSize + 1);
            fclose(f);
            return false;
        }
        if ((long)fread(g_openduneini, 1, fileSize, f) != fileSize) {
            Error("Failed to read opendune.ini\n");
            fclose(f);
            free(g_openduneini);
            g_openduneini = NULL;
            return false;
        }
        g_openduneini[fileSize] = '\0';
        fclose(f);
        return true;
    }

    /**
     * Release opendune.ini malloc'd buffer
     */
    void Free_IniFile() {
        free(g_openduneini);
        g_openduneini = NULL;
    }

    /**
     * Set language depending on value in opendune.ini
     *
     * @param config dune config to modify
     * @return False in case of error
     */
    boolean SetLanguage_From_IniFile(DuneCfg config) {
        char language[16];

        if (config == NULL || g_openduneini == NULL) return false;
        if (IniFile_GetString("language", NULL, language, sizeof(language)) == NULL) {
            return false;
        }
        if (strcasecmp(language, "ENGLISH") == 0)
            config.language = LANGUAGE_ENGLISH;
        else if(strcasecmp(language, "FRENCH") == 0)
            config.language = LANGUAGE_FRENCH;
        else if(strcasecmp(language, "GERMAN") == 0)
            config.language = LANGUAGE_GERMAN;
        else if(strcasecmp(language, "ITALIAN") == 0)
            config.language = LANGUAGE_ITALIAN;
        else if(strcasecmp(language, "SPANISH") == 0)
            config.language = LANGUAGE_SPANISH;
        return true;
    }

    public static String IniFile_GetString(String key, String defaultValue, char *dest, uint16 length) {
        int i;
        /* if g_openduneini is NULL, Ini_GetString() still does what we expect */
        String p = Ini_GetString("opendune", key, defaultValue, dest, length, g_openduneini);
        if (p != null) {
            /* Trim space from the beginning of the dest */
            for (i = 0; i < length && (dest[i] == ' ' || dest[i] == '\t') && (dest[i] != '\0'); i++);
            if (i > 0 && i < length) memmove(dest, dest+i, length - i);
        }
        return p;
    }

    public static int IniFile_GetInteger(String key, int defaultValue) {
        if (g_openduneini == NULL) return defaultValue;
        return Ini_GetInteger("opendune", key, defaultValue, g_openduneini);
    }

}
