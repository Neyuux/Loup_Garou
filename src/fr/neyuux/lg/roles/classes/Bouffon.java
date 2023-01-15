package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteEndEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Bouffon extends Role {

    public static final List<PlayerLG> NEED_TO_PLAY = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "§d§lBouffon";
    }

    @Override
    public String getConfigName() {
        return "Bouffon";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est de vous faire voter par le village. Si vous réussissez : vous aurez la possibilité de tuer un des joueurs qui a voté pour vous.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d " + this.getTimeout() + " secondes§f pour tuer un joueur.";
    }


    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (NEED_TO_PLAY.isEmpty()) {
            callback.run();
            return;
        }

        PlayerLG playerLG = NEED_TO_PLAY.remove(0);
        if (!playerLG.getPlayer().isOnline()) {
            this.onNightTurn(callback);
            return;
        }


        playerLG.getPlayer().setGameMode(GameMode.ADVENTURE);
        playerLG.setWake();

        game.wait(this.getTimeout(), () -> {
            this.onPlayerTurnFinish(playerLG);
            this.onNightTurn(callback);
         }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : "§9§lAu tour " + this.getDeterminingName(), true);

        playerLG.sendMessage("" + this.getActionMessage());
        this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        playerLG.getPlayer().setGameMode(GameMode.SPECTATOR);
        super.onPlayerTurnFinish(playerLG);
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> choosable = new ArrayList<>();
        for (PlayerLG voterLG : LG.getInstance().getGame().getAlive())
            if (!voterLG.isDead() && ((List<PlayerLG>)playerLG.getCache().get("bouffonVoters")).contains(voterLG))
                choosable.add(voterLG);

        for (PlayerLG aliveLG : game.getAlive())
            if (!choosable.contains(aliveLG))
                playerLG.getPlayer().hidePlayer(aliveLG.getPlayer());

        if (game.getGameType().equals(GameType.MEETING)) {

            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    haunt(choosen, playerLG, choosable);
                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, choosable, new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§7Voulez-vous §5hanter " + paramPlayerLG.getNameWithAttributes(playerLG) + "§7 ?", "§7Il sera §céliminé§d de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    haunt(choosenLG, playerLG, choosable);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void haunt(PlayerLG choosen, PlayerLG playerLG, List<PlayerLG> choosable) {
        if (choosen == null) return;

        if (choosable.contains(choosen)) {
            RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

            Bukkit.getPluginManager().callEvent(roleChoiceEvent);
            if (roleChoiceEvent.isCancelled()) return;

            LG.getInstance().getGame().kill(choosen);

            playerLG.sendMessage(LG.getPrefix() + "§7Tu décides de hanter " + choosen.getNameWithAttributes(playerLG) + "§7.");
            GameLG.playPositiveSound(playerLG.getPlayer());

        } else {
            playerLG.sendMessage(LG.getPrefix() + "§cCe joueur n'a pas voté pour vous !");
            GameLG.playNegativeSound(playerLG.getPlayer());
        }
    }


    @EventHandler
    public void onCloseBouffonInv(InventoryCloseEvent ev) {
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
    public void onVoteStart(VoteStartEvent ev) {
        LG.getInstance().getGame().getPlayersByRole(this.getClass()).forEach(playerLG -> playerLG.getCache().put("bouffonVoters", new ArrayList<>()));
    }

    @EventHandler
    public void onVoteEnd(VoteEndEvent ev) {
        PlayerLG choosen = ev.getChoosen();

        if (ev.getVote().getName().equals("Vote du Village") && choosen.getRole() != null && choosen.getRole() instanceof Bouffon) {
            NEED_TO_PLAY.add(choosen);
            Bukkit.broadcastMessage(LG.getPrefix() + "§aLe " + this.getDisplayName() + "§d" + ev.getChoosen().getName() + "§a réussi son objectif !");
        }
    }

}
