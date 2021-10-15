package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class SimpleVillageois extends Role {

    public SimpleVillageois() {
        super(
                "�e�lSimple �a�lVillageois",
                "Simple Villageois",
                "�fVous �tes �e�lSimple �a�lVillageois�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous n'avez �9aucun pouvoir�f particulier.",
                RoleEnum.SIMPLE_VILLAGEOIS,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
