package fr.neyuux.lgthierce;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class Anvil {

    public static void openAnvilInventory(final Player player, ItemStack it) {
   
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        FakeAnvil fakeAnvil = new FakeAnvil(entityPlayer);
        int containerId = entityPlayer.nextContainerCounter();
   
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("Repairing"), 0));
   
        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;
   
        Inventory inv = fakeAnvil.getBukkitView().getTopInventory();
        inv.setItem(0, it);
        player.setLevel(1);
   
        }
}

final class FakeAnvil extends ContainerAnvil {
	
	public FakeAnvil(EntityHuman entityHuman) {    
	    super(entityHuman.inventory, entityHuman.world, new BlockPosition(0,0,0), entityHuman);
	}
	
	
	@Override
	public boolean a(EntityHuman entityHuman) {
		return true;
	}
}