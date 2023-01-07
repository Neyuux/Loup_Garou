package fr.neyuux.lg.inventories.config;

import fr.neyuux.lg.items.menus.config.mainmenu.*;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import org.bukkit.entity.HumanEntity;

public class ConfigurationInv extends AbstractCustomInventory {

    private String openerName = null;


    public ConfigurationInv() {
        super("§c§lConfiguration", 45);
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(38, new OpListItemStack());
        this.setItem(30, new PlayersMenuItemStack(openerName));
        this.setItem(32, new ResetGameItemStack());
        this.setItem(13, new ParametersItemStack());
        this.setItem(15, new RolesFirstMenuItemStack());
        this.setItem(11, new ChangeGameTypeItemStack());
    }

    @Override
    public void open(HumanEntity player) {
        this.openerName = player.getName();
        super.open(player);
    }
}
