package millo.millomod2.client.hypercube.data;

public class Plot extends HypercubeLocation {

    private final int id;
    private final String name;
    private Maybe hasUnderground;


    public Plot(int id, String name) {
        this.id = id;
        this.name = name;
        this.hasUnderground = Maybe.UNKNOWN;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
