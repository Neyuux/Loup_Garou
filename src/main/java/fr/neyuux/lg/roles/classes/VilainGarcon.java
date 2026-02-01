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

public class VilainGarcon extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lVilain §b§lGarçon";
    }

    @Override
    public String getConfigName() {
        return "Vilain Garcon";
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
        return "§fVous êtes "+this.getDisplayName()+", votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez échanger les rôles de deux personnes.";
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
        return "§fVous avez §b " + this.getTimeout() + " secondes§f pour échanger les rôles de deux joueurs.";
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
                            if(swap(choosen, playerLG)) {

                                VilainGarcon.super.onPlayerTurnFinish(playerLG);
                                callback.run();
                            } else
                                run();
                        }
                    });
                }
            }.run();

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§bVoulez-vous §cchanger le rôle de " + paramPlayerLG.getNameWithAttributes(playerLG) + "§b ?", "§bIl l'échangera avec votre 2ème choix.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if (swap(choosenLG, playerLG)) {
                        
                        LG.closeSmartInv(playerLG.getPlayer());
                        playerLG.setSleep();
                        callback.run();
                    }
                }
            });
            
        }
    }

    private boolean swap(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return true;

        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        if (playerLG.getCache().has("vilainGarconFirstChoice")) {
            PlayerLG firstChoiceLG = (PlayerLG) playerLG.getCache().get("vilainGarconFirstChoice");

            Role role1 = choosen.getRole();
            Role role2 = firstChoiceLG.getRole();

            choosen.joinRole(role2);
            choosen.setCamp(choosen.getCamp().equals(role1.getBaseCamp())? role2.getBaseCamp() : choosen.getCamp());

            firstChoiceLG.joinRole(role1);
            firstChoiceLG.setCamp(firstChoiceLG.getCamp().equals(role1.getBaseCamp())? role1.getBaseCamp() : firstChoiceLG.getCamp());

            choosen.sendMessage(LG.getPrefix() + "§bLe " + this.getDisplayName() + " §ba échangé votre rôle. Vous êtes désormais " + role2.getDisplayName() + "§b.");
            GameLG.playNegativeSound(choosen.getPlayer());
            firstChoiceLG.sendMessage(LG.getPrefix() + "§bLe " + this.getDisplayName() + " §ba échangé votre rôle. Vous êtes désormais " + role1.getDisplayName() + "§b.");
            GameLG.playNegativeSound(firstChoiceLG.getPlayer());

            playerLG.sendMessage(LG.getPrefix() + "§bVous avez échangés les rôles de " + choosen.getNameWithAttributes(playerLG) + " §bet " + firstChoiceLG.getNameWithAttributes(playerLG) + "§b.");
            GameLG.playPositiveSound(playerLG.getPlayer());
            playerLG.setCanUsePowers(false);

            return true;
        } else {
            playerLG.getCache().put("vilainGarconFirstChoice", choosen);
            return false;
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

}
