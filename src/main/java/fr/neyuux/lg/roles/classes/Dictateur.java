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
import org.bukkit.event.EventHandler;

import java.util.Collections;

public class Dictateur extends Role {

    @Override
    public String getDisplayName() {
        return "§4§lDicta§2§lteur";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez faire un §9coup d'état §fet prendre le contrôle du vote. Vous serez le seul à pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le rôle de §bMaire§f.";
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
        return "§fVous avez §2" + this.getTimeout() + " §fsecondes pour choisir de faire un coup d'état.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        super.onPlayerNightTurn(playerLG, callback);
        DictateurInv.getInventory(this, playerLG, callback).open(playerLG.getPlayer());
        
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
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
                    Bukkit.broadcastMessage(LG.getPrefix() + "§eLe " + this.getDisplayName() + " §c" + playerLG.getName() + " §eeffectue un coup d'état ! Il a une minute pour choisir la personne qu'il souhaite éliminer. S'il élimine un Villageois, il se suicidera le lendemain, sinon, il deviendra maire.");

                    VoteLG dictaVote = new VoteLG("Vote du Dictateur", 60, true, (voterLG, secondsLeft) -> {
                        if (voterLG.getCache().has("vote"))
                            if (voterLG.getCache().get("vote") == null)
                                return LG.getPrefix() + "§4Vous ne votez pour §c§lpersonne§4.";
                            else
                                return LG.getPrefix() + "§4Vous votez pour §c§l" + ((PlayerLG)voterLG.getCache().get("vote")).getName() + "§c.";

                        return null;
                    }, ChatColor.DARK_RED, ChatColor.RED, vote.getVotable(), Collections.singletonList(playerLG), vote.getObservers());

                    dictaVote.start(() -> {
                        PlayerLG choosen = dictaVote.getChoosen();
                        GameLG game = LG.getInstance().getGame();

                        if (choosen.isLG()) {
                            playerLG.setMayor();

                            Bukkit.broadcastMessage(LG.getPrefix() + "§eLe " + Dictateur.this.getDisplayName() + " §ea éliminé un Loup-Garou ! Il prend donc le rôle de maire.");
                        } else {
                            game.kill(playerLG);

                            Bukkit.broadcastMessage(LG.getPrefix() + "§eLe " + Dictateur.this.getDisplayName() + " §ea éliminé un Villageois ! Il se suicidera donc demain.");
                        }

                        game.getGameRunnable().endDay(choosen);
                    });

                    playerLG.setCanUsePowers(false);

                    return;
                }
    }
}
