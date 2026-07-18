package millo.millomod2.client.features.guides;

public final class GuideData {

    private static final String COMMANDS = "Commands";
    private static final String DEVELOPMENT = "Development";
    private static final String MOVEMENT = "Movement";
    private static final String INTERFACE = "Interface";
    private static final String HUD = "HUD and notifications";
    private static final String INTEGRATION = "Integration";

    private GuideData() {}

    public static void register() {
        FeatureGuide.clearGuides();
        registerDevelopmentGuides();
        registerMovementGuides();
        registerInterfaceGuides();
        registerHudGuides();
        registerIntegrationGuides();
        registerCommandGuides();
    }

    private static FeatureGuide guide(String name, String category) {
        FeatureGuide guide = new FeatureGuide().setName(name).setCategory(category);
        FeatureGuide.registerGuide(guide);
        return guide;
    }

    private static void registerDevelopmentGuides() {
        guide("Editor", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Opening the editor")
                        .addParagraph("Press the keybind to open the text editor. In Dev mode, look at a linestarter to open that method directly.")
                        .addKeybind("editor", "key", "Open Editor")
                        .addWarning("Requires Action Dump data. Run /milloactiondump on Node Beta first."))
                .addSection(s -> s
                        .addHeader("Finding code")
                        .addText(t -> t
                                .addSnippet("Ctrl+F")
                                .addText(" searches the open code. ")
                                .addSnippet("Ctrl+Shift+F")
                                .addText(" or pressing Shift twice searches the hierarchy.")))
                .addSection(s -> s
                        .addHeader("Exporting a plot")
                        .addText(t -> t
                                .addSnippet("Ctrl+P")
                                .addText(" exports the active plot to a file.")))
                .addSection(s -> s
                        .addHeader("Teleporting to a line")
                        .addParagraph("Click the line number to teleport to that code-block."))
                .addSection(s -> s
                        .addHeader("Obtaining value items")
                        .addParagraph("Clicking (most) value items will give you a copy in your inventory."))
                .addSection(s -> s
                        .addHeader("Fetch All")
                        .addText(t -> t
                                .addText("The ")
                                .addSnippet("Fetch All")
                                .addText(" button in the top-right corner fetches all templates in the plot."))
                        .addWarning("This currently only works for admins. When ModAPI releases this will be available to everyone."))
                .addSection(s -> s
                        .addHeader("Previewing sounds")
                        .addParagraph("Click sound arguments, or a sound action, to preview the sound(s)."))
                .addSection(s -> s
                        .addHeader("Method grouping")
                        .addParagraph("The folder-regex setting decides how method names are grouped in the hierarchy."))
                .addSection(s -> s
                        .addHeader("Work in progress")
                        .addWarning("Currently still work in progress, no editing is supported yet. Only viewing is available."));

        guide("Quick Value Item", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Insert a value")
                        .addParagraph("In Dev mode, Shift-right-click an empty code-chest slot. Pick number, text, or variable; type its value; then press Enter to insert it.")
                        .addWarning("Press Escape to cancel the input instead."));

        guide("Value Item Editor", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Open the editor")
                        .addParagraph("In Dev mode, Shift-right-click a supported value item in a code chest."))
                .addSection(s -> s
                        .addHeader("Save or cancel")
                        .addParagraph("Press Enter or click outside the editor to save. Press Escape to discard changes.")
                        .addParagraph("Number, text, component, sound, and variable values are supported."));

        guide("Argument Display", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Read expected arguments")
                        .addParagraph("Open a code block's reference chest. Empty argument slots show their expected type; hover a slot for its full description.")
                        .addParagraph("Settings can independently show the type icons and descriptions.")
                        .addWarning("Requires the Reference Book to be in your inventory."));

        guide("Pick Chest Value", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Copy the first chest item")
                        .addParagraph("In Dev mode, look at a chest and press the keybind. The first non-empty item is copied into your inventory.")
                        .addKeybind("pick_chest_value", "key", "Pick Chest Value"));

        guide("Particle Color Shorthand", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Set the primary particle colour")
                        .addParagraph("Hold a particle value item, then send this six-digit hexadecimal colour in chat.")
                        .addSnippet("#ff8800"))
                .addSection(s -> s
                        .addHeader("Set the fade colour")
                        .addParagraph("Prefix the same colour format with f to set the fade colour.")
                        .addSnippet("f#5bcefa")
                        .addParagraph("Angle brackets around either colour are accepted."));

        guide("Sound Preview", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Play sounds from a container")
                        .addText(t -> t
                                .addText("Open a supported chest-style container and click the ")
                                .addSnippet("P")
                                .addText(" button added beside the menu. Every sound value item in that container is played."))
                        .addWarning("Requires Action Dump data. Run /milloactiondump on Node Beta first."))
                .addSection(s -> s
                        .addHeader("Delay between sounds")
                        .addParagraph("The first number value item in the container supplies the delay. Hold Shift while clicking P to ignore that delay and play sounds together."));

        guide("Blueprint Loader", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Find blueprint files")
                        .addText(t -> t
                                .addText("Put ")
                                .addSnippet(".bp")
                                .addText(" files in the ")
                                .addSnippet("millomod2/blueprints")
                                .addText(" folder inside your Minecraft run directory.")))
                .addSection(s -> s
                        .addHeader("Load a blueprint")
                        .addCommand("blueprint")
                        .addParagraph("Choose a file in the menu to convert it into a DiamondFire template."))
                .addSection(s -> s
                        .addHeader("Create an empty template")
                        .addText(t -> t
                                .addText("Use ")
                                .addSnippet("Get Template")
                                .addText(" in the Blueprint Loader when you need a blank template item.")));

        guide("Time Tracker", DEVELOPMENT)
                .addSection(s -> s
                        .addHeader("Record time automatically")
                        .addParagraph("Enable the feature in Settings. It records time by plot and DiamondFire mode as you move between them."))
                .addSection(s -> s
                        .addHeader("Saved data")
                        .addText(t -> t
                                .addText("Data is saved periodically and at client shutdown to ")
                                .addSnippet("millomod2/time_tracker.json")
                                .addText("."))
                        .addWarning("There is no in-game viewer yet."));
    }

