package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class Corbeau extends Role {

    @Override
    public String getDisplayName() {
        return "§8§lCorbeau";
    }

    @Override
    public String getConfigName() {
        return "Corbeau";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez désigner un joueur qui §9recevra 2 votes§f au petit matin...";
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
        return "§fVous avez §7" + getTimeout() + " secondes§f pour choisir qui recevra 2 votes au début du jour.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    visit(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§fVoulez-vous §7rendre visite§f à " + paramPlayerLG.getNameWithAttributes(playerLG) + "§f ?", "§fIl aura 2 votes au début du jour.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    visit(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void visit(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        choosen.getCache().put("corbeauTargeted", true);

        playerLG.sendMessage(LG.getPrefix() + "§fTu as rendu visite à " + choosen.getNameWithAttributes(playerLG) + "§f.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

    @EventHandler
    public void onNightStart(NightStartEvent ev) {
        for (PlayerLG playerLG : LG.getInstance().getGame().getAlive())
            playerLG.getCache().remove("corbeauTargeted");
    }

    @EventHandler
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();

        if (!vote.getName().equals("Vote du Village")) return;

        for (PlayerLG playerLG : vote.getVotable())
            if (playerLG.getCache().has("corbeauTargeted")) {

                vote.updateArmorStand(playerLG);
                Bukkit.broadcastMessage(LG.getPrefix() + "§7Le " + this.getDisplayName() + " §7a rendu visite à §e" + playerLG.getName() + "§7.");
            }
    }
}
