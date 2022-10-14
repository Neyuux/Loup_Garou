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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Frere extends Role {

    private final List<PlayerLG> brothers = new ArrayList<>();


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
        return null;
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

    private static String getBrothersMessage(PlayerLG playerLG, List<PlayerLG> brothers) {
        StringBuilder sb = new StringBuilder(LG.getPrefix() + "§3Vos " + playerLG.getRole().getDisplayName() + " §3sont §a§l");
        for (PlayerLG brotherLG : brothers)
            sb.append(brotherLG.getName()).append("  ");
        playerLG.sendMessage(sb.toString());

        return sb.toString();
    }


    public List<PlayerLG> getBrothers() {
        return this.brothers;
    }
}
