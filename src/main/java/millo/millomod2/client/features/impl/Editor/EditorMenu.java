package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.features.impl.Editor.elements.MainBody;
import millo.millomod2.client.features.impl.Editor.elements.TitleBar;
import millo.millomod2.client.features.impl.Editor.logic.EditorFileManager;
import millo.millomod2.client.features.impl.Editor.logic.EditorPlot;
import millo.millomod2.client.hypercube.data.HypercubeLocation;
import millo.millomod2.client.hypercube.data.Plot;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;

import java.util.ArrayList;

public class EditorMenu extends Menu {

    /**
     * EditorMenu
     *  - TitleBar
     *  - MainBody
     *      - Hierarchy
     *          - Folders / Methods
     *      - CodeBrowser
     *          - Tabs
     *          - CodeTextArea
     * Data:
     *  - EditorPlot
     *      # Current opened plot & plot metadata
     *      - EditorFileManager
     *
     */

    private static ArrayList<EditorPlot.Metadata> recentPlots;
    private static EditorPlot loadedPlot;

    private static MainBody cachedBody;
    private static int cachedId = -1;


    private TitleBar titleBar;
    private MainBody mainBody;

    protected EditorMenu(Screen previousScreen) {
        super(previousScreen);

        recentPlots = EditorFileManager.getRecentPlots();
    }

    @Override
    protected void init() {

        if (loadedPlot == null) {
            HypercubeLocation loc = HypercubeAPI.getHypercubeLocation();
            if (loc instanceof Plot plot) {
                loadedPlot = new EditorPlot(plot);

                recentPlots.removeIf(p -> p.id() == plot.getId());
                recentPlots.add(new EditorPlot.Metadata(plot.getId(), plot.getName(), plot.getOwner()));
                if (recentPlots.size() > 5) recentPlots.removeFirst();

                EditorFileManager.saveRecentPlots(recentPlots);
            }
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
            mainBody.updateMenu(this);
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

        loadPlot(new EditorPlot(plotId));
    }

    public void loadPlot(EditorPlot.Metadata plotMeta) {
        loadPlot(new EditorPlot(plotMeta));
    }

    private void loadPlot(EditorPlot plot) {
        loadedPlot = plot;
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

    private long lastShiftPressTime = 0;

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == 340) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShiftPressTime < 250) {
                mainBody.focusHierarchySearch();
                return true;
            }
            lastShiftPressTime = currentTime;
        }

        if (input.hasCtrl() && input.key() == 70) {
            if (input.hasShift()) {
                mainBody.focusHierarchySearch();
                return true;
            }
            mainBody.focusCodeBrowserSearch();
            return true;
        }
        return super.keyPressed(input);
    }

    public void openTemplate(TemplateModel template) {
        addTemplate(template);
        mainBody.getCodeBrowser().openTemplate(template);
        refresh();
    }

    public void addTemplate(TemplateModel template) {
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

    public MainBody getMain() {
        return mainBody;
    }

    public static MainBody getCachedBody() {
        return cachedBody;
    }

    public ArrayList<EditorPlot.Metadata> getRecentPlots() {
        return recentPlots;
    }

}
