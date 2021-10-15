package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class PetiteFille extends Role {

    public PetiteFille() {
        super(
                "�9�lPetite �b�lFille",
                "Petite Fille",
                "�fVous �tes �9�lPetite �b�lFille�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Chaque nuit, � la lev�e des Loups-Garous : vous pourrez �9espionner leurs messages�f.",
                RoleEnum.PETITE_FILLE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
