package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class LoupGarouBlanc extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lLoup-Garou §f§lBlanc";
    }

    @Override
    public String getConfigName() {
        return "Loup-Garou Blanc";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est de §9terminer la partie seul§f. Pour les autres §c§lLoups-Garous§f, vous apparaissez comme leur coéquipier : attention à ne pas être découvert...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §4" + this.getTimeout() + " secondes §fpour choisir de tuer un Loup-Garou.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> choosable = game.getLGs(true);

        choosable.remove(playerLG);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosable.contains(choosen) && choosen != null && choosen != playerLG) {
                    devour(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, choosable, new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§cVoulez-vous §4dévorer " + paramPlayerLG.getNameWithAttributes(playerLG) + "§c ?", "§cIl sera éliminé de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    devour(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void devour(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        LG.getInstance().getGame().kill(choosen);

        playerLG.sendMessage(LG.getPrefix() + "§4Tu as assassiné §C" + choosen.getNameWithAttributes(playerLG) + "§4.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseLGBInv(InventoryCloseEvent ev) {
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
