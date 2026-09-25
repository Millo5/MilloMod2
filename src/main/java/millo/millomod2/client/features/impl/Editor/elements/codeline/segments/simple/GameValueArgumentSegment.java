package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.GameValueArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.network.chat.Component;

public class GameValueArgumentSegment extends SimpleSegment<GameValueArgumentModel> {

    public GameValueArgumentSegment(GameValueArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(GameValueArgumentModel model) {
        String t = "["+ model.getTarget().charAt(0) +"]";
        if (t.equals("[D]")) t = "";

        return new SimpleArgumentBuilder(model.getType() + t)
                .style(Styles.GAME_VALUE)
                .tooltip(Component.literal(model.getTarget()).setStyle(Styles.COMMENT.getStyle()))
                .build();
    }

    @Override
    public Class<GameValueArgumentModel> getModelClass() {
        return GameValueArgumentModel.class;
    }

}
