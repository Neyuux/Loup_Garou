package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

public class Noctambule extends Role {
    @Override
    public String getDisplayName() {
        return "§9§lNoctambule";
    }

    @Override
    public String getConfigName() {
        return "Noctambule";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous devez choisir un joueur chez qui §9dormir§f, ce joueur connaîtra alors votre identité mais sera privé de ses pouvoirs pour la nuit.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.ONLINE;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §9" + this.getTimeout() + " secondes§f pour aller dormir chez quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    sleepWith(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§bVoulez-vous §9dormir §bchez §1" + paramPlayerLG.getNameWithAttributes(playerLG) + "§b ?", "§bIl connaîtra votre identité.", "§bCependant, il ne pourra pas utiliser ses pouvoirs cette nuit.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    sleepWith(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void sleepWith(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        choosen.setCanUsePowers(false);
        choosen.getCache().put("noctambuleCantUsePower", false);
        choosen.sendMessage(LG.getPrefix() + "§bLe " + this.getDisplayName() + " §1" + playerLG.getNameWithAttributes(choosen) + " ");
        choosen.getPlayer().playSound(choosen.getLocation(), Sound.WOLF_GROWL, 8f, 1.2f);

        playerLG.sendMessage(LG.getPrefix() + "§bTu dors chez " + choosen.getNameWithAttributes(playerLG) + "§b.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

    @EventHandler
    public void onNightEnd(NightEndEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            if (playerLG.getCache().has("noctambuleCantUsePower")) {
                playerLG.getCache().remove("noctambuleCantUsePower");
                playerLG.setCanUsePowers(true);
            }
    }
}
