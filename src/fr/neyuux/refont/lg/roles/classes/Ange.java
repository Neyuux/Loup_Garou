package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Ange extends Role {

    public Ange() {
        super("§b§lAnge",
                "§b§lAnge",
                "Ange",
                "§fVous êtes §b§lAnge§f, votre but est de vous faire lyncher(tuer) par le village au premier tour. Si vous réussissez : la victoire sera votre, mais dans le cas contraire : vous deviendrez §e§lSimple §a§lVillageois§f.",
                RoleEnum.ANGE,
                Camps.AUTRE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
