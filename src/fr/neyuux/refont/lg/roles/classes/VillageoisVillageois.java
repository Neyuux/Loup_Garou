package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class VillageoisVillageois extends Role {

    public VillageoisVillageois() {
        super("�a�lVillageois�e-�a�lVillageois",
                "�a�lVillageois�e-�a�lVillageois",
                "Villageois-Villageois",
                "�fVous �tes �a�lVillageois�e-�a�lVillageois�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Vous n'avez pas de pouvoir particulier, cependant, �2tout le monde connait votre identit�f...",
                RoleEnum.VILLAGEOIS_VILLAGEOIS,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
