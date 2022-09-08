package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class RolesFirstMenuItemStack extends CustomItemStack {

    public RolesFirstMenuItemStack() {
        super(Material.EMPTY_MAP, 1, "§6§lRôles");

        this.setLore("§fPermet de gérer les", "§frôles de la partie.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new RoleDecksInv().open(player);
    }
}
