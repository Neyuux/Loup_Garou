package fr.neyuux.lgthierce.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import fr.neyuux.lgthierce.Gstate;
import fr.neyuux.lgthierce.Index;
import fr.neyuux.lgthierce.role.Roles;
import fr.neyuux.lgthierce.task.GameRunnable;
import fr.neyuux.lgthierce.task.LGAutoStart;

public class StartExecutor implements Listener {

	private Index main;
	
	public StartExecutor(Index main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void onStartInv(InventoryOpenEvent ev) {
		Inventory inv = ev.getInventory();
		if (inv.getName().equals("startlamap")) {
			
			String ssender = inv.getItem(0).getItemMeta().getDisplayName();
			
			if (main.isState(Gstate.STARTING)) {
				main.setState(Gstate.PLAYING);
				
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLancement du jeu !");
				main.sendTitleForAllPlayers("§b§lGO !", "", 20, 20, 20);
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
					p.closeInventory();
					p.getInventory().clear();
					p.setExp(0f);
					main.clearArmor(p);
					p.setGameMode(GameMode.ADVENTURE);
					if (Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()) != null) Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName()).removeEntry(p.getName());
					p.setDisplayName(p.getName());
					p.setPlayerListName(p.getName());
					
					
				}
				
				main.StartRunnable.dealRoles();
				
				new GameRunnable(main).runTaskTimer(main, 0, 20);
			}
			else {
				
				if (main.players.size() >= 5) {
					if (main.isState(Gstate.PREPARING)) {
						if ((main.players.size() + main.spectators.size()) == Bukkit.getOnlinePlayers().size()) {
							List<Roles> roles = new ArrayList<Roles>();
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
				if (main.isState(Gstate.STARTING)) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§b" + ssender + " §ea démarré le jeu !");
				}
				
			}
		}
	
	
	}
	


}