package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class DurACuire extends Role {

    public DurACuire() {
        super(
                "§c§lDur §cà §6§lCuire",
                "Dur a Cuire",
                "§fVous êtes §c§lDur à §6§lCuire§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lorque les Loups voudront vous tuer, vous survivrez jusqu'au jour suivant.",
                RoleEnum.DUR_A_CUIRE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
