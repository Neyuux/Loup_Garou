package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.RoleChoosePlayerItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class RoleChoosePlayerInv extends AbstractCustomInventory {

    private final PlayerLG targetLG;
    private final ActionsGenerator generator;

    public RoleChoosePlayerInv(String name, PlayerLG targetLG, ActionsGenerator generator) {
        super(name, 36);
        this.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size());

        this.targetLG = targetLG;
        this.generator = generator;
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new CancelBarrierItemStack(() -> generator.doActionsAfterClick(null)));

        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG != targetLG)
                this.addItem(new RoleChoosePlayerItemStack(targetLG, playerLG, generator));
    }

    public interface ActionsGenerator {
        String[] generateLore(PlayerLG paramPlayerLG);
        void doActionsAfterClick(PlayerLG choosenLG);
    }
}
