package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Ancien extends Role {

    public Ancien() {
        super("�7�lAncien",
                "�7�lAncien",
                "Ancien",
                "�fVous �tes �7�lAncien�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, pendant une nuit, les Loups d�cident de vous attaquer, vous �9survivez�f �o(utilisable qu'une seule fois)�f. Cependant si le village vous �liminent pendant le jour, tous les villageois �4perdront leurs pouvoirs�f.",
                RoleEnum.ANCIEN,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
