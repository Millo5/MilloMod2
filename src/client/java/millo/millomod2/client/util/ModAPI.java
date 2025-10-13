package millo.millomod2.client.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.mcdiamondfire.proto.ModAPIMessage;
import com.mcdiamondfire.proto.ModAPIMessages;
import com.mcdiamondfire.proto.ModAPIUtility;
import com.mcdiamondfire.proto.messages.serverbound.server.C2SHandshakeRequest;
import com.mcdiamondfire.proto.messages.serverbound.server.C2SHandshakeRequestOrBuilder;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.hypercube.modapi.ModAPIPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Objects;
import java.util.Optional;

public class ModAPI {

    public static void onPayload(ModAPIPayload payload, ClientPlayNetworking.Context context) {
//        MilloLog.logInGame("Received ModAPI payload: " + payload);
//
//        JsonObject json = JsonParser.parseString(payload.json()).getAsJsonObject();
//        MilloLog.logInGame("Parsed JSON: " + json);
//
//        String packetId = json.get("packet_id").getAsString();
//        if (Objects.equals(packetId, "s2c_hello")) {
//            try {
//                sendPayload(C2SHandshakeRequest.newBuilder().build());
//            } catch (InvalidProtocolBufferException e) {
//                MilloLog.logInGame("Failed to send handshake request: " + e.getMessage());
//            }
//        }
//
//
    }

//    public static void sendPayload(Message payload) throws InvalidProtocolBufferException {
//        if (MilloMod.MC.player == null || MilloMod.MC.getNetworkHandler() == null) return;
//
//        String serialized = ModAPIUtility.serializeMessage(payload);
//        ClientPlayNetworking.send(new ModAPIPayload());
//
////        ModAPIMessages.getMessageClass()
//
////        ModAPIUtility.serializeMessage(payload.json());
////        ClientPlayNetworking.send(ModAPIPayload.ID, ModAPIPayload.CODEC.encodeStart(payload).getBytes());
////        MilloLog.logInGame("Sent ModAPI payload: " + payload);
//    }

}
