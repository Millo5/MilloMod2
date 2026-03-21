package millo.millomod2.client.hypercube.model.codefields;

import com.google.gson.JsonObject;

public class ActionCodeFields extends CodeFields {

    private String action;
    private String target;
    private String attribute;

    public ActionCodeFields(String actionName, String targetName, String attributeText) {
        this.action = actionName;
        this.target = targetName;
        this.attribute = attributeText;
    }

    @Override
    public void serializeOn(JsonObject jsonObject) {
        if (action != null) jsonObject.addProperty("action", action);
        if (target != null) jsonObject.addProperty("target", target);
        if (attribute != null) jsonObject.addProperty("attribute", attribute);
    }

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public String getAttribute() {
        return attribute;
    }

    public boolean isLSCancel() {
        return "LS-CANCEL".equals(attribute);
    }

    public boolean isNot() {
        return "NOT".equals(attribute);
    }

}
