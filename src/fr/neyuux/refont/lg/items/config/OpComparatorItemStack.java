package fr.neyuux.refont.lg.items.config;

import fr.neyuux.refont.lg.inventories.ConfigurationInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class OpComparatorItemStack extends CustomItemStack {

    public OpComparatorItemStack() {
        super(Material.REDSTONE_COMPARATOR, 1, "§c§lConfiguration de la partie");

        this.addGlowEffect();
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ConfigurationInv().open(player);
    }
}
