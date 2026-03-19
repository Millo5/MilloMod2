package millo.millomod2.client.features.impl.Editor.logic.model.arguments.particle;

import com.google.gson.JsonObject;

public class MaterialParticleField extends ParticleField<MaterialParticleField> {

    private String material;

    @Override
    MaterialParticleField self() {
        return this;
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        material = jsonObject.get("material").getAsString();
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("material", material);
    }

    @Override
    public String id() {
        return "Material";
    }

}
