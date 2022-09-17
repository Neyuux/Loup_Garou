package fr.neyuux.refont.lg.items.menus.config.players;

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
        super(Material.SKULL, 1, playerLG.getDisplayName());
        this.playerLG = playerLG;

        this.setDamage(3);
        this.setSkullOwner(playerLG.getName());
        this.setLore("", "§7Spectateur » " + playerLG.isSpectator(), "§aOP » " + playerLG.isOP(), "", "§7>>Clique pour gérer ce joueur");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new PlayerConfigInv(playerLG).open(player);
    }
}
