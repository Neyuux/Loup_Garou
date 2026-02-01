package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ChienLoupDogChoiceItemStack extends CustomItemStack {

    private final Runnable callback;

    public ChienLoupDogChoiceItemStack(Runnable callback) {
        super(Material.STAINED_CLAY, 1, "§a§lChien");

        this.setLore("§eVous transforme en §a§lChien§e.", "§eVous appartiendez donc au camp du Village et devrez", "§eéliminer tous les Loups-Garous.", "", "§7>>Clique pour sélectionner");
        this.setDamage(5);

        this.callback = callback;
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        playerLG.setCamp(Camps.VILLAGE);
        playerLG.sendMessage(LG.getPrefix() + "§eVous avez choisi le camp du §a§lVillage§e !");
        GameLG.playPositiveSound((Player) player);

        LG.closeSmartInv((Player) player);
        playerLG.setSleep();
        callback.run();
    }
}