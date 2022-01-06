package fr.neyuux.lgthierce.listeners;

import fr.neyuux.lgthierce.*;
import fr.neyuux.lgthierce.role.Roles;
import fr.neyuux.lgthierce.task.LGAutoStart;
import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.Map.Entry;

public class LGThierceListener implements Listener {
	
	private final LG main;
	
	public LGThierceListener(LG main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		main.updateGrades();
		player.getInventory().clear();
		main.clearArmor(player);
		player.updateInventory();
		
		for(Player p : Bukkit.getOnlinePlayers())
			p.showPlayer(player);
		
		PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false);
		player.addPotionEffect(saturation);
		
		player.setGameMode(GameMode.ADVENTURE);
		
		
		if (!main.isState(Gstate.PREPARING)) {
			player.setGameMode(GameMode.SPECTATOR);
			player.setPlayerListName("§8[§7Spectateur§8] §7" + player.getName());
			player.setDisplayName(player.getPlayerListName());
				
			player.resetMaxHealth();
			player.setHealth(player.getMaxHealth());
			
			player.sendMessage(main.getPrefix() + main.SendArrow + "§9Le jeu a déjà démarré !");
			player.sendMessage(main.getPrefix() + main.SendArrow + "§9Votre mode de jeu a été établi en §7spectateur§9.");
			main.spectators.add(player);
		} else {
			player.teleport( new Location(Bukkit.getWorld("LG"), 494, 12.2, 307, 0f, 0f));
			
			player.getInventory().setItem(1, main.getItem(Material.GHAST_TEAR, "§7§lDevenir Spectateur", Arrays.asList("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit")));
			player.getInventory().setItem(5, main.getItem(Material.EYE_OF_ENDER, "§a§lJouer", Arrays.asList("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit")));
			 for (Entry<String, List<UUID>> en : main.getGrades().entrySet())
					if (en.getValue().contains(player.getUniqueId()))
						player.getInventory().setItem(6, main.config.getComparator());
			player.updateInventory();
				
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AGJoueur").addEntry(player.getName());
			player.setDisplayName(player.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + player.getScoreboard().getEntryTeam(player.getName()).getSuffix());
			player.setPlayerListName(player.getDisplayName());
			main.setPlayerGrade(player);
			main.playerlg.put(player.getName(), new PlayerLG(main, player));
			
			if (main.isState(Gstate.PREPARING) || main.isState(Gstate.STARTING))
				for (Player p : Bukkit.getOnlinePlayers())
					main.updateScoreboard(p);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		if (main.players.contains(ev.getPlayer()) && main.isState(Gstate.PLAYING)) new DeathManager(main).eliminate(ev.getPlayer(), true);
		if (main.isState(Gstate.PREPARING) || main.isState(Gstate.STARTING)) {
			main.playerlg.remove(ev.getPlayer().getName());
			main.players.remove(ev.getPlayer());
			for (Player p : Bukkit.getOnlinePlayers()) {
				int connected = Bukkit.getOnlinePlayers().size() - 1;
				SimpleScoreboard ss = new SimpleScoreboard(main.getPrefix(), p);
				ss.add(p.getDisplayName(), 15);
				ss.add("§0", 14);
				ss.add("§6Nombre de §lRôles §f: §e" + main.AddedRoles.size(), 13);
				ss.add("§aNombre de §lJoueurs §f: §e" + main.players.size(), 12);
				ss.add("§7Joueurs en ligne §f: §e" + connected, 11);
				ss.add("§2§lType §2de jeu §f: §c" + main.getType().toString(), 10);
				ss.add("§7", 9);
				ss.add("§8------------", 8);
				ss.add("§e§oMap by §c§l§oNeyuux_", 7);
				p.setScoreboard(ss.getScoreboard());
			}
		}	
	}
	
	
	@EventHandler
	public void onResetGameInv(InventoryOpenEvent ev) {
		if (ev.getInventory().getName().equals("resetlamap")) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§b" + ev.getInventory().getItem(0).getItemMeta().getDisplayName() + " §ea reset la map !");
			main.rel();
		}
	}
	
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			player.updateInventory();
		}
	}
	
	
	@EventHandler()
	public void onDamage(EntityDamageEvent ev) {
		if (ev.getEntityType().equals(EntityType.PLAYER)) {
			if (!main.isCycle(Gcycle.NUIT) || ev.getDamage() != 0.1) ev.setCancelled(true);
			else ev.setDamage(0);
		} else if (ev.getEntityType().equals(EntityType.ITEM_FRAME)) ev.setCancelled(true);
		else if (ev.getEntityType().equals(EntityType.ARMOR_STAND))
			if (ev.getCause().equals(DamageCause.ENTITY_ATTACK)) ev.setCancelled(true);

	}
	
	
	@EventHandler
	public void onBedLeave(PlayerBedLeaveEvent ev) {
		Player player = ev.getPlayer();
		if (main.sleepingPlayers.contains(player)) {
			Block b = ev.getBed();
			
			player.teleport(new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()));
			((CraftPlayer) player).getHandle().a(new BlockPosition(b.getX(), b.getY(), b.getZ()));
			player.setSleepingIgnored(false);
		}
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent ev) {
		if (ev.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) ev.setCancelled(true);
	}
	
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent ev) {
		Player player = ev.getPlayer();
		String cmd = ev.getMessage();
		
		if (cmd.toLowerCase().startsWith("/me") || cmd.toLowerCase().startsWith("/say") || cmd.toLowerCase().startsWith("/minecraft:")) {
			ev.setCancelled(true);
			player.sendMessage("§4Vous n'êtes pas autorisé à me péter les couilles.");
			return;
		}

		final String string = cmd.toLowerCase().substring(1).split(" ", 2)[0];
		if (Bukkit.getPluginCommand("tell").getAliases().contains(string) || cmd.toLowerCase().startsWith("/tell") || Bukkit.getPluginCommand("respond").getAliases().contains(string) || cmd.toLowerCase().startsWith("/respond")) {
			ev.setCancelled(true);
			player.sendMessage("§4§lNey§6G§ei§2n§4§l_" + main.SendArrow + " §4Cette commande est désactivée en " + main.getPrefix() + "§4.");
		}
	}
	
	@EventHandler
	public void onMessage(AsyncPlayerChatEvent ev) {
		Player player = ev.getPlayer();
		String msg = ev.getMessage();
		
		if (main.spectators.contains(player)) {
			ev.setCancelled(true);
			
			for (Player p : main.spectators) {
				p.sendMessage("§8[§7Spectateur§8] §7" + player.getName() + main.SendArrow + "§f" + msg);
			}
		}
		
		if (main.isCycle(Gcycle.NONE)) {
			ev.setFormat(player.getDisplayName() + main.SendArrow + msg);
		}
	}
	
	
	
	
	
	@EventHandler
	public void onInteract(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		Action action = ev.getAction();
		Block cblock = ev.getClickedBlock();
		ItemStack current = player.getItemInHand();
		
		if (current == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_BLOCK))
			if (cblock == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) {
			
			if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
				if ((cblock.getType().equals(Material.CHEST) ||
					cblock.getType().equals(Material.HOPPER) ||
					cblock.getType().equals(Material.ENDER_CHEST) ||
					cblock.getType().equals(Material.ANVIL) || 
					cblock.getType().equals(Material.ENCHANTMENT_TABLE) ||
					cblock.getType().equals(Material.FURNACE) ||
					cblock.getType().equals(Material.DISPENSER) ||
					cblock.getType().equals(Material.WORKBENCH)) && !player.getGameMode().equals(GameMode.CREATIVE)) ev.setCancelled(true);
			}
			
			if (current.hasItemMeta()) {
			if (current.getItemMeta().hasDisplayName()) {
			if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lJouer") && current.getType().equals(Material.EYE_OF_ENDER)) {
				String s = "";

				switch (main.getType()) {
				case LIBRE:
					try {
						player.teleport(new Location(Bukkit.getWorld("LG"), main.LibreSpawnX, main.LibreSpawnY, main.LibreSpawnZ));
						} catch (NullPointerException e) {
							e.printStackTrace();
							Bukkit.broadcastMessage("§e" + Bukkit.getWorld("LG").toString());
							Bukkit.broadcastMessage("§5 " + main == null ? "NULL" : "NOT NULL");
							Bukkit.broadcastMessage("§c "+ player.getWorld().getUID().toString());
						}
					main.players.add(player);
					if (main.players.size() != 1) s = "s";

					player.getInventory().remove(current);
					LG.sendTitle(player, "§5§k §4§k §c§k §a§lVous jouerez cette partie ! §6§k §e§k §f§k ", "§6Il y a désormais §e" + main.players.size() + "§6 joueur"+s+".", 20, 60, 20);
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 9, 1);
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez été ajouté à la liste des joueurs.");
					StringBuilder splayers = new StringBuilder();
					for (Player p : main.players) {
						if (splayers.toString().equalsIgnoreCase("")) {
							splayers = new StringBuilder(p.getDisplayName());
						} else splayers.append(", ").append(p.getDisplayName());
					}
					player.sendMessage(main.getPrefix() + main.SendArrow + "§fListe des joueurs : " + splayers);
					break;



				case NONE:
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous devez attendre qu'un OP choisisse le type de jeu (Libre ou Réunion) !");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 7, 1);
					break;



				case REUNION:
					try {
						player.teleport(new Location(Bukkit.getWorld("LG"), main.ReunionSpawnX, main.ReunionSpawnY, main.ReunionSpawnZ));
					} catch (NullPointerException e) {
						e.printStackTrace();
						Bukkit.broadcastMessage("§e" + Bukkit.getWorld("LG").toString());
						Bukkit.broadcastMessage("§5 " + main == null ? "NULL" : "NOT NULL");
						Bukkit.broadcastMessage(player.getWorld().getUID().toString());
					}
					main.players.add(player);
					if (main.players.size() != 1) s = "s";

					player.getInventory().remove(current);
					LG.sendTitle(player, "§5§k §4§k §c§k §a§lVous jouerez cette partie ! §6§k §e§k §f§k ", "§6Il y a désormais §e" + main.players.size() + "§6 joueur"+s+".", 20, 60, 20);
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 9, 1);
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez été ajouté à la liste des joueurs.");
					StringBuilder splayers2 = new StringBuilder();
					for (Player p : main.players) {
						if (splayers2.toString().equalsIgnoreCase("")) {
							splayers2 = Optional.ofNullable(p.getDisplayName()).map(StringBuilder::new).orElse(null);
						} else splayers2.append(", ").append(p.getDisplayName());
					}
					player.sendMessage(main.getPrefix() + main.SendArrow + "§fListe des joueurs : " + splayers2);

					break;
				default:
					break;

				}

				if (main.players.size() >= 3) {
					if (main.isState(Gstate.PREPARING)) {
						if ((main.players.size() + main.spectators.size()) == Bukkit.getOnlinePlayers().size()) {
							List<Roles> roles = new ArrayList<>();
							for (java.util.Map.Entry<Roles, Integer> en : main.AddedRoles.entrySet()) {
								if (en.getValue() > 1) {
									int fois = en.getValue();
									while (fois != 0) {
										roles.add(en.getKey());
										fois--;
									}
								} else {
									roles.add(en.getKey());
								}
							}

							if (main.AddedRoles.containsKey(Roles.VOLEUR)) {
								if (roles.size() == (2 * main.AddedRoles.get(Roles.VOLEUR)) + main.players.size()) {
									main.setState(Gstate.STARTING);
									LGAutoStart autostart = new LGAutoStart(main);
									autostart.runTaskTimer(main, 0, 20);
									main.StartRunnable = autostart;
								} else {
									Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§4[§cErreur§4] §cIl n'y a pas assez de rôles pour le nombre de joueurs !");
									System.out.println("voleur " + main.players.size() + " " + main.AddedRoles.size());
								}
							} else {
								if (roles.size() == main.players.size()) {
									main.setState(Gstate.STARTING);
									LGAutoStart autostart = new LGAutoStart(main);
									autostart.runTaskTimer(main, 0, 20);
									main.StartRunnable = autostart;
								} else {
									Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§4[§cErreur§4] §cIl n'y a pas assez de rôles pour le nombre de joueurs !");
									System.out.println("" + main.players.size() + " " + main.AddedRoles.size());
								}
							}
						}
					}
				}

				for (Player p : Bukkit.getOnlinePlayers())
					main.updateScoreboard(p);

			} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§7§lDevenir Spectateur") && current.getType().equals(Material.GHAST_TEAR)) {

				main.players.remove(player);
				main.spectators.add(player);

				player.getInventory().clear();
				for (Entry<String, List<UUID>> en : main.getGrades().entrySet())
					if (en.getValue().contains(player.getUniqueId()))
						player.getInventory().setItem(6, main.config.getComparator());
				player.setGameMode(GameMode.SPECTATOR);
				player.setDisplayName("§8[§7Spectateur§8]" + player.getName());
				player.setPlayerListName(player.getDisplayName());
				Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(player.getName()).removeEntry(player.getName());
				player.sendMessage(main.getPrefix() + main.SendArrow + "§9Votre mode de jeu a été établi en §7spectateur§9.");
				player.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/spec off§c.");

				for (Player p : Bukkit.getOnlinePlayers())
					main.updateScoreboard(p);

				}
			}
			}
		}
	}
	

}
