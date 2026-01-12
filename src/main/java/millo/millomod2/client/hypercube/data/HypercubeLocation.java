package millo.millomod2.client.hypercube.data;

import net.minecraft.util.math.Vec3d;

public abstract class HypercubeLocation {

    private Vec3d pos = null;

    public HypercubeLocation() {
    }

    public HypercubeLocation(Vec3d pos) {
        this.pos = pos;
    }

    public Vec3d getPos() {
        return pos;
    }

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public Plot update(String name, int id, String owner) {
        if (this instanceof Plot plot && plot.getId() == id) {
            return plot;
        }
        Plot plot = new Plot(id, name, owner);
        plot.setPos(this.pos);
        return plot;
    }


    public static class UnknownLocation extends HypercubeLocation {
        public UnknownLocation() {
            super();
        }
    }

}
