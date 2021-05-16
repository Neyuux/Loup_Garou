package fr.neyuux.lgthierce;

import fr.neyuux.lgthierce.role.RCamp;
import fr.neyuux.lgthierce.role.RDeck;
import fr.neyuux.lgthierce.role.Roles;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class GameConfig implements Listener {
	
	private final LG main;
	
	public GameConfig(LG main) {
		this.main = main;
	}

	public void startConfig() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			 for (Entry<String, List<UUID>> en : main.getGrades().entrySet()) {
					if (en.getValue().contains(player.getUniqueId())) {
						player.getInventory().setItem(6, getComparator());
					}
				}
		 }
		
		main.AddedRoles.clear();
		main.DeckRoles.clear();
		main.pouvoirsComédien.clear();
		main.pouvoirsComédien.add(Roles.VOYANTE);
		main.pouvoirsComédien.add(Roles.MONTREUR_D$OURS);
		main.pouvoirsComédien.add(Roles.ANCIEN);
		
		main.initialiseRoles();
	}
	
	
	
	@EventHandler
	public void onInteractComparator(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack current = player.getItemInHand();
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) {
			if (current.equals(getComparator())) {
				
				if (main.isType(Gtype.NONE)) {
					Inventory invType = Bukkit.createInventory(null, 27, "§2§lType §cde la Partie");
					setInvCoin(invType, (byte)13, 0, (byte)1);
					setInvCoin(invType, (byte)13, 8, (byte)2);
					setInvCoin(invType, (byte)13, 18, (byte)3);
					setInvCoin(invType, (byte)13, 26, (byte)4);
					
					invType.setItem(11, main.getItem(Material.ENDER_PORTAL_FRAME, "§e§lLibre", Arrays.asList("§7Les joueurs sont libres de se balader", "§7dans le village.")));
					invType.setItem(15, main.getItem(Material.IRON_FENCE, "§d§lRéunion", Arrays.asList("§7Les joueurs sont bloqués sur un nénuphar.", "§7A eux de discuter sans se déplacer.")));
					
					player.openInventory(invType);
				}
				
				
				else {
					player.openInventory(getGameConfigInv(player));
				}
				
			}
		}
	}
	
	@EventHandler
	public void onInvInteractComparator(InventoryClickEvent ev) {
		if (ev.getCurrentItem() == null) return;
		if (ev.getCurrentItem().equals(getComparator())) {
			ev.setCancelled(true);
			if (!main.isType(Gtype.NONE)) {
				ev.getWhoClicked().closeInventory();
				ev.getWhoClicked().openInventory(getGameConfigInv((Player)ev.getWhoClicked()));
			} else {
				Inventory invType = Bukkit.createInventory(null, 27, "§2§lType §cde la Partie");
				setInvCoin(invType, (byte)13, 0, (byte)1);
				setInvCoin(invType, (byte)13, 8, (byte)2);
				setInvCoin(invType, (byte)13, 18, (byte)3);
				setInvCoin(invType, (byte)13, 26, (byte)4);
				
				invType.setItem(11, main.getItem(Material.ENDER_PORTAL_FRAME, "§e§lLibre", Arrays.asList("§7Les joueurs sont libres de se balader", "§7dans le village.")));
				invType.setItem(15, main.getItem(Material.IRON_FENCE, "§d§lRéunion", Arrays.asList("§7Les joueurs sont bloqués sur un block.", "§7A eux de discuter sans se déplacer.", "", "§c§lMAX : §n12 JOUEURS")));
				
				ev.getWhoClicked().openInventory(invType);
			}
		}
	}
	
	
	
	@EventHandler
	public void onInvChooseType(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		Inventory inv = event.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equalsIgnoreCase("§2§lType §cde la Partie")) {
			event.setCancelled(true);
			
			if (current.getType().equals(Material.ENDER_PORTAL_FRAME)) {
				setGameType(player, "§e§lLibre", Gtype.LIBRE);
				main.initialiseBeds();
								
			} else if (current.getType().equals(Material.IRON_FENCE)) {
				if (main.players.size() <= 12) {
					setGameType(player, "§d§lRéunion", Gtype.RÉUNION);
					main.initialiseNightBlocks();
				} else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§2Le mode §lRéunion §2nécessite §c12 joueurs MAXIMUM§2.");
					player.closeInventory();
				}
				
			} else if (current.getType().equals(Material.ARROW)) {
				player.openInventory(getGameConfigInv(player));
			}
			
			for (Player p : Bukkit.getOnlinePlayers())
				main.updateScoreboard(p);
			
		}
	}
	
	
	
	
	@EventHandler
	public void onConfigGameInv(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		Inventory inv = event.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equalsIgnoreCase("§c§lConfiguration")) {
			event.setCancelled(true);
			
			if (current.getType().equals(Material.BARRIER)) {
				Inventory invReset = Bukkit.createInventory(null, InventoryType.HOPPER, "§b§lReset §bla map");
				
				ItemStack oui = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
				ItemMeta ouim = oui.getItemMeta();
				ouim.setDisplayName("§a§lConfirmer");
				ouim.setLore(Arrays.asList("§fConfirme le", "§freset de la map."));
				oui.setItemMeta(ouim);
				
				ItemStack non = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
				ItemMeta nonm = non.getItemMeta();
				nonm.setDisplayName("§c§lRefuser");
				nonm.setLore(Arrays.asList("§fRefuse le", "§freset de la map."));
				non.setItemMeta(nonm);
				
				invReset.setItem(0, oui);
				invReset.setItem(4, non);
				
				player.openInventory(invReset);
			}
			
			
			
			else if (current.getType().equals(Material.ITEM_FRAME)) {
				Inventory invType = Bukkit.createInventory(null, 27, "§2§lType §cde la Partie");
				setInvCoin(invType, (byte)13, 0, (byte)1);
				setInvCoin(invType, (byte)13, 8, (byte)2);
				setInvCoin(invType, (byte)13, 18, (byte)3);
				setInvCoin(invType, (byte)13, 26, (byte)4);
				
				invType.setItem(11, main.getItem(Material.ENDER_PORTAL_FRAME, "§e§lLibre", Arrays.asList("§7Les joueurs sont libres de se balader", "§7dans le village.")));
				invType.setItem(15, main.getItem(Material.IRON_FENCE, "§d§lRéunion", Arrays.asList("§7Les joueurs sont bloqués sur un nénuphar.", "§7A eux de discuter sans se déplacer.")));
				invType.setItem(26, getFlècheRetour());
				
				player.openInventory(invType);
			}
			
			
			
			
			else if (current.getType().equals(Material.EMPTY_MAP)) {
				
				player.openInventory(getChooseDeckInv());
			}
			
			
			else if (current.getType().equals(Material.APPLE)) {
				
				player.openInventory(getConfigInv());
			}
			
			
			
			else if (current.getType().equals(Material.SKULL_ITEM)) {
				
				player.openInventory(getJoueursInv());
			}
		}
	}


	
	
	
	
	
	@EventHandler
	public void onResetInv(InventoryClickEvent ev) {
		Player player = (Player)ev.getWhoClicked();
		ItemStack current = ev.getCurrentItem();
		Inventory inv = ev.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equals("§b§lReset §bla map")) {
			ev.setCancelled(true);
			
			if (current.hasItemMeta()) {
				if (current.getItemMeta().hasDisplayName()) {
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§a§lConfirmer")) {
						main.rel();
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§b" + player.getName() + " §ea reset la map !");
					} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lRefuser")) {
						player.openInventory(getGameConfigInv(player));
					}
				}
			}
			
		}
	}
	
	
	
	
	@EventHandler
	public void onRolesInv(InventoryClickEvent ev) {
		Player player = (Player)ev.getWhoClicked();
		ItemStack current = ev.getCurrentItem();
		Inventory inv = ev.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equals("§6§lMenu §bDecks")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.WOOL)) {
				if (current.getItemMeta().getDisplayName().startsWith("§a") || current.getItemMeta().getDisplayName().startsWith("§6")) {
					RDeck deck = null;
					String dn = current.getItemMeta().getDisplayName();
					for (RDeck d : RDeck.values()) {
						if (dn.contains(d.name().replace('_', ' '))) deck = d;
					}
					
					player.openInventory(getChooseCampInv(deck));
				}
				
				
				else if (current.getItemMeta().getDisplayName().startsWith("§c")) {
					player.closeInventory();
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cCe deck est incompatible avec un autre deck déjà utilisé !");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 7, 1);
				}
			}
			
			
			else if (current.getType().equals(Material.ARROW)) player.openInventory(getGameConfigInv(player));
		}
		
		
		
		else if (inv.getName().startsWith("§aCamp §bDeck §l")) {
			ev.setCancelled(true);
			RDeck deck = RDeck.getByAlias(inv.getName().split("§aCamp §bDeck §l")[1]);
			Inventory rinv = null;
			int i = 0;
			
			if (current.getType().equals(Material.WOOL)) {

				switch (current.getItemMeta().getDisplayName()) {
					case "§cCamp des §lLoups-Garous":
						rinv = getRolesCampInv(deck, "lg");
						break;
					case "§aCamp du §lVillage":
						rinv = getRolesCampInv(deck, "village");
						break;
					case "§6Camp des §lAutres":
						rinv = getRolesCampInv(deck, "autres");
						break;
				}
				for (ItemStack it : rinv.getContents())
					if (it != null)
						i++;
				if (i > 1)
					player.openInventory(rinv);
				else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cIl n'y a pas de rôle de ce camp dans ce deck.");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 7, 1);
				}
				
			} else if (current.getType().equals(Material.ARROW)) player.openInventory(getChooseDeckInv());
		}
		
		
		
		else if (inv.getName().startsWith("§6§lRôles ")) {
			ev.setCancelled(true);
			InventoryAction action = ev.getAction();
			RDeck deck = null;
			int i = 0;
			while(deck == null) {
				ItemStack it = inv.getItem(i);
				if (it == null) return;
				if (it.getType().equals(Material.STAINED_GLASS_PANE)) {
					for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet())
						for (Roles r : en.getValue())
							if (("§a" + r.getName()).equalsIgnoreCase(it.getItemMeta().getDisplayName()) || ("§c" + r.getName()).equalsIgnoreCase(it.getItemMeta().getDisplayName()))
								deck = r.getDeck();
				}
			}
 			
			if (current.getType().equals(Material.STAINED_GLASS_PANE)) {
				
				if (action.equals(InventoryAction.PICKUP_ALL)) {
					
					if (current.getItemMeta().getDisplayName().contains("SOEUR")) {
						addRoleToList(Roles.SOEUR, 2, current);
						
					} else if (current.getItemMeta().getDisplayName().contains("FRERE")) {
						addRoleToList(Roles.FRÈRE, 3, current);
						
					} else {
						
						if (current.getItemMeta().getDisplayName().contains("SORCIERE")) {
							addRoleToList(Roles.SORCIÈRE, 1, current);
							
						} else if (current.getItemMeta().getDisplayName().contains("INFECT PERE DES LOUPS")) {
							addRoleToList(Roles.INFECT_PÈRE_DES_LOUPS, 1, current);
							
						} else if (current.getItemMeta().getDisplayName().contains("PRETRE")) {
							addRoleToList(Roles.PRÊTRE, 1, current);
							
						} else {
							Roles role = null;
							String itemRoleName = current.getItemMeta().getDisplayName();

							for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet())
								for (Roles r : en.getValue())
									if (("§a" + r.getName()).equalsIgnoreCase(itemRoleName) || ("§c" + r.getName()).equalsIgnoreCase(itemRoleName))
										role = r;


							if (role.equals(Roles.JOUEUR_DE_FLÛTE) || role.equals(Roles.CUPIDON) || role.equals(Roles.VOLEUR) || role.equals(Roles.COMÉDIEN) || role.equals(Roles.PYROMANE))
								if (main.AddedRoles.containsKey(role)) 
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cIl n'y peut avoir qu'un seul " + role.getDisplayName() + "§c.");
							addRoleToList(role, 1, current);
						}
					}
					
				} else if (action.equals(InventoryAction.PICKUP_HALF)) {

					if (current.getItemMeta().getDisplayName().contains("SOEUR")) {
						delRoleToList(Roles.SOEUR, 2, current);
						
					} else if (current.getItemMeta().getDisplayName().contains("FRERE")) {
						delRoleToList(Roles.FRÈRE, 3, current);
						
					} else {
						
						if (current.getItemMeta().getDisplayName().contains("SORCIERE")) {
							delRoleToList(Roles.SORCIÈRE, 1, current);
							
						} else if (current.getItemMeta().getDisplayName().contains("INFECT PERE DES LOUPS")) {
							delRoleToList(Roles.INFECT_PÈRE_DES_LOUPS, 1, current);
							
						} else if (current.getItemMeta().getDisplayName().contains("PRETRE")) {
							delRoleToList(Roles.PRÊTRE, 1, current);
							
						} else {
							Roles role = null;
							String itemRoleName = current.getItemMeta().getDisplayName();

							for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet())
								for (Roles r : en.getValue())
									if (("§a" + r.getName()).equalsIgnoreCase(itemRoleName) || ("§c" + r.getName()).equalsIgnoreCase(itemRoleName))
										role = r;
										
							
							delRoleToList(role, 1, current);
						}
					}
					
				}
				
			} else if (current.getType().equals(Material.ARROW)) player.openInventory(getChooseCampInv(deck));
			
			int roles = 0;
			for (Entry<Roles, Integer> addedroles : main.AddedRoles.entrySet())
				roles = roles + addedroles.getValue();
			for (Player p : Bukkit.getOnlinePlayers()) {
				SimpleScoreboard ss = new SimpleScoreboard(main.getPrefix(), p);
				ss.add(p.getDisplayName(), 15);
				ss.add("§0", 14);
				ss.add("§6Nombre de §lRôles §f: §e" + roles, 13);
				ss.add("§aNombre de §lJoueurs §f: §e" + main.players.size(), 12);
				ss.add("§7Joueurs en ligne §f: §e" + Bukkit.getOnlinePlayers().size(), 11);
				ss.add("§2§lType §2de jeu §f: §c" + main.getType().toString(), 10);
				ss.add("§7", 9);
				ss.add("§8------------", 8);
				ss.add("§e§oMap by §c§l§oNeyuux_", 7);
				p.setScoreboard(ss.getScoreboard());
			}
		}
	}
	
	
	
	
	
	@EventHandler
	public void onParamInv(InventoryClickEvent ev) {
		Player player = (Player)ev.getWhoClicked();
		ItemStack current = ev.getCurrentItem();
		Inventory inv = ev.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equals("§f§lParamètres §cde la partie")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.WATCH)) {
				main.cycleJourNuit = !main.cycleJourNuit;
				
				inv.setItem(11, getParamItem(Material.WATCH, (short)0, current.getItemMeta().getDisplayName(), Arrays.asList("§fActive ou non le changement", "§fd'atmosphère selon le cycle jour/nuit", "§f§o(Faisant passer du jour à la nuit)"), main.cycleJourNuit));
				
			} else if (current.getType().equals(Material.SIGN)) {
				main.chatDesLg = !main.chatDesLg;
				
				inv.setItem(13, getParamItem(Material.SIGN, (short)0, "§cChat des loups", Arrays.asList("§fActive ou non le chat", "§fentre loups-garous la nuit."), main.chatDesLg));
			
			} else if (current.getType().equals(Material.TRIPWIRE_HOOK)) {
				main.cupiTeamCouple = !main.cupiTeamCouple;
				
				inv.setItem(15, getParamItem(Material.TRIPWIRE_HOOK, (short)0, Roles.CUPIDON.getDisplayName() + "§f gagne avec son couple", Arrays.asList("§fActive ou non la possibilité", "§fque Cupidon gagne avec son couple."), main.cupiTeamCouple));

			} else if (current.getType().equals(Material.SKULL_ITEM)) {
				main.maire = !main.maire;
				
				inv.setItem(21, getParamItem(Material.SKULL_ITEM, (short)0, "§e§lMaire", Arrays.asList("§fActive ou non le fait que le", "§fmaire soit activé."), main.maire));

			} else if (current.getType().equals(Material.PUMPKIN)) {
				Inventory invComédien = Bukkit.createInventory(null, 9, "§6Pouvoirs " + Roles.COMÉDIEN.getDisplayName());
				invComédien.setItem(8, getFlècheRetour());
				
				invComédien.addItem(getPouvComédienItem(Roles.VOYANTE));
				invComédien.addItem(getPouvComédienItem(Roles.ANCIEN));
				invComédien.addItem(getPouvComédienItem(Roles.MONTREUR_D$OURS));
				invComédien.addItem(getPouvComédienItem(Roles.PETITE_FILLE));
				invComédien.addItem(getPouvComédienItem(Roles.RENARD));
				invComédien.addItem(getPouvComédienItem(Roles.SALVATEUR));
				invComédien.addItem(getPouvComédienItem(Roles.SORCIÈRE));
				
				player.openInventory(invComédien);
				
			} else if (current.getType().equals(Material.WEB)) {
				main.maitreRandom = !main.maitreRandom;
				
				inv.setItem(29, getParamItem(Material.WEB, (short)0, "§fModèle de l'" + Roles.ENFANT_SAUVAGE.getDisplayName() + " §7random", Arrays.asList("§fActive ou non la sélection aléatoire", "§fdu modèle de l'Enfant Sauvage."), main.maitreRandom));
				
			} else if (current.getType().equals(Material.BOW)) {
				main.coupleRandom = !main.coupleRandom;
				
				inv.setItem(31, getParamItem(Material.BOW, (short)0, "§dCouple" + " §7random", Arrays.asList("§fActive ou non la sélection", "§faléatoire du couple."), main.coupleRandom));

			} else if (current.getType().equals(Material.HOPPER)) {
				main.cupiEnCouple = !main.cupiEnCouple;
				
				inv.setItem(33, getParamItem(Material.HOPPER, (short)0, Roles.CUPIDON.getDisplayName() + "§d en Couple", Arrays.asList("§fActive ou non la sélection", "§fdu Cupidon dans le couple.."), main.cupiEnCouple));

			} else if (current.getType().equals(Material.PAPER)) {
				main.paroleChaman = !main.paroleChaman;
				
				inv.setItem(39, getParamItem(Material.PAPER, (short)0, "§fParole du " + Roles.CHAMAN.getDisplayName(), Arrays.asList("§fSi cette option est activée, le", "§fChaman pourra envoyer des message aux morts (en plus de les recevoir).", "§fSinon il pourra juste recevoir les messages des morts."), main.paroleChaman));

			} else if (current.getType().equals(Material.EYE_OF_ENDER)) {
				main.voyanteBavarde = !main.voyanteBavarde;
				
				inv.setItem(41, getParamItem(Material.EYE_OF_ENDER, (short)0, Roles.VOYANTE.getDisplayName() + " §bbavarde", Arrays.asList("§fSi cette option est activée, le", "§frôle que la Voyante a vu sera affiché", "§fpubliquement dans le chat."), main.voyanteBavarde));
			
			} else if (current.getType().equals(Material.ARROW) && current.getItemMeta().getDisplayName().equalsIgnoreCase("§cRetour")) {
				player.openInventory(getGameConfigInv(player));
			}
		}
		
		
		
		else if (inv.getName().equalsIgnoreCase("§6Pouvoirs " + Roles.COMÉDIEN.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.STAINED_GLASS_PANE)) {
			
				if (current.getItemMeta().getDisplayName().startsWith("§a")) {
					Roles role = null;
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§aSORCIERE")) role = Roles.SORCIÈRE;
					else {
						String itemRoleName = current.getItemMeta().getDisplayName();

						for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet())
							for (Roles r : en.getValue())
								if (("§a" + r.getName()).equalsIgnoreCase(itemRoleName))
									role = r;
					}
					
					main.pouvoirsComédien.remove(role);
					current.setItemMeta(getPouvComédienItem(role).getItemMeta());
					current.setData(getPouvComédienItem(role).getData());
					current.setDurability(getPouvComédienItem(role).getDurability());
				}
				
				
				else {
					Roles role = null;
					if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§cSORCIERE")) role = Roles.SORCIÈRE;
					else {
						String itemRoleName = current.getItemMeta().getDisplayName();

						for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet())
							for (Roles r : en.getValue())
								if (("§c" + r.getName()).equalsIgnoreCase(itemRoleName))
									role = r;
					}
					
					main.pouvoirsComédien.add(role);
					current.setItemMeta(getPouvComédienItem(role).getItemMeta());
					current.setData(getPouvComédienItem(role).getData());
					current.setDurability(getPouvComédienItem(role).getDurability());
				}
				
			} else if (current.getType().equals(Material.ARROW)) player.openInventory(getConfigInv());
		}
	}
	
	
	
	
	
	
	@EventHandler
	public void onJoueursInv(InventoryClickEvent ev) {
		Player player = (Player)ev.getWhoClicked();
		ItemStack current = ev.getCurrentItem();
		Inventory inv = ev.getInventory();
		
		if (current == null) return;
		
		if (inv.getName().equals(getJoueursInv().getName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				player.openInventory(getPlayerInv(p, current));
				
			} else if (current.getType().equals(Material.ARROW)) player.openInventory(getGameConfigInv(player));
		}
		
		
		
		else if (inv.getName().startsWith("§6Menu §b")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SUGAR)) {
				SkullMeta skm = (SkullMeta) inv.getItem(4).getItemMeta();
				Player p = Bukkit.getPlayer(skm.getOwner());
				
				if (main.spectators.contains(p)) {
					main.spectators.remove(p);
					p.getInventory().clear();
					p.setMaxHealth(20);
					p.setHealth(20);
					for (PotionEffect pe : p.getActivePotionEffects()) {
						p.removePotionEffect(pe.getType());
					}
					PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false);
					p.addPotionEffect(saturation);
					main.clearArmor(p);
					p.setDisplayName(p.getName());
					p.setPlayerListName(p.getName());
					p.setGameMode(GameMode.ADVENTURE);
					p.teleport(new Location(Bukkit.getWorld("LG"), 494, 12.2, 307, 0f, 0f));
					
					p.getInventory().setItem(1, main.getItem(Material.GHAST_TEAR, "§7§lDevenir Spectateur", Arrays.asList("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit")));
					p.getInventory().setItem(5, main.getItem(Material.EYE_OF_ENDER, "§a§lJouer", Arrays.asList("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit")));
					
					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AFJoueur").addEntry(p.getName());
					main.setPlayerGrade(p);
					if (!Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()).getName().equalsIgnoreCase("AFJoueur")) p.getInventory().setItem(6, main.config.getComparator());
					p.sendMessage(main.getPrefix() + main.SendArrow + player.getDisplayName() + " §9vous a retiré du mode §7§lSpectateur§9.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "§b" + p.getName() + " §7a bien été §cretiré§7 du mode §lSpectateur§7.");

				} else {
					main.players.remove(p);
					main.spectators.add(p);
					
					p.getInventory().clear();
					p.setGameMode(GameMode.SPECTATOR);
					p.setDisplayName("§8[§7Spectateur§8]" + p.getName());
					p.setPlayerListName(p.getDisplayName());
					Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()).removeEntry(p.getName());
					p.sendMessage(main.getPrefix() + main.SendArrow + "§9Votre mode de jeu a été établi en §7spectateur§9 par " + player.getDisplayName() + "§9.");
					p.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/spec off§c.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "§b" + p.getName() + " §7a bien été §amit§7 en §lSpectateur§7.");

				}
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 6, 1);
				String spec = "§cNon";
				if (main.spectators.contains(player)) spec = "§aOui";
				ItemMeta itm = current.getItemMeta();
				List<String> lore = itm.getLore();
				lore.remove(1);
				lore.add("§3Actuel : " + spec);
				itm.setLore(lore);
				current.setItemMeta(itm);
				
			} else if (current.getType().equals(Material.ARROW)) player.openInventory(getJoueursInv());
			
			else if (current.getType().equals(Material.STAINED_GLASS_PANE)) {
				
				Anvil.openAnvilInventory(player, inv.getItem(4));
			}
		}
		
		else if (inv.getType().equals(InventoryType.ANVIL)) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				if (current.hasItemMeta()) {
					if (current.getItemMeta().hasDisplayName()) {

						Roles r = null;
						Player p = Bukkit.getPlayer(((SkullMeta)current.getItemMeta()).getOwner());
						for (Entry<RDeck, List<Roles>> en : main.DeckRoles.entrySet()) {
							for (Roles role : en.getValue()) {
								
								if (role.getName().equalsIgnoreCase(current.getItemMeta().getDisplayName()))
									r = role;
								
							}
						}
						
						if (r != null) {
							main.playerlg.get(p.getName()).setRole(r);
							System.out.println("forcerole " + player.getName() + " > " + p.getName() + " ---> " + r.getName());
							player.closeInventory();
							player.getInventory().remove(Material.SKULL_ITEM);
						}
						
					}
				}
			}
		}
		
	}




	public ItemStack getComparator() {
		ItemStack c = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta cm = c.getItemMeta();
		cm.setDisplayName("§c§lConfiguration de la partie");
		cm.addEnchant(Enchantment.DURABILITY, 9, true);
		cm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		c.setItemMeta(cm);
	
		return c;
	}
	
	public ItemStack getFlècheRetour() {
		ItemStack it = new ItemStack(Material.ARROW);
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName("§cRetour");
		itm.setLore(Arrays.asList("§fRetourner au", "§fmenu précédent."));
		it.setItemMeta(itm);

		return it;
	}
	
	public ItemStack getColoredItem(Material mat, int amount, short color, String name, List<String> lore) {
		ItemStack it = new ItemStack(mat, amount, color);
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(name);
		itm.setLore(lore);
		
		it.setItemMeta(itm);
		return it;
	}
	
	private ItemStack getDeckItem(RDeck deck) {
		Wool w = new Wool();
		w.setColor(DyeColor.LIME);
		if (!main.AddedRoles.isEmpty()) {
			for (Entry<Roles, Integer> en : main.AddedRoles.entrySet()) {
				if (en.getKey().getDeck().equals(deck))
					w.setColor(DyeColor.ORANGE);
			}
		}
		
		String color = "§";
		String can = "non";
		ItemStack it = w.toItemStack();
		it.setAmount(1);
		ItemMeta itm = it.getItemMeta();
		
		if (w.getColor().equals(DyeColor.LIME)) {
			color = "§a";
			can = "Utilisable";
		} else if (w.getColor().equals(DyeColor.ORANGE)) {
			color = "§6";
			can = "Utilisé";
		} else if (w.getColor().equals(DyeColor.RED)) {
			color = "§c";
			can = "Inutilisable";
		}
		
		itm.setDisplayName(color + "§l" + deck.name().replace('_', ' '));
		itm.setLore(Arrays.asList(color + "Ce deck est §l" + can + color + ".", "", "§f§lCrédit : " + "§e" + deck.getCreator(), "", "§cAttention : §eCertains decks sont incompatibles", "§eavec d'autres !", "", "§b>>Cliquez pour ouvrir ce deck"));
		
		it.setItemMeta(itm);
		return it;
	}
	
	private ItemStack getRoleItem(Roles r) {
		ItemStack it;
		if (main.AddedRoles.containsKey(r)) {
			it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			ItemMeta itm = it.getItemMeta();
			itm.setDisplayName("§a" + r.getName().replace('è', 'e').replace('ê', 'e').replace('à', 'a').toUpperCase());
			itm.setLore(Arrays.asList("§3Nombre de §l" + r.getName() + " §b: §b§l" + main.AddedRoles.get(r), "", "§e>>Clique droit pour retirer", "§a>>Clique gauche pour ajouter"));
			it.setItemMeta(itm);
		} else {
			it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			ItemMeta itm = it.getItemMeta();
			itm.setDisplayName("§c" + r.getName().replace('è', 'e').replace('ê', 'e').replace('à', 'a').toUpperCase());
			itm.setLore(Arrays.asList("§3Nombre de §l" + r.getName() + " §b: §b" + "§l0", "", "§e>>Clique droit pour retirer", "§a>>Clique gauche pour ajouter"));
			it.setItemMeta(itm);
		}
		return it;
	}
	
	private ItemStack getPouvComédienItem(Roles r) {
		ItemStack it;
		if (main.pouvoirsComédien.contains(r)) {
			it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			ItemMeta itm = it.getItemMeta();
			itm.setDisplayName("§a" + r.getName().replace('è', 'e').toUpperCase());
			itm.setLore(Arrays.asList("§bValeur : §2§lUtilisé", "", "§e>>Clique pour retirer"));
			it.setItemMeta(itm);
		} else {
			it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			ItemMeta itm = it.getItemMeta();
			itm.setDisplayName("§c" + r.getName().replace('è', 'e').toUpperCase());
			itm.setLore(Arrays.asList("§bValeur : §4§lPas Utilisé", "", "§a>>Clique pour ajouter"));
			it.setItemMeta(itm);
		}
		return it;
	}
	
	private ItemStack getParamItem(Material type, short data, String name, List<String> lore, Object mainObj) {
		ItemStack it = new ItemStack(type, 1, data);
		ItemMeta itm = it.getItemMeta();
		String v = "valeur";
		itm.setDisplayName(name);

		if (mainObj != null) {
			if (mainObj instanceof Boolean) {
				Boolean b = (Boolean) mainObj;
				if (b) v = "§aOui";
				if (!b) v = "§cNon";
			} else if (mainObj instanceof String) {
				v = (String) mainObj;
			} else if (mainObj instanceof ArrayList<?>) {
				StringBuilder s = new StringBuilder("§c");
				for (Roles r : main.pouvoirsComédien) {
					if (s.toString().equalsIgnoreCase("§c")) {
						s.append(r.getName());
					} else s.append(", ").append(r.getName());
				}
				v = s.toString();
			} else {
				v = mainObj.toString().replace("[", "").replace("]", "");
			}
		}
		List<String> offlore = new ArrayList<>();
		offlore.add("§bValeur : §c" + v);
		offlore.add("");
		offlore.addAll(lore);
		itm.setLore(offlore);
		
		it.setItemMeta(itm);
		return it;
	}
	
	
	private void setInvCoin(Inventory inv, Byte color, int slot, Byte sens) {
		ItemStack verre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)color);
		if (sens == 1) {
		inv.setItem((slot), verre);
		inv.setItem((slot + 9), verre);
		inv.setItem((slot + 1), verre);
		} else if (sens == 2) {
			inv.setItem((slot), verre);
			inv.setItem((slot + 9), verre);
			inv.setItem((slot - 1), verre);
		} else if (sens == 3) {
			inv.setItem((slot), verre);
			inv.setItem((slot - 9), verre);
			inv.setItem((slot + 1), verre);
		} else if (sens == 4) {
			inv.setItem((slot), verre);
			inv.setItem((slot - 9), verre);
			inv.setItem((slot - 1), verre);
		}
	}
	
	private void addRoleToList(Roles r, int iOfRole, ItemStack current) { 
		if (main.AddedRoles.containsKey(r)) {
			int added = main.AddedRoles.get(r) + iOfRole;
			if (!r.equals(Roles.JOUEUR_DE_FLÛTE) && !r.equals(Roles.VOLEUR) && !r.equals(Roles.CUPIDON) && !r.equals(Roles.COMÉDIEN) && !r.equals(Roles.PYROMANE))
				main.AddedRoles.put(r, added);
		} else {
			main.AddedRoles.put(r, iOfRole);
		}
		current.setItemMeta(getRoleItem(r).getItemMeta());
		current.setDurability(getRoleItem(r).getDurability());
	}
	
	private void delRoleToList(Roles r, int iOfRole, ItemStack current) {
		if (main.AddedRoles.containsKey(r)) {
			if (main.AddedRoles.get(r) != iOfRole) {
				
				main.AddedRoles.put(r, main.AddedRoles.get(r) - iOfRole);
				
			} else if (main.AddedRoles.get(r) == iOfRole) main.AddedRoles.remove(r);
			
		}
		current.setItemMeta(getRoleItem(r).getItemMeta());
		current.setDurability(getRoleItem(r).getDurability());
	}
	
	
	private void setGameType(Player player, String stype, Gtype type) {
		main.setType(type);
		LG.sendTitleForAllPlayers("§fLe §2type §fde jeu "+stype+"§f a été choisi !", "§b" + player.getName() + " §ea choisi le §2type de jeu §e!", 20, 60, 20);
		for(Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("§2§lType §cde la Partie")) p.closeInventory();
		
		if (main.players.contains(p)) {
			main.players.remove(p);
			
			p.teleport( new Location(Bukkit.getWorld("LG"), 495, 11.2, 307, 0f, 0f));
			p.getInventory().setItem(5, main.getItem(Material.EYE_OF_ENDER, "§a§lJouer", Arrays.asList("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit")));
		}
		}
	
	}
	
	
	private Inventory getGameConfigInv(Player player) {
		Inventory inv = Bukkit.createInventory(null, 45, "§c§lConfiguration");
		List<String> ops = new ArrayList<>();
		setInvCoin(inv, (byte)14, 0, (byte)1);
		setInvCoin(inv, (byte)14, 8, (byte)2);
		setInvCoin(inv, (byte)14, 36, (byte)3);
		setInvCoin(inv, (byte)14, 44, (byte)4);
		
		for (Entry<String, List<UUID>> en : main.getGrades().entrySet()) {
			List<UUID> l = en.getValue();
			for (Player p : Bukkit.getOnlinePlayers()) {
			if (l.contains(p.getUniqueId())) {
				ops.add(p.getDisplayName());
			}
			}
		}
		inv.setItem(38, main.getItem(Material.SIGN, "§cListe des §lConfigurateurs", ops));
		
		ItemStack phead = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta pheadmeta = (SkullMeta) phead.getItemMeta();
		pheadmeta.setOwner(player.getName());
		pheadmeta.setLore(Arrays.asList("§fPermet de gérer", "§fles joueurs", "§f§o(spectateur, etc)"));
		pheadmeta.setDisplayName("§6Joueurs");
		phead.setItemMeta(pheadmeta);
		inv.setItem(30, phead);
		
		inv.setItem(32, main.getItem(Material.BARRIER, "§bReset la Map", Arrays.asList("§fPermet de reset", "§fla map.")));
		
		inv.setItem(13, main.getItem(Material.APPLE, "§f§lParamètres de la Partie", Arrays.asList("§fPermet de changer les", "§foptions de la partie.")));
		
		inv.setItem(15, main.getItem(Material.EMPTY_MAP, "§6§lRôles", Arrays.asList("§fPermet de gérer les", "§frôles de la partie.")));

		inv.setItem(11, main.getItem(Material.ITEM_FRAME, "§2Changer le §lType §2de jeu", Arrays.asList("§fPermet de changer le", "§ftype de jeu de la partie.", "", "§eActuel : §c§l" + main.getType().toString())));
		
		return inv;
	}
	
	private Inventory getConfigInv() {
		Inventory inv = Bukkit.createInventory(null, 45, "§f§lParamètres §cde la partie");
		setInvCoin(inv, (byte)0, 0, (byte)1);
		setInvCoin(inv, (byte)0, 8, (byte)2);
		setInvCoin(inv, (byte)0, 36, (byte)3);
		setInvCoin(inv, (byte)0, 44, (byte)4);
		inv.setItem(44, getFlècheRetour());
		//Modif des jours/nuits
		//Chat des lg
		//Cupi + couple
		//Couple aléatoire
		//Maire
		//Rôles du comédien
		//Modèle aléatoire
		
		inv.setItem(11, getParamItem(Material.WATCH, (short)0, "§fModification du §6cycle §eJour§f/§9Nuit", Arrays.asList("§fActive ou non le changement", "§fd'atmosphère selon le cycle jour/nuit", "§f§o(Faisant passer du jour à la nuit)"), main.cycleJourNuit));
		
		inv.setItem(13, getParamItem(Material.SIGN, (short)0, "§cChat des loups", Arrays.asList("§fActive ou non le chat", "§fentre loups-garous la nuit."), main.chatDesLg));
		
		inv.setItem(15, getParamItem(Material.TRIPWIRE_HOOK, (short)0, Roles.CUPIDON.getDisplayName() + "§f gagne avec son couple", Arrays.asList("§fActive ou non la possibilité", "§fque Cupidon gagne avec son couple."), main.cupiTeamCouple));
		
		inv.setItem(21, getParamItem(Material.SKULL_ITEM, (short)0, "§e§lMaire", Arrays.asList("§fActive ou non le fait que le", "§fmaire soit activé."), main.maire));
		
		inv.setItem(23, getParamItem(Material.PUMPKIN, (short)0, "§ePouvoirs du " + Roles.COMÉDIEN.getDisplayName(), Arrays.asList("§fListe des pouvoirs que le", "§fcomédien (s'il est activé) peut utiliser pendant la partie."), main.pouvoirsComédien));
		
		inv.setItem(29, getParamItem(Material.WEB, (short)0, "§fModèle de l'" + Roles.ENFANT_SAUVAGE.getDisplayName() + " §7random", Arrays.asList("§fActive ou non la sélection aléatoire", "§fdu modèle de l'Enfant Sauvage."), main.maitreRandom));
		
		inv.setItem(31, getParamItem(Material.BOW, (short)0, "§dCouple" + " §7random", Arrays.asList("§fActive ou non la sélection", "§faléatoire du couple."), main.coupleRandom));

		inv.setItem(33, getParamItem(Material.HOPPER, (short)0, Roles.CUPIDON.getDisplayName() + "§d en Couple", Arrays.asList("§fActive ou non la sélection", "§fdu Cupidon dans le couple."), main.cupiEnCouple));
		
		inv.setItem(39, getParamItem(Material.PAPER, (short)0, "§fParole du " + Roles.CHAMAN.getDisplayName(), Arrays.asList("§fSi cette option est activée, le", "§fChaman pourra envoyer des message aux morts",  "§f(en plus de les recevoir).", "§fSinon il pourra juste recevoir les messages des morts."), main.paroleChaman));
		
		inv.setItem(41, getParamItem(Material.EYE_OF_ENDER, (short)0, Roles.VOYANTE.getDisplayName() + " §bbavarde", Arrays.asList("§fSi cette option est activée, le", "§frôle que la Voyante a vu sera affiché", "§fpubliquement dans le chat."), main.voyanteBavarde));
		
		return inv;
	}
	
	private Inventory getJoueursInv() {
		Inventory inv = null;

		int nb = Bukkit.getOnlinePlayers().size();
		if (nb <= 4) {
			inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 8) {
			inv = Bukkit.createInventory(null, 9, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 17) {
			inv = Bukkit.createInventory(null, 18, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 26) {
			inv = Bukkit.createInventory(null, 27, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 35) {
			inv = Bukkit.createInventory(null, 36, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 44) {
			inv = Bukkit.createInventory(null, 45, "§c§lConfiguration §6Joueurs");
		} else if (nb <= 53) {
			inv = Bukkit.createInventory(null, 54, "§c§lConfiguration §6Joueurs");
		}
		
		inv.setItem(inv.getSize() - 1, getFlècheRetour());
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta itm = (SkullMeta) it.getItemMeta();
			List<String> lore = new ArrayList<>();
			itm.setDisplayName(player.getDisplayName());
			itm.setOwner(player.getName());
			lore.add("");
			

			String spec = "§cNon";
			if (main.spectators.contains(player)) spec = "§aOui";
			lore.add("§7Spectateur : " + spec);
			
			String op = "§aPossède §caucun grade";
			for (Entry<String, List<UUID>> en : main.getGrades().entrySet()) {
				if (en.getValue().contains(player.getUniqueId())) {
					String g = en.getKey();
					String gradeplace = "ABCDE";
					if (g.equalsIgnoreCase("Dieu")) gradeplace = "A";
					if (g.equalsIgnoreCase("DieuM")) gradeplace = "B";
					if (g.equalsIgnoreCase("DieuX")) gradeplace = "C";
					if (g.equalsIgnoreCase("DieuE")) gradeplace = "D";
					if (g.equalsIgnoreCase("Démon")) gradeplace = "E";
					if (g.equalsIgnoreCase("Leader")) gradeplace = "F";
					op = "§aPossède le grade " + Bukkit.getScoreboardManager().getMainScoreboard().getTeam("A" + gradeplace + g).getPrefix();
				}
			}
			lore.add(op);
			
			itm.setLore(lore);
			it.setItemMeta(itm);
			
			inv.addItem(it);
		}
		
		return inv;
	}
	
	private Inventory getPlayerInv(Player player, ItemStack current) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Menu §b" + player.getName());
		setInvCoin(inv, (byte)1, 0, (byte)1);
		setInvCoin(inv, (byte)1, 8, (byte)2);
		setInvCoin(inv, (byte)1, 18, (byte)3);
		setInvCoin(inv, (byte)1, 26, (byte)4);
		inv.setItem(26, getFlècheRetour());
		
		inv.setItem(4, current);
		
		String spec = "§cNon";
		if (main.spectators.contains(player)) spec = "§aOui";
		inv.setItem(12, main.getItem(Material.SUGAR, "§fMettre §b" + player.getName() + " §fen §7Spectateur§f.", Arrays.asList("", "§3Actuel : " + spec)));
		
		return inv;
	}
	
	private Inventory getRolesCampInv(RDeck deck, String camp) {
		Inventory inv;
		String invName = null;
		Integer invsize = null;
		List<Roles> Roles = new ArrayList<>();
		
		for (Roles r : main.DeckRoles.get(deck)) {
			
			if (camp.equalsIgnoreCase("lg")) {
				invName = "§c§lLoups-Garous";
				if (r.getCamp().equals(RCamp.LOUP_GAROU) || r.getCamp().equals(RCamp.LOUP_GAROU_BLANC)) {
					Roles.add(r);
				}
			} else if (camp.equalsIgnoreCase("village")) {
				invName = "§a§ldu Village";
				if (r.getCamp().equals(RCamp.VILLAGE)) {
					Roles.add(r);
				}
			} else if (camp.equalsIgnoreCase("autres")) {
				invName = "§6§lAutres";
				if (!r.getCamp().equals(RCamp.VILLAGE) && !r.getCamp().equals(RCamp.LOUP_GAROU) && !r.getCamp().equals(RCamp.LOUP_GAROU_BLANC)) {
					Roles.add(r);
				}
			}
			
		}
		
		if (Roles.size() < 36) invsize = 36;
		if (Roles.size() < 27) invsize = 27;
		if (Roles.size() < 18) invsize = 18;
		if (Roles.size() < 9) invsize = 9;
		inv = Bukkit.createInventory(null, invsize, "§6§lRôles " + invName);
		inv.setItem(invsize - 1, getFlècheRetour());
		
		for (Roles r : Roles) {
			inv.addItem(getRoleItem(r));
		}
		
		return inv;
	}
	
	private Inventory getChooseCampInv(RDeck deck) {
		Inventory invDeck = Bukkit.createInventory(null, 9, "§aCamp §bDeck §l" + deck.getAlias());
		invDeck.setItem(8, getFlècheRetour());
		
		invDeck.setItem(0, getColoredItem(Material.WOOL, 1, (short)14, "§cCamp des §lLoups-Garous", Arrays.asList("§4Affiche les rôles", "§4du camp des loups.", "§b>>Cliquer pour afficher.")));
		invDeck.setItem(6, getColoredItem(Material.WOOL, 1, (short)1, "§6Camp des §lAutres", Arrays.asList("§eAffiche les rôles", "§edu camp des autres rôles.", "§b>>Cliquer pour afficher.")));
		invDeck.setItem(3, getColoredItem(Material.WOOL, 1, (short)5, "§aCamp du §lVillage", Arrays.asList("§2Affiche les rôles", "§2du camp du village.", "§b>>Cliquer pour afficher.")));
		
		return invDeck;
	}
	
	private Inventory getChooseDeckInv() {
		Inventory invDeck = Bukkit.createInventory(null, 9, "§6§lMenu §bDecks");
		invDeck.setItem(8, getFlècheRetour());
		
		for (RDeck deck : RDeck.values())
			invDeck.addItem(getDeckItem(deck));
		
		return invDeck;
	}
	

	
}
