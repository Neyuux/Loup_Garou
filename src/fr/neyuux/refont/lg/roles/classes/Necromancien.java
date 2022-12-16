package fr.neyuux.refont.lg.roles.classes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.NightEndEvent;
import fr.neyuux.refont.lg.event.RoleChoiceEvent;
import fr.neyuux.refont.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Necromancien extends Role {

    private final List<EntityPlayer> fakePlayers = new ArrayList<>();
    private final List<PlayerLG> toRevive = new ArrayList<>();

    @Override
    public String getDisplayName() {
        return "§9§lNécromancien";
    }

    @Override
    public String getConfigName() {
        return "Nécromancien";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez §9récussiter§f un joueur. S'il avait un pouvoir, il le perd et devient §e§lSimple §a§lVillageois§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §9" + this.getTimeout() +  " secondes§f pour réssuciter quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {

            for (PlayerLG choosableLG : game.getPlayersInGame()) {
                if (choosableLG.getPlayer() == null || !choosableLG.getPlayer().isOnline())
                    return;

                Player choosable = choosableLG.getPlayer();

                playerLG.getPlayer().hidePlayer(choosable);

                if (choosableLG.isDead() && choosableLG.isSpectator() && !choosableLG.getCamp().equals(Camps.VILLAGE)) {
                    Location loc = choosableLG.getLocation();
                    WorldServer cworld = ((CraftWorld)loc.getWorld()).getHandle();
                    GameProfile gameProfile = new GameProfile(choosable.getUniqueId(), choosable.getName());
                    String[] properties = getTextures(choosable.getName());

                    //noinspection ConstantConditions
                    gameProfile.getProperties().put("textures", new Property("textures", properties[0], properties[1]));

                    EntityPlayer fakePlayer = new EntityPlayer(MinecraftServer.getServer(), cworld, gameProfile, new PlayerInteractManager(cworld));

                    loc = loc.setDirection(playerLG.getLocation().subtract(loc).toVector());
                    fakePlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                    fakePlayer.getDataWatcher().watch(10,  (byte) 0xFF);

                    float yaw = loc.getYaw();
                    float pitch = loc.getPitch();
                    PacketPlayOutPlayerInfo packetPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, fakePlayer);
                    PacketPlayOutNamedEntitySpawn packetSpawn = new PacketPlayOutNamedEntitySpawn(fakePlayer);
                    PlayerConnection co = ((CraftPlayer) choosable).getHandle().playerConnection;

                    co.sendPacket(packetPlayerInfo);
                    co.sendPacket(packetSpawn);
                    co.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(fakePlayer.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
                    co.sendPacket(new PacketPlayOutEntityHeadRotation(fakePlayer, (byte) ((yaw%360.)*256/360)));

                    this.fakePlayers.add(fakePlayer);
                }
            }

            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    if (choosen.getPlayer() != null && choosen.getPlayer().isOnline()) {
                        this.setReviving(choosen, playerLG);

                        this.removeFakePlayers(playerLG.getPlayer());

                        super.onPlayerTurnFinish(playerLG);
                        callback.run();
                    } else {
                        playerLG.sendMessage(LG.getPrefix() + "§cVous ne pouvez pas réssuciter ce joueur, il est déconnecté !");
                        GameLG.playNegativeSound(playerLG.getPlayer());
                    }
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {

            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getPlayersInGame().stream().filter(inGameLG -> inGameLG.getPlayer() != null && inGameLG.getPlayer().isOnline() && inGameLG.isDead() && inGameLG.getCamp().equals(Camps.VILLAGE) && inGameLG.isSpectator()).collect(Collectors.toList()), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    try {
                        return new String[] {"§9Voulez-vous §fréssuciter " + paramPlayerLG.getNameWithAttributes(playerLG) + "§9 ?", "§9Il sera de retour dans la partie mais", "§9son rôle sera remplacé par " + LG.getInstance().getRoles().get("SimpleVillageois").newInstance().getDisplayName() + "§9.", "", "§7>>Clique pour choisir"};
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return new String[] {"§c§lERREUR"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    setReviving(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void setReviving(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        this.toRevive.add(choosen);

        choosen.sendMessage(LG.getPrefix() + "§9Le " + this.getDisplayName() + " §9vous a réssucité ! §cNe vous déconnectez pas §9avant le début de la journée pour revenir dans la partie.");
        playerLG.sendMessage(LG.getPrefix() + "§9Tu as réssucité §1§l" + choosen.getNameWithAttributes(playerLG) + "§9.");
        GameLG.playPositiveSound(playerLG.getPlayer());
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
        this.removeFakePlayers(playerLG.getPlayer());
    }


    @EventHandler
    public void onCloseNecromancienInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();

        if (inv.getName().equals(this.getDisplayName()) && (boolean)PlayerLG.createPlayerLG(player).getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(inv);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onDayStart(NightEndEvent ev) {
        for (PlayerLG playerLG : toRevive) {
            this.revive(playerLG);
        }
    }


    private static String[] getTextures(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());

            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();
            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());

            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            return new String[]{texture,signature};
        }catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void removeFakePlayers(Player player) {
        for (EntityPlayer fakePlayer : this.fakePlayers) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, fakePlayers));
            connection.sendPacket(new PacketPlayOutEntityDestroy(fakePlayer.getId()));
        }
        fakePlayers.clear();
    }

    private void revive(PlayerLG playerLG) {
        try {
            LG main = LG.getInstance();
            GameLG game = main.getGame();
            SimpleVillageois newRole = (SimpleVillageois) main.getRoles().get("SimpleVillageois").newInstance();
            Player player = playerLG.getPlayer();

            game.getSpectators().remove(playerLG);

            playerLG.setDead(false);
            playerLG.setRole(newRole);
            playerLG.setCamp(Camps.VILLAGE);
            playerLG.getCache().clear();

            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getDisplayName());
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(playerLG.getLocation());

            player.getWorld().strikeLightningEffect(player.getLocation());
            //TODO game.updateAllScoreboards();
            Bukkit.broadcastMessage(LG.getPrefix() + "§9Le " + this.getDisplayName() + " §9a réssucité §1§l" + player.getName() + "§9 !");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cImpossible de réssuciter §e" + playerLG.getName() + "§c. Veuillez réessayer plus tard ou appeler Neyuux_.");
            e.printStackTrace();
        }
    }

}
