package fr.neyuux.refont.lg.items.menus;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class CancelBarrierItemStack extends CustomItemStack {

    private final ChoosePlayerInv.ActionsGenerator actionsGenerator;

    public CancelBarrierItemStack(ChoosePlayerInv.ActionsGenerator actionsGenerator) {
        super(Material.BARRIER, 1, "§cAnnuler");

        this.actionsGenerator = actionsGenerator;

        this.setLore("§7Annule l'action", "§7en cours.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        actionsGenerator.doActionsAfterClick(PlayerLG.createPlayerLG(player));
    }
}
