package fr.neyuux.lg;

import fr.neyuux.lg.config.ComedianPowers;
import fr.neyuux.lg.config.MayorSuccession;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.classes.*;
import fr.neyuux.lg.utils.CacheLG;
import fr.neyuux.lg.utils.SimpleScoreboard;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerLG {

    private static final HashMap<HumanEntity, PlayerLG> playerHashMap = new HashMap<>();
    
    private final GameLG game;

    public PlayerLG(HumanEntity human) {
        this.human = human;
        this.game = LG.getInstance().getGame();
        this.updateGamePlayerScoreboard();
    }

    private final HumanEntity human;

    @Setter
    @Getter
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

    public boolean canSee(PlayerLG playerLG) {
        return this.getPlayer().canSee(playerLG.getPlayer());
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

        if (receiver.isSpectator() && receiver.getPlayer() != null && receiver.getPlayer().getGameMode() == GameMode.SPECTATOR && this.game.getRolesAtStart().stream().noneMatch(role1 -> role1 instanceof Necromancien)) {

            name = this.getRole().getDisplayName() + " ";
            lastcolor = this.getCamp().getColor();

            name += lastcolor + this.human.getName();

            if (this.getCache().has("couple"))
                name += "§d \u2764";

            return name;
        }

        if (this.isSpectator()) {
            return "§8[§7Spectateur§8] §7" + this.getName();
        }

        if (this.game.getGameState() != GameState.PLAYING) {
            if (this.isOP()) return "§5§lOP §c" + this.getName();
            else return "§e" + this.getName();
        }

        if (this.getRole() instanceof President) {
            name += "§e§lPrésident ";
            lastcolor = "§6";
        }

        if (this.isLG() && receiver.isLG()) {
            name += "§c§lLG ";
            lastcolor = "§c";
        }

        if (receiver.getRole() instanceof Soeur) {
            Soeur soeur = (Soeur) receiver.getRole();

            if (soeur.getSister().equals(this) || receiver == this) {
                name += "§d§lSoeur ";
                lastcolor = "§d";
            }
        }

        if (receiver.getRole() instanceof Frere) {
            Frere brother = (Frere)receiver.getRole();

            if (brother.getBrothers().contains(this) || receiver == this) {
                name += "§3§lFrère ";
                lastcolor = "§d";
            }
        }

        if (receiver.getRole() instanceof Macon && this.getRole() instanceof Macon) {
            name += "§6§lMaçon ";
            lastcolor = "§d";
        }

        if (this.getRole() instanceof VillageoisVillageois) {
            name += "§a§lV§e-§a§lV ";
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

        if (this.getCache().has("couple")) {
            if (receiver.getRole() instanceof Cupidon || (this.getCache().get("couple") != null && receiver.getCache().has("couple") && receiver.getCache().get("couple") != null && this.getCache().get("couple").equals(receiver)))
                name += "§d \u2764";
        }
        return name;
    }

    public List<PlayerLG> get2NearbyPlayers(boolean addself) {
        List<PlayerLG> nearbys = new ArrayList<>();

        if (game.getGameType().equals(GameType.FREE)) {
            List<String> list = game.getAliveAlphabecticlySorted();

            int j = list.indexOf(this.getName());

            if (j - 1 > -1)
                nearbys.add(0, PlayerLG.createPlayerLG(Bukkit.getPlayer(list.get(j - 1))));
            else
                nearbys.add(0, PlayerLG.createPlayerLG(Bukkit.getPlayer(list.get(list.size() - 1))));

            if (addself) nearbys.add(this);

            PlayerLG playerLG;
            if (j + 1 < list.size())
                playerLG = PlayerLG.createPlayerLG(Bukkit.getPlayer(list.get(j + 1)));
            else
                playerLG = PlayerLG.createPlayerLG(Bukkit.getPlayer(list.get(0)));

            if (!nearbys.contains(playerLG))
                nearbys.add(playerLG);

        } else if (game.getGameType().equals(GameType.MEETING)) {
            PlayerLG closest1 = this.getClosestPlayer(game.getAliveExcept(this));

            nearbys.add(0, closest1);

            if (addself) nearbys.add(this);

            PlayerLG closest2 = this.getClosestPlayer(game.getAliveExcept(nearbys.get(0), this));
            if (closest2 != null && !nearbys.contains(closest2))
                nearbys.add(closest2);
        }

        return nearbys;
    }

    public PlayerLG getClosestPlayer(List<PlayerLG> choosable) {
        PlayerLG result = null;
        double lastDistance = Double.MAX_VALUE;
        for(PlayerLG plg : choosable) {
            Player p = plg.getPlayer();

            double distance = this.getPlayer().getLocation().distanceSquared(p.getLocation());
            if(distance < lastDistance) {
                lastDistance = distance;
                result = plg;
            }
        }

        return result;
    }

    public PlayerLG getPlayerOnCursor(List<PlayerLG> choosable) {
        Location loc = this.getPlayer().getLocation();

        if (loc.getPitch() > 60F && choosable.contains(this))
            return this;

        for (int i = 0; i < 50; i++) {

            loc.add(loc.getDirection());

            for (PlayerLG playerLG : choosable) {

                if (playerLG != this && !playerLG.isDead() &&
                        this.canSee(playerLG) &&
                        LG.distanceSquaredXZ(loc, playerLG.getLocation()) < 0.35D && Math.abs(loc.getY() - playerLG.getLocation().getY()) < 2.0D)
                    return playerLG;
            }
        }
        return null;
    }

    public void updateGamePlayerScoreboard() {
        Scoreboard scoreboard = this.getPlayer().getScoreboard();

        for (Team team : scoreboard.getTeams())
            for (Player player : Bukkit.getOnlinePlayers())
                if (team.getName().equals(player.getName())) {
                    team.unregister();
                    break;
                }

        for (PlayerLG targetLG : this.game.getAlive()) {
            Team team = scoreboard.registerNewTeam(targetLG.getName());
            String[] prefixsuffix = targetLG.getNameWithAttributes(this).split(targetLG.getName());

            String prefix = prefixsuffix[0];
            team.setPrefix(prefix.substring(0, Math.min(16, prefix.length())));
            if (prefixsuffix.length > 1) {
                String suffix = prefixsuffix[1];
                team.setSuffix(suffix.substring(0, Math.min(16, suffix.length())));
            }
            team.addEntry(targetLG.getName());
            this.getPlayer().setScoreboard(this.getPlayer().getScoreboard());
        }
    }

    public void sendLobbySideScoreboard() {
        SimpleScoreboard ss = new SimpleScoreboard(game.getPrefix().substring(0, game.getPrefix().length() - 9), this.getPlayer());

        ss.add(this.getPlayer().getDisplayName(), 15);
        ss.blankLine(14);
        ss.add("§6Nombre de §lRôles §f: §e" + game.getConfig().getAddedRoles().size(), 13);
        ss.add("§aNombre de §lJoueurs §f: §e" + game.getPlayersInGame().size(), 12);
        ss.add("§7Joueurs en ligne §f: §e" + Bukkit.getOnlinePlayers().size(), 11);
        ss.add("§2§lType §2de jeu §f: §c" + game.getGameType().getName(), 10);
        ss.blankLine(9);
        ss.add("§8------------", 8);
        ss.add("§e§oMap by §c§l§oNeyuux_", 7);
        this.getPlayer().setScoreboard(ss.getScoreboard());
    }

    public void sendListRolesSideScoreboard() {
        SimpleScoreboard scoreboard = new SimpleScoreboard("§6§lRôles", this.getPlayer());
        HashMap<String, Integer> roles = new HashMap<>();
        int i = 15;

        for (Role role : game.getAliveRoles())
            if (roles.containsKey(role.getDisplayName()))
                roles.put(role.getDisplayName(), roles.get(role.getDisplayName()) + 1);
            else
                roles.put(role.getDisplayName(), 1);

        for (Map.Entry<String, Integer> entry : roles.entrySet()) {
            scoreboard.add(entry.getKey() + " §f: §e" + entry.getValue(), i);
            i--;
        }

        this.getPlayer().setScoreboard(scoreboard.getScoreboard());

    }

    public void sendComedianPowersSideScoreboard() {
        SimpleScoreboard scoreboard = new SimpleScoreboard("§5§lPouvoirs du Comédien", this.getPlayer());
        List<ComedianPowers> powers = ((Comedien)game.getPlayersByRole(Comedien.class).get(0).getRole()).getRemaningPowers();
        int i = 15;

        for (ComedianPowers power : powers) {
            try {
                scoreboard.add(LG.getInstance().getRoles().get(power.getName()).newInstance().getDisplayName(), i);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            i--;
        }

        this.getPlayer().setScoreboard(scoreboard.getScoreboard());
    }

    public void setSleep() {
        Player player = this.getPlayer();
        Location loc = this.placement;

        player.teleport(placement);

        for (PlayerLG playerLG : this.game.getAlive())
            player.hidePlayer(playerLG.getPlayer());

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));

        if (this.game.getGameType().equals(GameType.FREE)) {
            ((CraftPlayer) player).getHandle().a(new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
            player.setSleepingIgnored(false);
        }

        this.isSleeping = true;
    }

    public void setWake() {
        Player player = this.getPlayer();

        this.isSleeping = false;

        if (game.getGameType().equals(GameType.FREE)) player.teleport(this.getLocation().add(0, 0.5, 0));
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        for (PlayerLG playerLG : this.game.getAlive())
            player.showPlayer(playerLG.getPlayer());
    }

    public void eliminate() {
        if (this.isDead) return;

        PlayerEliminationEvent event = new PlayerEliminationEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        this.isDead = true;

        this.setWake();
        game.getSpectators().add(this);
        this.getPlayer().setGameMode(GameMode.SPECTATOR);

        Bukkit.getWorld("LG").strikeLightningEffect(this.getLocation());

        game.getAliveRoles().remove(this.getRole());
        game.sendListRolesSideScoreboardToAllPlayers();
        game.updatePlayersScoreboard();

        this.showAllPlayers();

        Bukkit.broadcastMessage(LG.getPrefix() + "§6Le village a perdu un de ses membres : §e" + this.getName() + "§6 est §nmort§6. Il était " + this.getRole().getDisplayName() + "§6.");
        this.sendMessage(LG.getPrefix() + "§9Votre mode de jeu a été établi en §7spectateur§9.");

        for (Map.Entry<PlayerLG, String> messages : event.getMessagesToSend().entrySet())
            messages.getKey().sendMessage(messages.getValue());

        if (this.isMayor) {
            this.removeMayor();

            switch ((MayorSuccession)game.getConfig().getMayorSuccession().getValue()) {
                case RANDOM:
                    game.getAlive().get(LG.RANDOM.nextInt(game.getAlive().size())).setMayor();
                break;

                case CHOOSE:
                case REVOTE:
                    game.setMayor(this);
                break;

                default:
                break;
            }
        }

        game.checkWin();
    }

    public void setChoosing(CallbackChoice callback) {
        this.callbackChoice = callback;
    }

    public void stopChoosing() {
        this.callbackChoice = null;
    }

    public void callbackChoice(PlayerLG playerLG) {
        if (playerLG == null) return;
        this.callbackChoice.callback(playerLG);
    }

    public boolean isChoosing(){
        return this.callbackChoice != null;
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

    public void joinRole(Role role) {
        this.setRole(role);
        role.onPlayerJoin(this);
    }

    public boolean isLG () {
        return this.game.getLGs(false).contains(this);
    }

    public void joinCamp(Camps camp) {
        if (camp.equals(Camps.VAMPIRE) || camp.equals(Camps.LOUP_GAROU))
            game.getAlive().stream().filter(playerLG -> playerLG.getCamp().equals(camp)).collect(Collectors.toSet()).forEach(playerLG -> playerLG.sendMessage(game.getPrefix() + this.getNameWithAttributes(playerLG) + "§c a rejoint votre camp !"));
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

    public void setMayor() {
        if (game.getMayor() != null)
            game.getMayor().removeMayor();
        this.isMayor = true;
        game.setMayor(this);
        Bukkit.broadcastMessage(LG.getPrefix() + "§3§l" + this.getName() + " §ba été choisi comme nouveau maire !");
        this.getPlayer().getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        game.updatePlayersScoreboard();
    }

    public void removeMayor() {
        this.isMayor = false;
        if (game.getMayor().equals(this)) game.setMayor(null);
        this.getPlayer().getInventory().setChestplate(null);
    }

    public boolean canUsePowers() {
        return canUsePowers;
    }

    public void setCanUsePowers(boolean canUsePowers) {
        if (this.cache.has("noctambuleCantUsePower") && !canUsePowers)
            this.cache.remove("noctambuleCantUsePower");
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
        if (armorStand == null && this.armorStand != null && !this.armorStand.isDead()) this.armorStand.remove();

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
        }.runTaskTimer(LG.getInstance(), 2L, 1L);
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
