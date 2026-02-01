package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class ChoosePlayerItemStack extends CustomItemStack {

    private final PlayerLG playerLG;
    private final ChoosePlayerInv.ActionsGenerator generator;

    public ChoosePlayerItemStack(PlayerLG receiverLG, PlayerLG playerLG, ChoosePlayerInv.ActionsGenerator generator) {
        super(Material.SKULL_ITEM, 1, playerLG.getNameWithAttributes(receiverLG));
        this.playerLG = playerLG;

        this.generator = generator;

        this.setDamage(3);
        this.setSkullOwner(playerLG.getName());
        this.setLore(generator.generateLore(playerLG));
        this.setLoreLine(this.getLore().size() - 2, "§0" + this.hashCode());

        //Bukkit.broadcastMessage(receiverLG.getName());
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Inventory chooseInv = player.getOpenInventory().getTopInventory();
        chooseInv.remove(this);
        chooseInv.setItem(chooseInv.getSize() - 1, new CancelBarrierItemStack(generator));
        generator.doActionsAfterClick(playerLG);
    }
}
