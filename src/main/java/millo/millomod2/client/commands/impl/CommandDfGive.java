package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.util.PlayerUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;

public class CommandDfGive extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        cd.register(Arg.literal("dfgive")
                .then(Arg.argument("item", ItemStackArgumentType.itemStack(context))
                        .executes(ctx -> {
                            ItemStackArgument item = ctx.getArgument("item", ItemStackArgument.class);
                            ItemStack itemStack = item.createStack(1, false);
                            PlayerUtil.giveItem(itemStack);
                            return 1;
                        }))
        );
    }

    @Override
    public String getId() {
        return "colors";
    }
}
