package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Macon extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lMaçon";
    }

    @Override
    public String getConfigName() {
        return "Macon";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous et les autres §6§lMaçons§f vous reconnaissez entre-vous §8(car vous vous appellez tous Ricardo)§f ; vous pouvez donc avoir confiance en eux.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
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
        StringBuilder builder = new StringBuilder(LG.getPrefix() + "§dListe des maçons encore en jeu : \n");
        List<PlayerLG> macons = LG.getInstance().getGame().getPlayersByRole(this.getClass());

        macons.forEach(playerLG -> builder.append("§5§l - §d§l").append(playerLG.getName()));

        macons.forEach(playerLG -> playerLG.sendMessage(builder.toString()));
    }
}
