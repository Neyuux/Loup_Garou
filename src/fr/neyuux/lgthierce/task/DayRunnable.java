package fr.neyuux.lgthierce.task;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.neyuux.lgthierce.DeathManager;
import fr.neyuux.lgthierce.DisplayState;
import fr.neyuux.lgthierce.Gcycle;
import fr.neyuux.lgthierce.Index;
import fr.neyuux.lgthierce.PlayerLG;
import fr.neyuux.lgthierce.role.RCamp;
import fr.neyuux.lgthierce.role.Roles;

public class DayRunnable extends BukkitRunnable {
	
	private Index main;
	
	private int currentTimer = Integer.MAX_VALUE;
	private int currentServante = 0;
	List<Player> DictasInCoupDEtat = new ArrayList<Player>();
	List<Player> PacifTargets = new ArrayList<Player>();
	
	public DayRunnable(Index main) {
		this.main = main;
	}

	@Override
	public void run() {
		
		if (!main.isCycle(Gcycle.JOUR)) {
			cancel();
			return;
		}
		
		Bukkit.getWorld("LG").setTime(0);
		
		if (main.isDisplayState(DisplayState.ELECTIONS_DU_MAIRE)) {
			
			if (currentTimer == Integer.MAX_VALUE) currentTimer = 90;
			
			if (currentTimer == 90) {
				for (Player player : main.players) {
					player.getInventory().setItem(1, main.getItem(Material.BOOK, "§3Vote du maire", Arrays.asList("§b>>Clique droit pour ouvrir")));
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 8, 0.1f);
					main.playerlg.get(player.getName()).showArmorStandForAll();
				}
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§bDémarrage de l'élection du maire ! §r\n§bUtilisez votre livre pour voter !");
				Bukkit.broadcastMessage("§9Lorsqu'il y aura égalité pendant le vote d'élimination, le maire pourra trancher et choisira qui mourra parmis les joueurs ex-aequo.");
			}
			
			for (Player player : main.players) {
				Player p = main.playerlg.get(player.getName()).getVotedPlayer();
				
				if (p == null) Index.sendActionBar(player, "§3Vous ne votez pour §b§lpersonne§3.");
				else Index.sendActionBar(player, "§3Vous votez pour §b§l" + p.getName() + "§3.");
			}
			
			PlayerLG rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			if (!rdmp.canVote()) {
				while (!rdmp.canVote())
					rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			}
			if (rdmp.hasUsedPower() && currentTimer > 10) currentTimer = 10;
			
			
			if (currentTimer == 0) {
				HashMap<Player, Player> votes = new HashMap<Player, Player>();
				List<Entry<Player, Integer>> votesNumber = new ArrayList<Entry<Player, Integer>>();
				for (Player player : main.players) {
					if(main.playerlg.get(player.getName()).getVotedPlayer() != null) votes.put(player, main.playerlg.get(player.getName()).getVotedPlayer());
					player.getInventory().remove(Material.BOOK);
					player.closeInventory();
					main.playerlg.get(player.getName()).setVote(null);
					main.playerlg.get(player.getName()).resetVotes();
				}
				
				if (votes.isEmpty()) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§3Aucun vote pour un habitant n'a été enregistré. Il n'y aura donc pas de maire dans la partie.");
					main.maire = false;
					main.setDisplayState(DisplayState.VOTE);
					for (Player p : main.players) {
						main.playerlg.get(p.getName()).setHasUsedPower(false);
					}
					
					currentTimer = Integer.MAX_VALUE;
					return;
				}
				
				for (Player player : main.players) {
					int pvotes = 0;
					for (Entry<Player, Player> en : votes.entrySet()) {
						if (en.getValue().equals(player)) pvotes++;
					}
					SimpleEntry<Player, Integer> se = new SimpleEntry<Player, Integer>(player, pvotes);
					votesNumber.add(se);
				}
				
				while(votesNumber.size() != 1) {
					Integer i1 = votesNumber.get(0).getValue();
					Integer i2 = votesNumber.get(1).getValue();
					
					if (i1.compareTo(i2) > 0) {
						votesNumber.remove(votesNumber.get(1));
					} else if (i1.compareTo(i2) == 0) {
						int r = new Random().nextInt(2);
						if (r == 0) {
							votesNumber.remove(votesNumber.get(1));
						} else {
							votesNumber.remove(votesNumber.get(0));
						}
					} else {
						votesNumber.remove(votesNumber.get(0));
					}
				}
				
				Player maire = votesNumber.get(0).getKey();
				String s = "";
				if (votesNumber.get(0).getValue() != 1) s = "s";
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§b§l" + maire.getName() + "§3 a été élu comme maire du village avec §b§l" + votesNumber.get(0).getValue() + " §3vote" + s + " !");
				maire.setDisplayName("§b" + maire.getName());
				maire.setPlayerListName(maire.getDisplayName());
				main.playerlg.get(maire.getName()).setMaire(true);
				for (Player p : main.players) {
					main.playerlg.get(p.getName()).setHasUsedPower(false);
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 8, 2f);
				}
				
				currentTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.VOTE);
				
			}
			
		}
		
		
		if (main.isDisplayState(DisplayState.VOTE)) {
			
			if (currentTimer == Integer.MAX_VALUE) currentTimer = 150;
			
			if (currentTimer == 150) {
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					player.getInventory().setItem(1, main.getItem(Material.BOOK, "§eVote", Arrays.asList("§b>>Clique droit pour ouvrir")));
					
					if (playerlg.isInCoupDEtat() && playerlg.isRole(Roles.DICTATEUR))
						DictasInCoupDEtat.add(player);
					
					if (playerlg.isRole(Roles.IDIOT_DU_VILLAGE) && playerlg.hasUsedDefinitivePower())
						playerlg.setCanVote(false);
					
					if (playerlg.isPacifTargeted()) {
						playerlg.setPacifTargeted(false);
						player.getInventory().remove(Material.BOOK);
						PacifTargets.add(player);
						DictasInCoupDEtat.clear();
						for (Player p : main.players) {
							main.playerlg.get(p.getName()).setCanVote(false);
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 8, 1);
						}
						currentTimer = 5;
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe " + Roles.PACIFISTE.getName() + " §ea décidé de révéler le rôle de §5§l" + player.getName() + "§e : il est " + playerlg.getRole().getName() + "§e. §cPersonne ne pourra voter pendant ce tour.");	
					}
				}
				
				if (DictasInCoupDEtat.isEmpty() && PacifTargets.isEmpty()) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eC'est l'heure du vote du village ! §r\n§eUtilisez votre livre pour voter pour le joueur qui vous semble le plus susceptible de n'être pas dans votre camp.");
					Bukkit.broadcastMessage("§9Le joueur qui aura reçu le plus de votes se verra §céliminé §9de la partie...");
					for (Player p : Bukkit.getOnlinePlayers())
						p.playSound(p.getLocation(), Sound.GHAST_MOAN, 8, 0.1f);
					
					for (Player p : main.players) {
						if (main.playerlg.get(p.getName()).isCorbeauTargeted()) {
							main.playerlg.get(p.getName()).addVote();
							main.playerlg.get(p.getName()).addVote();
							main.playerlg.get(p.getName()).setCorbeauTargeted(false);
							
							for (Player pl : Bukkit.getOnlinePlayers())
								if (main.players.contains(pl))
									pl.sendMessage(main.getPrefix() + main.SendArrow + "§8" + main.getPlayerNameByAttributes(p, pl) + " §fa reçu la visite du "+Roles.CORBEAU.getDisplayName()+"§f...");
								else pl.sendMessage(main.getPrefix() + main.SendArrow + "§8" + p.getName() + " §fa reçu la visite du "+Roles.CORBEAU.getDisplayName()+"§f...");
						} else if (main.playerlg.get(p.getName()).isMamieTargeted()) {
							main.playerlg.get(p.getName()).setCanVote(false);
							main.playerlg.get(p.getName()).setMamieTargeted(false);
							p.sendMessage(main.getPrefix() + main.SendArrow + "§cLa " + Roles.MAMIE_GRINCHEUSE.getDisplayName() + " §cvous a restreint votre droit de vote pour ce tour.");
						}
					}
				} else if (!DictasInCoupDEtat.isEmpty()) {
					Player dicta = DictasInCoupDEtat.get(0);
					
					for (Player p : main.players) {
						if (!p.getName().equals(dicta.getName())) {
							main.playerlg.get(p.getName()).setCanVote(false);
							System.out.println("§c" + p);
						} else {
							main.playerlg.get(p.getName()).setCanVote(true);
							System.out.println("§a" + p);
						}
					}
					for (Player p : Bukkit.getOnlinePlayers())
						p.playSound(p.getLocation(), Sound.HORSE_ZOMBIE_DEATH, 8, 2f);
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe " + Roles.DICTATEUR.getDisplayName() + " §6" + dicta.getName() + " §eeffectue un coup d'état !");
					Bukkit.broadcastMessage("§9Il sera le seul à pouvoir voter, il pourra donc §céliminer§9 la personne de son choix. Si cette personne fait parti du camp des Loups, §6" + dicta.getName() + " §9récupérera le rôle de maire. Dans le cas inverse, il §cmourra§9.");
					dicta.getInventory().setItem(1, main.getItem(Material.BOOK, "§4Coup d'état", Arrays.asList("§b>>Clique droit pour ouvrir")));
				}
			}
			
			for (Player player : main.players) {
				Player p = main.playerlg.get(player.getName()).getVotedPlayer();
				
				if (p == null) Index.sendActionBar(player, "§eVous ne votez pour §6§lpersonne§e.");
				else Index.sendActionBar(player, "§eVous votez pour §6§l" + p.getName() + "§e.");
			}
			
			
			PlayerLG rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			if (!rdmp.canVote()) {
				while (!rdmp.canVote())
					rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			}
			if (rdmp.hasUsedPower() && currentTimer > 15) currentTimer = 15;
			
			
			if (currentTimer == 0 && PacifTargets.isEmpty()) {
				List<Entry<Player, Integer>> votesNumber = new ArrayList<Entry<Player, Integer>>();
				List<Entry<Player, Integer>> deletedVotes = new ArrayList<Entry<Player, Integer>>();
				for (Player player : main.players) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					player.getInventory().remove(Material.BOOK);
					player.closeInventory();
					playerlg.setVote(null);
				}
				for (Player spec : main.spectators) {
					if (main.playerlg.containsKey(spec.getName())) {
						PlayerLG speclg = main.playerlg.get(spec.getName());
						if (speclg.isRole(Roles.ANKOU) && speclg.hasUsedPower())
							if (speclg.getVotedPlayer() != null) main.playerlg.get(speclg.getVotedPlayer().getName()).addVote();
					}
				}
				for (Player player : main.players) {
					SimpleEntry<Player, Integer> se = new SimpleEntry<Player, Integer>(player, main.playerlg.get(player.getName()).getVotes());
					votesNumber.add(se);
				}
				
				int max = 0;
				for (Entry<Player, Integer> en : votesNumber) {
					if (max == 0) max = en.getValue();
					if (en.getValue() > max) max = en.getValue();
					continue;
				}
				for (Entry<Player, Integer> en : votesNumber) {
					Integer i1 = max;
					Integer i2 = en.getValue();
					
					if (i1.compareTo(i2) > 0) {
						deletedVotes.add(en);
					} else if (i2.compareTo(i1) > 0) {
						throw new ArrayIndexOutOfBoundsException("Le max n'est pas le vote le plus élevé");
					}
				}
				for (Entry<Player, Integer> en : deletedVotes) {
					main.playerlg.get(en.getKey().getName()).setInEqual(true);
					System.out.println(en.toString());
					votesNumber.remove(en);
				}
				System.out.println(max + " §e" + votesNumber.toString());
				
				if (max == 0) {
					if (main.AliveRoles.containsKey(Roles.BOUC_ÉMISSAIRE) && DictasInCoupDEtat.isEmpty()) {
						Player bc = main.getPlayersByRole(Roles.BOUC_ÉMISSAIRE).get(0);
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eIl y a égalité dans le vote, et ne sachant qui éliminer, le village décider de pendre le " + Roles.BOUC_ÉMISSAIRE.getDisplayName() + " §e... Paix à son âme §4de victime§e...");
						new DeathManager(main).eliminate(bc, true);
					} else {
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cAucun vote n'a été enregistré pour un habitant. Il n'y aura donc aucune exécution :(");
						currentTimer = Integer.MAX_VALUE;
						main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
						return;
					}
				}
				
				if (votesNumber.size() > 1) {
					if (main.AliveRoles.containsKey(Roles.BOUC_ÉMISSAIRE)) {
						Player bc = main.getPlayersByRole(Roles.BOUC_ÉMISSAIRE).get(0);
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eIl y a égalité dans le vote, et ne sachant qui éliminer, le village décider de pendre le " + Roles.BOUC_ÉMISSAIRE.getDisplayName() + " §e... Paix à son âme §4de victime§e...");
						new DeathManager(main).eliminate(bc, true);
						currentTimer = Integer.MAX_VALUE;
						main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
					}
					else if (main.maire) {
						Player maire = null;
						for (Player player : main.players)
							if (main.playerlg.get(player.getName()).isMaire()) maire = player;
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eIl y a égalité entre §6" + votesNumber.size() + "§e joueurs. Le maire du village \"" + maire.getDisplayName() + "§e\" va départager entre les joueurs.");
						String lequals = "";
						for (Entry<Player, Integer> en : votesNumber) {
							Player p = en.getKey();
							if (lequals == "") lequals = "§6" + p.getDisplayName();
							else lequals = lequals + "§e, §6" + p.getDisplayName();
						}
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eListe des personnes à égalité : " + lequals + "§e.");
						maire.getInventory().setItem(1, main.getItem(Material.BOOK, "§bDépartager les joueurs", Arrays.asList("§b>>Clique droit pour ouvrir")));
						maire.sendMessage(main.getPrefix() + main.SendArrow + "§3Vous pouvez sélectionner le joueur que vous souhaitez éliminer dans le livre.");
						main.setDisplayState(DisplayState.VOTE_MAIRE);
					}
					else {
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eIl y égalité dans les votes, il va donc il y en avoir un deuxième qui va départager les habitants à égalité.");
						String lequals = "";
						for (Entry<Player, Integer> en : votesNumber) {
							Player p = en.getKey();
							if (lequals == "") lequals = "§6" + p.getDisplayName();
							else lequals = lequals + "§e, §6" + p.getDisplayName();
						}
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eListe des habitants à égalité : " + lequals + "§e.");
						
						main.setDisplayState(DisplayState.VOTE2);
					}
				}
				else {
					Player targeted = votesNumber.get(0).getKey();
					if (DictasInCoupDEtat.isEmpty())
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe vote du village possède un résultat : §6" + targeted.getName() + " §ea obtenu le plus de votes(§6" + votesNumber.get(0).getValue() + "§e). Il sera donc éliminé.");
					else Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe " + Roles.DICTATEUR.getDisplayName() + " §6" + DictasInCoupDEtat.get(0).getName() + " §e a décidé d'éliminer §6§l" + targeted.getName() + "§e.");
					main.playerlg.get(targeted.getName()).setDayTargeted(true);
					currentTimer = Integer.MAX_VALUE;
					main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
					if (main.AliveRoles.containsKey(Roles.SERVANTE_DÉVOUÉE) && targeted != null) main.setDisplayState(DisplayState.CHOIX_SERVANTE);
					if (main.isDisplayState(DisplayState.CHOIX_SERVANTE)) {
						if (main.playerlg.get(targeted.getName()).isRole(Roles.IDIOT_DU_VILLAGE) && main.playerlg.get(targeted.getName()).canVote()) main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
						if (!DictasInCoupDEtat.isEmpty()) main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
					}
				}
				for (Player p : main.players) {
					main.playerlg.get(p.getName()).setHasUsedPower(false);
					main.playerlg.get(p.getName()).resetVotes();
				}
				currentTimer = Integer.MAX_VALUE;
				
			} else if (!PacifTargets.isEmpty()) {
				currentTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
			}
			
		}
		
		
		if (main.isDisplayState(DisplayState.VOTE2)) {
			
			if (currentTimer == Integer.MAX_VALUE) currentTimer = 30;
			
			if (currentTimer == 150) {
				for (Player player : main.players)
					player.getInventory().setItem(1, main.getItem(Material.BOOK, "§eDeuxième Vote", Arrays.asList("§b>>Clique droit pour ouvrir")));
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eC'est l'heure du vote du village ! §r\n§eUtilisez votre livre pour voter pour le joueur qui vous semble le plus susceptible de n'être pas dans votre camp.");
				Bukkit.broadcastMessage("§9Le joueur qui aura reçu le plus de votes se verra §céliminé §9de la partie...");
			}
			
			for (Player player : main.players) {
				if (!player.getInventory().contains(Material.BOOK))
					player.getInventory().setItem(1, main.getItem(Material.BOOK, "§eDeuxième Vote", Arrays.asList("§b>>Clique droit pour ouvrir")));
				
				Player p = main.playerlg.get(player.getName()).getVotedPlayer();
				
				if (p == null) Index.sendActionBar(player, "§eVous ne votez pour §6§lpersonne§e.");
				else Index.sendActionBar(player, "§eVous votez pour §6§l" + p.getName() + "§e.");
			}
			
			
			PlayerLG rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			if (!rdmp.canVote()) {
				while (!rdmp.canVote())
					rdmp = main.playerlg.get(main.players.get(new Random().nextInt(main.players.size())).getName());
			}
			if (rdmp.hasUsedPower() && currentTimer > 5) currentTimer = 5;
			
			
			if (currentTimer == 0) {
				List<Entry<Player, Integer>> votesNumber = new ArrayList<Entry<Player, Integer>>();
				List<Entry<Player, Integer>> deletedVotes = new ArrayList<Entry<Player, Integer>>();
				for (Player player : main.players) {
					player.getInventory().remove(Material.BOOK);
					player.closeInventory();
					main.playerlg.get(player.getName()).setVote(null);
					main.playerlg.get(player.getName()).resetVotes();
				}
				for (Player spec : main.spectators) {
					if (main.playerlg.containsKey(spec.getName())) {
						PlayerLG speclg = main.playerlg.get(spec.getName());
						if (speclg.isRole(Roles.ANKOU) && speclg.hasUsedPower())
							if (speclg.getVotedPlayer() != null) main.playerlg.get(speclg.getVotedPlayer().getName()).addVote();
					}
				}
				for (Player player : main.players) {
					SimpleEntry<Player, Integer> se = new SimpleEntry<Player, Integer>(player, main.playerlg.get(player.getName()).getVotes());
					votesNumber.add(se);
				}
				
				int max = 0;
				for (Entry<Player, Integer> en : votesNumber) {
					if (max == 0) max = en.getValue();
					if (en.getValue() > max) max = en.getValue();
					continue;
				}
				for (Entry<Player, Integer> en : votesNumber) {
					Integer i1 = max;
					Integer i2 = en.getValue();
					
					if (i1.compareTo(i2) > 0) {
						deletedVotes.add(en);
					} else if (i2.compareTo(i1) > 0) {
						throw new ArrayIndexOutOfBoundsException("Le max n'est pas le vote le plus élevé");
					}
				}
				for (Entry<Player, Integer> en : deletedVotes) {
					votesNumber.remove(en);
				}
				
				if (max == 0) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cAucun vote n'a été enregistré pour un habitant. Il n'y aura donc aucune exécution :(");
					currentTimer = Integer.MAX_VALUE;
					main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
					return;
				}
				
				if (votesNumber.size() > 1) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eIl y a une deuxième égalité, personne ne meurt donc aujourd'hui.");
					main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
					currentTimer = Integer.MAX_VALUE;
				}
				else {
					Player targeted = votesNumber.get(0).getKey();
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe vote du village possède un résultat : §6" + targeted.getName() + " §ea obtenu le plus de votes(§6" + votesNumber.get(0).getValue() + "§e). Il sera donc éliminé.");
					main.playerlg.get(targeted.getName()).setDayTargeted(true);
					currentTimer = Integer.MAX_VALUE;
					main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
					if (main.AliveRoles.containsKey(Roles.SERVANTE_DÉVOUÉE) && targeted != null) main.setDisplayState(DisplayState.CHOIX_SERVANTE);
					if (main.isDisplayState(DisplayState.CHOIX_SERVANTE)) 
						if (main.playerlg.get(targeted.getName()).isRole(Roles.IDIOT_DU_VILLAGE) && main.playerlg.get(targeted.getName()).canVote()) main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
				}
				
			}
			
		}
		
		
		if (main.isDisplayState(DisplayState.VOTE_MAIRE)) {
			Player maire = null;
			for (Player player : main.players)
				if (main.playerlg.get(player.getName()).isMaire()) maire = player;
			
			if (currentTimer == Integer.MAX_VALUE) currentTimer = 20;
			
			if (main.playerlg.get(maire.getName()).hasUsedPower()) currentTimer = 0;
			
			if (currentTimer == 0) {
				Boolean hasChoosed = false;
				Player targeted = null;
				for (Player player : main.players)
					if (main.playerlg.get(player.getName()).isDayTargeted()) targeted = player;
				
				if (targeted != null) hasChoosed = true;
				
				if (!hasChoosed) {
					maire.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez mit trop de temps à choisir.");
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§cLe maire a mit trop de temps à choisir. Il n'y aura donc aucune exécution :(");
				} else {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe §bmaire §ea choisi : §6§l" + targeted.getName() + " §esera exécuté aujourd'hui.");
				}
				maire.getInventory().remove(Material.BOOK);
				maire.closeInventory();
				for (Entry<String, PlayerLG> en : main.playerlg.entrySet()) {
					en.getValue().setHasUsedPower(false);
					en.getValue().setInEqual(false);
				}
				currentTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
				if (main.AliveRoles.containsKey(Roles.SERVANTE_DÉVOUÉE) && targeted != null) main.setDisplayState(DisplayState.CHOIX_SERVANTE);
				if (main.isDisplayState(DisplayState.CHOIX_SERVANTE)) 
					if (main.playerlg.get(targeted.getName()).isRole(Roles.IDIOT_DU_VILLAGE) && main.playerlg.get(targeted.getName()).canVote()) main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
			}
			
		}
		
		
		if (main.isDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR)) {
			
			if (currentTimer == Integer.MAX_VALUE) currentTimer = 3;
			
			if (currentTimer == 3) {
				Player targeted = null;
				PlayerLG targetedlg = null;
				for (Player p : main.players) 
					if (main.playerlg.get(p.getName()).isDayTargeted()) {
					targeted = p;
					targetedlg = main.playerlg.get(targeted.getName());
				}
				
				if (targeted == null) {
					currentTimer =0;
					return;
				}
				
				for (Player spec : main.spectators) {
					if (main.playerlg.containsKey(spec.getName())) {
						PlayerLG speclg = main.playerlg.get(spec.getName());
						if (speclg.isRole(Roles.ANKOU) && speclg.hasUsedPower() && speclg.getVotedPlayer() != null) {
							Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§6" + targeted.getName() + "§c avait reçu un vote de l'" + Roles.ANKOU.getDisplayName() + "§4 " + spec.getName() + "§c.");
							speclg.setHasUsedPower(false);
							speclg.removeAnkouVote();
							speclg.setVote(null);
						}
					}
				}
				
				if (targetedlg.isRole(Roles.IDIOT_DU_VILLAGE) && targetedlg.canVote() && !targetedlg.hasUsedDefinitivePower() && DictasInCoupDEtat.isEmpty())
					targetedlg.setDayTargeted(false);
					
				
				if (main.cycleJourNuit) Bukkit.getWorld("LG").setTime((long)23000);
				Bukkit.broadcastMessage("");

				if (targetedlg.isDayTargeted()) {
					if (targetedlg.isRole(Roles.CHASSEUR) || targetedlg.isRole(Roles.FOSSOYEUR)) currentTimer = Integer.MAX_VALUE;
					if (targetedlg.isRole(Roles.ANCIEN)) {
						for (Player p : main.players)
							if (main.playerlg.get(p.getName()).isCamp(RCamp.VILLAGE))
								main.playerlg.get(p.getName()).setRole(Roles.SIMPLE_VILLAGEOIS);
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fL'" + Roles.ANCIEN.getDisplayName() + " §f est mort ! C'est honteux ! Tous les villageois perdent leur pouvoirs...");
					}
					if (DictasInCoupDEtat.isEmpty()) new DeathManager(main).eliminate(targeted, true);
					else new DeathManager(main).eliminate(targeted, false);
					
					if (!DictasInCoupDEtat.isEmpty()) {
						Player dicta = DictasInCoupDEtat.get(0);
						Bukkit.broadcastMessage("");
						if (targetedlg.isCamp(RCamp.LOUP_GAROU) || targetedlg.isCamp(RCamp.LOUP_GAROU_BLANC)) {
							Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe " + Roles.DICTATEUR.getDisplayName() + " §ea réussi un coup de maître ! Le village, pour le remercier, lui donne le rôle de maire.");
							for (Player p : main.players) 
								if (main.playerlg.get(p.getName()).isMaire()) {
									main.playerlg.get(p.getName()).setMaire(false);
									p.setDisplayName(p.getName());
									p.setPlayerListName(p.getName());
								}
							main.playerlg.get(dicta.getName()).setMaire(true);
							main.maire = true;
							dicta.setDisplayName("§b" + dicta.getName());
							dicta.setPlayerListName(dicta.getDisplayName());
						} else {
							Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLe " + Roles.DICTATEUR.getDisplayName() + " §ea tué un innocent ! De honte, il se suicide devant tout le village...");
							new DeathManager(main).eliminate(dicta, true);
						}
						main.playerlg.get(dicta.getName()).setUsedDefinitivePower(true);
						for (Player p : main.players) {
							if (main.playerlg.get(p.getName()).isInCoupDEtat())
								main.playerlg.get(p.getName()).setInCoupDEtat(false);
							main.playerlg.get(p.getName()).setCanVote(true);
						}
					}
				} else {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§fLe village, juste avant de pendre §e" + targeted.getName() + "§f, remarque §fqu'il§4 est COMPLETEMENT DÉBILE§f, il ne meurt donc pas mais son de rôle de " + Roles.IDIOT_DU_VILLAGE.getDisplayName() + " §fest révélé et il ne pourra plus voter.");
					targetedlg.setCanVote(false);
					targetedlg.setUsedDefinitivePower(true);
					for (Player p : Bukkit.getOnlinePlayers())
						p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 10, 1);
				}
				
				if (main.days == 1) {
					for (Player p : main.players) {
						if (main.playerlg.get(p.getName()).isRole(Roles.ANGE))
							main.playerlg.get(p.getName()).setCamp(RCamp.VILLAGE);
						
						if (main.playerlg.get(p.getName()).isRole(Roles.MERCENAIRE))
							main.playerlg.get(p.getName()).setCamp(RCamp.VILLAGE);
						
						if (!main.playerlg.get(p.getName()).getTargetOf().isEmpty())
							main.playerlg.get(p.getName()).getTargetOf().clear();
					}
				}
				
			}
							
			if (currentTimer == 0) {
				currentTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.TOMBEE_DE_LA_NUIT);
			}
			
		}
		
		
		if (main.isDisplayState(DisplayState.CHOIX_SERVANTE)) {

			if (currentTimer == Integer.MAX_VALUE) currentTimer = 15;
			
			Player player = null;
			if (main.getPlayersByRole(Roles.SERVANTE_DÉVOUÉE).size() > currentServante)
				if (main.playerlg.get(main.getPlayersByRole(Roles.SERVANTE_DÉVOUÉE).get(currentServante).getName()).isServante())
					player = main.getPlayersByRole(Roles.SERVANTE_DÉVOUÉE).get(currentServante);
			
			if (player == null)
				for (Player p : main.players)
					if (p.getInventory().contains(Material.STONE_BUTTON)) player = p;
			Player targeted = null;
			for (Player p : main.players) if (main.playerlg.get(p.getName()).isDayTargeted()) targeted = p;
			
			if (currentTimer == 15) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§dLa " + Roles.SERVANTE_DÉVOUÉE.getDisplayName() + " §dpeut désormais choisir de prendre le rôle du joueur ciblé.");
			}
			
			if (!player.getOpenInventory().getTopInventory().getName().equals("§6Inv " + Roles.SERVANTE_DÉVOUÉE.getDisplayName()) && main.playerlg.get(player.getName()).getCouple().isEmpty() && !player.getInventory().contains(Material.STONE_BUTTON)) {
				Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "§6Inv " + Roles.SERVANTE_DÉVOUÉE.getDisplayName());
				inv.setItem(0, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)5, "§a§lPrendre le rôle de §d" + targeted.getName(), Arrays.asList("§7Prendre le rôle de §d" + targeted.getName() + "§7.", "§7Vous obtiendrez ses pouvoirs.", "", "§b>>Clique pour sélectionner")));
				
				inv.setItem(4, main.config.getColoredItem(Material.STAINED_CLAY, 1, (short)14, "§cNe §lpas§c prendre le rôle de §d" + targeted.getName(), Arrays.asList("§7Ne prend pas le rôle de §d" + targeted.getName() + "§7.", "", "§b>>Clique pour sélectionner")));
				
				ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta itm = (SkullMeta)it.getItemMeta();
				itm.setOwner(targeted.getName());
				itm.setDisplayName("§d" + targeted.getName());
				itm.setLore(Arrays.asList("§7Le joueur §d" + targeted.getName() + "§7 a été ciblé aujourd'hui.", "§7Voulez-vous prendre son rôle ?"));
				it.setItemMeta(itm);
				inv.setItem(2, it);
			
				player.openInventory(inv);
			} else {
				if (!main.playerlg.get(player.getName()).getCouple().isEmpty()) {
					if (currentTimer == 10) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "§cVotre amour pour §d§l" + main.playerlg.get(player.getName()).getCouple().get(0).getName() + " §cest plus fort que votre désir de changer de personne. §4Vous ne pouvez pas utiliser votre pouvoir.");
					}
				}
			}
			
			Index.sendActionBarForAllPlayers(main.getPrefix() + main.SendArrow + "§fAu tour de la " + Roles.SERVANTE_DÉVOUÉE.getDisplayName() + "§f.");
			
			if (main.playerlg.get(player.getName()).hasUsedPower()) {
				currentTimer = Integer.MAX_VALUE;
				main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
			}
			
			if (player.getInventory().contains(Material.STAINED_CLAY)) {
				player.getInventory().remove(Material.STAINED_CLAY);
				currentTimer = 0;
			}
			
			if (currentTimer == 0) {
				currentTimer = Integer.MAX_VALUE;
				currentServante++;
				player.closeInventory();
				if (currentServante == main.getPlayersByRole(Roles.SERVANTE_DÉVOUÉE).size()) {
					main.setDisplayState(DisplayState.ANNONCES_DES_MORTS_JOUR);
				}
				main.GRunnable.createScoreboardList();
			}
			
		}
		
		if (currentTimer != Integer.MAX_VALUE) currentTimer--;
		for (Player player : Bukkit.getOnlinePlayers())
			if (currentTimer != Integer.MAX_VALUE) player.setLevel(currentTimer);
		
	}

}
