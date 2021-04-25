package fr.neyuux.lgthierce.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import fr.neyuux.lgthierce.DisplayState;
import fr.neyuux.lgthierce.Gstate;
import fr.neyuux.lgthierce.Gtype;
import fr.neyuux.lgthierce.Index;
import fr.neyuux.lgthierce.PlayerLG;
import fr.neyuux.lgthierce.role.Roles;

public class LGAutoStart extends BukkitRunnable {

	private int timer = 11;
	private Index main;
	
	public LGAutoStart(Index main) {
		this.main = main;
	}
	
	
	@Override
	public void run() {
		
		if (main.players.size() + main.spectators.size() != Bukkit.getOnlinePlayers().size()) {
			main.setState(Gstate.PREPARING);
		}
		
		if (main.isType(Gtype.RÉUNION) && main.players.size() > 12) {
			main.setState(Gstate.PREPARING);
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§c§l12 JOUEURS MAXIMUM POUR LE MODE RÉUNION");
		}
		
		if (!main.isState(Gstate.STARTING)) {
			this.timer = 11;
			cancel();
			return;
		}
		
		for(Player pls : Bukkit.getOnlinePlayers()) {
			pls.setLevel(timer);
			pls.setExp((float)timer / 10);
			if (pls.getInventory().contains(Material.GHAST_TEAR)) {
				pls.getInventory().remove(Material.REDSTONE_COMPARATOR);
				pls.getInventory().remove(Material.GHAST_TEAR);
			}
			if (main.playerlg.containsKey(pls.getName())) {
				if (main.spectators.contains(pls))
					main.playerlg.remove(pls.getName());
			}
		}
		

		if (timer != 11 && timer != 0) {
			if (timer == 1) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLancement du jeu dans §c§l" + timer + "§c seconde !");
			} else {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLancement du jeu dans §c§l" + timer + "§c secondes !");
			}
		
		}
		timer--;
		
