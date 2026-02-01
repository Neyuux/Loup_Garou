package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class GrandMechantLoup extends Role {

    @Override
    public String getDisplayName() {
        return "§4§lGrand Méchant §c§lLoup";
    }

    @Override
    public String getConfigName() {
        return "Grand Méchant Loup";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Mais après cela, vous vous réveillez une deuxième fois pour §9dévorer une deuxième personne§f...";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.LOUP_GAROU;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §4" + this.getTimeout() + " secondes§f pour dévorer une deuxième personne.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    kill(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(game.getLGs(true).toArray(new PlayerLG[0])), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§cVoulez-vous §4tuer " + paramPlayerLG.getNameWithAttributes(playerLG) + "§c ?", "§cIl sera éliminé de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    kill(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void kill(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        LG.getInstance().getGame().kill(choosen);

        playerLG.sendMessage(LG.getPrefix() + "§4Tu as tué " + choosen.getNameWithAttributes(playerLG) + "§4.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    

    @EventHandler
    public void onLGElimination(PlayerEliminationEvent ev) {
        if (ev.getEliminated().isLG())
            for (PlayerLG playerLG : LG.getInstance().getGame().getPlayersByRole(this.getClass()))
                playerLG.setCanUsePowers(false);
    }
}
