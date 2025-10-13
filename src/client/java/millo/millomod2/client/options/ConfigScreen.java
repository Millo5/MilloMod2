package millo.millomod2.client.options;

import millo.millomod2.client.config.saving.ConfigSaving;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.util.MilloLog;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.*;

public class ConfigScreen extends WidgetContainerScreen {

    public ConfigScreen(Screen previousScreen) {
        super(previousScreen);

        WidgetListBox settingsList = new WidgetListBox()
                .useParentDimensions(true, true)
                .withDimensions(400, 100)
                .withPadding(10, 10, 5)
                .withAlignment(Widget.Alignment.CENTER);

        settingsList.addWidget(new TextDisplayWidget(Text.literal("MilloMod Settings"), 10, Widget.Alignment.CENTER));

        FeatureHandler.forEach(feature -> {
            FeatureConfig config = feature.getConfig();
            if (config.getValues().isEmpty()) return;
            WidgetListBox folder = new WidgetListFolder()
                    .withTitle(Text.translatable(feature.getId()))
                    .withDimensions(100, 100);

            feature.getConfig().forEach(configValue -> {
                WidgetListBox setting = new WidgetListBox(
                        new TextDisplayWidget(Text.translatable(configValue.getKey()), 10, Widget.Alignment.LEFT),
                        configValue.getConfigValue().createWidget()
                );
                folder.addWidget(setting);
//                folder.addWidget(new TextDisplayWidget(Text.literal(configValue.getKey()), 10, Widget.Alignment.LEFT));
            });

            folder.withDimensions(100, Math.min(200, 25 * (config.getValues().size() + 1)));

            settingsList.addWidget(folder);
        });

        addWidget(settingsList);
    }


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
