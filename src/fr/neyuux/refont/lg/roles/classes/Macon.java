package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Macon extends Role {

    public Macon() {
        super("§6§lMaçon",
                "§6§lMaçon",
                "Macon",
                "§fVous êtes §6§lMaçon§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous et les autres §6§lMaçons§f vous reconnaissez entre-vous §8(car vous vous appellez tous Ricardo)§f ; vous pouvez donc avoir confiance en eux.",
                RoleEnum.MACON,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
