package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.VoteLG;
import fr.neyuux.refont.lg.event.VoteEndEvent;
import fr.neyuux.refont.lg.event.VoteStartEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.old.lg.role.Roles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class IdiotDuVillage extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lIdiot §e§ldu Village";
    }

    @Override
    public String getConfigName() {
        return "Idiot du Village";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, une fois dans la partie, le village décide de vous pendre, ils §9reconnaîtront votre bêtise§f. Vous ne mourrez donc pas, mais §9vous ne pourrez plus voter§f.";
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


    @EventHandler
    public void onVoteEnd(VoteEndEvent ev) {
        VoteLG vote = ev.getVote();
        PlayerLG choosen = ev.getChoosen();

        if (vote.getName().equals("Vote du Village") && choosen.getRole() instanceof IdiotDuVillage && !choosen.getCache().has("idiotDuVillageVoted")) {
            vote.setChoosen(null);

            Bukkit.broadcastMessage(LG.getPrefix() + "§fLe village, juste avant de pendre §e" + choosen.getName() + "§f, remarque §fqu'il§4 est COMPLETEMENT DÉBILE§f, il ne meurt donc pas mais son de rôle de " + this.getDisplayName() + " §fest révélé et il ne pourra plus voter.");
            for (Player p : Bukkit.getOnlinePlayers())
                p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 8f, 1.2f);

            choosen.getCache().put("idiotDuVillageVoted", null);
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