    private static void registerMovementGuides() {
        guide("Waypoints", MOVEMENT)
                .addSection(s -> s
                        .addHeader("Open the manager")
                        .addCommand("waypoint", "wp")
                        .addParagraph("Manage saved waypoints for the current plot."))
                .addSection(s -> s
                        .addHeader("Create a waypoint")
                        .addCommand("waypoint new <name>")
                        .addParagraph("Creates a waypoint at your current position. Without <name>, it opens the naming menu."))
                .addSection(s -> s
                        .addHeader("Teleport to a waypoint")
                        .addCommand("waypoint tp <waypoint>")
                        .addParagraph("<waypoint> is the label of a saved waypoint on the current plot."))
                .addSection(s -> s
                        .addHeader("Delete a waypoint")
                        .addCommand("waypoint delete <waypoint>")
                        .addParagraph("Removes the saved waypoint with that label."))
                .addSection(s -> s
                        .addHeader("Return to Dev Exit")
                        .addCommand("wpe")
                        .addText(t -> t
                                .addText("Teleports to the automatic Dev Exit waypoint. If none exists, it runs ")
                                .addSnippet("/dev")
                                .addText(".")))
                .addSection(s -> s
                        .addHeader("Teleport with the keybind")
                        .addParagraph("In Dev or Build mode, look at a visible waypoint and press the keybind to teleport. Sneak while pressing it to open the manager.")
                        .addKeybind("waypoints", "key", "TP to Waypoint"))
                .addSection(s -> s
                        .addHeader("Automatic waypoints")
                        .addParagraph("Settings can show labels in world and automatically create Back and Dev Exit waypoints after teleports or leaving Dev mode."));

        guide("Flight Speed Toggle", MOVEMENT)
                .addSection(s -> s
                        .addHeader("Switch flight speeds")
                        .addParagraph("While flight is allowed, press the keybind to alternate between normal speed and the fast speed configured in Settings.")
                        .addKeybind("flight_speed_toggle", "key", "Toggle Flight Speed"));

        guide("Spectator Toggle", MOVEMENT)
                .addSection(s -> s
                        .addHeader("Toggle game mode")
                        .addParagraph("Press the keybind to switch between Creative and Spectator.")
                        .addKeybind("spectator_toggle", "key", "Spectator Toggle"))
                .addSection(s -> s
                        .addHeader("Hold Toggle")
                        .addParagraph("Enable Hold Toggle in Settings to enter Spectator while holding the key and return to Creative on release."));

        guide("Angel's Grace", MOVEMENT)
                .addSection(s -> s
                        .addHeader("A guardian angel for sinister accidents")
                        .addParagraph("Opened chat or a container while plummeting? Angel's Grace notices the fall and gently gives you flight before you meet the floor at an unholy speed.")
                        .addWarning("Your angel only works in Dev mode when you are allowed to fly."));

        guide("Dev Movement", MOVEMENT)
                .addSection(s -> s
                        .addHeader("Move through the codespace")
                        .addParagraph("Enable it in Settings to change how you move around the codespace."))
                .addSection(s -> s
                        .addHeader("Noclip and vertical movement")
                        .addParagraph("Noclip lets you pass through blocks. Down Angle sets how far down to look before descending through a floor; Up Angle does the same for jumping upward. Enable Down Sneak to require holding sneak before looking down can drop you through a floor."))
                .addSection(s -> s
                        .addHeader("Acceleration")
                        .addParagraph("Acceleration increases your movement speed while airborne and not flying. Acceleration Amount controls its strength, from 1 to 10."))
                .addSection(s -> s
                        .addHeader("Ultrakill mode")
                        .addParagraph("Replaces normal movement with ULTRAKILL."));
    }

