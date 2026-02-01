package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;

public class VillageoisVillageois extends Role {

    @Override
    public String getDisplayName() {
        return "§a§lVillageois§e-§a§lVillageois";
    }

    @Override
    public String getConfigName() {
        return "Villageois-Villageois";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Vous n'avez pas de pouvoir particulier, cependant, §2tout le monde connait votre identité§f...";
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

    

    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);

        Bukkit.broadcastMessage(LG.getPrefix() + "§aLe " + this.getDisplayName() + " §ade la partie est §e§l" + playerLG.getName());
    }
}
