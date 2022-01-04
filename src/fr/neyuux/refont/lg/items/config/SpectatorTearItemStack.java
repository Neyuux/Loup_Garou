package fr.neyuux.refont.lg.items.config;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpectatorTearItemStack extends CustomItemStack {

    public SpectatorTearItemStack() {
        super(Material.GHAST_TEAR, 1, "§7§lDevenir Spectateur");

        this.setLore("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent pievent = (PlayerInteractEvent) event;
            if (pievent.getAction().equals(Action.LEFT_CLICK_AIR) || pievent.getAction().equals(Action.LEFT_CLICK_BLOCK))
                return;
        }


    }
}
