package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class Soeur extends Role {

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
        return null;
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
        return 35;
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

            if (getPlayers().contains(sister)) {
                playerLG.sendMessage(LG.getPrefix() + "§dVotre " + this.getDisplayName() + "§d est §a§l" + sister.getName());
                sister.sendMessage(LG.getPrefix() + "§dVotre " + this.getDisplayName() + " §dest §a§l" + sister.getName());
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
}
