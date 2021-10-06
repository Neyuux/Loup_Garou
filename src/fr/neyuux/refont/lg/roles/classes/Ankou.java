package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Ankou extends Role {

    public Ankou() {
        super("�6�lAnkou",
                "�6�lAnkou",
                "Ankou",
                "�fVous �tes �6�lA�c�ln�f�lk�7�lo�8�lu�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois que vous mourrez, vous pouvez �9continuer de voter�f pendant deux tours maximum depuis le cimeti�re � l'aide de la commande �e",
                RoleEnum.ANKOU,
                Camps.VILLAGE,
                Decks.ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
