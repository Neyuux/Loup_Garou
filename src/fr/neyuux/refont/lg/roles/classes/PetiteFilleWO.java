package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class PetiteFilleWO extends Role {

    public PetiteFilleWO() {
        super("§9§lPetite §b§lFille §0§oWO",
                "§9§lPetite §b§lFille §0§oWO",
                "Petite Fille WO",
                "§fVous êtes §9§lPetite §b§lFille§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez ouvrir les yeux, si vous le faites, vous aurez 20% de chance de §9trouver un Loup§f et 5% de chance de §9mourir§f.",
                RoleEnum.PETITE_FILLE_WO,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
