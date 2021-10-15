package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.roles.Camps;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerLG {

    private static final HashMap<Player, PlayerLG> playerHashMap = new HashMap<>();

    public PlayerLG(Player player) {
        this.player = player;
    }

    private final Player player;

    private int place;

    private Camps camp;

    private boolean isDead;

    private List<PlayerLG> blacklistChoice = new ArrayList<>();


    public static PlayerLG createPlayerLG(Player player) {
        if (playerHashMap.containsKey(player))
            return playerHashMap.get(player);
        PlayerLG plg = new PlayerLG(player);
        playerHashMap.put(player, plg);
        return plg;
    }

    public static PlayerLG removePlayerLG(Player player) {
        return playerHashMap.remove(player);
    }
}
