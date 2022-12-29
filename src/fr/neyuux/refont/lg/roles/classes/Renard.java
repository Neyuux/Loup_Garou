package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Renard extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lRenard";
    }

    @Override
    public String getConfigName() {
        return "Renard";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez sélectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un §c§lLoup-Garou§f, vous §9gardez votre pouvoir§f. Par contre, s'il n'y en a aucun, vous §9perdez votre pouvoir§f.";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §6" + this.getTimeout() + " secondes §fpour renifler quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    flair(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§6Voulez-vous §5renifler " + paramPlayerLG.getNameWithAttributes(playerLG) + "§6 et ses deux voisins ?", "§6Cela vous permettra de savoir si un Loup-Garou", "§6se trouve parmis " + list3Players(paramPlayerLG, ChatColor.GOLD) + "§6.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    flair(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void flair(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        boolean hasLG = false;

        for (PlayerLG nearbyPlayer : choosen.get2NearbyPlayers(true)) if (nearbyPlayer.isLG()) hasLG = true;

        if (hasLG) {
            playerLG.sendMessage(LG.getPrefix() + "§aIl n'y a aucun Loups-Garous parmis " + list3Players(choosen, ChatColor.GREEN) + "§a !");
            GameLG.playPositiveSound(playerLG.getPlayer());
            playerLG.sendMessage(LG.getPrefix() + "§9Vous §cperdez §9votre pouvoir.");
            playerLG.setCanUsePowers(false);

        } else {
            playerLG.sendMessage(LG.getPrefix() + "§cUn Loup-Garou se trouve parmis " + list3Players(choosen, ChatColor.RED) + "§c.");
            playerLG.getPlayer().playSound(playerLG.getLocation(), Sound.WOLF_GROWL, 8f, 1.1f);
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseAssassinInv(InventoryCloseEvent ev) {
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


    private String list3Players(PlayerLG playerLG, ChatColor chatColor) {
        return "§e" + playerLG.getName() + chatColor + ", §e" + playerLG.get2NearbyPlayers(false).get(0).getName() + chatColor + " et §e" + playerLG.get2NearbyPlayers(false).get(1).getName();
    }
}
