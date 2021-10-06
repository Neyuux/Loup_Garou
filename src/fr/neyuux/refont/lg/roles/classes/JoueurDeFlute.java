package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class JoueurDeFlute extends Role {

    public JoueurDeFlute() {
        super("�5�lJoueur de Fl�te",
                "�5�lJoueur de Fl�te",
                "Joueur de Flute",
                "�fVous �tes �5�lJoueur de Fl�te�f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en �9enchantant tous les joueurs�f avec votre fl�te. Chaque nuit, vous pouvez enchanter jusqu'� 2 personnes.",
                RoleEnum.JOUEUR_DE_FLUTE,
                Camps.AUTRE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
