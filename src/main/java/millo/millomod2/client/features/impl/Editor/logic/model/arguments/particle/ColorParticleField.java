package millo.millomod2.client.features.impl.Editor.logic.model.arguments.particle;

import com.google.gson.JsonObject;

public class ColorParticleField extends ParticleField<ColorParticleField> {

    private int color;

    @Override
    ColorParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        color = jsonObject.get("rgb").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("rgb", color);
    }

    @Override
    public String id() {
        return "Color";
    }

}
