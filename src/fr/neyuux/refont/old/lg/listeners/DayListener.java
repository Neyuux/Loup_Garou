package fr.neyuux.refont.old.lg.listeners;

import fr.neyuux.refont.old.lg.*;
import fr.neyuux.refont.old.lg.role.RCamp;
import fr.neyuux.refont.old.lg.role.Roles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class DayListener implements Listener {
	
	private final LG main;
	
	public DayListener(LG main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent ev) {
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		if (!main.players.contains(ev.getPlayer())) return;
		
		Player player = ev.getPlayer();
		String msg = ev.getMessage();
		ev.setCancelled(false);
		
		if (!main.playerlg.get(ev.getPlayer().getName()).getCouple().isEmpty())
			if (ev.getMessage().startsWith("!")) {
				main.playerlg.get(ev.getPlayer().getName()).getCouple().get(0).sendMessage(main.getPlayerNameByAttributes(ev.getPlayer(), main.playerlg.get(ev.getPlayer().getName()).getCouple().get(0)) + main.SendArrow + ev.getMessage().substring(1));
				ev.setCancelled(true);
			}
				
		ev.setFormat("§e" + player.getName() + main.SendArrow + "§f" + msg);
	}
	
	@EventHandler
	public void onMoove(PlayerMoveEvent ev) {
		if (!main.isType(Gtype.RÉUNION)) return;
		if (!main.isState(Gstate.PLAYING)) return;
		if (main.isCycle(Gcycle.NONE)) return;
		
		Player player = ev.getPlayer();
		Location from = ev.getFrom();
		Location to = ev.getTo();
		
		if (!main.players.contains(player)) return;
		
		if (from.distanceSquared(to) > 0.001D)
			ev.setTo(from);
	}
	
	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent ev) {
		if (!main.isCycle(Gcycle.JOUR) && !main.isType(Gtype.RÉUNION)) return;
		if (!main.isState(Gstate.PLAYING)) return;
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		Player player = ev.getPlayer();
		if (!main.players.contains(player)) return;
		PlayerLG playerlg = main.playerlg.get(player.getName());
		
		 if (ev.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			 
			 if (main.isDisplayState(DisplayState.ELECTIONS_DU_MAIRE)) {
				 if (!playerlg.canVote()) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous ne pouvez pas voter.");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 8, 1);
					return;
				 }
				 
				 Player p = playerlg.getPlayerOnCursor(main.players);
				 if (p == null) return;
				 PlayerLG plg = main.playerlg.get(p.getName());
				 
				 if (playerlg.getVotedPlayer() != null) {
					 if (!playerlg.getVotedPlayer().getUniqueId().equals(p.getUniqueId())) {
						 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9" + player.getName() + " §3vote pour élire §b§l" + p.getName() + "§3 en tant que maire.");
						 plg.addVote();
						
						 Player vp = playerlg.getVotedPlayer();
						 main.playerlg.get(vp.getName()).removeVote();
						 playerlg.setVote(p);
					 } else {
						main.playerlg.get(playerlg.getVotedPlayer().getName()).removeVote();
						playerlg.setVote(null);
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9" + player.getName() + " §3a annulé son vote.");
					 }
				 } else {
					 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9" + player.getName() + " §3vote pour élire §b§l" + p.getName() + "§3 en tant que maire.");
					 plg.addVote();

					 if (playerlg.getVotedPlayer() != null) {
						 Player vp = playerlg.getVotedPlayer();
						 main.playerlg.get(vp.getName()).removeVote();
					 }
					 playerlg.setVote(p);
				 }

				 int votes = 0;
				 List<Player> ps = new ArrayList<>(main.players);
					for (Player psp : ps)
						votes = votes + main.playerlg.get(psp.getName()).getVotes();
					if (votes == ps.size())
						for (Player psp : ps) main.playerlg.get(psp.getName()).setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.VOTE)) {
				 if (!playerlg.canVote()) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous ne pouvez pas voter.");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 8, 1);
					return;
				 }
				 
				 Player p = playerlg.getPlayerOnCursor(main.players);
				 if (p == null) return;
				 PlayerLG plg = main.playerlg.get(p.getName());
				 
				 if (playerlg.getVotedPlayer() != null) {
					 if (!playerlg.getVotedPlayer().getUniqueId().equals(p.getUniqueId())) {
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §evote pour §6§l" + p.getName());
						plg.addVote();
						
						Player vp = playerlg.getVotedPlayer();
						main.playerlg.get(vp.getName()).removeVote();
						playerlg.setVote(p);
					 } else {
						 main.playerlg.get(playerlg.getVotedPlayer().getName()).removeVote();
						 playerlg.setVote(null);
						 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §ea annulé son vote.");
					 }
				 } else {
					 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §evote pour §6§l" + p.getName());
					 plg.addVote();

					 if (playerlg.getVotedPlayer() != null) {
						 Player vp = playerlg.getVotedPlayer();
						 main.playerlg.get(vp.getName()).removeVote();
					 }
					 playerlg.setVote(p);
				 }
				 
				 List<Player> ps = new ArrayList<>();
				 int votes = 0;
				 for (Player psp : main.players)
					 if (main.playerlg.get(psp.getName()).canVote())
						ps.add(psp);
				 for (Player psp : main.players)
					 votes = votes + main.playerlg.get(psp.getName()).getVotes();
				 if (votes == ps.size())
					 for (Player psp : ps) main.playerlg.get(psp.getName()).setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.VOTE2)) {
				 if (!playerlg.canVote()) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous ne pouvez pas voter.");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 8, 1);
					return;
				 }
				 
				 List<Player> votable = new ArrayList<>();
				 for (Player p : main.players)
					 if (!main.playerlg.get(p.getName()).isInEqual())
						 votable.add(p);
				 
				 Player p = playerlg.getPlayerOnCursor(votable);
				 if (p == null) return;
				 PlayerLG plg = main.playerlg.get(p.getName());
				 
				 if (playerlg.getVotedPlayer() != null) {
					 if (!playerlg.getVotedPlayer().getUniqueId().equals(p.getUniqueId())) {
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §evote pour §6§l" + p.getName());
						plg.addVote();
						
						Player vp = playerlg.getVotedPlayer();
						main.playerlg.get(vp.getName()).removeVote();
						playerlg.setVote(p);
					 } else {
						 main.playerlg.get(playerlg.getVotedPlayer().getName()).removeVote();
						 playerlg.setVote(null);
						 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §ea annulé son vote.");
					 }
				 } else {
					 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §evote pour §6§l" + p.getName());
					 plg.addVote();

					 if (playerlg.getVotedPlayer() != null) {
						 Player vp = playerlg.getVotedPlayer();
						 main.playerlg.get(vp.getName()).removeVote();
					 }
					 playerlg.setVote(p);
				 }

				 int votes = 0;
				 List<Player> ps = new ArrayList<>(main.players);
					for (Player psp : ps)
						votes = votes + main.playerlg.get(psp.getName()).getVotes();
					if (votes == ps.size())
						for (Player psp : ps) main.playerlg.get(psp.getName()).setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.VOTE_MAIRE)) {
				 List<Player> votable = new ArrayList<>();
				 for (Player p : main.players)
					 if (!main.playerlg.get(p.getName()).isInEqual())
						 votable.add(p);
				 
				 System.out.println(votable);
				 Player p = playerlg.getPlayerOnCursor(votable);
				 if (p == null) return;
				 if (!playerlg.isMaire()) return;
				 PlayerLG plg = main.playerlg.get(p.getName());
				 
				 plg.setDayTargeted(true);
				 playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.TIR_CHASSEUR)) {
				 List<Player> votable = new ArrayList<>();
				 for (Player p : main.players)
					 if (!p.getUniqueId().equals(player.getUniqueId()))
						 votable.add(p);
				 
				 Player p = playerlg.getPlayerOnCursor(votable);
				 if (p == null) return;
				 
				 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cDans un dernier souffle, le " + Roles.CHASSEUR.getDisplayName() + " §ctire sur §2" + p.getDisplayName());
				 new DeathManager(main).eliminate(p, true);
				
				playerlg.setHasUsedPower(true);
				player.getInventory().remove(Material.INK_SACK);
			 } else if (main.isDisplayState(DisplayState.CHOIX_FOSSOYEUR)) {
				 List<Player> votable = new ArrayList<>();
				 for (Player p : main.players)
					 if (!p.getUniqueId().equals(player.getUniqueId()))
						 votable.add(p);
				 
				 Player p = playerlg.getPlayerOnCursor(votable);
				 if (p == null) return;
				 PlayerLG plg = main.playerlg.get(p.getName());
				 
				 Player p2 = main.players.get(new Random().nextInt(main.players.size()));
				 if (plg.isCamp(main.playerlg.get(p2.getName()).getCamp())) {
					 while (plg.isCamp(main.playerlg.get(p2.getName()).getCamp())) {
						p2 = main.players.get(new Random().nextInt(main.players.size()));
				   	}
				 }
					
				 Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fAvant de mourir, le " + Roles.FOSSOYEUR.getDisplayName() + " §fcreuse les tombes de §8" + p.getDisplayName() + "§f et §8" + p2.getDisplayName() + ".§r\n§f§7Cela veut dire qu'ils ne sont pas dans le même camp.");
					
				 playerlg.setHasUsedPower(true);
				 player.getInventory().remove(Material.STONE_SPADE);
			 }
			 
		 }
	}
	
	
	@EventHandler
	public void onBookMaire(PlayerInteractEvent ev) {
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		Action action = ev.getAction();
		Block cblock = ev.getClickedBlock();
		
		if (current == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_BLOCK))
			if (cblock == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) {
			
			if (current.getType().equals(Material.BOOK)) {
				if (current.getItemMeta().getDisplayName().equals("§3Vote du maire")) {
					
					Inventory inv = Bukkit.createInventory(null, 27, "§6Inv §3Vote Maire");
					for (Player p : main.players) {
						PlayerLG plg = main.playerlg.get(p.getName());
						
						ItemStack it = new ItemStack(Material.SKULL_ITEM, plg.getVotes(), (short)3);
						SkullMeta itm = (SkullMeta) it.getItemMeta();
						itm.setOwner(p.getName());
						itm.setDisplayName("§3" + main.getPlayerNameByAttributes(p, player));
						itm.setLore(Arrays.asList("§7Vote pour le joueur §3" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7S'il obtient le plus de voix, il deviendra §bmaire §7du village.", "", "§bVotes : §3§l" + plg.getVotes(), "", "§b>>Clique pour voter"));
						it.setItemMeta(itm);
						
						inv.addItem(it);
					}
					inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
					
					player.openInventory(inv);
					
				}
			}
			
		}
	}
	
	@EventHandler
	public void onBookVoteMaireInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		if (inv.getName().equals("§6Inv §3Vote Maire")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG plg = main.playerlg.get(p.getName());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				int votes = 0;
				
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9" + player.getName() + " §3vote pour élire §b§l" + p.getName() + "§3 en tant que maire.");
				plg.addVote();

				if (playerlg.getVotedPlayer() != null) {
					Player vp = playerlg.getVotedPlayer();
					main.playerlg.get(vp.getName()).removeVote();
				}
				playerlg.setVote(p);
				player.closeInventory();
				
				try {
					for (Player pl : main.players) {
						if (pl.getOpenInventory().getTopInventory().getName().equals("§6Inv §3Vote Maire")) {
							Inventory plinv = pl.getOpenInventory().getTopInventory();
							
							for (ItemStack it : plinv.getContents()) {
								if (it != null) {
									if (it.getType().equals(Material.SKULL_ITEM)) {
										SkullMeta plitm = (SkullMeta) it.getItemMeta();
										
										if (plitm.getOwner().equals(itm.getOwner())) {
											it.setAmount(plg.getVotes());
										}
									}
								}
							}
							
						}
					}
				} catch (NullPointerException ignored) {}


				List<Player> ps = new ArrayList<>(main.players);
				for (Player psp : ps)
					votes = votes + main.playerlg.get(psp.getName()).getVotes();
				if (votes == ps.size()) {
					for (Player psp : ps) main.playerlg.get(psp.getName()).setHasUsedPower(true);
				}
				System.out.println(ps.size() + " " + votes);
				
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {

				
				if (main.playerlg.get(player.getName()).getVotedPlayer() != null) {
					Player vp = main.playerlg.get(player.getName()).getVotedPlayer();
					main.playerlg.get(vp.getName()).removeVote();
					
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§9" + player.getName() + " §3a annulé son vote.");
					
					try {
						for (Player pl : main.players) {
							if (pl.getOpenInventory().getTopInventory().getName().equals("§6Inv §3Vote Maire")) {
								Inventory plinv = pl.getOpenInventory().getTopInventory();
								
								for (ItemStack it : plinv.getContents()) {
									if (it != null) {
										if (it.getType().equals(Material.SKULL_ITEM)) {
											SkullMeta plitm = (SkullMeta) it.getItemMeta();
											
											if (plitm.getOwner().equals(vp.getName())) {
												it.setAmount(main.playerlg.get(vp.getName()).getVotes());
											}
										}
									}
								}
								
							}
						}
					} catch (NullPointerException ignored) {}
					main.playerlg.get(player.getName()).setVote(null);
				}
				
			
			}
			
			
			for (ItemStack it : inv.getContents()) {
				if (it == null) return;
				
				if (it.getType().equals(Material.SKULL)) {
					SkullMeta itm = (SkullMeta) it.getItemMeta();
					itm.setLore(Arrays.asList("§7Vote pour le joueur §3" + main.getPlayerNameByAttributes(Bukkit.getPlayer(itm.getOwner()), player) + "§7.", "§7S'il obtient le plus de voix, il deviendra §bmaire §7du village.", "", "§bVotes : §3§l" + main.playerlg.get(itm.getOwner()).getVotes(), "", "§b>>Clique pour voter"));
					it.setItemMeta(itm);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onBookVote(PlayerInteractEvent ev) {
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		Action action = ev.getAction();
		Block cblock = ev.getClickedBlock();
		
		if (current == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_BLOCK))
			if (cblock == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) {
			
			if (current.getType().equals(Material.BOOK)) {
				if (current.getItemMeta().getDisplayName().equals("§eVote") || current.getItemMeta().getDisplayName().equals("§4Coup d'état")) {
					
					Inventory inv = Bukkit.createInventory(null, 27, "§6Inv §eVote");
					for (Player p : main.players) {
						PlayerLG plg = main.playerlg.get(p.getName());
						
						ItemStack it = new ItemStack(Material.SKULL_ITEM, plg.getVotes(), (short)3);
						SkullMeta itm = (SkullMeta) it.getItemMeta();
						itm.setOwner(p.getName());
						itm.setDisplayName("§e" + main.getPlayerNameByAttributes(p, player));
						itm.setLore(Arrays.asList("§7Vote pour le joueur §e" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7S'il obtient le plus de voix, il sera §céliminé§7 de la partie.", "", "§bVotes : §3§l" + plg.getVotes(), "", "§b>>Clique pour voter"));
						it.setItemMeta(itm);
						
						inv.addItem(it);
					}
					inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
					
					player.openInventory(inv);
					
				} else if (current.getItemMeta().getDisplayName().equals("§eDeuxième Vote")) {
					
					Inventory inv = Bukkit.createInventory(null, 27, "§6Inv §eVote");
					for (Player p : main.players) {
						PlayerLG plg = main.playerlg.get(p.getName());
						
						if (!plg.isInEqual()) {
							ItemStack it = new ItemStack(Material.SKULL_ITEM, plg.getVotes(), (short)3);
							SkullMeta itm = (SkullMeta) it.getItemMeta();
							itm.setOwner(p.getName());
							itm.setDisplayName("§e" + main.getPlayerNameByAttributes(p, player));
							itm.setLore(Arrays.asList("§7Vote pour le joueur §e" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7S'il obtient le plus de voix, il sera §céliminé§7 de la partie.", "", "§bVotes : §3§l" + plg.getVotes(), "", "§b>>Clique pour voter"));
							it.setItemMeta(itm);
							
							inv.addItem(it);
						}
					}
					inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
					
					player.openInventory(inv);
					
				}
			}
			
		}
	}
	
	@EventHandler
	public void onBookVoteInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		PlayerLG playerlg = main.playerlg.get(player.getName());
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		if (inv.getName().equals("§6Inv §eVote")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				if (!playerlg.canVote()) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous ne pouvez pas voter.");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 8, 1);
					return;
				}
				
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG plg = main.playerlg.get(p.getName());
				int votes = 0;
				
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §evote pour §6§l" + p.getName());
				plg.addVote();

				if (playerlg.getVotedPlayer() != null) {
					Player vp = playerlg.getVotedPlayer();
					main.playerlg.get(vp.getName()).removeVote();
				}
				playerlg.setVote(p);

				try {
					for (Player pl : main.players) {
						if (pl.getOpenInventory().getTopInventory().getName().equals("§6Inv §eVote")) {
							Inventory plinv = pl.getOpenInventory().getTopInventory();
							
							for (ItemStack it : plinv.getContents())
								if (it != null)
									if (it.getType().equals(Material.SKULL_ITEM)) {
										SkullMeta plitm = (SkullMeta) it.getItemMeta();
										
										if (plitm.getOwner().equals(itm.getOwner()))
											it.setAmount(plg.getVotes());
									}
						}
					}
				} catch (NullPointerException ignored) {}
				
				
				List<Player> ps = new ArrayList<>();
				for (Player psp : main.players)
					if (main.playerlg.get(psp.getName()).canVote())
						ps.add(psp);
				for (Player psp : main.players)
					votes = votes + main.playerlg.get(psp.getName()).getVotes();
				if (votes == ps.size()) {
					for (Player psp : ps) main.playerlg.get(psp.getName()).setHasUsedPower(true);
				}
				System.out.println(ps.size() + " " + votes);
				
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				
				if (main.playerlg.get(player.getName()).getVotedPlayer() != null) {
					Player vp = playerlg.getVotedPlayer();
					main.playerlg.get(vp.getName()).removeVote();
					
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §ea annulé son vote.");
					
					try {
						for (Player pl : main.players) {
							if (pl.getOpenInventory().getTopInventory().getName().equals("§6Inv §eVote")) {
								Inventory plinv = pl.getOpenInventory().getTopInventory();
								
								for (ItemStack it : plinv.getContents()) {
									if (it != null) {
										if (it.getType().equals(Material.SKULL_ITEM)) {
											SkullMeta plitm = (SkullMeta) it.getItemMeta();
											
											if (plitm.getOwner().equals(vp.getName())) {
												it.setAmount(main.playerlg.get(vp.getName()).getVotes());
											}
										}
									}
								}
								
							}
						}
					} catch (NullPointerException ignored) {}
					main.playerlg.get(player.getName()).setVote(null);
				}
				
			}
			
			for (ItemStack it : inv.getContents()) {
				if (it == null) return;
				
				if (it.getType().equals(Material.SKULL)) {
					SkullMeta itm = (SkullMeta) it.getItemMeta();
					itm.setLore(Arrays.asList("§7Vote pour le joueur §3" + main.getPlayerNameByAttributes(Bukkit.getPlayer(itm.getOwner()), player) + "§7.", "§7S'il obtient le plus de voix, il deviendra §bmaire §7du village.", "", "§bVotes : §3§l" + main.playerlg.get(itm.getOwner()).getVotes(), "", "§b>>Clique pour voter"));
					it.setItemMeta(itm);
				}
			}
			
		}
	}
	
	
	@EventHandler
	public void onBookMaireChoose(PlayerInteractEvent ev) {
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		Action action = ev.getAction();
		Block cblock = ev.getClickedBlock();
		
		if (current == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_BLOCK))
			if (cblock == null) return;
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) {
			
			if (current.getType().equals(Material.BOOK)) {
				if (current.getItemMeta().getDisplayName().equals("§bDépartager les joueurs")) {
					Inventory inv = Bukkit.createInventory(null, 27, "§6Inv §3Choix Maire");
					
					for (Player p : main.players) {
						if (!main.playerlg.get(p.getName()).isInEqual()) {
							ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
							SkullMeta itm = (SkullMeta) it.getItemMeta();
							itm.setOwner(p.getName());
							itm.setDisplayName("§3" + main.getPlayerNameByAttributes(p, player));
							itm.setLore(Arrays.asList("§7Chosis le joueur §e" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7Si vous le choisissez, il sera §céliminé§7 de la partie.", "", "§b>>Clique pour voter"));
							it.setItemMeta(itm);
							
							inv.addItem(it);
						}
							
					}
					
					player.openInventory(inv);
					
				}
			}
			
		}
	}
	
	@EventHandler
	public void onBookMaireChooseInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.JOUR)) return;
		
		if (inv.getName().equals("§6Inv §3Choix Maire")) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
				Player maire = null;
				for (Player p2 : main.players)
					if (main.playerlg.get(p2.getName()).isMaire()) maire = p2;
				
				
				main.playerlg.get(p.getName()).setDayTargeted(true);
				main.playerlg.get(maire.getName()).setHasUsedPower(true);
				
				player.closeInventory();
				player.getInventory().remove(current);
			}
		}
	}
	
	
	
	@EventHandler
	public void onInteractFusil(PlayerInteractEvent ev) {
		if (!main.isDisplayState(DisplayState.TIR_CHASSEUR)) return;
		
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		
		if (current == null) return;
		
		if (current.getType().equals(Material.INK_SACK)) {
			if (current.getItemMeta().getDisplayName().equals("§2Fusil")) {
				Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.CHASSEUR.getDisplayName());
				inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
				
				for (Player p : main.players) {
					if (!p.getUniqueId().equals(player.getUniqueId())) {
						ItemStack skull = main.config.getColoredItem(Material.SKULL_ITEM, 1, (short) 3, "§a" + main.getPlayerNameByAttributes(p, player), Arrays.asList("§7Tire sur §a" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7Il sera éliminé de la partie.", "", "§b>>Clique pour sélectionner"));
						SkullMeta itm = (SkullMeta) skull.getItemMeta();
						itm.setOwner(p.getName());
						skull.setItemMeta(itm);
						
						inv.addItem(skull);
					}
				}
				
				player.openInventory(inv);
			}
		}
	}
	
	@EventHandler
	public void onFusilInv(InventoryClickEvent ev) {
		if (!main.isDisplayState(DisplayState.TIR_CHASSEUR)) return;
		
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (inv.getName().equals("§6Inv " + Roles.CHASSEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
				
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cDans un dernier souffle, le " + Roles.CHASSEUR.getDisplayName() + " §ctire sur §2" + p.getDisplayName());
				new DeathManager(main).eliminate(p, true);
				
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.getInventory().remove(Material.INK_SACK);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§2Le " + Roles.CHASSEUR.getDisplayName() + " §2n'a tiré sur personne.");
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.getInventory().remove(Material.INK_SACK);
				player.closeInventory();
			}
			
		}
	}
	
	@EventHandler
	public void onInteractPelle(PlayerInteractEvent ev) {
		if (!main.isDisplayState(DisplayState.CHOIX_FOSSOYEUR)) return;
		
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		
		if (current == null) return;
		
		if (current.getType().equals(Material.STONE_SPADE)) {
			if (current.getItemMeta().getDisplayName().equals("§7Pelle")) {
				Inventory inv = Bukkit.createInventory(null, 27, "§6Inv " + Roles.FOSSOYEUR.getDisplayName());
				inv.setItem(26, main.getItem(Material.BARRIER, "§c§lAnnuler", Collections.singletonList("§7N'effectue pas l'action en cours.")));
				
				for (Player p : main.players) {
					if (!p.getUniqueId().equals(player.getUniqueId())) {
						ItemStack skull = main.config.getColoredItem(Material.SKULL_ITEM, 1, (short) 3, "§a" + main.getPlayerNameByAttributes(p, player), Arrays.asList("§7Creuse la tombe de §a" + main.getPlayerNameByAttributes(p, player) + "§7.", "§7Le jeu sélectionnera un autre joueur d'un camp opposé et creusera sa tombe.", "", "§b>>Clique pour sélectionner"));
						SkullMeta itm = (SkullMeta) skull.getItemMeta();
						itm.setOwner(p.getName());
						skull.setItemMeta(itm);
						
						inv.addItem(skull);
					}
				}
				
				player.openInventory(inv);
			}
		}
	}
	
	@EventHandler
	public void onPelleInv(InventoryClickEvent ev) {
		if (!main.isDisplayState(DisplayState.CHOIX_FOSSOYEUR)) return;
		
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (inv.getName().equals("§6Inv " + Roles.FOSSOYEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
				Player p2 = main.players.get(new Random().nextInt(main.players.size()));
				if (main.playerlg.get(p.getName()).isCamp(main.playerlg.get(p2.getName()).getCamp())) {
					while (main.playerlg.get(p.getName()).isCamp(main.playerlg.get(p2.getName()).getCamp())) {
						p2 = main.players.get(new Random().nextInt(main.players.size()));
					}
				}
				
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fAvant de mourir, le " + Roles.FOSSOYEUR.getDisplayName() + " §fcreuse les tombes de §8" + p.getDisplayName() + "§f et §8" + p2.getDisplayName() + ".§r\n§f§7Cela veut dire qu'ils ne sont pas dans le même camp.");
				
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.getInventory().remove(Material.STONE_SPADE);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fLe " + Roles.FOSSOYEUR.getDisplayName() + " §fn'a pas creusé de tombe.");
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.getInventory().remove(Material.STONE_SPADE);
				player.closeInventory();
			}
			
		}
	}
	
	@EventHandler
	public void onSDInv(InventoryClickEvent ev) {
		if (!main.isDisplayState(DisplayState.CHOIX_SERVANTE)) return;
		
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (inv.getName().equals("§6Inv " + Roles.SERVANTE_DÉVOUÉE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.STAINED_CLAY)) {
				PlayerLG playerlg = main.playerlg.get(player.getName());
				Player targeted = null;
				for (Player p : main.players) if (main.playerlg.get(p.getName()).isDayTargeted()) targeted = p;
				PlayerLG tlg = main.playerlg.get(targeted.getName());
				
				if (current.getDurability() == 5) {
					
					playerlg.setRole(tlg.getRole());
					playerlg.setCamp(playerlg.getRole().getCamp());
					if (playerlg.isCamp(RCamp.VOLEUR)) playerlg.setCamp(RCamp.VILLAGE);
					if (playerlg.isCamp(RCamp.ANGE) && main.days != 1) playerlg.setCamp(RCamp.VILLAGE);
					if (playerlg.isCamp(RCamp.MERCENAIRE) && main.days != 1) playerlg.setCamp(RCamp.VILLAGE);
					if (playerlg.isCamp(RCamp.CHIEN_LOUP)) playerlg.setCamp(tlg.getCamp());
					if (playerlg.isInfected()) playerlg.setCamp(RCamp.LOUP_GAROU);
					if (playerlg.isMaire()) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "§3Vous avez changé d'apparence, vous n'êtes donc plus §b§lmaire§3.");
						playerlg.setMaire(false);
						player.setDisplayName(player.getName());
						player.setPlayerListName(player.getName());
					}
					if (playerlg.isCharmed()) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "§5Vous avez changé d'apparence, vous n'êtes donc plus §d§lcharmé§5.");
						playerlg.setCharmed(false);
					}
					player.sendMessage(main.getPrefix() + main.SendArrow + "§dVous avez bien subtilisé le rôle \"" + tlg.getRole().getDisplayName() + "\" §dde §5" + targeted.getDisplayName() + "§d.");
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 9, 2f);
					tlg.setRole(Roles.SERVANTE_DÉVOUÉE);
					tlg.setServante(false);
					player.getInventory().setItem(19, main.getItem(Material.STONE_BUTTON, "", null));
					playerlg.setHasUsedPower(true);

				} else player.getInventory().addItem(current);
				player.closeInventory();
			}
		}
	}
	
	
}
