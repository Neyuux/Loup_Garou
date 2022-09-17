package fr.neyuux.refont.lg.items.menus.config.reset;

import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class NoResetMapItemStack extends CustomItemStack {

    public NoResetMapItemStack() {
        super(Material.STAINED_CLAY, 1, "§c§lRetour");
        this.setDamage(14);

        this.setLore("§7Revenir au menu", "§7précédent.");
    }


    @Override
    public void use(HumanEntity player, Event event) {
        new ConfigurationInv().open(player);
    }
}