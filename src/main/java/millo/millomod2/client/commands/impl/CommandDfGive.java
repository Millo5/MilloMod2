package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.PlayerUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.world.item.ItemStack;

public class CommandDfGive extends Command {

    @Override
    public void register(Minecraft instance, CommandDispatcher<FabricClientCommandSource> cd, CommandBuildContext context) {
        cd.register(Arg.literal("dfgive")
                .then(Arg.literal("clipboard")
                        .executes((ctx) -> {
                            String clipboard = Minecraft.getInstance().keyboardHandler.getClipboard();
                            ItemStack item = ItemUtil.fromNbt(clipboard);
                            PlayerUtil.giveItem(item);
                            return 1;
                        }))
                .then(Arg.argument("item", ItemArgument.item(context))
                        .executes(ctx -> {
                            ItemInput item = ctx.getArgument("item", ItemInput.class);
                            ItemStack itemStack = item.createItemStack(1);
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
