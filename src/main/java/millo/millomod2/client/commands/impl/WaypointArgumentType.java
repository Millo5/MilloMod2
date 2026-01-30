package millo.millomod2.client.commands.impl;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Waypoints.Waypoint;
import millo.millomod2.client.features.impl.Waypoints.Waypoints;

import java.util.concurrent.CompletableFuture;

public class WaypointArgumentType implements ArgumentType<Waypoint> {

    @Override
    public Waypoint parse(StringReader reader) throws CommandSyntaxException {
        final String waypointName = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        Waypoints feat = FeatureHandler.get(Waypoints.class);
        Waypoint wp = feat.getWaypointByLabel(waypointName);

        if (wp == null) {
            throw new SimpleCommandExceptionType(new LiteralMessage("Waypoint '" + waypointName + "' not found")).create();
        }

        return wp;
    }


    public static Waypoint getWaypoint(CommandContext<?> context, String name) {
        return context.getArgument(name, Waypoint.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Waypoints feat = FeatureHandler.get(Waypoints.class);

        for (String waypointLabel : feat.getWaypointLabels()) {
            if (waypointLabel.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(waypointLabel);
            }
        }

        return builder.buildFuture();
    }
}
