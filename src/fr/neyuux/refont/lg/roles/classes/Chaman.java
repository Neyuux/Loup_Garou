package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
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
            if (!game.isThiefRole(this)) {
                callback.run();
                return;
            } else {
                createFakeTurn(callback);
            }
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

            }, (currentPlayerLG, secondsLeft) -> (listeningChamans.contains(currentPlayerLG)) ? "§a§lTu écoutes les morts..." : "§9§lAu tour " + this.getDeterminingName());

            listeningChamans.forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + this.getActionMessage()));

        } else {
            createFakeTurn(callback);
        }
    }
}
