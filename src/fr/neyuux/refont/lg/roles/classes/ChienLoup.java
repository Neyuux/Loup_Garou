package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class ChienLoup extends Role {

    public ChienLoup() {
        super ("§a§lChien§e-§c§lLoup",
                "§a§lChien§e-§c§lLoup",
                "Chien-Loup",
                "§fVous êtes §a§lChien§e-§c§lLoup§f, au début de la partie, vous allez devoir choisir entre devenir §c§lLoup-Garou §fou §e§lSimple §a§lVillageois§f.",
                RoleEnum.CHIEN_LOUP,
                Camps.AUTRE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }

}
