package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.chat.ChatLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Soeur extends Role {

    public static final ChatLG CHAT = new ChatLG("�d", ChatColor.LIGHT_PURPLE, null);


    private PlayerLG sister;

    @Override
    public String getDisplayName() {
        return "�d�lSoeur";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Pendant la partie, votre soeur sera votre co�quipi�re, vous pouvez donc �9lui faire confiance�f.";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �d" + this.getTimeout() + " secondes �fpour parler � votre soeur.";
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
                playerLG.sendMessage(LG.getPrefix() + "�dVotre " + this.getDisplayName() + "�d est �a�l" + sister.getName());
                sister.sendMessage(LG.getPrefix() + "�dVotre " + this.getDisplayName() + " �dest �a�l" + sister.getName());
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
                        Bukkit.broadcastMessage(LG.getPrefix() + "�4[�cErreur�4] Impossible de d�terminer le role de la "+this.getConfigName()+" de �b" + playerLG.getName() + "�c Veuillez appeler Neyuux_ ou r�essayer plus tard.");
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
        PlayerLG[] sisters = new PlayerLG[]{sisterLG1, ((Soeur)sisterLG1.getRole()).getSister()};
        List<PlayerLG> playableSisters = Arrays.stream(sisters).filter(PlayerLG::canUsePowers).collect(Collectors.toList());

        players.remove(sisters[1]);

        if (playableSisters.isEmpty()) {
            game.wait(this.getTimeout(), () -> onNightTurn(callback), (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
            return;
        }

        CHAT.openChat(playableSisters, playableSisters);

        for (PlayerLG sisterLG : sisters) {
            if (sisterLG.canUsePowers()) {
                sisterLG.setWake();
                sisterLG.sendMessage(LG.getPrefix() + this.getActionMessage());
            }
        }

        game.wait(this.getTimeout(), () -> {
            for (PlayerLG playableSister : playableSisters) {
                super.onPlayerTurnFinish(playableSister);
            }
            CHAT.closeChat();
            this.onNightTurn(callback);

        }, (currentPlayer, secondsLeft) -> (playableSisters.contains(currentPlayer)) ? "�9�lA toi de jouer !" : LG.getPrefix() + "�9�lAu tour " + this.getDeterminingName(), true);
    }
}
