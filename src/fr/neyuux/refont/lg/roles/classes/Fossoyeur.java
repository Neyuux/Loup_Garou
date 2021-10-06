package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Fossoyeur extends Role {

    public Fossoyeur() {
        super("�8�lFossoyeur",
                "�8�lFossoyeur",
                "Fossoyeur",
                "�fVous �tes �8�lFossoyeur�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). A votre mort, vous pourrez choisir un joueur ; et dans le chat, sera indiqu� le pseudo de ce joueur et le pseudo d'un autre joueur d'un �9camp diff�rent�f du premier.",
                RoleEnum.FOSSOYEUR,
                Camps.VILLAGE,
                Decks.WOLFY);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
