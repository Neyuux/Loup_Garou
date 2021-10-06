package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Comedien extends Role {

    public Comedien() {
        super("§5§lComédien",
                "§5§lComédien",
                "Comédien",
                "§fVous êtes §5§lComédien§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous obtiendrez §93 pouvoirs de rôles villageois§f que vous pourrez utiliser une fois chacun pendant la partie.",
                RoleEnum.COMEDIEN,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
