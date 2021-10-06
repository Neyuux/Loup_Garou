package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Noctambule extends Role {

    public Noctambule() {
        super("�9�lNoctambule",
                "�9�lNoctambule",
                "Noctambule",
                "�fVous �tes �9�lNoctambule�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous devez choisir un joueur chez qui �9dormir�f, ce joueur conna�tra alors votre identit� mais est priv� de ses pouvoirs pour la nuit.",
                RoleEnum.NOCTAMBULE,
                Camps.VILLAGE,
                Decks.ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
