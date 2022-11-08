package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.RoleChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Bouffon extends Role {

    private static final List<PlayerLG> needToPlay = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "�d�lBouffon";
    }

    @Override
    public String getConfigName() {
        return "Bouffon";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est de vous faire voter par le village. Si vous r�ussissez : vous aurez la possibilit� de tuer un des joueurs qui a vot� pour vous.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �d " + this.getTimeout() + " secondes�f pour tuer un joueur.";
    }


    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (needToPlay.isEmpty()) {
            callback.run();
            return;
        }

        PlayerLG playerLG = needToPlay.remove(0);


        playerLG.getPlayer().setGameMode(GameMode.ADVENTURE);
        playerLG.setWake();

        game.wait(this.getTimeout(), () -> {
            this.onPlayerTurnFinish(playerLG);
            this.onNightTurn(callback);
         }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "�9�lA toi de jouer !" : "�9�lAu tour " + this.getDeterminingName());

        playerLG.sendMessage("" + this.getActionMessage());
        this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        playerLG.getPlayer().setGameMode(GameMode.SPECTATOR);
        super.onPlayerTurnFinish(playerLG);
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> choosable = new ArrayList<>();
        for (PlayerLG voterLG : LG.getInstance().getGame().getAlive())
            if (!voterLG.isDead()) //TODO hasvotedForBouffon
                choosable.add(voterLG);

        if (game.getGameType().equals(GameType.MEETING)) {

            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    haunt(choosen, playerLG, choosable);
                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (game.getGameType().equals(GameType.FREE)) {
            new RoleChoosePlayerInv(this.getDisplayName(), playerLG, choosable, new RoleChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"�7Voulez-vous �5hanter " + paramPlayerLG.getNameWithAttributes(playerLG) + "�7 ?", "�7Il sera �c�limin�d de la partie.", "", "�7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    haunt(choosenLG, playerLG, choosable);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            });
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void haunt(PlayerLG choosen, PlayerLG playerLG, List<PlayerLG> choosable) {
        if (choosen == null) return;

        if (choosable.contains(choosen)) {
            RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

            Bukkit.getPluginManager().callEvent(roleChoiceEvent);
            if (roleChoiceEvent.isCancelled()) return;

            LG.getInstance().getGame().kill(choosen);

            playerLG.sendMessage(LG.getPrefix() + "�7Tu d�cides de hanter " + choosen.getNameWithAttributes(playerLG) + "�7.");
            GameLG.playPositiveSound(playerLG.getPlayer());

        } else {
            playerLG.sendMessage(LG.getPrefix() + "�cCe joueur n'a pas vot� pour vous !");
            GameLG.playNegativeSound(playerLG.getPlayer());
        }
    }


    @EventHandler
    public void onCloseBouffonInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();

        if (inv.getName().equals(this.getDisplayName()) && (boolean)PlayerLG.createPlayerLG(player).getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(inv);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

}
