package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.logging.Level;


public class MontreurDOurs extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lMontreur d'Ours";
    }

    @Override
    public String getConfigName() {
        return "Montreur d'Ours";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). A chaque matinée, votre ours grognera si un ou deux de vos voisins est un §c§lLoup-Garou§f.";
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
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }

    
    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersByRole(this.getClass())) {
            for (PlayerLG nearLG : playerLG.get2NearbyPlayers(true))
                if (nearLG.isLG()) {
                    Bukkit.broadcastMessage(LG.getPrefix() + "§6GRRRRRRRRRRRRRRRRRR !");

                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.WOLF_GROWL, 7f, 1.1f);

                    Bukkit.getLogger().log(Level.INFO, "Montreur " + playerLG.getName() + " growl " + nearLG.getName());
                }
        }
    }
}
