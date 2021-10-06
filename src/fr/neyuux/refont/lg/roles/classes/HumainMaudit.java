package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class HumainMaudit extends Role {

    public HumainMaudit()  {
        super("§e§lHumain §4§lMaudit",
                "§e§lHumain §4§lMaudit",
                "Humain Maudit",
                "§fVous êtes §e§lHumain §4§lMaudit§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Si vous vous faites cibler par les Loups, vous ne mourrez pas et devenez l'un d'entre-eux.",
                RoleEnum.HUMAIN_MAUDIT,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE
                );
    }

    @Override
    public void onDistribution(Player player) {

    }
}
