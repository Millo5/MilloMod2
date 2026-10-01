package millo.millomod2.client.util;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Debug;
import millo.millomod2.client.features.impl.Notifications.Notifications;
import millo.millomod2.client.util.logging.MilloLog;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PlayerUtil {

    /***
     * Make the player send a command
     * @param command the command to send (with or without the leading /)
     */
    public static void sendCommand(String command) {
        if (MilloMod.net() == null) return;
        if (command.startsWith("/")) command = command.substring(1);

        if (Debug.logCommands()) {
            Notifications.notify(Component.literal(command));
        }

        MilloMod.net().sendCommand(command);
    }

    /***
     * Make the player send a message
     * @param message the message to send
     */
    public static void sendMessage(String message) {
        if (MilloMod.net() == null) return;

        MilloMod.net().sendChat(message);
    }


    public static void setInventorySlot(int slot, ItemStack item) {
        MilloMod.net().send(new ServerboundSetCreativeModeSlotPacket(slot, ItemStack.EMPTY));
        MilloMod.net().send(new ServerboundSetCreativeModeSlotPacket(slot, item));
        MilloMod.player().getInventory().setItem(slot - 36, item);
    }

    public static void sendOffhandItem(ItemStack itemStack) {
        MilloMod.net().send(new ServerboundSetCreativeModeSlotPacket(45, itemStack));
        MilloMod.player().getInventory().setItem(45, itemStack);
    }

    public static void sendHandItem(ItemStack item) {
        MilloMod.net().send(new ServerboundSetCreativeModeSlotPacket(MilloMod.player().getInventory().getSelectedSlot() + 36, item));
        MilloMod.player().getInventory().setItem(MilloMod.player().getInventory().getSelectedSlot(), ItemStack.EMPTY);
    }


    public static void giveItem(ItemStack item) {
        Minecraft mc = MilloMod.MC;
        if (MilloMod.player() == null || MilloMod.player().getInventory() == null) return;
        NonNullList<ItemStack> inv = MilloMod.player().getInventory().getNonEquipmentItems();

        if (!mc.player.isCreative()) return;
        if (mc.gameMode == null) return;

        for (int index = 0; index < inv.size(); index++) {
            ItemStack i = inv.get(index);
            ItemStack compareItem = i.copy();
            compareItem.setCount(item.getCount());
            if (item == compareItem) {
                while (i.getCount() < i.getMaxStackSize() && item.getCount() > 0) {
                    i.setCount(i.getCount() + 1);
                    item.setCount(item.getCount() - 1);
                }
            } else {
                if (i.getItem() == Items.AIR) {
                    if (index < 9)
                        mc.gameMode.handleCreativeModeItemAdd(item, index + 36);
                    inv.set(index, item);
                    return;
                }
            }
        }

        int slot = mc.player.getInventory().getFreeSlot();

        if (slot == -1) {
            MilloLog.logInGame(Component.literal("No inventory room!").setStyle(Styles.SCARY.getStyle()));
            return;
        }

        mc.player.getInventory().setItem(slot, item);
        mc.gameMode.handleCreativeModeItemAdd(item, slot);
    }

    public static void sendSneak(boolean sneaking) {
        Input playerInput = MilloMod.player().input.keyPresses;
        MilloMod.net().send(new ServerboundPlayerInputPacket(
                new Input(
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
