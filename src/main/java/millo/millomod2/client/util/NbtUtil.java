package millo.millomod2.client.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtSizeTracker;
import net.minecraft.nbt.NbtTypes;

import java.io.DataInputStream;
import java.io.IOException;

public class NbtUtil {

    public static NbtCompound read(DataInputStream in) throws IOException {
        byte type = in.readByte();
        if (type != NbtElement.COMPOUND_TYPE) {
            throw MilloLog.throwError("Expected TAG_COMPOUND (10) but found " + type);
        }
        in.readUTF();
        return (NbtCompound) NbtTypes.byId(type).read(in, NbtSizeTracker.forLevel());
    }

    public static String convertToBlockString(NbtCompound entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.getString("Name").orElseThrow());
        if (entry.contains("Properties")) {
            NbtCompound properties = entry.getCompound("Properties").orElseThrow();
            sb.append("[");
            for (String key : properties.getKeys()) {
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
