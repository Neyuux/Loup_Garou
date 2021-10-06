package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ChevalierALEpeeRouillee extends Role {

    public ChevalierALEpeeRouillee() {
        super("§7§lChevalier §eà l'§7Épée §6§lRouillée",
                "§7§lChevalier §eà l'§7Épée §6§lRouillée",
                "Chevalier a l'Épée Rouillée",
                "§fVous êtes §7§lChevalier §eà l'§7Épée §6§lRouillée§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Si vous mourrez par les loups, le premier Loup à votre gauche§o(réunion)§f ou en dessous dans le tab§o(libre)§f tombera gravement malade : §9il mourra§f au tour suivant.",
                RoleEnum.CHEVALIER_A_L_EPEE_ROUILLEE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
