package com.tsoft.dune2.file;

import static com.tsoft.dune2.file.ConvertCase.*;
import static com.tsoft.dune2.file.SearchDirectory.*;
import static com.tsoft.dune2.os.EndianService.HTOBE32;
import static com.tsoft.dune2.utils.CFunc.*;
import static java.lang.Math.min;

public class FileService {

    public static final int FILE_MODE_READ       = 0x01;
    public static final int FILE_MODE_WRITE      = 0x02;
    public static final int FILE_MODE_READ_WRITE = FILE_MODE_READ | FILE_MODE_WRITE;

    public static final int FILE_MAX = 8;
    public static final int FILE_INVALID = 0xFF;

    static String g_dune_data_dir = DUNE_DATA_DIR;
    static String g_personal_data_dir = ".";

    /* In order to avoid to open/close the same .PAK file multiple time
     * in a row, we cache the last opened PAK file.
     * DUNE II code is very conservative about file access, and only open
     * one file at once. */
    static FILE * s_currentPakFp = null;
    static FileInfo s_currentPakInfo = null;

    /**
     * Extensions to stdio.h
     */

    /**
     * Read a long value from a little endian file.
     */
    public static long fread_le_long(CFile stream) {
        byte[] buffer = stream.fread(4);
	    return READ_LE_long(buffer, 0);
    }

    /**
     * Read a int value from a little endian file.
     */
    public static int fread_le_int(CFile stream) {
        byte[] buffer = stream.fread(2);
        if (buffer.length != 2) throw new IllegalStateException();
	    return READ_LE_int(buffer, 0);
    }

    /**
     * Write a long value from a little endian file.
     */
    public static boolean fwrite_le_long(long value, FILE *stream) {
        if (putc(value & 0xff, stream) == EOF) return false;
        if (putc((value >> 8) & 0xff, stream) == EOF) return false;
        if (putc((value >> 16) & 0xff, stream) == EOF) return false;
        if (putc((value >> 24) & 0xff, stream) == EOF) return false;
        return true;
    }

    /**
     * Write a int value from a little endian file.
     */
    public static boolean fwrite_le_int(int value, FILE *stream) {
        if (putc(value & 0xff, stream) == EOF) return false;
        if (putc((value >> 8) & 0xff, stream) == EOF) return false;
        return true;
    }

    static void File_MakeCompleteFilename(byte[] buf, size_t len, int dir, String filename, int convert) {
        int j;
        int i = 0;

        if (dir == SEARCHDIR_GLOBAL_DATA_DIR || dir == SEARCHDIR_CAMPAIGN_DIR) {
            /* Note: campaign specific data directory not implemented. */
            i = snprintf(buf, len, "%s\\%s", g_dune_data_dir, filename);
        } else if (dir == SEARCHDIR_PERSONAL_DATA_DIR) {
            i = snprintf(buf, len, "%s\\%s", g_personal_data_dir, filename);
        }
        buf[len - 1] = '\0';

        if (i > (int)len) {
            Warning("output truncated : %s (%s)\n", buf, filename);
            i = (int)len;
        }
        if (convert != NO_CONVERT) {
            for (j = i - 1; j >= 0; j--) {
                if (buf[j] == '/' || buf[j] == '\\')
                    break;
                if (convert == CONVERT_TO_LOWERCASE) {
                    if ('A' <= buf[j] && buf[j] <= 'Z')
                        buf[j] = buf[j] + 'a' - 'A';
                } else if (convert == CONVERT_TO_UPPERCASE) {
                    if ('a' <= buf[j] && buf[j] <= 'z')
                        buf[j] = buf[j] - 'a' + 'A';
                }
            }
        }
    }

    /**
     * Open a file from the data/ directory
     */
    static FILE *fopendatadir(int dir, String name, String mode) {
        char filenameComplete[1024];
        FileInfo fileInfo;
	    String filename;

        Debug("fopendatadir(%d, %s, %s)\n", dir, name, mode);
        if (dir != SEARCHDIR_PERSONAL_DATA_DIR) {
            fileInfo = FileInfo_Find_ByName(name, null);
            if (fileInfo != null) {
                /* Take the filename from the FileInfo structure, as it was read
                 * from the data/ directory */
                filename = fileInfo.filename;
            } else {
                filename = name;
            }
            File_MakeCompleteFilename(filenameComplete, sizeof(filenameComplete), dir, filename, NO_CONVERT);
            return fopen(filenameComplete, mode);
        } else {
            FILE *f;
            /* try both in lower and upper case */
            File_MakeCompleteFilename(filenameComplete, sizeof(filenameComplete), dir, name, CONVERT_TO_UPPERCASE);
            f = fopen(filenameComplete, mode);
            if (f != null) return f;
            File_MakeCompleteFilename(filenameComplete, sizeof(filenameComplete), dir, name, CONVERT_TO_LOWERCASE);
            return fopen(filenameComplete, mode);
        }
    }
    
