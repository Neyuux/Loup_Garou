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

public class ChienLoupWolfChoiceItemStack extends CustomItemStack {

    public ChienLoupWolfChoiceItemStack() {
        super(Material.STAINED_CLAY, 1, "§a§lChien");

        this.setLore("§eVous transforme en §c§lLoup§e.", "§eVous appartiendez donc au camp des Loups-Garous et devrez", "§eéliminer tous les villageois.", "", "§7>>Clique pour sélectionner");
        this.setDamage(14);

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        playerLG.setCamp(Camps.LOUP_GAROU);
        playerLG.sendMessage(LG.getPrefix() + "§eVous avez choisi le camp des §c§lLoups-Garous§e !");
        GameLG.playPositiveSound((Player) player);

        player.closeInventory();
    }
}