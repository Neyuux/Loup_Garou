package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.ComedianPowers;
import fr.neyuux.refont.lg.inventories.config.parameters.ParameterComedianPowersInv;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ComedianPowerGlassItemStack extends CustomItemStack {

    private final Role role;
    private final Constructor<? extends Role> constructorRole;

    public ComedianPowerGlassItemStack(Constructor<? extends Role> constructorRole) {
        super(Material.STAINED_GLASS_PANE, 1, "role");
        this.role = this.getRole(constructorRole);
        this.constructorRole = constructorRole;


        this.setItemMetas(this.isActivated());
    }

    @Override
    public void use(HumanEntity player, Event event) {
        if (this.isActivated()) {
            ((ArrayList<ComedianPowers>)LG.getInstance().getGame().getConfig().getComedianPowers().getValue()).remove(ComedianPowers.getByRole(constructorRole));
            this.setItemMetas(true);
        } else {
            ((ArrayList<ComedianPowers>)LG.getInstance().getGame().getConfig().getComedianPowers().getValue()).add(ComedianPowers.getByRole(constructorRole));
            this.setItemMetas(false);
        }
    }


    private Role getRole(Constructor<? extends Role> role) {
        try {
            return ((Constructor<Role>)role).newInstance(LG.getInstance().getGame());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cUne erreur s'est produite dans la création des pouvoirs du Comédien !");
        }
        return null;
    }

    private boolean isActivated() {
        for (ComedianPowers power : (ArrayList<ComedianPowers>)LG.getInstance().getGame().getConfig().getComedianPowers().getValue())
            if (power.getRole().equals(constructorRole))
                return true;
        return false;
    }

    private void setItemMetas(boolean isActivated) {
        if (isActivated) {
            this.setDisplayName("§a" + role.getConfigName());
            this.setDamage(5);
            this.setLore(Arrays.asList("§bValeur : §2§lUtilisé", "", "§7>>Clique pour retirer"));
        } else {
            this.setDisplayName("§c" + role.getConfigName());
            this.setDamage(14);
            this.setLore(Arrays.asList("§bValeur : §4§lPas Utilisé", "", "§7>>Clique pour ajouter"));
        }
    }
}
