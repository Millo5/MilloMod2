package millo.millomod2.client.features.impl.Editor.logic.model.codefields;

import com.google.gson.JsonObject;

public class ActionCodeFields extends CodeFields {

    private String actionName;
    private String targetName;
    private String attributeText;

    public ActionCodeFields(String actionName, String targetName, String attributeText) {
        this.actionName = actionName;
        this.targetName = targetName;
        this.attributeText = attributeText;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        if (actionName != null) jsonObject.addProperty("action", actionName);
        if (targetName != null) jsonObject.addProperty("target", targetName);
        if (attributeText != null) jsonObject.addProperty("attribute", attributeText);
    }
}
