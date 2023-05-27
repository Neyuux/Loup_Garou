package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
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

import java.util.ArrayList;
import java.util.List;

public class JoueurDeFlute extends Role {

    @Override
    public String getDisplayName() {
        return "§5§lJoueur de Flûte";
    }

    @Override
    public String getConfigName() {
        return "Joueur de Flute";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en §9enchantant tous les joueurs§f avec votre flûte. Chaque nuit, vous pouvez enchanter jusqu'à 2 personnes.";
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
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §5" + this.getTimeout()+ " secondes §fpour choisir qui vous voulez enchanter.";
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            new Runnable() {
                @Override
                public void run() {
                    playerLG.setChoosing(choosen -> {
                        if (choosen != null && choosen != playerLG) {
                            if(enchant(choosen, playerLG)) {

                                JoueurDeFlute.super.onPlayerTurnFinish(playerLG);
                                callback.run();
                            } else
                                run();
                        }
                    });
                }
            }.run();

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, getNonEnchantedPlayers(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§dVoulez-vous §5enchanter " + paramPlayerLG.getNameWithAttributes(playerLG) + "§d ?", "§dIl sera mis au courant ce matin.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if (enchant(choosenLG, playerLG)) {
                        playerLG.getCache().put("unclosableInv", false);
                        playerLG.getPlayer().closeInventory();
                        playerLG.setSleep();
                        callback.run();
                    }
                }
            });
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private boolean enchant(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return true;

        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        choosen.getCache().put("enchanted", true);
        choosen.sendMessage(LG.getPrefix() + "§dA l'aide de votre flûte et de sa mélodie, vous charmez §5" + choosen.getNameWithAttributes(playerLG) + "§d.");

        playerLG.sendMessage(LG.getPrefix() + "§dLe " + this.getDisplayName() + " §dvous a charmé.");
        GameLG.playPositiveSound(playerLG.getPlayer());
        
        LG.getInstance().getGame().checkWin();

        if (playerLG.getCache().has("joueurDeFluteFirstChoice")) {
            playerLG.getCache().remove("joueurDeFluteFirstChoice");
            return true;
        } else {
            playerLG.getCache().put("joueurDeFluteFirstChoice", choosen);
            return false;
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseJoueurDeFluteInv(InventoryCloseEvent ev) {
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


    public static List<PlayerLG> getNonEnchantedPlayers() {
        List<PlayerLG> nonEnchanted = new ArrayList<>();

        for (PlayerLG aliveLG : LG.getInstance().getGame().getAlive())
            if (!aliveLG.getCache().has("enchanted") && !(aliveLG.getRole() instanceof JoueurDeFlute))
                nonEnchanted.add(aliveLG);

        return nonEnchanted;
    }
}
