package fr.neyuux.refont.lg.items.menus.config.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.GameConfig;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class RoleAddingItemStack extends CustomItemStack {

    private final Role role;

    public RoleAddingItemStack(Role role) {
        super(Material.STAINED_GLASS_PANE, 1);

        this.role = role;

        this.updateMeta();
    }

    @Override
    public void use(HumanEntity player, Event event) {
        InventoryClickEvent clickEvent = (InventoryClickEvent) event;
        Inventory inv = clickEvent.getInventory();
        List<Constructor<? extends Role>> added = LG.getInstance().getGame().getConfig().getAddedRoles();
        int slot = CustomItemStack.getSlot(inv, this);

        try {
            if (clickEvent.isLeftClick()) {
                int number = 0;
                for (Constructor<? extends Role> constructor : added)
                    if (constructor.equals(role.getClass().getConstructor()))
                        number++;

                int times = 1;
                if (role.getConfigName().equals("Frere")) times = 3;
                else if (role.getConfigName().equals("Soeur")) times = 2;

                if (number + times > role.getMaxNumber()) {
                    GameLG.playNegativeSound((Player) player);
                    player.sendMessage(LG.getPrefix() + "§cIl ne peut pas y avoir plus de §4§l" + role.getMaxNumber() + " " + role.getDisplayName() + " §cdans une partie !");
                    return;
                }

                for (int i = times; i!= 0; i--)
                    added.add(role.getClass().getConstructor());


            } else if (clickEvent.isRightClick()) {
                added.remove(role.getClass().getConstructor());
            }
        } catch (NoSuchMethodException e) {
            Bukkit.broadcastMessage("§4[§cErreur§4]§c La création des objets pour ajouter des rôles a échoué. Veuillez réessayer ou appeler Neyuux_.");
            e.printStackTrace();
        }

        this.updateMeta();

        inv.setItem(slot, this);
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
