package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteEndEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class Ancien extends Role {
    @Override
    public String getDisplayName() {
        return "�7�lAncien";
    }

    @Override
    public String getConfigName() {
        return "Ancien";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, pendant une nuit, les Loups d�cident de vous attaquer, vous �9survivez�f �o(utilisable qu'une seule fois)�f. Cependant si le village vous �liminent pendant le jour, tous les villageois �4perdront leurs pouvoirs�f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVoteEnd(VoteEndEvent ev) {
        GameLG game = LG.getInstance().getGame();
        VoteLG vote = ev.getVote();
        PlayerLG choosen = ev.getChoosen();

        if (vote.getName().equals("Vote du Village") && choosen != null && choosen.getRole() instanceof Ancien) {
            Bukkit.broadcastMessage(LG.getPrefix() + "�cLe Village a �limin� l'" + this.getDisplayName() + " �c! Tous les villageois perdent donc leurs pouvoirs.");
            for (Player p : Bukkit.getOnlinePlayers())
                p.playSound(p.getLocation(), Sound.WITHER_HURT, 8f, 1.05f);

            for (PlayerLG playerLG : game.getAlive())
                if (playerLG.getCamp().equals(Camps.VILLAGE)) {

                    game.getAliveRoles().remove(playerLG.getRole());
                    playerLG.setRole(new SimpleVillageois());
                    game.getAliveRoles().add(playerLG.getRole());
                }
        }
    }

    @EventHandler
    public void onLGChoice(RoleChoiceEvent ev) {
        PlayerLG playerLG = ev.getChoosen();

        if (ev.getRole() instanceof LoupGarou && playerLG.getRole() instanceof Ancien && playerLG.canUsePowers()) {
            ev.setCancelled(true);
            playerLG.setCanUsePowers(false);

            playerLG.sendMessage(LG.getPrefix() + "�cVous avez �t� attaqu� par les Loups-Garous cette nuit. Gr�ce � votre exp�rience vous avez �asurv�cu�c. Cependant, vous mourrez s'il d�cident de revenir.");
            playerLG.sendTitle(LG.getPrefix() + "�aVous avez surv�cu !", "�cVous avez �t� attaqu� par les Loups mais vous avez surv�cu.", 20, 60, 20);
            GameLG.playPositiveSound(playerLG.getPlayer());
        }
    }
}
