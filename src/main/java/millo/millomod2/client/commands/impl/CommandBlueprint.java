package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.menus.BlueprintsMenu;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class CommandBlueprint extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        LiteralArgumentBuilder<FabricClientCommandSource> cmdb = Arg.literal("blueprint")
                .executes(ctx -> {
                    new BlueprintsMenu(null).open();
                    return 1;
                });
        cd.register(cmdb);
    }

    @Override
    public String getId() {
        return "blueprint";
    }
}
