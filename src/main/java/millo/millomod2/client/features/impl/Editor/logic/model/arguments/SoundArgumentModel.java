package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class SoundArgumentModel extends ArgumentModel<SoundArgumentModel> {

    private double volume, pitch;
    private String sound, variant;

    @Override
    public SoundArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "snd";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        sound = jsonObject.get("sound").getAsString();
        volume = jsonObject.get("vol").getAsDouble();
        pitch = jsonObject.get("pitch").getAsDouble();
        variant = jsonObject.has("variant") ? jsonObject.get("variant").getAsString() : null;
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("pitch", pitch);
        jsonObject.addProperty("vol", volume);
        jsonObject.addProperty("sound", sound);
        if (variant != null) jsonObject.addProperty("variant", variant);
    }
}
