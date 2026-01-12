package millo.millomod2.client.hypercube.data;

import millo.millomod2.client.features.FeatureHandler;

public class Plot extends HypercubeLocation {

    private final int id;
    private final String name;
    private final String owner;
    private Maybe hasUnderground;


    public Plot(int id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.hasUnderground = Maybe.UNKNOWN;

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
        return hasUnderground == Maybe.TRUE;
    }

    public void setHasUnderground(boolean hasUnderground) {
        this.hasUnderground = hasUnderground ? Maybe.TRUE : Maybe.FALSE;
    }


    private enum Maybe {
        TRUE,
        FALSE,
        UNKNOWN
    }

}
