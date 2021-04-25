package fr.neyuux.lgthierce;

import fr.neyuux.lgthierce.commands.CommandAnkou;
import fr.neyuux.lgthierce.commands.SpecExecutor;
import fr.neyuux.lgthierce.commands.StartExecutor;
import fr.neyuux.lgthierce.listeners.DayListener;
import fr.neyuux.lgthierce.listeners.LGThierceListener;
import fr.neyuux.lgthierce.listeners.NightListener;
import fr.neyuux.lgthierce.role.RCamp;
import fr.neyuux.lgthierce.role.RDeck;
import fr.neyuux.lgthierce.role.Roles;
import fr.neyuux.lgthierce.task.GameRunnable;
import fr.neyuux.lgthierce.task.LGAutoStart;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

public class Index extends JavaPlugin {
	
	public List<Player> players = new ArrayList<>();
	public List<Player> spectators = new ArrayList<>();
	public int nights = 0;
	public int days = 0;
	public List<Roles> CalledRoles = new ArrayList<>();
	public HashMap<Roles, Integer> AliveRoles = new HashMap<>();
	public List<Roles> RolesVoleur = new ArrayList<>();
	public HashMap<Player, Boolean> SosoKillPots = new HashMap<>();
	public HashMap<Player, Boolean> SosoRézPots = new HashMap<>();
	public List<Player> sleepingPlayers = new ArrayList<>();
	public HashMap<Player, Player> lastSalvaté = new HashMap<>();
	
	private Gstate state;
	private Gcycle cycle;
	private Gtype type;
	private DisplayState displayState;
	public final Map<String, PlayerLG> playerlg = new HashMap<>();
	
	public static final HashMap<String, List<UUID>> Grades = new HashMap<>();
	public static final String prefix = "§c§lLoups§e§l-§6§lGarous";
	public final String SendArrow = "§8§l» §r";
	public GameConfig config = new GameConfig(this);
	public GameRunnable GRunnable = null;
	public LGAutoStart StartRunnable = null;

	public HashMap<RDeck, List<Roles>> DeckRoles = new HashMap<>();
	public HashMap<Roles, Integer> AddedRoles = new HashMap<>();
	public Boolean cycleJourNuit = true;
	public Boolean chatDesLg = true;
	public Boolean cupiTeamCouple = false;
	public Boolean cupiEnCouple = false;
	public Boolean coupleRandom = false;
	public Boolean maitreRandom = false;
	public Boolean maire = true;
	public Boolean paroleChaman = false;
	public Boolean voyanteBavarde = false;
	public List<Roles> pouvoirsComédien = new ArrayList<>();
	
	
	public double LibreSpawnX = 363.5;
	public double LibreSpawnY = 84.1;
	public double LibreSpawnZ = 626.5;
	
	public double ReunionSpawnX = 108.5;
	public double ReunionSpawnY = 30.2;
	public double ReunionSpawnZ = 850.5;
	
	
	public final List<Block> BedList = new ArrayList<>();
	public final Map<Block, Integer> BlockList = new HashMap<>();
	public final Map<Block, Integer> UsedBlockList = new HashMap<>();
	
	
	public HashMap<Roles, Integer> RoleScoreboard = new HashMap<>();



	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
		System.out.println("LG Enabling");
		if (!System.getProperties().containsKey("RELOAD")) {
			Properties prop = new Properties(System.getProperties());
			prop.put("RELOAD", "FALSE");
		} else
			if (System.getProperty("RELOAD").equals("TRUE"))
				return;
		
		pm.registerEvents(new LGThierceListener(this), this);
		pm.registerEvents(new NightListener(this), this);
		pm.registerEvents(new DayListener(this), this);
		pm.registerEvents(new GameConfig(this), this);
		pm.registerEvents(new SpecExecutor(this), this);
		pm.registerEvents(new StartExecutor(this), this);
		
		getCommand("ankou").setExecutor(new CommandAnkou(this));
		
		
		
		for (Team t : s.getTeams()) {
			if (!t.getDisplayName().startsWith("Kits")) {
				t.unregister();
			}
		}
		for (Objective ob : s.getObjectives()) {
				ob.unregister();
		}
		
		String tabplace = "A";
		String st = "";
		String color = "§f";
		s.registerNewTeam(tabplace + "ADieu" + st);
		s.getTeam(tabplace + "ADieu" + st).setDisplayName("Dieu" + st);
		s.getTeam(tabplace + "ADieu" + st).setPrefix("§c§lDieu. " + color + "§l");
		s.getTeam(tabplace + "ADieu" + st).setSuffix("§d§k§laa§r");
		
