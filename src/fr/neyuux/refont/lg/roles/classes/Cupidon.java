package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.chat.ChatLG;
import fr.neyuux.refont.lg.event.PlayerEliminationEvent;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class Cupidon extends Role {

    public static final ChatLG CHAT = new ChatLG("§9\u2764 ", ChatColor.LIGHT_PURPLE, '!');

    @Override
    public String getDisplayName() {
        return "§9§lCupi§d§ldon";
    }

    @Override
    public String getConfigName() {
        return "Cupidon";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous pouvez §9sélectionner 2 joueurs§f pour qu'ils deviennent le §d§lCouple§f de cette partie. Ils devront gagner ensemble ou avec leur camp d'origine (s'ils sont ensemble) ; et si l'un d'entre eux meurt, l'autre se suicidera d'un chagrin d'amour.";
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
        return "§fVous avez §d" + this.getTimeout() + " secondes §f pour choisir le couple de cette partie.";
    }


    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        super.onPlayerJoin(playerLG);
        if ((boolean)LG.getInstance().getGame().getConfig().getRandomCouple().getValue())
            playerLG.sendMessage(LG.getPrefix() + "§d§lRAPPEL : §dLe paramètre §7§lCouple Random§d est activé et vous ne pourrez pas choisir le couple.");
        if ((boolean)LG.getInstance().getGame().getConfig().getCupiInCouple().getValue())
            playerLG.sendMessage(LG.getPrefix() + "§d§lRAPPEL : §dLe paramètre §5§lCupi en Couple§d est activé. Par conséquent, vous serez forcément en couple.");
        if ((boolean)LG.getInstance().getGame().getConfig().getCupiWinWithCouple().getValue())
            playerLG.sendMessage(LG.getPrefix() + "§d§lRAPPEL : §dLe paramètre §a§lCupi gagne avec le Couple§d est activé. Par conséquent, vous pouvez gagner avec votre couple.");
    }

    @Override
    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (game.getPlayersByRole(this.getClass()).isEmpty()) {
            if (game.isNotThiefRole(this)) callback.run();
            else game.wait(Cupidon.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Cupidon.this.getDeterminingName(), true);
            return;
        }

        PlayerLG playerLG = game.getPlayersByRole(this.getClass()).get(0);

        if (playerLG.canUsePowers()) {

            if ((boolean)game.getConfig().getRandomCouple().getValue()) {
                game.wait(Cupidon.this.getTimeout() / 4, () -> {
                    Random random = new Random();
                    while (!couple(game.getAlive().get(random.nextInt(game.getAlive().size())), playerLG)) {
                        couple(game.getAlive().get(random.nextInt(game.getAlive().size())), playerLG);
                    }
                    callback.run();
                }, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Cupidon.this.getDeterminingName(), true);


            } else {

                playerLG.setWake();

                game.wait(Cupidon.this.getTimeout(), () -> {
                    this.onPlayerTurnFinish(playerLG);
                    callback.run();

                }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : LG.getPrefix() + "§9§lAu tour " + Cupidon.this.getDeterminingName(), true);

                playerLG.sendMessage(LG.getPrefix() + Cupidon.this.getActionMessage());
                Cupidon.this.onPlayerNightTurn(playerLG, callback);
            }

        } else {
            game.wait(Cupidon.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + Cupidon.this.getDeterminingName(), true);
        }
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            new Runnable() {
                @Override
                public void run() {
                    playerLG.setChoosing(choosen -> {
                        if (choosen != null) {

                            if (couple(choosen, playerLG)) {

                                Cupidon.super.onPlayerTurnFinish(playerLG);
                                callback.run();
                            } else
                                run();
                        }
                    });
                }
            }.run();


        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§9Voulez-vous §dmettre en couple " + paramPlayerLG.getNameWithAttributes(playerLG) + "§9 ?", "§9Son destin sera lié avec une deuxième personne.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if (couple(choosenLG, playerLG)) {
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

    private boolean couple(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return true;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        if ((boolean)LG.getInstance().getGame().getConfig().getCupiInCouple().getValue() && !playerLG.getCache().has("cupidonFirstChoice")) {
            playerLG.getCache().put("cupidonFirstChoice", playerLG);
            if (choosen.equals(playerLG))
                return false;
        }

        if (playerLG.getCache().has("cupidonFirstChoice") && !playerLG.getCache().get("cupidonFirstChoice").equals(choosen)) {
            PlayerLG choosen2 = (PlayerLG) playerLG.getCache().get("cupidonFirstChoice");

            choosen.getCache().put("couple", choosen2);
            choosen2.getCache().put("couple", choosen);

            CHAT.openChat(new HashSet<>(), new HashSet<>(Arrays.asList(choosen, choosen2)));

            choosen.sendMessage(LG.getPrefix() + "§dVous recevez soudainement une flèche, elle vous transperce. Regardant au loin, vous apercevez §5" + choosen2.getName() + " §d, vous vous effondrez de joie et remerciez " + this.getDisplayName() + " §dpour avoir fait ce choix. §r\n§dVous êtes amoureux de §5" + choosen2.getName() + " §d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
            choosen.sendMessage(LG.getPrefix() + "§9Utilisez §e! §9pour lui parler de manière privée.");
            choosen2.sendMessage(LG.getPrefix() + "§dVous recevez soudainement une flèche, elle vous transperce. Regardant au loin, vous apercevez §5" + choosen.getName() + " §d, vous vous effondrez de joie et remerciez " + this.getDisplayName() + " §dpour avoir fait ce choix. §r\n§dVous êtes amoureux de §5" + choosen.getName() + " §d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
            choosen2.sendMessage(LG.getPrefix() + "§9Utilisez §e! §9pour lui parler de manière privée.");

            choosen.updateGamePlayerScoreboard();
            choosen2.updateGamePlayerScoreboard();

            playerLG.sendMessage(LG.getPrefix() + "§dVos 2 flèches ont bien transpercé §5" + choosen.getNameWithAttributes(playerLG) + " §det §5" + choosen2.getNameWithAttributes(playerLG) + "§d. Ils ne se quitteront plus désormais...");
            playerLG.getCache().remove("cupidonFirstChoice");
            playerLG.updateGamePlayerScoreboard();
            GameLG.playPositiveSound(playerLG.getPlayer());
            return true;

        } else {
            playerLG.getCache().put("cupidonFirstChoice", choosen);
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
    public void onCloseCupidonInv(InventoryCloseEvent ev) {
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
    public void onCoupleElimination(PlayerEliminationEvent ev) {
        PlayerLG playerLG = ev.getEliminated();

        if (playerLG.getCache().has("couple")) {
            PlayerLG coupleLG = (PlayerLG) playerLG.getCache().get("couple");

            if (coupleLG.isDead()) return;

            Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                if (coupleLG.isDead()) return;
                Bukkit.broadcastMessage(LG.getPrefix() + "§cDans un chagrin d'amour suite à la mort de son bien-aimé, §e" + coupleLG.getName() + "§c met fin à sa vie...");

                coupleLG.eliminate();

                Bukkit.broadcastMessage(LG.getPrefix() + "§d§l\u2764 §e" + playerLG.getName() + " §det §e" + coupleLG.getName() + " §détaient en Couple ! §d§l\u2764");
            }, 1L);
        }
    }
}
