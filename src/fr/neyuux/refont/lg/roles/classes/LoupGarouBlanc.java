package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class LoupGarouBlanc extends Role {


    public LoupGarouBlanc() {
        super(
                "§c§lLoup-Garou §f§lBlanc",
                "Loup-Garou Blanc",
                "§fVous êtes §c§lLoup-Garou §f§lBlanc§f, votre objectif est de §9terminer la partie seul§f. Pour les autres §c§lLoups-Garous§f, vous apparaissez comme leur coéquipier : attention à ne pas être découvert...",
                RoleEnum.LOUP_GAROU_BLANC,
                Camps.AUTRE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
