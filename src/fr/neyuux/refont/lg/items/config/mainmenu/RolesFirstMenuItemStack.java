package fr.neyuux.refont.lg.items.config.mainmenu;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class RolesFirstMenuItemStack extends CustomItemStack {

    public RolesFirstMenuItemStack() {
        super(Material.EMPTY_MAP, 1, "§6§lRôles");

        this.setLore("§fPermet de gérer les", "§frôles de la partie.");
    }

    @Override
    public void use(HumanEntity player, ClickType clickType) {
        //TODO
    }
}
