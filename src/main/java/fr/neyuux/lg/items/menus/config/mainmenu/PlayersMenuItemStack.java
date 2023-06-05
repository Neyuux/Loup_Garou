package fr.neyuux.lg.items.menus.config.mainmenu;

import fr.neyuux.lg.inventories.config.players.ListPlayersInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class PlayersMenuItemStack extends CustomItemStack {

    public PlayersMenuItemStack(String owner) {
        super(Material.SKULL_ITEM, 1, "§6Joueurs");

        this.setDamage(3);
        this.setSkullOwner(owner);
        this.setLore("§fPermet de gérer", "§fles joueurs", "§f§o(spectateur, etc)");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ListPlayersInv().open(player);
    }
}
