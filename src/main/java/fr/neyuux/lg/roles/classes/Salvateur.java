package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.DayStartEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class Salvateur extends Role {

    private PlayerLG choosen;

    @Override
    public String getDisplayName() {
        return "§e§lSalvateur";
    }

    @Override
    public String getConfigName() {
        return "Salvateur";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Chaque nuit, vous pourrez §9protéger§f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas protéger deux fois la même personne.";
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
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §e" + this.getTimeout() + " secondes§f pour protéger quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    protect(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§eVoulez-vous §9protéger " + paramPlayerLG.getNameWithAttributes(playerLG) + "§e ?", "§eIl ne pourra pas mourir des Loups-Garous pour ce tour.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    protect(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void protect(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        choosen.getCache().put("salvateurProtected", playerLG);
        this.choosen = choosen;

        playerLG.sendMessage(LG.getPrefix() + "§eTu as protégé " + choosen.getNameWithAttributes(playerLG) + "§e.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    

    @EventHandler
    public void onLGChoose(RoleChoiceEvent ev) {
        PlayerLG choosenLG = ev.getChoosen();
        if (ev.getRole().getClass().equals(LoupGarou.class) && choosenLG.getCache().has("salvateurProtected")) {
            ev.setCancelled(true);
        }
        if (choosen != null)this.choosen.getCache().remove("salvateurProtected");
        this.choosen = null;
    }

    @EventHandler
    public void onDayS(DayStartEvent ev) {
        if (choosen != null)this.choosen.getCache().remove("salvateurProtected");
        this.choosen = null;
    }
}
