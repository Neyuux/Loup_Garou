package fr.neyuux.lg.items.menus.config.reset;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class YesResetMapItemStack extends CustomItemStack {

    public YesResetMapItemStack() {
        super(Material.STAINED_CLAY, 1, "§a§lOui");

        this.setLore("§7>>Clique pour", "§7reset la Map.");
        this.setDamage(5);

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().resetGame();
        Bukkit.broadcastMessage(LG.getPrefix() + "§b" + player.getName() + " §ea reset la partie !");
        GameLG.playPositiveSound((Player) player);
    }
}