package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.PlayerEliminationEvent;
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

public class GrandMechantLoup extends Role {

    @Override
    public String getDisplayName() {
        return "§4§lGrand Méchant §c§lLoup";
    }

    @Override
    public String getConfigName() {
        return "Grand Méchant Loup";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Mais après cela, vous vous réveillez une deuxième fois pour §9dévorer une deuxième personne§f...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.LOUP_GAROU;
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
        return "§fVous avez §4" + this.getDisplayName() + " secondes§f pour dévorer une deuxième personne.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    kill(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(game.getLGs(true).toArray(new PlayerLG[0])), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§cVoulez-vous §4tuer " + paramPlayerLG.getNameWithAttributes(playerLG) + "§c ?", "§cIl sera éliminé de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    kill(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void kill(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        LG.getInstance().getGame().kill(choosen);

        playerLG.sendMessage(LG.getPrefix() + "§4Tu as tué " + choosen.getNameWithAttributes(playerLG) + "§4.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseGrandMechantLoupInv(InventoryCloseEvent ev) {
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

    @EventHandler
    public void onLGElimination(PlayerEliminationEvent ev) {
        if (ev.getEliminated().isLG())
            for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersByRole(this.getClass()))
                playerLG.setCanUsePowers(false);
    }
}