    static GFile[] s_file = new GFile[FILE_MAX];

    static FileInfoLinkedElem s_files_in_root = null;
    
    static PakFileInfoLinkedElem s_files_in_pak = null;

    /**
     * Find the FileInfo for the given filename.
     *
     * @param filename The filename to get the FileInfo for.
     * @return The FileInfo pointer or null if not found.
     */
    static FileInfo FileInfo_Find_ByName(String filename, FileInfo **pakInfo) {
        {
            FileInfoLinkedElem e;
            for (e = s_files_in_root; e != null; e = e.next) {
                if (!strcasecmp(e.info.filename, filename)) {
                    if (pakInfo) *pakInfo = null;
                    return &e.info;
                }
            }
        }
        {
            PakFileInfoLinkedElem e;
            for (e = s_files_in_pak; e != null; e = e.next) {
                if (!strcasecmp(e.info.filename, filename)) {
                    if (pakInfo) *pakInfo = e.pak;
                    return e.info;
                }
            }
        }
        return null;
    }

    /**
     * Internal function to truly open a file.
     *
     * @param filename The name of the file to open.
     * @param mode The mode to open the file in. Bit 1 means reading, bit 2 means writing.
     * @return An index value refering to the opened file, or FILE_INVALID.
     */
    static int _File_Open(int dir, String filename, int mode) {
        FileInfo pakInfo = null;

        if ((mode & FILE_MODE_READ_WRITE) == 0) return FILE_INVALID;

        /* Find a free spot in our limited array */
        int fileIndex;
        for (fileIndex = 0; fileIndex < FILE_MAX; fileIndex++) {
            if (s_file[fileIndex].fp == null) break;
        }

        if (fileIndex >= FILE_MAX) {
            Warning("Limit of %d open files reached.\n", FILE_MAX);
            return FILE_INVALID;
        }

        if (mode == FILE_MODE_READ && dir != SEARCHDIR_PERSONAL_DATA_DIR) {
            /* Look in PAK only for READ only files, and not Personnal files */
            FileInfo fileInfo = FileInfo_Find_ByName(filename, pakInfo);
            if (fileInfo == null) return FILE_INVALID;

            if (pakInfo == null) {
                /* Check if we can find the file outside any PAK file */
                s_file[fileIndex].fp = fopendatadir(dir, filename, "rb");
                if (s_file[fileIndex].fp == null) return FILE_INVALID;

                s_file[fileIndex].start    = 0;
                s_file[fileIndex].position = 0;
                fseek(s_file[fileIndex].fp, 0, SEEK_END);
                s_file[fileIndex].size = ftell(s_file[fileIndex].fp);
                fseek(s_file[fileIndex].fp, 0, SEEK_SET);
            } else {
                /* file is found in PAK */
                if (pakInfo != s_currentPakInfo) {
                    if (s_currentPakFp != null) fclose(s_currentPakFp);
                    s_currentPakFp = fopendatadir(dir, pakInfo.filename, "rb");
                    s_currentPakInfo = pakInfo;
                }
                s_file[fileIndex].fp = s_currentPakFp;
                if (s_file[fileIndex].fp == null) return FILE_INVALID;

                s_file[fileIndex].start    = fileInfo.filePosition;
                s_file[fileIndex].position = 0;
                s_file[fileIndex].size     = fileInfo.fileSize;

                /* Go to the start of the file now */
                fseek(s_file[fileIndex].fp, s_file[fileIndex].start, SEEK_SET);
            }
            s_file[fileIndex].pakInfo = pakInfo;
            return fileIndex;
        }

        /* Check if we can find the file outside any PAK file */
        s_file[fileIndex].fp = fopendatadir(dir, filename, (mode == FILE_MODE_WRITE) ? "wb" : ((mode == FILE_MODE_READ_WRITE) ? "wb+" : "rb"));
        if (s_file[fileIndex].fp != null) {
            s_file[fileIndex].start    = 0;
            s_file[fileIndex].position = 0;
            s_file[fileIndex].size     = 0;
            s_file[fileIndex].pakInfo  = null;

            /* We can only check the size of the file if we are reading (or appending) */
            if ((mode & FILE_MODE_READ) != 0) {
                fseek(s_file[fileIndex].fp, 0, SEEK_END);
                s_file[fileIndex].size = ftell(s_file[fileIndex].fp);
                fseek(s_file[fileIndex].fp, 0, SEEK_SET);
            }

            return fileIndex;
        }
        return FILE_INVALID;
    }

