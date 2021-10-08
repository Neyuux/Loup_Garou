package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Necromancien extends Role {

    public Necromancien() {
        super("§9§lNécromancien",
                "§9§lNécromancien",
                "Nécromancien",
                "§fVous êtes §9§lNégromancien§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez §9récussiter§f un joueur. S'il avait un pouvoir, il le perd et devient §e§lSimple §a§lVillageois§f.",
                RoleEnum.NECROMANCIEN,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
