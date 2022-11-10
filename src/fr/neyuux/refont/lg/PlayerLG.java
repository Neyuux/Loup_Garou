package fr.neyuux.refont.lg;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.roles.classes.*;
import fr.neyuux.refont.lg.utils.CacheLG;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.util.*;

public class PlayerLG {

    private static final HashMap<HumanEntity, PlayerLG> playerHashMap = new HashMap<>();
    
    private final GameLG game;

    public PlayerLG(HumanEntity human) {
        this.human = human;
        this.game = LG.getInstance().getGame();
    }

    private final HumanEntity human;

    private Role role;

    private final CacheLG cache = new CacheLG();

    private Camps camp;

    private boolean isDead = false;

    private boolean isMayor = false;

    private boolean canUsePowers = true;

    private boolean isSleeping = false;

    private Location placement;

    private CallbackChoice callbackChoice;

    private ArmorStand armorStand;


    public void sendMessage(String msg) {
        if (this.human != null) {
            this.getPlayer().sendMessage(msg);
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

    public void showAllPlayers() {
        this.showPlayer(Bukkit.getOnlinePlayers().toArray(new Player[]{}));
    }

    public void showPlayer(Player... hided) {
        for (Player p : hided)
            this.getPlayer().showPlayer(p);
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
        String lastcolor = "§e";

        if (this.isLG() && receiver.isLG()) {
            name = "§c§lLG ";
            lastcolor = "§c";
        }

        if (receiver.getRole() instanceof Soeur) {
            Soeur soeur = (Soeur) receiver.getRole();

            if (soeur.getSister().equals(this))
                name += "§d§lSoeur ";
            lastcolor = "§d";
        }

        if (receiver.getRole() instanceof Frere) {
            Frere brother = (Frere)receiver.getRole();

            if (brother.getBrothers().contains(this)) {
                name += "§3§lFrère ";
                lastcolor = "§d";
            }
        }

        if (receiver.getRole() instanceof Macon && this.getRole() instanceof Macon) {
            name += "§6§lMaçon ";
            lastcolor = "§d";
        }

        if (this.getRole() instanceof VillageoisVillageois) {
            name += name + "§a§lV§e-§a§lV ";
            lastcolor = "§a";
        }

        if (this.isMayor()) {
            name += "§6§lMaire ";
            lastcolor = "§e";
        }

        if (receiver.getRole() instanceof Mercenaire && this.game.getDay() == 1) {
            PlayerLG target = (PlayerLG) receiver.getCache().get("target");

            if (target != null && target.equals(this)) {
                name += "§c§lCible ";
                lastcolor = "§5";
            }

        }

        if (receiver.getRole() instanceof Jumeau) {
            PlayerLG twin = (PlayerLG) receiver.getCache().get("jumeau");

            if (twin != null && twin.equals(this)) {
                name += "§5§lJumeau ";
                lastcolor = "§d";
            }
        }

        name += lastcolor + this.human.getName();

        if (this.getCache().has("couple") && this.getCache().get("couple") != null || receiver.getRole() instanceof Cupidon) {
            PlayerLG couple = (PlayerLG) this.getCache().get("couple");

            if (couple.equals(receiver) || receiver.getRole() instanceof Cupidon) {
                name += "§d \u2764";
            }
        }
        return name;
    }

    public void createScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();

        LG.registerBaseTeams(scoreboard);
        for (Team team : main.getTeams()) {
            Team newteam = scoreboard.getTeam(team.getName());
            if (newteam != null)
                for (String entry : team.getEntries())
                    newteam.addEntry(entry);
        }
        this.getPlayer().setScoreboard(scoreboard);
    }

    public void updateGamePlayerScoreboard() {
        Scoreboard scoreboard = this.getPlayer().getScoreboard();

        for (PlayerLG targetLG : this.game.getAlive()) {
            Team team = scoreboard.registerNewTeam(targetLG.getName());
            String[] prefixsuffix = targetLG.getNameWithAttributes(this).split(targetLG.getName());

            team.setPrefix(prefixsuffix[0]);
            if (prefixsuffix.length > 1)team.setSuffix(prefixsuffix[1]);
            team.addEntry(targetLG.getName());
        }
    }

    public void setSleep() {
        Player player = this.getPlayer();
        Location loc = this.placement;

        player.teleport(placement);

        for (PlayerLG playerLG : this.game.getAlive())
            player.hidePlayer(playerLG.getPlayer());

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false));

        if (this.game.getGameType().equals(GameType.FREE)) {
            ((CraftPlayer) player).getHandle().a(new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
            player.setSleepingIgnored(false);
        }

        this.isSleeping = true;
    }

    public void setWake() {
        Player player = this.getPlayer();

        this.isSleeping = false;

        if (game.getGameType().equals(GameType.FREE)) player.teleport(player.getBedSpawnLocation());
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        for (PlayerLG playerLG : this.game.getAlive())
            player.showPlayer(playerLG.getPlayer());
    }

    public void setChoosing(CallbackChoice callback) {
        this.callbackChoice = callback;
    }

    public void stopChoosing() {
        this.callbackChoice = null;
    }

    public void callbackChoice(PlayerLG playerLG) {
        this.callbackChoice.callback(playerLG);
    }

    public Player getPlayer() {
        if (this.human instanceof Player)
            return (Player)this.human;
        return null;
    }

    public boolean isOP() {
        return this.game.getOPs().contains(this.human);
    }

    public boolean isSpectator() {
        return this.game.getSpectators().contains(this);
    }

    public boolean isInGame() {
        return this.game.getPlayersInGame().contains(this);
    }

    public Role getRole() {
        return role;
    }

    public void joinRole(Role role) {
        this.setRole(role);
        role.onPlayerJoin(this);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLG () {
        return this.game.getLGs().contains(this);
    }

    public void joinCamp(Camps camp) {
        if (camp.equals(Camps.VAMPIRE) || camp.equals(Camps.LOUP_GAROU))
            game.getAlive().forEach(playerLG -> playerLG.sendMessage(game.getPrefix() + this.getNameWithAttributes(playerLG) + "§c a rejoint votre camp !"));
        this.setCamp(camp);
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

    public boolean canUsePowers() {
        return canUsePowers;
    }

    public void setCanUsePowers(boolean canUsePowers) {
        this.canUsePowers = canUsePowers;
    }

    public Location getPlacement() {
        return placement;
    }

    public void setPlacement(Location placement) {
        this.placement = placement;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStand armorStand) {
        if (armorStand == null) this.armorStand.remove();

        this.armorStand = armorStand;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (PlayerLG.this.armorStand == null) {
                    cancel();
                    return;
                }
                //noinspection ConstantConditions
                armorStand.teleport(PlayerLG.this.getPlayer().getEyeLocation());
            }
        }.runTaskTimer(LG.getInstance(), 1L, 1L);
    }


    public static PlayerLG createPlayerLG(HumanEntity human) {
        if (playerHashMap.containsKey(human))
            return playerHashMap.get(human);

        PlayerLG plg = new PlayerLG(human);
        playerHashMap.put(human, plg);
        return plg;
    }

    public static void removePlayerLG(HumanEntity human) {
        playerHashMap.remove(human);
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


    public interface CallbackChoice {
        void callback(PlayerLG choosen);
    }
}