    /**
     * Memorize a file from the data/ directory.
     *
     * @param filename The name of the file.
     * @param filesize The size of the file.
     * @return A pointer to the newly created FileInfo.
     */
    static FileInfo File_Init_AddFileInRootDir(String filename, long filesize) {
        FileInfoLinkedElem elem = new FileInfoLinkedElem();

        elem.next = s_files_in_root;
        elem.info = new FileInfo();
        elem.filenamebuffer = filename;
        elem.info.filename = elem.filenamebuffer;
        elem.info.fileSize = filesize;
        elem.info.filePosition = 0;
        s_files_in_root = elem;
        return elem.info;
    }

    /**
     * Memorize a file inside a PAK file.
     *
     * @param filename the filename as indicated in PAK header.
     * @param filesize the size as calculated from PAK header.
     * @param position the position of the file from the start of the PAK file.
     * @param pakInfo FileInfo pointer for the PAK file.
     * @return A pointer to the newly created FileInfo.
     */
    static FileInfo _File_Init_AddFileInPak(String filename, long filesize, long position, FileInfo pakInfo) {
        PakFileInfoLinkedElem elem = new PakFileInfoLinkedElem();

        elem.next = s_files_in_pak;
        elem.pak = pakInfo;
        elem.info = new FileInfo();
        elem.filenamebuffer = filename;
        elem.info.filename = elem.filenamebuffer;
        elem.info.fileSize = filesize;
        elem.info.filePosition = position;
        elem.info.flags.inPAKFile = true;
        s_files_in_pak = elem;
        return elem.info;
    }

    /**
     * Process (parse) a PAK file.
     *
     * @param pakpath real path to open PAK file.
     * @param paksize size (bytes) of the PAK file.
     * @param pakInfo pointer to the FileInfo for PAK file.
     * @return True if PAK processing was ok.
     */
    static boolean _File_Init_ProcessPak(String pakpath, long paksize, FileInfo pakInfo) {
        FILE *f;
        long position;
        long nextposition;
        long size;
        char filename[256];
        int i;

        f = fopen(pakpath, "rb");
        if (f == null) {
            Error("failed to open %s", pakpath);
            return false;
        }
        if (!fread_le_long(&nextposition, f)) {
            fclose(f);
            return false;
        }

        while (nextposition != 0) {
            position = nextposition;
            for (i = 0; i < sizeof(filename); i++) {
                if (fread(filename + i, 1, 1, f) != 1) {
                    fclose(f);
                    return false;
                }
                if (filename[i] == '\0') break;
            }
            if (i == sizeof(filename)) {
                fclose(f);
                return false;
            }
            if (!fread_le_long(&nextposition, f)) {
                fclose(f);
                return false;
            }

            size = (nextposition != 0) ? nextposition - position : paksize - position;
            if (_File_Init_AddFileInPak(filename, size, position, pakInfo) == null) {
                fclose(f);
                return false;
            }
        }

        fclose(f);
        return true;
    }

    /**
     * Callback for processing files found in data/ directory.
     *
     * @param name The name of the file.
     * @param path The relative path of the file.
     * @param size The file size (bytes).
     * @return True if the processing went OK.
     */
    static boolean _File_Init_Callback(String name, String path, long size) {
        FileInfo fileInfo;

        fileInfo = _File_Init_AddFileInRootDir(name, size);
        if (fileInfo == null) return false;

        String ext = strrchr(path, '.');
        if (ext != null) {
            if (".pak".equalsIgnoreCase(ext)) {
                if (!_File_Init_ProcessPak(path, size, fileInfo)) {
                    Warning("Failed to process PAK file %s\n", path);
                    return false;
                }
            }
        }

        return true;
    }

