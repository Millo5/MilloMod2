package millo.millomod2.client.hypercube.template;

import com.google.gson.JsonObject;

public class ArgumentItemData {

    // Block Tags
    public String option;
    public String tag;
    public String action;
    public String block;

    // Values
    public String type;
    public String name;
    public String scope;
    public boolean legacy;

    // Arguments
    public boolean plural;
    public boolean optional;
    public String description;

    // Game Value
    public String target;

    // Other
    public JsonObject data;
    public String item;
    public LocationData loc;
    public boolean isBlock;

    public String particle;
    public ClusterData cluster;

    public String sound;
    public double pitch;
    public double vol;
    public String variant;

    public String pot;
    public int amp;
    public int dur;

    // Vectors
    public double x;
    public double y;
    public double z;


    @Override
    public String toString() {
        return "ArgumentItemData{" +
                "option='" + option + '\'' +
                ", tag='" + tag + '\'' +
                ", action='" + action + '\'' +
                ", block='" + block + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", scope='" + scope + '\'' +
                ", legacy=" + legacy +
                ", plural=" + plural +
                ", optional=" + optional +
                ", description='" + description + '\'' +
                ", target='" + target + '\'' +
                ", data=" + data +
                ", item='" + item + '\'' +
                ", loc=" + loc +
                ", particle='" + particle + '\'' +
                ", cluster=" + cluster +
                ", sound='" + sound + '\'' +
                ", pitch=" + pitch +
                ", variant=" + variant +
                ", vol=" + vol +
                ", pot='" + pot + '\'' +
                ", amp=" + amp +
                ", dur=" + dur +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
