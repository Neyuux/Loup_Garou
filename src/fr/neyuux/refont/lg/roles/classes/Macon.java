package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Macon extends Role {

    public Macon() {
        super("�6�lMa�on",
                "�6�lMa�on",
                "Macon",
                "�fVous �tes �6�lMa�on�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous et les autres �6�lMa�ons�f vous reconnaissez entre-vous �8(car vous vous appellez tous Ricardo)�f ; vous pouvez donc avoir confiance en eux.",
                RoleEnum.MACON,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
