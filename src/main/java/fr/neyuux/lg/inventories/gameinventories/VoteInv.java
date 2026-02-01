package fr.neyuux.lg.inventories.gameinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ChoosePlayerItemStack;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteInv implements InventoryProvider {

    private final VoteLG vote;
    private final int rows;
    private PlayerLG receiverLG;

    public VoteInv(VoteLG vote) {
        this.vote = vote;
        this.rows = LG.adaptIntToInvSize(vote.getVotable().size() + 18);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        this.receiverLG = PlayerLG.createPlayerLG(player);

        final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, LG.translateChatColorToByte(this.vote.getSecondColor())).setDisplayName("§f"));

        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(rows - 2, 0, GLASS_PANE);
        contents.set(rows - 2, 8, GLASS_PANE);

        contents.set(rows - 1, 0, GLASS_PANE);
        contents.set(rows - 1, 1, GLASS_PANE);
        contents.set(rows - 1, 7, GLASS_PANE);

        CustomItemStack cancelItem = new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG playerLG) {
                VoteInv.this.vote.vote(playerLG, null);
            }
        });

        contents.set(rows - 1, 8, ClickableItem.of(cancelItem, ev -> cancelItem.use(ev.getWhoClicked(), ev)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<PlayerLG> votable = new ArrayList<>(vote.getVotable());

        for (int slot = 10; slot < rows * 9 - 10; slot++) {
           if (votable.isEmpty()) break;

           PlayerLG votableLG = votable.remove(0);

            ChoosePlayerItemStack choose = new ChoosePlayerItemStack(receiverLG, votableLG, new ChoosePlayerInv.ActionsGenerator() {
                @Override
                public String[] generateLore(PlayerLG playerLG) {
                    if (playerLG == null) return new String[0];
                    return new String[]{vote.getFirstColor() + "Choisis le joueur " + playerLG.getNameWithAttributes(receiverLG) + vote.getFirstColor() + " pour le vote.", "", vote.getFirstColor() + "Nombre de votes actuels : " + vote.getSecondColor() + "§l" + vote.getVotesFor(playerLG).size(), "", "§7>>Clique pour voter"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG paramPlayerLG) {
                    //Bukkit.broadcastMessage("§a" + receiverLG.getName());
                    receiverLG.callbackChoice(paramPlayerLG);
                }
            });
            choose.setAmount(this.vote.getVotesFor(votableLG).size());

            contents.set(slot / 9, slot % 9, ClickableItem.of(choose, ev -> choose.use(ev.getWhoClicked(), ev)));
        }
    }


    public static SmartInventory getInventory(VoteLG vote) {
        return SmartInventory.builder()
                .id("vote")
                .provider(new VoteInv(vote))
                .size(LG.adaptIntToInvSize(vote.getVotable().size() + 18), 9)
                .title(vote.getFirstColor() + "§lVote")
                .closeable(true)
                .build();
    }
}
