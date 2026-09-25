package millo.millomod2.client.hypercube.data;

import net.minecraft.world.phys.Vec3;

public abstract class HypercubeLocation {

    private Vec3 pos = null;

    public HypercubeLocation() {
    }

    public HypercubeLocation(Vec3 pos) {
        this.pos = pos;
    }

    public Vec3 getPos() {
        return pos;
    }

    public void setPos(Vec3 pos) {
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
