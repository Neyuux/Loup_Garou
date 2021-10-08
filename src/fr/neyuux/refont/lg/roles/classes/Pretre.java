package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Pretre extends Role {

    public Pretre() {
        super("�e�lPr�tre",
                "�e�lPr�tre",
                "Pretre",
                "�fVous �tes �e�lPr�tre�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous poss�dez une fiole d'eau b�nite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il �9mourra�f sinon, vous mourrez.",
                RoleEnum.PRETRE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
