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

import java.util.ArrayList;

public class Detective extends Role {

    @Override
    public String getDisplayName() {
        return "§7§lDétective";
    }

    @Override
    public String getConfigName() {
        return "Détective";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous serez appelé pour §9enquêter§f sur deux joueurs : vous saurez s'ils sont du même camp ou non.";
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
        return "Vous avez §7" + getTimeout() + " secondes§f pour choisir 2 personnes à enquêter.";
    }

    @Override
    public void onPlayerJoin(PlayerLG playerLG) {
        playerLG.getCache().put("deteciveAleardyInvestigated", new ArrayList<>());
        super.onPlayerJoin(playerLG);
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

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
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).toArray(new PlayerLG[0])), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§fVoulez-vous §7enquêter sur " + paramPlayerLG.getNameWithAttributes(playerLG) + "§f ?", "§fVous saurez s'il est du même camp que votre 2ème choix.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if (investigate(choosenLG, playerLG)) {
                        
                        LG.closeSmartInv(playerLG.getPlayer());
                        playerLG.setSleep();
                        callback.run();
                    }
                }
            });
            
        }
    }

    private boolean investigate(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return true;

        if (((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).contains(choosen)) {
            playerLG.sendMessage(LG.getPrefix() + "§cTu as déjà enquêté sur " + choosen.getNameWithAttributes(playerLG) + "§c !");
            GameLG.playNegativeSound(playerLG.getPlayer());
            return false;
        }

        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        if (playerLG.getCache().has("detectiveFirstChoice")) {
            PlayerLG firstChoiceLG = (PlayerLG) playerLG.getCache().get("detectiveFirstChoice");

            if (firstChoiceLG.equals(choosen))
                return false;

            if (firstChoiceLG.getCamp().equals(choosen.getCamp()) && choosen.getCamp() != Camps.AUTRE) {
                playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "§a et " + firstChoiceLG.getNameWithAttributes(playerLG) + "§a sont du même camp !");
                GameLG.playPositiveSound(playerLG.getPlayer());
            } else {
                playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "§c et " + firstChoiceLG.getNameWithAttributes(playerLG) + "§c ne sont pas du même camp !");
                GameLG.playNegativeSound(playerLG.getPlayer());
            }

            ((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).add(firstChoiceLG);
            ((ArrayList<PlayerLG>)playerLG.getCache().get("deteciveAleardyInvestigated")).add(choosen);
            playerLG.getCache().remove("detectiveFirstChoice");

            return true;
        } else {
            playerLG.getCache().put("detectiveFirstChoice", choosen);
            return false;
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    
}
