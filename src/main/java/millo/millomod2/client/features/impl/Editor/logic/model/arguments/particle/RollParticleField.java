package millo.millomod2.client.features.impl.Editor.logic.model.arguments.particle;

import com.google.gson.JsonObject;

public class RollParticleField extends ParticleField<RollParticleField> {

    private int roll;

    @Override
    RollParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        roll = jsonObject.get("roll").getAsInt();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("roll", roll);
    }

    @Override
    public String id() {
        return "Roll";
    }

}
