package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class MamieGrincheuse extends Role {

    public MamieGrincheuse(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "�d�lMamie �6�lGrincheuse";
    }

    @Override
    public String getScoreboardName() {
        return "�d�lMamie �6�lGrincheuse";
    }

    @Override
    public String getConfigName() {
        return "Mamie Grincheuse";
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez choisir un joueur, l'emp�chant de voter au jour suivant ; mais vous ne pouvez pas s�lectionner deux fois de suite la m�me personne.";
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
        return 0;
    }

    @Override
    public String getActionMessage() {
        return null;
    }
}
