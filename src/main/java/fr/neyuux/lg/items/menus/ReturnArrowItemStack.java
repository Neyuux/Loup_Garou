package fr.neyuux.lg.items.menus;

import fr.neyuux.lg.utils.CustomItemStack;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class ReturnArrowItemStack extends CustomItemStack {

    public ReturnArrowItemStack() {
        super(Material.ARROW, 1, "§cRetour");

        this.setLore("§7Revenir au menu", "§7précédent.");
    }
}