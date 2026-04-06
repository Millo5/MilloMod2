package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class PowerParticleField extends ParticleField<PowerParticleField> {

    private double power;

    @Override
    PowerParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        power = jsonObject.get("power").getAsDouble();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("power", power);
    }

    @Override
    public String id() {
        return "Power";
    }

}
