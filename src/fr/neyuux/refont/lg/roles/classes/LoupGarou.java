package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class LoupGarou extends Role {

    public LoupGarou() {
        super("�c�lLoup-Garou",
                "�c�lLoup-Garou",
                "Loup-Garou",
                "�fVous �tes �c�lLoup-Garou�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer...",
                RoleEnum.LOUP_GAROU,
                Camps.LOUP_GAROU,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }

}
