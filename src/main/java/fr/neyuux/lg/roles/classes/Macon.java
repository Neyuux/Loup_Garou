package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Macon extends Role {

    @Override
    public String getDisplayName() {
        return "�6�lMa�on";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous et les autres �6�lMa�ons�f vous reconnaissez entre-vous �8(car vous vous appellez tous Ricardo)�f ; vous pouvez donc avoir confiance en eux.";
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
    public void onNightStart(NightStartEvent ev) {
        if (LG.getInstance().getGame().getNight() != 1) return;

        StringBuilder builder = new StringBuilder(LG.getPrefix() + "�dListe des ma�ons encore en jeu : \n");
        List<PlayerLG> macons = LG.getInstance().getGame().getPlayersByRole(this.getClass());

        macons.forEach(playerLG -> builder.append("�5�l - �d�l").append(playerLG.getName()));

        macons.forEach(playerLG -> playerLG.sendMessage(builder.toString()));
    }

    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        StringBuilder builder = new StringBuilder(LG.getPrefix() + "�dListe des ma�ons encore en jeu : \n");
        List<PlayerLG> macons = LG.getInstance().getGame().getPlayersByRole(this.getClass());

        macons.forEach(playerLG -> builder.append("�5�l - �d�l").append(playerLG.getName()));

        macons.forEach(playerLG -> playerLG.sendMessage(builder.toString()));
    }
}
