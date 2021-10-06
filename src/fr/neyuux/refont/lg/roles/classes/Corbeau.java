package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Corbeau extends Role {

    public Corbeau(){
        super("§8§lCorbeau",
                "§8§lCorbeau",
                "Corbeau",
                "§fVous êtes §8§lCorbeau§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez désigner un joueur qui §9recevra 2 votes§f au petit matin...",
                RoleEnum.CORBEAU,
                Camps.VILLAGE,
                Decks.ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
