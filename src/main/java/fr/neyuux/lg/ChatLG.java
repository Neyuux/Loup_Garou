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
        LG.getInstance().getGame().registerEvents(this);
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
        String msg = ev.getMessage().trim();
        char first = msg.charAt(0);

        if (this.actors == null) return;

        ev.setCancelled(true);

        if (this.actors.contains(PlayerLG.createPlayerLG(ev.getPlayer()))) {
            if (this.textCode == null) {
                if (!Character.isAlphabetic(first) && !Character.isDigit(first))
                    return;

            } else {
                if (this.textCode.compareTo(first) == 0) msg = msg.substring(1);
                else return;
            }
            String finalMsg = msg;

            this.actors.forEach(actorLG -> actorLG.sendMessage(namePrefix + PlayerLG.createPlayerLG(ev.getPlayer()).getNameWithAttributes(actorLG) + " §8§l» " + textColor + finalMsg));
            this.spies.forEach(spyLG -> spyLG.sendMessage(namePrefix + "§kZIZITOUDUR" + " §8§l» " + textColor + finalMsg));
        }

    }

}
