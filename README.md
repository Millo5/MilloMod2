<div align="center">
  <h1 align="center">MilloMod</h1>
  <a href="https://github.com/Millo5/MilloMod2/blob/master/LICENSE.txt">
    <img alt="License" src="https://img.shields.io/badge/license-All%20Rights%20Reserved-green.svg">
  </a>

  *MilloMod is a client-side utility mod for [DiamondFire](https://https://mcdiamondfire.com/)<br>
  aiming to improve development efficiency.*
</div>

# Download
You can download the latest build from the [releases page](https://github.com/Millo5/MilloMod2/releases) or from the [Modrinth Page](https://modrinth.com/mod/millo-mod-2).

# Features
|Feature|Description|Status|
|---|---|---|
|Editor|A text-based interface for codeblocks|Awaiting ModAPI|
|Quick Value Item|Shift-Right Click an empty slot in a code-chest to insert a value-item directly|Complete|
|Angels Grace|Saves you from falling while having a menu open|Complete|
|Lagslayer HUD|HUD overlay of CPU usage|Complete|
|Container Search|Quickly search through menus|Complete|
|Socket Serve|A local socket server for tool integration, More info below|Complete|
|Toggle Sprint Display|HUD display if sprint is enabled|Complete|
|Notifications|Filtered chat messages appear in a temporary notification tray|Complete|
|Skin Preview|Hovering over a playerhead displays the skin adjacent to the menu|Complete|
|Time Tracker|Tracks how much time is spent in different modes and plots|Unviewable|
|Sound Preview|Instantly play all sounds in a menu at once|Complete|
|Particle Color Shorthand|Type `#abcdef` or `f#abcdef` for particle colors|Complete|
|Waypoints|Set plot specific waypoints, teleport to them with a keybind|Complete|
|Argument Display|Preview what arguments are expected in codeblock chests|Complete|
|Dev Movement|No Clip through blocks, accelerated aerial movement, thanks [CodeClient](https://github.com/DFOnline/CodeClient/blob/main/src/main/java/dev/dfonline/codeclient/dev/NoClip.java)|Complete|

# Keybinds
|Keybind|Description|Status|
|---|---|---|
|Spectator Toggle|Toggle between spectator and creative mode|Complete|
|Show Item Tags|Hold to show custom item tags in the item lore|Complete|
|Pick Chest Value|Press to obtain the first item in the chest looked at|Complete|
|Command Wheel|Hold to open a radial to quickly execute commands|Unfinished In-Game Config|

# Commands
|Command|Description|Status|
|---|---|---|
|/milloactiondump|Read the entire actiondump|Complete|
|/colors \| /col|Open a color wheel, save color palettes to plots|Complete|
|/settings|View the mod's settings|Complete|
|/waypoint \| /wp|Open a menu to manage waypoints|Complete|

# Socket Serve
`SocketServe` provides a local socket server accessable on port `31321` (`ws://localhost:31321`).
Received JSON objects require three keys:
 - "type": "item" | "template"
 - "source": Origin of the request
 - "data": ItemNBT or Template Data

```json
{
	"type": "item",
	"source": "Whomever Sent Item",
	"data": "minecraft:stone[custom_name=\"Hello\"]"
}
```




