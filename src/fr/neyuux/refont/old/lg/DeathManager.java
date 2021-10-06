package fr.neyuux.refont.old.lg;

import fr.neyuux.refont.old.lg.role.RCamp;
import fr.neyuux.refont.old.lg.role.Roles;
import fr.neyuux.refont.old.lg.task.LGAutoStop;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Map.Entry;

public class DeathManager {
	
	private final LG main;
	
	public DeathManager(LG main) {
		this.main = main;
	}
	


	public void eliminate(Player player, Boolean canCheckWin) {
		PlayerLG playerlg = main.playerlg.get(player.getName());
		if (playerlg.isComédien()) playerlg.setRole(Roles.COMÉDIEN);
		playerlg.resetCELInfected();
		playerlg.setSalvation(false);
		playerlg.setLGTargeted(false);
		playerlg.setLGBTargeted(false);
		playerlg.setSosoTargeted(false);
		playerlg.setGMLTargeted(false);
		playerlg.setHuilé(false);
		playerlg.setCharmed(false);
		playerlg.setCorbeauTargeted(false);
		playerlg.setFDJDied(false);
		playerlg.setInCoupDEtat(false);
		playerlg.setMamieTargeted(false);
		playerlg.setPacifTargeted(false);
		playerlg.setPF2Died(false);
		playerlg.setPretreThrower(null);
		playerlg.setPyroTargeted(false);
		if (playerlg.isRole(Roles.VOLEUR)) playerlg.setVoleur(false);
		if (playerlg.isRole(Roles.SERVANTE_DÉVOUÉE)) playerlg.setServante(false);
		if (!main.players.contains(player)) return;
		
		Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6Le village a perdu un de ses membres : §e" + player.getName() + "§6 est §nmort§6. Il était " + playerlg.getRole().getDisplayName() + "§6.");
		
		main.players.remove(player);
		main.spectators.add(player);
		Roles r = main.playerlg.get(player.getName()).getRole();
		if (main.AliveRoles.get(r) > 1) {
			main.AliveRoles.put(r, main.AliveRoles.get(r) - 1);
		} else main.AliveRoles.remove(r);
		main.GRunnable.createScoreboardList();
		main.sleepingPlayers.remove(player);
		playerlg.setVivant(false);
		player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous êtes MORT.");
		for (Player p : Bukkit.getOnlinePlayers())
			player.showPlayer(p);
		
		if (r.equals(Roles.PRÉSIDENT)) {
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire des§r §c§lLoups-Garous §c§l§n§kaa", "§6§l§nNombre de survivants §f: §e" + main.players.size(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire des §c§lLoups-Garous §e!");
			Bukkit.broadcastMessage("§6Survivant(s) §7("+main.players.size()+") §f:");
			for (Player p : main.players) Bukkit.broadcastMessage(getFinalPlayerMessage(p.getName()));
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
			
			displayRoleList();
			
			new LGAutoStop(main).runTaskTimer(main, 0, 20);
			return;
		}
		
