package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ChoosePlayerItemStack extends CustomItemStack {

    private final ChoosePlayerInv.ActionsGenerator generator;

    public ChoosePlayerItemStack(PlayerLG receiverLG, PlayerLG playerLG, ChoosePlayerInv.ActionsGenerator generator) {
        super(Material.SKULL_ITEM, 1, playerLG.getNameWithAttributes(receiverLG));

        this.generator = generator;

        this.setDamage(3);
        this.setSkullOwner(playerLG.getName());
        this.setLore(generator.generateLore(playerLG));

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        player.getOpenInventory().getTopInventory().remove(this);
        generator.doActionsAfterClick(PlayerLG.createPlayerLG(player));
    }
}
