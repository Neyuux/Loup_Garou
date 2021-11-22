package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlayerLG {

    private static final HashMap<Player, PlayerLG> playerHashMap = new HashMap<>();

    public PlayerLG(Player player) {
        this.player = player;
    }

    private final Player player;

    private Role role;

    private int place;

    private Camps camp;

    private boolean isDead;

    private List<PlayerLG> blacklistChoice = new ArrayList<>();


    public void sendMessage(String msg) {
        if (this.player != null) {
            this.getPlayer().sendMessage(LG.getPrefix() + msg);
        }
    }

    public void sendActionBar(String msg) {
        if (this.player == null) return;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        try {
            ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(ppoc);
        } catch (NullPointerException e) {e.printStackTrace();}
    }

    public void sendTitle(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        if (this.player == null) return;
        try {
            Object chatTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(
                    Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(
                    Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                    fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = Objects.requireNonNull(getNMSClass("IChatBaseComponent")).getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getConstructor(
                    Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(
                    Objects.requireNonNull(getNMSClass("PacketPlayOutTitle")).getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
                    fadeInTime, showTime, fadeOutTime);

            sendPacket(packet);
            sendPacket(timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        if (this.player != null)
            return this.player.getName();
        return null;
    }

    public String getDisplayName() {
        if (this.player != null)
            return this.player.getDisplayName();
        return null;
    }

    public String getNameWithAttributes(PlayerLG receiver) {
        String name = "";

        if (LG.getInstance().getGame())
    }

    public Player getPlayer() {
        return player;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Camps getCamp() {
        return this.camp;
    }

    public void setCamp(Camps camp) {
        this.camp = camp;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }


    public static PlayerLG createPlayerLG(Player player) {
        if (playerHashMap.containsKey(player))
            return playerHashMap.get(player);
        PlayerLG plg = new PlayerLG(player);
        playerHashMap.put(player, plg);
        return plg;
    }

    public static PlayerLG removePlayerLG(Player player) {
        return playerHashMap.remove(player);
    }

    private void sendPacket(Object packet) {
        if (this.player == null) return;
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
