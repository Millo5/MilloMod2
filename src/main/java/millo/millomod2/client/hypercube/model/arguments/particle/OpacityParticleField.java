package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class OpacityParticleField extends ParticleField<OpacityParticleField> {

    private int opacity;

    @Override
    OpacityParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        opacity = jsonObject.get("opacity").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("opacity", opacity);
    }

    @Override
    public String id() {
        return "Opacity";
    }

}
