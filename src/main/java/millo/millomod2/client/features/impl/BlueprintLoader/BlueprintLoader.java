package millo.millomod2.client.features.impl.BlueprintLoader;

import millo.millomod2.client.features.Feature;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.NbtUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class BlueprintLoader extends Feature {

    private static final int MARK = 0x0AE5BB36;
    private final static String FOLDER_NAME = "blueprints";
    private boolean loading = false;

    @Override
    public String getId() {
        return "blueprint_loader";
    }

    private final ArrayList<String> foundBlueprints = new ArrayList<>();

    public void searchForBlueprints() {
        foundBlueprints.clear();
        File folder = FileUtil.getOrCreateFolder(FileUtil.getModFolder(), FOLDER_NAME);
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".bp")) {
                foundBlueprints.add(file.getName());
            }
        }
    }

    public ArrayList<String> getFoundBlueprints() {
        return foundBlueprints;
    }

    public void readBlueprint(String blueprint) {
        if (loading) {
            MilloLog.errorInGame("Already loading a blueprint!");
            return;
        }
        loading = true;
        try {
            Schematic schem = read(blueprint);
            new DFSchematicExporter(schem).export(blueprint);
        } catch (IOException e) {
            MilloLog.errorInGame("Failed to read blueprint: " + e.getMessage());
        } finally {
            loading = false;
        }

    }

    public void giveTemplate() {
        PlayerUtil.giveItem(BlueprintTemplates.TEMPLATE);
    }


    public Schematic read(String name) throws IOException {
        File folder = FileUtil.getOrCreateFolder(FileUtil.getModFolder(), FOLDER_NAME);
        File file = new File(folder, name);
        return read(file);
    }

    public Schematic read(File file) throws IOException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            if (in.readInt() != MARK) {
                throw new IllegalArgumentException("Invalid blueprint file: " + file.getName());
            }

            int headerTagSize = in.readInt();
            in.readNBytes(headerTagSize);
            int thumbnailLength = in.readInt();
            in.readNBytes(thumbnailLength);
            int blockDataLength = in.readInt();
            byte[] blockData = in.readNBytes(blockDataLength);
            DataInputStream blockDataStream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(blockData)));

            NbtCompound blockDataTag = NbtUtil.read(blockDataStream);

            blockDataStream.close();
            in.close();
            NbtList blockRegions = blockDataTag.getList("BlockRegion").orElseThrow();
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int maxZ = Integer.MIN_VALUE;
            for (NbtElement tag : blockRegions) {
                NbtCompound region = (NbtCompound) tag;
                minX = Math.min(minX, region.getInt("X", 0));
                minY = Math.min(minY, region.getInt("Y", 0));
                minZ = Math.min(minZ, region.getInt("Z", 0));
                maxX = Math.max(maxX, region.getInt("X", 0));
                maxY = Math.max(maxY, region.getInt("Y", 0));
                maxZ = Math.max(maxZ, region.getInt("Z", 0));
            }
            int[] size = {(maxX - minX + 1) * 16, (maxY - minY + 1) * 16, (maxZ - minZ + 1) * 16};
            int dataVersion =  blockDataTag.getInt("DataVersion", -1);
            Schematic.Builder builder = new Schematic.Builder(file, dataVersion, size);
            for (NbtElement tag : blockRegions) {
                NbtCompound region = (NbtCompound) tag;
                NbtCompound blockStatesTag = region.getCompound("BlockStates").orElseThrow();
                NbtList paletteTag = blockStatesTag.getList("palette").orElseThrow();
                String[] palette = new String[paletteTag.size()];
                for (int i = 0; i < palette.length; i++)
                    palette[i] = NbtUtil.convertToBlockString((NbtCompound) paletteTag.get(i));
                long[] data = palette.length == 1 ? new long[256] : blockStatesTag.getLongArray("data").orElseThrow();
                int regionX = region.getInt("X", 0) - minX;
                int regionY = region.getInt("Y", 0) - minY;
                int regionZ = region.getInt("Z", 0) - minZ;
                int[] blockStateData = new int[4096];
                int bitsPerValue = Math.max(4, Integer.SIZE - Integer.numberOfLeadingZeros(palette.length - 1));
                int valuesPerLong = Long.SIZE / bitsPerValue;
                int index = 0;
                int mask = (1 << bitsPerValue) - 1;
                for (long num : data) {
                    for (int i = 0; i < valuesPerLong && index < 4096; i++) {
                        blockStateData[index++] = (int) (num & mask);
                        num >>>= bitsPerValue;
                    }
                }

                int i = 0;
                for (int y = 0; y < 16; y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            builder.setBlockAt(regionX * 16 + x, regionY * 16 + y, regionZ * 16 + z, palette[blockStateData[i++]]);
                        }
                    }
                }
            }
            return builder.trim().build();
        }
    }
}
