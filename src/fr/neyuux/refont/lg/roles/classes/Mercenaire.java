package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Mercenaire extends Role {

    public Mercenaire() {
        super(
                "�c�lMerce�5�lnaire",
                "Mercenaire",
                "�fVous �tes �c�lMerce�5�lnaire�f, le premier jour, votre objectif est d'�liminer la cible qui vous est attribu�e. Si vous y parvenez, vous gagnez seul la partie instantan�ment. Sinon, vous devenez �e�lSimple �a�lVillageois�f.",
                RoleEnum.MERCENAIRE,
                Camps.VILLAGE,
                Decks.WOLFY);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
