package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;

public class ChienLoupDogChoiceItemStack extends CustomItemStack {

    public ChienLoupDogChoiceItemStack() {
        super(Material.STAINED_CLAY, 1, "§a§lChien");

        this.setLore("§eVous transforme en §a§lChien§e.", "§eVous appartiendez donc au camp du Village et devrez", "§eéliminer tous les Loups-Garous.", "", "§7>>Clique pour sélectionner");
        this.setDamage(5);

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        playerLG.setCamp(Camps.VILLAGE);
        playerLG.sendMessage(LG.getPrefix() + "§eVous avez choisi le camp du §a§lVillage§e !");
        GameLG.playPositiveSound((Player) player);

        player.closeInventory();
    }
}