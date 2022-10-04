package fr.neyuux.refont.lg.items.menus.config.changegametypeinv;

import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ChangeGameTypeToFreeItemStack extends CustomItemStack {

    public ChangeGameTypeToFreeItemStack() {
        super(Material.ENDER_PORTAL_FRAME, 1, "§e§lLibre");

        this.setLore("§7Les joueurs sont libres de se balader", "§7dans le village.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().setGameType(GameType.FREE);
        player.closeInventory();
    }
}
