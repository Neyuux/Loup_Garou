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
import java.util.Random;

public class Frere extends Role {

    public PlayerLG[] brothers;


    @Override
    public String getDisplayName() {
        return "§3§lFrère";
    }

    @Override
    public String getConfigName() {
        return "Frere";
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
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §3" + this.getTimeout() + " secondes§f pour parler avec vos frères.";
    }

    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        if (brothers != null) {

            if (brothers[0].getRole().getConfigName().equals(this.getConfigName()) && brothers[1].getRole().getConfigName().equals(this.getConfigName())) {
                playerLG.sendMessage(LG.getPrefix() + "§3Vos " + this.getDisplayName() + " §3sont §a§l" + brothers[0].getName() + " §3et §a§l" + brothers[1].getName());
                brothers[0].sendMessage(LG.getPrefix() + "§3Vos " + this.getDisplayName() + " §3sont §a§l" + brothers[1].getName() + " §3et §a§l" + playerLG.getName());
                brothers[1].sendMessage(LG.getPrefix() + "§3Vos " + this.getDisplayName() + " §3sont §a§l" + brothers[0].getName() + " §3et §a§l" + playerLG.getName());
            }

        } else {
            PlayerLG[] newbrothers = null;
            PlayerLG brother1 = null;

            while (newbrothers == null) {
                PlayerLG random = LG.getInstance().getGame().getPlayersInGame().get(new Random().nextInt(LG.getInstance().getGame().getPlayersInGame().size()));

                if (random.getRole() == null) {
                    Role newRole = null;

                    try {
                        for (Constructor<? extends Role> constructor : LG.getInstance().getGame().getConfig().getAddedRoles()) {
                            Role role = constructor.newInstance();

                            if (role.getConfigName().equals(this.getConfigName()) && !role.equals(this)) {
                                newRole = role;
                                break;
                            }
                        }

                        if (brother1 == null) brother1 = random;
                        else newbrothers = new PlayerLG[]{brother1, random};
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] Impossible de déterminer le role de la soeur de §b" + playerLG.getName() + "§c Veuillez appeler Neyuux_ ou réessayer plus tard.");
                    }
                    random.setRole(newRole);
                }
            }
            brothers = newbrothers;
        }
    }
}
