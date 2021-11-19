package fr.neyuux.refont.lg.chat;

import fr.neyuux.refont.lg.PlayerLG;

import java.util.HashMap;
import java.util.Map;

public class ChatLG {

    private final ChatLGCallback defaultCallback;
    private final HashMap<PlayerLG, ChatLGCallback> viewers = new HashMap<>();

    public ChatLG(ChatLGCallback defaultCallback) {
        this.defaultCallback = defaultCallback;
    }


    public void sendMessage(PlayerLG sender, String msg) {
        String sending = this.getViewers().get(sender).send(sender, msg);

        for (Map.Entry<PlayerLG, ChatLGCallback> entry : this.getViewers().entrySet())
            entry.getKey().sendMessage(sending != null ? sending : entry.getValue().receive(sender, msg));
    }

    public void join(PlayerLG playerLG, ChatLGCallback callback) {
        if (this.getViewers().containsKey(playerLG)) this.getViewers().replace(playerLG, callback);
        else this.getViewers().put(playerLG, callback);
    }

    public ChatLGCallback getDefaultCallback() {
        return defaultCallback;
    }

    public HashMap<PlayerLG, ChatLGCallback> getViewers() {
        return viewers;
    }


    public interface ChatLGCallback {
        String receive(PlayerLG sender, String msg);

        default String send(PlayerLG sender, String msg) {return null;}
    }
}