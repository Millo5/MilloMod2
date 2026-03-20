package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.ShowItemTags;
import millo.millomod2.client.util.KeyUtil;
import millo.millomod2.client.util.ItemUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ItemStack.class)
public abstract class MItemStack {

    @Shadow @Nullable public abstract ComponentMap getComponents();

    @Shadow public abstract ItemStack copy();

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        if (player == null) return;
        ShowItemTags feature = FeatureHandler.get(ShowItemTags.class);

        if (!KeyUtil.isKeyDown(feature.getKeybind())) return;

        Map<String, Object> tags = ItemUtil.getItemTags(copy());
        if (tags == null) return;

        Set<String> keys = tags.keySet();
        if (keys.isEmpty()) return;

        List<String> sortedKeys = keys.stream().sorted().toList();

        List<Text> t = cir.getReturnValue();
        if (!(t instanceof ArrayList<Text>)) return;

        t.add(Text.of(""));
        t.add(Text.literal("Tags:").setStyle(Styles.COMMENT.getStyle()));
        sortedKeys.forEach(key -> {
                    String value = tags.get(key).toString();
                    value = value.length() > 50 ? value.substring(0, 50)+"..." : value;
                    t.add(Text.literal(key.replaceFirst("^.+:", "")).setStyle(Styles.ACTION.getStyle())
                            .append(Text.literal(" = ").setStyle(Styles.DEFAULT.getStyle()))
                            .append(Text.literal(value).setStyle(Styles.NAME.getStyle())));
                }
        );

        cir.setReturnValue(t);
    }

}
