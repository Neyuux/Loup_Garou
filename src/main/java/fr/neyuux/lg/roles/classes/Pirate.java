package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.event.VoteEndEvent;
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

public class Pirate extends Role {

    @Override
    public String getDisplayName() {
        return "�e�lPirate";
    }

    @Override
    public String getConfigName() {
        return "Pirate";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�r, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez prendre un joueur en otage. Cela vous permettra �9faire tuer�f votre otage � votre place si vous �tes vis� par le vote du village.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �e" + this.getTimeout() + " secondes �fpour voter pour prendre en otage.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    hostage(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"�eVoulez-vous �cprendre en otage " + paramPlayerLG.getNameWithAttributes(playerLG) + "�e ?", "�eIl mourra � votre place si vous obtenez.", "�ele plus de voix au vote du village.", "", "�7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    hostage(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }
    private void hostage(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        playerLG.getCache().put("pirateHostage", choosen);

        choosen.sendMessage(LG.getPrefix() + "�eLe " + this.getDisplayName() + "�e t'as choisi comme otage !");
        playerLG.sendMessage(LG.getPrefix() + "�eTu as choisi �c" + choosen.getNameWithAttributes(playerLG) + "�e comme otage pour le restant de la partie.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }


    @EventHandler
    public void onClosePirateInv(InventoryCloseEvent ev) {
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

    @EventHandler
    public void onVoteEnd(VoteEndEvent ev) {
        VoteLG vote = ev.getVote();
        PlayerLG choosen = vote.getChoosen();

        if (vote.getName().equals("Vote du Village") && choosen.getRole() instanceof Pirate && choosen.getCache().has("pirateHostage")) {
            PlayerLG hostage = (PlayerLG) choosen.getCache().get("pirateHostage");

            vote.setChoosen(hostage);
            Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(LG.getPrefix() + "�eLe " + Pirate.this.getDisplayName() + " �e�l" + choosen.getName() + " �6poss�dait �c�l" + hostage.getName() + " �6en otage. Ce dernier sera donc pendu � la place du Pirate.");
            }, 1L);
        }
    }
}
