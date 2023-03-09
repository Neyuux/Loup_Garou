package fr.neyuux.lg;

import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.VoteEndEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.classes.Ankou;
import fr.neyuux.lg.roles.classes.Bouffon;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class VoteLG {

    private final int timer;

    private final boolean randomIfEqual;

    private final GameLG.StringTimerMessage timerMessage;

    private final ChatColor firstColor;

    private final ChatColor secondColor;

    private Runnable callback;

    private final List<PlayerLG> votable;

    private final List<PlayerLG> voters;

    private final List<PlayerLG> observers;

    private PlayerLG choosen;

    private final String name;


    public VoteLG(String name, int timer, boolean randomIfEqual, GameLG.StringTimerMessage timerMessage, ChatColor firstColor, ChatColor secondColor, List<PlayerLG> votable, List<PlayerLG> voters, List<PlayerLG> observers) {
        this.timer = timer;
        this.randomIfEqual = randomIfEqual;
        this.timerMessage = timerMessage;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.votable = votable;
        this.voters = voters;
        this.observers = observers;
        this.name = name;

        List<PlayerLG> spectators = LG.getInstance().getGame().getSpectators();

        this.observers.addAll(spectators);
        this.voters.removeAll(spectators);
    }


    public void start(Runnable callback) {
        LG lg = LG.getInstance();
        GameLG game = lg.getGame();

        this.callback = callback;

        game.wait(timer, () -> this.end(false), timerMessage, true);
        game.setVote(this);

        Bukkit.getPluginManager().callEvent(new VoteStartEvent(this));

        for (PlayerLG voterLG : voters) {
            voterLG.getCache().put("vote", null);
            voterLG.setChoosing(choosen -> this.vote(voterLG, choosen));
            voterLG.setArmorStand(null);
            lg.getItemsManager().updateVoteItems(voterLG);
        }
    }

    public void vote(PlayerLG voter, PlayerLG voted) {
        GameLG game = LG.getInstance().getGame();

        if (!this.voters.contains(voter)) {
            voter.sendMessage(LG.getPrefix() + "§cVous ne pouvez pas voter.");
            GameLG.playNegativeSound(voter.getPlayer());
            return;
        }

        if (voted != null && !this.votable.contains(voted)) {
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

        int votes = 0;
        for (PlayerLG playerLG : this.getVoters())
            if (this.getVote(playerLG) != null)
                votes++;

        if (votes == this.getVoters().size() && game.getWaitTicksToSeconds() > timer / 8) {
            game.wait(timer / 8, () -> this.end(false), timerMessage, true);
        }

        if (voter.getRole() instanceof Ankou && voter.isDead()) {
            String name = voter.getRole().getDeterminingName().substring(3);
            StringBuilder message = new StringBuilder(LG.getPrefix() + name.substring(0, name.length() - 1).toUpperCase() + name.substring(1) + " §4" + voter.getName() + " §c");

            if (voted != null) {
                if (voteChanged) message.append("change son vote pour §4").append(voted.getName()).append("§c.");
                else message.append("vote pour §4").append(voted.getName()).append("§c.");
            } else
                message.append("a annulé son vote.");

            game.getSpectators().forEach(playerLG -> playerLG.sendMessage(message.toString()));

        } else  {
            StringBuilder message = new StringBuilder(LG.getPrefix() + secondColor + voter.getName() + " " + firstColor);

            if (voted != null) {
                if (voteChanged) message.append("change son vote pour ").append(secondColor).append(voted.getName()).append(firstColor).append(".");
                else message.append("vote pour ").append(secondColor).append(voted.getName()).append(firstColor).append(".");
            } else
                message.append("a annulé son vote.");

            this.sendObserversMessage(message.toString());
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void end(boolean cancel) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> killers = null;
        List<PlayerLG> finalists = new ArrayList<>();
        HashSet<PlayerLG> voted = new HashSet<>();
        StringBuilder builder = new StringBuilder();

        for (PlayerLG playerLG : this.votable) {
            List<PlayerLG> playerVoters = this.getVotesFor(playerLG);

            if (playerLG.getRole() instanceof Bouffon) playerLG.getCache().put("bouffonVoters", playerVoters);

            if (playerVoters.size() > 0) voted.add(playerLG);

            playerLG.setArmorStand(null);
            for (PlayerLG voter : playerVoters)
                if (voter != null && voter.getRole() instanceof Ankou && voter.isDead()) {
                    this.sendObserversMessage(LG.getPrefix() + "§4L'" + voter.getRole().getDisplayName() + " §cavait voté pour §4" + playerLG.getName() + "§c.");
                    voter.getCache().put("ankouVotes", (int)voter.getCache().get("ankouVotes") - 1);
                }
        }

        for (PlayerLG playerLG : voted) {
            if (finalists.isEmpty()) finalists.add(playerLG);
            else {
                int currentVotes = this.getVotesFor(finalists.get(0)).size();
                int playerLGVotes = this.getVotesFor(playerLG).size();

                if (currentVotes < playerLGVotes) {
                    finalists.clear();
                } else if (currentVotes <= playerLGVotes)
                    finalists.add(playerLG);
            }
        }

        if (!finalists.isEmpty())
            killers = this.getVotesFor(finalists.get(0));

        if (finalists.isEmpty() || this.getVotesFor(finalists.get(0)).isEmpty())
            cancel = true;

        for (PlayerLG playerLG : game.getPlayersInGame()) {
            if (playerLG.getCache().has("vote")) playerLG.getCache().remove("vote");
            playerLG.stopChoosing();
        }

        if (finalists.size() != 1) {
            if (cancel) {
                this.choosen = null;
                Bukkit.getPluginManager().callEvent(new VoteEndEvent(this, this.choosen));
               this.sendObserversMessage(LG.getPrefix() + "§cAucun vote n'a été enregistré pour un habitant. Il n'y aura donc aucune exécution.");
               this.callback.run();
               return;
            }
            builder.append(LG.getPrefix()).append(firstColor).append("Il y a égalité dans les votes ! ").append(secondColor).append(finalists.size()).append(firstColor).append(" joueurs ont obtenus le même nombre de votes : ");
            for (PlayerLG finalist : finalists)
                builder.append(secondColor).append(finalist.getName()).append(firstColor).append(", ");
            builder.deleteCharAt(builder.lastIndexOf(","));
            builder.append("\n");

            if (this.randomIfEqual) {
                this.choosen = finalists.get(new Random().nextInt(finalists.size()));
                builder.append("Un joueur va donc être chosi aléatoirement parmi ceux qui ont été le plus votés... \n").append(LG.getPrefix()).append(this.choosen.getDisplayName()).append(firstColor).append((" a été désigné."));
                game.cancelWait();
                this.callback.run();

            } else {
                if (game.getMayor() != null) {
                    builder.append(firstColor).append("Le §6Maire ").append(firstColor).append("a ").append(secondColor).append(30).append(" secondes").append(firstColor).append(" pour départager les joueurs.");

                    PlayerLG mayor = game.getMayor();

                    if (game.getGameType().equals(GameType.MEETING)) {
                        game.wait(30, () -> {
                            mayor.stopChoosing();
                            mayor.getPlayer().closeInventory();

                            Bukkit.broadcastMessage(LG.getPrefix() + "§cLe Maire a mis trop de temps à choisir. Il n'y aura donc aucune exécution aujourd'hui.");

                            Bukkit.getPluginManager().callEvent(new VoteEndEvent(this, this.choosen));
                            game.cancelWait();
                            this.callback.run();
                            game.setVote(null);
                        },(player, secondsLeft) -> (mayor == player) ? (LG.getPrefix() + "§eTu as §6§l" + secondsLeft + " §6seconde" + LG.getPlurial(secondsLeft) + "§e pour faire ton choix !") : (LG.getPrefix() + "§eLe maire fait son choix..."), true);

                        mayor.setChoosing(choosen -> {
                            if (choosen != null)
                                if (!finalists.contains(choosen)) {
                                    mayor.sendMessage(LG.getPrefix() + "§cCe joueur n'est pas concerné !");
                                    GameLG.playNegativeSound(mayor.getPlayer());

                                } else {
                                    this.setChoosen(choosen);

                                    Bukkit.getPluginManager().callEvent(new VoteEndEvent(this, this.choosen));
                                    game.cancelWait();
                                    this.callback.run();
                                    game.setVote(null);
                                    mayor.stopChoosing();
                                }
                        });

                    } else if (game.getGameType().equals(GameType.FREE)) {
                        new ChoosePlayerInv("§eDépartager les Votes", mayor, finalists, new ChoosePlayerInv.ActionsGenerator() {

                            @Override
                            public String[] generateLore(PlayerLG paramPlayerLG) {
                                return new String[] {firstColor + "Chosis le joueur " + secondColor + paramPlayerLG.getNameWithAttributes(mayor) + firstColor + ".",firstColor + "Si vous le choisissez, il sera §céliminé" + firstColor + " de la partie.", "", "§7>>Cliquer pour choisir"};
                            }

                            @Override
                            public void doActionsAfterClick(PlayerLG choosenLG) {
                                mayor.getCache().put("unclosableInv", false);
                                mayor.getPlayer().closeInventory();

                                VoteLG.this.setChoosen(choosenLG);

                                Bukkit.broadcastMessage(LG.getPrefix() + "§eLe Maire a porté son choix sur §6" + choosenLG.getName() + "§e.");

                                Bukkit.getPluginManager().callEvent(new VoteEndEvent(VoteLG.this, VoteLG.this.choosen));
                                game.cancelWait();
                                VoteLG.this.callback.run();
                                game.setVote(null);
                            }
                        });
                        mayor.getCache().put("unclosableInv", true);
                    }

                    this.sendObserversMessage(builder.toString());
                    for (PlayerLG playerLG : this.getVoters())
                        game.getItemsManager().updateStartItems(playerLG);

                } else {
                    builder.append("Un second vote va débuter pour départager les joueurs à égalité. \n");
                    VoteLG secondVote = new VoteLG("Vote du Village", 70, true, (playerLG, secondsLeft) -> {
                        if (playerLG.getCache().has("vote"))
                            if (playerLG.getCache().get("vote") == null)
                                return LG.getPrefix() + "§eVous ne votez pour §6§lpersonne§e.";
                            else
                                return LG.getPrefix() + "§eVous votez pour §6§l" + ((PlayerLG)playerLG.getCache().get("vote")).getName() + "§e.";

                        return null;
                    }, firstColor, secondColor, finalists, VoteLG.this.voters, this.observers);

                    secondVote.start(() -> {
                        VoteLG.this.choosen = secondVote.getChoosen();
                        game.cancelWait();
                        this.callback.run();
                        game.setVote(null);
                    });
                    this.sendObserversMessage(builder.toString());
                    this.observers.forEach(playerLG -> playerLG.sendTitle("§eSecond vote du Village !", "§6Départagez les joueurs à égalité.", 10, 90, 10));
                }
                return;
            }
        } else {
            PlayerLG eliminated = finalists.get(0);

            builder.append(LG.getPrefix()).append(firstColor).append("Le vote possède un résultat : ").append(secondColor).append(eliminated.getName()).append(" ").append(firstColor).append("a obtenu le plus de votes(").append(secondColor).append(killers.size()).append(firstColor).append("). Il a donc été choisi.");
            this.choosen = eliminated;
        }

        Bukkit.getPluginManager().callEvent(new VoteEndEvent(this, this.choosen));
        game.cancelWait();
        this.sendObserversMessage(builder.toString());
        this.callback.run();
        game.setVote(null);
    }

    public void forceStop() {
        GameLG game = LG.getInstance().getGame();

        for (PlayerLG playerLG : this.voters) {
            game.getItemsManager().updateStartItems(playerLG);
            playerLG.getCache().remove("vote");
            playerLG.stopChoosing();
        }

        this.votable.forEach(playerLG -> playerLG.setArmorStand(null));

        game.cancelWait();
        game.setVote(null);
    }


    private PlayerLG getVote(PlayerLG voter) {
        return (PlayerLG) voter.getCache().get("vote");
    }

    private List<PlayerLG> getVotesFor(PlayerLG target) {
        List<PlayerLG> voters = new ArrayList<>();
        for (PlayerLG playerLG : this.getVoters())
            if (playerLG.getCache().has("vote") && playerLG.getCache().get("vote") != null && playerLG.getCache().get("vote").equals(target))
                voters.add(playerLG);

        if (target.getCache().has("corbeauTargeted")) {
            voters.add(null);
            voters.add(null);
        }
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

    public List<PlayerLG> getVoters() {
        return voters;
    }

    public PlayerLG getChoosen() {
        return choosen;
    }

    public void setChoosen(PlayerLG choosen) {
        this.choosen = choosen;
    }

    public ChatColor getFirstColor() {
        return firstColor;
    }

    public ChatColor getSecondColor() {
        return secondColor;
    }

    public List<PlayerLG> getVotable() {
        return votable;
    }

    public List<PlayerLG> getObservers() {
        return observers;
    }

    public String getName() {
        return name;
    }

    public Runnable getCallback() {
        return callback;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    private void sendObserversMessage(String message) {
        this.observers.forEach(playerLG -> playerLG.sendMessage(message));
    }
}