    static boolean File_MakeDirectory(String dir) {
        DWORD attributes;
        boolean success = true;

        attributes = GetFileAttributes(dir);
        if (attributes != INVALID_FILE_ATTRIBUTES) {
            return ((attributes & FILE_ATTRIBUTE_DIRECTORY) != 0);
        }

        /* create intermediate folders if they do not exist */
        success = (SHCreateDirectoryEx(null, dir, null) == ERROR_SUCCESS);

        return success;
    }

    /**
     * Initialize the personal and global data directories, and the file tables.
     *
     * @return True if and only if everything was ok.
     */
    public static boolean File_Init() {
        char buf[1024];
        char *homedir = null;

        if (IniFile_GetString("savedir", null, buf, sizeof(buf)) != null) {
            /* savedir is defined in opendune.ini */
            strncpy(g_personal_data_dir, buf, sizeof(g_personal_data_dir));
        } else {
            /* %APPDATA%/OpenDUNE (win32) */
            if (SHGetFolderPath( null, CSIDL_APPDATA/*CSIDL_COMMON_APPDATA*/, null, 0, buf ) != S_OK) {
                Warning("Cannot find AppData directory.\n");
                snprintf(g_personal_data_dir, sizeof(g_personal_data_dir), ".");
            } else {
                PathAppend(buf, TEXT("OpenDUNE"));
                strncpy(g_personal_data_dir, buf, sizeof(g_personal_data_dir));
            }
        }

        if (!File_MakeDirectory(g_personal_data_dir)) {
            Error("Cannot open personal data directory %s. Do you have sufficient permissions?\n", g_personal_data_dir);
            return false;
        }

        if (IniFile_GetString("datadir", null, buf, sizeof(buf)) != null) {
            /* datadir is defined in opendune.ini */
            strncpy(g_dune_data_dir, buf, sizeof(g_dune_data_dir));
        } else if (g_dune_data_dir[0] == '\0') {
        }
        File_MakeCompleteFilename(buf, sizeof(buf), SEARCHDIR_GLOBAL_DATA_DIR, "", NO_CONVERT);

        if (!ReadDir_ProcessAllFiles(buf, _File_Init_Callback)) {
            Error("Cannot initialise files. Does %s directory exist ?\n", buf);
            return false;
        }

        return true;
    }

    /**
     * Free all ressources loaded in memory.
     */
    public static void File_Uninit() {
        if (s_currentPakFp != null) fclose(s_currentPakFp);
        s_currentPakFp = null;
        s_currentPakInfo = null;
        while (s_files_in_root != null) {
            FileInfoLinkedElem e = s_files_in_root;
            s_files_in_root = e.next;
            free(e);
        }

        while (s_files_in_pak != null) {
            PakFileInfoLinkedElem e = s_files_in_pak;
            s_files_in_pak = e.next;
            free(e);
        }
    }

    /**
     * Check if a file exists either in a PAK or on the disk.
     *
     * @param dir directory for this file
     * @param filename The filename to check for.
     * @param fileSize Filled with the file size if the file exists
     * @return True if and only if the file can be found.
     */
    static boolean File_Exists_Ex(int dir, String filename, long *fileSize) {
        boolean exists = false;

        if (dir != SEARCHDIR_PERSONAL_DATA_DIR) {
            FileInfo fileInfo;
            fileInfo = FileInfo_Find_ByName(filename, null);
            if (fileInfo != null) {
                exists = true;
                if (fileSize != null) *fileSize = fileInfo.fileSize;
            }
        } else {
            int index = _File_Open(dir, filename, FILE_MODE_READ);
            if (index != FILE_INVALID) {
                exists = true;
                if (fileSize != null) *fileSize = File_GetSize(index);
                File_Close(index);
            }
        }

        return exists;
    }

    /**
     * Open a file for reading/writing/appending.
     *
     * @param filename The name of the file to open.
     * @param mode The mode to open the file in. Bit 1 means reading, bit 2 means writing.
     * @return An index value referring to the opened file, or FILE_INVALID.
     */
    public static int File_Open_Ex(int dir, String filename, int mode) {
        int res = _File_Open(dir, filename, mode);

        if (res == FILE_INVALID) {
            if(dir == SEARCHDIR_PERSONAL_DATA_DIR) {
                Warning("Unable to open file '%s'.\n", filename);
            } else {
                Error("Unable to open file '%s'.\n", filename);
                System.exit(1);
            }
        }

        return res;
    }

