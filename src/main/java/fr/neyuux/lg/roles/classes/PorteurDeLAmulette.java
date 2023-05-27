package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

public class PorteurDeLAmulette extends Role {

    @Override
    public String getDisplayName() {
        return "�6�lPorteur de �d�lL'Amulette";
    }

    @Override
    public String getConfigName() {
        return "Porteur de L'Amulette";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous ne pouvez pas mourir des Loups.";
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
    public void onChoiceLG(RoleChoiceEvent ev) {
        GameLG game = LG.getInstance().getGame();
        PlayerLG choosen = ev.getChoosen();

        if (ev.getRole().getClass().equals(LoupGarou.class) && game.getPlayersByRole(this.getClass()).contains(choosen)) {
            ev.setCancelled(true);

            game.sendMessage(LoupGarou.class, "�d�l" + choosen.getName() + " �c n'a pas pu �tre tu�.");
        }
    }
}
