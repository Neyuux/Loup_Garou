package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MamieGrincheuse extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lMamie §6§lGrincheuse";
    }

    @Override
    public String getConfigName() {
        return "Mamie Grincheuse";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez choisir un joueur, l'empêchant de voter au jour suivant ; mais vous ne pouvez pas sélectionner deux fois de suite la même personne.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §e " + this.getTimeout() + " secondes§f pour retirer le droit de vote de quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> choosable = game.getAliveExcept(playerLG, (PlayerLG) playerLG.getCache().get("lastChoosen"));

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    cancelVote(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, choosable, new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§dVoulez-vous §6empêcher " + paramPlayerLG.getNameWithAttributes(playerLG) + "§de voter ?", "§dVous ne pourrez pas le sélectionner au prochain tour.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    cancelVote(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void cancelVote(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        choosen.getCache().put("mamieGrincheuseCancelVote", true);

        playerLG.sendMessage(LG.getPrefix() + "§dTu as retiré le droit de vote à " + choosen.getNameWithAttributes(playerLG) + "§d pour ce tour.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseMamieGrincheuseInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().equals(this.getDisplayName()) && (boolean)playerLG.getCache().get("unclosableInv")) {
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

    @EventHandler
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();
        for (PlayerLG playerLG : vote.getVoters())
            if (vote.getName().equals("Vote du Village") && playerLG.getCache().has("mamieGrincheuseCancelVote")) {
                vote.getVoters().remove(playerLG);
                playerLG.sendMessage(LG.getPrefix() + "§cLa " + this.getDisplayName() + " §ca annulé votre droit de vote pour ce tour.");
                playerLG.getCache().put("vote", null);
                playerLG.stopChoosing();
                playerLG.setArmorStand(null);
                LG.getInstance().getItemsManager().updateVoteItems(playerLG);

                playerLG.getCache().remove("mamieGrincheuseCancelVote");
            }
    }
}
