package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.config.ComedianPowers;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.Comedien;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ComedianPowerChoiceItemStack extends CustomItemStack {

    private final Comedien comedian;
    private final Runnable callback;
    private Role newRole = null;

    public ComedianPowerChoiceItemStack(ComedianPowers power, Comedien comedian, Runnable callback) {
        super(Material.MAP, 1, power.getName());
        this.comedian = comedian;

        this.callback = callback;
        try {
            this.newRole = LG.getInstance().getRoles().get(power.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cErreur lors de la création des rôles du Comédien. Veuillez appeler Neyuux_ ou réessayer plus tard.");
        }

        this.setDisplayName(newRole.getDisplayName());
        this.setLore("§dVous donne le pouvoir du rôle " + newRole.getDisplayName(), "§dpour ce tour. Vous ne pourrez donc plus l'utiliser.", "", "§7>>Clique pour sélectionner");

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        GameLG game = LG.getInstance().getGame();

        playerLG.getCache().put("comedianpower", newRole);
        playerLG.setRole(newRole);

        game.getAliveRoles().add(newRole);
        game.getGameRunnable().calculateRoleOrder();
        while (true) {
            if (game.getGameRunnable().getRolesOrder().get(0) instanceof Comedien) {
                game.getGameRunnable().getRolesOrder().remove(0);
                break;
            } else game.getGameRunnable().getRolesOrder().remove(0);
        }

        playerLG.sendMessage(LG.getPrefix() + "§dPour cette nuit, vous obtenez le pouvoir " + newRole.getDeterminingName() + "§d.");
        GameLG.playPositiveSound((Player) player);

        comedian.isInvOpen = false;

        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}