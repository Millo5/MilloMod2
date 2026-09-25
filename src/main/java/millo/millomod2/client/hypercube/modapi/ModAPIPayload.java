package millo.millomod2.client.hypercube.modapi;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
/**
 * ModAPI payload, make sure Messages are serialized with
 * {@link com.mcdiamondfire.proto.ModAPIUtility#serializeMessage(Message)} or
 * {@link com.mcdiamondfire.proto.ModAPIUtility#serializeMessage(Message, Integer)}
 * and not cast to a String.
 *
 * @param json
 */
public record ModAPIPayload(String json) implements CustomPacketPayload {

    public static final Identifier CHANNEL = Identifier.fromNamespaceAndPath("hypercube", "pm");
    public static final CustomPacketPayload.Type<ModAPIPayload> ID = new CustomPacketPayload.Type<>(CHANNEL);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModAPIPayload> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ModAPIPayload::json, ModAPIPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}