    private static void registerInterfaceGuides() {
        guide("Container Search", INTERFACE)
                .addSection(s -> s
                        .addHeader("Search chest menus")
                        .addText(t -> t
                                .addText("Type in the search field, or press ")
                                .addSnippet("Ctrl+F")
                                .addText(" while a chest-style container is open. Non-matching items are dimmed.")))
                .addSection(s -> s
                        .addHeader("Matching and Enter")
                        .addParagraph("Every typed word must be in an item's name. Settings can keep the field visible and make Enter pick up the first match."));

        guide("Skin Preview", INTERFACE)
                .addSection(s -> s
                        .addHeader("Preview a player head")
                        .addParagraph("Hover over a player head in a container to render its skin beside the menu."));

        guide("Show Item Tags", INTERFACE)
                .addSection(s -> s
                        .addHeader("Reveal item metadata")
                        .addParagraph("Hold the keybind while viewing an item tooltip to reveal its custom item tags.")
                        .addKeybind("show_item_tags", "key", "Show Item Tags"));

        guide("Slot Index", INTERFACE)
                .addSection(s -> s
                        .addHeader("Display slot numbers")
                        .addParagraph("Hold the keybind while a container is open to show each slot's index.")
                        .addKeybind("slot_index", "key", "Display Slot Index"));

        guide("Command Wheel", INTERFACE)
                .addSection(s -> s
                        .addHeader("Choose and execute a command")
                        .addParagraph("Hold the keybind, point at an entry, then release the key to execute the selected command.")
                        .addKeybind("command_wheel", "key", "Command Wheel"))
                .addSection(s -> s
                        .addHeader("Change pages")
                        .addParagraph("Scroll while the wheel is open to switch pages.")
                        .addWarning("Settings support is not available yet. For now, edit each page's commands and labels in the JSON file."));
    }

    private static void registerHudGuides() {
        guide("Lagslayer HUD", HUD)
                .addSection(s -> s
                        .addHeader("Show plot CPU usage")
                        .addParagraph("Enable the feature to display your plot's CPU usage as a radial meter, bar, or text.")
                        .addWarning("Requires plot CPU usage to be shown."))
                .addSection(s -> s
                        .addHeader("Appearance")
                        .addParagraph("Settings control the display mode, percentage text, smoothing, and HUD position."));

        guide("Toggle Sprint Display", HUD)
                .addSection(s -> s
                        .addHeader("Show sprint state")
                        .addParagraph("Enable this optional HUD label to display custom text while sprint input is active. Change its text and HUD position in Settings."));

        guide("Notifications", HUD)
                .addSection(s -> s
                        .addHeader("Choose message categories")
                        .addParagraph("Enable only the HUD notifications you want: friend joins, votes, player joins, plot notices, or fly-speed changes. Selected chat messages can be filtered out of chat and shown as notifications instead."))
                .addSection(s -> s
                        .addHeader("Custom match")
                        .addParagraph("Custom is a regular expression using find matching, so it can match part of a message. Matching messages are mirrored to the notification tray and suppressed from chat."))
                .addSection(s -> s
                        .addHeader("Tray appearance")
                        .addParagraph("Choose whether notifications grow up or down, then position the tray in Settings."));
    }

