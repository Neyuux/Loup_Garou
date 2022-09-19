package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.inventories.config.ChangeGameTypeInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ChangeGameTypeItemStack extends CustomItemStack {

    public ChangeGameTypeItemStack() {
        super(Material.ITEM_FRAME, 1, "§2Changer le §lType §2de jeu");

        this.setLore("§fPermet de changer le", "§ftype de jeu de la partie.", "", "§eActuel : §c§l" + LG.getInstance().getGame().getGameType().getName());
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ChangeGameTypeInv().open(player);
    }
}
