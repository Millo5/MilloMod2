package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class MotionParticleField extends ParticleField<MotionParticleField> {

    private double x, y, z;

    @Override
    MotionParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        x = jsonObject.get("x").getAsDouble();
        y = jsonObject.get("y").getAsDouble();
        z = jsonObject.get("z").getAsDouble();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("z", z);
    }

    @Override
    public String id() {
        return "Motion";
    }

}
