package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.ServanteDevouee;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ServanteDevoueeTakeRoleItemStack extends CustomItemStack {

    private final Runnable callback;
    private final PlayerLG choosen;

    public ServanteDevoueeTakeRoleItemStack(PlayerLG choosen, Runnable callback) {
        super(Material.STAINED_CLAY, 1, "§a§lPrendre son rôle");
        this.setDamage(5);

        this.choosen = choosen;
        this.callback = callback;

        this.setLore("§7>>Cliquez pour prendre le rôle de §e" + choosen.getName());

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        ServanteDevouee role1 = (ServanteDevouee) playerLG.getRole();
        Role role2 = choosen.getRole();

        playerLG.joinRole(role2);
        playerLG.setCanUsePowers(choosen.canUsePowers());

        for (PlayerLG aliveLG : LG.getInstance().getGame().getAlive()) {
            if (aliveLG.getCache().has("enfantSauvageModel") && aliveLG.getCache().get("enfantSauvageModel").equals(choosen))
                aliveLG.getCache().put("enfantSauvageModel", playerLG);
        }

        choosen.setRole(role1);
        choosen.setCanUsePowers(false);

        playerLG.sendMessage(LG.getPrefix() + "§dVous avez prit le rôle de " +choosen.getName() +" !");
        GameLG.playPositiveSound((Player) player);

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        LG.getInstance().getGame().cancelWait();
        callback.run();
    }
}