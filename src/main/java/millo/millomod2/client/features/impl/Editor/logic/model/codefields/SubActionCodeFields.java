package millo.millomod2.client.features.impl.Editor.logic.model.codefields;

import com.google.gson.JsonObject;

public class SubActionCodeFields extends ActionCodeFields {

    public String subActionName;

    public SubActionCodeFields(String actionName, String targetName, String attributeText, String subActionName) {
        super(actionName, targetName, attributeText);
        this.subActionName = subActionName;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        super.serializeOn(jsonObject);
        if (subActionName != null) jsonObject.addProperty("subAction", subActionName);
    }

}
