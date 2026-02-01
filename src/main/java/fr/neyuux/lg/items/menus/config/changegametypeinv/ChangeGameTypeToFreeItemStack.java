package fr.neyuux.lg.items.menus.config.changegametypeinv;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ChangeGameTypeToFreeItemStack extends CustomItemStack {

    public ChangeGameTypeToFreeItemStack() {
        super(Material.ENDER_PORTAL_FRAME, 1, "§e§lLibre");

        this.setLore("§7Les joueurs sont libres de se balader", "§7dans le village.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().setGameType(GameType.FREE);
        LG.closeSmartInv((Player) player);
    }
}
