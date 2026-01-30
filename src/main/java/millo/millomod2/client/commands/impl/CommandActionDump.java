package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.ActionDumpReader;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class CommandActionDump extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        cd.register(Arg.literal("milloactiondump")
                .executes(ctx -> {
                    FeatureHandler.get(ActionDumpReader.class).read();
                    return 1;
                })
        );
    }

    @Override
    public String getId() {
        return "milloactiondump";
    }
}
