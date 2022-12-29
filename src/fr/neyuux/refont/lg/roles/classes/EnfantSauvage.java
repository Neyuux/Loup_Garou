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

import java.util.Random;

public class EnfantSauvage extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lEnfant Sauvage";
    }

    @Override
    public String getConfigName() {
        return "Enfant Sauvage";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, vous allez, au début de la partie, devoir choisir votre maître. Si celui-ci §9meurt§f, vous devenez un §c§lLoup-Garou§f. Tant que cela ne s'est pas passé, votre but est d'éliminer est §9d'éliminer tous les loups-garous (ou rôles solos)§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §6" + this.getTimeout() + " secondes §fpour choisir votre modèle.";
    }


    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        if ((boolean)LG.getInstance().getGame().getConfig().getWildChildRandomModel().getValue())
            playerLG.sendMessage(LG.getPrefix() + "§6§lRAPPEL : §6Le paramètre §a§lModèle Random§6 est activé et vous ne pourrez pas choisir votre modèle.");
    }

    @Override
    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (players.isEmpty()) {
            if (game.isNotThiefRole(this)) callback.run();
            else game.wait(EnfantSauvage.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + EnfantSauvage.this.getDeterminingName(), true);
            return;
        }

        PlayerLG playerLG = players.get(0);

        if (playerLG.canUsePowers()) {

            if ((boolean)game.getConfig().getWildChildRandomModel().getValue()) {
                game.wait(EnfantSauvage.this.getTimeout() / 4, () -> {
                    model(game.getAliveExcept(playerLG).get(new Random().nextInt(game.getAliveExcept(playerLG).size())), playerLG);
                    callback.run();
                }, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + EnfantSauvage.this.getDeterminingName(), true);


            } else {

                playerLG.setWake();

                game.wait(EnfantSauvage.this.getTimeout(), () -> {
                    this.onPlayerTurnFinish(playerLG);
                    this.onNightTurn(callback);

                }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : LG.getPrefix() + "§9§lAu tour " + EnfantSauvage.this.getDeterminingName(), true);

                playerLG.sendMessage(LG.getPrefix() + EnfantSauvage.this.getActionMessage());
                EnfantSauvage.this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));
            }

        } else {
            game.wait(EnfantSauvage.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + EnfantSauvage.this.getDeterminingName(), true);
        }
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    model(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§eVoulez-vous choisir " + paramPlayerLG.getNameWithAttributes(playerLG) + "§ecomme §6modèle§e ?", "§eVous deviendrez §c§lLoup-Garou§e s'il meurt.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    model(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void model(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        choosen.getCache().put("enfantSauvageModel", playerLG);

        playerLG.sendMessage(LG.getPrefix() + "§6Tu as choisi " + choosen.getNameWithAttributes(playerLG) + "§6 comme modèle.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseEnfantSauvageInv(InventoryCloseEvent ev) {
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
    public void onElimination(PlayerEliminationEvent ev) {
        PlayerLG choosen = ev.getEliminated();

        if (choosen.getCache().has("enfantSauvageModel")) {
            PlayerLG esLG = (PlayerLG) choosen.getCache().get("enfantSauvageModel");

            esLG.setCamp(Camps.LOUP_GAROU);

            LG.getInstance().getGame().getLGs(true).forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + "§cUn joueur à rejoint votre camp !"));
            esLG.sendMessage(LG.getPrefix() + "§cVotre Modèle est mort ! Vous devenez donc Loup-Garou.");
        }
    }
}
