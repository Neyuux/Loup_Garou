package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.inventories.config.ResetInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ResetGameItemStack extends CustomItemStack {

    public ResetGameItemStack() {
        super(Material.BARRIER, 1, "§bReset la Map");

        this.setLore("§fPermet de reset", "§fla map.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ResetInv().open(player);
    }
}
