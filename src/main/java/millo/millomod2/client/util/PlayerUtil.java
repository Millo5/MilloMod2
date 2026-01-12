package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Debug;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.collection.DefaultedList;

public class PlayerUtil {

    /***
     * Make the player send a command
     * @param command the command to send (with or without the leading /)
     */
    public static void sendCommand(String command) {
        if (MilloMod.net() == null) return;
        if (command.startsWith("/")) command = command.substring(1);

        if (Debug.logCommands()) {
            Notifications.notify(Text.literal(command));
        }

        MilloMod.net().sendChatCommand(command);
    }

    /***
     * Make the player send a message
     * @param message the message to send
     */
    public static void sendMessage(String message) {
        if (MilloMod.net() == null) return;

        MilloMod.net().sendChatMessage(message);
    }


    public static void setInventorySlot(int slot, ItemStack item) {
        MilloMod.net().sendPacket(new CreativeInventoryActionC2SPacket(slot, ItemStack.EMPTY));
        MilloMod.net().sendPacket(new CreativeInventoryActionC2SPacket(slot, item));
        MilloMod.player().getInventory().setStack(slot - 36, item);
    }

    public static void sendOffhandItem(ItemStack itemStack) {
        MilloMod.net().sendPacket(new CreativeInventoryActionC2SPacket(45, itemStack));
        MilloMod.player().getInventory().setStack(45, itemStack);
    }

    public static void sendHandItem(ItemStack item) {
        MilloMod.net().sendPacket(new CreativeInventoryActionC2SPacket(MilloMod.player().getInventory().getSelectedSlot() + 36, item));
        MilloMod.player().getInventory().setStack(MilloMod.player().getInventory().getSelectedSlot(), ItemStack.EMPTY);
    }


    public static void giveItem(ItemStack item) {
        MinecraftClient mc = MilloMod.MC;
        if (MilloMod.player() == null || MilloMod.player().getInventory() == null) return;
        DefaultedList<ItemStack> inv = MilloMod.player().getInventory().getMainStacks();

        if (!mc.player.isCreative()) return;
        if (mc.interactionManager == null) return;

        for (int index = 0; index < inv.size(); index++) {
            ItemStack i = inv.get(index);
            ItemStack compareItem = i.copy();
            compareItem.setCount(item.getCount());
            if (item == compareItem) {
                while (i.getCount() < i.getMaxCount() && item.getCount() > 0) {
                    i.setCount(i.getCount() + 1);
                    item.setCount(item.getCount() - 1);
                }
            } else {
                if (i.getItem() == Items.AIR) {
                    if (index < 9)
                        mc.interactionManager.clickCreativeStack(item, index + 36);
                    inv.set(index, item);
                    return;
                }
            }
        }

        int slot = mc.player.getInventory().getEmptySlot();

        if (slot == -1) {
            mc.player.sendMessage(Text.literal("No inventory room!").setStyle(Styles.SCARY.getStyle()), false);
            return;
        }

        mc.player.getInventory().setStack(slot, item);
        mc.interactionManager.clickCreativeStack(item, slot);
    }

    public static void sendSneak(boolean sneaking) {
        PlayerInput playerInput = MilloMod.player().input.playerInput;
        MilloMod.net().sendPacket(new PlayerInputC2SPacket(
                new PlayerInput(
                        playerInput.forward(),
                        playerInput.backward(),
                        playerInput.left(),
                        playerInput.right(),
                        playerInput.jump(),
                        sneaking,
                        playerInput.sprint()
                )));
    }
}
