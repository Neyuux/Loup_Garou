package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Sorciere extends Role {

    public Sorciere() {
        super("§5§lSorcière",
                "§5§lSorcière",
                "Sorciere",
                "§fVous êtes §5§lSorcière§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Vous possèdez 2 §2potions§f : une §apotion de vie§f§o(qui vous permettera de réssuciter un joueur, dont le nom vous sera donné, dévoré par les Loups)§f et une §4potion de mort§f§o(qui vous permettera de tuer un joueur de votre choix)§f.",
                RoleEnum.SORCIERE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
