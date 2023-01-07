package fr.neyuux.lg.items.menus.config.changegametypeinv;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ChangeGameTypeToMeetingItemStack extends CustomItemStack {

    public ChangeGameTypeToMeetingItemStack() {
        super(Material.IRON_FENCE, 1, "�d�lR�union");

        this.setLore("�7Les joueurs sont bloqu�s sur un n�nuphar.", "�7A eux de discuter sans se d�placer.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().setGameType(GameType.MEETING);
        player.closeInventory();
    }
}