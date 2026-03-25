package millo.millomod2.client.util;

import com.google.gson.JsonObject;
import millo.millomod2.client.MilloMod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class FileUtil {

    public static Path getModFolder() {
        Path path = MilloMod.MC.runDirectory.toPath().resolve(MilloMod.MOD_ID);
        path.toFile().mkdirs();
        return path;
    }

    public static String readJson(String name) {
        return readJson(getModFolder().resolve(name));
    }

    public static String readJson(Path resolve) {
        File file = resolve.toFile();
        if (!file.exists()) return null;

        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeJson(String name, JsonObject json) {
        writeJson(name, json.toString());
    }

    public static void writeJson(String name, String data) {
        try {
            File file = getModFolder().resolve(name).toFile();
            Files.deleteIfExists(file.toPath());
            Files.createFile(file.toPath());
            Files.write(file.toPath(), data.getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            MilloLog.errorInGame("Couldn't save json: " + e);
        }
    }

    public static void write(Path path, String data) {
        try {
            File file = path.toFile();
            Files.deleteIfExists(file.toPath());
            Files.createFile(file.toPath());
            Files.write(file.toPath(), data.getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            MilloLog.errorInGame("Couldn't save file: " + e);
        }
    }

    public static String read(Path filePath) {
        File file = filePath.toFile();
        if (!file.exists()) return null;

        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            return null;
        }
    }

    public static Iterable<Path> listFiles(Path plotFolder) {
        File folder = plotFolder.toFile();
        if (!folder.exists()) return new ArrayList<>();

        ArrayList<Path> paths = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) return paths;

        for (File file : files) {
            paths.add(file.toPath());
        }
        return paths;
    }

    public static void delete(File file) {
        if (!file.exists()) return;
        if (!file.delete()) {
            MilloLog.errorInGame("Failed to delete file: " + file);
        }
    }

    public static void copy(File source, File dest) {
        if (source == null || dest == null) {
            throw new IllegalArgumentException("Source and destination files cannot be null");
        }

        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            try (FileInputStream fis = new FileInputStream(source);
                 FileChannel fcs = fis.getChannel();
                 FileOutputStream fos = new FileOutputStream(dest);
                 FileChannel fcd = fos.getChannel()) {
                fcd.transferFrom(fcs, 0, fcs.size());
            } catch (IOException ex) {
                MilloLog.errorInGame("Failed to copy file: " + ex);
            }
        }
    }
}
