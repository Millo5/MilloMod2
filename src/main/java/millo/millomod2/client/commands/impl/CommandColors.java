package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.menus.ColorsMenu;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class CommandColors extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        cd.register(Arg.literal("colors")
                .executes(ctx -> {
                    ColorsMenu screen = new ColorsMenu(null);
                    instance.send(() -> instance.setScreen(screen));
                    return 1;
                })
        );
        cd.register(Arg.literal("col")
                .executes(ctx -> {
                    ColorsMenu screen = new ColorsMenu(null);
                    instance.send(() -> instance.setScreen(screen));
                    return 1;
                })
        );
    }

    @Override
    public String getId() {
        return "colors";
    }
}
