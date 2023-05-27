package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.classes.ChienLoup;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ChienLoupWolfChoiceItemStack extends CustomItemStack {

    private final Runnable callback;

    public ChienLoupWolfChoiceItemStack(Runnable callback) {
        super(Material.STAINED_CLAY, 1, "§c§lLoup");

        this.setLore("§eVous transforme en §c§lLoup§e.", "§eVous appartiendez donc au camp des Loups-Garous et devrez", "§eéliminer tous les villageois.", "", "§7>>Clique pour sélectionner");
        this.setDamage(14);

        this.callback = callback;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        playerLG.setCamp(Camps.LOUP_GAROU);
        playerLG.sendMessage(LG.getPrefix() + "§eVous avez choisi le camp des §c§lLoups-Garous§e !");
        GameLG.playPositiveSound((Player) player);

        LG.getInstance().getGame().getGameRunnable().updateRoleOrder(ChienLoup.class);

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        this.callback.run();
    }
}