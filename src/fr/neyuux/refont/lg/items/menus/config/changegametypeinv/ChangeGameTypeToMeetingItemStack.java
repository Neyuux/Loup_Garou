package fr.neyuux.refont.lg.items.menus.config.changegametypeinv;

import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ChangeGameTypeToMeetingItemStack extends CustomItemStack {

    public ChangeGameTypeToMeetingItemStack() {
        super(Material.IRON_FENCE, 1, "§d§lRéunion");

        this.setLore("§7Les joueurs sont bloqués sur un nénuphar.", "§7A eux de discuter sans se déplacer.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().setGameType(GameType.MEETING);
        player.closeInventory();
    }
}