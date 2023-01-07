package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.*;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Vampire extends Role {

    private final static ChatLG CHAT = new ChatLG("§6", ChatColor.RED, null);

    @Override
    public String getDisplayName() {
        return "§5§lVampire";
    }

    @Override
    public String getConfigName() {
        return "Vampire";
    }

    @Override
    public String getDeterminingName() {
        return "des " + this.getDisplayName() + "s";
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+", votre but est d'éliminer tous les joueurs qui ne sont pas des Vampires. Chaque nuit vous vous réveillez pour §9mordre§f un joueur. Cette morsure §9transformera§f le joueur en Vampire une nuit sur deux, ou sinon §9tuera§f le joueur.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VAMPIRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d" + this.getTimeout() + " secondes §fpour voter pour choisir qui mordre.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> voters = new ArrayList<>();

        for (PlayerLG vampire : game.getPlayersByRole(this.getClass()))
            if (vampire.canUsePowers()) {
                voters.add(vampire);
                playerLG.setWake();
            }

        CHAT.openChat(new HashSet<>(), new HashSet<>(voters));

        VoteLG lgvote = new VoteLG("Vote des Vampires", this.getTimeout(), true, (paramPlayerLG, secondsLeft) -> {
            if (paramPlayerLG.getCache().has("vote"))
                if (paramPlayerLG.getCache().get("vote") == null)
                    return LG.getPrefix() + "§5Vous ne votez pour §4§lpersonne§c.";
                else
                    return LG.getPrefix() + "§5Vous votez pour §4§l" + ((PlayerLG)paramPlayerLG.getCache().get("vote")).getName() + "§5.";

            return LG.getPrefix() + "§fAu tour " + this.getDeterminingName();
        }, ChatColor.RED, ChatColor.DARK_PURPLE, game.getAlive(), voters, voters);

        lgvote.start(() -> {
            eliminate(lgvote.getChoosen());

            CHAT.closeChat();

            for (PlayerLG lg : game.getPlayersByRole(this.getClass())) {
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

        game.getPlayersByRole(this.getClass()).forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + "§5Vous avez mordu §4" + choosen.getNameWithAttributes(playerLG) + "§5."));

        if (game.getNight() % 2 == 0) game.kill(choosen);
        else {
            game.getPlayersByRole(this.getClass()).forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + " §d a rejoint le camp des Vampires !"));
            choosen.getCache().put("vampireInfected", choosen.getRole());
            choosen.joinRole(new Vampire());
            choosen.setCamp(Camps.VAMPIRE);
            choosen.sendMessage(LG.getPrefix() + "§5Vous avez été infecté par un vampire ! Vous devenez l'un d'entre eux et perdez vos pouvoirs.");
            choosen.getPlayer().playSound(choosen.getLocation(), Sound.ENDERDRAGON_WINGS, 8f, 1.6f);
        }
    }
}
