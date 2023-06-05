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

public class Detective extends Role {

    @Override
    public String getDisplayName() {
        return "�7�lD�tective";
    }

    @Override
    public String getConfigName() {
        return "D�tective";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous serez appel� pour �9enqu�ter�f sur deux joueurs : vous saurez s'ils sont du m�me camp ou non.";
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
        return 30;
    }

    @Override
    public String getActionMessage() {
        return "Vous avez �7" + getTimeout() + " secondes�f pour choisir 2 personnes � enqu�ter.";
    }

    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        playerLG.getCache().put("deteciveAleardyInvestigated", new ArrayList<>());
        super.onPlayerJoin(playerLG);
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
                            if(investigate(choosen, playerLG)) {

                                Detective.super.onPlayerTurnFinish(playerLG);
                                callback.run();
                            } else
                                run();
                        }
                    });
                }
            }.run();

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).toArray(new PlayerLG[0])), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"�fVoulez-vous �7enqu�ter sur " + paramPlayerLG.getNameWithAttributes(playerLG) + "�f ?", "�fVous saurez s'il est du m�me camp que votre 2�me choix.", "", "�7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if (investigate(choosenLG, playerLG)) {
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

    private boolean investigate(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return true;

        if (((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).contains(choosen)) {
            playerLG.sendMessage(LG.getPrefix() + "�cTu as d�j� enqu�t� sur " + choosen.getNameWithAttributes(playerLG) + "�c !");
            GameLG.playNegativeSound(playerLG.getPlayer());
            return false;
        }

        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        if (playerLG.getCache().has("detectiveFirstChoice")) {
            PlayerLG firstChoiceLG = (PlayerLG) playerLG.getCache().get("detectiveFirstChoice");

            if (firstChoiceLG.getCamp().equals(choosen.getCamp()) && choosen.getCamp() != Camps.AUTRE) {
                playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "�a et " + firstChoiceLG.getNameWithAttributes(playerLG) + "�a sont du m�me camp !");
                GameLG.playPositiveSound(playerLG.getPlayer());
            } else {
                playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "�c et " + firstChoiceLG.getNameWithAttributes(playerLG) + "�c ne sont pas du m�me camp !");
                GameLG.playNegativeSound(playerLG.getPlayer());
            }

            ((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).add(firstChoiceLG);
            ((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).add(choosen);

            return true;
        } else {
            playerLG.getCache().put("detectiveFirstChoice", choosen);
            return false;
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }


    @EventHandler
    public void onCloseDetectiveInv(InventoryCloseEvent ev) {
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
}
