package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class GardeDuCorps extends Role {

    public GardeDuCorps() {
        super("�7�lGarde �e�ldu Corps",
                "�7�lGarde �e�ldu Corps",
                "Garde du Corps",
                "�fVous �tes �7�lGarde �e�ldu Corps�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pouvez choisir de �9prot�ger�f un joueur. S'il est cens� mourir pendant la nuit, vous �9mourrez�f � sa place.",
                RoleEnum.GARDE_DU_CORPS,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
