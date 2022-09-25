package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.*;
import fr.neyuux.refont.lg.utils.CacheLG;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PlayerLG {

    private static final HashMap<HumanEntity, PlayerLG> playerHashMap = new HashMap<>();
    
    private GameLG game;

    public PlayerLG(HumanEntity human) {
        this.human = human;
        this.game = LG.getInstance().getGame();
    }

    private final HumanEntity human;

    private Role role;

    private final CacheLG cache = new CacheLG();

    private Block block;

    private Camps camp;

    private boolean isDead;

    private boolean isMayor;

    private final List<PlayerLG> blacklistChoice = new ArrayList<>();


    public void sendMessage(String msg) {
        if (this.human != null) {
            this.getPlayer().sendMessage(LG.getPrefix() + msg);
        }
    }

    public void sendActionBar(String msg) {
        if (this.human == null) return;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        try {
            ((CraftPlayer) this.human).getHandle().playerConnection.sendPacket(ppoc);
        } catch (NullPointerException e) {e.printStackTrace();}
    }

    public void sendTitle(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        if (this.human == null) return;
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
        if (this.human != null)
            return this.human.getName();
        return null;
    }

    public String getDisplayName() {
        if (this.human != null && this.human instanceof Player)
            return ((Player) this.human).getDisplayName();
        return null;
    }

    public Location getLocation() {
        if (this.human != null)
            return this.human.getLocation();
        return null;
    }

    public String getNameWithAttributes(PlayerLG receiver) {
        String name = "";

        if (this.isLG() && receiver.isLG()) {
            if (receiver.getPlayer().getScoreboard().getTeam("LG").hasEntry(this.getPlayer().getName())) {
                name = "§c§lLG §c";
            }
        }

        if (receiver.getRole() instanceof Soeur) {
            PlayerLG soeur = (PlayerLG) receiver.getCache().get("soeur");

            if (soeur != null && soeur.equals(this))
                name += "§d§lSoeur §d";
        }

        if (receiver.getRole() instanceof Frere) {
            List<PlayerLG> brothers = (List<PlayerLG>) receiver.getCache().get("frere");

            if (brothers != null && brothers.contains(this)) {
                name += "§3§lFrère §d";
            }
        }

        if (this.getRole() instanceof VillageoisVillageois) {
            name += name + "§a§lV§e-§a§lV §a";
        }

        if (this.isMayor()) {
            name += "§6§lMaire §e";
        }

        if (this.getCache().has("couple") && this.getCache().get("couple") != null) {
            PlayerLG couple = (PlayerLG) this.getCache().get("couple");

            if (couple.equals(receiver) || receiver.getRole() instanceof Cupidon) {
                name += "§d§lCouple §d";
            }
        }

        if (receiver.getRole() instanceof Mercenaire && this.game.getDay() == 1) {
            PlayerLG target = (PlayerLG) receiver.getCache().get("target");

            if (target != null && target.equals(this))
                name += "§c§lCible §5";
        }

        if (receiver.getRole() instanceof Jumeau) {
            PlayerLG twin = (PlayerLG) receiver.getCache().get("twin");

            if (twin != null && twin.equals(this))
                name += "§5§lJumeau §d";
        }

        name += this.human.getName();
        return name;
    }

    public Player getPlayer() {
        if (this.human instanceof Player)
            return (Player)this.human;
        return null;
    }

    public boolean isOP() {
        if (this.game == null) {
            this.game = LG.getInstance().getGame();
            return true;
        } else if (this.game != LG.getInstance().getGame())
            this.game = LG.getInstance().getGame();
        return this.game.getOPs().contains(this.human);
    }

    public boolean isSpectator() {
        System.out.println(game + " / " + LG.getInstance().getGame());
        if (this.game == null) {
            this.game = LG.getInstance().getGame();
            return false;
        } else if (this.game != LG.getInstance().getGame())
            this.game = LG.getInstance().getGame();
        System.out.println(this.game.getSpectators());
        return this.game.getSpectators().contains(this);
    }

    public boolean isInGame() {
        if (this.game == null) {
            this.game = LG.getInstance().getGame();
            return false;
        } else if (this.game != LG.getInstance().getGame())
            this.game = LG.getInstance().getGame();
        return this.game.getPlayersInGame().contains(this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLG () {
        return this.game.getLGs().contains(this);
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

    public boolean isMayor() {
        return isMayor;
    }

    public void setMayor(boolean mayor) {
        isMayor = mayor;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }


    public static PlayerLG createPlayerLG(HumanEntity human) {
        if (playerHashMap.containsKey(human))
            return playerHashMap.get(human);

        PlayerLG plg = new PlayerLG(human);
        playerHashMap.put(human, plg);
        return plg;
    }

    public static PlayerLG removePlayerLG(HumanEntity human) {
        return playerHashMap.remove(human);
    }

    public CacheLG getCache() {
        return cache;
    }

    private void sendPacket(Object packet) {
        if (this.human == null) return;
        try {
            Player player = (Player)human;
            Object handle = (player).getClass().getMethod("getHandle").invoke(player);
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

    public static HashMap<HumanEntity, PlayerLG> getPlayersMap() {
        return playerHashMap;
    }
}
