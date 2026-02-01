package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.classes.Voleur;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VoleurGetNewRoleItemStack extends CustomItemStack {

    private final Runnable callback;
    private final Role role;
    private final Voleur voleur;

    public VoleurGetNewRoleItemStack(Runnable callback, Role role, Role otherRole, Voleur voleur) {
        super(Material.EMPTY_MAP, 1, role.getDisplayName());

        this.setLore("§bVous permet de sélectionner le rôle", role.getDisplayName() + "§b. Vous rejoindrez " + "§ble camp " + role.getBaseCamp().getColor() + role.getBaseCamp().getName() + "§b.", "", "§bVous supprimerez les rôles", otherRole.getDisplayName() + " §bet " + voleur.getDisplayName() + " §bde la partie.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;
        this.role = role;
        this.voleur = voleur;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        Role role2;
        if (this.role.equals(Voleur.getRole1())) {
            role2 = Voleur.getRole2();
            Voleur.setRole1(voleur);
        } else {
            role2 = Voleur.getRole1();
            Voleur.setRole2(voleur);
        }

        playerLG.joinRole(this.role);
        playerLG.setCamp(this.role.getBaseCamp());

        playerLG.getCache().put("voleur", voleur);

        GameLG.playPositiveSound((Player) player);
        player.sendMessage(LG.getPrefix() + "§3Vous avez sélectionné le rôle " + this.role.getDisplayName() + "§3. Vous incarnerez donc ce-dernier et supprimez les rôles " + voleur.getDisplayName() + " §3et " + role2.getDisplayName() + " §3de la partie.");

        
        LG.closeSmartInv((Player) player);
        playerLG.setSleep();
        callback.run();
    }
}