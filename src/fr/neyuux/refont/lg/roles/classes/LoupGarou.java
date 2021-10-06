package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class LoupGarou extends Role {

    public LoupGarou() {
        super("§c§lLoup-Garou",
                "§c§lLoup-Garou",
                "Loup-Garou",
                "§fVous êtes §c§lLoup-Garou§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer...",
                RoleEnum.LOUP_GAROU,
                Camps.LOUP_GAROU,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }

}
