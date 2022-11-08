package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class DictateurDoRebellionItemStack extends CustomItemStack {

    private final Runnable callback;

    public DictateurDoRebellionItemStack(Runnable callback) {
        super(Material.DIAMOND_HOE, 1, "§2§lFaire un coup d'état");

        this.setLore("§eFaire un coup d'état vous permet", "§ed'être le seul à pouvoir voter.", "§eSi vous votez pour un §aVillageois§e,", "§evous vous suiciderez le lendemain.", "§eSinon, vous deviendrez maire du village.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        playerLG.getCache().put("dictateurCoupDEtat", true);
        playerLG.sendMessage(LG.getPrefix() + "§2Vous effectuerez un coup d'état au prochain tour !");
        GameLG.playPositiveSound((Player) player);

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}