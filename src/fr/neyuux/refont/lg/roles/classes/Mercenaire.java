package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

public class Mercenaire extends Role {

    public Mercenaire() {
        super(
                "§c§lMerce§5§lnaire",
                "Mercenaire",
                "§fVous êtes §c§lMerce§5§lnaire§f, le premier jour, votre objectif est d'éliminer la cible qui vous est attribuée. Si vous y parvenez, vous gagnez seul la partie instantanément. Sinon, vous devenez §e§lSimple §a§lVillageois§f.",
                RoleEnum.MERCENAIRE,
                Camps.VILLAGE,
                Decks.WOLFY);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
