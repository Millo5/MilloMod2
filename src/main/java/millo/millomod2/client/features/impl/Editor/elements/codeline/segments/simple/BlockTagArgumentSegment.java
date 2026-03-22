package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.data.VariableScope;
import millo.millomod2.client.hypercube.model.arguments.BlockTagArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class BlockTagArgumentSegment extends SimpleSegment<BlockTagArgumentModel> {

    public BlockTagArgumentSegment(BlockTagArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(BlockTagArgumentModel model) {
        if (model.getVariable() != null) {
            VariableScope scope = model.getVariable().getScope();
            String cmd = "var " + model.getVariable().getName() + switch (scope) {
                case SAVED -> " -s";
                case LOCAL -> " -l";
                case LINE -> " -i";
                default -> "";
            };
            MutableText name = Text.literal(model.getVariable().getName()).setStyle(Styles.VAR.getStyle())
                    .append(Text.literal("°").setStyle(scope.getStyle()));

            MutableText text = Text.literal("{")
                    .append(name)
                    .append("}");
            return new SimpleArgumentBuilder(text)
                    .style(Styles.BLOCK_TAG)
                    .onClickCmd(cmd)
                    .tooltip(Text.empty().append(Text.literal(model.getTag() + "\n\n").setStyle(Styles.COMMENT.getStyle()))
                            .append(Text.literal("Default Value: " + model.getOption()).setStyle(Styles.UNSAVED.getStyle())))
                    .build();
        }
        return new SimpleArgumentBuilder(model.getOption())
                .style(Styles.BLOCK_TAG)
                .tooltip(Text.literal(model.getTag()).setStyle(Styles.COMMENT.getStyle()))
                .build();
    }

    @Override
    public Class<BlockTagArgumentModel> getModelClass() {
        return BlockTagArgumentModel.class;
    }

}
