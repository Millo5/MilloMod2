package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class VectorArgumentModel extends ArgumentModel<VectorArgumentModel> {

    private double x, y, z;

    @Override
    public VectorArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "vec";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        x = jsonObject.get("x").getAsDouble();
        y = jsonObject.get("y").getAsDouble();
        z = jsonObject.get("z").getAsDouble();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("z", z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
