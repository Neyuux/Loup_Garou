package fr.neyuux.refont.lg.items.hotbar;

import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class OpComparatorItemStack extends CustomItemStack {

    public OpComparatorItemStack() {
        super(Material.REDSTONE_COMPARATOR, 1, "�c�lConfiguration de la partie");

        this.addGlowEffect();
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ConfigurationInv().open(player);
    }
}
