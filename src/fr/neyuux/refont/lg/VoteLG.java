package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.event.VoteStartEvent;
import fr.neyuux.refont.lg.roles.classes.Ankou;
import fr.neyuux.refont.old.lg.DisplayState;
import fr.neyuux.refont.old.lg.Gtype;
import fr.neyuux.refont.old.lg.role.RCamp;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoteLG {

    private int timer;

    private final boolean randomIfEqual;

    private final GameLG.StringTimerMessage timerMessage;

    private final ChatColor firstColor;

    private final ChatColor secondColor;

    private Runnable callback;

    private List<PlayerLG> votable;

    private List<PlayerLG> voters;

    private List<PlayerLG> observers;


    public VoteLG(int timer, boolean randomIfEqual, GameLG.StringTimerMessage timerMessage, ChatColor firstColor, ChatColor secondColor) {
        this.timer = timer;
        this.randomIfEqual = randomIfEqual;
        this.timerMessage = timerMessage;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }


    public void start(Runnable callback, List<PlayerLG> votable, List<PlayerLG> voters, List<PlayerLG> observers) {
        this.callback = callback;
        this.votable = votable;
        this.voters = voters;
        this.observers = observers;

        LG.getInstance().getGame().wait(timer, this::end, timerMessage);
        for (PlayerLG voterLG : voters) {
            voterLG.getCache().put("vote", null);
            voterLG.setChoosing(choosen -> this.vote(voterLG, choosen));
        }

        Bukkit.getPluginManager().callEvent(new VoteStartEvent(this));
    }

    private void vote(PlayerLG voter, PlayerLG voted) {

        if (!this.voters.contains(voter)) {
            voter.sendMessage(LG.getPrefix() + "§cVous ne pouvez pas voter.");
            GameLG.playNegativeSound(voter.getPlayer());
            return;
        }

        if (!this.votable.contains(voted)) {
            voter.sendMessage(LG.getPrefix() + voted.getNameWithAttributes(voter) + "§c n'est pas votable.");
            GameLG.playNegativeSound(voter.getPlayer());
            return;
        }

        if (voted == this.getVote(voter))
            voted = null;

        boolean voteChanged = false;

        if (voter.getCache().get("vote") != null) {
            PlayerLG actualVote = (PlayerLG) voter.getCache().get("vote");

            voteChanged = true;
            voter.getCache().put("vote", null);
            this.updateArmorStand(actualVote);
        }

        voter.getCache().put("vote", voted);
        if (voted != null) this.updateArmorStand(voted);
        

        if (voter.getRole() instanceof Ankou && voter.isDead()) {
            String name = voter.getRole().getDeterminingName().substring(3);
            StringBuilder message = new StringBuilder(LG.getPrefix() + name.substring(0, 1).toUpperCase() + name.substring(1) + " §4" + voter.getName() + " §c");

            if (voted != null) {
                if (voteChanged) message.append("change son vote pour §4").append(voted.getName()).append("§c.");
                else message.append("vote pour §4").append(voted.getName()).append("§c.");
            } else
                message.append("a annulé son vote.");

            LG.getInstance().getGame().getSpectators().forEach(playerLG -> playerLG.sendMessage(message.toString()));

        } else  {
            StringBuilder message = new StringBuilder(LG.getPrefix() + secondColor + voter.getName() + " " + firstColor);

            if (voted != null) {
                if (voteChanged) message.append("change son vote pour ").append(secondColor).append(voted.getName()).append(firstColor).append(".");
                else message.append("vote pour ").append(secondColor).append(voted.getName()).append(firstColor).append(".");
            } else
                message.append("a annulé son vote.");

            Bukkit.broadcastMessage(message.toString());
        }
    }

    private void end() {
    }


    private PlayerLG getVote(PlayerLG voter) {
        return (PlayerLG) voter.getCache().get("vote");
    }

    private List<PlayerLG> getVotesFor(PlayerLG target) {
        List<PlayerLG> voters = new ArrayList<>();
        for (PlayerLG playerLG : this.getVoters())
            if (playerLG.getCache().get("vote").equals(target))
                voters.add(playerLG);
        return voters;
    }

    public void updateArmorStand(PlayerLG playerLG) {
        int votes = this.getVotesFor(playerLG).size();

        if (votes == 0) {
            playerLG.setArmorStand(null);
            return;
        }

        if (playerLG.getArmorStand() != null) {
            playerLG.getArmorStand().setCustomName(secondColor + "§l" + votes + " " + firstColor + "vote" + LG.getPlurial(votes));
            return;
        }

        ArmorStand as = (ArmorStand) Bukkit.getWorld("LG").spawnEntity(playerLG.getPlayer().getEyeLocation(), EntityType.ARMOR_STAND);

        as.setCustomNameVisible(true);
        as.setCustomName(secondColor + "§l" + votes + " " + firstColor + "vote" + LG.getPlurial(votes));
        as.setGravity(false);
        as.setSmall(true);
        as.setVisible(false);

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(as.getEntityId());
        for (PlayerLG aliveLG : LG.getInstance().getGame().getAlive())
            if (!this.observers.contains(aliveLG))
                ((CraftPlayer) aliveLG.getPlayer()).getHandle().playerConnection.sendPacket(packet);

        playerLG.setArmorStand(as);
    }


    public List<PlayerLG> getVotable() {
        return votable;
    }

    public List<PlayerLG> getVoters() {
        return voters;
    }
}
