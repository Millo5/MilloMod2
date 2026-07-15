package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.menus.GuideMenu;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class CommandGuide extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        cd.register(Arg.literal("guide")
                .executes(ctx -> {
                    new GuideMenu(null).open();
                    return 1;
                }));
        cd.register(Arg.literal("millohelp")
                .executes(ctx -> {
                    new GuideMenu(null).open();
                    return 1;
                }));
        cd.register(Arg.literal("millohelpmeplease")
                .executes(ctx -> {
                    new GuideMenu(null).open();
                    return 1;
                }));
    }

    @Override
    public String getId() {
        return "guide_command";
    }
}
