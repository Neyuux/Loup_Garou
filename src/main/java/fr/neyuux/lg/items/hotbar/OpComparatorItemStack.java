package fr.neyuux.lg.items.hotbar;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.config.GameConfig;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class OpComparatorItemStack extends CustomItemStack {

    public OpComparatorItemStack() {
        super(Material.REDSTONE_COMPARATOR);
        this.setDisplayName("§c§lConfiguration de la Partie");
        this.addGlowEffect();

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        GameConfig config = LG.getInstance().getGame().getConfig();
        if (config.getDayCycle() == null) config.registerParameters();
        ConfigurationInv.INVENTORY.open((Player) player);
    }
}
