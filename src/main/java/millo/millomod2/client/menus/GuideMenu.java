package millo.millomod2.client.menus;

import millo.millomod2.client.features.guides.FeatureGuide;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuideMenu extends Menu {

    private ListElement featureList;
    private ListElement guideContent;
    private final String initialGuideName;
    private FeatureGuide selectedGuide;
    private final Map<FeatureGuide, ButtonElement> guideButtons = new HashMap<>();

    public GuideMenu(Screen parent) {
        this(parent, null);
    }

    public GuideMenu(Screen parent, String initialGuideName) {
        super(parent);
        this.initialGuideName = initialGuideName;
    }

    @Override
    protected void init() {

        int mainWidth = (int) (width * 0.8);
        int mainHeight = (int) (height * 0.8);
        int padding = 10;
        int headerHeight = 12;
        int contentGap = 6;
        int contentWidth = mainWidth - padding * 2;
        int contentHeight = mainHeight - padding * 2 - headerHeight - contentGap;
        int featureListWidth = Math.max(120, contentWidth / 4);
        int guideContentWidth = contentWidth - featureListWidth - contentGap;

        FlexElement<?> screen = FlexElement.create(width, height)
                .mainAlign(MainAxisAlignment.CENTER)
                .crossAlign(CrossAxisAlignment.CENTER);

        FlexElement<?> main = FlexElement.create(mainWidth, mainHeight)
                .mainAlign(MainAxisAlignment.START)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .direction(ElementDirection.COLUMN)
                .background(0x80000000)
                .gap(contentGap)
                .padding(padding);

        TextElement header = TextElement.create("MilloMod Guide")
                .align(TextElement.TextAlignment.CENTER);

        FlexElement<?> content = FlexElement.create(contentWidth, contentHeight)
                .direction(ElementDirection.ROW)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .gap(contentGap);

        featureList = createScrollList(featureListWidth, contentHeight, 0, 0)
                .border(new ClickableElement.Border().right(0xAA666666));
        guideContent = createScrollList(guideContentWidth, contentHeight, 6, 5);

        String lastCategory = null;
        ArrayList<FeatureGuide> guides = FeatureGuide.getGuides();
        if (selectedGuide == null) {
            selectedGuide = guides.stream()
                    .filter(guide -> guide.getName().equals(initialGuideName))
                    .findFirst()
                    .orElse(guides.isEmpty() ? null : guides.getFirst());
        }

        for (FeatureGuide guide : guides) {
            if (!guide.getCategory().equals(lastCategory)) {
                featureList.addChild(TextElement.create(Text.literal(guide.getCategory())));
                lastCategory = guide.getCategory();
            }
            ButtonElement button = ButtonElement.create(featureListWidth, 20)
                    .message(Text.literal(guide.getName()))
                    .onPress((b) -> {
                        selectedGuide = guide;
                        showGuide(guide);
                        updateGuideButtonStates();
                    });
            guideButtons.put(guide, button);
            featureList.addChild(button);
        }

        updateGuideButtonStates();
        if (selectedGuide != null) showGuide(selectedGuide);


        content.addChildren(featureList, guideContent);

        main.addChildren(
                header,
                ButtonElement.create(contentWidth, 1).background(0xAA666666),
                content);
        screen.addChild(main);
        addDrawableChild(screen);
    }

    private ListElement createScrollList(int listWidth, int listHeight, int padding, int gap) {
        return ListElement.create(listWidth, listHeight)
                .maxExpansion(listHeight)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .padding(padding)
                .gap(gap);
    }

    private void showGuide(FeatureGuide guide) {
        guideContent.clearChildren();
        guideContent.addChild(
                TextElement.create(guide.getName())
                        .align(TextElement.TextAlignment.CENTER)
        );
        guide.populateGuide(guideContent);
    }

    private void updateGuideButtonStates() {
        guideButtons.forEach((guide, button) -> {
            boolean selected = guide == selectedGuide;
            button.background(selected ? 0x70404070 : 0x00000000);
            button.hoverBackground(selected ? 0x90404080 : 0x30303030);
        });
    }

}
