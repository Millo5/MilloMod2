package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class FadeColorParticleField extends ParticleField<FadeColorParticleField> {

    private int color;

    @Override
    FadeColorParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        color = jsonObject.get("rgb_fade").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("rgb_fade", color);
    }

    @Override
    public String id() {
        return "Fade Color";
    }

}
