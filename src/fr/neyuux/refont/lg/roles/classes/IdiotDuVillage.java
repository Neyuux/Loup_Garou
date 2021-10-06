package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class IdiotDuVillage extends Role {

    public IdiotDuVillage() {
        super("§d§lIdiot §e§ldu Village",
                "§d§lIdiot §e§ldu Village",
                "Idiot du Village",
                "§fVous êtes §d§lIdiot §e§ldu Village§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, une fois dans la partie, le village décide de vous pendre, ils §9reconnaîtront votre bêtise§f. Vous ne mourrez donc pas, mais §9vous ne pourrez plus voter§f.",
                RoleEnum.IDIOT_DU_VILLAGE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
