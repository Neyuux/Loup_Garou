package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ChoosePlayerItemStack;
import fr.neyuux.lg.utils.AbstractCustomInventory;

import java.util.List;

public class ChoosePlayerInv extends AbstractCustomInventory {

    private final PlayerLG receiverLG;
    private final ActionsGenerator generator;
    private List<PlayerLG> choosable;

    public ChoosePlayerInv(String name, PlayerLG receiverLG, List<PlayerLG> choosable, ActionsGenerator generator) {
        super(name, 36);
        this.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size());

        this.receiverLG = receiverLG;
        this.generator = generator;
        this.choosable = choosable;
        if (choosable == null)
            this.choosable = LG.getInstance().getGame().getAlive();
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new CancelBarrierItemStack(generator));

        for (PlayerLG playerLG : choosable)
            if (playerLG != receiverLG)
                this.addItem(new ChoosePlayerItemStack(receiverLG, playerLG, generator));
    }

    public interface ActionsGenerator {
        String[] generateLore(PlayerLG paramPlayerLG);
        void doActionsAfterClick(PlayerLG paramPlayerLG);
    }
}