		s.registerNewTeam(tabplace + "BDieuM" + st);
		s.getTeam(tabplace + "BDieuM" + st).setDisplayName("DieuM" + st);
		s.getTeam(tabplace + "BDieuM" + st).setPrefix("§5§lDieu. " + color + "§l");
		s.getTeam(tabplace + "BDieuM" + st).setSuffix("§6§k§laa§r");
		
		s.registerNewTeam(tabplace + "CDieuX" + st);
		s.getTeam(tabplace + "CDieuX" + st).setDisplayName("DieuX" + st);
		s.getTeam(tabplace + "CDieuX" + st).setPrefix("§6§lDieu. " + color + "§l");
		s.getTeam(tabplace + "CDieuX" + st).setSuffix("§5§k§laa§r");
		
		s.registerNewTeam(tabplace + "DDieuE" + st);
		s.getTeam(tabplace + "DDieuE" + st).setDisplayName("DieuE" + st);
		s.getTeam(tabplace + "DDieuE" + st).setPrefix("§3§lDieu. " + color + "§l");
		s.getTeam(tabplace + "DDieuE" + st).setSuffix("§0§k§laa§r");
		
		s.registerNewTeam(tabplace + "EDémon" + st);
		s.getTeam(tabplace + "EDémon" + st).setDisplayName("Démon" + st);
		s.getTeam(tabplace + "EDémon" + st).setPrefix("§b§lDémon. " + color + "§l");
		s.getTeam(tabplace + "EDémon" + st).setSuffix("§c§k§laa§r");
		
		s.registerNewTeam(tabplace + "FLeader" + st);
		s.getTeam(tabplace + "FLeader" + st).setDisplayName("Leader" + st);
		s.getTeam(tabplace + "FLeader" + st).setPrefix("§2Leader. " + color + "§l");
		s.getTeam(tabplace + "FLeader" + st).setSuffix("§0§kaa§r");
		
		s.registerNewTeam("AGJoueur");
		s.getTeam("AGJoueur").setDisplayName("Joueur");
		s.getTeam("AGJoueur").setPrefix("§e");
		s.getTeam("AGJoueur").setSuffix("§r");
		
		s.registerNewTeam("RVillageois");
		s.getTeam("RVillageois").setDisplayName("Villageois-Villageois");
		s.getTeam("RVillageois").setPrefix("§a§lV§e-§a§lV §a");
		s.getTeam("RVillageois").setSuffix("§r");
		
		s.registerNewTeam("RPrésident");
		s.getTeam("RPrésident").setDisplayName("Président");
		s.getTeam("RPrésident").setPrefix("§e§lPrésident §6");
		s.getTeam("RPrésident").setSuffix("§r");
		
		rel();
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		System.out.println("LG Disabling");
		
