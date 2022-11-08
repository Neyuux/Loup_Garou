package fr.neyuux.refont.old.lg.task;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.neyuux.refont.old.lg.*;
import fr.neyuux.refont.old.lg.role.RCamp;
import fr.neyuux.refont.old.lg.role.Roles;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

public class NightRunnable extends BukkitRunnable {

	private final LG main;
	
	private int currentTimer = Integer.MAX_VALUE;
	private int currentPlayerInPlayerList = 0;
	public static Map<Player, List<EntityPlayer>> fakeplayers = new HashMap<>();
	
	public NightRunnable(LG main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		
		if (!main.isCycle(Gcycle.NUIT)) {
			cancel();
			return;
		}
		
		
		if (main.isDisplayState(DisplayState.TOMBEE_DE_LA_NUIT)) {
			main.setDisplayState(DisplayState.DEMARRAGE_DE_LA_NUIT);
			if (main.cycleJourNuit) Bukkit.getWorld("LG").setTime(18000);
		}
		

		if (main.isDisplayState(DisplayState.DEMARRAGE_DE_LA_NUIT) && main.CalledRoles.isEmpty()) {
			main.nights++;
			Bukkit.broadcastMessage("      §9§lNUIT " + main.nights);
			for (Player player : Bukkit.getOnlinePlayers())
				player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 4, 0.1f);
			main.fillCalledRoles();
			currentPlayerInPlayerList = 0;
			for (Player player : main.players) setSleep(player);
			for (Roles r : main.CalledRoles) {
				for (Player player : main.getPlayersByRole(r)) {
					main.playerlg.get(player.getName()).setHasUsedPower(false);
					if (!player.hasPotionEffect(PotionEffectType.BLINDNESS)) setSleep(player);
				}
			}
			
			if (main.AliveRoles.containsKey(Roles.CHAMAN)) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9Le " + Roles.CHAMAN.getDisplayName() + "§9 peut désormais écouter les morts.");
				main.sendRoleMessage(main.getPrefix() + main.SendArrow + "§aVous pouvez discuter avec les morts : lorsqu'un spectateur enverra un message, vous le recevrez.", Roles.CHAMAN);
				if (main.paroleChaman) main.sendRoleMessage(main.getPrefix() + main.SendArrow + "§aVous pouvez également leur envoyer des messages.", Roles.CHAMAN);
			}
		}
		
		if (main.CalledRoles.isEmpty()) {
			main.days++;
			main.setCycle(Gcycle.JOUR);
			main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_NUIT);
			return;
		}
		
		
		for (Player player : main.sleepingPlayers)
			if (!player.isSleeping()) setSleep(player);
			
			
			if (main.nights == 1) {
				
				if (main.CalledRoles.get(0).equals(Roles.VOLEUR)) {
					if (!main.isDisplayState(DisplayState.NUIT_VOLEUR)) {
						main.setDisplayState(DisplayState.NUIT_VOLEUR);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
						
					} else {
						Player player = null;
						if (main.getPlayersByRole(Roles.VOLEUR).size() > currentPlayerInPlayerList)
							player = main.getPlayersByRole(Roles.VOLEUR).get(currentPlayerInPlayerList);
						
						if (player == null) {
							for (Player p : main.players) {
								if (p.getInventory().contains(Material.BARRIER)) player = p;
							}
						}
						
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.VOLEUR.getDisplayName());
						
						if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.VOLEUR.getDisplayName()) && !main.playerlg.get(player.getName()).hasUsedPower()) {
							Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.VOLEUR.getDisplayName());
							Roles r1 = main.RolesVoleur.get(0);
							Roles r2 = main.RolesVoleur.get(1);
							
							/*ItemStack it1 = new ItemStack(Material.MAP, 1, main.getRoleMap(r1).getDurability());
							ItemMeta itm1 = it1.getItemMeta();
							itm1.setDisplayName(r1.getDisplayName());
							itm1.setLore(Arrays.asList("§7Vous transforme en " + r1.getDisplayName() + "§7.", "§7Cela veut dire que vous gagnerez", "§7avec le camp de ce rôle.", "§7Donc, il n'y aura pas de " + r2.getDisplayName(), "§7dans la partie.", "", "§b>>Clique pour devenir"));
							itm1.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							it1.setItemMeta(itm1);*/
							
							/*ItemStack it2 = new ItemStack(Material.MAP, 1, main.getRoleMap(r2).getDurability());
							ItemMeta itm2 = it2.getItemMeta();
							itm2.setDisplayName(r2.getDisplayName());
							itm2.setLore(Arrays.asList("§7Vous transforme en " + r2.getDisplayName() + "§7.", "§7Cela veut dire que vous gagnerez", "§7avec le camp de ce rôle.", "§7Donc, il n'y aura pas de " + r1.getDisplayName(), "§7dans la partie.", "", "§b>>Clique pour devenir"));
							itm2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
							it2.setItemMeta(itm2);*/
							
							
							inv.setItem(2, main.getItem(Material.BARRIER, Roles.VOLEUR.getDisplayName(), Arrays.asList("§7Vous gardez votre rôle. Vous gagnerez donc", "§7avec le Village et les rôles", r1.getDisplayName() + " §7et " + r2.getDisplayName() + " §7ne seront pas dans la partie.", "", "§b>>Clique pour devenir")));
							//inv.setItem(0, it1);
							//inv.setItem(4, it2);
							
							setWake(player);
							player.openInventory(inv);
						}
						
						if (main.playerlg.get(player.getName()).hasUsedPower()) {
							currentTimer = 0;
						}
						
						if (currentTimer == 0) {
							if (!main.playerlg.get(player.getName()).hasUsedPower()) {
								player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir. §r\n§fVotre rôle est désormais " + Roles.VOLEUR.getDisplayName() + "§f." + "§fVous êtes comme un Simple Villageois. Cependant, les deux autres rôles que vous n'avez pas choisi ne seront pas dans la partie.");
								main.playerlg.get(player.getName()).setVoleur(true);
								main.playerlg.get(player.getName()).setCamp(RCamp.VILLAGE);
								main.playerlg.get(player.getName()).setRole(Roles.VOLEUR);
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							if (main.CalledRoles.contains(main.playerlg.get(player.getName()).getRole())) {
								main.playerlg.get(player.getName()).setHasUsedPower(false);
								System.out.println(1);
							} else System.out.println(2 + main.playerlg.get(player.getName()).getRole().getName());
							
							player.closeInventory();
							player.getInventory().remove(Material.BARRIER);
							setSleep(player);
							main.CalledRoles.remove(Roles.VOLEUR);							
							currentTimer = Integer.MAX_VALUE;
							if (main.CalledRoles.isEmpty()) return;
							if (main.CalledRoles.get(0).equals(Roles.VOLEUR)) {
								currentPlayerInPlayerList++;
							} else currentPlayerInPlayerList = 0;
						}
						
					}
				}
				
				
				else if (main.CalledRoles.get(0).equals(Roles.CUPIDON)) {
					if(currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_CUPIDON);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 30;
						
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.CUPIDON.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.CUPIDON)) {
							Player player = main.getPlayersByRole(Roles.CUPIDON).get(currentPlayerInPlayerList);
							
							if (!main.coupleRandom) {
								
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.CUPIDON.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.CUPIDON.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										for (Player p : main.players) {
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											itm.setOwner(p.getName());
											itm.setDisplayName("§d" + p.getName());
											itm.setLore(Arrays.asList("§7Mettre en couple §d" + p.getName() + "§7.", "", "§b>>Clique pour sélectionner"));
											it.setItemMeta(itm);
											
											inv.addItem(it);
										}
										setWake(player);
										player.openInventory(inv);
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 30) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 30 secondes pour choisir votre couple. Pour faire cela, il suffit de cliquer sur les joueurs voulus.");
										if (main.cupiEnCouple) player.sendMessage(main.getPrefix() + main.SendArrow + "§dVous serez forcément en couple, cela a été paramétré dans la configuration de la partie, vous devez cliquer sur le joueur qui sera votre amant pendant cette partie.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
								
								if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
								if (currentTimer == 0) {
									boolean isCoupleAlive = false;
									if (main.playerlg.get(player.getName()).hasUsedPower()) isCoupleAlive = true;
									
									if (!isCoupleAlive) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir votre Couple. Il n'y aura donc pas de sexe pendant la partie :(");
										main.playerlg.get(player.getName()).setHasUsedPower(true);
									}
									player.closeInventory();
									setSleep(player);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									main.CalledRoles.remove(Roles.CUPIDON);
								}
							}
							
							else {
								if (currentTimer == 28) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§dSélection du couple aléatoire...");
								} else if (currentTimer == 23) {
									Player p1 = main.players.get(new Random().nextInt(main.players.size()));
									if (main.cupiEnCouple) p1 = player;
									Player p2 = main.players.get(new Random().nextInt(main.players.size()));
									
									if (p1.getName().equals(p2.getName())) {
										while (p1.getName().equals(p2.getName())) {
											p2 = main.players.get(new Random().nextInt(main.players.size()));
										}
									}
									
									PlayerLG p1lg = main.playerlg.get(p1.getName());
									PlayerLG p2lg = main.playerlg.get(p2.getName());
									p1lg.addCouple(p2);
									p2lg.addCouple(p1);
									
									Team t = null;
									for (Team team : player.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t = team;
									Team t1 = null;
									for (Team team : p1.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t1 = team;
									Team t2 = null;
									for (Team team : p2.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t2 = team;
									
									t1.addEntry(p2.getName());
									t2.addEntry(p1.getName());
									t.addEntry(p1.getName());
									t.addEntry(p2.getName());
									
									
									p1.sendMessage(main.getPrefix() + main.SendArrow + "§dVous recevez soudainement une flèche, elle vous transperce. Regardant au loin, vous apercevez §5" + p2.getName() + " §d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " §dpour avoir fait ce choix. §r\n§dVous êtes amoureux de §5" + p2.getName() + " §d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
									p2.sendMessage(main.getPrefix() + main.SendArrow + "§dVous recevez soudainement une flèche, elle vous transperce. Regardant au loin, vous apercevez §5" + p1.getName() + " §d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " §dpour avoir fait ce choix. §r\n§dVous êtes amoureux de §5" + p1.getName() + " §d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
								
									player.closeInventory();
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									player.sendMessage(main.getPrefix() + main.SendArrow + "§dVos 2 flèches ont transpercé §5" + main.getPlayerNameByAttributes(p1, player) + " §det §5" + main.getPlayerNameByAttributes(p2, player) + "§d. Ils ne se quitteront plus désormais...");

								} else if (currentTimer == 20) {
									setSleep(player);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									main.CalledRoles.remove(Roles.CUPIDON);
								}
							}
								
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.CUPIDON)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.CUPIDON);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
						
					}
				}
				
				
				else if (main.CalledRoles.get(0).equals(Roles.ENFANT_SAUVAGE)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_ES);
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de l'" + Roles.ENFANT_SAUVAGE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.ENFANT_SAUVAGE)) {
							Player player = main.getPlayersByRole(Roles.ENFANT_SAUVAGE).get(currentPlayerInPlayerList);
							
							if (!main.maitreRandom) {
							
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.ENFANT_SAUVAGE.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.ENFANT_SAUVAGE.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										for (Player p : main.players) {
											if (!p.getName().equals(player.getName())) {
												ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
												SkullMeta itm = (SkullMeta) it.getItemMeta();
												itm.setOwner(p.getName());
												itm.setDisplayName("§6" + p.getName());
												itm.setLore(Arrays.asList("§7Sélectionner le joueur §6" + p.getName(), "§7en temps que maître.", "", "§b>>Clique pour sélectionner"));
												it.setItemMeta(itm);
												
												inv.addItem(it);
											}
										}
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 20) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§6Vous avez 20 secondes pour choisir votre modèle. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									}
								}
								
								if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
								if (currentTimer == 0) {
									boolean isMaitreAlive = false;
									if (main.playerlg.get(player.getName()).hasUsedPower()) isMaitreAlive = true;
									
									if (!isMaitreAlive) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous n'avez pas choisi de modèle. Il sera donc choisi aléatoirement.");
										int rdm = new Random().nextInt(main.players.size());
										Player p = main.players.get(rdm);
										if (p.equals(player))
											while (p.equals(player)) {
												rdm = new Random().nextInt(main.players.size());
												p = main.players.get(rdm);
											}
										main.playerlg.get(p.getName()).addmaitre(player);
										player.sendMessage(main.getPrefix() + main.SendArrow + "§fVotre modèle est §c" + p.getName());
										main.playerlg.get(player.getName()).setHasUsedPower(true);
									}
									player.closeInventory();
									setSleep(player);
									main.CalledRoles.remove(Roles.ENFANT_SAUVAGE);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									if (main.CalledRoles.get(0).equals(Roles.ENFANT_SAUVAGE)) {
										currentPlayerInPlayerList++;
									} else currentPlayerInPlayerList = 0;
								}
							} else {

								if (currentTimer == 18) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§6Sélection du modèle aléatoire...");
								} else if (currentTimer == 13) {
									Player p1 = main.players.get(new Random().nextInt(main.players.size()));
									
									if (p1.getName().equals(player.getName())) {
										while (p1.getName().equals(player.getName())) {
											p1 = main.players.get(new Random().nextInt(main.players.size()));
										}
									}
									
									PlayerLG p1lg = main.playerlg.get(p1.getName());
									p1lg.addmaitre(player);
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									
									player.closeInventory();
									player.sendMessage(main.getPrefix() + main.SendArrow + "§e" + main.getPlayerNameByAttributes(p1, player) + " §6a été choisi comme modèle.");
									
								} else if (currentTimer == 11) {
									setSleep(player);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									main.CalledRoles.remove(Roles.ENFANT_SAUVAGE);
								}
							
							}
							
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.ENFANT_SAUVAGE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.ENFANT_SAUVAGE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.CHIEN_LOUP)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_CL);
						System.out.println(main.CalledRoles.toString());
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.CHIEN_LOUP.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.CHIEN_LOUP)) {
							Player player = main.getPlayersByRole(Roles.CHIEN_LOUP).get(currentPlayerInPlayerList);
							
							if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.CHIEN_LOUP.getDisplayName())) {
								Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.CHIEN_LOUP.getDisplayName());
								
								inv.setItem(0, main.getItem(Material.BOWL, "§a§lChien", Arrays.asList("§7Vous transforme en §a§lChien§7.", "§7Vous appartiendez donc au camp du Village et devrez", "§7éliminer tous les Loups-Garous.", "", "§b>>Clique pour sélectionner")));
								
								inv.setItem(4, main.getItem(Material.RABBIT_STEW, "§c§lLoup", Arrays.asList("§7Vous transforme en §c§lLoup§7.", "§7Vous appartiendez donc au camp des Loups-Garous et devrez", "§7éliminer les Villageois.", "", "§b>>Clique pour sélectionner")));
								
								setWake(player);
								player.openInventory(inv);
								
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir. Vous serez donc un §a§lChien §cpendant cette partie.");
									main.playerlg.get(player.getName()).setCamp(RCamp.VILLAGE);
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								main.playerlg.get(player.getName()).setChoosenCampCL(main.playerlg.get(player.getName()).getCamp());
								player.closeInventory();
								if (main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU)) main.playerlg.get(player.getName()).setHasUsedPower(false);
								setSleep(player);
								main.CalledRoles.remove(Roles.CHIEN_LOUP);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.CHIEN_LOUP)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.CHIEN_LOUP)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.CHIEN_LOUP);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
			}
			
			
			
			else if (main.CalledRoles.get(0).equals(Roles.JUMEAU)) {

				if (currentTimer == Integer.MAX_VALUE) {
					main.setDisplayState(DisplayState.NUIT_JUMEAU);
					System.out.println(main.getDisplayState().name());
					
					currentTimer = 20;
				} else {
					LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.JUMEAU.getDisplayName());
					
					if (!main.RolesVoleur.contains(Roles.JUMEAU)) {
						Player player = main.getPlayersByRole(Roles.JUMEAU).get(currentPlayerInPlayerList);
						
						if (main.isType(Gtype.LIBRE)) {
							if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.JUMEAU.getDisplayName())) {
								Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.JUMEAU.getDisplayName());
								inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
								
								for (Player p : main.players) {
									if (!p.getName().equals(player.getName())) {
										inv.addItem(getHeadItem(p, "§d" + p.getName(), Arrays.asList("§7Sélectionne §d" + p.getName() + "§7comme Jumeau pour cette partie.", "§7A sa mort, vous récupérerez son rôle.", "§7S'il était infecté, charmé, en couple au autre, vous ne le serez pas.", "", "§b>>Clique pour sélectionner")));
									}
								}
								setWake(player);
								player.openInventory(inv);
								
							}
						} else {
							if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
							if (currentTimer == 22) {
								for (Player p : main.players)
									player.showPlayer(p);
								
								player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 20 secondes pour sélectionner quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
							}
						}
						
						if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
						
						if (currentTimer == 0) {
							Boolean isChoosed = false;
							if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
							
							if (!isChoosed) {
								player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							player.closeInventory();
							setSleep(player);
							main.CalledRoles.remove(Roles.JUMEAU);
							currentTimer = Integer.MAX_VALUE;
							if (main.CalledRoles.isEmpty()) return;
							if (main.CalledRoles.get(0).equals(Roles.JUMEAU)) {
								currentPlayerInPlayerList++;
							} else currentPlayerInPlayerList = 0;
						}
					} else {
						if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.JUMEAU)) {
							if (currentTimer == 15) {
								main.CalledRoles.remove(Roles.JUMEAU);
								currentPlayerInPlayerList = 0;
								currentTimer = Integer.MAX_VALUE;
							}
						}
					}
				}
			}
			
			
			
			if (main.CalledRoles.get(0).equals(Roles.NOCTAMBULE)) {

				if (currentTimer == Integer.MAX_VALUE) {
					main.setDisplayState(DisplayState.NUIT_NOCTA);
					System.out.println(main.getDisplayState().name());
					
					currentTimer = 22;
				} else {
					LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.NOCTAMBULE.getDisplayName());
					
					if (!main.RolesVoleur.contains(Roles.NOCTAMBULE)) {
						Player player = main.getPlayersByRole(Roles.NOCTAMBULE).get(currentPlayerInPlayerList);
						
						if (main.isType(Gtype.LIBRE)) {
							if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.NOCTAMBULE.getDisplayName())) {
								Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.NOCTAMBULE.getDisplayName());
								inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
								
								for (Player p : main.players) {
									if (!p.getName().equals(player.getName())) {
										inv.addItem(getHeadItem(p, "§b" + p.getName(), Arrays.asList("§7Passe la nuit chez §b" + p.getName() + "§7.", "§7Il connaîtra votre rôle mais il ne pourra pas utiliser son pouvoir de nuit s'il en a.", "", "§b>>Clique pour sélectionner")));
									}
								}
								setWake(player);
								player.openInventory(inv);
								
							}
						} else {
							if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
							if (currentTimer == 22) {
								for (Player p : main.players)
									player.showPlayer(p);
								
								player.sendMessage(main.getPrefix() + main.SendArrow + "§3Vous avez 22 secondes choisir chez qui passer la nuit. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
								player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
							}
						}
						
						if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
						
						if (currentTimer == 0) {
							Boolean isChoosed = false;
							if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
							
							if (!isChoosed) {
								player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							player.closeInventory();
							setSleep(player);
							main.CalledRoles.remove(Roles.NOCTAMBULE);
							currentTimer = Integer.MAX_VALUE;
							if (main.CalledRoles.isEmpty()) return;
							if (main.CalledRoles.get(0).equals(Roles.NOCTAMBULE)) {
								currentPlayerInPlayerList++;
							} else currentPlayerInPlayerList = 0;
						}
					} else {
						if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.NOCTAMBULE)) {
							if (currentTimer == 15) {
								main.CalledRoles.remove(Roles.NOCTAMBULE);
								currentPlayerInPlayerList = 0;
								currentTimer = Integer.MAX_VALUE;
							}
						}
					}
				}
			}
			
			
			
			
			else if (main.CalledRoles.get(0).equals(Roles.COMÉDIEN)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_COMEDIEN);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.COMÉDIEN.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.COMÉDIEN)) {
							try {
								Player player = main.getPlayersByRole(Roles.COMÉDIEN).get(currentPlayerInPlayerList);
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								if (currentTimer == 0) {
									Boolean isChoosed = false;
									if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
									
									if (!isChoosed) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir. Vous n'aurez donc aucun pouvoir cette nuit.");
										main.playerlg.get(player.getName()).setComédien(true);
									}
									player.closeInventory();
									setSleep(player);
									for (Player p : main.players)
										if (main.playerlg.get(p.getName()).isComédien())
											main.playerlg.get(p.getName()).setHasUsedPower(false);
									main.CalledRoles.remove(Roles.COMÉDIEN);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									if (main.CalledRoles.get(0).equals(Roles.COMÉDIEN)) {
										currentPlayerInPlayerList++;
									} else currentPlayerInPlayerList = 0;
									
								} else {
									
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.COMÉDIEN.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 9, "§6Inv " + Roles.COMÉDIEN.getDisplayName());
									inv.setItem(8, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Roles r : main.pouvoirsComédien) {
										/*ItemStack it = new ItemStack(Material.MAP, 1, main.getRoleMap(r).getDurability());
										ItemMeta itm = it.getItemMeta();
										itm.setDisplayName(r.getDisplayName());
										itm.setLore(Arrays.asList("§7Vous donne le pouvoir du rôle " + r.getDisplayName(), "§7pour ce tour. Pour ne pourrez donc plus l'utiliser.", "", "§b>>Clique pour sélectionner"));
										itm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
										it.setItemMeta(itm);
										
										inv.addItem(it);*/
									}
									
									setWake(player);
									player.openInventory(inv);
									
									}	
								}
							} catch (Exception err) {
								err.printStackTrace();
								for (Player p : main.players)
									if (main.playerlg.get(p.getName()).isComédien())
										main.playerlg.get(p.getName()).setHasUsedPower(false);
								main.CalledRoles.remove(Roles.COMÉDIEN);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.COMÉDIEN)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
							
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.COMÉDIEN)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.COMÉDIEN);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				} 
				
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.VOYANTE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_VOVO);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 22;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.VOYANTE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.VOYANTE)) {
							Player player = main.getPlayersByRole(Roles.VOYANTE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.VOYANTE.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.VOYANTE.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!p.getName().equals(player.getName())) {
											inv.addItem(getHeadItem(p, "§d" + p.getName(), Arrays.asList("§7Vous donne le rôle de §d" + p.getName() + "§7.", "§7Cependant, vous ne saurez pas s'il est infecté ou en couple etc...", "", "§b>>Clique pour sélectionner")));
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 22 secondes pour espionner quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.VOYANTE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.VOYANTE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.VOYANTE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.VOYANTE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.VOYANTE_D$AURA)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_VOVO_D$AURA);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 22;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.VOYANTE_D$AURA.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.VOYANTE_D$AURA)) {
							Player player = main.getPlayersByRole(Roles.VOYANTE_D$AURA).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.VOYANTE_D$AURA.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.VOYANTE_D$AURA.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players)
										if (!p.getName().equals(player.getName()))
											inv.addItem(getHeadItem(p, "§d" + p.getName(), Arrays.asList("§7Permet de savoir si §d" + p.getName() + "§7 est Loup ou non.", "", "§b>>Clique pour sélectionner")));
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 22 secondes pour espionner quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.VOYANTE_D$AURA);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.VOYANTE_D$AURA)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.VOYANTE_D$AURA)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.VOYANTE_D$AURA);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.ENCHANTEUR)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_ENCHANT);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 22;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de l' " + Roles.ENCHANTEUR.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.ENCHANTEUR)) {
							Player player = main.getPlayersByRole(Roles.ENCHANTEUR).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.ENCHANTEUR.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.ENCHANTEUR.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!p.getName().equals(player.getName()) && !player.getScoreboard().getTeam("LG").hasEntry(p.getName())) {
											inv.addItem(getHeadItem(p, "§d" + p.getName(), Arrays.asList("§7Enchante §d" + p.getName() + "§7.", "§7Vous saurez s'il appartient au camp des loups ou s'il est voyante.", "", "§b>>Clique pour sélectionner")));
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 22 secondes pour enchanter quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.ENCHANTEUR);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.ENCHANTEUR)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.ENCHANTEUR)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.ENCHANTEUR);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.DÉTECTIVE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_DÉTEC);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 28;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.DÉTECTIVE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.DÉTECTIVE)) {
							Player player = main.getPlayersByRole(Roles.DÉTECTIVE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.DÉTECTIVE.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.DÉTECTIVE.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!p.getName().equals(player.getName())) {
											inv.addItem(getHeadItem(p, "§8" + p.getName(), Arrays.asList("§7Inspecte le camp de §8" + p.getName() + "§7.", "§7Vous devrez choisir une deuxième personne.", "", "§b>>Clique pour sélectionner")));
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§8Vous avez 22 secondes pour comparer les camps de deux personnes. Pour faire cela, il suffit de cliquer sur les joueurs voulus.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.DÉTECTIVE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.DÉTECTIVE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.DÉTECTIVE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.DÉTECTIVE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.RENARD)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_RENARD);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 40;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.RENARD.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.RENARD)) {
							Player player = main.getPlayersByRole(Roles.RENARD).get(currentPlayerInPlayerList);
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.RENARD.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.RENARD.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										List<String> splayers = new ArrayList<>();
										int i = 0;
										for (Player p : main.players) splayers.add(p.getName());
										
										java.util.Collections.sort(splayers);
										
										for (String sp : splayers) {
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											int i2 = i - 1;
											if (i <= 0) i2 = splayers.size() - 1;
											int i3 = i + 1;
											if (i3 >= splayers.size()) i3 = 0;
											itm.setOwner(sp);
											itm.setDisplayName("§e" + sp);
											itm.setLore(Arrays.asList("§7Si vous flairez §e" + sp + " §7vous conserverez vos pouvoirs", "§7si parmis §e" + sp + "§7, §e" + splayers.get(i2) + " §7et §e" + splayers.get(i3), "§7se trouve un §c§lLoup-Garou§7 sinon,", "§7vous perdrez vos pouvoirs.", "", "§b>>Clique pour sélectionner"));
											it.setItemMeta(itm);
											inv.addItem(it);
											
											i++;
										}
									
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 40) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§6Vous avez 40 secondes pour renifler quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
								
								if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
								if (currentTimer == 0) {
									Boolean isChoosed = false;
									if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
									
									if (!isChoosed) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
										main.playerlg.get(player.getName()).setHasUsedPower(true);
									}
									player.closeInventory();
									if (player.getInventory().contains(Material.STONE_BUTTON)) player.getInventory().remove(Material.STONE_BUTTON);
									setSleep(player);
									main.CalledRoles.remove(Roles.RENARD);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									if (main.CalledRoles.get(0).equals(Roles.RENARD)) {
										currentPlayerInPlayerList++;
									} else currentPlayerInPlayerList = 0;
								}
								
							} else {
								if (currentTimer == 15) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous n'avez plus de pouvoir.");
									currentTimer = 0;
								}
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.RENARD)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.RENARD);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.PACIFISTE)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_PACIF);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.PACIFISTE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.PACIFISTE)) {
							Player player = main.getPlayersByRole(Roles.PACIFISTE).get(currentPlayerInPlayerList);
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.PACIFISTE.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.RENARD.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										
										for (Player p : main.players) {
											if (p.equals(player)) continue;
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											itm.setOwner(p.getName());
											itm.setDisplayName("§d" + p.getName());
											itm.setLore(Arrays.asList("§7Si vous sélectionnez §d" + p.getName() + ", vous", "§7allez révéler son rôle à tout le village.", "§7Mais il n'y aura pas de vote pendant la journée", "", "§b>>Clique pour sélectionner"));
											it.setItemMeta(itm);
											inv.addItem(it);
										}
									
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 20) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 20 secondes pour sélectionner quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
								
								if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
								if (currentTimer == 0) {
									Boolean isChoosed = false;
									if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
									
									if (!isChoosed) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
										main.playerlg.get(player.getName()).setHasUsedPower(true);
									}
									player.closeInventory();
									if (player.getInventory().contains(Material.STONE_BUTTON)) player.getInventory().remove(Material.STONE_BUTTON);
									setSleep(player);
									main.CalledRoles.remove(Roles.PACIFISTE);
									currentTimer = Integer.MAX_VALUE;
									if (main.CalledRoles.isEmpty()) return;
									if (main.CalledRoles.get(0).equals(Roles.PACIFISTE)) {
										currentPlayerInPlayerList++;
									} else currentPlayerInPlayerList = 0;
								}
								
							} else {
								if (currentTimer == 15) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous n'avez plus de pouvoir.");
									currentTimer = 0;
								}
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.PACIFISTE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.PACIFISTE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.FILLE_DE_JOIE)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_FDJ);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.FILLE_DE_JOIE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.FILLE_DE_JOIE)) {
							Player player = main.getPlayersByRole(Roles.FILLE_DE_JOIE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.FILLE_DE_JOIE.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.FILLE_DE_JOIE.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
										SkullMeta itm = (SkullMeta) it.getItemMeta();
										itm.setOwner(p.getName());
										itm.setDisplayName("§5" + p.getName());
										itm.setLore(Arrays.asList("§7Dormir chez §d" + main.getPlayerNameByAttributes(p, player) + "§7. Si vous faites cela et", "§7que les Loups vous attaquent, vous serez immunisé.", "§7Cependant, s'il est Loup ou attaqué par les Loups, vous mourrez.", "", "§b>>Clique pour sélectionner"));
										it.setItemMeta(itm);
										
										inv.addItem(it);
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 20) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§dVous avez 20 secondes pour choisir quelqu'un chez qui dormir. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.FILLE_DE_JOIE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.FILLE_DE_JOIE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.FILLE_DE_JOIE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.FILLE_DE_JOIE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.GARDE_DU_CORPS)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_GDC);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.GARDE_DU_CORPS.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.GARDE_DU_CORPS)) {
							Player player = main.getPlayersByRole(Roles.GARDE_DU_CORPS).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.GARDE_DU_CORPS.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.GARDE_DU_CORPS.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
										SkullMeta itm = (SkullMeta) it.getItemMeta();
										itm.setOwner(p.getName());
										itm.setDisplayName("§6" + p.getName());
										itm.setLore(Arrays.asList("§7Protéger le joueur §e" + p.getName() + "§7. Il sera donc", "§7protégé pour toute la nuit.", "§7Cependant, s'il est censé mourir peu importe la façon,", "§7vous mourrez à sa place."));
										it.setItemMeta(itm);
										
										inv.addItem(it);
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 20) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§7Vous avez 20 secondes pour protéger quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.GARDE_DU_CORPS);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.GARDE_DU_CORPS)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.GARDE_DU_CORPS)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.GARDE_DU_CORPS);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.SALVATEUR)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_SALVA);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.SALVATEUR.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.SALVATEUR)) {
							Player player = main.getPlayersByRole(Roles.SALVATEUR).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.SALVATEUR.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.SALVATEUR.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
										SkullMeta itm = (SkullMeta) it.getItemMeta();
										itm.setOwner(p.getName());
										itm.setDisplayName("§6" + p.getName());
										itm.setLore(Arrays.asList("§7Protéger le joueur §e" + p.getName() + "§7. Il sera donc", "§7protégé de l'attaque des Loups.", "§7Cependant, si la sorcière ou un autre rôle le", "§7cible, il mourra tout de même."));
										it.setItemMeta(itm);
										
										inv.addItem(it);
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 20) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§eVous avez 20 secondes pour protéger quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.SALVATEUR);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.SALVATEUR)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.SALVATEUR)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.SALVATEUR);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.LOUP_GAROU)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_LG);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 35;
					} else {
						List<Player> lgs = new ArrayList<>();
						for (Player player : main.players) {
							if (main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
								if (!main.playerlg.get(player.getName()).isNoctaTargeted())
									if (!main.playerlg.get(player.getName()).isRole(Roles.ENCHANTEUR))
										lgs.add(player);
							main.playerlg.get(player.getName()).showArmorStandOnlyForLG();
						}
						
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour des §c§lLoups-Garous");
						
						if (!lgs.isEmpty()) {
							
							for (Player player : lgs) {
								if (main.sleepingPlayers.contains(player)) {
									
									setWake(player);
									if (main.isType(Gtype.RÉUNION))
										for (Player p : main.players)
											player.showPlayer(p);
									player.getInventory().setItem(2, main.getItem(Material.BOOK, "§cDévorer un joueur", Arrays.asList("§fOuvre un menu pour dévorer un joueur.", "", "§b>>Clique pour ouvrir")));
								}
							}
							
							for (Player player : lgs) if (!player.getInventory().contains(Material.BOOK)) player.getInventory().setItem(2, main.getItem(Material.BOOK, "§cDévorer un joueur", Arrays.asList("§fOuvre un menu pour dévorer un joueur.", "", "§b>>Clique pour ouvrir")));
	
							for (Player player : lgs)
								for (Player p : lgs) player.showPlayer(p);
							
							if (main.playerlg.get(lgs.get(0).getName()).hasUsedPower() && currentTimer > 5) currentTimer = 5;
							
							if (currentTimer == 0) {
								List<Entry<Player, Integer>> votes = new ArrayList<>();
								boolean isGrailled = false;
								for (Player player : main.players) {
									SimpleEntry<Player, Integer> se = new SimpleEntry<>(player, main.playerlg.get(player.getName()).getVotes());
									votes.add(se);
									main.playerlg.get(player.getName()).resetVotes();
									main.playerlg.get(player.getName()).setVote(null);
								}
								
								while (votes.size() != 1) {
									Integer i1 = votes.get(0).getValue();
									Integer i2 = votes.get(1).getValue();
									
									if (i1.compareTo(i2) > 0) {
										votes.remove(1);
									} else if (i1.compareTo(i2) == 0) {
										int r = new Random().nextInt(2);
										if (r == 0) {
											votes.remove(1);
										} else {
											votes.remove(0);
										}
									} else {
										votes.remove(0);
									}
								}
								
								Player grailled = votes.get(0).getKey();
								PlayerLG grailledLG = main.playerlg.get(grailled.getName());
								int iVote = votes.get(0).getValue();
								if (iVote != 0) {
									grailledLG.setLGTargeted(true);
									isGrailled = true;
									main.sendRoleMessage(main.getPrefix() + main.SendArrow + "§c§l" + grailled.getName() + " §ca été dévoré pendant cette nuit...", Roles.LOUP_GAROU);
								}
								
								for (Player player : lgs) {
									
									if (!isGrailled) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir. Vous ne vous délectez donc de personne cette nuit...");
									}
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									player.getInventory().remove(Material.BOOK);
									player.closeInventory();
									setSleep(player);
									if (main.playerlg.get(player.getName()).isRole(Roles.GRAND_MÉCHANT_LOUP) || main.playerlg.get(player.getName()).isRole(Roles.LOUP_GAROU_BLANC) || main.playerlg.get(player.getName()).isRole(Roles.INFECT_PÈRE_DES_LOUPS))
										main.playerlg.get(player.getName()).setHasUsedPower(false);
								}
								if (grailledLG.isRole(Roles.ANCIEN)) {
									if (!grailledLG.hasUsedDefinitivePower()) {
										grailledLG.setLGTargeted(false);
										grailledLG.setUsedDefinitivePower(true);
									}
								} else if (grailledLG.isRole(Roles.CHAPERON_ROUGE)) {
									for (Player p : main.players) {
										if (main.playerlg.get(p.getName()).isRole(Roles.CHASSEUR)) {
											p.sendMessage(main.getPrefix() + main.SendArrow + "§aVous avez protégé §c" + grailled.getName() + "§a cette nuit.");
											grailledLG.setLGTargeted(false);
										}
									}
								} else if (grailledLG.isRole(Roles.DUR_À_CUIRE)) {
									
									grailledLG.setDACDying();
									grailledLG.setLGTargeted(false);
									grailled.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez été attaqué cette nuit. Grâce à votre rôle et à vos muscles, vous survivez jusqu'au matin prochain.");
									grailled.playSound(grailled.getLocation(), Sound.ZOMBIE_HURT, 8, 1.5f);
									
								} else if (grailledLG.isRole(Roles.FILLE_DE_JOIE)) {
									if (!grailledLG.getOtherPlayerHouse().getName().equals(grailled.getName()))
										grailledLG.setLGTargeted(false);
								} else if (grailledLG.isRole(Roles.HUMAIN_MAUDIT)) {
									if (!grailledLG.isCamp(RCamp.LOUP_GAROU)) {
										grailled.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez été attaqué par les Loups cette nuit. Vous devenez donc l'un d'entre eux.");
										main.sendRoleMessage(main.getPrefix() + main.SendArrow + "§c§l" + grailled.getName() + " §cest en fait humain maudit. Il se transforme donc un Loup-Garou suite à votre attaque.", Roles.LOUP_GAROU);
										grailledLG.setCamp(RCamp.LOUP_GAROU);
									}
								} else if (grailledLG.isRole(Roles.PORTEUR_DE_L$AMULETTE)) {
									grailledLG.setLGTargeted(false);
								}
								
								for (Player p : main.players)
									if (main.playerlg.get(p.getName()).isRole(Roles.FILLE_DE_JOIE))
										if (main.playerlg.get(main.playerlg.get(p.getName()).getOtherPlayerHouse().getName()).isLGTargeted() || main.playerlg.get(main.playerlg.get(p.getName()).getOtherPlayerHouse().getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(main.playerlg.get(p.getName()).getOtherPlayerHouse().getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
											main.playerlg.get(p.getName()).setFDJDied(true);
								
								main.CalledRoles.remove(Roles.LOUP_GAROU);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
							}
						} else {
							if (currentTimer == 28) {
								main.CalledRoles.remove(Roles.LOUP_GAROU);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
							}
						}
					}
				}
				
				
				else if (main.CalledRoles.get(0).equals(Roles.INFECT_PÈRE_DES_LOUPS)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_IPDL);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de l'" + Roles.INFECT_PÈRE_DES_LOUPS.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.INFECT_PÈRE_DES_LOUPS)) {
							Player player = main.getPlayersByRole(Roles.INFECT_PÈRE_DES_LOUPS).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							Player grailled = null;
							for (Player p : main.players) if (main.playerlg.get(p.getName()).isLGTargeted()) grailled = p;
							if (grailled == null) {
								if (currentTimer == 15) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cLes loups n'ont dévoré personne.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
									
									if (!main.playerlg.get(player.getName()).hasUsedPower()) {
										
										if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.INFECT_PÈRE_DES_LOUPS.getDisplayName())) {
											Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.INFECT_PÈRE_DES_LOUPS.getDisplayName());
											
											inv.setItem(0, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)5, "§a§lInfecter §e" + grailled.getName(), Arrays.asList("§7Infecter §e" + grailled.getName() + " §7lui fera rejoindre", "§7le camp des Loups-Garous.", "§7Cependant, il gardera ses pouvoirs de Villageois s'il en a.", "", "§b>>Clique pour sélectionner")));
											
											inv.setItem(4, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)14, "§cNe §lpas§c infecter §e" + grailled.getName(), Arrays.asList("§7N'infecte pas le joueur §e" + grailled.getName() + "§7.", "", "§b>>Clique pour sélectionner")));
											
											inv.setItem(2, getHeadItem(grailled, "§e" + grailled.getName(), Arrays.asList("§7Le joueur §e" + grailled.getName() + "§7 a été tué cette nuit.", "§7Voulez-vous §cl'infecter §7?")));
			
											setWake(player);
											player.openInventory(inv);
											
										}
									}
								} else {
									if (currentTimer == 15) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà utilisé votre pouvoir.");
										main.playerlg.get(player.getName()).setHasUsedPower(true);
										currentTimer = 0;
									}
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.INFECT_PÈRE_DES_LOUPS);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.INFECT_PÈRE_DES_LOUPS)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.INFECT_PÈRE_DES_LOUPS)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.INFECT_PÈRE_DES_LOUPS);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.GRAND_MÉCHANT_LOUP)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_GML);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.GRAND_MÉCHANT_LOUP.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.GRAND_MÉCHANT_LOUP)) {
							Player player = main.getPlayersByRole(Roles.GRAND_MÉCHANT_LOUP).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.GRAND_MÉCHANT_LOUP.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.GRAND_MÉCHANT_LOUP.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) && !main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) {
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											itm.setOwner(p.getName());
											itm.setDisplayName("§c" + p.getName());
											itm.setLore(Arrays.asList("§7Dévore le joueur §c" + p.getName() + "§7.", "§7Il sera éliminé de la partie.", "", "§b>>Clique pour sélectionner"));
											it.setItemMeta(itm);
											
											inv.addItem(it);
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 25) {
									for (Player p : main.players)
										if (!main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) && !main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
											player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§4Vous avez 25 secondes pour dévorer un villageois. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.GRAND_MÉCHANT_LOUP);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.GRAND_MÉCHANT_LOUP)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.GRAND_MÉCHANT_LOUP)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.GRAND_MÉCHANT_LOUP);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				else if (main.CalledRoles.get(0).equals(Roles.LOUP_GAROU_BLANC)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_LGB);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.LOUP_GAROU_BLANC.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.LOUP_GAROU_BLANC)) {
							Player player = main.getPlayersByRole(Roles.LOUP_GAROU_BLANC).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							List<Player> lgs = new ArrayList<>();
							for (Player p : main.players) {
								if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
									lgs.add(p);
							}
							if (lgs.size() == 1) {
								player.sendMessage(main.getPrefix() + main.SendArrow + "§cIl n'y a plus de loups à éliminer.");
								main.playerlg.get(player.getName()).setHasUsedPower(true);
								currentTimer = 0;
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.LOUP_GAROU_BLANC.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.LOUP_GAROU_BLANC.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : lgs) {
										if (!p.getName().equals(player.getName())) {
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											itm.setOwner(p.getName());
											itm.setDisplayName("§c" + p.getName());
											itm.setLore(Arrays.asList("§7Dévore le Loup-Garou §c" + p.getName() + "§7.", "§7Il sera éliminé de la partie.", "", "§b>>Clique pour sélectionner"));
											it.setItemMeta(itm);
											
											inv.addItem(it);
										}
									}
									if (inv.getContents().length != 0) {
										setWake(player);
										player.openInventory(inv);
									}
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 25) {
									for (Player p : lgs)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§4Vous avez 25 secondes pour tuer un Loup-Garou. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.LOUP_GAROU_BLANC);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.LOUP_GAROU_BLANC)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.LOUP_GAROU_BLANC)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.LOUP_GAROU_BLANC);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.PETITE_FILLE2)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_PF2);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.PETITE_FILLE2.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.PETITE_FILLE2)) {
							Player player = main.getPlayersByRole(Roles.PETITE_FILLE2).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.PETITE_FILLE2.getDisplayName())) {
								Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.PETITE_FILLE2.getDisplayName());
								inv.setItem(1, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)4, "§eRegarder", Arrays.asList("§7Observe le village cette nuit :", "§7il y a 20% de chance que vous trouviez un Loup.", "§7Mais également 5% de chance que vous mourrez.")));
								inv.setItem(3, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)11, "§cNe rien faire", Collections.singletonList("§7N'observe pas le village cette nuit.")));
								
								setWake(player);
								player.openInventory(inv);	
							}
								
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.PETITE_FILLE2);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.PETITE_FILLE2)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
							
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.PETITE_FILLE2)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.PETITE_FILLE2);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
			
				
				else if (main.CalledRoles.get(0).equals(Roles.SORCIÈRE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_SOSO);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 30;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.SORCIÈRE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.SORCIÈRE)) {
							Player player = main.getPlayersByRole(Roles.SORCIÈRE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (!player.getOpenInventory().getTopInventory().getName().startsWith("§6Inv ")) {
								Player grailled = null;
								for (Player p : main.players) if (main.playerlg.get(p.getName()).isLGTargeted()) grailled = p;
								Inventory binv = Bukkit.createInventory(null, InventoryType.BREWING, "§6Inv " + Roles.SORCIÈRE.getDisplayName());
								binv.setItem(1, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
								
								if (grailled != null)binv.setItem(3, getHeadItem(grailled, "§c" + grailled.getName(), Arrays.asList("§7En cette nuitée, §c" + grailled.getName() + "§7 a été englouti par les loups.", "§7Vous pouvez le réssusciter et/ou tuer quelqu'un d'autre.")));
								if (grailled == null)binv.setItem(3, main.getItem(Material.SKULL_ITEM, "§cPersonne", Arrays.asList("§7En cette nuitée, personne n'a été attaqué.", "§7Vous pouvez tout de même tuer quelqu'un.")));
								if (!main.SosoRézPots.get(player))binv.setItem(3, main.getItem(Material.BARRIER, "§cPlus de potions", Collections.singletonList("§cVous n'avez plus de potion pour §aréssuciter.")));
								
								if (grailled != null && main.SosoRézPots.get(player)) {
									Potion potréz = new Potion(PotionType.INSTANT_HEAL, 1);
									ItemStack itréz = potréz.toItemStack(1);
									ItemMeta itrézm = itréz.getItemMeta();
									itrézm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
									itrézm.setDisplayName("§aRéssusciter §c" + grailled.getName());
									itrézm.setLore(Arrays.asList("§7Réssucite le joueur §c" + grailled.getName() + "§7.", "§7Il ne sera donc pas éliminé.", "", "§b>>Clique pour sélectionner"));
									itréz.setItemMeta(itrézm);
									
									binv.setItem(0, itréz);
								}
									
								if (main.SosoKillPots.get(player)) {
									Potion potkill = new Potion(PotionType.INSTANT_DAMAGE, 1);
									ItemStack itkill = potkill.toItemStack(1);
									ItemMeta itkillm = itkill.getItemMeta();
									itkillm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
									itkillm.setDisplayName("§4Tuer un joueur");
									itkillm.setLore(Arrays.asList("§7Tue un autre joueur de la partie.", "§7On le retrouvera mort au petit matin.", "", "§b>>Clique pour sélectionner"));
									itkill.setItemMeta(itkillm);
									
									binv.setItem(2, itkill);
								}
								
								
								setWake(player);
								player.openInventory(binv);
								
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.SORCIÈRE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.SORCIÈRE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.SORCIÈRE)) {
								if (currentTimer == 20) {
									main.CalledRoles.remove(Roles.SORCIÈRE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				
				}
			
			
				
				else if (main.CalledRoles.get(0).equals(Roles.PRÊTRE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_PRÊTRE);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.PRÊTRE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.PRÊTRE)) {
							Player player = main.getPlayersByRole(Roles.PRÊTRE).get(currentPlayerInPlayerList);
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
							
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.PRÊTRE.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.PRÊTRE.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										
										for (Player p : main.players)
											if (!p.getName().equals(player.getName()))
												inv.addItem(getHeadItem(p, "§f" + p.getName(), Arrays.asList("§7Lance de l'eau bénite sur §f" + p.getName() + "§7.", "§7S'il est un Loup, il mourra, sinon, vous mourrez.", "", "§b>>Clique pour sélectionner")));
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 25) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§eVous avez 25 secondes pour jeter de l'eau bénite sur quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
							} else {
								if (currentTimer == 23)
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà utilisé votre pouvoir.");
								
								if (currentTimer == 20) {
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.PRÊTRE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.PRÊTRE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.PRÊTRE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.PRÊTRE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
				
				else if (main.CalledRoles.get(0).equals(Roles.NÉCROMANCIEN)) {
					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_NÉCRO);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.NÉCROMANCIEN.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.NÉCROMANCIEN)) {
							Player player = main.getPlayersByRole(Roles.NÉCROMANCIEN).get(currentPlayerInPlayerList);
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								int morts = 0;
								for (PlayerLG plg : main.playerlg.values())
									if (!plg.isVivant()) morts++;
								if (morts == 0) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cPersonne n'est encore mort.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
								
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.NÉCROMANCIEN.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.NÉCROMANCIEN.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										
										for (Player p : main.players) {
											if (p.equals(player) || main.playerlg.get(p.getName()).isVivant()) continue;
											ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
											SkullMeta itm = (SkullMeta) it.getItemMeta();
											itm.setOwner(p.getName());
											itm.setDisplayName("§b" + p.getName());
											itm.setLore(Arrays.asList("§7Réssuciter le joueur §b" + p.getName() + "§7. S'il avait un pouvoir", "§7il les perdras et deviendra Simple Villageois."));
											it.setItemMeta(itm);
											
											inv.addItem(it);
										}
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 24) {
										fakeplayers.put(player, new ArrayList<>());
										for (PlayerLG plg : main.playerlg.values())
											if (!plg.isVivant() && Bukkit.getOnlinePlayers().contains(plg.player)) {
												Player p = plg.player;
												Location loc = new Location(plg.getBlock().getWorld(), plg.getBlock().getX()+0.5, plg.getBlock().getY() + 1, plg.getBlock().getZ()+0.5, 0f, 0f);
												WorldServer s = ((CraftWorld) loc.getWorld()).getHandle();
											    World w = ((CraftWorld) p.getWorld()).getHandle();
											    String[] prop;
											    prop = textures(p.getName());
											    GameProfile gp = new GameProfile(p.getUniqueId(), p.getName());
											    gp.getProperties().put("textures", new Property("textures",prop[0],prop[1]));
											    EntityPlayer c = new EntityPlayer(MinecraftServer.getServer(), s, gp, new PlayerInteractManager(w));
											    loc = loc.setDirection(player.getLocation().subtract(loc).toVector());
											    c.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
											    byte flags = (byte) 0xFF;
											    c.getDataWatcher().watch(10,  flags) ;
											    PacketPlayOutPlayerInfo pi = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, c);
											    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(c);
											    PlayerConnection co = ((CraftPlayer) player).getHandle().playerConnection;
											    float yaw = loc.getYaw();
									            float pitch = loc.getPitch();
											    co.sendPacket(pi);
											    co.sendPacket(spawn);
											    co.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(c.getId(), (byte) ((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
									            co.sendPacket(new PacketPlayOutEntityHeadRotation(c, (byte) ((yaw%360.)*256/360)));
											    fakeplayers.get(player).add(c);
											}
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§bVous avez 25 secondes pour réssuciter quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
							} else {
								if (currentTimer == 23)
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà utilisé votre pouvoir.");
								
								if (currentTimer == 20) {
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
							}
								
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								if (fakeplayers.containsKey(player)) {
									for (EntityPlayer e : fakeplayers.get(player)) {
										PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
										connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, e));
										connection.sendPacket(new PacketPlayOutEntityDestroy(e.getId()));
									}
									fakeplayers.remove(player);
								}
								
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.NÉCROMANCIEN);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.NÉCROMANCIEN)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.NÉCROMANCIEN)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.NÉCROMANCIEN);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
				else if (main.CalledRoles.get(0).equals(Roles.VILAIN_GARÇON)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_VG);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 25;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.VILAIN_GARÇON.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.VILAIN_GARÇON)) {
							Player player = main.getPlayersByRole(Roles.VILAIN_GARÇON).get(currentPlayerInPlayerList);
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower() && !player.getInventory().contains(Material.STONE_BUTTON)) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
							
								if (main.isType(Gtype.LIBRE)) {
									if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.VILAIN_GARÇON.getDisplayName())) {
										Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.VILAIN_GARÇON.getDisplayName());
										inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
										
										for (Player p : main.players)
											if (!p.getName().equals(player.getName()))
												inv.addItem(getHeadItem(p, "§b" + p.getName(), Arrays.asList("§7Sélectionne §b" + p.getName() + "§7.", "§7En sélectionnant une autre personne,", "§7ils échangeront leurs rôles.", "", "§b>>Clique pour sélectionner")));
										setWake(player);
										player.openInventory(inv);
										
									}
								} else {
									if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
									if (currentTimer == 25) {
										for (Player p : main.players)
											player.showPlayer(p);
										
										player.sendMessage(main.getPrefix() + main.SendArrow + "§bVous avez 25 secondes pour échanger les rôles de deux personnes. Pour faire cela, il suffit de cliquer sur les joueurs voulus.");
										player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
									}
								}
							} else {
								if (currentTimer == 23)
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà utilisé votre pouvoir.");
								
								if (currentTimer == 20) {
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.VILAIN_GARÇON);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.VILAIN_GARÇON)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.VILAIN_GARÇON)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.VILAIN_GARÇON);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				else if (main.CalledRoles.get(0).equals(Roles.DICTATEUR)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_DICTA);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.DICTATEUR.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.DICTATEUR)) {
							Player player = main.getPlayersByRole(Roles.DICTATEUR).get(currentPlayerInPlayerList);
							
							
							if (!main.playerlg.get(player.getName()).hasUsedDefinitivePower()) {
								
								if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
									currentTimer = 0;
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.DICTATEUR.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.DICTATEUR.getDisplayName());
									inv.setItem(1, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)5, "§aFaire un coup d'état", Arrays.asList("§7Fait un coup d'état :", "§7vous serez le seul à pouvoir voter au jour prochain.", "§7Si vous votez un §c§lLoup§7, vous obtiendrez le rôle de maire, sinon,", "§7si vous votez, un villageois, vous vous suiciderez.")));
									inv.setItem(3, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)14, "§cNe rien faire", Collections.singletonList("§7Ne fait pas de coup d'état.")));
									
									setWake(player);
									player.openInventory(inv);
									
								}
								
								if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
								
							} else {
								
								if (currentTimer == 18)
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà utilisé votre pouvoir.");
								
								if (currentTimer == 15) {
									main.playerlg.get(player.getName()).setHasUsedPower(true);
									currentTimer = 0;
								}
								
							}
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.DICTATEUR);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.DICTATEUR)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
							
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.DICTATEUR)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.DICTATEUR);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
			
			
			
				else if (main.CalledRoles.get(0).equals(Roles.MAMIE_GRINCHEUSE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_MAMIE);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 20;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.MAMIE_GRINCHEUSE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.MAMIE_GRINCHEUSE)) {
							Player player = main.getPlayersByRole(Roles.MAMIE_GRINCHEUSE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.MAMIE_GRINCHEUSE.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.MAMIE_GRINCHEUSE.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players)
										if (!p.getName().equals(player.getName()))
											inv.addItem(getHeadItem(p, "§d" + p.getName(), Arrays.asList("§7Interdire à §d" + p.getName() + "§7le pouvoir de voter pour un tour.", "", "§b>>Clique pour sélectionner")));
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§dVous avez 20 secondes pour retirer le droit de vote à quelqu'un pour un tour. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.MAMIE_GRINCHEUSE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.MAMIE_GRINCHEUSE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.MAMIE_GRINCHEUSE)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.MAMIE_GRINCHEUSE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.CORBEAU)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_CORBEAU);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 22;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.CORBEAU.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.CORBEAU)) {
							Player player = main.getPlayersByRole(Roles.CORBEAU).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.CORBEAU.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.CORBEAU.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!p.getName().equals(player.getName())) {
											inv.addItem(getHeadItem(p, "§f" + p.getName(), Arrays.asList("§7Rendre visite à §f" + p.getName() + "§7.", "§7Il recevra 2 votes au début du prochain jour.", "", "§b>>Clique pour sélectionner")));
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 22) {
									for (Player p : main.players)
										player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§fVous avez 22 secondes pour rendre visite à quelqu'un. Pour faire cela, il suffit de cliquer sur le joueur voulu.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								Boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.CORBEAU);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.CORBEAU)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.CORBEAU)) {
								if (currentTimer == 15) {
									main.CalledRoles.remove(Roles.CORBEAU);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.JOUEUR_DE_FLÛTE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_JDF);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 30;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.JOUEUR_DE_FLÛTE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.JOUEUR_DE_FLÛTE)) {
							Player player = main.getPlayersByRole(Roles.JOUEUR_DE_FLÛTE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (main.isType(Gtype.LIBRE)) {
								if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.JOUEUR_DE_FLÛTE.getDisplayName())) {
									Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.JOUEUR_DE_FLÛTE.getDisplayName());
									inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
									
									for (Player p : main.players) {
										if (!main.playerlg.get(p.getName()).isCharmed() && !p.getName().equals(player.getName())) {
											inv.addItem(getHeadItem(p, "§d" + p.getName(), Collections.singletonList("§7Charme le joueur §d" + p.getName() + "§7.")));
										}
									}
									setWake(player);
									player.openInventory(inv);
									
								}
							} else {
								if (!main.playerlg.get(player.getName()).hasUsedPower()) setWake(player);
								if (currentTimer == 30) {
									for (Player p : main.players)
										if (!main.playerlg.get(p.getName()).isCharmed())
											player.showPlayer(p);
									
									player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez 30 secondes pour charmer 2 personnes. Pour faire cela, il suffit de cliquer sur les joueurs voulus.");
									player.getInventory().setItem(8, main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
								}
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.JOUEUR_DE_FLÛTE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.JOUEUR_DE_FLÛTE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.JOUEUR_DE_FLÛTE)) {
								if (currentTimer == 20) {
									main.CalledRoles.remove(Roles.JOUEUR_DE_FLÛTE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				
				}
			
			
				else if (main.CalledRoles.get(0).equals(Roles.PYROMANE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_PYRO);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 30;
					} else {
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.PYROMANE.getDisplayName());
						
						if (!main.RolesVoleur.contains(Roles.PYROMANE)) {
							Player player = main.getPlayersByRole(Roles.PYROMANE).get(currentPlayerInPlayerList);
							
							if (main.playerlg.get(player.getName()).isNoctaTargeted()) {
								currentTimer = 0;
								main.playerlg.get(player.getName()).setHasUsedPower(true);
							}
							
							if (!player.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§6Inv " + Roles.PYROMANE.getDisplayName()) && currentTimer == 30) {
								Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.PYROMANE.getDisplayName());
								inv.setItem(2, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
								
								inv.setItem(0, main.getItem(Material.LAVA_BUCKET, "§6Mettre de l'essence sur 2 joueurs", Arrays.asList("§7Sélectionner 2 joueurs qui", "§7seront recouvert d'essence jusqu'à la fin de la partie.", "", "§b>>Clique pour sélectionner")));
								
								List<String> lore = new ArrayList<>();
								lore.add("§7Tuer tous les joueurs qui sont couvert d'essence.");
								for (PlayerLG plg : main.playerlg.values())
									if (plg.isHuilé() && plg.isVivant())
										lore.add("§e" + main.getPlayerNameByAttributes(plg.player, player));
								if (lore.size() != 1)
									lore.set(0, "§7Liste des joueurs huilés §6(§e" + (lore.size() - 1) + "§6)§7 :");
								else
									lore.set(0, "§7Il n'y a actuellement §caucun §7joueur huilé.");
								lore.add("");
								lore.add("§b>>Clique pour sélectionner");
								inv.setItem(4, main.getItem(Material.FLINT_AND_STEEL, "§6Faire brûler les joueurs huilés", lore));
								setWake(player);
								player.openInventory(inv);
							}
							
							if (main.playerlg.get(player.getName()).hasUsedPower()) currentTimer = 0;
							
							if (currentTimer == 0) {
								boolean isChoosed = false;
								if (main.playerlg.get(player.getName()).hasUsedPower()) isChoosed = true;
								
								if (!isChoosed) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
									main.playerlg.get(player.getName()).setHasUsedPower(true);
								}
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.PYROMANE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.PYROMANE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
						} else {
							if (currentPlayerInPlayerList + 1 == main.AliveRoles.get(Roles.PYROMANE)) {
								if (currentTimer == 20) {
									main.CalledRoles.remove(Roles.PYROMANE);
									currentPlayerInPlayerList = 0;
									currentTimer = Integer.MAX_VALUE;
								}
							}
						}
					}
				
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.SOEUR)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_SOEUR);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 35;
					} else {
						List<Player> soeurs = new ArrayList<>();
						int ps = 0;
						while (soeurs.size() != 1) {
							Player player = main.players.get(ps);
							if (main.playerlg.get(player.getName()).isRole(Roles.SOEUR) && !main.playerlg.get(player.getName()).hasUsedPower() && !main.playerlg.get(player.getName()).isNoctaTargeted()) soeurs.add(player);
							ps++;
						}
						soeurs.add(main.playerlg.get(soeurs.get(0).getName()).getsoeur().get(0));
						
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour des " + Roles.SOEUR.getDisplayName() + "s");
						
						for (Player player : soeurs) {
							if (main.sleepingPlayers.contains(player)) {
								
								player.showPlayer(main.playerlg.get(player.getName()).getsoeur().get(0));
								setWake(player);
							}
						}
						
						if (currentTimer == 0) {
							
							for (Player player : soeurs) {

								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.SOEUR);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.SOEUR)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
							
						}
					}
				
				}
				
				
				
				else if (main.CalledRoles.get(0).equals(Roles.FRÈRE)) {

					if (currentTimer == Integer.MAX_VALUE) {
						main.setDisplayState(DisplayState.NUIT_FRERE);
						System.out.println(main.getDisplayState().name());
						
						currentTimer = 35;
					} else {
						List<Player> frères = new ArrayList<>();
						int ps = 0;
						while (frères.size() != 1) {
							Player player = main.players.get(ps);
							if (main.playerlg.get(player.getName()).isRole(Roles.FRÈRE) && !main.playerlg.get(player.getName()).hasUsedPower() && !main.playerlg.get(player.getName()).isNoctaTargeted()) frères.add(player);
							ps++;
						}
						frères.add(main.playerlg.get(frères.get(0).getName()).getfrère().get(0));
						frères.add(main.playerlg.get(frères.get(0).getName()).getfrère().get(1));
						
						LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour des " + Roles.FRÈRE.getDisplayName() + "s");
						
						for (Player player : frères) {
							if (main.sleepingPlayers.contains(player)) {
								
								setWake(player);
							}
						}
						
						if (currentTimer == 0) {
							
							for (Player player : frères) {
								main.playerlg.get(player.getName()).setHasUsedPower(true);
								player.closeInventory();
								setSleep(player);
								main.CalledRoles.remove(Roles.FRÈRE);
								currentTimer = Integer.MAX_VALUE;
								if (main.CalledRoles.isEmpty()) return;
								if (main.CalledRoles.get(0).equals(Roles.FRÈRE)) {
									currentPlayerInPlayerList++;
								} else currentPlayerInPlayerList = 0;
							}
							
						}
					}
				}
				
				
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (currentTimer != Integer.MAX_VALUE) player.setLevel(currentTimer);
				}
				if (currentTimer != Integer.MAX_VALUE) currentTimer--;
		
	}
	
	
	private void setSleep(Player player) {
		if (main.isType(Gtype.LIBRE)) {
			Block b = main.playerlg.get(player.getName()).getBlock();
			Block block = Bukkit.getWorld("LG").getBlockAt(b.getX(), b.getY(), b.getZ());
			
			player.teleport(block.getLocation());
			((CraftPlayer) player).getHandle().a(new BlockPosition(b.getX(), b.getY(), b.getZ()));
			player.setSleepingIgnored(false);
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 2, false, false));

		for (Player p : main.players) {
			player.hidePlayer(p);
		}
		if (player.getInventory().contains(main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")))) player.getInventory().remove(main.getItem(Material.BARRIER, "§cAnnuler", Collections.singletonList("§7Annule l'action en cours.")));
		if (!main.sleepingPlayers.contains(player)) main.sleepingPlayers.add(player);
	}
	
	private void setWake(Player player) {
		if (main.isType(Gtype.LIBRE)) player.damage(0.1);
		main.sleepingPlayers.remove(player);
		if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
			player.removePotionEffect(PotionEffectType.BLINDNESS);
		if (main.isType(Gtype.RÉUNION) && !main.isDisplayState(DisplayState.NUIT_NÉCRO)) {
			for (Player p : main.players)
				player.showPlayer(p);
		}
	}
	
	public static ItemStack getHeadItem(Player player, String name, List<String> lore) {
		ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta itm = (SkullMeta)it.getItemMeta();
		itm.setOwner(player.getName());
		itm.setDisplayName(name);
		itm.setLore(lore);
		it.setItemMeta(itm);
		
		return it;
	}

	
	
    public static String[] textures(String name) {
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
    
}
