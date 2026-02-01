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

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    check(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§dVoulez-vous §5regarder l'aura §dà " + paramPlayerLG.getNameWithAttributes(playerLG) + "§d ?", "§dVous découvrez s'il est Loup-Garou ou non.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    check(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void check(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        if (LG.getInstance().getGame().getLGs(false).contains(choosen)) {
            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + " §da une Aura §4§lSombre§d, il est donc du côté des Loups-Garous !");
            playerLG.sendTitle("§4§lSombre", "§dVous avez senti l'aura de §5§l" + choosen.getDisplayName() + "§d !", 5, 80, 5);
            if ((boolean)LG.getInstance().getGame().getConfig().getChattyVoyante().getValue())
                playerLG.sendMessage(LG.getPrefix() + "§dLa " + this.getDisplayName() + "§d a espionné un joueur dont l'aura est §4§lSombre§d !");


        } else {
            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + " §da une Aura §f§lClaire§d, il n'est donc §apas §ddu côté des Loups-Garous !");
            playerLG.sendTitle("§f§lClaire", "§dVous avez senti l'aura de §5§l" + choosen.getDisplayName() + "§d !", 5, 80, 5);
            if ((boolean)LG.getInstance().getGame().getConfig().getChattyVoyante().getValue())
                playerLG.sendMessage(LG.getPrefix() + "§dLa " + this.getDisplayName() + "§d a espionné un joueur dont l'aura est §f§lClaire§d !");
        }

        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    
}
