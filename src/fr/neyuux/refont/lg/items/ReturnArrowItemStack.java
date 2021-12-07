package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ReturnArrowItemStack extends CustomItemStack {

    public ReturnArrowItemStack() {
        super(Material.ARROW, 1, "§cRetour");

        this.setLore("§7Revenir au menu", "§7précédent.");
    }


    @Override
    public void use(Player player, ClickType clickType) {

    }
}