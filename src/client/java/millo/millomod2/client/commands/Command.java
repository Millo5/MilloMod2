package millo.millomod2.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public abstract class Command {

    public abstract void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess context);

    public abstract String getId();

}
