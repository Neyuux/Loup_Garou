package fr.neyuux.refont.lg.items.menus.config.players;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.config.players.PlayerConfigInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class PlayerConfigItemStack extends CustomItemStack {

    private final PlayerLG playerLG;

    public PlayerConfigItemStack(PlayerLG playerLG) {
        super(Material.SKULL_ITEM, 1, playerLG.getDisplayName());
        this.playerLG = playerLG;

        this.setDamage(3);
        this.setSkullOwner(playerLG.getName());
        this.setLore("", "�7Spectateur � " + LG.getStringBoolean(playerLG.isSpectator()), "�aOP � " + LG.getStringBoolean(playerLG.isOP()), "", "�7>>Clique pour g�rer ce joueur");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new PlayerConfigInv(playerLG).open(player);
    }
}
