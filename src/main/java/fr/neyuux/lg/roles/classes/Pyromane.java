package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.PyromaneInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
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
        return "�6�lPyromane";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les joueurs de la partie et de, par cons�quent, de gagner seul. Chaque nuit, vous pourrez d�cider d'enrober un joueur d'essence ou de mettre le feu � tous les joueurs d�j� huil�s...";
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
        return "�fChoisissez votre action pour ce tour...";
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
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }

    public void turnFinished(PlayerLG playerLG) {
        StringBuilder builder = new StringBuilder(LG.getPrefix() + "�6Liste des joueurs huil�s : ");

        for (PlayerLG oiledPlayer : this.getOiledPlayers()) {
            oiledPlayer.sendMessage(LG.getPrefix() + "�6Tu es huil� !");
            oiledPlayer.sendTitle("�6�lTu es huil� !", "�6Tu risques de mourir si tu ne trouves pas le Pyromane !", 20, 40, 20);
            oiledPlayer.getPlayer().playSound(oiledPlayer.getLocation(), Sound.WOLF_SHAKE, 6f, 1.2f);
            builder.append(oiledPlayer.getName()).append(", ");
        }

        playerLG.sendMessage(builder.substring(0, 2));

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
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().startsWith(this.getDisplayName()) && (boolean)playerLG.getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerLG.getCache().put("unclosableInv", false);
                    player.openInventory(inv);
                    playerLG.getCache().put("unclosableInv", true);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }
}
