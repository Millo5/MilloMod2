package millo.millomod2.client.hypercube.template;

public class TemplateBlock {

    public String id;
    public String direct;
    public String type;
    public String block;
    public String data;
    public String action;
    public Arguments args;
    public String target;
    public String attribute;
    public String subAction;

    @Override
    public String toString() {
        return "TemplateBlock{" +
                "id='" + id + '\'' +
                ", direct='" + direct + '\'' +
                ", type='" + type + '\'' +
                ", block='" + block + '\'' +
                ", data='" + data + '\'' +
                ", action='" + action + '\'' +
                ", args=" + args +
                ", target='" + target + '\'' +
                ", attribute='" + attribute + '\'' +
                ", subAction='" + subAction + '\'' +
                '}';
    }

}
