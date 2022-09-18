package fr.neyuux.refont.lg.items.hotbar;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.GameConfig;
import fr.neyuux.refont.lg.inventories.config.ConfigurationInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class OpComparatorItemStack extends CustomItemStack {

    public OpComparatorItemStack() {
        super(Material.REDSTONE_COMPARATOR);
        this.setDisplayName("§c§lConfiguration de la Partie");
        this.addGlowEffect();
    }

    @Override
    public void use(HumanEntity player, Event event) {
        GameConfig config = LG.getInstance().getGame().getConfig();
        if (config.getDayCycle() == null) config.registerParameters();
        new ConfigurationInv().open(player);
    }
}
