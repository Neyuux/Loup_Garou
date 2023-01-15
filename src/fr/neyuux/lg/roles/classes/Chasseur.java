package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.DayEndEvent;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Chasseur extends Role {

    public static final List<PlayerLG> NEED_TO_PLAY = new ArrayList<>();

    @Override
    public String getDisplayName() {
        return "§2§lChasseur";
    }

    @Override
    public String getConfigName() {
        return "Chasseur";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). A votre mort, vous dégainerez votre fusil et avec la dernière balle de votre chargeur, vous pourrez §9emmener quelqu'un dans la mort avec vous§f.";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §2" + this.getTimeout() + " secondes §fpour tirer sur quelqu'un.";
    }


    @Override
    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (NEED_TO_PLAY.isEmpty()) {
            callback.run();
            return;
        }

        PlayerLG playerLG = NEED_TO_PLAY.remove(0);

        if (playerLG.canUsePowers()) {

            playerLG.setWake();
            playerLG.getPlayer().setGameMode(GameMode.ADVENTURE);
            playerLG.getPlayer().teleport(playerLG.getPlacement());

            game.wait(Chasseur.this.getTimeout(), () -> {
                this.onPlayerTurnFinish(playerLG);
                this.onNightTurn(callback);

            }, (currentPlayer, secondsLeft) ->(currentPlayer == playerLG) ? "§9§lA toi de jouer !" : LG.getPrefix() + "§9§lAu tour " + Chasseur.this.getDeterminingName(), true);

            playerLG.sendMessage(LG.getPrefix() + Chasseur.this.getActionMessage());
            Chasseur.this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));

        } else {
            callback.run();
        }
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && !choosen.isDead()) {
                    fire(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§fVoulez-vous §2tirer sur " + paramPlayerLG.getNameWithAttributes(playerLG) + "§f ?", "§fIl sera éliminé de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    fire(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.sendMessage(LG.getPrefix() + "§cVous avez mit trop de temps !");
        Bukkit.broadcastMessage(LG.getPrefix() + "§2Le " + this.getDisplayName() + " §2a décidé de ne pas tirer.");
        super.onPlayerTurnFinish(playerLG);
    }


    private void fire(PlayerLG choosen, PlayerLG playerLG) {
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        Bukkit.broadcastMessage(LG.getPrefix() + "§2Dans un dernier souffle, le " + this.getDisplayName() + " §b" + playerLG.getName() + " §2a tiré sur §e" + choosen.getName() + "§2.");

        choosen.eliminate();
    }


    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        if (!NEED_TO_PLAY.isEmpty()) {
            ev.setCancelled(true);
            this.onNightTurn(() -> LG.getInstance().getGame().getGameRunnable().endNight());
        }
    }

    @EventHandler
    public void onDayEnd(DayEndEvent ev) {
        if (!NEED_TO_PLAY.isEmpty()) {
            ev.setCancelled(true);
            this.onNightTurn(() -> LG.getInstance().getGame().getGameRunnable().endDay(null));
        }
    }

    @EventHandler
    public void onElimination(PlayerEliminationEvent ev) {
        if (ev.getEliminated().getRole() instanceof Chasseur)
            NEED_TO_PLAY.add(ev.getEliminated());
    }
}
