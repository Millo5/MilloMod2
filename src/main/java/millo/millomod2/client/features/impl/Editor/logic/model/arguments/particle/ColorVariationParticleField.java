package millo.millomod2.client.features.impl.Editor.logic.model.arguments.particle;

import com.google.gson.JsonObject;

public class ColorVariationParticleField extends ParticleField<ColorVariationParticleField> {

    private int colorVariation;

    @Override
    ColorVariationParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        colorVariation = jsonObject.get("colorVariation").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("colorVariation", colorVariation);
    }

    @Override
    public String id() {
        return "Color Variation";
    }

}
