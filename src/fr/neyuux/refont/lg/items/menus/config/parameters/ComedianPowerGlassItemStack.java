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
import java.util.*;

public class ComedianPowerGlassItemStack extends CustomItemStack {

    private final ComedianPowers power;
    private final Role role;
    private final ArrayList<ComedianPowers> listPowers;

    public ComedianPowerGlassItemStack(ComedianPowers power) {
        super(Material.STAINED_GLASS_PANE, 1, "role");
        this.power = power;
        this.role = this.getRole(LG.getInstance().getRoles().get(power.getName()));
        this.listPowers = (ArrayList<ComedianPowers>) LG.getInstance().getGame().getConfig().getComedianPowers().getValue();


        this.setItemMetas(this.isActivated());
    }

    @Override
    public void use(HumanEntity player, Event event) {
        if (this.isActivated()) {
            listPowers.remove(power);
            this.setItemMetas(true);
        } else {
            listPowers.add(power);
            this.setItemMetas(false);
        }
    }


    private Role getRole(Constructor<? extends Role> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cUne erreur s'est produite dans la création des pouvoirs du Comédien !");
        }
        return null;
    }

    private boolean isActivated() {
        for (ComedianPowers activatedPower : listPowers)
            if (activatedPower.getName().equals(power.getName()))
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
