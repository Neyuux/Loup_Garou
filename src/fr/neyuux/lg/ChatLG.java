package fr.neyuux.lg;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatLG implements Listener {

    private Set<PlayerLG> spies;
    private Set<PlayerLG> actors;
    private final ChatColor textColor;
    private final Character textCode;
    private final String namePrefix;

    public ChatLG(String namePrefix, ChatColor textColor, Character textCode) {
        this.namePrefix = namePrefix;
        this.textColor = textColor;
        this.textCode = textCode;
    }


    public void openChat(Set<PlayerLG> spies, Set<PlayerLG> actors) {
        this.spies = spies;
        this.actors = actors;
        LG.getInstance().getServer().getPluginManager().registerEvents(this, LG.getInstance());
    }

    public void closeChat() {
        this.spies = null;
        this.actors = null;
    }

    public void addSpies(Set<PlayerLG> playerLGS) {
        this.spies.addAll(playerLGS);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        String msg = ev.getMessage();
        char first = msg.charAt(0);

        if (this.actors == null) return;

        ev.setCancelled(true);
        if (this.textCode == null)
            if (!Character.isAlphabetic(first) && !Character.isDigit(first))
                return;
        if ((this.textCode == null || this.textCode.compareTo(first) == 0) && this.actors.contains(PlayerLG.createPlayerLG(ev.getPlayer()))) {
            this.actors.forEach(actorLG -> actorLG.sendMessage(namePrefix + PlayerLG.createPlayerLG(ev.getPlayer()).getNameWithAttributes(actorLG) + " §8§l» " + textColor + msg.substring(1).trim()));
            this.spies.forEach(spyLG -> spyLG.sendMessage(namePrefix + "§kZIZITOUDUR" + " §8§l» " + textColor + msg.substring(1).trim()));
        }
    }

}
