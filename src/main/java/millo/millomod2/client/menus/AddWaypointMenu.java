package millo.millomod2.client.menus;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Waypoints.Waypoint;
import millo.millomod2.client.features.impl.Waypoints.Waypoints;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class AddWaypointMenu extends Menu {

    private final Waypoints feature;
    private Vec3 defaultPos;

    private TextFieldElement inputX;
    private TextFieldElement inputY;
    private TextFieldElement inputZ;
    private TextFieldElement inputTitle;
    private TextFieldElement inputColor;


    private String preInputTitle = "New Waypoint";

    public AddWaypointMenu(Screen parent, Vec3 pos, String name) {
        this(parent, pos);
        preInputTitle = name;
    }

    public AddWaypointMenu(Screen parent, Vec3 pos) {
        super(parent == null ? new WaypointMenu(null) : parent);
        defaultPos = pos;
        feature = FeatureHandler.get(Waypoints.class);
    }

    @Override
    protected void init() {
        ListElement main = ListElement.create(width /4 * 3, height)
                .position(width / 8, 0)
                .maxExpansion(height)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(40)
                .gap(10);
        addRenderableWidget(main);


        FlexElement<?> posInput = FlexElement.create(width / 2, 20)
                .background(0x80000000)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(20)
                .gap(5);
        main.addChild(posInput);
        FlexElement<?> posInputFields = FlexElement.create(200, 14)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .gap(5);
        inputX = new TextFieldElement(60, 14, Component.literal(String.valueOf(defaultPos.x)));
        inputY = new TextFieldElement(60, 14, Component.literal(String.valueOf(defaultPos.y)));
        inputZ = new TextFieldElement(60, 14, Component.literal(String.valueOf(defaultPos.z)));
        inputX.setValue(String.format("%.2f", defaultPos.x).replace(",", "."));
        inputY.setValue(String.format("%.2f", defaultPos.y).replace(",", "."));
        inputZ.setValue(String.format("%.2f", defaultPos.z).replace(",", "."));

        posInputFields.addChildren(inputX, inputY, inputZ);

        var posInputLabel = TextElement.create("Position");
        posInput.addChildren(posInputLabel, posInputFields);



        FlexElement<?> titleInput = FlexElement.create(width / 2, 20)
                .background(0x80000000)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(20)
                .gap(5);
        main.addChild(titleInput);
        inputTitle = new TextFieldElement(200, 14, Component.literal(preInputTitle));
        inputTitle.setValue(preInputTitle);
        inputColor = new TextFieldElement(40, 14, Component.literal("#ffffff"));
        inputColor.setValue("#ffffff");

        var titleInputLabel = TextElement.create("Label");
        titleInput.addChildren(titleInputLabel, inputTitle, inputColor);

        var saveButton = ButtonElement.create(200, 20)
                .background(0x80000000)
                .hoverBackground(0x80333333)
                .onPress((button) -> save())
                .message(Component.literal("Save"));
        var cancelButton = ButtonElement.create(200, 20)
                .background(0x80000000)
                .hoverBackground(0x80333333)
                .onPress((button) -> onClose())
                .message(Component.literal("Cancel"));

        main.addChildren(saveButton, cancelButton);
    }

    private void save() {
        try {
            double x = Double.parseDouble(inputX.getValue());
            double y = Double.parseDouble(inputY.getValue());
            double z = Double.parseDouble(inputZ.getValue());

            Waypoint waypoint = new Waypoint(new Vec3(x, y, z), inputTitle.getValue(), Styles.ofHex(inputColor.getValue()).getColor().getValue());
            feature.add(waypoint);

            onClose();
        } catch (Exception e) {
            MilloLog.errorInGame(e.getMessage());
        }
    }

    protected void renderBlurredBackground(GuiGraphics context) {}

    public void setName(String name) {
        inputTitle.setValue(name);
    }
}
