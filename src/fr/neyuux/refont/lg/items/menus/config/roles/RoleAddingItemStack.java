package fr.neyuux.refont.lg.items.menus.config.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.GameConfig;
import fr.neyuux.refont.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.refont.lg.inventories.config.roles.RolesCampChooseInv;
import fr.neyuux.refont.lg.inventories.config.roles.RolesListRolesInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class RoleAddingItemStack extends CustomItemStack {

    private final Role role;

    public RoleAddingItemStack(Role role) {
        super(Material.STAINED_GLASS_PANE, 1);

        this.role = role;

        this.updateMeta();
    }

    @Override
    public void use(HumanEntity player, Event event) {
        try {
            LG.getInstance().getGame().getConfig().getAddedRoles().add(role.getClass().getConstructor());
        } catch (NoSuchMethodException e) {
            Bukkit.broadcastMessage("§4[§cErreur§4]§c La création des objets pour ajouter des rôles a échoué. Veuillez réessayer ou appeler Neyuux_.");
            e.printStackTrace();
        }
        this.updateMeta();
    }

    private void updateMeta() {
        try {
            GameConfig config = LG.getInstance().getGame().getConfig();
            Constructor<? extends Role> constructor = role.getClass().getConstructor();
            if (config.getAddedRoles().contains(constructor)) {
                this.setDamage(5);
                this.setDisplayName("§a" + role.getConfigName().toUpperCase());
            } else {
                this.setDamage(14);
                this.setDisplayName("§c" + role.getConfigName().toUpperCase());

            }
            this.setLore(Arrays.asList("§3Nombre de §l" + role.getConfigName() + " §b: §b§l" + config.getNumberOfRole(constructor), "", "§e>>Clique droit pour retirer", "§a>>Clique gauche pour ajouter"));


        } catch(Exception e) {
            Bukkit.broadcastMessage("§4[§cErreur§4]§c La création des objets pour ajouter des rôles a échoué. Veuillez réessayer ou appeler Neyuux_.");
            e.printStackTrace();
        }
    }

}
