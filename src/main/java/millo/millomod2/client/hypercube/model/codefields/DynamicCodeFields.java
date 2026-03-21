package millo.millomod2.client.hypercube.model.codefields;


import com.google.gson.JsonObject;

public class DynamicCodeFields extends CodeFields {

    private final String data;

    public DynamicCodeFields(String data) {
        this.data = data;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        jsonObject.addProperty("data", data);
    }

    public String getData() {
        return data;
    }
}
