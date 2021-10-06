package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class GrandMechantLoup extends Role {

    public GrandMechantLoup() {
        super("�4�lGrand M�chant �c�lLoup",
                "�4�lGrand M�chant �c�lLoup",
                "Grand M�chant Loup",
                "�fVous �tes �4�lGrand M�chant �c�lLoup�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer. Mais apr�s cela, vous vous r�veillez une deuxi�me fois pour �9d�vorer une deuxi�me personne�f...",
                RoleEnum.GRAND_MECHANT_LOUP,
                Camps.LOUP_GAROU,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
