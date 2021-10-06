package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class MontreurDOurs extends Role {

    public MontreurDOurs() {
        super("�6�lMontreur d'Ours",
                "�6�lMontreur d'Ours",
                "Montreur d'Ours",
                "�fVous �tes �6�lMontreur d'Ours�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). A chaque matin�e, votre ours grognera si un ou deux de vos voisins est un �c�lLoup-Garou�f.",
                RoleEnum.MONTREUR_D_OURS,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
