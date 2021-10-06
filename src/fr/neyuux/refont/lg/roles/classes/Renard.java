package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Renard extends Role {

    public Renard() {
        super("§6§lRenard",
                "§6§lRenard",
                "Renard",
                "§fVous êtes §6§lRenard§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez sélectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un §c§lLoup-Garou§f, vous §9gardez votre pouvoir§f. Par contre, s'il n'y en a aucun, vous §9perdez votre pouvoir§f.",
                RoleEnum.RENARD,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
