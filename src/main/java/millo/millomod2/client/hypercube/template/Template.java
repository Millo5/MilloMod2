package millo.millomod2.client.hypercube.template;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.client.util.JsonUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@Deprecated(forRemoval = true)
public class Template {

    public String b64Code;
    public ArrayList<TemplateBlock> blocks;

    public static Template parseItemNBT(String codeTemplateData) {
        CodeTemplateData templateData = new Gson().fromJson(codeTemplateData, CodeTemplateData.class);
        return parseBase64(templateData.code);
    }

    public static Template parseBase64(String data) {
        try {
            byte[] decompressed = decompress(Base64.getDecoder().decode(data));
            String jsonString = new String(decompressed);

            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            TemplateModel model = new TemplateModel().deserialize(json);
            JsonUtil.compare(model.serialize(), json);

//            PlayerUtil.giveItem(createTemplateItem(toCompressedBase64(model.serialize().toString())));

            Template template = new Gson().fromJson(jsonString, Template.class);
            template.b64Code = data;
            return template;
        } catch (IOException e) {
            return null;
        }
    }

    private static byte[] decompress(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

    private static String toCompressedBase64(String data) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             java.util.zip.GZIPOutputStream gzipOS = new java.util.zip.GZIPOutputStream(bos)) {
            gzipOS.write(data.getBytes());
            gzipOS.close();
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to compress data", e);
        }
    }

    public String getName() {
        if (blocks == null || blocks.isEmpty()) return "Empty Template";

        String name = blocks.getFirst().data;
        if (name == null) name = blocks.getFirst().action;
        if (name.isEmpty()) name = blocks.getFirst().block + "_empty";
        return name;
    }

    public MethodType getTemplateMethodType() {
        if (blocks == null || blocks.isEmpty()) return MethodType.FUNC;

        return MethodType.valueOf(blocks.getFirst().block.toUpperCase());
    }

    /**
     * @return The method name with a ".type" suffix, where type is determined by the first block in the template
     */
    public String getMethodName() {
        return getTemplateMethodType().suffixString(getName());
    }

    public ItemStack getItem() {
        return createTemplateItem(b64Code);
    }


    public static ItemStack createTemplateItem(String b64Code) {
        ItemStack item = new ItemStack(Items.ENDER_CHEST);

        NbtCompound nbt = new NbtCompound();

        NbtCompound pbv = new NbtCompound();
        String data = "{\"author\":\"MILLOMOD\",\"name\":\"§6» §e#NAME\",\"version\":1,\"code\":\"#CODE\"}"
                .replace("#NAME", "Template")
                .replace("#CODE", b64Code);
        pbv.putString("hypercube:codetemplatedata", data);

        nbt.put("PublicBukkitValues", pbv);

        NbtComponent custom_data = NbtComponent.of(nbt);

        item.set(DataComponentTypes.ITEM_NAME, Text.literal("Template").setStyle(Styles.BLOCK_TAG.getStyle()));
        item.set(DataComponentTypes.CUSTOM_DATA, custom_data);

        return item;
    }

}
