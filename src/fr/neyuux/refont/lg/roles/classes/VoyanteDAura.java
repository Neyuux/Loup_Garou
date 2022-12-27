package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
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

public class VoyanteDAura extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lVoyante §4§ld'Aura";
    }

    @Override
    public String getConfigName() {
        return "Voyante d'Aura";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous vous réveillerez et découvrez si un joueur que vous choisirez est Loup ou non.";
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
        return "§fVous avez §4" + this.getTimeout() + " secondes §fpour espionner l'aura de quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    check(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§dVoulez-vous §5regarder l'aura §dà " + paramPlayerLG.getNameWithAttributes(playerLG) + "§d ?", "§dVous découvrez s'il est Loup-Garou ou non.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    check(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void check(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        if (LG.getInstance().getGame().getLGs(false).contains(choosen)) {
            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + " §da une Aura §4§lSombre§d, il est donc du côté des Loups-Garous !");
            playerLG.sendTitle("§4§lSombre", "§dVous avez senti l'aura de §5§l" + playerLG.getDisplayName() + "§d !", 5, 80, 5);
            if ((boolean)LG.getInstance().getGame().getConfig().getChattyVoyante().getValue())
                playerLG.sendMessage(LG.getPrefix() + "§dLa " + this.getDisplayName() + "§d a espionné un joueur dont l'aura est §4§lSombre§d !");


        } else {
            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + " §da une Aura §f§lClaire§d, il n'est donc §apas §ddu côté des Loups-Garous !");
            playerLG.sendTitle("§f§lClaire", "§dVous avez senti l'aura de §5§l" + playerLG.getDisplayName() + "§d !", 5, 80, 5);
            if ((boolean)LG.getInstance().getGame().getConfig().getChattyVoyante().getValue())
                playerLG.sendMessage(LG.getPrefix() + "§dLa " + this.getDisplayName() + "§d a espionné un joueur dont l'aura est §f§lClaire§d !");
        }

        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseVoyanteAuraInv(InventoryCloseEvent ev) {
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
