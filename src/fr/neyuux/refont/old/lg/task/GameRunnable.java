package fr.neyuux.refont.old.lg.task;

import fr.neyuux.refont.old.lg.*;
import fr.neyuux.refont.old.lg.role.RCamp;
import fr.neyuux.refont.old.lg.role.Roles;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class GameRunnable extends BukkitRunnable {

	private final LG main;
	private int LectureDesRolesTimer = 7;
	private int AnnoncesDesMortsTimer = Integer.MAX_VALUE;
	private int TirTimer = Integer.MAX_VALUE;
	private int FossoTimer = Integer.MAX_VALUE;
	private List<List<Entry<Integer, String>>> scoreboards = new ArrayList<>();
	private int displayedScoreboard = 0;
	private int ScoreboardTimer = 0;
	
	public GameRunnable(LG main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		
		if (!main.isState(Gstate.PLAYING)) {
			cancel();
			return;
		}
		
		if (main.GRunnable == null) main.GRunnable = this;

		if (main.isDisplayState(DisplayState.DISTRIBUTION_DES_ROLES)) {
			int numberOfPlayerWithRole = 0;
			for (Player player : main.players) if (main.playerlg.get(player.getName()).getRole() != null) numberOfPlayerWithRole++;
			
			if (numberOfPlayerWithRole == main.players.size()) {
				main.setDisplayState(DisplayState.LECTURE_DES_ROLES);
				this.createScoreboardList();
			}
		}
		
		
		if (main.isDisplayState(DisplayState.LECTURE_DES_ROLES)) {
			
			if (LectureDesRolesTimer == 6) {
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					
					if (playerlg.getRole().getCamp().equals(RCamp.LOUP_GAROU) || playerlg.getRole().getCamp().equals(RCamp.LOUP_GAROU_BLANC)) {
						player.getScoreboard().getTeam("LG").addEntry(player.getName());
						for (Player p : main.players) {
							PlayerLG plg = main.playerlg.get(p.getName());
							
							if (!playerlg.isRole(Roles.ENCHANTEUR))
								if (plg.getRole().getCamp().equals(RCamp.LOUP_GAROU) || plg.getRole().getCamp().equals(RCamp.LOUP_GAROU_BLANC))
									player.getScoreboard().getTeam("LG").addEntry(p.getName());
						}
					}
					
					if (playerlg.getRole().equals(Roles.SOEUR)) {
						Team t = null;
						for (Team team : player.getScoreboard().getTeams()) if (team.getName().startsWith("RSoeur")) t = team;
						t.addEntry(player.getName());
						for (Player p : main.players) {
							
							if (playerlg.getsoeur().contains(p) || main.playerlg.get(p.getName()).getsoeur().contains(player)) {
								t.addEntry(p.getName());
							}
						}
					}
					
					if (playerlg.getRole().equals(Roles.FRÈRE)) {
						Team t = null;
						for (Team team : player.getScoreboard().getTeams()) if (team.getName().startsWith("RFrère")) t = team;
						t.addEntry(player.getName());
						for (Player p : main.players) {
							
							if (playerlg.getfrère().contains(p) || main.playerlg.get(p.getName()).getfrère().contains(player)) {
								t.addEntry(p.getName());
							}
						}
					}
				}
				for (Player p : main.getPlayersByRole(Roles.MAÇON)) {
					main.setMaçonScoreboard(p);
					Team t = null;
					for (Team team : p.getScoreboard().getTeams()) if (team.getName().startsWith("RSoeur")) t = team;
					StringBuilder sma = new StringBuilder("§r\n");
					for (Player ps : main.getPlayersByRole(Roles.MAÇON)) {
						sma.append("§6§l").append(ps.getName()).append("§r\n");
						t.addEntry(ps.getName());
					}
					p.sendMessage(main.getPrefix() + main.SendArrow + "§6Liste des maçons : " + sma);
				}
				
			} else if (LectureDesRolesTimer == 0) {
				
				main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
				LectureDesRolesTimer = 7;
				return;
			}
			
			LectureDesRolesTimer--;
			for (Player player : Bukkit.getOnlinePlayers()) player.setLevel(LectureDesRolesTimer);
		}
		
		
		
		if (!main.isDisplayState(DisplayState.LECTURE_DES_ROLES) && !main.isDisplayState(DisplayState.DISTRIBUTION_DES_ROLES)) {
			if (ScoreboardTimer == 0) {
					String objectiveName = "§6§lRôles";
					if (scoreboards.get(displayedScoreboard).get(scoreboards.get(displayedScoreboard).size() - 2).getValue().startsWith("§d")) objectiveName = "§dPouvoirs du " + Roles.COMÉDIEN.getDisplayName();
					
					for (Player p : Bukkit.getOnlinePlayers()) {
						SimpleScoreboard ss = new SimpleScoreboard(objectiveName, p);
						for (Entry<Integer, String> en : scoreboards.get(displayedScoreboard))
							ss.add(en.getValue(), en.getKey());
						p.setScoreboard(ss.getScoreboard());
				}
				displayedScoreboard++;
				if (scoreboards.size() <= displayedScoreboard) displayedScoreboard = 0;
				ScoreboardTimer = 7;
			} else ScoreboardTimer--;
		}
		
		
		
		if (main.isDisplayState(DisplayState.TOMBEE_DE_LA_NUIT)) {
			
			if (main.isType(Gtype.LIBRE))
				for (Player player : main.players) {
					int bedI = new Random().nextInt(main.BedList.size());
					if (main.nights == 0)main.playerlg.get(player.getName()).setBlock(main.BedList.get(bedI));
					main.BedList.remove(bedI);
				}
				
			else {
				int blockI = 0;
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					if (main.nights == 0) {
						playerlg.setBlock(new ArrayList<>(main.BlockList.keySet()).get(blockI));
						main.UsedBlockList.put(new ArrayList<>(main.BlockList.keySet()).get(blockI), main.BlockList.get(new ArrayList<>(main.BlockList.keySet()).get(blockI)));
					}
					player.teleport(new Location(Bukkit.getWorld("LG"), playerlg.getBlock().getX() + 0.5, 16.01564, playerlg.getBlock().getZ() + 0.5));
					blockI++;
				}
			}
			
			main.setCycle(Gcycle.NUIT);
			new DeathManager(main).checkWin();
			new NightRunnable(main).runTaskTimer(main, 0, 20);
			
		}
		
		
		
		if (main.isDisplayState(DisplayState.ANNONCES_DES_MORTS_NUIT)) {
			if (AnnoncesDesMortsTimer == Integer.MAX_VALUE) AnnoncesDesMortsTimer = 3;
			
			if (AnnoncesDesMortsTimer == 3) {
				if (main.cycleJourNuit) Bukkit.getWorld("LG").setTime(23000);
				
				for (Player p : main.players) {
					PlayerLG plg = main.playerlg.get(p.getName());
					Roles r = plg.getRole();
					if (plg.isNoctaTargeted())
						plg.setNoctaTargeted(false);
					
					if (plg.isComédien() && plg.isVivant()) {
						
						if (plg.isRole(Roles.MONTREUR_D$OURS)) {
							Player pu = plg.get2NearestPlayers().get(1);
							Player pa = plg.get2NearestPlayers().get(0);
							
							if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(pu.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(pu.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(pa.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(pa.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) {
								for (Player player : Bukkit.getOnlinePlayers())
									player.playSound(player.getLocation(), Sound.WOLF_GROWL, 10, 1);
								Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6Grrrrrr");
								p.sendMessage(main.getPrefix() + main.SendArrow + "§6Votre Ours a détecté un " + Roles.LOUP_GAROU.getDisplayName() + " §6parmis vous, ou vos deux voisins ! §oPseudos : §e" + pu.getName() + "§6§o et §e" + pa.getName() + "§6§o.");
							}
						}
						
						if (main.AliveRoles.get(r) == 1)
							main.AliveRoles.remove(r);
						else main.AliveRoles.put(r, main.AliveRoles.get(r) - 1);
						
						if (main.AliveRoles.containsKey(Roles.COMÉDIEN))
							main.AliveRoles.put(Roles.COMÉDIEN, main.AliveRoles.get(Roles.COMÉDIEN) + 1);
						else main.AliveRoles.put(Roles.COMÉDIEN, 1);
						this.createScoreboardList();
						
						plg.setRole(Roles.COMÉDIEN);
						plg.setComédien(false);
					}
				}
				
				
				DeathManager dm = new DeathManager(main);
				Player lgtarget = null;
				List<Player> sosotargets = new ArrayList<>();
				List<Player> lgbtargets = new ArrayList<>();
				List<Player> gmltargets = new ArrayList<>();
				List<Player> diedfdjs = new ArrayList<>();
				List<Player> diedpf2 = new ArrayList<>();
				List<Player> pretretargets = new ArrayList<>();
				List<Player> pyrotargets = new ArrayList<>();
				List<Player> celtargets = new ArrayList<>();
				List<Player> diedDACs = new ArrayList<>();
				List<Player> rézPlayers = new ArrayList<>();
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					
					if (playerlg.isGMLTargeted()) gmltargets.add(player);
					if (playerlg.isLGBTargeted()) lgbtargets.add(player);
					if (playerlg.isSosoTargeted()) sosotargets.add(player);
					if (playerlg.isLGTargeted()) lgtarget = player;
					if (playerlg.isFDJDied()) diedfdjs.add(player);
					if (playerlg.isPF2Died()) diedpf2.add(player);
					if (playerlg.getPretreThrower() != null) pretretargets.add(player);
					if (playerlg.isPyroTargeted()) pyrotargets.add(player);
					if (lgtarget != null)
						if (main.playerlg.get(lgtarget.getName()).isRole(Roles.CHEVALIER_À_L$ÉPÉE_ROUILLÉE)) {
							Player targeted = playerlg.get2NearestPlayers().get(0);
							String place = "en dessous";
							if (main.isType(Gtype.RÉUNION)) place = "à gauche";
							
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fLe premier " + Roles.LOUP_GAROU.getDisplayName() + " §f"+place+" de §6" + lgtarget.getName() + " §fest empoisonné par §7l'épée §fdu " + Roles.CHEVALIER_À_L$ÉPÉE_ROUILLÉE.getDisplayName() + "§f.");
							Bukkit.broadcastMessage("");
							main.playerlg.get(targeted.getName()).setCELInfected();
							targeted.sendMessage(main.getPrefix() + main.SendArrow + "§cLe " + Roles.CHEVALIER_À_L$ÉPÉE_ROUILLÉE.getDisplayName() + " §cvous a empoisonné ! Vous mourrez au prochain matin...");
						}
					
					playerlg.removeDayCELInfected();
					if (playerlg.getDayBeforeDieOfCEL() == 0) celtargets.add(player);
					
					playerlg.removeDayDACDying();
					if (playerlg.getDayBeforeDieOfDAC() == 0) diedDACs.add(player);
					
					
					if (gmltargets.contains(player) || lgbtargets.contains(player) || lgtarget == player || sosotargets.contains(player) || diedfdjs.contains(player) || pretretargets.contains(player))
						if (playerlg.hasProtector()) {
							Player protector = playerlg.getProtector();
							if (gmltargets.contains(player)) {
								gmltargets.remove(player);
								gmltargets.add(protector);
							} else if (lgbtargets.contains(player)) {
								lgbtargets.remove(player);
								lgbtargets.add(protector);
							} else if (sosotargets.contains(player)) {
								sosotargets.remove(player);
								sosotargets.add(protector);
							} else if (lgtarget == player) 
								lgtarget = protector;
							else if (diedfdjs.contains(player)) {
								diedfdjs.remove(player);
								diedfdjs.add(protector);
							} else if (pretretargets.contains(player)) {
								pretretargets.remove(player);
								pretretargets.add(protector);
							}
						}
				}
				
				for (PlayerLG playerlg : main.playerlg.values())
					if (playerlg.isNécroTargeted()) rézPlayers.add(playerlg.player);
				
				
				if (lgtarget != null)
					if (!main.playerlg.get(lgtarget.getName()).hasSalvation())
						dm.eliminate(lgtarget, false);
					else {
						main.playerlg.get(lgtarget.getName()).setSalvation(false);
						main.playerlg.get(lgtarget.getName()).setLGTargeted(false);
					}
				
				for (Player p : pretretargets) {
					PlayerLG plg = main.playerlg.get(p.getName());
					if (!plg.isCamp(RCamp.LOUP_GAROU) && !plg.isCamp(RCamp.LOUP_GAROU_BLANC)) {
						pretretargets.remove(p);
						pretretargets.add(plg.getPretreThrower());
					}
				}
				
				if (!sosotargets.isEmpty())
					for (Player p : sosotargets)
						dm.eliminate(p, false);
				if (!lgbtargets.isEmpty())
					for (Player p : lgbtargets)
						dm.eliminate(p, false);
				if (!gmltargets.isEmpty())
					for (Player p : gmltargets)
						dm.eliminate(p, false);
				if (!diedfdjs.isEmpty())
					for (Player p : diedfdjs)
						dm.eliminate(p, false);
				if (!celtargets.isEmpty())
					for (Player ct : celtargets)
						dm.eliminate(ct, false);
				if (!diedDACs.isEmpty())
					for (Player dt : diedDACs)
						dm.eliminate(dt, false);
				if (!diedpf2.isEmpty())
					for (Player p : diedpf2)
						dm.eliminate(p, false);
				if (!pretretargets.isEmpty())
					for (Player p : pretretargets)
						dm.eliminate(p, false);
				if (!pyrotargets.isEmpty())
					for (Player p : pyrotargets)
						dm.eliminate(p, false);
				if (!rézPlayers.isEmpty())
					for (Player p : rézPlayers) {
						if (p == null) continue;
						if (!Bukkit.getOnlinePlayers().contains(p)) continue;
						PlayerLG plg = main.playerlg.get(p.getName());
						main.players.add(p);
						main.spectators.remove(p);
						p.setGameMode(GameMode.ADVENTURE);
						p.setDisplayName(p.getName());
						p.setPlayerListName(p.getName());
						plg.setInfected(false);
						plg.setCharmed(false);
						plg.getCouple().clear();
						p.teleport(new Location(p.getWorld(), plg.getBlock().getX()+0.5, plg.getBlock().getY() + 1, plg.getBlock().getZ()+0.5));
						Roles r = plg.getRole();
						if (plg.isCamp(RCamp.VILLAGE))
							plg.setRole(Roles.SIMPLE_VILLAGEOIS);
						if (!r.getCamp().equals(RCamp.VILLAGE)) {
							if (r.getCamp().equals(RCamp.LOUP_GAROU_BLANC) || r.getCamp().equals(RCamp.PYROMANE) || r.getCamp().equals(RCamp.JOUEUR_DE_FLÛTE))
								plg.setCamp(r.getCamp());
							else if (r.getCamp().equals(RCamp.MERCENAIRE) || r.getCamp().equals(RCamp.ANGE) || r.getCamp().equals(RCamp.CHIEN_LOUP) || r.getCamp().equals(RCamp.VOLEUR)) {
								plg.setCamp(RCamp.VILLAGE);
								plg.setRole(Roles.SIMPLE_VILLAGEOIS);
							}
						} else {
							plg.setCamp(RCamp.VILLAGE);
							plg.setRole(Roles.SIMPLE_VILLAGEOIS);
						}
						try {
							//p.getInventory().setItem(4, main.getRoleMap(plg.getRole()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (main.AliveRoles.containsKey(r)) {
							main.AliveRoles.put(r, main.AliveRoles.get(r) + 1);
						} else main.AliveRoles.put(r, 1);
						main.GRunnable.createScoreboardList();
						plg.setVivant(true);
						plg.setNécroTargeted(false);
						Bukkit.getWorld("LG").strikeLightningEffect(p.getLocation());
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§1§l" + p.getName() + " §ba été réssucité par le " + Roles.NÉCROMANCIEN.getDisplayName() + " §b!");
					}
				
				for (Player mo : main.getPlayersByRole(Roles.MONTREUR_D$OURS)) {
					PlayerLG molg = main.playerlg.get(mo.getName());
					
					Player pu = molg.get2NearestPlayers().get(1);
					Player pa = molg.get2NearestPlayers().get(0);
					
					if (main.playerlg.get(mo.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(mo.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(pu.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(pu.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(pa.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(pa.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) {
						for (Player player : Bukkit.getOnlinePlayers())
							player.playSound(player.getLocation(), Sound.WOLF_GROWL, 10, 1);
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6Grrrrrr");
						mo.sendMessage(main.getPrefix() + main.SendArrow + "§6Votre Ours a détecté un " + Roles.LOUP_GAROU.getDisplayName() + " §6parmis vous, ou vos deux voisins ! §oPseudos : §e" + pu.getName() + "§6§o et §e" + pa.getName() + "§6§o.");
					}
				}
				
				
				for (Player p : main.players) {
					PlayerLG plg = main.playerlg.get(p.getName());
					
					if (plg.isCharmed() || plg.isRole(Roles.JOUEUR_DE_FLÛTE)) {
						StringBuilder scharmes = new StringBuilder();
						List<Player> charmeds = new ArrayList<>();
						for (Player p2 : main.players) if (main.playerlg.get(p2.getName()).isCharmed()) charmeds.add(p2);
						for (Player p2 : charmeds) {
							if (scharmes.toString().equals("")) {
								scharmes = new StringBuilder("§5" + p2.getName());
							} else scharmes.append("§d, §5").append(p2.getName());
						}
						p.sendMessage(main.getPrefix() + main.SendArrow + "§dListe des Charmés : " + scharmes);
						
					}
					
					if (plg.isRole(Roles.MERCENAIRE) && main.days == 1) {
						int rdm = new Random().nextInt(main.players.size());
						Player pm = main.players.get(rdm);
						PlayerLG pmlg = main.playerlg.get(pm.getName());
						if (pm.getUniqueId().equals(p.getUniqueId()) || !pmlg.getTargetOf().isEmpty())
							while (pm.getUniqueId().equals(p.getUniqueId()) || !pmlg.getTargetOf().isEmpty()) {
								rdm = new Random().nextInt(main.players.size());
								pm = main.players.get(rdm);
								pmlg = main.playerlg.get(pm.getName());
							}
						
						pmlg.getTargetOf().add(p);
						p.sendMessage(main.getPrefix() + main.SendArrow + "§cVotre cible pour ce tour est : §5§l" + main.getPlayerNameByAttributes(pm, p) + "§c.");
						LG.sendTitle(p, "§cVotre cible est §5§l" + pm.getDisplayName(), "§4Tuez le pour gagner la partie !", 15, 50, 15);
					}
					
					if (plg.isRole(Roles.FILLE_DE_JOIE))
						plg.setOtherPlayerHouse(p);
					
				}
			
			}
			
			if (AnnoncesDesMortsTimer == 0) {
				AnnoncesDesMortsTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.VOTE);
				Player maire = null;
				for (Player p : main.players)
					if (main.playerlg.get(p.getName()).isMaire())
						maire = p;
				if (main.days == 1 && main.maire && maire == null) main.setDisplayState(DisplayState.ELECTIONS_DU_MAIRE);
				new DeathManager(main).checkWin();
				Bukkit.broadcastMessage("      §e§lJOUR " + main.days);
				for(Player player : Bukkit.getOnlinePlayers())
					player.playSound(player.getLocation(), Sound.CHICKEN_IDLE, 8, 1.2f);
				for (Player player : main.players) {
					for (Player p : main.players)
						player.showPlayer(p);
					if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
							player.removePotionEffect(PotionEffectType.BLINDNESS);
					
					main.playerlg.get(player.getName()).setHasUsedPower(false);
					main.playerlg.get(player.getName()).resetVotes();
					main.playerlg.get(player.getName()).showArmorStandForAll();
					main.playerlg.get(player.getName()).setSalvation(false);
				}
				new DayRunnable(main).runTaskTimer(main, 0, 20);
			}
			
			
			if (AnnoncesDesMortsTimer != Integer.MAX_VALUE) AnnoncesDesMortsTimer--;
		}
		
		
		if (main.isDisplayState(DisplayState.TIR_CHASSEUR)) {
			Player c = null;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (main.playerlg.containsKey(player.getName())) {
					if (main.playerlg.get(player.getName()).isRole(Roles.CHASSEUR)) {
						if (!main.playerlg.get(player.getName()).isVivant()) {
							if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
								c = player;
							}
						}
					}
				}
			}
			
			if (TirTimer == Integer.MAX_VALUE) TirTimer = 15;
			
			if (main.playerlg.get(c.getName()).hasUsedPower()) TirTimer = 0;
			
			LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.CHASSEUR.getDisplayName());
			
			if (TirTimer == 0) {
				c.closeInventory();
				if (!main.playerlg.get(c.getName()).hasUsedPower()) {
					c.sendMessage(main.getPrefix() + main.SendArrow + "§aVous avez mit trop de temps à choisir.");
					main.playerlg.get(c.getName()).setHasUsedPower(true);
				}
				c.setGameMode(GameMode.SPECTATOR);
				c.sendMessage(main.getPrefix() + main.SendArrow + "§7Votre mode de jeu à été établi en §lSpectateur§7.");
				c.setDisplayName("§8[§7" + main.playerlg.get(c.getName()).getRole().getName() + "§8] §b" + c.getName());
				c.setPlayerListName(c.getDisplayName());
				c.getInventory().clear();
				Bukkit.getWorld("LG").strikeLightningEffect(c.getLocation());
				for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.FIREWORK_BLAST2, 10, 1);
				for (PotionEffect pe : c.getActivePotionEffects()) c.removePotionEffect(pe.getType());
				
				new DeathManager(main).checkWin();
				
				TirTimer = Integer.MAX_VALUE;
				AnnoncesDesMortsTimer = Integer.MAX_VALUE;
				if (main.sleepingPlayers.contains(main.players.get(0))) {
					main.setDisplayState(DisplayState.VOTE);
					if (main.days == 1 && main.maire) main.setDisplayState(DisplayState.ELECTIONS_DU_MAIRE);
					Bukkit.broadcastMessage("      §e§lJOUR " + main.days);
					for (Player player : Bukkit.getOnlinePlayers()) {
						for (Player p : Bukkit.getOnlinePlayers())
							player.showPlayer(p);
						if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
								player.removePotionEffect(PotionEffectType.BLINDNESS);
						
						main.playerlg.get(player.getName()).setHasUsedPower(false);
						main.playerlg.get(player.getName()).resetVotes();
					}
					new DeathManager(main).checkWin();
					new DayRunnable(main).runTaskTimer(main, 0, 20);
				} else {
					if (main.days == 1) {
						for (Player p : main.players) {
							if (main.playerlg.get(p.getName()).isRole(Roles.ANGE))
								main.playerlg.get(p.getName()).setCamp(RCamp.VILLAGE);
						}
					}
					
					main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
				}
			}
			
			if (TirTimer != Integer.MAX_VALUE) TirTimer--;
			for (Player player : Bukkit.getOnlinePlayers()) if (TirTimer != Integer.MAX_VALUE) player.setLevel(TirTimer);
		}
		
		if (main.isDisplayState(DisplayState.CHOIX_FOSSOYEUR)) {
			Player f = null;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (main.playerlg.containsKey(player.getName())) {
					if (main.playerlg.get(player.getName()).isRole(Roles.FOSSOYEUR)) {
						if (!main.playerlg.get(player.getName()).isVivant()) {
							if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
								f = player;
							}
						}
					}
				}
			}
			
			if (FossoTimer == Integer.MAX_VALUE) FossoTimer = 15;
			
			if (main.playerlg.get(f.getName()).hasUsedPower()) FossoTimer = 0;
			
			LG.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour du " + Roles.FOSSOYEUR.getDisplayName());
			
			if (FossoTimer == 0) {
				f.closeInventory();
				if (!main.playerlg.get(f.getName()).hasUsedPower()) {
					f.sendMessage(main.getPrefix() + main.SendArrow + "§aVous avez mit trop de temps à choisir.");
					main.playerlg.get(f.getName()).setHasUsedPower(true);
				}
				f.setGameMode(GameMode.SPECTATOR);
				f.sendMessage(main.getPrefix() + main.SendArrow + "§7Votre mode de jeu à été établi en §lSpectateur§7.");
				f.setDisplayName("§8[§7" + main.playerlg.get(f.getName()).getRole().getName() + "§8] §b" + f.getName());
				f.setPlayerListName(f.getDisplayName());
				f.getInventory().clear();
				Bukkit.getWorld("LG").strikeLightningEffect(f.getLocation());
				for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.FIREWORK_BLAST2, 10, 1);
				for (PotionEffect pe : f.getActivePotionEffects()) f.removePotionEffect(pe.getType());
				
				FossoTimer = Integer.MAX_VALUE;
				AnnoncesDesMortsTimer = Integer.MAX_VALUE;
				if (main.sleepingPlayers.contains(main.players.get(0))) {
					main.setDisplayState(DisplayState.VOTE);
					if (main.days == 1 && main.maire) main.setDisplayState(DisplayState.ELECTIONS_DU_MAIRE);
					Bukkit.broadcastMessage("      §e§lJOUR " + main.days);
					for (Player player : Bukkit.getOnlinePlayers()) {
						for (Player p : Bukkit.getOnlinePlayers())
							player.showPlayer(p);
						if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
								player.removePotionEffect(PotionEffectType.BLINDNESS);
						
						main.playerlg.get(player.getName()).setHasUsedPower(false);
						main.playerlg.get(player.getName()).resetVotes();
					}
					new DeathManager(main).checkWin();
					new DayRunnable(main).runTaskTimer(main, 0, 20);
				} else {
					if (main.days == 1) {
						for (Player p : main.players) {
							if (main.playerlg.get(p.getName()).isRole(Roles.ANGE))
								main.playerlg.get(p.getName()).setCamp(RCamp.VILLAGE);
						}
					}
					
					main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
				}
			}
			
			if (FossoTimer != Integer.MAX_VALUE) FossoTimer--;
			for (Player player : Bukkit.getOnlinePlayers()) if (FossoTimer != Integer.MAX_VALUE) player.setLevel(FossoTimer);
		}
		
	}
	
	
	public void createScoreboardList() {
		List<List<Entry<Integer, String>>> list = new ArrayList<>();
		boolean thereComédien = false;
		if (main.AliveRoles.containsKey(Roles.COMÉDIEN))
			thereComédien = true;
		
		List<Entry<Integer, String>> s = new ArrayList<>();
		int line = 15;
		
		for (Entry<Roles, Integer> en : main.AliveRoles.entrySet()) {
			SimpleEntry<Integer, String> se = new SimpleEntry<>(line, en.getKey().getDisplayName() + " §f» §e" + en.getValue());
			s.add(se);
			line--;
		}
		SimpleEntry<Integer, String> fin1 = new SimpleEntry<>(1, "§8------------");
		SimpleEntry<Integer, String> fin2 = new SimpleEntry<>(0, "§e§oMap by §c§l§oNeyuux_");
		s.add(fin1);
		s.add(fin2);
		
		list.add(s);
		
		if (main.AliveRoles.size() >= 13) {
			List<Entry<Integer, String>> s2 = new ArrayList<>();
			line = 15;
			
			for (Entry<Roles, Integer> en : main.AliveRoles.entrySet()) {
				SimpleEntry<Integer, String> se = new SimpleEntry<>(line, en.getKey().getDisplayName() + " §f» §e" + en.getValue());
				if (!s.contains(se)) s2.add(se);
				line--;
			}
			s2.add(fin1);
			s2.add(fin2);
			
			list.add(s2);
		}
			
		if (thereComédien) {
			List<Entry<Integer, String>> sc = new ArrayList<>();
			line = 15;
			
			for (Roles r : main.pouvoirsComédien) {
				SimpleEntry<Integer, String> se = new SimpleEntry<>(line, r.getDisplayName());
				sc.add(se);
				line--;
			}
			sc.add(fin1);
			sc.add(fin2);
			
			list.add(sc);
		}
		
		this.scoreboards = list;
	}
	

}
