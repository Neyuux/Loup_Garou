package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.roleinventories.DictateurInv;
import fr.neyuux.refont.lg.inventories.roleinventories.PyromaneInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Pyromane extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lPyromane";
    }

    @Override
    public String getConfigName() {
        return "Pyromane";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les joueurs de la partie et de, par conséquent, de gagner seul. Chaque nuit, vous pourrez décider d'enrober un joueur d'essence ou de mettre le feu à tous les joueurs déjà huilés...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fChoisissez votre action pour ce tour...";
    }

    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);
        if (this.getOiledPlayers().contains(playerLG))
            playerLG.getCache().remove("pyromaneOiled");
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        new PyromaneInv(this, playerLG, callback).open(playerLG.getPlayer());
        playerLG.getCache().put("unclosableInv", true);
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

    public void turnFinished(PlayerLG playerLG) {
        StringBuilder builder = new StringBuilder(LG.getPrefix() + "§6Liste des joueurs huilés : ");

        for (PlayerLG oiledPlayer : this.getOiledPlayers()) {
            oiledPlayer.sendMessage(LG.getPrefix() + "§6Tu es huilé !");
            oiledPlayer.getPlayer().playSound(oiledPlayer.getLocation(), Sound.WOLF_SHAKE, 6f, 1.2f);
            builder.append(oiledPlayer.getName()).append(", ");
        }

        playerLG.sendMessage(builder.substring(2));

        super.onPlayerTurnFinish(playerLG);
    }

    public List<PlayerLG> getOiledPlayers() {
        List<PlayerLG> list = new ArrayList<>();

        for (PlayerLG oilLG : LG.getInstance().getGame().getAlive())
            if (oilLG.getCache().has("pyromaneOiled"))
                list.add(oilLG);

        return list;
    }


    @EventHandler
    public void onClosePyromaneInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();

        if (inv.getName().equals(this.getDisplayName()) && (boolean)PlayerLG.createPlayerLG(player).getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(inv);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }
}
