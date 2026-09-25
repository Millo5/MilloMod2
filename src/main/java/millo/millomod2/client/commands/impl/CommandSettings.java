package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.menus.ConfigMenu;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class CommandSettings extends Command {

    @Override
    public void register(Minecraft instance, CommandDispatcher<FabricClientCommandSource> cd, CommandBuildContext context) {
        cd.register(Arg.literal("settings")
                .executes(ctx -> {
                    ConfigMenu screen = new ConfigMenu(null);
                    instance.schedule(() -> instance.setScreen(screen));
                    return 1;
                })
        );
    }

    @Override
    public String getId() {
        return "settings";
    }
}
