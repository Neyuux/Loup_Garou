package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class PetiteFilleWO extends Role {

    @Override
    public String getDisplayName() {
        return "�9�lPetite �b�lFille �0�oWO";
    }

    @Override
    public String getConfigName() {
        return "Petite Fille (WO)";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez ouvrir les yeux, si vous le faites, vous aurez 20% de chance de �9trouver un Loup�f et 5% de chance de �9mourir�f.";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �b" + this.getDisplayName() + " secondes�f pour choisir d'aller espionner ou non.";
    }
}
