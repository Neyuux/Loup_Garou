package fr.neyuux.refont.lg.inventories.roleinventories;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.items.menus.roleinventories.RoleChoosePlayerItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class RoleChoosePlayerInv extends AbstractCustomInventory {

    private final PlayerLG targetLG;
    private final ActionsGenerator generator;

    public RoleChoosePlayerInv(String name, PlayerLG targetLG, ActionsGenerator generator) {
        super(name, 36);
        this.adaptIntToInvSize(LG.getInstance().getGame().getPlayersInGame().size() - 1);

        this.targetLG = targetLG;
        this.generator = generator;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)11);

        for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersInGame())
            if (playerLG != targetLG)
                this.addItem(new RoleChoosePlayerItemStack(targetLG, playerLG, generator));
    }

    public interface ActionsGenerator {
        String[] generateLore(PlayerLG paramPlayerLG);
        void doActionsAfterClick(PlayerLG choosenLG);
    }
}
