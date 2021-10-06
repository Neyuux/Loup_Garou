package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class EnfantSauvage extends Role {

    public EnfantSauvage() {
        super("§6§lEnfant Sauvage",
                "§6§lEnfant Sauvage",
                "Enfant Sauvage",
                "§fVous êtes §6§lEnfant Sauvage§f, vous allez, au début de la partie, devoir choisir votre maître. Si celui-ci §9meurt§f, vous devenez un §c§lLoup-Garou§f. Tant que cela ne s'est pas passé, votre but est d'éliminer est §9d'éliminer tous les loups-garous (ou rôles solos)§f.",
                RoleEnum.ENFANT_SAUVAGE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
