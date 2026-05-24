package millo.millomod2.client.features.impl.BlueprintLoader;

import millo.millomod2.client.hypercube.model.ModelUtil;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.math.ByteArray;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DFSchematicExporter {


    private static final String TEMPLATE_TEMPLATE = """
            {
                "blocks": [
                    {
                        "id": "block",
                        "block": "func",
                        "args": {
                            "items": [
                                {
                                    "item": {
                                        "id": "pn_el",
                                        "data": {
                                            "name": "chunkData",
                                            "type": "var",
                                            "plural": false,
                                            "optional": false
                                        }
                                    },
                                    "slot": 0
                                },
                                {
                                    "item": {
                                        "id": "pn_el",
                                        "data": {
                                            "name": "paletteData",
                                            "type": "var",
                                            "plural": false,
                                            "optional": false
                                        }
                                    },
                                    "slot": 1
                                },
                                {
                                    "item": {
                                        "id": "bl_tag",
                                        "data": {
                                            "option": "False",
                                            "tag": "Is Hidden",
                                            "action": "dynamic",
                                            "block": "func"
                                        }
                                    },
                                    "slot": 26
                                }
                            ]
                        },
                        "data": "<name>"
                    },
                    <blocks>
                ]
            }
            """;

    private static final String BLOCK_TEMPLATE = """
            {
                "id": "block",
                "block": "set_var",
                "args": {
                    "items": [
                        {
                            "item": {
                                "id": "var",
                                "data": {
                                    "name": "<var_name>",
                                    "scope": "line"
                                }
                            },
                            "slot": 0
                        },
                        <items>
                    ]
                },
                "action": "CreateList"
            }
            """;

    private static final String STRING_TEMPLATE = """
            {
                "item": {
                    "id": "txt",
                    "data": {
                        "name": "<text>"
                    }
                },
                "slot": <slot>
            }
            """;

    private final int chunkSize = 4;
    private final Schematic schem;
    private ByteArray byteArray;

    private int chunkXBounds;
    private int chunkYBounds;
    private int chunkZBounds;
    private int paletteBounds;
    private int totalChunks;

    public DFSchematicExporter(Schematic schem) {
        this.schem = schem;
    }

    public void export(String name) {
        byteArray = new ByteArray();

        chunkXBounds = Integer.SIZE - Integer.numberOfLeadingZeros((int) Math.ceil(schem.getSize()[0] / (double) chunkSize));
        chunkYBounds = Integer.SIZE - Integer.numberOfLeadingZeros((int) Math.ceil(schem.getSize()[1] / (double) chunkSize));
        chunkZBounds = Integer.SIZE - Integer.numberOfLeadingZeros((int) Math.ceil(schem.getSize()[2] / (double) chunkSize));
        paletteBounds = Integer.SIZE - Integer.numberOfLeadingZeros(schem.getPalette().size());

        ByteArray header = new ByteArray();

        header.add(chunkXBounds, 5);
        header.add(chunkYBounds, 5);
        header.add(chunkZBounds, 5);
        header.add(paletteBounds, 5);

        int[] size = schem.getSize();
        int chunksX = (int) Math.ceil(size[0] / (double) chunkSize);
        int chunksY = (int) Math.ceil(size[1] / (double) chunkSize);
        int chunksZ = (int) Math.ceil(size[2] / (double) chunkSize);

        totalChunks = 0;
        for (int x = 0; x < chunksX; x++) {
            for (int y = 0; y < chunksY; y++) {
                for (int z = 0; z < chunksZ; z++) {
                    exportChunk(x, y, z);
                }
            }
        }
        header.add(totalChunks, 24);

        header.append(byteArray);
        byteArray = header;

        String[] gzip = byteArray.compress();
        ArrayList<String> paletteStrings = getPaletteStrings();

        // Output template
        StringBuilder chunkItems = new StringBuilder();
        for (int j = 0; j < gzip.length; j++) {
            chunkItems.append(Template.string(gzip[j], j + 1));
            if (j < gzip.length - 1) {
                chunkItems.append(",\n");
            }
        }
        String blockString = Template.block("chunkData", chunkItems.toString());

        StringBuilder paletteItems = new StringBuilder();
        for (int j = 0; j < paletteStrings.size(); j++) {
            paletteItems.append(Template.string(paletteStrings.get(j), j + 1));
            if (j < paletteStrings.size() - 1) {
                paletteItems.append(",\n");
            }
        }
        blockString += ",\n" + Template.block("paletteData", paletteItems.toString());

        String template = Template.template(name, blockString);
        Template.send(template);
    }

    private ArrayList<String> getPaletteStrings() {
        ArrayList<String> paletteStrings = new ArrayList<>();

        StringBuilder paletteString = new StringBuilder();
        for (String block : schem.getPalette()) {
            String blockString = block.replace("minecraft:", "")
                    .replaceAll("(,\\w+=(?:none|false|unconnected)|\\w+=(?:none|false|unconnected),?)", "")
                    .replace("[]", "")
                    + ";";
            if (paletteString.length() + blockString.length() > 10000) {
                paletteStrings.add(paletteString.substring(0, paletteString.length() - 1));
                paletteString = new StringBuilder();
            }
            paletteString.append(blockString);
        }
        paletteStrings.add(paletteString.substring(0, paletteString.length() - 1));
        return paletteStrings;
    }

    private void exportChunk(int chunkX, int chunkY, int chunkZ) {

        // Chunk format;
        // X:           12 bits
        // Y:           12 bits
        // Z:           12 bits
        // Block count: 6 bits (0-64)
        // N blocks:
        //  - Block ID: 16 bits (index in palette)
        //  - Block data: 64 bits (bitmask of block positions in chunk)

        HashMap<String, Long> blocks = new HashMap<>();

        int startX = chunkX * chunkSize;
        int startY = chunkY * chunkSize;
        int startZ = chunkZ * chunkSize;
        int endX = startX + chunkSize;
        int endY = startY + chunkSize;
        int endZ = startZ + chunkSize;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (int z = startZ; z < endZ; z++) {
                    if (!schem.inBounds(x, y, z)) continue;

                    String block = schem.getBlock(x, y, z);
                    if (Schematic.isEmpty(block)) continue;

                    int blockIndex = (x - startX) * chunkSize * chunkSize + (y - startY) * chunkSize + (z - startZ);

                    blocks.computeIfAbsent(block, k -> 0L);
                    blocks.put(block, blocks.get(block) | (1L << blockIndex));
                }
            }
        }

        if (blocks.isEmpty()) {
            return;
        }
        totalChunks++;

        // Chunk is not empty, so we create a byte array

        ByteArray chunkByteArray = new ByteArray();

        chunkByteArray.add(chunkX, chunkXBounds);
        chunkByteArray.add(chunkY, chunkYBounds);
        chunkByteArray.add(chunkZ, chunkZBounds);

        int chunkPaletteSize = Math.min(paletteBounds, 6);

        chunkByteArray.add(blocks.size(), chunkPaletteSize);

        for (var entry : blocks.entrySet()) {
            int blockId = schem.getPalette().indexOf(entry.getKey());

            chunkByteArray.add(blockId, paletteBounds);
            chunkByteArray.add(entry.getValue(), 64);
        }
        byteArray.append(chunkByteArray);
    }

    private static class Template {
        public static String string(String text, int slot) {
            return STRING_TEMPLATE.replace("<text>", text)
                    .replace("<slot>", String.valueOf(slot));
        }

        public static String block(String varName, String... items) {
            String itemsString = String.join(",\n", items);
            return BLOCK_TEMPLATE.replace("<var_name>", varName)
                    .replace("<items>", itemsString);
        }

        public static String template(String name, String blocks) {
            return TEMPLATE_TEMPLATE.replace("<blocks>", blocks).replace("<name>", "schematic."+name.replaceFirst("\\.bp$", ""));
        }

        public static void send(String template) {
            try {
                ItemStack item = ModelUtil.createTemplateItem(ModelUtil.compress(template));
                PlayerUtil.giveItem(item);
            } catch (IOException e) {
                MilloLog.errorInGame("Failed to create template item: " + e.getMessage());
            }
        }
    }

}
