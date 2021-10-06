package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.RoleEnum;
import org.bukkit.entity.Player;

public class Salvateur extends Role {

    public Salvateur() {
        super("�e�lSalvateur",
                "�e�lSalvateur",
                "Salvateur",
                "�fVous �tes �e�lSalvateur�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Chaque nuit, vous pourrez �9prot�ger�f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas prot�ger deux fois la m�me personne.",
                RoleEnum.SALVATEUR,
                Camps.VILLAGE,
                Decks.THIERCELIEUX);
    }

    @Override
    public void onDistribution(Player player) {

    }
}
