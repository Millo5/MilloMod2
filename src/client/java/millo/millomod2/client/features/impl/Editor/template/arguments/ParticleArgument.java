package millo.millomod2.client.features.impl.Editor.template.arguments;

import com.google.gson.JsonObject;
import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ParticleArgument extends Argument<ParticleArgument> {

    private String particle;
    private HashMap<String, String> data;
    

    @Override
    protected Text getDisplayText() {

        ArrayList<Text> tooltip = new ArrayList<>();
        boolean newline = false;
        String[] keys = new String[] {"Amount", "Spread", "n", "Motion", "Motion Variation", "Material", "Roll", "Color", "Color Variation", "Size", "Size Variation"};
        for (String key : keys) {
            if (key.equals("n")) newline = true;
            if (!data.containsKey(key)) continue;

            if (newline) tooltip.add(Text.of("\n"));
            newline = false;

            String value = data.get(key);
            Style style = Styles.DEFAULT.getStyle();

            tooltip.add(Text.literal(key+": ").setStyle(Styles.UNSAVED.getStyle()));
            tooltip.add(Text.literal(value).setStyle(style));
            tooltip.add(Text.of("\n"));
        }
        tooltip.removeLast();
        //  Use when tooltips have been implemented

        return Text.literal(particle).setStyle(Styles.PARTICLE.getStyle());
    }

    @Override
    public ParticleArgument from(ArgumentItemData data) {
        this.particle = data.particle;

        JsonObject p = data.data;
        HashMap<String, String> pMap = p.keySet().stream().collect(
                Collectors.toMap(key -> key, key -> p.get(key).toString(), (a, b) -> b, HashMap::new));
        
        pMap.put("Amount", String.valueOf(data.cluster.amount));
        pMap.put("Spread", data.cluster.horizontal + " " + data.cluster.vertical);

        if (pMap.containsKey("x")) {
            String x = pMap.get("x");
            String y = pMap.get("y");
            String z = pMap.get("z");

            pMap.remove("x");
            pMap.remove("y");
            pMap.remove("z");

            pMap.put("Motion", String.format("%s %s %s", x, y, z));
        }

        int color;
        if (pMap.containsKey("rgb")) {
            color = Integer.parseInt(pMap.get("rgb"));

            pMap.remove("rgb");

            pMap.put("Color", "#"+Integer.toHexString(color));
        }
        if (pMap.containsKey("motionVariation")) pMap.put("Motion Variation", pMap.get("motionVariation") + "%");
        if (pMap.containsKey("colorVariation")) pMap.put("Color Variation", pMap.get("colorVariation") + "%");
        if (pMap.containsKey("sizeVariation")) pMap.put("Size Variation", pMap.get("sizeVariation") + "%");
        if (pMap.containsKey("roll")) pMap.put("Roll", pMap.get("roll"));
        if (pMap.containsKey("size")) pMap.put("Size", pMap.get("size"));
        if (pMap.containsKey("material")) pMap.put("Material", pMap.get("material"));

        return this;
    }
}