		if (!r.equals(Roles.CHASSEUR) && !r.equals(Roles.FOSSOYEUR)) {
		
			player.setGameMode(GameMode.SPECTATOR);
			player.sendMessage(main.getPrefix() + main.SendArrow + "§7Votre mode de jeu à été établi en §lSpectateur§7.");
			player.setDisplayName("§8[§7" + main.playerlg.get(player.getName()).getRole().getName() + "§8] §b" + player.getName());
			player.setPlayerListName(player.getDisplayName());
			player.getInventory().clear();
			Bukkit.getWorld("LG").strikeLightningEffect(player.getLocation());
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.FIREWORK_BLAST2, 10, 1);
			for (PotionEffect pe : player.getActivePotionEffects()) player.removePotionEffect(pe.getType());
		} else {
			if (r.equals(Roles.CHASSEUR)) {
				main.setDisplayState(DisplayState.TIR_CHASSEUR);
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cLe " + r.getDisplayName() + " §cdoit maintenant utiliser son dernier souffle pour tirer...");
				player.getInventory().setItem(0, main.getItem(Material.INK_SACK, "§2Fusil", null));
				player.getInventory().setHeldItemSlot(0);
				player.sendMessage(main.getPrefix() + main.SendArrow + "§aClique droit pour utiliser votre fusil (inventaire).");
				for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 10, 1.5f);
			} else {
				main.setDisplayState(DisplayState.CHOIX_FOSSOYEUR);
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§7Le " + r.getDisplayName() + " §7doit maintenant creuser deux tombes de joueurs de camp opposé.");
				player.getInventory().setItem(0, main.getItem(Material.STONE_SPADE, "§7Pelle", null));
				player.getInventory().setHeldItemSlot(0);
				player.sendMessage(main.getPrefix() + main.SendArrow + "§aClique droit pour creuser les tombes. Vous devrez choisir un joueur et le jeu décidera d'un deuxième joueur d'un camp opposé choisi au hasard.");
				for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.DIG_GRASS, 10, 1.5f);
			}
		}
		
		if (!playerlg.getCouple().isEmpty()) {
			if (main.playerlg.get(playerlg.getCouple().get(0).getName()).isVivant()) {
				new DeathManager(main).eliminate(playerlg.getCouple().get(0), true);
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + player.getName() + " §eet §6" + playerlg.getCouple().get(0).getName() + " §eétaient en §dCouple §e!");
			}
		}
		
		if (r.equals(Roles.VOYANTE) && main.AliveRoles.containsKey(Roles.VOYANTE_APPRENTIE)) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§dÉtant donné que la Voyante est morte, la " + Roles.VOYANTE_APPRENTIE.getDisplayName() + " §dva prendre sa place.");
			Player p = main.getPlayersByRole(Roles.VOYANTE_APPRENTIE).get(0);
			player.sendMessage(main.getPrefix() + main.SendArrow + r.getDescription());
			LG.sendTitle(player, "§fVous êtes " + r.getDisplayName(), "§fVotre camp : §e" + main.playerlg.get(p.getName()).getCamp(), 10, 60, 10);
			main.playerlg.get(p.getName()).setRole(r);
			player.getInventory().setItem(4, main.getRoleMap(r));
		}
		
		if (r.equals(Roles.ANGE) && main.days == 1 && main.isDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR)) winAnge(player);
		
		if (playerlg.isRole(Roles.JOUEUR_DE_FLÛTE)) {
			for (Player p : main.players)
				main.playerlg.get(p.getName()).setCharmed(false);
		}
		
		if (playerlg.isRole(Roles.PYROMANE)) {
			for (Player p : main.players)
				main.playerlg.get(p.getName()).setHuilé(false);
		}
		
		if (playerlg.isRole(Roles.ANKOU))
			player.sendMessage(main.getPrefix() + main.SendArrow + "§7Vous pouvez continuer de voter pendant 2 jours à l'aide de la commande : §e/ankou§7.");
		
		if (!playerlg.getmaitre().isEmpty()) {
			Player maitre = playerlg.getmaitre().get(0);
			
			main.playerlg.get(maitre.getName()).setCamp(RCamp.LOUP_GAROU);
			maitre.sendMessage(main.getPrefix() + main.SendArrow + "§6Votre Modèle est MORT ! Vous êtes donc devenu un " + Roles.LOUP_GAROU.getDisplayName() + " §6!");
			maitre.playSound(maitre.getLocation(), Sound.WOLF_HOWL, 10, 1);
			
			for (Player p : main.players)
				if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
					p.getScoreboard().getTeam("LG").addEntry(maitre.getName());
		}
		
		if (!playerlg.getTargetOf().isEmpty() && main.days == 1 && main.isDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR)) {
			Player merce = playerlg.getTargetOf().get(0);
			
			merce.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez accompli votre tâche : vous avez fait tué §5" + player.getName() + " §clors du premier vote, vous gagnez donc la partie !");
			winMercenaire(merce);
		}
		
		if (!playerlg.getJumeauOf().isEmpty())
			for (Player p : playerlg.getJumeauOf()) {
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (plg.isVivant()) {
					plg.setRole(r);
					plg.setCamp(r.getCamp());
					if (plg.isInfected() && !plg.getCamp().equals(RCamp.LOUP_GAROU_BLANC))
						plg.setCamp(RCamp.LOUP_GAROU);
					if (plg.getCamp().equals(RCamp.VOLEUR))
						plg.setCamp(RCamp.VILLAGE);
					if (plg.isCamp(RCamp.ANGE) || plg.isCamp(RCamp.MERCENAIRE))
						if (main.days != 1)
							plg.setCamp(RCamp.VILLAGE);
					if (plg.isCamp(RCamp.CHIEN_LOUP))
						plg.setCamp(playerlg.getCamp());
					
					p.sendMessage(main.getPrefix() + main.SendArrow + "§5Votre §lJumeau §d\"§5"+player.getName()+"§d\" §5est mort ! Vous dérobez donc son rôle et devenez " + r.getDisplayName() + "§5 !");
				}
			}
		
		if (canCheckWin) checkWin();
	}
	
	
	
	private void winAnge(Player p) {
		LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire de l'"+Roles.ANGE.getDisplayName()+" §c§l§n§kaa", "§6§l§nSurvivant §f: §e" + p.getName(), 10, 90, 20);
		Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire de l'"+Roles.ANGE.getDisplayName()+" §e!");
		Bukkit.broadcastMessage("§6Vainqueur §f:");
		Bukkit.broadcastMessage(getFinalPlayerMessage(p.getName()));
		for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
		main.setState(Gstate.FINISH);
		
		displayRoleList();
		
		new LGAutoStop(main).runTaskTimer(main, 0, 20);
	}
	
	private void winMercenaire(Player p) {
		LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du§r "+Roles.MERCENAIRE.getDisplayName()+" §c§l§n§kaa", "§6§l§nSurvivant §f: §e" + p.getName(), 10, 90, 20);
		Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du "+Roles.MERCENAIRE.getDisplayName()+" §e!");
		Bukkit.broadcastMessage("§6Vainqueur §f:");
		Bukkit.broadcastMessage(getFinalPlayerMessage(p.getName()));
		for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
		main.setState(Gstate.FINISH);
		
		displayRoleList();
		
		new LGAutoStop(main).runTaskTimer(main, 0, 20);
	}



	public void checkWin() {
		RCamp victoryCamp = null;
		
		if (main.isDisplayState(DisplayState.TIR_CHASSEUR)) return;
		if (main.isState(Gstate.FINISH)) return;
		
		if (main.players.size() == 1) {
			victoryCamp = main.playerlg.get(main.players.get(0).getName()).getCamp();
			System.out.println("1 = " + victoryCamp);
		}
		
		else {
			
			if (main.players.size() == 0) {
				
				LG.sendTitleForAllPlayers("§5§kaa§r §7§l§nÉgalité§5 §kaa", "§cAucun survivant.", 10, 90, 20);
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cAucun membre du village n'a survécu à l'épopée des loups. Le village de Thiercelieux est désormais entièrement vide d'habitant...");
				for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
				main.setState(Gstate.FINISH);
				new LGAutoStop(main).runTaskTimer(main, 0, 20);
				
			} else {
				int LGAlive = 0;
				int VillageAlive = 0;
				int CoupleAlive = 0;
				int charmés = 0;
				
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					
					if (playerlg.isCharmed()) charmés++;
					if (!playerlg.getCouple().isEmpty()) {
						CoupleAlive++;
					} else {
						if (playerlg.getRole().equals(Roles.CUPIDON) && main.cupiTeamCouple) CoupleAlive++;
					}
					
					switch (playerlg.getCamp()) {
					case LOUP_GAROU:
						LGAlive++;
						break;
					case VILLAGE:
						VillageAlive++;
						break;
						
					default:
						break;
					
					}
					
				}
				
				if (LGAlive == main.players.size()) victoryCamp = RCamp.LOUP_GAROU;
				if (VillageAlive == main.players.size()) victoryCamp = RCamp.VILLAGE;
				if (CoupleAlive == main.players.size()) victoryCamp = RCamp.COUPLE;
				if (charmés + 1 == main.players.size()) victoryCamp = RCamp.JOUEUR_DE_FLÛTE;
				System.out.println("lg" + LGAlive + " vivi" + VillageAlive + " cpl" + CoupleAlive + " charm" + charmés + " total" + main.players.size());
			}
			
			System.out.println(victoryCamp + "");
			
		}
		
		
		if (victoryCamp == null) return;
		main.setState(Gstate.FINISH);
		main.setDisplayState(DisplayState.DISTRIBUTION_DES_ROLES);
		
		if (victoryCamp.equals(RCamp.LOUP_GAROU)) {
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire des§r §c§lLoups-Garous §c§l§n§kaa", "§6§l§nNombre de survivants §f: §e" + main.players.size(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire des §c§lLoups-Garous §e!");
			Bukkit.broadcastMessage("§6Survivant(s) §7("+main.players.size()+") §f:");
			for (Player player : main.players) Bukkit.broadcastMessage(getFinalPlayerMessage(player.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		else if (victoryCamp.equals(RCamp.LOUP_GAROU_BLANC)) {
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du "+Roles.LOUP_GAROU_BLANC.getDisplayName()+" §c§l§n§kaa", "§6§l§nSurvivant §f: §e" + main.players.get(0).getName(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du "+Roles.LOUP_GAROU_BLANC.getDisplayName()+" §e!");
			Bukkit.broadcastMessage("§6Survivant §f:");
			for (Player player : main.players) Bukkit.broadcastMessage(getFinalPlayerMessage(player.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		
		else if (victoryCamp.equals(RCamp.COUPLE) && main.players.size() > 1) {
			Player p = null;
			for (Player player : main.players)
				if (!main.playerlg.get(player.getName()).getCouple().isEmpty()) p = player;
			
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du§r §d§lCouple §c§l§n§kaa", "§6§l§nSurvivants §f: §e" + p.getName() + "§f et §e" + main.playerlg.get(p.getName()).getCouple().get(0).getName(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du §d§lCouple §e!");
			Bukkit.broadcastMessage("§6Survivants §f:");
			for (Player player : main.players) Bukkit.broadcastMessage(getFinalPlayerMessage(player.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		
		else if (victoryCamp.equals(RCamp.JOUEUR_DE_FLÛTE)) {
			Player p = null;
			for (Player player : main.players)
				if (main.playerlg.get(player.getName()).isRole(Roles.JOUEUR_DE_FLÛTE)) p = player;
			
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du§r "+Roles.JOUEUR_DE_FLÛTE.getDisplayName()+" §c§l§n§kaa", "§6§l§nSurvivant §f: §e" + p.getName(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du "+Roles.JOUEUR_DE_FLÛTE.getDisplayName()+" §e!");
			Bukkit.broadcastMessage("§6Survivant §f:");
			Bukkit.broadcastMessage(getFinalPlayerMessage(p.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		
		else if (victoryCamp.equals(RCamp.PYROMANE)) {
			Player p = null;
			for (Player player : main.players)
				if (main.playerlg.get(player.getName()).isRole(Roles.PYROMANE)) p = player;
			
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du§r "+Roles.PYROMANE.getDisplayName()+" §c§l§n§kaa", "§6§l§nSurvivant §f: §e" + p.getName(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du "+Roles.PYROMANE.getDisplayName()+" §e!");
			Bukkit.broadcastMessage("§6Survivant §f:");
			Bukkit.broadcastMessage(getFinalPlayerMessage(p.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		
		else if (victoryCamp.equals(RCamp.VILLAGE)) {
			LG.sendTitleForAllPlayers("§c§l§n§kaa§r §e§l§nVictoire du§r §e§lVillage §c§l§n§kaa", "§6§l§nNombre de survivants §f: §e" + main.players.size(), 10, 90, 20);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eVictoire du §e§lVillage §e!");
			Bukkit.broadcastMessage("§6Survivant(s) §7("+main.players.size()+") §f:");
			for (Player player : main.players) Bukkit.broadcastMessage(getFinalPlayerMessage(player.getName()));
			for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 10, 1);
			main.setState(Gstate.FINISH);
		}
		
		displayRoleList();
			
		new LGAutoStop(main).runTaskTimer(main, 0, 20);
	}
	
	
	private String getFinalPlayerMessage(String player) {
		String s = "§e" + player;
		PlayerLG playerlg = main.playerlg.get(player);
		s = s + " §7» " + playerlg.getRole().getDisplayName();
		if (playerlg.isRole(Roles.SOEUR)) s = s + "§d (avec §o" + playerlg.getsoeur().get(0).getName() + "§d)";
		if (playerlg.isRole(Roles.FRÈRE)) s = s + "§d (avec §o" + playerlg.getfrère().get(0).getName() + "§d et §o" + playerlg.getfrère().get(1).getName() + "§d)";
		if (playerlg.isServante()) s = s + " §d(" + Roles.SERVANTE_DÉVOUÉE.getDisplayName() + "§d)";
		if (playerlg.isInfected()) s = s + " §c§oInfecté";
		if (!playerlg.getCouple().isEmpty()) s = s + " §den couple avec §l" + playerlg.getCouple().get(0).getName();
		if (playerlg.isCharmed()) s = s + " §5§oCharmé";
		if (playerlg.isVoleur()) s = s + " §3(" + Roles.VOLEUR.getDisplayName() + "§3)";
		if (playerlg.isMaire()) s = s + " §b§oMaire";
		
		return s;
	}
	
	private void displayRoleList() {
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§6Liste des §a§ljoueurs §6/ §lrôles §6: ");
		
		for (Entry<String, PlayerLG> en : main.playerlg.entrySet()) {
			Bukkit.broadcastMessage(getFinalPlayerMessage(en.getKey()));
		}
		
		if (main.AddedRoles.containsKey(Roles.VOLEUR)) {
			String s = "";
			if (main.AddedRoles.get(Roles.VOLEUR) != 1) s = "s";
			
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§3§lRôle"+s+" §3non distribué"+s+" :");
			for (Roles r : main.RolesVoleur)
				Bukkit.broadcastMessage(r.getDisplayName());
		}
		
	}


}