		super.onDisable();
	}
	
	public Gstate getState() {
		return this.state;
	}
	
	public void setState(Gstate state) {
		
		this.state = state;
		
	}
	
	public boolean isState(Gstate state) {
		return this.state == state;
	}
	
	public Gcycle getCycle() {
		return this.cycle;
	}
	
	public void setCycle(Gcycle cycle) {
		
		this.cycle = cycle;
		
	}
	
	public boolean isCycle(Gcycle cycle) {
		return this.cycle == cycle;
	}
	
	public Gtype getType() {
		return this.type;
	}
	
	public void setType(Gtype type) {
		
		this.type = type;
		
	}
	
	public boolean isType(Gtype type) {
		return this.type == type;
	}
	
	
	public DisplayState getDisplayState() {
		return displayState;
	}

	public void setDisplayState(DisplayState displayState) {
		this.displayState = displayState;
	}
	
	public boolean isDisplayState(DisplayState displayState) {
		return this.displayState == displayState;
	}
	
	

	public HashMap<String, List<UUID>> getGrades() { 
		return Grades;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void clearArmor(Player player){
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
	}
	
	public ItemStack getItem(Material type, String name, List<String> lore) {
		ItemStack it = new ItemStack(type);
		ItemMeta itm = it.getItemMeta();
		
		itm.setDisplayName(name);
		if (lore != null) itm.setLore(lore);
		
		it.setItemMeta(itm);
		return it;
	}
	
	
	public void updateGrades() {
		List<UUID> Dieux = new ArrayList<>();
		List<UUID> DieuxM = new ArrayList<>();
		List<UUID> DieuxX = new ArrayList<>();
		List<UUID> DieuE = new ArrayList<>();
		List<UUID> Démons = new ArrayList<>();
		List<UUID> Leaders = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers())  {
		if (p.getUniqueId().toString().equals("0234db8c-e6e5-45e5-8709-ea079fa575bb")) {
			Dieux.add(p.getUniqueId());
		}
		if (p.getUniqueId().toString().equals("a9198cde-e7b0-407e-9b52-b17478e17f90")) {
			DieuxM.add(p.getUniqueId());
		}
		if (p.getUniqueId().toString().equals("290d1443-a362-4f79-b616-893bfb1361e5")) {
			DieuxX.add(p.getUniqueId());
		}
		
		if (p.getUniqueId().toString().equals("9a4d5447-13e0-43a3-87af-977ba87e77a7") || p.getUniqueId().toString().equals("cb067197-d121-4bfc-ac47-d6b4e40841b2")) {
			DieuE.add(p.getUniqueId());
		}
												//Goteix																			//OffColor															//Newiland																			//Kaul																		//Davyre																	//Gwen / TryHardeuse													Eternel
		if (p.getUniqueId().toString().equals("ac5ec348-baf0-4ad9-b8d2-e5fc817414f7") || p.getUniqueId().toString().equals("41ba0eb8-5d55-4aaa-9a57-b6cd9ee6e301") || p.getUniqueId().toString().equals("ada86697-6253-4ccf-9121-9e19c5288cf1") || p.getUniqueId().toString().equals("e08c6ffd-f577-422d-9dcd-a8d22c20be97") || p.getUniqueId().toString().equals("9c9e3477-d951-431b-ac11-344ec8a5a919") || p.getUniqueId().toString().equals("6a29cae9-c3f0-4e9d-8249-90227a1c669a") || p.getUniqueId().toString().equals("3a8ed81d-d284-4b1c-86b9-d835c27b2a33")) {
			Démons.add(p.getUniqueId());
		}
		
		if (p.isOp() && !Dieux.contains(p.getUniqueId()) && !DieuxM.contains(p.getUniqueId()) && !DieuxX.contains(p.getUniqueId()) && !Démons.contains(p.getUniqueId())) {
			Leaders.add(p.getUniqueId());
		}
		}
		Grades.put("Dieu", Dieux);
		Grades.put("DieuM", DieuxM);
		Grades.put("DieuX", DieuxX);
		Grades.put("DieuE", DieuE);
		Grades.put("Démon", Démons);
		Grades.put("Leader", Leaders);
	}
	
	public void setPlayerGrade(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
		String tabplace = "A";
		String gradeplace = "ZBSGEF";
		
		for (Entry<String, List<UUID>> en : getGrades().entrySet()) {
			String g = en.getKey();
			if (g.equalsIgnoreCase("Dieu")) gradeplace = "A";
			if (g.equalsIgnoreCase("DieuM")) gradeplace = "B";
			if (g.equalsIgnoreCase("DieuX")) gradeplace = "C";
			if (g.equalsIgnoreCase("DieuE")) gradeplace = "D";
			if (g.equalsIgnoreCase("Démon")) gradeplace = "E";
			if (g.equalsIgnoreCase("Leader")) gradeplace = "F";
			List<UUID> l = en.getValue();
			String grade = tabplace + gradeplace + g;
			if (l.contains(player.getUniqueId())) {
				s.getTeam(grade).addEntry(player.getName());
				player.setDisplayName(s.getTeam(grade).getPrefix() + player.getName() + s.getTeam(grade).getSuffix());
				player.setPlayerListName(player.getDisplayName());
			}
		}
	}
	
	
	public void setLGScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Scoreboard ms = player.getScoreboard();
		for (Team t : ms.getTeams()) {
			if (!t.getName().equalsIgnoreCase("LG") && !t.getName().startsWith("RSoeur") && !t.getName().startsWith("RFrère") && !t.getName().startsWith("Couple") && !t.getName().startsWith("RMaçon")) {
				s.registerNewTeam(t.getName());
				Team ts = s.getTeam(t.getName());
				ts.setDisplayName(t.getDisplayName());
				ts.setPrefix(t.getPrefix());
				ts.setSuffix(t.getSuffix());
				for (String p : t.getEntries()) ts.addEntry(p);
			}
		}
		
		s.registerNewTeam("LG");
		Team tlg = s.getTeam("LG");
		tlg.setPrefix("§c§lLG §c");
		tlg.setSuffix("§r");
		
		player.setScoreboard(s);
	}
	
	public void setSoeurScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Scoreboard ms = player.getScoreboard();
		for (Team t : ms.getTeams()) {
			if (!t.getName().equalsIgnoreCase("LG") && !t.getName().startsWith("RSoeur") && !t.getName().startsWith("RFrère") && !t.getName().startsWith("Couple") && !t.getName().startsWith("RMaçon")) {
				s.registerNewTeam(t.getName());
				Team ts = s.getTeam(t.getName());
				ts.setDisplayName(t.getDisplayName());
				ts.setPrefix(t.getPrefix());
				ts.setSuffix(t.getSuffix());
				for (String p : t.getEntries()) ts.addEntry(p);
			}
		}
		
		String tname = "RSoeur";
		if (!RoleScoreboard.containsKey(Roles.SOEUR)) {
			RoleScoreboard.put(Roles.SOEUR, 1);
		} else RoleScoreboard.put(Roles.SOEUR, RoleScoreboard.get(Roles.SOEUR) + 1);
		if (RoleScoreboard.get(Roles.SOEUR) != 1) tname = "RSoeur" + RoleScoreboard.get(Roles.SOEUR);
 		
		s.registerNewTeam(tname);
		Team tso = s.getTeam(tname);
		tso.setPrefix(Roles.SOEUR.getDisplayName() + " §d");
		tso.setSuffix("§r");
		
		player.setScoreboard(s);
	}
	
	public void setMaçonScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Scoreboard ms = player.getScoreboard();
		for (Team t : ms.getTeams()) {
			if (!t.getName().equalsIgnoreCase("LG") && !t.getName().startsWith("RSoeur") && !t.getName().startsWith("RFrère") && !t.getName().startsWith("Couple") && !t.getName().startsWith("RMaçon")) {
				s.registerNewTeam(t.getName());
				Team ts = s.getTeam(t.getName());
				ts.setDisplayName(t.getDisplayName());
				ts.setPrefix(t.getPrefix());
				ts.setSuffix(t.getSuffix());
				for (String p : t.getEntries()) ts.addEntry(p);
			}
		}
		
		String tname = "RMaçon";
		if (!RoleScoreboard.containsKey(Roles.MAÇON)) {
			RoleScoreboard.put(Roles.MAÇON, 1);
		} else RoleScoreboard.put(Roles.MAÇON, RoleScoreboard.get(Roles.MAÇON) + 1);
		if (RoleScoreboard.get(Roles.MAÇON) != 1) tname = "RSoeur" + RoleScoreboard.get(Roles.MAÇON);
 		
		s.registerNewTeam(tname);
		Team tso = s.getTeam(tname);
		tso.setPrefix(Roles.MAÇON.getDisplayName() + " §6");
		tso.setSuffix("§r");
		
		player.setScoreboard(s);
	}
	
	public void setFrèreScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Scoreboard ms = player.getScoreboard();
		for (Team t : ms.getTeams()) {
			if (!t.getName().equalsIgnoreCase("LG") && !t.getName().startsWith("RSoeur") && !t.getName().startsWith("RFrère") && !t.getName().startsWith("Couple") && !t.getName().startsWith("RMaçon")) {
				s.registerNewTeam(t.getName());
				Team ts = s.getTeam(t.getName());
				ts.setDisplayName(t.getDisplayName());
				ts.setPrefix(t.getPrefix());
				ts.setSuffix(t.getSuffix());
				for (String p : t.getEntries()) ts.addEntry(p);
			}
		}
		
		String tname = "RFrère";
		if (!RoleScoreboard.containsKey(Roles.FRÈRE)) {
			RoleScoreboard.put(Roles.FRÈRE, 1);
		} else RoleScoreboard.put(Roles.FRÈRE, RoleScoreboard.get(Roles.FRÈRE) + 1);
		if (RoleScoreboard.get(Roles.FRÈRE) != 1) tname = "RFrère" + RoleScoreboard.get(Roles.FRÈRE);
 		
		s.registerNewTeam(tname);
		Team tfrr = s.getTeam(tname);
		tfrr.setPrefix(Roles.FRÈRE.getDisplayName() + " §d");
		tfrr.setSuffix("§r");
		
		player.setScoreboard(s);
	}
	
	public void setCoupleScoreboard(Player player) {
		Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
		Scoreboard ms = player.getScoreboard();
		for (Team t : ms.getTeams()) {
			if (!t.getName().equalsIgnoreCase("LG") && !t.getName().startsWith("RSoeur") && !t.getName().startsWith("RFrère") && !t.getName().startsWith("Couple") && !t.getName().startsWith("RMaçon")) {
				s.registerNewTeam(t.getName());
				Team ts = s.getTeam(t.getName());
				ts.setDisplayName(t.getDisplayName());
				ts.setPrefix(t.getPrefix());
				ts.setSuffix(t.getSuffix());
				for (String p : t.getEntries()) ts.addEntry(p);
			}
		}
		
		String tname = "Couple";
		if (!RoleScoreboard.containsKey(Roles.CUPIDON)) {
			RoleScoreboard.put(Roles.CUPIDON, 1);
		} else RoleScoreboard.put(Roles.CUPIDON, RoleScoreboard.get(Roles.CUPIDON) + 1);
		if (RoleScoreboard.get(Roles.CUPIDON) != 1) tname = "Couple" + RoleScoreboard.get(Roles.CUPIDON);
 		
		s.registerNewTeam(tname);
		Team tc = s.getTeam(tname);
		tc.setPrefix("§d§lCouple §d");
		tc.setSuffix("§r");
		
		player.setScoreboard(s);
	}	
	
	
	public void initialiseRoles() {
		for (RDeck deck : RDeck.values()) {
			List<Roles> roles = new ArrayList<>();
			
			for (Roles r : Roles.values())
				if (r.getDeck().equals(deck)) roles.add(r);
			
			DeckRoles.put(deck, roles);
		}
	}
	
	
	public void initialiseBeds() {
		World w = Bukkit.getWorld("LG");
		BedList.add(w.getBlockAt(364, 79, 605));
		BedList.add(w.getBlockAt(347, 83, 633));
		BedList.add(w.getBlockAt(343, 83, 636));
		BedList.add(w.getBlockAt(344, 87, 640));
		BedList.add(w.getBlockAt(345, 87, 640));
		BedList.add(w.getBlockAt(331, 85, 645));
		BedList.add(w.getBlockAt(337, 88, 649));
		BedList.add(w.getBlockAt(360, 83, 647));
		BedList.add(w.getBlockAt(357, 87, 643));
		BedList.add(w.getBlockAt(359, 86, 660));
		BedList.add(w.getBlockAt(355, 82, 662));
		BedList.add(w.getBlockAt(351, 84, 684));
		BedList.add(w.getBlockAt(351, 80, 686)); // 13
		BedList.add(w.getBlockAt(373, 83, 635));
		BedList.add(w.getBlockAt(373, 83, 634));
		BedList.add(w.getBlockAt(376, 86, 661));
		BedList.add(w.getBlockAt(384, 82, 661));
		BedList.add(w.getBlockAt(367, 82, 678));
		BedList.add(w.getBlockAt(371, 85, 673));
		BedList.add(w.getBlockAt(371, 80, 577)); //20
		BedList.add(w.getBlockAt(345, 87, 640));
		BedList.add(w.getBlockAt(391, 86, 602));
		BedList.add(w.getBlockAt(387, 87, 623));
		BedList.add(w.getBlockAt(394, 84, 625));
		BedList.add(w.getBlockAt(391, 83, 642));
		BedList.add(w.getBlockAt(392, 83, 642)); // 26
	}
	public void initialiseNightBlocks() {
		World w = Bukkit.getWorld("LG");
		
		BlockList.put(w.getBlockAt(112, 16, 813), 1);
		BlockList.put(w.getBlockAt(109, 16, 812), 2);
		BlockList.put(w.getBlockAt(106, 16, 809), 3);
		BlockList.put(w.getBlockAt(105, 16, 806), 4);
		BlockList.put(w.getBlockAt(106, 16, 803), 5);
		BlockList.put(w.getBlockAt(109, 16, 800), 6);
		BlockList.put(w.getBlockAt(112, 16, 799), 7);
		BlockList.put(w.getBlockAt(115, 16, 800), 8);
		BlockList.put(w.getBlockAt(118, 16, 803), 9);
		BlockList.put(w.getBlockAt(119, 16, 806), 10);
		BlockList.put(w.getBlockAt(118, 16, 809), 11);
		BlockList.put(w.getBlockAt(115, 16, 812), 12);
	}
	
	
	public void fillCalledRoles() {
		
		if (AliveRoles.containsKey(Roles.VOLEUR) && this.nights == 1) addAFillCalledRole(Roles.VOLEUR);
		if (AliveRoles.containsKey(Roles.CUPIDON) && this.nights == 1) addAFillCalledRole(Roles.CUPIDON);
		if (AliveRoles.containsKey(Roles.ENFANT_SAUVAGE) && this.nights == 1) addAFillCalledRole(Roles.ENFANT_SAUVAGE);
		if (AliveRoles.containsKey(Roles.CHIEN_LOUP) && this.nights == 1) addAFillCalledRole(Roles.CHIEN_LOUP);
		if (AliveRoles.containsKey(Roles.JUMEAU) && this.nights == 1) addAFillCalledRole(Roles.JUMEAU);
		if (AliveRoles.containsKey(Roles.NOCTAMBULE)) addAFillCalledRole(Roles.NOCTAMBULE);
		if (AliveRoles.containsKey(Roles.COMÉDIEN)) 
			if (!pouvoirsComédien.isEmpty()) addAFillCalledRole(Roles.COMÉDIEN);
		if (AliveRoles.containsKey(Roles.VOYANTE)) addAFillCalledRole(Roles.VOYANTE);
		if (AliveRoles.containsKey(Roles.VOYANTE_D$AURA)) addAFillCalledRole(Roles.VOYANTE_D$AURA);
		if (AliveRoles.containsKey(Roles.ENCHANTEUR)) addAFillCalledRole(Roles.ENCHANTEUR);
		if (AliveRoles.containsKey(Roles.DÉTECTIVE)) addAFillCalledRole(Roles.DÉTECTIVE);
		if (AliveRoles.containsKey(Roles.RENARD)) addAFillCalledRole(Roles.RENARD);
		if (AliveRoles.containsKey(Roles.PACIFISTE)) addAFillCalledRole(Roles.PACIFISTE);
		if (AliveRoles.containsKey(Roles.FILLE_DE_JOIE)) addAFillCalledRole(Roles.FILLE_DE_JOIE);
		if (AliveRoles.containsKey(Roles.GARDE_DU_CORPS)) addAFillCalledRole(Roles.GARDE_DU_CORPS);
		if (AliveRoles.containsKey(Roles.SALVATEUR)) addAFillCalledRole(Roles.SALVATEUR);
		if (AliveRoles.containsKey(Roles.LOUP_GAROU)) {
			addAFillCalledRole(Roles.LOUP_GAROU);
		} else {
			if (AliveRoles.containsKey(Roles.INFECT_PÈRE_DES_LOUPS) && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
			if (AliveRoles.containsKey(Roles.GRAND_MÉCHANT_LOUP) && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
			if (AliveRoles.containsKey(Roles.LOUP_GAROU_BLANC) && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
			if (AliveRoles.containsKey(Roles.CHIEN_LOUP))
				for (Player player : this.getPlayersByRole(Roles.CHIEN_LOUP))
					if (this.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
			if (AliveRoles.containsKey(Roles.ENFANT_SAUVAGE))
				for (Player player : this.getPlayersByRole(Roles.ENFANT_SAUVAGE))
					if (this.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
			if (AddedRoles.containsKey(Roles.INFECT_PÈRE_DES_LOUPS))
				for (Player player : this.players)
					if (this.playerlg.get(player.getName()).isInfected() && !this.CalledRoles.contains(Roles.LOUP_GAROU)) addAFillCalledRole(Roles.LOUP_GAROU);
		}
		if (AliveRoles.containsKey(Roles.INFECT_PÈRE_DES_LOUPS)) addAFillCalledRole(Roles.INFECT_PÈRE_DES_LOUPS);
		if (AliveRoles.containsKey(Roles.GRAND_MÉCHANT_LOUP)) {
			boolean isAllLGAlive = true;
			for (Entry<String, PlayerLG> en : this.playerlg.entrySet())
				if (en.getValue().isCamp(RCamp.LOUP_GAROU) || en.getValue().isCamp(RCamp.LOUP_GAROU_BLANC))
					if (!en.getValue().isVivant()) isAllLGAlive = false;
			if (isAllLGAlive) addAFillCalledRole(Roles.GRAND_MÉCHANT_LOUP);
		}
			
		if (AliveRoles.containsKey(Roles.LOUP_GAROU_BLANC)) 
			if (this.nights % 2 == 0) addAFillCalledRole(Roles.LOUP_GAROU_BLANC);
		if (AliveRoles.containsKey(Roles.PETITE_FILLE2)) addAFillCalledRole(Roles.PETITE_FILLE2);
		if (AliveRoles.containsKey(Roles.SORCIÈRE)) addAFillCalledRole(Roles.SORCIÈRE);
		if (AliveRoles.containsKey(Roles.PRÊTRE)) addAFillCalledRole(Roles.PRÊTRE);
		if (AliveRoles.containsKey(Roles.NÉCROMANCIEN)) addAFillCalledRole(Roles.NÉCROMANCIEN);
		if (AliveRoles.containsKey(Roles.VILAIN_GARÇON)) addAFillCalledRole(Roles.VILAIN_GARÇON);
		if (AliveRoles.containsKey(Roles.DICTATEUR)) addAFillCalledRole(Roles.DICTATEUR);
		if (AliveRoles.containsKey(Roles.MAMIE_GRINCHEUSE)) addAFillCalledRole(Roles.MAMIE_GRINCHEUSE);
		if (AliveRoles.containsKey(Roles.CORBEAU)) addAFillCalledRole(Roles.CORBEAU);
		if (AliveRoles.containsKey(Roles.JOUEUR_DE_FLÛTE)) addAFillCalledRole(Roles.JOUEUR_DE_FLÛTE);
		if (AliveRoles.containsKey(Roles.PYROMANE)) addAFillCalledRole(Roles.PYROMANE);
		if (AliveRoles.containsKey(Roles.SOEUR)) addAFillCalledRole(Roles.SOEUR);
		if (AliveRoles.containsKey(Roles.FRÈRE)) addAFillCalledRole(Roles.FRÈRE);
		
		System.out.println(this.CalledRoles.toString());
	}
	
	private void addAFillCalledRole(Roles r) {
		int i = 1;
		if (!r.equals(Roles.LOUP_GAROU)) i = AliveRoles.get(r);
		if (r.equals(Roles.SOEUR) || r.equals(Roles.FRÈRE)) i = i/ 2;
		
		while (i != 0) {
			CalledRoles.add(r);
			i--;
		}
	}
	
	
	public List<Player> getPlayersByRole(Roles r) {
		List<Player> lp = new ArrayList<Player>();
		if (!this.isState(Gstate.PLAYING)) throw new NullPointerException("Les rôles ne sont pas distribués");
		
		for (Player player : this.players) {
			if (this.playerlg.get(player.getName()).getRole().equals(r)) lp.add(player);
		}
		
		return lp;
	}
	
	
	public String getPlayerNameByAttributes(Player player, Player receveier) {
		String name = "";
		PlayerLG plg = this.playerlg.get(player.getName());
		PlayerLG rlg = this.playerlg.get(receveier.getName());
		
		if (rlg.isCamp(RCamp.LOUP_GAROU) || rlg.isCamp(RCamp.LOUP_GAROU_BLANC)) 
			if (plg.isCamp(RCamp.LOUP_GAROU) || plg.isCamp(RCamp.LOUP_GAROU_BLANC))
				if (receveier.getScoreboard().getTeam("LG").hasEntry(player.getName()))
					name = "§c§lLG §c";
		if (rlg.isRole(Roles.SOEUR))
			if (plg.isRole(Roles.SOEUR))
				name = name + "§d§lSoeur §d";
		if (rlg.isRole(Roles.FRÈRE))
			if (plg.isRole(Roles.FRÈRE))
				name = name + "§3§lFrère §3";
		if (plg.isRole(Roles.VILLAGEOIS_VILLAGEOIS))
			name = name + "§a§lV§e-§a§lV §a";
		if (plg.isMaire()) name = name + "§6§lMaire §e";
		if (!plg.getCouple().isEmpty())
			if (plg.getCouple().get(0).equals(receveier)) name = name + "§d§lCou§9§lple §d";
		if (rlg.isRole(Roles.MERCENAIRE) && days == 1 && !plg.getTargetOf().isEmpty())
			if (plg.getTargetOf().get(0).equals(player))
				name = name + "§c§lCible §5";
		if (rlg.isRole(Roles.JUMEAU) && !plg.getJumeauOf().isEmpty())
			if (plg.getJumeauOf().contains(player))
				name = name + "§5§lJumeau §d";
		
		name = name + player.getName();
		return name;
	}
	
	
	public ItemStack getRoleMap(Roles r) {
		ItemStack it = null;
		ItemMeta itm = null;
		List<Block> chests = new ArrayList<Block>();
		chests.add(Bukkit.getWorld("LG").getBlockAt(442, 11, 510));
		chests.add(Bukkit.getWorld("LG").getBlockAt(439, 11, 510));

		for (Block block : chests) {
			org.bukkit.block.Chest chest = (org.bukkit.block.Chest)  block.getState();
	
			Inventory inventory = chest.getInventory();

			for (ItemStack its : inventory.getContents()) {
				if (its != null)  {
					if (its.hasItemMeta()) {
						if (its.getItemMeta().getDisplayName().equalsIgnoreCase(r.getName())) {
							it = new ItemStack(its.getType(), its.getAmount(), its.getDurability());
							itm = its.getItemMeta();
						}
					}
				}
			}
		}
		
		it.setItemMeta(itm);
		return it;
	}
	
	
	public void sendRoleMessage(String message, Roles r) {
		List<Player> receviers = new ArrayList<Player>();
		receviers = getPlayersByRole(r);
		if (r.equals(Roles.LOUP_GAROU)) {
			receviers.clear();
			for (Player player : this.players) {
				if (this.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) || this.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
					receviers.add(player);
				if (this.playerlg.get(player.getName()).isRole(Roles.ENCHANTEUR) && this.isDisplayState(DisplayState.NUIT_LG))
					receviers.remove(player);
			}
		}
		
		for (Player player : receviers) {
			player.sendMessage(message);
		}
	}
	
	
	
	public void rel() {
		players.clear();
		spectators.clear();
		playerlg.clear();
		BedList.clear();
		BlockList.clear();
		nights = 0;
		days = 0;
		CalledRoles.clear();
		AddedRoles.clear();
		AliveRoles.clear();
		RolesVoleur.clear();
		SosoKillPots.clear();
		SosoRézPots.clear();
		sleepingPlayers.clear();
		lastSalvaté.clear();
		GRunnable = null;
		
		updateGrades();
		for (Team t : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
			if (!t.getName().startsWith("Kit") && !t.getName().equalsIgnoreCase("Teams")) {
				for(String offp : t.getEntries()) {
					t.removeEntry(offp);
				}
			}
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.getInventory().clear();
			p.setExp(0f);
			p.setLevel(0);
			p.setMaxHealth(20);
			p.setHealth(20);
			for (PotionEffect pe : p.getActivePotionEffects()) {
				p.removePotionEffect(pe.getType());
			}
			PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false);
			p.addPotionEffect(saturation);
			clearArmor(p);
			p.setDisplayName(p.getName());
			p.setPlayerListName(p.getName());
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport( new Location(Bukkit.getWorld("LG"), 494, 12.2, 307, 0f, 0f)); //494 12 307
			
			p.getInventory().setItem(1, getItem(Material.GHAST_TEAR, "§7§lDevenir Spectateur", Arrays.asList("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit")));
			p.getInventory().setItem(5, getItem(Material.EYE_OF_ENDER, "§a§lJouer", Arrays.asList("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit")));
			
			p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AGJoueur").addEntry(p.getName());
			p.setDisplayName(p.getScoreboard().getEntryTeam(p.getName()).getPrefix() + p.getName() + p.getScoreboard().getEntryTeam(p.getName()).getSuffix());
			p.setPlayerListName(p.getDisplayName());
			setPlayerGrade(p);
			for (Player player : Bukkit.getOnlinePlayers()) p.showPlayer(player);
			this.playerlg.put(p.getName(), new PlayerLG(this, p));
			
			Bukkit.getWorld("LG").setTime(0);
		}
		
		setCycle(Gcycle.NONE);
		setState(Gstate.PREPARING);
		setType(Gtype.NONE);
		initialiseRoles();
		config.startConfig();
		Bukkit.getScheduler().cancelTasks(this);
		
		for (Player p : Bukkit.getOnlinePlayers())
			updateScoreboard(p);
	}
	
	public void updateScoreboard(Player p) {
		SimpleScoreboard ss = new SimpleScoreboard(getPrefix(), p);
		ss.add(p.getDisplayName(), 15);
		ss.add("§0", 14);
		ss.add("§6Nombre de §lRôles §f: §e" + this.AddedRoles.size(), 13);
		ss.add("§aNombre de §lJoueurs §f: §e" + this.players.size(), 12);
		ss.add("§7Joueurs en ligne §f: §e" + Bukkit.getOnlinePlayers().size(), 11);
		ss.add("§2§lType §2de jeu §f: §c" + this.getType().toString(), 10);
		ss.add("§7", 9);
		ss.add("§8------------", 8);
		ss.add("§e§oMap by §c§l§oNeyuux_", 7);
		p.setScoreboard(ss.getScoreboard());
	}



	public static void setPlayerTabList(Player player,String header, String footer) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabTitle);
		try {
			Field field = headerPacket.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(headerPacket, tabFoot);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.sendPacket(headerPacket);
		}
	}
	
	
	
	private void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void sendTitle(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        try {
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + title + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), chatTitle,
                    fadeInTime, showTime, fadeOutTime);

            Object chatsTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + subtitle + "\"}");
            Constructor<?> timingTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
                    int.class, int.class, int.class);
            Object timingPacket = timingTitleConstructor.newInstance(
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), chatsTitle,
                    fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packet);
            sendPacket(player, timingPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void sendTitleForAllPlayers(String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
    	for (Player p : Bukkit.getOnlinePlayers()) sendTitle(p, title, subtitle, fadeInTime, showTime, fadeOutTime);
    }
    
    
    public static void sendActionBarForAllPlayers(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendActionBar(p, message);
		}
	}
    
    
    public static void sendActionBar(Player p, String message) {
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
    }
	
	
}
