package millo.millomod2.client.menus;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.config.saving.ConfigSaving;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.FolderElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;

public class ConfigMenu extends Menu {

    public ConfigMenu(Screen previousScreen) {
        super(previousScreen);
    }

    @Override
    protected void init() {
//        FlexElement container = FlexElement.create(width, height)
//                .direction(ElementDirection.COLUMN)
//                .mainAlign(MainAxisAlignment.START)
//                .crossAlign(CrossAxisAlignment.CENTER)
//                .padding(40)
//                .gap(10);
        ListElement list = ListElement.create(width/4 * 3, height)
                .position(width/8, 0)
                .maxExpansion(height)
                .crossAlign(CrossAxisAlignment.CENTER)
                .padding(40)
                .gap(10);

        list.addChild(
                TextElement.create("MilloMod Settings").align(TextElement.TextAlignment.CENTER)
        );

        FeatureHandler.forEach(feature -> {
            FeatureConfig config = feature.getConfig();
            if (config.getValues().isEmpty()) return;

            FolderElement folder = FolderElement.create(width / 2, 800, MilloMod.translatable("feature", feature.getId()));
            folder.getContent().crossAlign(CrossAxisAlignment.CENTER);

            config.forEach(configValue -> {
                FlexElement setting = FlexElement.create(width / 2 - 10, 20)
                        .direction(ElementDirection.ROW)
                        .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                        .crossAlign(CrossAxisAlignment.CENTER)
                        .gap(5);

                TextElement configText = TextElement.create(MilloMod.translatable("feature", "config", configValue.getKey()));

                ClickableWidget widget = configValue.getConfigValue().createWidget();
                setting.addChildren(
                        configText,
                        widget
                );
//                setting.grow(configText, 1);
//                setting.grow(widget, 2);
                folder.addChild(setting);
            });

            list.addChild(folder);
        });

        addDrawableChild(list);
    }

    //    @Override
//    protected void init() {
//        ListWidget list = ListWidget.create()
//                .withAlignment(WidgetPos.Alignment.CENTER)
//                .withDimensions(width, height, true);
//
//        list.addWidget(
//                TextWidget.create()
//                        .withText(Text.literal("MilloMod Settings"))
//                        .withAlignment(TextWidget.TextAlignment.CENTER)
//        );
//
//        FeatureHandler.forEach(feature -> {
//            FeatureConfig config = feature.getConfig();
//            if (config.getValues().isEmpty()) return;
//
//            FolderWidget folder = new FolderWidget()
//                    .withTitle(MilloMod.translatable("feature", feature.getId()))
//                    .withDimensions(800, 200, true);
//
//            feature.getConfig().forEach(configValue -> {
//                ListWidget setting = ListWidget.create().addWidgets(
//                        TextWidget.create().withText(MilloMod.translatable("feature", "config", configValue.getKey())),
//                        configValue.getConfigValue().createWidget()
//                )
//                                .withDimensions(600, 100, true);
//                folder.addWidget(setting);
//            });
//
////            folder.withDimensions(100, Math.min(200, 25 * (config.getValues().size() + 1)));
//
//            list.addWidget(folder);
//        });
//
//
//        addWidget(list);
//    }


    //    @Override
//    public void init() {
//        container.clearWidgets();
//
//        var settingsList = new WidgetList()
//                .withAlignment(WidgetPos.Alignment.CENTER)
//                .withDimensions(width, height);
//
//
//        settingsList.addWidget(new TextDisplayWidget(Text.literal("MilloMod Settings"))
//                .withTextAlignment(TextDisplayWidget.TextAlignment.CENTER)
//        );
//
//        FeatureHandler.forEach(feature -> {
//            FeatureConfig config = feature.getConfig();
//            if (config.getValues().isEmpty()) return;
//
//            WidgetFolder folder = new WidgetFolder()
//                    .withTitle(MilloMod.translatable("feature", feature.getId()))
//                    .withDimensions(0, 0);
//
//            feature.getConfig().forEach(configValue -> {
//                WidgetList setting = new WidgetList(
//                        new TextDisplayWidget(MilloMod.translatable("feature", "config", configValue.getKey())),
//                        configValue.getConfigValue().createWidget()
//                );
//                folder.addWidget(setting);
//            });
//
////            folder.withDimensions(100, Math.min(200, 25 * (config.getValues().size() + 1)));
//
//            settingsList.addWidget(folder);
//        });
//
//
//        container.addWidget(settingsList);
//
//    }


    @Override
    protected void applyBlur(DrawContext context) {}


    @Override
    public void close() {
        try {
            ConfigSaving.getInstance().save();
        } catch (Exception e) {
            MilloLog.error("Failed to save config: " + e.getMessage());
        }
        super.close();
    }
}
