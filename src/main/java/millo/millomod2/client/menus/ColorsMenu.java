package millo.millomod2.client.menus;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import millo.millomod2.client.rendering.gui.ColorSelectArea;
import millo.millomod2.client.rendering.gui.ColorSquare;
import millo.millomod2.client.rendering.gui.ColorValueSlider;
import millo.millomod2.client.util.FileUtil;
import millo.millomod2.client.util.HypercubeAPI;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.GridElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ColorsMenu extends Menu {
    private static final String FILENAME = "colors.json";
    private static JsonObject savedData;

    private static int[] cachedRGB = null;
    private static ListElement cachedRecent;

    private boolean lock;
    private final HashMap<String, TextFieldElement> inputFields = new HashMap<>();
    private final float[] hsb = new float[3];
    private final int[] rgb = new int[3];

    private final ArrayList<Integer> savedColors = new ArrayList<>();
    private final int plotId;

    private ListElement recentColors;
    private GridElement savedGrid;

    public ColorsMenu(Screen parent) {
        super(parent);

        plotId = HypercubeAPI.getPlotId();

        if (plotId > 0) {
            if (savedData == null) {
                String jsonStr = FileUtil.readJson(FILENAME);
                if (jsonStr != null) savedData = (JsonObject) JsonParser.parseString(jsonStr);
                else savedData = new JsonObject();
            }

            String pid = String.valueOf(plotId);
            if (savedData.has(pid)) {
                JsonArray saved = savedData.getAsJsonArray(pid);
                for (JsonElement jsonElement : saved) savedColors.add(jsonElement.getAsInt());
            }
        }
    }

    @Override
    protected void init() {
        if (cachedRGB != null) {
            rgb[0] = cachedRGB[0];
            rgb[1] = cachedRGB[1];
            rgb[2] = cachedRGB[2];
        } else rgb[0] = 255;
        cachedRGB = rgb;

        FlexElement<?> screen = FlexElement.create(width, height)
                .mainAlign(MainAxisAlignment.CENTER)
                .crossAlign(CrossAxisAlignment.CENTER);

        FlexElement<?> main = FlexElement.create(256, 256)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .direction(ElementDirection.COLUMN)
                .background(0x80000000)
                .gap(5)
                .padding(10);


        // Top half
        ListElement topHalf = ListElement.create(10, 128 - 10- 5)
                .gap(10)
                .crossAlign(CrossAxisAlignment.START)
                .direction(ElementDirection.ROW);
        main.addChild(topHalf);

        {
            FlexElement<?> rawComponents = FlexElement.create(40, topHalf.getHeight())
                    .gap(5)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.CENTER)
                    .direction(ElementDirection.COLUMN);
            topHalf.addChildren(rawComponents);

            FlexElement<?> RGBFields = FlexElement.create(40, topHalf.getHeight() / 2 - 2)
                    .gap(5)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.STRETCH)
                    .direction(ElementDirection.COLUMN);
            FlexElement<?> HSBFields = FlexElement.create(40, topHalf.getHeight() / 2 - 2)
                    .gap(5)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.STRETCH)
                    .direction(ElementDirection.COLUMN);

            HSBFields.addChildren(
                    fieldEntry("Hu", (v) -> {
                        hsb[0] = Math.clamp(v, 0, 360) / 360f;
                        updateFromHSB(false);
                    }),
                    fieldEntry("Sa", (v) -> {
                        hsb[1] = Math.clamp(v, 0, 100) / 100f;
                        updateFromHSB(false);
                    }),
                    fieldEntry("Br", (v) -> {
                        hsb[2] = Math.clamp(v, 0, 100) / 100f;
                        updateFromHSB(false);
                    })
            );
            RGBFields.addChildren(
                    fieldEntry("Re", (v) -> {
                        rgb[0] = Math.clamp(v, 0, 255);
                        updateFromRGB(false);
                    }),
                    fieldEntry("Gr", (v) -> {
                        rgb[1] = Math.clamp(v, 0, 255);
                        updateFromRGB(false);
                    }),
                    fieldEntry("Bl", (v) -> {
                        rgb[2] = Math.clamp(v, 0, 255);
                        updateFromRGB(false);
                    })
            );

            rawComponents.addChildren(HSBFields, RGBFields);
        }

        {
            FlexElement<?> column = FlexElement.create(50, topHalf.getHeight())
                    .padding(0)
                    .gap(5)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.START)
                    .direction(ElementDirection.COLUMN);
            topHalf.addChild(column);

            ListElement topColumn = ListElement.create(50, topHalf.getHeight() / 2)
                    .padding(2)
                    .gap(2)
                    .crossAlign(CrossAxisAlignment.CENTER)
                    .direction(ElementDirection.COLUMN);
            column.addChildren(topColumn);

            TextFieldElement hexInput = new TextFieldElement(50, 15, Text.literal("#abcdef"));
            hexInput.setChangedListener((v) -> {
                if (v.matches("^#[0-9a-fA-F]{6}$")) {
                    var col = Color.decode(v);
                    rgb[0] = col.getRed();
                    rgb[1] = col.getGreen();
                    rgb[2] = col.getBlue();
                    updateFromRGB(true);
                }
            });
            inputFields.put("hex", hexInput);
            topColumn.addChild(hexInput);
            topColumn.addChild(ButtonElement.create(50, 14)
                    .background(0x80000000)
                    .message(Text.literal("Copy"))
                    .onPress((button) -> {
                        client.keyboard.setClipboard(hexInput.getText());
                        saveAsRecent();
                    }));
            topColumn.addChild(ButtonElement.create(50, 14)
                    .background(0x80000000)
                    .message(Text.literal("<Copy>"))
                    .onPress((button) -> {
                        client.keyboard.setClipboard("<"+hexInput.getText()+">");
                        saveAsRecent();
                    }));



            ListElement bottomColumn = ListElement.create(50, topHalf.getHeight() / 2)
                    .padding(4)
                    .gap(2)
                    .crossAlign(CrossAxisAlignment.CENTER)
                    .direction(ElementDirection.COLUMN);
            column.addChildren(bottomColumn);
            bottomColumn.addChild(ButtonElement.create(50, 14)
                    .background(0x80000000)
                    .message(Text.literal("Save"))
                    .onPress((button) -> {
                        int c = new Color(rgb[0], rgb[1], rgb[2]).hashCode();
                        if (savedColors.contains(c)) return;

                        savedColors.add(c);
                        refreshSavedGrid();
                    }));
            if (cachedRecent != null) {
                recentColors = cachedRecent;
                for (ClickableWidget child : recentColors.getChildren()) {
                    if (child instanceof ColorSquare cs) cs.setMenu(this);
                }
            }
            else recentColors = ListElement.create(50, 15)
                    .direction(ElementDirection.ROW)
                    .gap(2)
                    .crossAlign(CrossAxisAlignment.CENTER);
            bottomColumn.addChild(recentColors);
            cachedRecent = recentColors;
        }
        {
            savedGrid = new GridElement(110, topHalf.getHeight())
                    .padding(2)
                    .gap(2);
            topHalf.addChild(savedGrid);
        }


        updateFromRGB(true);

        // Bottom half
        FlexElement<?> colorPicker = FlexElement.create(10, 128 - 10 - 5)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .direction(ElementDirection.ROW);
        main.addChild(colorPicker);

        var slider = new ColorValueSlider(this, 20, 10, hsb);
        var area = new ColorSelectArea(this, 256-45, 10, hsb);

        colorPicker.addChildren(slider, area);

        screen.addChild(main);
        addDrawableChild(screen);
        refreshSavedGrid();
    }

    public void removeSavedColor(int color) {
        savedColors.removeIf(i -> i.equals(color));
        refreshSavedGrid();
    }

    private void refreshSavedGrid() {
        savedGrid.clearChildren();
        for (Integer savedColor : savedColors) {
            savedGrid.addChild(new ColorSquare(15, 15, savedColor, this, true));
        }
    }

    private void saveAsRecent() {
        ArrayList<ClickableWidget> copy = new ArrayList<>(recentColors.getChildren());
        if (copy.size() > 2) copy.remove(0);
        copy.add(new ColorSquare(15, 15, new Color(rgb[0], rgb[1], rgb[2]).hashCode(), this, false));

        recentColors.clearChildren();
        recentColors.addChildren(copy);
    }

    private FlexElement<?> fieldEntry(String label, Consumer<Integer> change) {
        FlexElement<? extends FlexElement<?>> field = FlexElement.create(50, 20)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .direction(ElementDirection.ROW);

        var text = new TextFieldElement(35, 15, Text.literal("-"));
        text.setChangedListener((v) -> {
            try {
                change.accept(Integer.parseInt(v));
            } catch (NumberFormatException ignored) {}
        });
        field.addChildren(
                TextElement.create(Text.literal(String.valueOf(label.charAt(0))).setStyle(Styles.COMMENT.getStyle()))
                        .offset(1, 0),
                text
        );

        inputFields.put(label, text);
        return field;
    }


    public void updateFromHSB(boolean updateText) {
        if (lock) return;
        lock = true;

        var col = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        rgb[0] = col.getRed();
        rgb[1] = col.getGreen();
        rgb[2] = col.getBlue();


        String hex = String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
        inputFields.get("hex").setText(hex);

        inputFields.get("Re").setText(String.valueOf(rgb[0]));
        inputFields.get("Gr").setText(String.valueOf(rgb[1]));
        inputFields.get("Bl").setText(String.valueOf(rgb[2]));

        if (updateText) {
            inputFields.get("Hu").setText(String.valueOf((int) (hsb[0] * 360)));
            inputFields.get("Sa").setText(String.valueOf((int) (hsb[1] * 100)));
            inputFields.get("Br").setText(String.valueOf((int) (hsb[2] * 100)));
        }

        lock = false;
    }

    public void updateFromRGB(boolean updateText) {
        if (lock) return;
        lock = true;
        Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

        String hex = String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
        inputFields.get("hex").setText(hex);

        inputFields.get("Hu").setText(String.valueOf((int) (hsb[0] * 360)));
        inputFields.get("Sa").setText(String.valueOf((int) (hsb[1] * 100)));
        inputFields.get("Br").setText(String.valueOf((int) (hsb[2] * 100)));

        if (updateText) {
            inputFields.get("Re").setText(String.valueOf(rgb[0]));
            inputFields.get("Gr").setText(String.valueOf(rgb[1]));
            inputFields.get("Bl").setText(String.valueOf(rgb[2]));
        }
        lock = false;
    }


    protected void applyBlur(DrawContext context) {}

    @Override
    public void close() {

        /*

        if (plotId > 0) {
            if (savedData == null) {
                String jsonStr = FileUtil.readJson("colors");
                savedData = (JsonObject) JsonParser.parseString(jsonStr);
            }

            String pid = String.valueOf(plotId);
            if (savedData.has(pid)) {
                JsonArray saved = savedData.getAsJsonArray(pid);
                for (JsonElement jsonElement : saved) savedColors.add(jsonElement.getAsInt());
            }
        }
         */
        if (!savedColors.isEmpty() && plotId > 0) {
            JsonArray array = new JsonArray();
            for (Integer savedColor : savedColors) {
                array.add(savedColor);
            }

            String pid = String.valueOf(plotId);
            savedData.add(pid, array);

            FileUtil.writeJson(FILENAME, savedData);
        }

        super.close();
    }

    public void setColor(int color) {
        rgb[0] = (color >> 16) & 0xFF;
        rgb[1] = (color >> 8) & 0xFF;
        rgb[2] = color & 0xFF;
        updateFromRGB(true);
    }
}
