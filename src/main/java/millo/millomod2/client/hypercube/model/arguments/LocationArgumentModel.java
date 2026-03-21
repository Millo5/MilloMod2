package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class LocationArgumentModel extends ArgumentModel<LocationArgumentModel> {

    private boolean isBlock;
    private double x, y, z, pitch, yaw;

    @Override
    public LocationArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "loc";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        isBlock = jsonObject.get("isBlock").getAsBoolean();
        JsonObject loc = jsonObject.get("loc").getAsJsonObject();
        x = loc.get("x").getAsDouble();
        y = loc.get("y").getAsDouble();
        z = loc.get("z").getAsDouble();
        pitch = loc.get("pitch").getAsDouble();
        yaw = loc.get("yaw").getAsDouble();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("isBlock", isBlock);
        JsonObject loc = new JsonObject();
        loc.addProperty("x", x);
        loc.addProperty("y", y);
        loc.addProperty("z", z);
        loc.addProperty("pitch", pitch);
        loc.addProperty("yaw", yaw);
        jsonObject.add("loc", loc);
    }

    public boolean isBlock() {
        return isBlock;
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

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }
}
