package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class MotionVariationParticleField extends ParticleField<MotionVariationParticleField> {

    private int variation;

    @Override
    MotionVariationParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        variation = jsonObject.get("motionVariation").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("motionVariation", variation);
    }

    @Override
    public String id() {
        return "Motion Variation";
    }

}
