package millo.millomod2.client.features.guides;

public class GuideData {

    private final static String COMMANDS_CATEGORY = "Commands";

    public static void register() {
        DfGiveCommand();
    }

    private static void DfGiveCommand() {
        new FeatureGuide()
                .setName("Dfgive Command")
                .setCategory(COMMANDS_CATEGORY)
                .addSection(s -> s
                        .addHeader("Dfgive Command")
                )
                .addSection(s -> s
                        .addText(t -> t
                                .addSnippet("/dfgive <item>")
                                .addText(" gives an item like base mc /give.")
                        )
                )
                .addSection(s -> s
                        .addText(t -> t
                                .addSnippet("/dfgive clipboard")
                               .addText("gives the item-nbt from your clipboard.")
                        )
                        .addWarning("Test warning")
                );
    }

}
