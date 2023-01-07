package fr.neyuux.lg.items.menus;

import fr.neyuux.lg.utils.AbstractCustomInventory;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class ReturnArrowItemStack extends CustomItemStack {

    private final AbstractCustomInventory previousInv;
    private final Inventory inv;

    public ReturnArrowItemStack(AbstractCustomInventory previousInv) {
        super(Material.ARROW, 1, "§cRetour");

        this.previousInv = previousInv;
        this.inv = null;

        this.setLore("§7Revenir au menu", "§7précédent. §0(" + previousInv.getID() + "§0)");

        addItemInList(this);
    }

    public ReturnArrowItemStack(Inventory previousInv) {
        super(Material.ARROW, 1, "§cRetour");

        this.inv = previousInv;
        this.previousInv = null;

        this.setLore("§7Revenir au menu", "§7précédent.");

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        if (inv == null) this.previousInv.open(player);
        else player.openInventory(this.inv);
    }
}