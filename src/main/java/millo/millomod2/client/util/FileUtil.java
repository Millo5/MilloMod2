package millo.millomod2.client.util;

import com.google.gson.JsonObject;
import millo.millomod2.client.MilloMod;

import java.io.File;
import java.io.IOException;
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
}
