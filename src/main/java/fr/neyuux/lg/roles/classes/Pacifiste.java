package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.ResetEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteStartEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class Pacifiste extends Role {

    private static final List<PlayerLG> TO_REVEAL = new ArrayList<>();

    @Override
    public String getDisplayName() {
        return "§d§lPacifiste";
    }

    @Override
    public String getConfigName() {
        return "Pacifiste";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez révéler le rôle d'un joueur et empêcher tous les joueurs de voter ce jour là.";
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
        return "§fVous avez §d" + this.getTimeout() + " secondes §fpour choisir quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    reveal(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§fVoulez-vous §drévéler le rôle§f de " + paramPlayerLG.getNameWithAttributes(playerLG) + "§f ?", "§fCela annulera également le prochain vote du village.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    reveal(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void reveal(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        TO_REVEAL.add(choosen);
        playerLG.setCanUsePowers(false);

        playerLG.sendMessage(LG.getPrefix() + "§dLe rôle de " + choosen.getNameWithAttributes(playerLG) + " §dsera révélé au lever du soleil.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    

    @EventHandler
    public void onVoteStart(VoteStartEvent ev) {
        VoteLG vote = ev.getVote();

        if (vote.getName().equals("Vote du Village") && !TO_REVEAL.isEmpty()) {
            int seconds = TO_REVEAL.size() * 3;
            int currentseconds = 0;

            vote.getVoters().clear();

            for (PlayerLG revealed : TO_REVEAL) {

                Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {

                    Bukkit.broadcastMessage("");
                    GameLG.sendTitleToAllPlayers("§d§l" + revealed.getName() + " §fest " + revealed.getRole().getDisplayName() + "§f !", "§fLe " + this.getDisplayName() + " §fa révélé son rôle !", 6, 48, 6);
                    Bukkit.broadcastMessage(LG.getPrefix() + "§fLe " + this.getDisplayName() + " §fa révélé le rôlé de §e§l" + revealed.getName() + "§f, il est " + revealed.getRole().getDisplayName() + "§f.");
                }, currentseconds * 20L + 1L);

                currentseconds += 3;
            }

            Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(LG.getPrefix() + "§cL'utilisation du pouvoir du " + this.getDisplayName() + " §crequiert cependant l'annulation des votes.");
                vote.end(true);
                TO_REVEAL.clear();
            }, seconds * 20L + 5L);
        }
    }

    @EventHandler
    public void onReset(ResetEvent ev) {
        TO_REVEAL.clear();
    }
}
