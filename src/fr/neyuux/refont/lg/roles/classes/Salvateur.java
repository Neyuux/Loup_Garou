package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Salvateur extends Role {

    public Salvateur() {
        super("§e§lSalvateur",
                "§e§lSalvateur",
                "Salvateur",
                "§fVous êtes §e§lSalvateur§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Chaque nuit, vous pourrez §9protéger§f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas protéger deux fois la même personne.",
                RoleEnum.SALVATEUR,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
