package fr.neyuux.lg.items.menus.config.reset;

import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class NoResetMapItemStack extends CustomItemStack {

    public NoResetMapItemStack() {
        super(Material.STAINED_CLAY, 1, "§c§lRetour");
        this.setDamage(14);

        this.setLore("§7Revenir au menu", "§7précédent.");

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        new ConfigurationInv().open(player);
    }
}