package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.*;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class LoupGarou extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lLoup-Garou";
    }

    @Override
    public String getConfigName() {
        return "Loup-Garou";
    }

    @Override
    public String getDeterminingName() {
        return "des " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer...";
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
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §c" + this.getTimeout() + " secondes §fpour voter pour choisir qui dévorer.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> voters = new ArrayList<>();

        for (PlayerLG lg : game.getLGs())
            if (lg.canUsePowers()) {
                voters.add(lg);
                playerLG.setWake();
            }

        VoteLG lgvote = new VoteLG(50, true, (paramPlayerLG, secondsLeft) -> {
            if (paramPlayerLG.getCache().has("vote"))
                if (paramPlayerLG.getCache().get("vote") == null)
                    return LG.getPrefix() + "§cVous ne votez pour §4§lpersonne§c.";
                else
                    return LG.getPrefix() + "§cVous votez pour §4§l" + ((PlayerLG)paramPlayerLG.getCache().get("vote")).getName() + "§c.";

            return null;
        }, ChatColor.RED, ChatColor.DARK_RED, game.getAlive(), voters, voters);

        lgvote.start(() -> {
            eliminate(lgvote.getChoosen());

            for (PlayerLG lg : game.getLGs()) {
                lg.getPlayer().closeInventory();
                lg.setSleep();
            }
            callback.run();
        });
    }

    private void eliminate(PlayerLG choosen) {
        if (choosen == null) return;

        GameLG game = LG.getInstance().getGame();
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        game.kill(choosen);

        game.getLGs().forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + "§cVous avez dévoré §4" + choosen.getNameWithAttributes(playerLG) + "§c."));
    }
}
