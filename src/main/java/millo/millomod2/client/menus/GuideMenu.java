package millo.millomod2.client.menus;

import millo.millomod2.client.features.guides.FeatureGuide;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GuideMenu extends Menu {

    private ListElement featureList;
    private ListElement guideContent;

    public GuideMenu(Screen parent) {
        super(parent);
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

        featureList = createScrollList(featureListWidth, contentHeight);
        guideContent = createScrollList(guideContentWidth, contentHeight);
        guideContent.addChild(
                TextElement.create("Select a feature to view its guide.")
                        .align(TextElement.TextAlignment.CENTER)
        );

        // populate feature list
        for (FeatureGuide guide : FeatureGuide.guides) {
            featureList.addChild(
                    ButtonElement.create(featureListWidth, 20)
                            .message(Text.literal(guide.getName()))
                            .onPress((b) -> {
                                guideContent.clearChildren();
                                guide.populateGuide(guideContent);
                            })
                            .background(0xAA666666)
            );
        }


        content.addChildren(featureList, guideContent);

        main.addChildren(
                header,
                ButtonElement.create(mainWidth, 1).background(0xAA666666),
                content);
        screen.addChild(main);
        addDrawableChild(screen);
    }

    private ListElement createScrollList(int listWidth, int listHeight) {
        return ListElement.create(listWidth, listHeight)
                .maxExpansion(listHeight)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .padding(6)
                .gap(3);
    }

}