    /**
     * Close an opened file.
     *
     * @param index The index given by File_Open() of the file.
     */
    public static void File_Close(int index) {
        if (index >= FILE_MAX) return;
        if (s_file[index].fp == null) return;

        if (s_file[index].pakInfo != null) {
            s_file[index].fp = null;	/* do not close PAK file */
            return;
        }

        fclose(s_file[index].fp);
        s_file[index].fp = null;
    }

    /**
     * Read bytes from a file into a buffer.
     *
     * @param index The index given by File_Open() of the file.
     * @param buffer The buffer to read into.
     * @param length The amount of bytes to read.
     * @return The amount of bytes truly read, or 0 if there was a failure.
     */
    public static byte[] File_Read(int index, long length) {
        if (index >= FILE_MAX) return null;
        if (s_file[index].fp == null) return null;
        if (s_file[index].position >= s_file[index].size) return null;
        if (length == 0) return null;

        if (length > s_file[index].size - s_file[index].position) length = s_file[index].size - s_file[index].position;

        byte[] buffer = s_file[index].fp.fread(length);
        if (buffer == null) {
            Error("Read error\n");
            File_Close(index);

            length = 0;
        }

        s_file[index].position += length;
        return buffer;
    }

    /**
     * Read a 16bit unsigned from the file (written on disk in Little endian)
     *
     * @param index The index given by File_Open() of the file.
     * @return The integer read.
     */
    public static int File_Read_LE16(int index) {
        byte[] buffer = File_Read(index, 2);
        return READ_LE_int(buffer, 0);
    }

    /**
     * Read a 32bit unsigned from the file (written on disk in Little endian)
     *
     * @param index The index given by File_Open() of the file.
     * @return The integer read.
     */
    public static Long File_Read_LE32(int index) {
        byte[] buffer = File_Read(index, 4);
        return (buffer == null) ? null : (long)READ_LE_long(buffer, 0);
    }

    /**
     * Write bytes from a buffer to a file.
     *
     * @param index The index given by File_Open() of the file.
     * @param buffer The buffer to write from.
     * @param length The amount of bytes to write.
     * @return The amount of bytes truly written, or 0 if there was a failure.
     */
    public static long File_Write(int index, byte[] buffer, long length) {
        if (index >= FILE_MAX) return 0;
        if (s_file[index].fp == null) return 0;

        if (fwrite(buffer, length, 1, s_file[index].fp) != 1) {
            Error("Write error\n");
            File_Close(index);

            length = 0;
        }

        s_file[index].position += length;
        if (s_file[index].position > s_file[index].size) {
            s_file[index].size = s_file[index].position;
        }
        return length;
    }

    /**
     * Write a 16bit unsigned to the file (written on disk in Little endian)
     *
     * @param index The index given by File_Open() of the file.
     * @param value The 16bit unsigned integer
     * @return true if the operation succeeded
     */
    public static boolean File_Write_LE16(int index, int value) {
        byte[] buffer = new byte[2];
        WRITE_LE_int(buffer, value);
        return (File_Write(index, buffer, 2) == 2);
    }

    /**
     * Seek inside a file.
     *
     * @param index The index given by File_Open() of the file.
     * @param position Position to fix to.
     * @param mode Mode of seeking. 0 = SEEK_SET, 1 = SEEK_CUR, 2 = SEEK_END.
     * @return The new position inside the file, relative from the start.
     */
    public static long File_Seek(int index, long position, int mode) {
        if (index >= FILE_MAX) return 0;
        if (s_file[index].fp == null) return 0;
        if (mode > 2) { File_Close(index); return 0; }

        switch (mode) {
            case 0:
                fseek(s_file[index].fp, s_file[index].start + position, SEEK_SET);
                s_file[index].position = position;
                break;
            case 1:
                fseek(s_file[index].fp, position, SEEK_CUR);
                s_file[index].position += position;
                break;
            case 2:
                fseek(s_file[index].fp, s_file[index].start + s_file[index].size - position, SEEK_SET);
                s_file[index].position = s_file[index].size - position;
                break;
        }

        return s_file[index].position;
    }

