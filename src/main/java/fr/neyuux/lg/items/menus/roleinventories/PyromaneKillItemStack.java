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
        super(Material.FLINT_AND_STEEL, 1, "�c�lMettre le feu");
        this.pyromane = pyromane;

        this.setLore("�eMettre le feu permet de faire", "�ebr�ler tous les joueurs huil�s.", "", "�7>>Clique pour s�lectionner");

        this.callback = callback;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        pyromane.getOiledPlayers().forEach(oiledLG -> LG.getInstance().getGame().kill(oiledLG));

        playerLG.sendMessage(LG.getPrefix() + "�cVous avez �limin� tous les joueurs huil�s !");
        GameLG.playPositiveSound((Player) player);

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}