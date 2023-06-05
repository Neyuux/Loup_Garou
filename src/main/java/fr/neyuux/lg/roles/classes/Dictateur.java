package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.DictateurInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;

public class Dictateur extends Role {

    @Override
    public String getDisplayName() {
        return "�4�lDicta�2�lteur";
    }

    @Override
    public String getConfigName() {
        return "Dictateur";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez faire un �9coup d'�tat �fet prendre le contr�le du vote. Vous serez le seul � pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le r�le de �bMaire�f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WOLFY;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �2" + this.getTimeout() + " secondes pour choisir de faire un coup d'�tat.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        new DictateurInv(this, playerLG, callback).open(playerLG.getPlayer());
        playerLG.getCache().put("unclosableInv", true);
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }


    @EventHandler
    public void onCloseDictateurInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().equals(this.getDisplayName()) && (boolean)playerLG.getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerLG.getCache().put("unclosableInv", false);
                    player.openInventory(inv);
                    playerLG.getCache().put("unclosableInv", true);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onNightStart(NightStartEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            playerLG.getCache().remove("dictateurCoupDEtat");
    }


    @EventHandler
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();

        if (vote.getName().equals("Vote du Village"))
            for (PlayerLG playerLG : vote.getVoters())
                if (playerLG.getCache().has("dictateurCoupDEtat")) {

                    vote.forceStop();

                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(LG.getPrefix() + "�eLe " + this.getDisplayName() + " �c" + playerLG.getName() + " �eeffectue un coup d'�tat ! Il a une minute pour choisir la personne qu'il souhaite �liminer. S'il �limine un Villageois, il se suicidera le lendemain, sinon, il deviendra maire.");

                    VoteLG dictaVote = new VoteLG("Vote du Dictateur", 60, true, (voterLG, secondsLeft) -> {
                        if (voterLG.getCache().has("vote"))
                            if (voterLG.getCache().get("vote") == null)
                                return LG.getPrefix() + "�4Vous ne votez pour �c�lpersonne�4.";
                            else
                                return LG.getPrefix() + "�4Vous votez pour �c�l" + ((PlayerLG)voterLG.getCache().get("vote")).getName() + "�c.";

                        return null;
                    }, ChatColor.DARK_RED, ChatColor.RED, vote.getVotable(), Collections.singletonList(playerLG), vote.getObservers());

                    dictaVote.start(() -> {
                        PlayerLG choosen = dictaVote.getChoosen();
                        GameLG game = LG.getInstance().getGame();

                        if (choosen.isLG()) {
                            playerLG.setMayor();

                            Bukkit.broadcastMessage(LG.getPrefix() + "�eLe " + Dictateur.this.getDisplayName() + " �ea �limin� un Loup-Garou ! Il prend donc le r�le de maire.");
                        } else {
                            game.kill(playerLG);

                            Bukkit.broadcastMessage(LG.getPrefix() + "�eLe " + Dictateur.this.getDisplayName() + " �ea �limin� un Villageois ! Il se suicidera donc demain.");
                        }

                        game.getGameRunnable().endDay(choosen);
                    });

                    playerLG.setCanUsePowers(false);

                    return;
                }
    }
}