    /**
     * Get the size of a file.
     *
     * @param index The index given by File_Open() of the file.
     * @return The size of the file.
     */
    public static int File_GetSize(int index) {
        if (index >= FILE_MAX) return 0;
        if (s_file[index].fp == null) return 0;

        return s_file[index].size;
    }

    /**
     * Delete a file from the disk.
     *
     * @param filename The filename to remove.
     */
    public static void File_Delete_Personal(String filename) {
        char[] filenameComplete = new char[1024];

        File_MakeCompleteFilename(filenameComplete, sizeof(filenameComplete), SEARCHDIR_PERSONAL_DATA_DIR, filename, CONVERT_TO_LOWERCASE);

        if (unlink(filenameComplete) < 0) {
            /* try with the upper case file name */
            File_MakeCompleteFilename(filenameComplete, sizeof(filenameComplete), SEARCHDIR_PERSONAL_DATA_DIR, filename, CONVERT_TO_UPPERCASE);
            unlink(filenameComplete);
        }
    }

    /**
     * Create a file on the disk.
     *
     * @param filename The filename to create.
     */
    public static void File_Create_Personal(String filename) {
        int index = _File_Open(SEARCHDIR_PERSONAL_DATA_DIR, filename, FILE_MODE_WRITE);
        if (index != FILE_INVALID) File_Close(index);
    }

    /**
     * Reads length bytes from filename into buffer.
     *
     * @param filename Then name of the file to read.
     * @param buffer The buffer to read into.
     * @param length The amount of bytes to read.
     * @return The amount of bytes truly read, or 0 if there was a failure.
     */
    static byte[] File_ReadBlockFile_Ex(int dir, String filename, long length) {
        int index = File_Open_Ex(dir, filename, FILE_MODE_READ);
        if (index == FILE_INVALID) return null;
        byte[] buffer = File_Read(index, length);
        File_Close(index);
        return buffer;
    }

    /**
     * Reads the whole file in the memory.
     *
     * @param filename The name of the file to open.
     * @return The pointer to allocated memory where the file has been read.
     */
    public static byte[] File_ReadWholeFile(String filename) {
        int index = File_Open(filename, FILE_MODE_READ);
        if (index == FILE_INVALID) return null;
        int length = File_GetSize(index);

        byte[] buffer = new byte[length + 1];
        if (buffer == null) {
            Error("Failed to allocate %lu bytes of memory.\n", length + 1);
            return null;
        }

        if (File_Read(index, buffer, length) != length) {
            free(buffer);
            return null;
        }

        /* In case of text-files it can be very important to have a \0 at the end */
        ((char *)buffer)[length] = '\0';

        File_Close(index);

        return buffer;
    }

    /**
     * Reads the whole file in the memory. The file should contain little endian
     * 16bits unsigned integers. It is converted to host byte ordering if needed.
     *
     * @param filename The name of the file to open.
     * @param mallocFlags The type of memory to allocate.
     * @return The pointer to allocated memory where the file has been read.
     */
    public static byte[] File_ReadWholeFileLE16(String filename) {
        int index = File_Open(filename, FILE_MODE_READ);
        int count = File_GetSize(index) / 2;

        byte[] buffer = new byte[count * 2];
        if (File_Read(index, buffer, count * sizeof(int)) != count * sizeof(int)) {
            free(buffer);
            return null;
        }

        File_Close(index);

        return buffer;
    }

    /**
     * Reads the whole file into buffer.
     *
     * @param filename The name of the file to open.
     * @param buf The buffer to read into.
     * @return The length of the file.
     */
    static byte[] File_ReadFile(String filename) {
        int index = File_Open(filename, FILE_MODE_READ);
        int length = File_GetSize(index);
        byte[] buf = File_Read(index, length);
        File_Close(index);

        return buf;
    }

    /**
     * Open a chunk file (starting with FORM) for reading.
     *
     * @param filename The name of the file to open.
     * @return An index value refering to the opened file, or FILE_INVALID.
     */
    public static int ChunkFile_Open_Ex(int dir, String filename) {
        int index = File_Open_Ex(dir, filename, FILE_MODE_READ);

        if (index == FILE_INVALID) return index;

        byte[] header = File_Read(index, 4);

        if (header != HTOBE32(CC_FORM)) {
            File_Close(index);
            return FILE_INVALID;
        }

        File_Seek(index, 4, 1);

        return index;
    }

