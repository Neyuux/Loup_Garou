package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class BoucEmissaire extends Role {

    public BoucEmissaire() {
        super("ßcßlBouc ßaßl…missaire",
                "ßcßlBouc ßaßl…missaire",
                "Bouc …missaire",
                "ßfVous Ítes ßcßlBouc ßaßl…missaireßf, votre but est d'Èliminer tous les ßcßlLoups-Garousßf (ou rÙles solos). Si, pendant la partie, il y a ÈgalitÈ dans les votes, ß9vous mourrezßf. (ß8ßlßnßmßoGROSSE VICTIMEßf)",
                RoleEnum.BOUC_EMISSAIRE,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
