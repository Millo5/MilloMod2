package millo.millomod2.client.hypercube.model.codefields;

import com.google.gson.JsonObject;

public class SubActionCodeFields extends ActionCodeFields {

    private String subAction;

    public SubActionCodeFields(String actionName, String targetName, String attributeText, String subActionName) {
        super(actionName, targetName, attributeText);
        this.subAction = subActionName;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        super.serializeOn(jsonObject);
        if (subAction != null) jsonObject.addProperty("subAction", subAction);
    }

    public String getSubAction() {
        return subAction;
    }
}
