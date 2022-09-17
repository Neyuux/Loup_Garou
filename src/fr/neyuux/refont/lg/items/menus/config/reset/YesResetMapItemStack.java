package fr.neyuux.refont.lg.items.menus.config.reset;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class YesResetMapItemStack extends CustomItemStack {

    public YesResetMapItemStack() {
        super(Material.STAINED_CLAY, 1, "§a§lOui");

        this.setLore("§7>>Clique pour", "§7reset la Map.");
    }


    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().resetGame();
    }
}