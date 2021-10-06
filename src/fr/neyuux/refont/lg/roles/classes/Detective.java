package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Detective extends Role {

    public Detective() {
        super("�7�lD�tective",
                "�7�lD�tective",
                "D�tective",
                "�fVous �tes �7�lD�tective�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous serez appel� pour �9enqu�ter�f sur deux joueurs : vous saurez s'ils sont du m�me camp ou non.",
                RoleEnum.DETECTIVE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
