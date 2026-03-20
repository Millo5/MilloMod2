package millo.millomod2.client.features.impl.Editor.logic.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ModelUtil {

    public static TemplateModel parseFromItemNBT(String nbt) {
        JsonObject jsonObject = JsonParser.parseString(nbt).getAsJsonObject();
        return parseFromGzip(jsonObject.get("code").getAsString());
    }

    public static TemplateModel parseFromGzip(String gzip) {
        try {
            String decompressed = decompress(gzip);

            JsonObject json = JsonParser.parseString(decompressed).getAsJsonObject();
            TemplateModel model = new TemplateModel().deserialize(json);

            // Warn for missing data
            JsonUtil.compare(model.serialize(), json);

            return model;
        } catch (Exception e) {
            return null;
        }
    }

    private static String decompress(String compressed) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(compressed));
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toString();
        }
    }

    public static String compress(String str) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(str.getBytes());
        }
        return Base64.getEncoder().encodeToString(bos.toByteArray());
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
