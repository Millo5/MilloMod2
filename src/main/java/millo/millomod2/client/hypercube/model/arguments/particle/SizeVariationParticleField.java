package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class SizeVariationParticleField extends ParticleField<SizeVariationParticleField> {

    private int variation;

    @Override
    SizeVariationParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        variation = jsonObject.get("sizeVariation").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("sizeVariation", variation);
    }

    @Override
    public String id() {
        return "Size Variation";
    }

}
