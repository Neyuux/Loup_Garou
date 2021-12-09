package fr.neyuux.refont.lg.items;

import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class ReturnArrowItemStack extends CustomItemStack {

    private final AbstractCustomInventory previousInv;

    public ReturnArrowItemStack(AbstractCustomInventory previousInv) {
        super(Material.ARROW, 1, "§cRetour");

        this.previousInv = previousInv;

        this.setLore("§7Revenir au menu", "§7précédent.");
    }


    @Override
    public void use(HumanEntity player, ClickType clickType) {
        this.previousInv.open(player);
    }
}