package fr.neyuux.lg.items.menus.config.mainmenu;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.inventories.config.ChangeGameTypeInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ChangeGameTypeItemStack extends CustomItemStack {

    public ChangeGameTypeItemStack() {
        super(Material.ITEM_FRAME, 1, "§2Changer le §lType §2de jeu");

        this.setLore("§fPermet de changer le", "§ftype de jeu de la partie.", "", "§eActuel : §c§l" + LG.getInstance().getGame().getGameType().getName());

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ChangeGameTypeInv().open(player);
    }
}
