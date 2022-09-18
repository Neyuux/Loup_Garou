package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class PlayersMenuItemStack extends CustomItemStack {

    public PlayersMenuItemStack(String owner) {
        super(Material.SKULL_ITEM, 1, "§6Joueurs");

        this.setDamage(3);
        this.setSkullOwner(owner);
        this.setLore("§fPermet de gérer", "§fles joueurs", "§f§o(spectateur, etc)");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        //TODO open players inv
    }
}
