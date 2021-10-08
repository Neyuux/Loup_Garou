package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pretre extends Role {

    public Pretre() {
        super("§e§lPrêtre",
                "§e§lPrêtre",
                "Pretre",
                "§fVous êtes §e§lPrêtre§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous possédez une fiole d'eau bénite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il §9mourra§f sinon, vous mourrez.",
                RoleEnum.PRETRE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
