package millo.millomod2.client.util;

import millo.millomod2.client.util.logging.MilloLog;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class NbtUtil {

    public static CompoundTag read(DataInputStream in) throws IOException {
        byte type = in.readByte();
        if (type != Tag.TAG_COMPOUND) {
            throw MilloLog.throwError("Expected TAG_COMPOUND (10) but found " + type);
        }
        in.readUTF();
        return (CompoundTag) TagTypes.getType(type).load(in, NbtAccounter.uncompressedQuota());
    }

    public static String convertToBlockString(CompoundTag entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getString("Name").orElseThrow());
        if (entry.contains("Properties")) {
            CompoundTag properties = entry.getCompound("Properties").orElseThrow();
            sb.append("[");
            for (String key : properties.keySet()) {
                sb.append(key);
                sb.append("=");
                sb.append(properties.getString(key).orElseThrow());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        return sb.toString();
    }
}
