package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.RoleChoosePlayerItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

import java.util.List;

public class ChoosePlayerInv extends AbstractCustomInventory {

    private final PlayerLG targetLG;
    private final ActionsGenerator generator;
    private List<PlayerLG> choosable;

    public ChoosePlayerInv(String name, PlayerLG targetLG, List<PlayerLG> choosable, ActionsGenerator generator) {
        super(name, 36);
        this.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size());

        this.targetLG = targetLG;
        this.generator = generator;
        this.choosable = choosable;
        if (choosable == null)
            this.choosable = LG.getInstance().getGame().getAlive();
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new CancelBarrierItemStack(() -> generator.doActionsAfterClick(null)));

        for (PlayerLG playerLG : choosable)
            if (playerLG != targetLG)
                this.addItem(new RoleChoosePlayerItemStack(targetLG, playerLG, generator));
    }

    public interface ActionsGenerator {
        String[] generateLore(PlayerLG paramPlayerLG);
        void doActionsAfterClick(PlayerLG choosenLG);
    }
}
