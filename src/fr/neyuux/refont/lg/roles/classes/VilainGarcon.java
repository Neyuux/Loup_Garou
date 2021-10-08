package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class VilainGarcon extends Role {

    public VilainGarcon() {
        super("§c§lVilain §b§lGarçon",
                "§c§lVilain §b§lGarçon",
                "Vilain Garcon",
                "§fVous êtes §c§lVilain §b§lGarçon§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez échanger les rôles de deux personnes.",
                RoleEnum.VILAIN_GARCON,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
