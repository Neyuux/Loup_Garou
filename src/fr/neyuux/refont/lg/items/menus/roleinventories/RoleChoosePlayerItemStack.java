package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.roleinventories.RoleChoosePlayerInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class RoleChoosePlayerItemStack extends CustomItemStack {

    private final RoleChoosePlayerInv.ActionsGenerator generator;
    private final PlayerLG playerLG;

    public RoleChoosePlayerItemStack(PlayerLG targetLG, PlayerLG playerLG, RoleChoosePlayerInv.ActionsGenerator generator) {
        super(Material.SKULL_ITEM, 1, playerLG.getNameWithAttributes(targetLG));

        this.generator = generator;
        this.playerLG = playerLG;

        this.setDamage(3);
        this.setSkullOwner(playerLG.getName());
        this.setLore(generator.generateLore(playerLG));

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        player.getOpenInventory().getTopInventory().remove(this);
        generator.doActionsAfterClick(playerLG);
    }
}
