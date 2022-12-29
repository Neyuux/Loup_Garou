package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.event.VoteEndEvent;
import fr.neyuux.refont.lg.event.VoteStartEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

public class Ancien extends Role {
    @Override
    public String getDisplayName() {
        return "§7§lAncien";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, pendant une nuit, les Loups décident de vous attaquer, vous §9survivez§f §o(utilisable qu'une seule fois)§f. Cependant si le village vous éliminent pendant le jour, tous les villageois §4perdront leurs pouvoirs§f.";
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

        if (vote.getName().equals("Vote du Village") && choosen.getRole() instanceof Ancien) {
            Bukkit.broadcastMessage(LG.getPrefix() + "§cLe Village a éliminé l'" + this.getDisplayName() + " §c! Tous les villageois perdent donc leurs pouvoirs.");
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
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();
        for (PlayerLG playerLG : vote.getVoters())
            if (playerLG.getCache().has("idiotDuVillageVoted")) {
                vote.getVoters().remove(playerLG);
                playerLG.sendMessage(LG.getPrefix() + "§cVous ne pouvez plus voter...");
                playerLG.getCache().put("vote", null);
                playerLG.stopChoosing();
                playerLG.setArmorStand(null);
                LG.getInstance().getItemsManager().updateVoteItems(playerLG);
            }
    }
}
