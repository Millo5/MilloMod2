package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.menus.GuideMenu;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public class CommandGuide extends Command {

    @Override
    public void register(Minecraft instance, CommandDispatcher<FabricClientCommandSource> cd, CommandBuildContext context) {
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
        cd.register(Arg.literal("millohelpme")
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
