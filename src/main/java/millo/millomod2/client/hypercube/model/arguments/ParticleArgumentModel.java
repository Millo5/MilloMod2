package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;
import millo.millomod2.client.hypercube.model.arguments.particle.ParticleField;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.util.MilloLog;

import java.util.ArrayList;

public class ParticleArgumentModel extends ArgumentModel<ParticleArgumentModel> {

    private String particle;

    // Cluster
    private int amount;
    private double horizontal;
    private double vertical;

    private ArrayList<ParticleField<?>> dataFields;
    // "Motion", "Motion Variation", "Color", "Color Variation", "Duration", "Roll", "Fade Color"


    @Override
    public ParticleArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "part";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        particle = jsonObject.get("particle").getAsString();

        JsonObject cluster = jsonObject.get("cluster").getAsJsonObject();
        amount = cluster.get("amount").getAsInt();
        horizontal = cluster.get("horizontal").getAsDouble();
        vertical = cluster.get("vertical").getAsDouble();

        JsonObject data = jsonObject.get("data").getAsJsonObject();
        dataFields = new ArrayList<>();
        ActionDump actionDump = ActionDump.getActionDump().orElseThrow();
        for (String fieldName : actionDump.getParticleFields(particle)) {
            try {
                ParticleField<? extends ParticleField<?>> field = ParticleField.getParticleField(fieldName);
                field.deserialize(data);
                dataFields.add(field);
            } catch (Exception e) {
                MilloLog.logWarning("Failed to deserialize particle field " + fieldName + " for particle " + particle + ": " + e.getMessage());
            }
        }

    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("particle", particle);

        JsonObject cluster = new JsonObject();
        cluster.addProperty("amount", amount);
        cluster.addProperty("horizontal", horizontal);
        cluster.addProperty("vertical", vertical);
        jsonObject.add("cluster", cluster);

        JsonObject data = new JsonObject();
        for (ParticleField<?> field : dataFields) {
            field.serializeOn(data);
        }
        jsonObject.add("data", data);
    }

    private int getDataInt(JsonObject data, String key) {
        return data.has(key) ? data.get(key).getAsInt() : 0;
    }

    private double getDataDouble(JsonObject data, String key) {
        return data.has(key) ? data.get(key).getAsDouble() : 0;
    }

    //


    public ArrayList<ParticleField<?>> getDataFields() {
        return dataFields;
    }

    public double getHorizontal() {
        return horizontal;
    }

    public double getVertical() {
        return vertical;
    }

    public int getAmount() {
        return amount;
    }

    public String getParticle() {
        return particle;
    }
}
