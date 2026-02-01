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

public class Jumeau extends Role {

    @Override
    public String getDisplayName() {
        return "§5§lJumeau";
    }

    @Override
    public String getConfigName() {
        return "Jumeau";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lors de la première nuit, vous devrez choisir un joueur. Lorsque ce joueur mourra, vous obtiendrez son rôle.";
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
        return "§fVous avez §5" + this.getTimeout() + " secondes §fpour choisir qui va être votre jumeau.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    setTwin(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§5Voulez-vous choisir " + paramPlayerLG.getNameWithAttributes(playerLG) + "§5comme §dmodèle§5 ?", "§5Vous obtiendrez son rôle s'il meurt.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    setTwin(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void setTwin(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        playerLG.getCache().put("jumeau", choosen);

        playerLG.sendMessage(LG.getPrefix() + "§5Tu as choisi " + choosen.getNameWithAttributes(playerLG) + "§5 comme modèle.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

     @EventHandler
    public void onJumeauDeath(PlayerEliminationEvent ev) {
        PlayerLG dead = ev.getEliminated();
        LG main = LG.getInstance();
        GameLG game = main.getGame();

        for (PlayerLG playerLG : game.getPlayersByRole(Jumeau.class)) {
            if (playerLG.getCache().has("jumeau") && playerLG.getCache().get("jumeau").equals(dead)) {
                playerLG.setRole(dead.getRole());
                
                Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                    playerLG.sendMessage(LG.getPrefix() + "§5Votre jumeau est mort ! Vous reprenez donc son rôle et ses attributs.");
                    playerLG.joinRole(dead.getRole());

                    if (dead.getCache().has("infected")) {
                        playerLG.getCache().put("infected", new Object());

                        playerLG.joinCamp(Camps.LOUP_GAROU);
                        playerLG.sendMessage(LG.getPrefix() + "§cEn reprenant le rôle de votre Jumeau, vous vous rendez compte qu'il était infecté. Vous passez donc dans le camp des Loups-Garou.");
                        LG.getInstance().getGame().getLGs(false).forEach(PlayerLG::updateGamePlayerScoreboard);
                    }
                }, 1L);

            }
        }
     }
}
