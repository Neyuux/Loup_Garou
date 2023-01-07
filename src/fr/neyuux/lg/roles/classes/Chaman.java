package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class Chaman extends Role {

    private final List<PlayerLG> listeningChamans = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "§b§lCha§a§lman";
    }

    @Override
    public String getConfigName() {
        return "Chaman";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pour §9voir les messages des morts§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.ONLINE;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §b" + this.getTimeout() + " seconds§f pour écouter les morts.";
    }


    @Override
    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> players = game.getPlayersByRole(this.getClass());

        game.cancelWait();

        if (players.isEmpty()) {
            if (game.isNotThiefRole(this)) callback.run();
            else LG.getInstance().getGame().wait(this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
            return;
        }

        for (PlayerLG chaman : players)
            if (chaman.canUsePowers())
                listeningChamans.add(chaman);

        if (!listeningChamans.isEmpty()) {

            listeningChamans.forEach(PlayerLG::setWake);

            game.wait(this.getTimeout(), () -> {
                listeningChamans.forEach(PlayerLG::setSleep);
                listeningChamans.clear();
                super.onNightTurn(callback);

            }, (currentPlayerLG, secondsLeft) -> (listeningChamans.contains(currentPlayerLG)) ? "§a§lTu écoutes les morts..." : "§9§lAu tour " + this.getDeterminingName(), true);

            listeningChamans.forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + this.getActionMessage()));

        } else {
            LG.getInstance().getGame().wait(this.getTimeout() / 4, () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onChamanChat(AsyncPlayerChatEvent ev) {
        if (!listeningChamans.isEmpty()) {
            ev.setCancelled(false);
            PlayerLG senderLG = PlayerLG.createPlayerLG(ev.getPlayer());
            GameLG game = LG.getInstance().getGame();

            if (senderLG.isSpectator()) {
                ev.getRecipients().clear();
                game.getSpectators().forEach(playerLG -> ev.getRecipients().add(playerLG.getPlayer()));
                listeningChamans.forEach(playerLG -> ev.getRecipients().add(playerLG.getPlayer()));
                ev.setFormat("§7§lSpectateur §7" + senderLG.getName() + " §8» §b" + ev.getMessage());
            }

            if (listeningChamans.contains(senderLG) && (boolean)game.getConfig().getChamanChat().getValue()) {
                ev.getRecipients().clear();
                game.getSpectators().forEach(playerLG -> ev.getRecipients().add(playerLG.getPlayer()));
                ev.getRecipients().add(senderLG.getPlayer());
                ev.setFormat(this.getDisplayName() + " §e" + senderLG.getName() + " §8» §a" + ev.getMessage());
            }
        }
    }
}
