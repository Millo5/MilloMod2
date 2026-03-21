package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class SizeParticleField extends ParticleField<SizeParticleField> {

    private double size;

    @Override
    SizeParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        size = jsonObject.get("size").getAsDouble();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("size", size);
    }

    @Override
    public String id() {
        return "Size";
    }

}
