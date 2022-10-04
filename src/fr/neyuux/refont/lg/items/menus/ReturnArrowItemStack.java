package fr.neyuux.refont.lg.items.menus;

import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class ReturnArrowItemStack extends CustomItemStack {

    private final AbstractCustomInventory previousInv;

    public ReturnArrowItemStack(AbstractCustomInventory previousInv) {
        super(Material.ARROW, 1, "§cRetour");

        this.previousInv = previousInv;

        this.setLore("§7Revenir au menu", "§7précédent. §0(" + previousInv.getID() + "§0)");

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        this.previousInv.open(player);
    }
}