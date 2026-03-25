package millo.millomod2.client.net;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.menus.UpdateMenu;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.MilloLog;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class UpdateService {

    private static final String IGNORE_FILE = ".ignoreupdate";
    private static final String GITHUB_API = "https://api.github.com/repos/Millo5/MilloMod2/releases/latest";
    private static UpdateInfoResult cachedResult = null;

    public static CompletableFuture<UpdateInfoResult> checkForUpdates() {
        if (updateQueued) {
            return CompletableFuture.completedFuture(UpdateInfoResult.failed());
        }

        if (getIgnoreFile().exists()) {
            MilloLog.logInGame("Update check skipped: User has chosen to ignore updates.");
            return CompletableFuture.completedFuture(UpdateInfoResult.failed());
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URI(GITHUB_API).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() != 200) return UpdateInfoResult.failed();

                String json = new String(conn.getInputStream().readAllBytes());

                String latestVersion = json.split("\"tag_name\":\"")[1].split("\"")[0];
                String downloadUrl = json.split("\"browser_download_url\":\"")[1].split("\"")[0];

                MilloLog.logInGame(latestVersion);
                MilloLog.logInGame(downloadUrl);

                String currentVersion = "v" + MilloMod.MOD_VERSION;

                boolean outdated = !latestVersion.equals(currentVersion);
                cachedResult = new UpdateInfoResult(outdated, latestVersion, downloadUrl);
                return cachedResult;
            } catch (Exception e) {
                MilloLog.error(e.getMessage());
                cachedResult = UpdateInfoResult.failed();
                return cachedResult;
            }
        });
    }

    public static void openUpdateScreen() {
        new UpdateMenu(MilloMod.MC.currentScreen).open();
    }

    private static boolean updateQueued = false;
    public static CompletableFuture<UpdateResult> update() {
        CompletableFuture<UpdateResult> future = new CompletableFuture<>();
        if (updateQueued) {
            future.complete(UpdateResult.UPDATE_PENDING);
            return future;
        }
        updateQueued = true;

        if (cachedResult == null) {
            future.complete(UpdateResult.ERROR);
            MilloLog.errorInGame("Update status unknown. Please check for updates first.");
            return future;
        }

        File localUpdateFile = getUpdateFile();
        if (localUpdateFile.exists()) {
            MilloLog.log("Update file already exists, deleting...");
            FileUtil.delete(localUpdateFile);
        }

        Executors.newSingleThreadExecutor().submit(() -> tryFetchNewUpdate(cachedResult, future));

        return future;
    }

    private static void tryFetchNewUpdate(UpdateInfoResult updateInfo, CompletableFuture<UpdateResult> future) {
        File oldJar = MilloMod.getModJar();
        File newJar = getUpdateFile();


        try {
            URL url = URI.create(updateInfo.downloadUrl).toURL();
            URLConnection conn = url.openConnection();

            downloadFile(conn, newJar);

            MilloLog.logInGame("Download complete");
            future.complete(UpdateResult.SUCCESS);


            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (!oldJar.exists() || oldJar.isDirectory()) {
                        MilloLog.error("Cannot apply update: Original mod file not found.");
                        return;
                    }
                    MilloLog.log("Applying update...");

                    FileUtil.copy(newJar, oldJar);
                    newJar.delete();

                    MilloLog.log("Update applied successfully!");
                } catch (Exception e) {
                    MilloLog.error("Cannot apply update: " + e.getMessage());
                }
            }));

        } catch (IOException e) {
            MilloLog.error("Invalid download URL: " + updateInfo.downloadUrl);
            future.complete(UpdateResult.ERROR);
            return;
        }

    }

    private static void downloadFile(URLConnection conn, File file) {
        try {
            int fileSize = conn.getContentLength();
            if (!file.isFile()) file.createNewFile();

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;
            int totalRead = 0;
            int lastLoggedProgress = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                int progress = (int) ((totalRead / (float) fileSize) * 100);
                if (progress - lastLoggedProgress >= 10) {
                    MilloLog.logInGame("Download progress: " + progress + "%");
                    lastLoggedProgress = progress;
                }
            }

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            file.delete();
            MilloLog.errorInGame("Error downloading update: " + e.getMessage());
        }

    }

    private static Path getUpdateFolder() {
        Path updatesDir = FileUtil.getModFolder().resolve("update");
        if (!updatesDir.toFile().exists()) {
            new File(updatesDir.toString()).mkdirs();
        }
        return updatesDir;
    }

    private static File getUpdateFile() {
        return getUpdateFolder().resolve("MilloMod2-update.jar").toFile();
    }

    public static void ignoreUpdates() {
        try {
            File ignoreFile = getIgnoreFile();
            if (!ignoreFile.exists()) {
                ignoreFile.createNewFile();
            }
        } catch (IOException e) {
            MilloLog.errorInGame("Couldn't create ignore file: " + e.getMessage());
        }
    }

    private static File getIgnoreFile() {
        return FileUtil.getModFolder().resolve(IGNORE_FILE).toFile();
    }

    public record UpdateInfoResult(boolean outdated, String latestVersion, String downloadUrl) {

        public static UpdateInfoResult failed() {
                return new UpdateInfoResult(false, null, null);
            }

    }

    public enum UpdateResult {
        SUCCESS,
        ALREADY_UP_TO_DATE,
        UPDATE_PENDING,
        ERROR
    }


}
