package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Chaman extends Role {

    public Chaman() {
        super("§b§lCha§a§lman",
                "§b§lCha§a§lman",
                "Chaman",
                "§fVous êtes §b§lCha§a§lman§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pour §9voir les messages des morts§f.",
                RoleEnum.CHAMAN,
                Camps.VILLAGE,
                Decks.ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
