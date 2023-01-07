package fr.neyuux.lg.items.menus;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

import java.util.Arrays;

public class CancelBarrierItemStack extends CustomItemStack {

    private final ChoosePlayerInv.ActionsGenerator actionsGenerator;

    public CancelBarrierItemStack(ChoosePlayerInv.ActionsGenerator actionsGenerator) {
        super(Material.BARRIER, 1, "§cAnnuler");

        this.actionsGenerator = actionsGenerator;

        this.setLore("§7Annule l'action", "§7en cours.");

        if (!Arrays.equals(actionsGenerator.generateLore(null), new String[0]))
            this.setLore(actionsGenerator.generateLore(null));

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        actionsGenerator.doActionsAfterClick(PlayerLG.createPlayerLG(player));
    }
}
