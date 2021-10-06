package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class InfectPereDesLoups extends Role {

    public InfectPereDesLoups() {
        super("§2§lInfect §8§lPère §cdes §lLoups",
                "§2§lInfect §8§lPère §cdes §lLoups",
                "Infect Pere Des Loups",
                "§fVous êtes §2§lInfect §8§lPère §cdes §lLoups§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Après cela, vous vous réveillez et §9choisissez si votre victime deviendra §c§lLoup-Garou§f (elle gardera également ses pouvoirs de villageois si elle en a un).",
                RoleEnum.INFECT_PERE_DES_LOUPS,
                Camps.LOUP_GAROU,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
