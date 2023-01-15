package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.ChatLG;
import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Frere extends Role {

    private final HashSet<PlayerLG> brothers = new HashSet<>();

    public static final ChatLG CHAT = new ChatLG("§d", ChatColor.DARK_AQUA, null);


    @Override
    public String getDisplayName() {
        return "§3§lFrère";
    }

    @Override
    public String getConfigName() {
        return "Frere";
    }

    @Override
    public String getDeterminingName() {
        return "des " + this.getDisplayName() + "s";
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Pendant la partie, vos deux frères seront vos coéquipiers, vous pouvez donc §9leur faire confiance§f.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §3" + this.getTimeout() + " secondes§f pour parler avec vos frères.";
    }

    


    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        if (!this.brothers.isEmpty()) {
            playerLG.sendMessage(getBrothersMessage(playerLG, this.brothers));
            for (PlayerLG brotherLG : this.brothers)
                brotherLG.sendMessage(getBrothersMessage(brotherLG, ((Frere)brotherLG.getRole()).getBrothers()));

        } else {

            while (this.brothers.size() != 2) {
                PlayerLG random = LG.getInstance().getGame().getPlayersInGame().get(new Random().nextInt(LG.getInstance().getGame().getPlayersInGame().size()));

                if (random.getRole() == null) {
                    Frere newRole = null;

                    try {
                        for (Constructor<? extends Role> constructor : LG.getInstance().getGame().getConfig().getAddedRoles()) {
                            Role role = constructor.newInstance();

                            if (role.getConfigName().equals(this.getConfigName()) && !role.equals(this)) {
                                newRole = (Frere) role;
                                break;
                            }
                        }

                        if (newRole == null) return;

                        this.brothers.add(random);
                        System.out.println("add " + playerLG.getName() + " brother : " + random.getName());

                        newRole.getBrothers().add(playerLG);

                        for (PlayerLG brotherLG : this.brothers)
                            if (!brotherLG.equals(random))
                                newRole.getBrothers().add(brotherLG);

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] Impossible de déterminer le role du frere de §b" + playerLG.getName() + "§c Veuillez appeler Neyuux_ ou réessayer plus tard.");
                    }
                    random.setRole(newRole);
                }
            }
        }
    }

    private static String getBrothersMessage(PlayerLG playerLG, HashSet<PlayerLG> brothers) {
        StringBuilder sb = new StringBuilder(LG.getPrefix() + "§3Vos " + playerLG.getRole().getDisplayName() + " §3sont §a§l");
        for (PlayerLG brotherLG : brothers)
            sb.append(brotherLG.getName()).append("  ");
        playerLG.sendMessage(sb.toString());

        return sb.toString();
    }


    public HashSet<PlayerLG> getBrothers() {
        return this.brothers;
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

        PlayerLG brotherLG1 = players.remove(0);
        Set<PlayerLG> playableBrothers = new HashSet<>(this.getBrothers());
        playableBrothers.add(brotherLG1);

        players.removeIf(playerLG -> playerLG.isDead() || !playerLG.canUsePowers());

        if (playableBrothers.isEmpty()) {
            game.wait(this.getTimeout(), () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
            return;
        }

        CHAT.openChat(new HashSet<>(), new HashSet<>(playableBrothers));

        for (PlayerLG brotherLG : brothers) {
            if (brotherLG.canUsePowers()) {
                brotherLG.setWake();
                brotherLG.sendMessage(LG.getPrefix() + this.getActionMessage());
            }
        }

        game.wait(this.getTimeout(), () -> {
            for (PlayerLG brother : playableBrothers) super.onPlayerTurnFinish(brother);
            CHAT.closeChat();
            this.onNightTurn(callback);

        }, (currentPlayer, secondsLeft) -> (playableBrothers.contains(currentPlayer)) ? "§9§lA toi de jouer !" : LG.getPrefix() + "§9§lAu tour " + this.getDeterminingName(), true);
    }
}
