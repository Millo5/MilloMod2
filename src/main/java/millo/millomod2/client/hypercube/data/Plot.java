package millo.millomod2.client.hypercube.data;

import millo.millomod2.client.features.FeatureHandler;

public class Plot extends HypercubeLocation {

    private final int id;
    private final String name;
    private final String owner;
    private boolean hasUnderground;
    private boolean isMega;


    public Plot(int id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.hasUnderground = false;

        FeatureHandler.onEnterPlot(this);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public boolean hasUnderground() {
        return hasUnderground;
    }

    public void setHasUnderground(boolean hasUnderground) {
        this.hasUnderground = hasUnderground;
    }

    public boolean isMega() {
        return isMega;
    }

    public void setMega(boolean mega) {
        isMega = mega;
    }

    public int getFloorHeight() {
        return hasUnderground ? 5 : 50;
    }

    public double getDepth() {
        return isMega ? 300 : 20;
    }
}
