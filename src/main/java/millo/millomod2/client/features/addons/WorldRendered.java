package millo.millomod2.client.features.addons;

import millo.millomod2.client.rendering.world.Renderer;

import java.util.ArrayList;

public interface WorldRendered {

    ArrayList<WorldRendered> features = new ArrayList<>();

    static void register(WorldRendered worldRendered) {
        features.add(worldRendered);
    }

    void worldRender(Renderer renderer);

}
