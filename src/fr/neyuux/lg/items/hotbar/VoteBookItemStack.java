package fr.neyuux.lg.items.hotbar;

import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.inventories.gameinventories.VoteInv;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class VoteBookItemStack extends CustomItemStack {

    private final VoteLG vote;

    public VoteBookItemStack(VoteLG vote) {
        super(Material.BOOK);

        this.vote = vote;

        this.setDisplayName(vote.getFirstColor() + vote.getName());
        this.setLore("§7>>Cliquer pour ouvrir l'inventaire de vote");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG receiverLG = PlayerLG.createPlayerLG(player);

        new VoteInv(vote, receiverLG, new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG playerLG) {
                if (playerLG == null) return new String[0];
                return new String[] {vote.getFirstColor() + "Choisis le joueur " + playerLG.getNameWithAttributes(receiverLG) + vote.getFirstColor() + " pour le vote.", "", "§7>>Clique pour voter"};
            }

            @Override
            public void doActionsAfterClick(PlayerLG paramPlayerLG) {
                receiverLG.callbackChoice(paramPlayerLG);
            }
        }).open(player);
    }
}
