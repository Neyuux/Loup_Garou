package fr.neyuux.lg.items.hotbar;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpectatorTearItemStack extends CustomItemStack {

    public SpectatorTearItemStack() {
        super(Material.GHAST_TEAR, 1, "§7§lDevenir Spectateur");

        this.setLore("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent pievent = (PlayerInteractEvent) event;
            if (pievent.getAction().equals(Action.LEFT_CLICK_AIR) || pievent.getAction().equals(Action.LEFT_CLICK_BLOCK))
                return;
        }

        LG.getInstance().getGame().setSpectator(PlayerLG.createPlayerLG(player));
    }
}
