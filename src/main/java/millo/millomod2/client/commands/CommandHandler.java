package millo.millomod2.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.commands.impl.CommandActionDump;
import millo.millomod2.client.commands.impl.CommandColors;
import millo.millomod2.client.commands.impl.CommandSettings;
import millo.millomod2.client.commands.impl.CommandWaypoint;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    public static CommandHandler INSTANCE;

    private final List<Command> commands = new ArrayList<>();

    public List<Command> getCommands() {
        return commands;
    }


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess context) {
        INSTANCE = new CommandHandler(dispatcher, context);
    }

    private CommandHandler(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess context) {
        register(dispatcher, context,
                new CommandSettings(),
                new CommandActionDump(),
                new CommandColors(),
                new CommandWaypoint()
        );
    }

    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess context, Command cmd) {
        cmd.register(MilloMod.MC, dispatcher, context);
        commands.add(cmd);
    }
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess context, Command... cmds) {
        for (Command cmd : cmds) {
            register(dispatcher, context, cmd);
        }
    }
}
