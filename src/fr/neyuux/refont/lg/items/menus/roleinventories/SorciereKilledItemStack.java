package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.classes.LoupGarou;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;

public class SorciereKilledItemStack extends CustomItemStack {

    public SorciereKilledItemStack(PlayerLG playerLG) {
        super(Material.BARRIER, 1, "§a§lPersonne n'est mort");
        this.setLore("§7Les Loups-Garous n'ont tué personne cette nuit.");

        PlayerLG targetLG = LoupGarou.getLastTargetedByLG();

        if (targetLG != null) {
            this.setType(Material.SKULL_ITEM);
            this.setDamage(3);
            this.setSkullOwner(targetLG.getName());
            this.setDisplayName(targetLG.getNameWithAttributes(playerLG) + "§c est Mort.");
            this.setLore("§e" + targetLG.getName() + " §7a été tué par les Loups-Garous", "§7cette nuit. Vous pouvez choisir de le sauver.");
        }

        addItemInList(this);
    }
}