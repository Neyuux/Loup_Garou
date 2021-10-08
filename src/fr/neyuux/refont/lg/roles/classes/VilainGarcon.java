package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class VilainGarcon extends Role {

    public VilainGarcon() {
        super("�c�lVilain �b�lGar�on",
                "�c�lVilain �b�lGar�on",
                "Vilain Garcon",
                "�fVous �tes �c�lVilain �b�lGar�on�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez �changer les r�les de deux personnes.",
                RoleEnum.VILAIN_GARCON,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