    /**
     * Close an opened chunk file.
     *
     * @param index The index given by ChunkFile_Open() of the file.
     */
    public static void ChunkFile_Close(int index) {
        if (index == FILE_INVALID) return;

        File_Close(index);
    }

    /**
     * Seek to the given chunk inside a chunk file.
     *
     * @param index The index given by ChunkFile_Open() of the file.
     * @param chunk The chunk to seek to.
     * @return The length of the chunk (0 if not found).
     */
    public static long ChunkFile_Seek(int index, long chunk) {
        long value = 0;
        long length = 0;
        boolean first = true;

        while (true) {
            if (File_Read(index, value, 4) != 4 && !first) return 0;

            if (value == 0 && File_Read(index, value, 4) != 4 && !first) return 0;

            if (File_Read(index, &length, 4) != 4 && !first) return 0;

            length = HTOBE32(length);

            if (value == chunk) {
                File_Seek(index, -8, 1);
                return length;
            }

            if (first) {
                File_Seek(index, 12, 0);
                first = false;
                continue;
            }

            length += 1;
            length &= 0xFFFFFFFE;
            File_Seek(index, length, 1);
        }
    }

    /**
     * Read bytes from a chunk file into a buffer.
     *
     * @param index The index given by ChunkFile_Open() of the file.
     * @param chunk The chunk to read from.
     * @param buffer The buffer to read into.
     * @param length The amount of bytes to read.
     * @return The amount of bytes truly read, or 0 if there was a failure.
     */
    public static long ChunkFile_Read(int index, long chunk, byte[] buffer, long buflen) {
        long value = 0;
        long length = 0;
        boolean first = true;

        while (true) {
            if (File_Read(index, &value, 4) != 4 && !first) return 0;

            if (value == 0 && File_Read(index, &value, 4) != 4 && !first) return 0;

            if (File_Read(index, &length, 4) != 4 && !first) return 0;

            length = HTOBE32(length);

            if (value == chunk) {
                buflen = min(buflen, length);

                File_Read(index, buffer, buflen);

                length += 1;
                length &= 0xFFFFFFFE;

                if (buflen < length) File_Seek(index, length - buflen, 1);

                return buflen;
            }

            if (first) {
                File_Seek(index, 12, 0);
                first = false;
                continue;
            }

            length += 1;
            length &= 0xFFFFFFFE;
            File_Seek(index, length, 1);
        }
    }

    public static boolean File_Exists(String FILENAME) {
        return File_Exists_Ex(SEARCHDIR_GLOBAL_DATA_DIR, FILENAME, null);
    }

    public static int File_Exists_GetSize(String FILENAME, long FILESIZE) {
        return File_Exists_Ex(SEARCHDIR_GLOBAL_DATA_DIR, FILENAME, FILESIZE);
    }

    public static boolean File_Exists_Personal(String FILENAME) {
        return File_Exists_Ex(SEARCHDIR_PERSONAL_DATA_DIR, FILENAME, null);
    }

    public static int File_Open(String FILENAME, int MODE) {
        return File_Open_Ex(SEARCHDIR_GLOBAL_DATA_DIR, FILENAME, MODE);
    }

    public static int File_Open_Personal(String FILENAME, int MODE) {
        return File_Open_Ex(SEARCHDIR_PERSONAL_DATA_DIR, FILENAME, MODE);
    }

    public static int File_ReadBlockFile(String FILENAME, byte[] BUFFER, long LENGTH) {
        return File_ReadBlockFile_Ex(SEARCHDIR_GLOBAL_DATA_DIR, FILENAME, BUFFER, LENGTH);
    }

    public static int File_ReadBlockFile_Personal(String FILENAME, byte[] BUFFER, long LENGTH) {
        return File_ReadBlockFile_Ex(SEARCHDIR_PERSONAL_DATA_DIR, FILENAME, BUFFER, LENGTH);
    }

    public static int ChunkFile_Open(String FILENAME) {
        return ChunkFile_Open_Ex(SEARCHDIR_GLOBAL_DATA_DIR, FILENAME);
    }

    public static int ChunkFile_Open_Personal(String FILENAME) {
        return ChunkFile_Open_Ex(SEARCHDIR_PERSONAL_DATA_DIR, FILENAME);
    }
}
