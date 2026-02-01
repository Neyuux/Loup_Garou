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
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Fossoyeur extends Role {

    private static final List<PlayerLG> NEED_TO_PLAY = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "§8§lFossoyeur";
    }

    @Override
    public String getConfigName() {
        return "Fossoyeur";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 2;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). A votre mort, vous pourrez choisir un joueur ; et dans le chat, sera indiqué le pseudo de ce joueur et le pseudo d'un autre joueur d'un §9camp différent§f du premier.";
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
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §3" + this.getTimeout() + " secondes §fpour choisir la personne à qui vous voulez creuser la tombe.";
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

            game.wait(Fossoyeur.this.getTimeout(), () -> {
                this.onPlayerTurnFinish(playerLG);
                this.onNightTurn(callback);

            }, (currentPlayer, secondsLeft) ->(currentPlayer == playerLG) ? "§9§lA toi de jouer !" : LG.getPrefix() + "Au tour " + Fossoyeur.this.getDeterminingName(), true);

            playerLG.sendMessage(LG.getPrefix() + Fossoyeur.this.getActionMessage());
            Fossoyeur.this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));

        } else {
            callback.run();
        }
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && !choosen.isDead()) {
                    dig(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§7Voulez-vous §ccreuser la tombe de " + paramPlayerLG.getNameWithAttributes(playerLG) + "§7 ?", "§7La comparaison entre son camp et celui d'un.", "§7autre joueur aléatoire sera révélée dans le chat.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    dig(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.sendMessage(LG.getPrefix() + "§cVous avez mit trop de temps !");
        Bukkit.broadcastMessage(LG.getPrefix() + "§8Le " + this.getDisplayName() + " §8a décidé de ne pas creuser de tombe.");
        super.onPlayerTurnFinish(playerLG);
    }


    private void dig(PlayerLG choosen, PlayerLG playerLG) {
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        List<PlayerLG> aliveexcept = LG.getInstance().getGame().getAliveExcept(choosen, playerLG);
        PlayerLG randomLG = aliveexcept.get(LG.RANDOM.nextInt(aliveexcept.size()));

        Bukkit.broadcastMessage(LG.getPrefix() + "§8Dans un dernier souffle, le " + this.getDisplayName() + " §b" + playerLG.getName() + " §8a creuse la tombe de §e" + choosen.getName() + "§8 et §e" + randomLG.getName() + "§8.");

        if (choosen.getCamp().equals(randomLG.getCamp()))
            Bukkit.broadcastMessage(LG.getPrefix() + "§8Les deux joueurs dont la tombe a été creusée sont §a§ldu MÊME CAMP §8!");
        else
            Bukkit.broadcastMessage(LG.getPrefix() + "§8Les deux joueurs dont la tombe a été creusée ne sont §c§lPAS DU MÊME CAMP §8!");

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.DIG_GRASS, 8f, 1f));
    }


    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        if (!NEED_TO_PLAY.isEmpty()) {
            ev.setCancelled(true);
            this.onNightTurn(() -> LG.getInstance().getGame().getGameRunnable().nextDay());
        }
    }

    @EventHandler
    public void onDayEnd(DayEndEvent ev) {
        if (!NEED_TO_PLAY.isEmpty()) {
            ev.setCancelled(true);
            this.onNightTurn(() -> LG.getInstance().getGame().getGameRunnable().nextDay());
        }
    }

    @EventHandler
    public void onElimination(PlayerEliminationEvent ev) {
        if (ev.getEliminated().getRole() instanceof Fossoyeur)
            NEED_TO_PLAY.add(ev.getEliminated());
    }
}
