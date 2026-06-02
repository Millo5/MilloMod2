package millo.millomod2.client.hypercube.model.arguments;

import com.google.gson.JsonObject;

public class BucketVariableArgumentModel extends ArgumentModel<BucketVariableArgumentModel> {

    private String name, key, namespaceType, namespaceAlias;

    @Override
    public BucketVariableArgumentModel self() {
        return this;
    }

    @Override
    public String id() {
        return "bucket_var";
    }

    @Override
    protected void deserializeItem(JsonObject jsonObject) {
        this.name = jsonObject.get("name").getAsString();
        this.key = jsonObject.get("key").getAsString();
        this.namespaceType = jsonObject.get("namespace_type").getAsString(); // DEFAULT, ALIAS
        this.namespaceAlias = jsonObject.get("namespace_alias").getAsString();
    }

    @Override
    protected void serializeItem(JsonObject jsonObject) {
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("namespace_type", namespaceType);
        jsonObject.addProperty("namespace_alias", namespaceAlias);
    }

    public String getName() {
        return name;
    }
    public String getKey() {
        return key;
    }

    public String getNamespaceType() {
        return namespaceType;
    }

    public String getNamespaceAlias() {
        return namespaceAlias;
    }

    public boolean usesAlias() {
        return !namespaceType.equals("DEFAULT");
    }
}
