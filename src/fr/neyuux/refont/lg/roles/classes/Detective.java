package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Detective extends Role {

    public Detective() {
        super("§7§lDétective",
                "§7§lDétective",
                "Détective",
                "§fVous êtes §7§lDétective§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous serez appelé pour §9enquêter§f sur deux joueurs : vous saurez s'ils sont du même camp ou non.",
                RoleEnum.DETECTIVE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
