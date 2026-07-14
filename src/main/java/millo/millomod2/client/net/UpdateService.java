package millo.millomod2.client.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.menus.UpdateMenu;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.MilloLog;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UpdateService {

    private static final String IGNORE_FILE = ".ignoreupdate";
    private static final String GITHUB_API = "https://api.github.com/repos/Millo5/MilloMod2/releases/latest";
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 30000;
    private static final int MAX_RELEASE_METADATA_SIZE = 2 * 1024 * 1024;
    private static final long MAX_UPDATE_SIZE = 100L * 1024L * 1024L;
    private static final AtomicBoolean updateQueued = new AtomicBoolean();
    private static final ExecutorService UPDATE_EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Millo-Update-Thread");
        thread.setDaemon(true);
        return thread;
    });

    private static volatile UpdateInfoResult cachedResult;

    public static CompletableFuture<UpdateInfoResult> checkForUpdates() {
        if (updateQueued.get()) {
            return CompletableFuture.completedFuture(UpdateInfoResult.failed());
        }

        if (getIgnoreFile().exists()) {
            MilloLog.LOGGER.info("Update check skipped: User has chosen to ignore updates");
            return CompletableFuture.completedFuture(UpdateInfoResult.failed());
        }

        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection connection = null;
            try {
                connection = openConnection(new URI(GITHUB_API).toURL());
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setRequestProperty("User-Agent", "MilloMod2/" + MilloMod.MOD_VERSION);

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException("GitHub returned HTTP " + connection.getResponseCode());
                }

                JsonObject release;
                try (InputStream input = connection.getInputStream()) {
                    byte[] response = input.readNBytes(MAX_RELEASE_METADATA_SIZE + 1);
                    if (response.length > MAX_RELEASE_METADATA_SIZE) {
                        throw new IOException("GitHub release metadata is too large");
                    }
                    release = JsonParser.parseString(new String(response, StandardCharsets.UTF_8)).getAsJsonObject();
                }

                String latestVersion = release.get("tag_name").getAsString();
                String downloadUrl = findDownloadUrl(release.getAsJsonArray("assets"));
                validateDownloadUrl(URI.create(downloadUrl).toURL(), true);

                String currentVersion = "v" + MilloMod.MOD_VERSION;
                MilloLog.LOGGER.info("Found MilloMod release {} (current version: {})", latestVersion, currentVersion);
                boolean outdated = !latestVersion.equals(currentVersion);
                cachedResult = new UpdateInfoResult(outdated, latestVersion, downloadUrl);
                return cachedResult;
            } catch (Exception e) {
                MilloLog.error("Failed to check for updates: " + e.getMessage());
                cachedResult = UpdateInfoResult.failed();
                return cachedResult;
            } finally {
                if (connection != null) connection.disconnect();
            }
        }, UPDATE_EXECUTOR);
    }

    public static void openUpdateScreen() {
        new UpdateMenu(MilloMod.MC.currentScreen).open();
    }

    public static CompletableFuture<UpdateResult> update() {
        if (!updateQueued.compareAndSet(false, true)) {
            return CompletableFuture.completedFuture(UpdateResult.UPDATE_PENDING);
        }

        try {
            return CompletableFuture.supplyAsync(UpdateService::tryFetchNewUpdate, UPDATE_EXECUTOR)
                    .whenComplete((result, throwable) -> {
                        if (throwable != null || result != UpdateResult.SUCCESS) updateQueued.set(false);
                    });
        } catch (Exception e) {
            updateQueued.set(false);
            return CompletableFuture.failedFuture(e);
        }
    }

    private static UpdateResult tryFetchNewUpdate() {
        Path partialFile = null;
        try {
            UpdateInfoResult updateInfo = cachedResult;
            if (updateInfo == null || updateInfo.downloadUrl() == null) {
                throw new IOException("Update status unknown. Please check for updates first.");
            }

            File oldJar = MilloMod.getModJar();
            if (!oldJar.isFile()) throw new IOException("Original mod file not found.");

            Path updateFile = getUpdateFile().toPath();
            partialFile = updateFile.resolveSibling(updateFile.getFileName() + ".part");
            URL url = URI.create(updateInfo.downloadUrl()).toURL();
            validateDownloadUrl(url, true);
            Files.deleteIfExists(partialFile);
            downloadFile(url, partialFile);
            validateJar(partialFile);
            moveReplacing(partialFile, updateFile);

            MilloLog.LOGGER.info("Update download complete");
            scheduleInstallation(updateFile, oldJar.toPath());
            return UpdateResult.SUCCESS;
        } catch (Exception e) {
            if (partialFile != null) {
                try {
                    Files.deleteIfExists(partialFile);
                } catch (IOException ignored) {}
            }
            MilloLog.error("Could not download update: " + e.getMessage());
            return UpdateResult.ERROR;
        }
    }

    private static void downloadFile(URL url, Path file) throws IOException {
        HttpURLConnection connection = openDownloadConnection(url);
        try {
            int response = connection.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                throw new IOException("Download returned HTTP " + response);
            }
            validateDownloadUrl(connection.getURL(), false);

            long fileSize = connection.getContentLengthLong();
            if (fileSize > MAX_UPDATE_SIZE) throw new IOException("Update exceeds maximum size");

            try (InputStream input = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream output = new FileOutputStream(file.toFile())) {
                byte[] buffer = new byte[8192];
                long totalRead = 0;
                int lastLoggedProgress = 0;
                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) {
                    totalRead += bytesRead;
                    if (totalRead > MAX_UPDATE_SIZE) throw new IOException("Update exceeds maximum size");
                    output.write(buffer, 0, bytesRead);

                    if (fileSize > 0) {
                        int progress = (int) (totalRead * 100 / fileSize);
                        if (progress - lastLoggedProgress >= 10) {
                            MilloLog.LOGGER.info("Update download progress: {}%", progress);
                            lastLoggedProgress = progress;
                        }
                    }
                }
            }
        } finally {
            connection.disconnect();
        }
    }

    private static void validateJar(Path file) throws IOException {
        try (ZipFile jar = new ZipFile(file.toFile())) {
            ZipEntry metadataEntry = jar.getEntry("fabric.mod.json");
            if (metadataEntry == null || metadataEntry.getSize() > 1024 * 1024) {
                throw new IOException("Downloaded file is not a Fabric mod");
            }

            try (InputStream input = jar.getInputStream(metadataEntry)) {
                byte[] metadataBytes = input.readNBytes(1024 * 1024 + 1);
                if (metadataBytes.length > 1024 * 1024) throw new IOException("Mod metadata is too large");
                JsonObject metadata = JsonParser.parseString(new String(metadataBytes, StandardCharsets.UTF_8)).getAsJsonObject();
                if (!metadata.has("id") || !MilloMod.MOD_ID.equals(metadata.get("id").getAsString())) {
                    throw new IOException("Downloaded file is not MilloMod2");
                }
                validateDependency(metadata, "minecraft");
                validateDependency(metadata, "fabricloader");
            }
        } catch (RuntimeException e) {
            throw new IOException("Downloaded mod metadata is invalid", e);
        }
    }

    private static void scheduleInstallation(Path updateFile, Path oldJar) {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    File update = updateFile.toFile();
                    File old = oldJar.toFile();
                    if (!old.exists() || old.isDirectory()) {
                        MilloLog.error("Cannot apply update: Original mod file not found.");
                        return;
                    }

                    MilloLog.LOGGER.info("Applying update...");
                    FileUtil.copy(update, old);
                    update.delete();
                    MilloLog.LOGGER.info("Update applied successfully!");
                },
                "Millo-Update-Apply"
        ));
        MilloLog.LOGGER.info("Update scheduled for installation when Minecraft exits");
    }

    private static HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setInstanceFollowRedirects(true);
        return connection;
    }

    private static HttpURLConnection openDownloadConnection(URL initialUrl) throws IOException {
        URL url = initialUrl;
        for (int redirects = 0; redirects <= 5; redirects++) {
            HttpURLConnection connection = openConnection(url);
            connection.setInstanceFollowRedirects(false);
            int response = connection.getResponseCode();
            if (response < 300 || response >= 400) return connection;

            String location = connection.getHeaderField("Location");
            connection.disconnect();
            if (location == null) throw new IOException("Update redirect has no destination");
            url = URI.create(url.toString()).resolve(location).toURL();
            validateDownloadUrl(url, false);
        }
        throw new IOException("Too many update redirects");
    }

    private static String findDownloadUrl(JsonArray assets) throws IOException {
        if (assets == null) throw new IOException("Release has no assets");
        for (var assetElement : assets) {
            JsonObject asset = assetElement.getAsJsonObject();
            if (!asset.has("name") || !asset.has("browser_download_url")) continue;
            String name = asset.get("name").getAsString().toLowerCase(Locale.ROOT);
            if (!name.endsWith(".jar") || name.endsWith("-sources.jar")
                    || name.endsWith("-dev.jar") || name.endsWith("-javadoc.jar")) continue;
            return asset.get("browser_download_url").getAsString();
        }
        throw new IOException("Release has no runnable JAR asset");
    }

    private static void validateDependency(JsonObject metadata, String modId) throws IOException {
        if (!metadata.has("depends") || !metadata.get("depends").isJsonObject()) {
            throw new IOException("Downloaded mod has no dependency information");
        }

        JsonObject dependencies = metadata.getAsJsonObject("depends");
        if (!dependencies.has(modId)) throw new IOException("Downloaded mod has no " + modId + " dependency");

        Version installedVersion = getInstalledVersion(modId);
        JsonElement dependency = dependencies.get(modId);
        ArrayList<String> predicates = new ArrayList<>();
        if (dependency.isJsonPrimitive()) {
            predicates.add(dependency.getAsString());
        } else if (dependency.isJsonArray()) {
            dependency.getAsJsonArray().forEach(element -> predicates.add(element.getAsString()));
        } else {
            throw new IOException("Downloaded mod has an invalid " + modId + " dependency");
        }

        try {
            for (String predicate : predicates) {
                if (VersionPredicate.parse(predicate).test(installedVersion)) return;
            }
        } catch (Exception e) {
            throw new IOException("Downloaded mod has an invalid " + modId + " version requirement", e);
        }
        throw new IOException("Downloaded mod is not compatible with installed " + modId + " " + installedVersion.getFriendlyString());
    }

    private static Version getInstalledVersion(String modId) throws IOException {
        return FabricLoader.getInstance().getModContainer(modId)
                .orElseThrow(() -> new IOException("Installed " + modId + " version could not be determined"))
                .getMetadata().getVersion();
    }

    private static void validateDownloadUrl(URL url, boolean repositoryPathRequired) throws IOException {
        String host = url.getHost().toLowerCase();
        boolean trustedHost = host.equals("github.com") || host.endsWith(".githubusercontent.com");
        if (!url.getProtocol().equals("https") || !trustedHost || url.getUserInfo() != null) {
            throw new IOException("Untrusted update URL");
        }
        if (repositoryPathRequired && (!host.equals("github.com")
                || !url.getPath().startsWith("/Millo5/MilloMod2/releases/download/"))) {
            throw new IOException("Update URL does not belong to MilloMod2");
        }
    }

    private static void moveReplacing(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Path getUpdateFolder() {
        Path updatesDir = FileUtil.getModFolder().resolve("update");
        try {
            Files.createDirectories(updatesDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create update folder", e);
        }
        return updatesDir;
    }

    private static File getUpdateFile() {
        return getUpdateFolder().resolve("MilloMod2-update.jar").toFile();
    }

    public static void ignoreUpdates() {
        try {
            Files.createDirectories(getIgnoreFile().toPath().getParent());
            if (!getIgnoreFile().exists()) Files.createFile(getIgnoreFile().toPath());
        } catch (IOException e) {
            MilloLog.error("Couldn't create ignore file: " + e.getMessage());
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
