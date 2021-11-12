package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class HumainMaudit extends Role {

    public HumainMaudit(GameLG gameLG)  {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�e�lHumain �c�lMaudit";
    }

    @Override
    public String getConfigName() {
        return "Humain Maudit";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Si vous vous faites cibler par les Loups, vous ne mourrez pas et devenez l'un d'entre-eux.";
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
}
