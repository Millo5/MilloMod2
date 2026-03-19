package millo.millomod2.client.features.impl.Editor.logic.model.arguments;

import com.google.gson.JsonObject;

public class PotionArgumentModel extends ArgumentModel<PotionArgumentModel> {

    private double duration, amplifier;
    private String potion;

    @Override
    public PotionArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "pot";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        this.duration = jsonObject.get("dur").getAsDouble();
        this.amplifier = jsonObject.get("amp").getAsDouble();
        this.potion = jsonObject.get("pot").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("dur", duration);
        jsonObject.addProperty("amp", amplifier);
        jsonObject.addProperty("pot", potion);
    }
}
