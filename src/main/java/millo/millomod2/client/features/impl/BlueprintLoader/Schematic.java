package millo.millomod2.client.features.impl.BlueprintLoader;


import java.io.File;
import java.util.*;

public class Schematic {
    private final String[][][] blocks;
    private final List<String> palette;
    private final int dataVersion;
    private final File sourceFile;

    public Schematic(String[][][] blocks, List<String> palette, int dataVersion, File sourceFile) {
        this.blocks = blocks;
        this.palette = palette;
        this.dataVersion = dataVersion;
        this.sourceFile = sourceFile;
    }


    public int[] getSize() {
        return new int[]{blocks.length, blocks[0].length, blocks[0][0].length};
    }

    public String getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public int getPaletteBlock(int x, int y, int z) {
        return palette.indexOf(blocks[x][y][z]);
    }

    public List<String> getPalette() {
        return palette;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public static boolean isEmpty(String block) {
        return block == null || block.equals("minecraft:air") || block.equals("minecraft:void_air");
    }

    public boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < blocks.length &&
                y >= 0 && y < blocks[0].length &&
                z >= 0 && z < blocks[0][0].length;
    }

    public static class Builder {
        private String[][][] blocks;
        private final SequencedSet<String> palette;
        private final File sourceFile;
        private final int dataVersion;

        public Builder(File sourceFile, int dataVersion, int xSize, int ySize, int zSize) {
            this.sourceFile = sourceFile;
            this.dataVersion = dataVersion;
            blocks = new String[xSize][ySize][zSize];
            palette = new LinkedHashSet<>();
        }

        public Builder(File file, int dataVersion, int[] size) {
            this(file, dataVersion, size[0], size[1], size[2]);
        }

        public void setBlockAt(int x, int y, int z, String id) {
            blocks[x][y][z] = id;
            palette.add(id);
        }

        public Builder trim() {
            int minX = 0, minY = 0, minZ = 0;
            int maxX = blocks.length - 1, maxY = blocks[0].length - 1, maxZ = blocks[0][0].length - 1;

            // X
            loop:
            for (int x = 0; x < blocks.length; x++) {
                for (int y = 0; y < blocks[0].length; y++) {
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            minX = x;
                            break loop;
                        }
                    }
                }
            }
            loop:
            for (int x = blocks.length - 1; x >= 0; x--) {
                for (int y = 0; y < blocks[0].length; y++) {
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            maxX = x;
                            break loop;
                        }
                    }
                }
            }

            // Y
            loop:
            for (int y = 0; y < blocks[0].length; y++) {
                for (int x = 0; x < blocks.length; x++) {
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            minY = y;
                            break loop;
                        }
                    }
                }
            }
            loop:
            for (int y = blocks[0].length - 1; y >= 0; y--) {
                for (int x = 0; x < blocks.length; x++) {
                    for (int z = 0; z < blocks[0][0].length; z++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            maxY = y;
                            break loop;
                        }
                    }
                }
            }

            // Z
            loop:
            for (int z = 0; z < blocks[0][0].length; z++) {
                for (int x = 0; x < blocks.length; x++) {
                    for (int y = 0; y < blocks[0].length; y++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            minZ = z;
                            break loop;
                        }
                    }
                }
            }
            loop:
            for (int z = blocks[0][0].length - 1; z >= 0; z--) {
                for (int x = 0; x < blocks.length; x++) {
                    for (int y = 0; y < blocks[0].length; y++) {
                        if (!isEmpty(blocks[x][y][z])) {
                            maxZ = z;
                            break loop;
                        }
                    }
                }
            }

            String[][][] newBlocks = new String[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        newBlocks[x - minX][y - minY][z - minZ] = blocks[x][y][z];
                    }
                }
            }
            blocks = newBlocks;

            return this;
        }

        public Schematic build() {
            return new Schematic(blocks, palette.stream().toList(), dataVersion, sourceFile);
        }

    }
}
