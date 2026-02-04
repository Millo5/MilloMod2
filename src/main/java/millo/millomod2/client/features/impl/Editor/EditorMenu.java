package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.features.impl.Editor.elements.MainBody;
import millo.millomod2.client.features.impl.Editor.elements.TitleBar;
import millo.millomod2.client.features.impl.Editor.logic.EditorFileManager;
import millo.millomod2.client.features.impl.Editor.logic.EditorPlot;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.screen.Screen;

public class EditorMenu extends Menu {

    private static EditorPlot loadedPlot;

    private static MainBody cachedBody;
    private static int cachedId = -1;

    private TitleBar titleBar;
    private MainBody mainBody;

    protected EditorMenu(Screen previousScreen) {
        super(previousScreen);
    }

    @Override
    protected void init() {

        if (loadedPlot == null) {
            HypercubeLocation loc = HypercubeAPI.getHypercubeLocation();
            if (loc instanceof Plot plot) loadedPlot = new EditorPlot(plot);
        }

        FlexElement<?> main = FlexElement.create(width, height)
                .background(0x80000000)
                .gap(0)
                .padding(0)
                .direction(ElementDirection.COLUMN)
                .mainAlign(MainAxisAlignment.START)
                .crossAlign(CrossAxisAlignment.STRETCH);

        titleBar = new TitleBar(this);
        titleBar.setLoadedPlot(loadedPlot);

        if (loadedPlot != null && cachedBody != null && cachedId == loadedPlot.getPlotId()) {
            mainBody = cachedBody;
        } else {
            mainBody = new MainBody(this);
            cachedBody = mainBody;

            if (loadedPlot != null) cachedId = loadedPlot.getPlotId();
            mainBody.getHierarchy().reload();
        }


        main.addChildren(
                titleBar,
                mainBody
        );
        addDrawableChild(main);
    }

    public void loadPlot(int plotId) {
        boolean exists = EditorFileManager.plotExists(plotId);
        if (!exists) return;

        loadedPlot = new EditorPlot(plotId);
        titleBar.setLoadedPlot(loadedPlot);

        // Rebuild main body
        mainBody = new MainBody(this);
        cachedBody = mainBody;
        cachedId = loadedPlot.getPlotId();
        mainBody.getHierarchy().reload();

        // Refresh layout
        this.clearChildren();
        this.init();
    }

    public void openTemplate(Template template) {
        addTemplate(template);
        mainBody.getCodeBrowser().openTemplate(template);
        refresh();
    }

    public void addTemplate(Template template) {
        loadedPlot.addTemplate(template);
    }

    public void refresh() {
        mainBody.getHierarchy().reload();
    }

    public EditorPlot getLoadedPlot() {
        return loadedPlot;
    }

    public static void unloadPlot() {
        loadedPlot = null;
    }

    public void openPlotSelector(ButtonElement button) {
        client.setScreen(new PlotSelectorMenu(this));
    }

    public void searchContext(ButtonElement button) {

    }

    public MainBody getMain() {
        return mainBody;
    }
}
