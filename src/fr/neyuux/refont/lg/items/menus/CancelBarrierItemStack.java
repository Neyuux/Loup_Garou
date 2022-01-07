package fr.neyuux.refont.lg.items.menus;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class CancelBarrierItemStack extends CustomItemStack {

    public CancelBarrierItemStack() {
        super(Material.BARRIER, 1, "§cAnnuler");

        this.setLore("§7Annule l'action", "§7en cours.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        //TODO
    }
}
