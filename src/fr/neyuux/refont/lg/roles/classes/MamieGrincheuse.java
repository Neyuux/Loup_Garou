package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class MamieGrincheuse extends Role {

    public MamieGrincheuse() {
        super("�d�lMamie �c�lGrincheuse",
                "�d�lMamie �c�lGrincheuse",
                "Mamie Grincheuse",
                "�fVous �tes �d�lMamie �c�lGrincheuse�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez choisir un joueur, l'emp�chant de voter au jour suivant ; mais vous ne pouvez pas s�lectionner deux fois de suite la m�me personne.",
                RoleEnum.MAMIE_GRINCHEUSE,
                Camps.VILLAGE,
                Decks.WEREWOLF_ONLINE);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
