package com.tsoft.game.utils.console.nes;

import com.tsoft.game.utils.base.Ref;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class RomExtractor {

    public static class NesRomInfo {
        public String name;
        public int size;
        public String format;
        public int prgRomSizeLsb;
        public int chrRomSizeLsb;
        public byte[] trainerArea;
        public byte[] prgRom;
        public byte[] chrRom;
        public byte[] misc;
    }

    /**
     * <a href="https://www.nesdev.org/wiki/NES_2.0">NES 2.0</a>
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 1) {
            usage();
            System.exit(1);
        }

        Path path = Paths.get(args[0]);
        byte[] buf = Files.readAllBytes(path);

        NesRomInfo info = new NesRomInfo();
        Ref<Integer> off = new Ref<>(0x0);

        info.name = path.getFileName().toString();
        info.size = buf.length;
        info.format = getNesFormat(buf, off);
        info.prgRomSizeLsb = getPrgRomSizeLsb(buf, off);
        info.chrRomSizeLsb = getChrRomSizeLsb(buf, off);
        info.trainerArea = getTrainerArea(buf, off);
        info.prgRom = getRom(buf, off, info.prgRomSizeLsb * 0x4000);
        info.chrRom = getRom(buf, off, info.chrRomSizeLsb * 0x2000);
        info.misc = getRom(buf, off, info.size - off.value);

        writeToFile(path, info.trainerArea, ".tra");
        writeToFile(path, info.prgRom, ".prg");
        writeToFile(path, info.chrRom, ".chr");
    }

    private static void writeToFile(Path path, byte[] buf, String ext) throws IOException {
        if (buf == null) {
            return;
        }

        String fileName = path.toUri().toString();
        int n = fileName.lastIndexOf('.');
        URI uri = URI.create(fileName.substring(0, n) + ext);
        Files.write(Paths.get(uri), buf);
    }

    private static byte[] getRom(byte[] buf, Ref<Integer> off, int size) {
        if (size == 0) {
            return null;
        }

        int start = off.value;
        off.value += size;
        return Arrays.copyOfRange(buf, start, start + size);
    }

    // The Trainer Area follows the 16-byte Header and precedes the PRG-ROM area
    // if bit 2 of Header byte 6 is set. It is always 512 bytes in size if present
    private static byte[] getTrainerArea(byte[] buf, Ref<Integer> off) {
        boolean present = (buf[6] & 0x4) != 0;

        if (present) {
            off.value += 0x200;
        }

        return present ? Arrays.copyOfRange(buf, 16, 16 + 512) : null;
    }

    private static int getPrgRomSizeLsb(byte[] buf, Ref<Integer> off) {
        return buf[4];
    }

    private static int getChrRomSizeLsb(byte[] buf, Ref<Integer> off) {
        return buf[5];
    }

    private static String getNesFormat(byte[] buf, Ref<Integer> off) {
        boolean iNes = (buf[0] == 'N' && buf[1] == 'E' && buf[2] == 'S' && buf[3] == 0x1A);
        boolean nes20 = iNes && (buf[7] & 0x0C) == 0x08;

        off.value += 0x10;

        return nes20 ? "NES2.0" : "iNES";
    }

    private static void usage() {
        System.out.println(
            "Usage: <nes file>"
        );
    }

    public static void print(String msg, Object ... args) {
        if (args == null || args.length == 0) {
            System.out.print(msg);
            return;
        }

        for (Object arg : args) {
            int n = msg.indexOf("{}");
            if (n == -1) {
                throw new IllegalArgumentException("Invalid args in mas: " + msg);
            }

            msg = msg.substring(0, n) + arg.toString() + msg.substring(n + 2);
        }

        System.out.print(msg);
    }

    public static void println(String msg, Object ... args) {
        print(msg + "\n", args);
    }
}
