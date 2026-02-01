package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.roles.classes.Pyromane;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PyromaneKillItemStack extends CustomItemStack {

    private final Runnable callback;
    private final Pyromane pyromane;

    public PyromaneKillItemStack(Pyromane pyromane, Runnable callback) {
        super(Material.FLINT_AND_STEEL, 1, "§c§lMettre le feu");
        this.pyromane = pyromane;

        this.setLore("§eMettre le feu permet de faire", "§ebrûler tous les joueurs huilés.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        pyromane.getOiledPlayers().forEach(oiledLG -> LG.getInstance().getGame().kill(oiledLG));

        playerLG.sendMessage(LG.getPrefix() + "§cVous avez éliminé tous les joueurs huilés !");
        GameLG.playPositiveSound((Player) player);

        
        LG.closeSmartInv((Player) player);
        playerLG.setSleep();
        callback.run();
    }
}