package fr.neyuux.refont.lg.inventories.gameinventories;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.VoteLG;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.refont.lg.items.menus.roleinventories.ChoosePlayerItemStack;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;

public class VoteInv extends AbstractCustomInventory {

    private final VoteLG vote;
    private final PlayerLG receiverLG;
    private final ChoosePlayerInv.ActionsGenerator generator;

    public VoteInv(VoteLG vote, PlayerLG receiverLG, ChoosePlayerInv.ActionsGenerator generator) {
        super(vote.getFirstColor() + "§lVote", 36);

        this.vote = vote;
        this.receiverLG = receiverLG;
        this.generator = generator;

        this.adaptIntToInvSize(vote.getVotable().size() + 18);
    }

    @Override
    public void registerItems() {
        this.setAllCorners(LG.translateChatColorToByte(vote.getSecondColor()));

        this.setItem(this.getSize() - 1, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG playerLG) {
                playerLG.callbackChoice(null);
            }
        }));

        for (PlayerLG votableLG : vote.getVotable())
            for (int slot = 10; slot < this.getSize() - 9; slot++)
                if (this.getItem(slot) == null) {

                    this.setItem(slot, new ChoosePlayerItemStack(receiverLG, votableLG, generator));
                    return;
                }
    }
}
