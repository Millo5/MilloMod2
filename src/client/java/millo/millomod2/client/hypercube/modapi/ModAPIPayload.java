package millo.millomod2.client.hypercube.modapi;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
/**
 * ModAPI payload, make sure Messages are serialized with
 * {@link com.mcdiamondfire.proto.ModAPIUtility#serializeMessage(Message)} or
 * {@link com.mcdiamondfire.proto.ModAPIUtility#serializeMessage(Message, Integer)}
 * and not cast to a String.
 *
 * @param json
 */
public record ModAPIPayload(String json) implements CustomPayload {

    public static final Identifier CHANNEL = Identifier.of("hypercube", "pm");
    public static final CustomPayload.Id<ModAPIPayload> ID = new CustomPayload.Id<>(CHANNEL);
    public static final PacketCodec<RegistryByteBuf, ModAPIPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ModAPIPayload::json, ModAPIPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}