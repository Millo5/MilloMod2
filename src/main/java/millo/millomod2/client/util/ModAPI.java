package millo.millomod2.client.util;

import millo.millomod2.client.hypercube.modapi.ModAPIPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

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
