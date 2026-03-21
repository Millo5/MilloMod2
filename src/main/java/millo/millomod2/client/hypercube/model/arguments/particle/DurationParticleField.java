package millo.millomod2.client.hypercube.model.arguments.particle;

import com.google.gson.JsonObject;

public class DurationParticleField extends ParticleField<DurationParticleField> {

    private int time;

    @Override
    DurationParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        time = jsonObject.get("time").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("time", time);
    }

    @Override
    public String id() {
        return "Duration";
    }

}
