package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Voleur extends Role {

    public Voleur() {
        super("�3�lVoleur",
                "�3�lVoleur",
                "Voleur",
                "�fVous �tes �3�lVoleur�f, au d�but de la partie, vous allez devoir �9choisir �fentre les deux r�les qui n'ont pas �t� distribu� (ou en choisir aucun)...",
                RoleEnum.VOLEUR,
                Camps.AUTRE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
