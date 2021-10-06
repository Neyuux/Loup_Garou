package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Enchanteur extends Role {

    public Enchanteur() {
        super("§c§lEnchanteur",
                "§c§lEnchanteur",
                "Enchanteur",
                "§fVous êtes §c§lEnchanteur§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Vous ne connaissez pas les autres Loups. Chaque nuit, vous pourrez enchanter un joueur et découvrir s'il agit d'une voyante ou un Loup.",
                RoleEnum.ENCHANTEUR,
                Camps.LOUP_GAROU,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
