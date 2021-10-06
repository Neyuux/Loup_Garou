package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Cupidon extends Role {

    public Cupidon() {
        super("§9§lCupi§d§ldon",
                "§9§lCupi§d§ldon",
                "Cupidon",
                "§fVous êtes §9§lCupi§d§ldon§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous pouvez §9sélectionner 2 joueurs§f pour qu'ils deviennent le §d§lCouple§f de cette partie. Ils devront gagner ensemble ou avec leur camp d'origine (s'ils sont ensemble) ; et si l'un d'entre eux meurt, l'autre se suicidera d'un chagrin d'amour.",
                RoleEnum.CUPIDON,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
