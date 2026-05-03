package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import millo.millomod2.client.commands.Arg;
import millo.millomod2.client.commands.Command;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Waypoints.Waypoint;
import millo.millomod2.client.features.impl.Waypoints.Waypoints;
import millo.millomod2.client.menus.AddWaypointMenu;
import millo.millomod2.client.menus.WaypointMenu;
import millo.millomod2.client.util.PlayerUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;

public class CommandWaypoint extends Command {

    @Override
    public void register(MinecraftClient instance, CommandDispatcher<FabricClientCommandSource> cd, CommandRegistryAccess context) {
        LiteralArgumentBuilder<FabricClientCommandSource> cmdb = Arg.literal("waypoint")
                .executes(ctx -> {
                    new WaypointMenu(null).open();
                    return 1;
                })
                .then(Arg.literal("new")
                        .executes(ctx -> {
                            if (instance.player == null) return 1;
                            new AddWaypointMenu(null, instance.player.getEntityPos()).open();
                            return 1;
                        })
                        .then(Arg.argument("name", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    if (instance.player == null) return 1;
                                    var menu = new AddWaypointMenu(null, instance.player.getEntityPos(), StringArgumentType.getString(ctx, "name"));
                                    menu.open();
                                    return 1;
                                })
                        )
                )
                .then(Arg.literal("tp")
                        .then(Arg.argument("waypoint", new WaypointArgumentType())
                                .executes(ctx -> {
                                    Waypoint wp = WaypointArgumentType.getWaypoint(ctx, "waypoint");
                                    wp.teleport();
                                    return 1;
                                })
                        )
                )
                .then(Arg.literal("delete")
                        .then(Arg.argument("waypoint", new WaypointArgumentType())
                                .executes(ctx -> {
                                    Waypoint wp = WaypointArgumentType.getWaypoint(ctx, "waypoint");
                                    FeatureHandler.get(Waypoints.class).removeWaypoint(wp);
                                    return 1;
                                })
                        )
                )
                ;

        var cmd = cd.register(cmdb);
        cd.register(Arg.literal("wp").executes(ctx -> {
            new WaypointMenu(null).open();
            return 1;
        }).redirect(cmd));

        cd.register(Arg.literal("wpe").executes(ctx -> {
            Waypoint wp = FeatureHandler.get(Waypoints.class).getWaypointByLabel("Dev Exit");
            if (wp != null) wp.teleport();
            else PlayerUtil.sendCommand("dev");
            return 1;
        }));
    }

    @Override
    public String getId() {
        return "waypoint";
    }
}
