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

public class Soeur extends Role {

    public static final ChatLG CHAT = new ChatLG("§d", ChatColor.LIGHT_PURPLE, null);


    private PlayerLG sister;

    @Override
    public String getDisplayName() {
        return "§d§lSoeur";
    }

    @Override
    public String getConfigName() {
        return "Soeur";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Pendant la partie, votre soeur sera votre coéquipière, vous pouvez donc §9lui faire confiance§f.";
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
        return "§fVous avez §d" + this.getTimeout() + " secondes §fpour parler à votre soeur.";
    }

    


    public PlayerLG getSister() {
        return sister;
    }

    public void setSister(PlayerLG sister) {
        this.sister = sister;
    }

    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        if (sister != null) {

            if (LG.getInstance().getGame().getPlayersByRole(this.getClass()).contains(sister)) {
                playerLG.sendMessage(LG.getPrefix() + "§dVotre " + this.getDisplayName() + "§d est §a§l" + sister.getName());
                sister.sendMessage(LG.getPrefix() + "§dVotre " + this.getDisplayName() + " §dest §a§l" + playerLG.getName());
            }

        } else {
            PlayerLG newsister = null;
            while (newsister == null) {
                PlayerLG random = LG.getInstance().getGame().getPlayersInGame().get(new Random().nextInt(LG.getInstance().getGame().getPlayersInGame().size()));

                if (random.getRole() == null) {
                    this.sister = random;
                    newsister = random;
                    Soeur newRole = null;

                    try {
                        for (Constructor<? extends Role> constructor : LG.getInstance().getGame().getConfig().getAddedRoles()) {
                            Role role = constructor.newInstance();

                            if (role.getConfigName().equals(this.getConfigName()) && !role.equals(this)) {
                                newRole = (Soeur) role;
                                break;
                            }
                        }

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] Impossible de déterminer le role de la "+this.getConfigName()+" de §b" + playerLG.getName() + "§c Veuillez appeler Neyuux_ ou réessayer plus tard.");
                    }

                    if (newRole == null) {
                        System.out.println("lonely sister : " + playerLG.getName());
                        return;
                    }

                    newRole.setSister(playerLG);
                    newsister.setRole(newRole);
                }
            }
        }
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

        PlayerLG sisterLG1 = players.remove(0);
        Set<PlayerLG> sisters = new HashSet<>(Arrays.asList(sisterLG1, ((Soeur)sisterLG1.getRole()).getSister()));

        players.removeAll(sisters);
        sisters.removeIf(playerLG -> playerLG.isDead() || !playerLG.canUsePowers());

        if (sisters.size() != 2) {
            game.wait(this.getTimeout(), () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
            return;
        }

        CHAT.openChat(new HashSet<>(), sisters);

        for (PlayerLG sisterLG : sisters) {
            if (sisterLG.canUsePowers()) {
                sisterLG.setWake();
                sisterLG.sendMessage(LG.getPrefix() + this.getActionMessage());
            }
        }

        game.wait(this.getTimeout(), () -> {
            for (PlayerLG playableSister : sisters) {
                super.onPlayerTurnFinish(playableSister);
            }
            CHAT.closeChat();
            this.onNightTurn(callback);

        }, (currentPlayer, secondsLeft) -> (sisters.contains(currentPlayer)) ? "§9§lA toi de jouer !" : LG.getPrefix() + "§9§lAu tour " + this.getDeterminingName(), true);
    }
}
