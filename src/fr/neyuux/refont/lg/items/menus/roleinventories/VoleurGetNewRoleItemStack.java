package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.Voleur;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VoleurGetNewRoleItemStack extends CustomItemStack {

    private final Runnable callback;
    private final Role role;
    private final Voleur voleur;

    public VoleurGetNewRoleItemStack(Runnable callback, Role role, Voleur voleur) {
        super(Material.DIAMOND_HOE, 1, "§2§lFaire un coup d'état");

        this.setLore("§eFaire un coup d'état vous permet", "§ed'être le seul à pouvoir voter.", "§eSi vous votez pour un §aVillageois§e,", "§evous vous suiciderez le lendemain.", "§eSinon, vous deviendrez maire du village.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;
        this.role = role;
        this.voleur = voleur;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        Role role2;
        if (this.role.equals(voleur.role1))
            role2 = voleur.role2;
        else
            role2 = voleur.role1;

        playerLG.joinRole(this.role);
        playerLG.setCamp(this.role.getBaseCamp());

        GameLG.playPositiveSound((Player) player);
        player.sendMessage(LG.getPrefix() + "§3Vous avez sélectionné le rôle " + this.role.getDisplayName() + "§3. Vous incarnerez donc ce-dernier et supprimez les rôles " + voleur.getDisplayName() + " §3et " + role2.getDisplayName() + " §3de la partie.");

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}