    private static void registerIntegrationGuides() {
        guide("Socket Serve", INTEGRATION)
                .addSection(s -> s
                        .addHeader("Start the local server")
                        .addParagraph("Enable Socket Serve in Settings. A notification confirms that the WebSocket server has started; disable it to stop the server."))
                .addSection(s -> s
                        .addHeader("Connect your tool")
                        .addParagraph("Connect to this endpoint from a program running on the same computer.")
                        .addSnippet("ws://localhost:31321")
                        .addParagraph("It listens on localhost only, so no other player or computer can connect through your network."))
                .addSection(s -> s
                        .addHeader("Request field: type")
                        .addText(t -> t
                                .addSnippet("type")
                                .addText(" is required and must be exactly ")
                                .addSnippet("item")
                                .addText(" or ")
                                .addSnippet("template")
                                .addText(".")))
                .addSection(s -> s
                        .addHeader("Request field: source")
                        .addText(t -> t
                                .addSnippet("source")
                                .addText(" is the required, non-empty name of the sending tool (up to 256 characters). It is shown in received-message feedback.")))
                .addSection(s -> s
                        .addHeader("Request field: data")
                        .addText(t -> t
                                .addSnippet("data")
                                .addText(" is required. For items it is an item string such as ")
                                .addSnippet("minecraft:stone")
                                .addText(" or serialized item NBT; for templates it is encoded template data.")))
                .addSection(s -> s
                        .addHeader("Send an item")
                        .addParagraph("Use type item and provide an item string or serialized item NBT in data.")
                        .addSnippet("""
                                {
                                  "type": "item",
                                  "source": "My Tool",
                                  "data": "minecraft:stone"
                                }
                                """))
                .addSection(s -> s
                        .addHeader("Send a template")
                        .addParagraph("Use type template and provide the template payload in data. The source name tells the player where it came from.")
                        .addSnippet("""
                                {
                                  "type": "template",
                                  "source": "My Tool",
                                  "data": "<template data>"
                                }
                                """))
                .addSection(s -> s
                        .addHeader("Troubleshooting")
                        .addParagraph("If startup fails, make sure port 31321 is free, then disable and re-enable Socket Serve to retry. Check the client log for the reported error."));
    }

    private static void registerCommandGuides() {
        guide("Guide Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Open the guide")
                        .addCommand("guide", "millohelp", "millohelpme")
                        .addParagraph("Opens this guide."));

        guide("Settings Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Open settings")
                        .addCommand("settings")
                        .addParagraph("Change feature settings and positions."));

        guide("Colors Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Open the colour menu")
                        .addCommand("colors", "col")
                        .addParagraph("A colour wheel to freely pick colors."))
                .addSection(s -> s
                        .addHeader("Saving")
                        .addText(t -> t
                                .addText("Save colours per plot. Right click to remove a saved color.")));

        guide("DfGive Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Give an item")
                        .addCommand("dfgive <item>")
                        .addParagraph("Creates the supplied Minecraft item in your inventory."))
                .addSection(s -> s
                        .addHeader("Give from the clipboard")
                        .addCommand("dfgive clipboard")
                        .addParagraph("Creates an item from an item-NBT string in your system clipboard."));

        guide("Blueprint Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Open Blueprint Loader")
                        .addCommand("blueprint")
                        .addParagraph("Lists .bp files in the blueprints folder."));

        guide("Action Dump Command", COMMANDS)
                .addSection(s -> s
                        .addHeader("Download action data")
                        .addCommand("milloactiondump")
                        .addParagraph("Reads DiamondFire's action dump and saves it for features that use it.")
                        .addWarning("This command can only be run on Node Beta."));
    }
}
