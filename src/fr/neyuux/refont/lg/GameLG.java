package fr.neyuux.refont.lg;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class GameLG implements Listener {

    private GameState gameState;

    public GameLG() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }


    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}