		if (timer == -1) {
			main.setState(Gstate.PLAYING);
			
			cancel();
			this.timer = 11;
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLancement du jeu !");
			main.sendTitleForAllPlayers("§b§lGO !", "", 20, 20, 20);
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
				p.closeInventory();
				p.getInventory().clear();
				p.setTotalExperience(0);
				main.clearArmor(p);
				p.setGameMode(GameMode.ADVENTURE);
				if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()) != null) Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()).removeEntry(p.getName());
				p.setDisplayName(p.getName());
				p.setPlayerListName(p.getName());
				
				
			}
			
			dealRoles();
			
			new GameRunnable(main).runTaskTimer(main, 0, 20);
		}
		
		
		
		if (timer==9) {
			System.out.println("9");
		main.sendTitleForAllPlayers("§4§l10", "§c§oPréparation...", 20, 30, 20);
		for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 2f);
		} else if (timer==4) {
			System.out.println("4");
			main.sendTitleForAllPlayers("§6§l5", "§cAttention !", 5, 10, 5);
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.7f);
		} else if (timer==3) {
			System.out.println("3");
			main.sendTitleForAllPlayers("§e§l4", "§cAttention !", 5, 10, 5);
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.5f);
		} else if (timer==2) {
			System.out.println("2");
			main.sendTitleForAllPlayers("§2§l3", "§6A vos marques...", 5, 10, 5);
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.2f);
		} else if (timer==1) {
			main.sendTitleForAllPlayers("§a§l2", "§ePrêts ?", 5, 10, 5);
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1.1f);
		} else if (timer==0) {
			main.sendTitleForAllPlayers("§f§l1", "§aDécollage...", 5, 10, 5);
			for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 8, 1f);
		}
		
	}
	
	
	
	
	public void dealRoles() {
		List<Player> playersWithoutRole = new ArrayList<Player>();
		for (Player p : main.players) playersWithoutRole.add(p);
		List<Roles> roles = new ArrayList<Roles>();
		for (java.util.Map.Entry<Roles, Integer> en : main.AddedRoles.entrySet()) {
			if (en.getValue() > 1) {
				int fois = en.getValue();
				while (fois != 0) {
					roles.add(en.getKey());
					main.AliveRoles.put(en.getKey(), en.getValue());
					fois--;
				}
			} else {
				roles.add(en.getKey());
				main.AliveRoles.put(en.getKey(), en.getValue());
			}
		}
		if (roles.contains(Roles.VOLEUR) && main.RolesVoleur.size() < main.AddedRoles.get(Roles.VOLEUR) * 2) {
			Random rdmv = new Random();
			
			int rv1 = rdmv.nextInt(roles.size());
			Roles role = roles.get(rv1);
			if (role.equals(Roles.VOLEUR) || role.equals(Roles.SOEUR) || role.equals(Roles.FRÈRE)) {
				while (role.equals(Roles.VOLEUR) || role.equals(Roles.SOEUR) || role.equals(Roles.FRÈRE)) {
					rv1 = rdmv.nextInt(roles.size());
					role = roles.get(rv1);
				}
			}
			roles.remove(role);
			main.RolesVoleur.add(role);
			
			int rv2 = rdmv.nextInt(roles.size());
			Roles role2 = roles.get(rv2);
			if (role2.equals(Roles.VOLEUR) || role2.equals(Roles.SOEUR) || role2.equals(Roles.FRÈRE)) {
				while (role2.equals(Roles.VOLEUR) || role2.equals(Roles.SOEUR) || role2.equals(Roles.FRÈRE)) {
					rv2 = rdmv.nextInt(roles.size());
					role2 = roles.get(rv2);
				}
			}
			roles.remove(role2);
			main.RolesVoleur.add(role2);
		}
		
		for (Player p : main.players) {
			if (main.playerlg.get(p.getName()).getRole() != null) {
				roles.remove(main.playerlg.get(p.getName()).getRole());
			}
		}
		
		Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
			
			@Override
			public void run() {
				if (playersWithoutRole.isEmpty()) return;
				main.setDisplayState(DisplayState.DISTRIBUTION_DES_ROLES);
				
				int rdm = new Random().nextInt(playersWithoutRole.size());
				Player player = playersWithoutRole.get(rdm);
				PlayerLG playerlg = main.playerlg.get(player.getName());
				Roles role = null;
				System.out.println(roles.toString() + roles.size());
				
				Index.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + " §8Dealing Role to §b" + player.getName());
				System.out.println(main.playerlg.get(player.getName()).getRole() + " " + player.getName());
				
				if (main.playerlg.get(player.getName()).getRole() == null) {
					int Rolerdm = new Random().nextInt(roles.size());

					for (Player p : Bukkit.getOnlinePlayers()) p.playSound(p.getLocation(), Sound.NOTE_STICKS, 0, 9);
					
					role = roles.get(Rolerdm);
					
					player.sendMessage(main.getPrefix() + main.SendArrow + role.getDescription());
					main.sendTitle(player, "§fVous êtes " + role.getDisplayName(), "§fVotre camp : §e" + role.getCamp(), 10, 60, 10);
					playerlg.setRole(role);
					playerlg.setCamp(role.getCamp());
					player.getInventory().setItem(4, main.getRoleMap(role));
					
					
					
					if (role.equals(Roles.SOEUR)) {
						if (playerlg.getsoeur().isEmpty()) {
							int rdm2 = new Random().nextInt(playersWithoutRole.size());
							Player p = playersWithoutRole.get(rdm2);
							PlayerLG plg = main.playerlg.get(p.getName());
							
							if (player.getUniqueId().equals(p.getUniqueId()) || !plg.getsoeur().isEmpty()) {
								while (player.getUniqueId().equals(p.getUniqueId()) || !plg.getsoeur().isEmpty()) {
									rdm2 = new Random().nextInt(playersWithoutRole.size());
									p = playersWithoutRole.get(rdm2);
									plg = main.playerlg.get(p.getName());
								}
							}
							
							plg.setRole(Roles.SOEUR);
							
							System.out.println("Soeur " + p);
							
							plg.addsoeur(player);
							playerlg.addsoeur(p);
							
							roles.remove(role);
						}
						player.sendMessage("§dVotre " + Roles.SOEUR.getDisplayName() + " §dest §l" + playerlg.getsoeur().get(0).getName() + "§d.");
					}
					
					else if (role.equals(Roles.FRÈRE)) {
						if (playerlg.getfrère().isEmpty()) {
							int rdm2 = new Random().nextInt(playersWithoutRole.size());
							Player p = playersWithoutRole.get(rdm2);
							PlayerLG plg = main.playerlg.get(p.getName());
							
							int rdm3 = new Random().nextInt(playersWithoutRole.size());
							Player p2 = playersWithoutRole.get(rdm3);
							PlayerLG p2lg = main.playerlg.get(p2.getName());
							
							
							if (player.getUniqueId().equals(p.getUniqueId()) || !plg.getfrère().isEmpty()) {
								while (player.getUniqueId().equals(p.getUniqueId()) || !plg.getfrère().isEmpty()) {
									rdm2 = new Random().nextInt(playersWithoutRole.size());
									p = playersWithoutRole.get(rdm2);
									plg = main.playerlg.get(p.getName());
								}
							}
							
							if (player.getUniqueId().equals(p2.getUniqueId()) || !p2lg.getfrère().isEmpty()) {
								while (player.getUniqueId().equals(p2.getUniqueId()) || !p2lg.getfrère().isEmpty()) {
									rdm3 = new Random().nextInt(playersWithoutRole.size());
									p = playersWithoutRole.get(rdm3);
									p2lg = main.playerlg.get(p2.getName());
								}
							}
							
							if (p.getUniqueId().equals(p2.getUniqueId())) {
								while (p.getUniqueId().equals(p2.getUniqueId())) {
									rdm3 = new Random().nextInt(playersWithoutRole.size());
									p2 = playersWithoutRole.get(rdm3);
									p2lg = main.playerlg.get(p2.getName());
								}
							}
							
							plg.setRole(Roles.FRÈRE);
							p2lg.setRole(Roles.FRÈRE);
							
							plg.addfrère(player);
							p2lg.addfrère(player);
							
							playerlg.addfrère(p);
							p2lg.addfrère(p);
							
							playerlg.addfrère(p2);
							plg.addfrère(p2);
							
							roles.remove(role);
							roles.remove(role);
					}
						player.sendMessage("§dVos " + Roles.FRÈRE.getDisplayName() + "s §dsont §l" + playerlg.getfrère().get(0).getName() + " §det §l" + playerlg.getfrère().get(1).getName() + "§d.");
					}
					
					System.out.println(player.getName() + " ---> " + role.getDisplayName());
					playersWithoutRole.remove(player);
					roles.remove(role);
				}
				
				else {
					role = playerlg.getRole();
					
					playerlg.setCamp(role.getCamp());
					player.sendMessage(main.getPrefix() + main.SendArrow + role.getDescription());
					main.sendTitle(player, "§fVous êtes " + role.getDisplayName(), "§fVotre camp : §e" + playerlg.getCamp(), 10, 60, 10);
					
					if (role.equals(Roles.SOEUR)) {
						if (!playerlg.getsoeur().isEmpty()) {
							player.sendMessage("§dVotre " + Roles.SOEUR.getDisplayName() + " §dest §l" + playerlg.getsoeur().get(0).getName() + "§d.");
						}
					}
					
					if (role.equals(Roles.FRÈRE)) {
						if (!playerlg.getfrère().isEmpty()) {
							player.sendMessage("§dVos " + Roles.FRÈRE.getDisplayName() + "s §dsont §l" + playerlg.getfrère().get(0).getName() + "§d et §l" + playerlg.getfrère().get(1).getName() + "§d.");
						}
					}
					
					System.out.println(player.getName() + "pre-roled --->" + role.getDisplayName());
					playersWithoutRole.remove(player);
					if (roles.contains(role)) roles.remove(role);
				}
				try {
					player.getInventory().setItem(4, main.getRoleMap(role));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 15);
	
		
	}
	

	
}
