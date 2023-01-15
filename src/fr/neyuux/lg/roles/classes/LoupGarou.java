package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.*;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LoupGarou extends Role {

    public static final ChatLG CHAT = new ChatLG("§c", ChatColor.RED, null);
    private static PlayerLG lastTargetedByLG;

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
        return "des §c§lLoups-Garous";
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
        return 50;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §c" + this.getTimeout() + " secondes §fpour voter pour choisir qui dévorer.";
    }


    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> players = game.getLGs(true);

        game.cancelWait();

        if (players.isEmpty()) {
            if (game.isNotThiefRole(this)) callback.run();
            else LG.getInstance().getGame().wait(LoupGarou.this.getTimeout() / 4, callback, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + LoupGarou.this.getDeterminingName(), true);
            return;
        }

        List<PlayerLG> voters = new ArrayList<>();

        for (PlayerLG lg : players)
            if (lg.canUsePowers()) {
                voters.add(lg);
                lg.setWake();
            }

        if ((boolean)game.getConfig().getChatLG().getValue()) CHAT.openChat(new HashSet<>(), new HashSet<>(voters));

        VoteLG lgvote = new VoteLG("Vote des Loups", this.getTimeout(), true, (paramPlayerLG, secondsLeft) -> {
            if (paramPlayerLG.getCache().has("vote"))
                if (paramPlayerLG.getCache().get("vote") == null)
                    return LG.getPrefix() + "§cVous ne votez pour §4§lpersonne§c.";
                else
                    return LG.getPrefix() + "§cVous votez pour §4§l" + ((PlayerLG)paramPlayerLG.getCache().get("vote")).getName() + "§c.";

            return LG.getPrefix() + "§fAu tour " + this.getDeterminingName();
        }, ChatColor.RED, ChatColor.DARK_RED, game.getAlive(), voters, voters);

        lgvote.start(() -> {
            eliminate(lgvote.getChoosen());

            CHAT.closeChat();

            for (PlayerLG lg : game.getLGs(true)) {
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
        setLastTargetedByLG(choosen);

        game.getLGs(true).forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + "§cVous avez dévoré §4" + choosen.getNameWithAttributes(playerLG) + "§c."));
    }


    @EventHandler
    public void onEndNight(NightStartEvent ev) {
        lastTargetedByLG = null;
    }


    public static PlayerLG getLastTargetedByLG() {
        return lastTargetedByLG;
    }

    public static void setLastTargetedByLG(PlayerLG newlasttarget) {
        lastTargetedByLG = newlasttarget;
    }
}
