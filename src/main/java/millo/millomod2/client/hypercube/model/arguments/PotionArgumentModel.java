package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class PotionArgumentModel extends ArgumentModel<PotionArgumentModel> {

    private int duration, amplifier;
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
        this.duration = jsonObject.get("dur").getAsInt();
        this.amplifier = jsonObject.get("amp").getAsInt();
        this.potion = jsonObject.get("pot").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("dur", duration);
        jsonObject.addProperty("amp", amplifier);
        jsonObject.addProperty("pot", potion);
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public String getPotion() {
        return potion;
    }
}
