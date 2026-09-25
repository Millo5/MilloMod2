package millo.millomod2.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

public abstract class Command {

    public abstract void register(Minecraft instance, CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext context);

    public abstract String getId();

}
