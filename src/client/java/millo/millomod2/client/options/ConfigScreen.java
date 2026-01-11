package millo.millomod2.client.options;

import millo.millomod2.client.config.saving.ConfigSaving;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.menu.Menu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class ConfigScreen extends Menu {

    public ConfigScreen(Screen previousScreen) {
        super(previousScreen);
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
