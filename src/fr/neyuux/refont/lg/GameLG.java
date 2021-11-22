package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.chat.ChatLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.LoupGarouBlanc;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GameLG implements Listener {

    private GameState gameState;

    private int day = 0;

    private int night = 0;

    private PlayerLG mayor;

    private final ArrayList<Role> rolesAtStart = new ArrayList<>();

    private final ArrayList<Role> aliveRoles = new ArrayList<>();

    private final ArrayList<PlayerLG> playersInGame = new ArrayList<>();

    private ChatLG spectatorChat = new ChatLG((sender, msg) -> {
        return sender.getDisplayName() + " §8» §7" + msg;
    });

    private ChatLG dayChat = new ChatLG((sender, msg) -> {
        return "§e" + sender.getName() + " §6» §f" + msg;
    });


    public GameLG() {
        Bukkit.getPluginManager().registerEvents(this, LG.getInstance());
    }


    public void sendMessage(Role role, String msg) {
        for (PlayerLG playerLG : this.getAlive()) {
            if (role != null) {
                if (role.getPlayers().contains(playerLG))
                    playerLG.sendMessage(msg);
            } else playerLG.sendMessage(msg);
        }
    }

    public void sendActionBar(String msg) {
        for (PlayerLG playerLG : this.playersInGame) {
            playerLG.sendActionBar(msg);
        }
    }

    public List<PlayerLG> getAlive() {
        ArrayList<PlayerLG> alive = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (playerLG.isDead())
                alive.add(playerLG);

        return alive;
    }

    public List<PlayerLG> getLGs() {
        ArrayList<PlayerLG> lgs = new ArrayList<>();

        for (PlayerLG playerLG : this.playersInGame)
            if (playerLG.getCamp().equals(Camps.LOUP_GAROU)
                || playerLG.getRole().getClass().equals(LoupGarouBlanc.class))
                lgs.add(playerLG);

        return lgs;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getDay() {
        return day;
    }

    public void addDay() {
        this.day += 1;
    }

    public int getNight() {
        return night;
    }

    public void addNight() {
        this.night += 1;
    }

    public PlayerLG getMayor() {
        return mayor;
    }

    public void setMayor(PlayerLG mayor) {
        this.mayor = mayor;
    }

    public ArrayList<Role> getRolesAtStart() {
        return rolesAtStart;
    }

    public ArrayList<Role> getAliveRoles() {
        return aliveRoles;
    }
}