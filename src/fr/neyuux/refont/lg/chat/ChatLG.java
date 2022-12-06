package fr.neyuux.refont.lg.chat;

import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatLG implements Listener {

    private List<PlayerLG> spies;
    private List<PlayerLG> actors;
    private final ChatColor textColor;
    private final Character textCode;
    private final String namePrefix;

    public ChatLG(String namePrefix, ChatColor textColor, Character textCode) {
        this.namePrefix = namePrefix;
        this.textColor = textColor;
        this.textCode = textCode;
    }


    public void openChat(List<PlayerLG> spies, List<PlayerLG> actors) {
        this.spies = spies;
        this.actors = actors;
    }

    public void closeChat() {
        this.spies = null;
        this.actors = null;
    }

    public void addSpies(ArrayList<PlayerLG> playerLGS) {
        this.spies.addAll(playerLGS);
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent ev) {
        String msg = ev.getMessage();

        if (this.textCode == null || this.textCode.compareTo(msg.charAt(0)) == 0) {
            this.actors.forEach(actorLG -> actorLG.sendMessage(namePrefix + PlayerLG.createPlayerLG(ev.getPlayer()).getNameWithAttributes(actorLG) + " §8§l» " + textColor + msg.substring(1).trim()));
            this.spies.forEach(spyLG -> spyLG.sendMessage(namePrefix + "§kZIZITOUDUR" + " §8§l» " + textColor + msg.substring(1).trim()));
        }
    }

}
