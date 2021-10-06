package fr.neyuux.refont.old.lg.listeners;

import fr.neyuux.refont.old.lg.*;
import fr.neyuux.refont.old.lg.role.RCamp;
import fr.neyuux.refont.old.lg.role.Roles;
import fr.neyuux.refont.old.lg.task.NightRunnable;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class NightListener implements Listener {
	
	private final LG main;
	private final HashMap<Player, Player> firstCupidonChoice = new HashMap<>();
	private final HashMap<Player, Player> firstD�tectiveChoice = new HashMap<>();
	private final HashMap<Player, Player> firstVilainGar�onChoice = new HashMap<>();
	private final List<Player> ActionsSosos = new ArrayList<>();
	private final List<Player> JDFHadCharmedSomeone = new ArrayList<>();
	private final List<Player> PyroHadChoosedSomeone = new ArrayList<>();
	
	public NightListener(LG main) {
		this.main = main;
	}
	
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent ev) {
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (!main.players.contains(ev.getPlayer())) return;
		
		if (!main.playerlg.get(ev.getPlayer().getName()).getCouple().isEmpty())
			if (ev.getMessage().startsWith("!"))
				main.playerlg.get(ev.getPlayer().getName()).getCouple().get(0).sendMessage(main.getPlayerNameByAttributes(ev.getPlayer(), main.playerlg.get(ev.getPlayer().getName()).getCouple().get(0)) + main.SendArrow + ev.getMessage().substring(1));
		
		ev.setCancelled(true);
	}
	
	
	
	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent ev) {
		if (!main.isCycle(Gcycle.JOUR) && !main.isType(Gtype.R�UNION)) return;
		if (!main.isState(Gstate.PLAYING)) return;
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		Player player = ev.getPlayer();
		if (!main.players.contains(player)) return;
		PlayerLG playerlg = main.playerlg.get(player.getName());
		
		if (main.sleepingPlayers.contains(player)) return;
		if (playerlg.hasUsedPower() && !main.isDisplayState(DisplayState.NUIT_LG)) return;
		
		 if (ev.getAnimationType() == PlayerAnimationType.ARM_SWING) {
			 
			 if (main.isDisplayState(DisplayState.NUIT_CUPIDON)) {
				 if (main.cupiEnCouple) firstCupidonChoice.put(player, player);
					
					if (!firstCupidonChoice.containsKey(player)) {
						Player p = playerlg.getPlayerOnCursor(main.players);
						if (p == null) return;
						firstCupidonChoice.put(player, p);
						player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					} else {
						Player p1 = firstCupidonChoice.get(player);
						List<Player> choosable = new ArrayList<>();
						for (Player p : main.players)
							if (!p.getName().equals(p1.getName()))
								choosable.add(p);
						Player p2 = playerlg.getPlayerOnCursor(choosable);
						if (p2 == null) return;
						player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
						
						PlayerLG p1lg = main.playerlg.get(p1.getName());
						PlayerLG p2lg = main.playerlg.get(p2.getName());
						p1lg.addCouple(p2);
						p2lg.addCouple(p1);
						firstCupidonChoice.remove(player);
						
						Team t = null;
						for (Team team : player.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t = team;
						Team t1 = null;
						for (Team team : p1.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t1 = team;
						Team t2 = null;
						for (Team team : p2.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t2 = team;
						
						t1.addEntry(p2.getName());
						t1.addEntry(p1.getName());
						t2.addEntry(p1.getName());
						t2.addEntry(p2.getName());
						t.addEntry(p1.getName());
						t.addEntry(p2.getName());
						
						
						p1.sendMessage(main.getPrefix() + main.SendArrow + "�dVous recevez soudainement une fl�che, elle vous transperce. Regardant au loin, vous apercevez �5" + p2.getName() + " �d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " �dpour avoir fait ce choix. �r\n�dVous �tes amoureux de �5" + p2.getName() + " �d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
						p1.sendMessage(main.getPrefix() + main.SendArrow + "�9Utilisez �e! �9pour lui parler de mani�re priv�e.");
						p2.sendMessage(main.getPrefix() + main.SendArrow + "�dVous recevez soudainement une fl�che, elle vous transperce. Regardant au loin, vous apercevez �5" + p1.getName() + " �d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " �dpour avoir fait ce choix. �r\n�dVous �tes amoureux de �5" + p1.getName() + " �d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
						p2.sendMessage(main.getPrefix() + main.SendArrow + "�9Utilisez �e! �9pour lui parler de mani�re priv�e.");
						
						player.closeInventory();
						main.playerlg.get(player.getName()).setHasUsedPower(true);
						player.sendMessage(main.getPrefix() + main.SendArrow + "�dVos 2 fl�ches ont bien transperc� �5" + main.getPlayerNameByAttributes(p1, player) + " �det �5" + main.getPlayerNameByAttributes(p2, player) + "�d. Ils ne se quitteront plus d�sormais...");
					}
			 } else if (main.isDisplayState(DisplayState.NUIT_ES)) {
				List<Player> choosable = new ArrayList<>();
				for (Player p : main.players)
					if (!p.getUniqueId().equals(player.getUniqueId()))
						choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
					
				main.playerlg.get(p.getName()).addmaitre(player);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous avez bien choisi �6" + main.getPlayerNameByAttributes(p, player) + "�e en mod�le.");
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			 } else if (main.isDisplayState(DisplayState.NUIT_JUMEAU)) {
				List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.getJumeauOf().add(player);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous avez bien choisi �5�l" + main.getPlayerNameByAttributes(p, player) + " �dcomme Jumeau.");
				
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_NOCTA)) {
				List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());

				plg.setNoctaTargeted(true);
				p.sendMessage(main.getPrefix() + main.SendArrow + "�9Le " + Roles.NOCTAMBULE.getDisplayName() + " �1\"�b" + main.getPlayerNameByAttributes(player, p) + "�1\"�9 a d�cid� de dormir chez vous ! �cSi vous aviez un pouvoir de nuit, vous le perdez pour ce tour.");
				p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 9, 1);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�9Vous avez dormi chez �b" + main.getPlayerNameByAttributes(p, player) + "�9.");
				
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_VOVO)) {
				List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dTu d�couvres dans ton boule magique que �5" + main.getPlayerNameByAttributes(p, player) + "�d est " + plg.getRole().getDisplayName() + "�d.");
				if (main.voyanteBavarde) {
					Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "�dLa " + Roles.VOYANTE.getDisplayName() + " �da observ� un joueur qui est " + plg.getRole().getDisplayName() + " �d!");
					for (Player pl : main.players)
						if (!pl.getUniqueId().equals(player.getUniqueId()))
							pl.playSound(pl.getLocation(), Sound.PORTAL, 6, 2f);
				}
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_VOVO_D$AURA)) {
				List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				boolean isLG = false;
				if (plg.isCamp(RCamp.LOUP_GAROU) || plg.isCamp(RCamp.LOUP_GAROU_BLANC)) isLG = true;
				
				if (isLG)
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dTu d�couvres dans ton boule magique que �5" + main.getPlayerNameByAttributes(p, player) + "�d �cfait parti des �c�lLoups-Garous�d.");
				else
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dTu d�couvres dans ton boule magique que �5" + main.getPlayerNameByAttributes(p, player) + "�d �ane fait pas parti des �a�lLoups-Garous�d.");
				if (main.voyanteBavarde) {
					if (isLG)
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "�dLa " + Roles.VOYANTE_D$AURA.getDisplayName() + " �da observ� un joueur qui �cfait parti des �c�lLoups-Garous �d!");
					else
						Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "�dLa " + Roles.VOYANTE_D$AURA.getDisplayName() + " �da observ� un joueur qui �ane fait pas parti des �a�lLoups-Garous �d!");
					for (Player pl : main.players)
						if (!pl.getUniqueId().equals(player.getUniqueId()))
							pl.playSound(pl.getLocation(), Sound.PORTAL, 6, 2f);
				}
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_ENCHANT)) {
				List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (plg.isCamp(RCamp.LOUP_GAROU) || plg.isCamp(RCamp.LOUP_GAROU_BLANC)) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dfait parti des �c�lLoups-Garous�d.");
					player.getScoreboard().getTeam("LG").addEntry(p.getName());
				} else if (plg.isRole(Roles.VOYANTE))
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dest une "+Roles.VOYANTE.getDisplayName()+"�d.");
				else
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dest �cni Loup, ni Voyante�d.");
				
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				playerlg.setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_D�TEC)) {
				 List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()) && !firstD�tectiveChoice.containsValue(p))
							choosable.add(p);
				if (!firstD�tectiveChoice.containsKey(player)) {
					Player p = playerlg.getPlayerOnCursor(choosable);
					if (p == null) return;
					firstD�tectiveChoice.put(player, p);
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				} else {
					Player p1 = firstD�tectiveChoice.get(player);
					Player p2 = playerlg.getPlayerOnCursor(choosable);
					if (p2 == null) return;
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					
					PlayerLG p1lg = main.playerlg.get(p1.getName());
					PlayerLG p2lg = main.playerlg.get(p2.getName());
					firstD�tectiveChoice.remove(player);
					
					boolean sameCamp = Boolean.FALSE;
					if (p1lg.isCamp(p2lg.getCamp()) || p1lg.getCouple().contains(p2))
						sameCamp = true;
					
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
					if (sameCamp)
						player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�couvrez que �f�l" + main.getPlayerNameByAttributes(p1, player) + " �7et �f�l" + main.getPlayerNameByAttributes(p2, player) + " �aappartiennent au m�me camp�7.");
					else
						player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�couvrez que �f�l" + main.getPlayerNameByAttributes(p1, player) + " �7et �f�l" + main.getPlayerNameByAttributes(p2, player) + " �cn'appartiennent pas au m�me camp�7.");
					}
			 } else if (main.isDisplayState(DisplayState.NUIT_RENARD)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				Player p2 = plg.get2NearestPlayers().get(0);
				Player p3 = plg.get2NearestPlayers().get(1);
				
				boolean thereALG = false;
				if (main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p2.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p3.getName()).isCamp(RCamp.LOUP_GAROU)) thereALG = true;
				if (main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(p2.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(p3.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) thereALG = true;
			
				if (thereALG) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous d�couvrez qu'entre �6" + main.getPlayerNameByAttributes(p, player) + "�e, �6" + main.getPlayerNameByAttributes(p2, player) + "�e et �6" + main.getPlayerNameByAttributes(p3, player) + " �ese trouve un �c�lLoup-Garou�e.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �agardez �fvotre pouvoir.");
				} else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous d�couvrez qu'entre �6" + main.getPlayerNameByAttributes(p, player) + "�e, �6" + main.getPlayerNameByAttributes(p2, player) + "�e et �6" + main.getPlayerNameByAttributes(p3, player) + " �ene se trouve �aaucun �eLoups.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �cperdez �fvotre pouvoir.");
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
					player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				}
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_PACIF)) {
				 	List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
					Player p = playerlg.getPlayerOnCursor(choosable);
					if (p == null) return;
					PlayerLG plg = main.playerlg.get(p.getName());
					
					plg.setPacifTargeted(true);
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dLe r�le de �5�l" + main.getPlayerNameByAttributes(p, player) + " �dva �tre r�v�l� au d�but du jour.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �cperdez �fvotre pouvoir.");
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
					player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				 } else if (main.isDisplayState(DisplayState.NUIT_FDJ)) {
					Player p = playerlg.getPlayerOnCursor(main.players);
					if (p == null) return;
					
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous d�cidez de dormir chez �5" + main.getPlayerNameByAttributes(p, player) + "�d pour cette nuit. Si vous vous faites attaquer par les Loups, vous survivrez, mais, s'il est Loup ou attaqu� par les Loups, vous mourrez.");
					playerlg.setOtherPlayerHouse(p);
						
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			 } else if (main.isDisplayState(DisplayState.NUIT_GDC)) {
				 	List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()))
							choosable.add(p);
					Player p = playerlg.getPlayerOnCursor(choosable);
					if (p == null) return;
					PlayerLG plg = main.playerlg.get(p.getName());
					
					player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�7 pour cette nuit. S'il est cens� mourir pendant cette nuit, il survivra.");
					plg.setProtected(player);
					playerlg.setHasUsedPower(true);
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				 } else if (main.isDisplayState(DisplayState.NUIT_SALVA)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (!main.lastSalvat�.containsKey(player)) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�f pour cette nuit. S'il se fait attaquer par les loups, il survivra.");
					plg.setSalvation(true);
					playerlg.setHasUsedPower(true);
				} else {
					if (main.lastSalvat�.get(player).equals(p)) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous ne pouvez pas prot�ger deux fois de suite la m�me personne !");
					} else {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�f pour cette nuit. S'il se fait attaquer par les loups, il survivra.");
						plg.setSalvation(true);
						playerlg.setHasUsedPower(true);
					}
				}
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				main.lastSalvat�.put(player, p);
			 } else if (main.isDisplayState(DisplayState.NUIT_LG)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (playerlg.getVotedPlayer() != null) {
					 if (!playerlg.getVotedPlayer().getUniqueId().equals(p.getUniqueId())) {
						main.sendRoleMessage(main.getPrefix() + main.SendArrow + "�c" + player.getName() + " �6vote pour d�vorer �e" + p.getName(), Roles.LOUP_GAROU);
						plg.addVote();

						 if (playerlg.getVotedPlayer() != null) {
							 Player vp = playerlg.getVotedPlayer();
							 main.playerlg.get(vp.getName()).removeVote();
						 }
						 playerlg.setVote(p);
					 } else {
						main.playerlg.get(playerlg.getVotedPlayer().getName()).removeVote();
						playerlg.setVote(null);
						main.sendRoleMessage(main.getPrefix() + main.SendArrow + "�c" + player.getName() + " �6a annul� son vote.", Roles.LOUP_GAROU);
					}
				 } else {
					main.sendRoleMessage(main.getPrefix() + main.SendArrow + "�c" + player.getName() + " �6vote pour d�vorer �e" + p.getName(), Roles.LOUP_GAROU);
					plg.addVote();

					if (playerlg.getVotedPlayer() != null) {
						Player vp = playerlg.getVotedPlayer();
						main.playerlg.get(vp.getName()).removeVote();
					}
					playerlg.setVote(p);
				}
				
				List<Player> lgs = new ArrayList<>();
				int votes = 0;
				for (Player lgp : main.players)
					if (main.playerlg.get(lgp.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(lgp.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) lgs.add(lgp);
				for (Player lgp : lgs)
					if (main.playerlg.get(lgp.getName()).getVotedPlayer() != null) votes++;
				if (votes == lgs.size())
					for (Player lgp : lgs) main.playerlg.get(lgp.getName()).setHasUsedPower(true);
			 } else if (main.isDisplayState(DisplayState.NUIT_GML)) {
				List<Player> choosable = new ArrayList<>();
				for (Player p : main.players)
					if (!main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) && !main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
						choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setGMLTargeted(true);
				playerlg.setHasUsedPower(true);
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�cN'�tant pas r�pu de votre pr�c�dent repas, vous avez d�vor� �4" + main.getPlayerNameByAttributes(p, player) + "�c...");
			 } else if (main.isDisplayState(DisplayState.NUIT_LGB)) {
				List<Player> choosable = new ArrayList<>();
				for (Player p : main.players)
					if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
						choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setLGBTargeted(true);
				playerlg.setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�cD'un grand coup de griffe, vous poignardez �4" + p.getName() + "�c : il meurt sur le coup...");
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			 } else if (main.isDisplayState(DisplayState.NUIT_PR�TRE)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setPretreThrower(player);
				playerlg.setHasUsedPower(true);
				playerlg.setUsedDefinitivePower(true);
				player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				player.sendMessage(main.getPrefix() + main.SendArrow + "�f" + p.getName() + " �ca re�u de l'eau b�nite.");
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				} else if (main.isDisplayState(DisplayState.NUIT_N�CRO)) {
				Location loc = player.getLocation();
				EntityPlayer fakeplayer = null;
				for (int i = 0; i < 50; i++) {
				      loc.add(loc.getDirection());
				      for (EntityPlayer p : NightRunnable.fakeplayers.get(player)) {
				    	  if (p.getId() != player.getEntityId() && PlayerLG.distanceSquaredXZ(loc, new Location(player.getWorld(), p.locX, p.locY, p.locZ)) < 0.35D && Math.abs(loc.getY() - p.locY) < 2.0D)
				        	fakeplayer = p;
				     }
				}
				if (fakeplayer == null) return;
				Player p = Bukkit.getPlayer(fakeplayer.getName());
				if (p == null) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�1" + fakeplayer.getName() + " �cest d�connect� !");
					return;
				}
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�1�l" + main.getPlayerNameByAttributes(p, player) + " �9sera r�ssucit� au d�but du jour !");
				plg.setN�croTargeted(true);
				
				playerlg.setHasUsedPower(true);
				playerlg.setUsedDefinitivePower(true);
				player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			 } else if (main.isDisplayState(DisplayState.NUIT_VG)) {
				 List<Player> choosable = new ArrayList<>();
					for (Player p : main.players)
						if (!p.getUniqueId().equals(player.getUniqueId()) && !firstVilainGar�onChoice.containsValue(p))
							choosable.add(p);
				if (!firstVilainGar�onChoice.containsKey(player)) {
					Player p = playerlg.getPlayerOnCursor(choosable);
					if (p == null) return;
					firstVilainGar�onChoice.put(player, p);
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				} else {
					Player p1 = firstVilainGar�onChoice.get(player);
					Player p2 = playerlg.getPlayerOnCursor(choosable);
					if (p2 == null) return;
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					
					PlayerLG p1lg = main.playerlg.get(p1.getName());
					Roles r1 = p1lg.getRole();
					PlayerLG p2lg = main.playerlg.get(p2.getName());
					Roles r2 = p2lg.getRole();
					firstVilainGar�onChoice.remove(player);
					
					p1lg.setRole(r2);
					exchangeCamps(p1lg, p2lg, r2);
					p2lg.setRole(r1);
					exchangeCamps(p2lg, p1lg, r1);
					
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
					main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
					player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
					player.sendMessage(main.getPrefix() + main.SendArrow + "�bVous avez �chang� les r�les de �c�l" + main.getPlayerNameByAttributes(p1, player) + " �bet �c�l" + main.getPlayerNameByAttributes(p2, player) + "�b.");
					p1.sendMessage(main.getPrefix() + main.SendArrow + "�bVotre r�le a �t� �chang� avec quelqu'un d'autre par le " + Roles.VILAIN_GAR�ON.getDisplayName() + "�b. Vous �tes maintenant " + r2.getDisplayName() + "�b.");
					p1.playSound(p1.getLocation(), Sound.CLICK, 8, 1.5f);
					p2.sendMessage(main.getPrefix() + main.SendArrow + "�bVotre r�le a �t� �chang� avec quelqu'un d'autre par le " + Roles.VILAIN_GAR�ON.getDisplayName() + "�b. Vous �tes maintenant " + r1.getDisplayName() + "�b.");
					p2.playSound(p2.getLocation(), Sound.CLICK, 8, 1.5f);
					p1.getInventory().setItem(4, main.getRoleMap(r2));
					p2.getInventory().setItem(4, main.getRoleMap(r1));
					p1.sendMessage(main.getPrefix() + main.SendArrow + r2.getDescription());
					LG.sendTitle(p1, "�fVous �tes " + r2.getDisplayName(), "�fVotre camp : �e" + p1lg.getCamp(), 10, 60, 10);
					p2.sendMessage(main.getPrefix() + main.SendArrow + r1.getDescription());
					LG.sendTitle(p2, "�fVous �tes " + r1.getDisplayName(), "�fVotre camp : �e" + p2lg.getCamp(), 10, 60, 10);
				}
			 } else if (main.isDisplayState(DisplayState.NUIT_MAMIE)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setMamieTargeted(true);
				playerlg.setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�d" + p.getName() + " �cne pourra pas voter pendant ce tour.");
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					 
			} else if (main.isDisplayState(DisplayState.NUIT_CORBEAU)) {
				Player p = playerlg.getPlayerOnCursor(main.players);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setCorbeauTargeted(true);
				playerlg.setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�8" + p.getName() + " �faura �e2 votes�f au d�but du jour.");
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				 
			 } else if (main.isDisplayState(DisplayState.NUIT_JDF)) {
				List<Player> choosable = new ArrayList<>();
				for (Player p : main.players)
					if (!main.playerlg.get(p.getName()).isCharmed() && !p.getUniqueId().equals(player.getUniqueId()))
						choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				List<Player> charmeds = new ArrayList<>();
				StringBuilder scharmes = new StringBuilder();
				if (p.equals(player)) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous ne pouvez pas vous charmer vous m�me !");
					player.playSound(player.getLocation(), Sound.ITEM_BREAK, 10, 1);
					return;
				}
				
				plg.setCharmed(true);
				for (Player p2 : main.players) if (main.playerlg.get(p2.getName()).isCharmed()) charmeds.add(p2);
				for (Player p2 : charmeds) {
					if (scharmes.toString().equals("")) {
						scharmes = new StringBuilder("�5" + p2.getName());
					} else scharmes.append("�d, �5").append(p2.getName());
				}
				if (JDFHadCharmedSomeone.contains(player)) {
					playerlg.setHasUsedPower(true);
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dListe des Charm�s : " + scharmes);
				}
				JDFHadCharmedSomeone.add(player);
				if (playerlg.hasUsedPower()) JDFHadCharmedSomeone.remove(player);
				p.sendMessage(main.getPrefix() + main.SendArrow + "�dLe " + Roles.JOUEUR_DE_FL�TE.getDisplayName() + " �dvous a charm�. Liste des charm�s : " + scharmes);
				
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dA l'aide de votre fl�te et de sa m�lodie, vous charmez �5" + main.getPlayerNameByAttributes(p, player) + "�d.");
			 } else if (main.isDisplayState(DisplayState.NUIT_PYRO)) {
				List<Player> choosable = new ArrayList<>();
				for (Player p : main.players)
					if (!main.playerlg.get(p.getName()).isHuil�() && !p.getUniqueId().equals(player.getUniqueId()))
						choosable.add(p);
				Player p = playerlg.getPlayerOnCursor(choosable);
				if (p == null) return;
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setHuil�(true);
				if (PyroHadChoosedSomeone.contains(player))
					playerlg.setHasUsedPower(true);
				else PyroHadChoosedSomeone.add(player);
				p.sendMessage(main.getPrefix() + main.SendArrow + "�eLe " + Roles.PYROMANE.getDisplayName() + " �evous a huil�.");
				
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�eA l'aide de votre fid�le essence, vous huilez �6" + main.getPlayerNameByAttributes(p, player) + "�e.");
			}
		 }
	}
	
	
	@EventHandler
	public void onAnnulation(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		Action action = ev.getAction();
		ItemStack current = player.getItemInHand();
		
		if (current == null) return;
		if (current.getType().equals(Material.AIR)) return;
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (current.equals(main.getItem(Material.BARRIER, "�cAnnuler", Collections.singletonList("�7Annule l'action en cours.")))) {
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			}
		}
	}
	
	
	
	@EventHandler
	public void onVoleurInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.VOLEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.MAP)) {
				PlayerLG playerlg = main.playerlg.get(player.getName());
				Roles r = Roles.getByDisplayName(current.getItemMeta().getDisplayName());
				
				playerlg.setRole(r);
				playerlg.setCamp(r.getCamp());
				playerlg.setVoleur(true);
				playerlg.setHasUsedPower(true);
				main.RolesVoleur.remove(r);
			} else if (current.getType().equals(Material.BARRIER)) {
				main.playerlg.get(player.getName()).setVoleur(true);
				main.playerlg.get(player.getName()).setCamp(RCamp.VILLAGE);
				main.playerlg.get(player.getName()).setRole(Roles.VOLEUR);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.closeInventory();
			player.getInventory().setItem(19, new ItemStack(Material.BARRIER));
			player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous avez choisi le r�le " + main.playerlg.get(player.getName()).getRole().getDisplayName() + "�f.");
			if (!main.playerlg.get(player.getName()).getRole().equals(Roles.VOLEUR))
				player.sendMessage(main.playerlg.get(player.getName()).getRole().getDescription());
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
			
		}
	}
	
	@EventHandler
	public void onCupidonInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equalsIgnoreCase("�6Inv " + Roles.CUPIDON.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				if (main.cupiEnCouple) firstCupidonChoice.put(player, player);
				
				if (!firstCupidonChoice.containsKey(player)) {
					SkullMeta itm = (SkullMeta) current.getItemMeta();
					Player p = Bukkit.getPlayer(itm.getOwner());
					firstCupidonChoice.put(player, p);
					inv.remove(current);
				} else {
					Player p1 = firstCupidonChoice.get(player);
					SkullMeta itm = (SkullMeta) current.getItemMeta();
					Player p2 = Bukkit.getPlayer(itm.getOwner());
					if (p1.getName().equals(p2.getName())) {
						while (p1.getName().equals(p2.getName())) {
							p2 = main.players.get(new Random().nextInt(main.players.size()));
						}
					}
					
					PlayerLG p1lg = main.playerlg.get(p1.getName());
					PlayerLG p2lg = main.playerlg.get(p2.getName());
					p1lg.addCouple(p2);
					p2lg.addCouple(p1);
					firstCupidonChoice.remove(player);
					
					Team t = null;
					for (Team team : player.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t = team;
					Team t1 = null;
					for (Team team : p1.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t1 = team;
					Team t2 = null;
					for (Team team : p2.getScoreboard().getTeams()) if (team.getName().startsWith("Couple")) t2 = team;
					
					t1.addEntry(p2.getName());
					t1.addEntry(p1.getName());
					t2.addEntry(p1.getName());
					t2.addEntry(p2.getName());
					t.addEntry(p1.getName());
					t.addEntry(p2.getName());
					
					
					p1.sendMessage(main.getPrefix() + main.SendArrow + "�dVous recevez soudainement une fl�che, elle vous transperce. Regardant au loin, vous apercevez �5" + p2.getName() + " �d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " �dpour avoir fait ce choix. �r\n�dVous �tes amoureux de �5" + p2.getName() + " �d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
					p2.sendMessage(main.getPrefix() + main.SendArrow + "�dVous recevez soudainement une fl�che, elle vous transperce. Regardant au loin, vous apercevez �5" + p1.getName() + " �d, vous vous effondrez de joie et remerciez " + Roles.CUPIDON.getDisplayName() + " �dpour avoir fait ce choix. �r\n�dVous �tes amoureux de �5" + p1.getName() + " �d, vous devez gagner ensemble et si l'un d'entre-vous meurt, il emporte l'autre avec un chagrin d'amour...");
				
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dVos 2 fl�ches ont bien transperc� �5" + main.getPlayerNameByAttributes(p1, player) + " �det �5" + main.getPlayerNameByAttributes(p2, player) + "�d. Ils ne se quitteront plus d�sormais...");
				}
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onESInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.ENFANT_SAUVAGE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				main.playerlg.get(p.getName()).addmaitre(player);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous avez bien choisi �6" + main.getPlayerNameByAttributes(p, player) + "�e en mod�le.");
			}
			
		} else if (current.getType().equals(Material.BARRIER)) {
			player.closeInventory();
			main.playerlg.get(player.getName()).setHasUsedPower(true);
		}
		
		player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
	}
	
	@EventHandler
	public void onCLInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.CHIEN_LOUP.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.BOWL)) {
				PlayerLG playerlg = main.playerlg.get(player.getName());
				
				playerlg.setCamp(RCamp.VILLAGE);
				playerlg.setHasUsedPower(true);
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous allez incarner un �a�lChien �fpendant cette partie.");
				
			} else if (current.getType().equals(Material.RABBIT_STEW)) {
				PlayerLG playerlg = main.playerlg.get(player.getName());
				
				playerlg.setCamp(RCamp.LOUP_GAROU);
				playerlg.setHasUsedPower(true);
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous allez incarner un �c�lLoup �fpendant cette partie.");
				
				if (!main.CalledRoles.contains(Roles.LOUP_GAROU)) {
					main.CalledRoles.clear();
					main.fillCalledRoles();
					while (!main.CalledRoles.get(0).equals(Roles.CHIEN_LOUP)) {
						main.CalledRoles.remove(main.CalledRoles.get(0));
					}
				}
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onJumeauInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.JUMEAU.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				main.playerlg.get(p.getName()).getJumeauOf().add(player);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous avez bien choisi �5�l" + main.getPlayerNameByAttributes(p, player) + " �dcomme Jumeau.");
			}
			
		} else if (current.getType().equals(Material.BARRIER)) {
			player.closeInventory();
			main.playerlg.get(player.getName()).setHasUsedPower(true);
		}
		
		player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
	}
	
	
	@EventHandler
	public void onNoctainv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.NOCTAMBULE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				plg.setNoctaTargeted(true);
				p.sendMessage(main.getPrefix() + main.SendArrow + "�9Le " + Roles.NOCTAMBULE.getDisplayName() + " �1\"�b" + main.getPlayerNameByAttributes(player, p) + "�1\"�9 a d�cid� de dormir chez vous ! �cSi vous aviez un pouvoir de nuit, vous le perdez pour ce tour.");
				p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 9, 1);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�9Vous avez dormi chez �b" + main.getPlayerNameByAttributes(p, player) + "�9.");
				
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onCom�dienInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.COM�DIEN.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.MAP)) {
				Roles r = Roles.getByDisplayName(current.getItemMeta().getDisplayName());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				
				playerlg.setRole(r);
				playerlg.setCom�dien(true);
				main.pouvoirsCom�dien.remove(r);
				
				if (main.AliveRoles.containsKey(r))
						main.AliveRoles.put(r, main.AliveRoles.get(r) + 1);
				else main.AliveRoles.put(r, 1);
				main.CalledRoles.clear();
				main.fillCalledRoles();
				while (!main.CalledRoles.get(0).equals(Roles.COM�DIEN)) {
					main.CalledRoles.remove(main.CalledRoles.get(0));
				}
				main.CalledRoles.remove(Roles.COM�DIEN);
				if (main.AliveRoles.get(Roles.COM�DIEN) > 1) {
					main.AliveRoles.put(Roles.COM�DIEN, main.AliveRoles.get(Roles.COM�DIEN) - 1);
				} else main.AliveRoles.remove(Roles.COM�DIEN);
				
				main.GRunnable.createScoreboardList();
				
				player.closeInventory();
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous incarnerez le r�le de " + r.getDisplayName() + " �dpendant cette nuit.");
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onVovoInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.VOYANTE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dTu d�couvres dans ton boule magique que �5" + main.getPlayerNameByAttributes(p, player) + "�d est " + plg.getRole().getDisplayName() + "�d.");
				if (main.voyanteBavarde) {
				Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "�dLa " + Roles.VOYANTE.getDisplayName() + " �da observ� un joueur qui est " + plg.getRole().getDisplayName() + " �d!");
				for (Player pl : main.players)
					if (!pl.getUniqueId().equals(player.getUniqueId()))
						pl.playSound(pl.getLocation(), Sound.PORTAL, 6, 2f);
				}
				
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onEnchantInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.ENCHANTEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (plg.isCamp(RCamp.LOUP_GAROU) || plg.isCamp(RCamp.LOUP_GAROU_BLANC)) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dfait parti des �c�lLoups-Garous�d.");
					player.getScoreboard().getTeam("LG").addEntry(p.getName());
				} else if (plg.isRole(Roles.VOYANTE))
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dest une "+Roles.VOYANTE.getDisplayName()+"�d.");
				else
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dGr�ce � ton enchantement, tu d�couvres que " + main.getPlayerNameByAttributes(p, player) + " �dest �cni Loup, ni Voyante�d.");
				
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onD�tecInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.D�TECTIVE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				if (!firstD�tectiveChoice.containsKey(player)) {
					Player p = Bukkit.getPlayer(itm.getOwner());
					firstD�tectiveChoice.put(player, p);
					
					for (ItemStack it : inv.getContents())
						if (it != null)
							if (it.getType().equals(Material.SKULL_ITEM)) {
								SkullMeta meta = (SkullMeta) it.getItemMeta();
								meta.setLore(Arrays.asList("�7Inspecte les camps de �8" + p.getName() + " �7et �8" + meta.getOwner() + "�7.", "�7Vous saurez s'ils sont ensemble ou non.", "", "�b>>Clique pour s�lectionner"));
								it.setItemMeta(meta);
							}
				} else {
					Player p1 = firstD�tectiveChoice.get(player);
					Player p2 = Bukkit.getPlayer(itm.getOwner());
					if (p2 == null) return;
					
					PlayerLG p1lg = main.playerlg.get(p1.getName());
					PlayerLG p2lg = main.playerlg.get(p2.getName());
					firstD�tectiveChoice.remove(player);
					
					boolean sameCamp = Boolean.FALSE;
					if (p1lg.isCamp(p2lg.getCamp()) || p1lg.getCouple().contains(p2))
						sameCamp = true;
					
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
					if (sameCamp)
						player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�couvrez que �f�l" + main.getPlayerNameByAttributes(p1, player) + " �7et �f�l" + main.getPlayerNameByAttributes(p2, player) + " �aappartiennent au m�me camp�7.");
					else
						player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�couvrez que �f�l" + main.getPlayerNameByAttributes(p1, player) + " �7et �f�l" + main.getPlayerNameByAttributes(p2, player) + " �cn'appartiennent pas au m�me camp�7.");
					}
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onRenardInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.RENARD.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				
				Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
				Player p2 = main.playerlg.get(p.getName()).get2NearestPlayers().get(0);
				Player p3 = main.playerlg.get(p.getName()).get2NearestPlayers().get(1);
				
				boolean thereALG = false;
				if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p2.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p3.getName()).isCamp(RCamp.LOUP_GAROU)) thereALG = true;
				if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(p2.getName()).isCamp(RCamp.LOUP_GAROU_BLANC) || main.playerlg.get(p3.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) thereALG = true;
			
				if (thereALG) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous d�couvrez qu'entre �6" + main.getPlayerNameByAttributes(p, player) + "�e, �6" + main.getPlayerNameByAttributes(p2, player) + "�e et �6" + main.getPlayerNameByAttributes(p3, player) + " �ese trouve un �c�lLoup-Garou�e.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �agardez �fvotre pouvoir.");
				} else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous d�couvrez qu'entre �6" + main.getPlayerNameByAttributes(p, player) + "�e, �6" + main.getPlayerNameByAttributes(p2, player) + "�e et �6" + main.getPlayerNameByAttributes(p3, player) + " �ene se trouve �aaucun �eLoups.");
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �cperdez �fvotre pouvoir.");
					main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
					player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				}
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			 
			 else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onPacifInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.RENARD.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
				
				main.playerlg.get(p.getName()).setPacifTargeted(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dLe r�le de �5�l" + main.getPlayerNameByAttributes(p, player) + " �dva �tre r�v�l� au d�but du jour.");
				player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous �cperdez �fvotre pouvoir.");
				main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
				player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			 
			 else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onFDJInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.FILLE_DE_JOIE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous d�cidez de dormir chez �5" + main.getPlayerNameByAttributes(p, player) + "�d pour cette nuit. Si vous vous faites attaquer par les Loups, vous survivrez, mais, s'il est Loup ou attaqu� par les Loups, vous mourrez.");
				main.playerlg.get(player.getName()).setOtherPlayerHouse(p);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.closeInventory();
				
			}
			 else if (current.getType().equals(Material.BARRIER)) {
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onGDCInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.GARDE_DU_CORPS.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�7Vous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�7 pour cette nuit. S'il est cens� mourir pendant cette nuit, il survivra.");
				main.playerlg.get(p.getName()).setProtected(player);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.closeInventory();
				
			}
			 else if (current.getType().equals(Material.BARRIER)) {
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onSalvaInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.SALVATEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				if (!main.lastSalvat�.containsKey(player)) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�f pour cette nuit. S'il se fait attaquer par les loups, il survivra.");
					main.playerlg.get(p.getName()).setSalvation(true);
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				} else {
					if (main.lastSalvat�.get(player).equals(p)) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous ne pouvez pas prot�ger deux fois de suite la m�me personne !");
					} else {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�fVous d�cidez de prot�ger �e" + main.getPlayerNameByAttributes(p, player) + "�f pour cette nuit. S'il se fait attaquer par les loups, il survivra.");
						main.playerlg.get(p.getName()).setSalvation(true);
						main.playerlg.get(player.getName()).setHasUsedPower(true);
					}
				}
				main.lastSalvat�.put(player, p);
				player.closeInventory();
				
			}
			 else if (current.getType().equals(Material.BARRIER)) {
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	
	@EventHandler
	public void onLGClickOnBook(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		ItemStack current = player.getItemInHand();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (current.getType().equals(Material.BOOK)) {
			Inventory inv = Bukkit.createInventory(null, 27, "�6Inv " + Roles.LOUP_GAROU.getDisplayName());
			
			for (Player p : main.players) {
				PlayerLG plg = main.playerlg.get(p.getName());
				
				ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta itm = (SkullMeta) it.getItemMeta();
				itm.setOwner(p.getName());
				itm.setDisplayName(main.getPlayerNameByAttributes(p, player));
				itm.setLore(Arrays.asList("�7Vote pour le joueur �c" + main.getPlayerNameByAttributes(p, player) + "�7.", "�7S'il obtient le plus de voix, il se fera d�vorer.", "", "�bVotes : �3�l" + plg.getVotes(), "", "�b>>Clique pour voter"));
				it.setItemMeta(itm);
				
				inv.addItem(it);
			}
			
			player.openInventory(inv);
		}
	}
	
	@EventHandler
	public void onLGChat(AsyncPlayerChatEvent ev) {
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (!main.isDisplayState(DisplayState.NUIT_LG) || !main.chatDesLg) return;
		
		Player player = ev.getPlayer();
		if (!main.playerlg.get(player.getName()).isVivant()) return;
		if (main.chatDesLg) {
			String msg = ev.getMessage();
			List<Player> lgs = new ArrayList<>();
			for (Player p : main.players)
				if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) lgs.add(p);
			
			if (main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(player.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) {
				
				ev.setCancelled(true);
				for (Player p : lgs) {
					p.sendMessage("�c�lLG" + " �c" + player.getName() + " �f: " + msg);
				}
				main.sendRoleMessage("�c�lLG " + main.SendArrow + "�4" + msg, Roles.PETITE_FILLE);
				
			}
		} else {
			player.sendMessage(main.getPrefix() + main.SendArrow + "�cChat des LG d�sactiv�.");
		}
	}
	
	@EventHandler
	public void onLGInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.LOUP_GAROU.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG plg = main.playerlg.get(p.getName());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				int votes = 0;
				
				main.sendRoleMessage(main.getPrefix() + main.SendArrow + "�c" + player.getName() + " �6vote pour d�vorer �e" + p.getName(), Roles.LOUP_GAROU);
				plg.addVote();

				if (playerlg.getVotedPlayer() != null) {
					Player vp = playerlg.getVotedPlayer();
					main.playerlg.get(vp.getName()).removeVote();
				}
				playerlg.setVote(p);

				List<Player> lgs = new ArrayList<>();
				for (Player lgp : main.players)
					if (main.playerlg.get(lgp.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(lgp.getName()).isCamp(RCamp.LOUP_GAROU_BLANC)) lgs.add(lgp);
				for (Player lgp : lgs)
					if (main.playerlg.get(lgp.getName()).getVotedPlayer() != null) votes++;
				if (votes == lgs.size()) {
					for (Player lgp : lgs) main.playerlg.get(lgp.getName()).setHasUsedPower(true);
				}
				
				player.closeInventory();
				
			}
			
		}
	}
	
	@EventHandler
	public void onIPDLInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.INFECT_P�RE_DES_LOUPS.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.STAINED_CLAY)) {
				Player grailled = null;
				for (Player p : main.players) if (main.playerlg.get(p.getName()).isLGTargeted()) grailled = p;
				
				if (current.getDurability() == (short)5) {
					PlayerLG plg = main.playerlg.get(grailled.getName());
					plg.setCamp(RCamp.LOUP_GAROU);
					plg.setInfected(true);
					plg.setLGTargeted(false);
					grailled.sendMessage(main.getPrefix() + main.SendArrow + "�cL'" + Roles.INFECT_P�RE_DES_LOUPS.getDisplayName() + " �ca d�cid� de vous tranformer en �c�lLoup-Garou�c. Vous vous r�veillerez d�sormais avec vos comp�res lycanthropes pour d�vorer un joueur. Vous gardez tout de m�me les pouvoirs de votre r�le.");
					main.playerlg.get(player.getName()).setUsedDefinitivePower(true);
					player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
					player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous avez bien infect� �e" + main.getPlayerNameByAttributes(grailled, player) + "�c.");
					
					
				} else if (current.getDurability() == (short)14) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous n'avez pas infect� �e" + main.getPlayerNameByAttributes(grailled, player) + "�c.");
				}
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.closeInventory();
			}
			
		}
	}
	
	@EventHandler
	public void onGMLInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.GRAND_M�CHANT_LOUP.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				main.playerlg.get(p.getName()).setGMLTargeted(true);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�cN'�tant pas r�pu de votre pr�c�dent repas, vous avez d�vor� �4" + main.getPlayerNameByAttributes(p, player) + "�c...");
				player.closeInventory();
			}
			
			 else if (current.getType().equals(Material.BARRIER)) {
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onLGBInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.LOUP_GAROU_BLANC.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				main.playerlg.get(p.getName()).setLGBTargeted(true);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�cD'un grand coup de griffe, vous poignardez �4" + p.getName() + "�c : il meurt sur le coup...");
				player.closeInventory();
			}
			
			 else if (current.getType().equals(Material.BARRIER)) {
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	@EventHandler
	public void onPF2Inv(InventoryClickEvent ev) {
		Player player = (Player)ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.PETITE_FILLE2.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.STAINED_CLAY))
				if (current.getDurability() == (short)4) {
					
					Random r = new Random();
					int findlg = r.nextInt(5) + 1;
					int die = r.nextInt(20) + 1;
					
					if (findlg == 5) {
						Player lg = main.getPlayersByRole(Roles.LOUP_GAROU).get(r.nextInt(main.getPlayersByRole(Roles.LOUP_GAROU).size()));
						player.sendMessage(main.getPrefix() + main.SendArrow + "�eVous avez trouv� un Loup ! Son nom est �9�l" + main.getPlayerNameByAttributes(lg, player) + "�e.");
					}
					
					if (die == 20) {
						main.playerlg.get(player.getName()).setPF2Died(true);
						player.sendMessage(main.getPrefix() + main.SendArrow + "�cLes Loups vous ont trouv� ! Vous servirez de dessert ce soir...");
					}
					
					if (findlg != 5 && die != 20)
						player.sendMessage(main.getPrefix() + main.SendArrow + "�9Vous n'avez rien trouv� ce soir...");
					
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				} else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�9Vous n'avez pas observ� le village ce soir.");
					main.playerlg.get(player.getName()).setHasUsedPower(true);
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
				}
		}
	}
	
	@EventHandler
	public void onSosoInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.SORCI�RE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.POTION)) {
				
				if (current.getItemMeta().getDisplayName().startsWith("�aR�ssusciter �c")) {
					Player grailled = null;
					for (Player p : main.players) if (main.playerlg.get(p.getName()).isLGTargeted()) grailled = p;
					
					main.playerlg.get(grailled.getName()).setLGTargeted(false);
					player.sendMessage(main.getPrefix() + main.SendArrow + "�aVous soignez les blessures de �2" + main.getPlayerNameByAttributes(grailled, player) + "�a.");
					player.playSound(player.getLocation(), Sound.CAT_HIT, 5, 2f);
					main.SosoR�zPots.put(player, false);
					if (ActionsSosos.contains(player)) main.playerlg.get(player.getName()).setHasUsedPower(true);
					if (!ActionsSosos.contains(player)) ActionsSosos.add(player);
					inv.remove(current);
				} else {
					Inventory invKill = Bukkit.createInventory(null, 27, "�6Inv  �4kill " + Roles.SORCI�RE.getDisplayName());
					
					for (Player p : main.players) {
						if (!p.getName().equals(player.getName())) {
							ItemStack it = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
							SkullMeta itm = (SkullMeta) it.getItemMeta();
							itm.setOwner(p.getName());
							itm.setDisplayName("�c" + main.getPlayerNameByAttributes(p, player));
							itm.setLore(Arrays.asList("�7Tue le joueur �c" + main.getPlayerNameByAttributes(p, player) + "�7.", "�7Il sera �limin� de la partie.", "", "�b>>Clique pour s�lecionner"));
							it.setItemMeta(itm);
							
							invKill.addItem(it);
						}
					}
					invKill.setItem(26, main.config.getFl�cheRetour());
					player.openInventory(invKill);
				}
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
		} else if (inv.getName().equals("�6Inv  �4kill " + Roles.SORCI�RE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
			
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				
				main.playerlg.get(p.getName()).setSosoTargeted(true);
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous jetez une potion de mort sur le joueur �4" + main.getPlayerNameByAttributes(p, player) + "�c il sera retrouv� mort au petit matin...");
				player.playSound(player.getLocation(), Sound.SPLASH2, 8, 1f);
				player.closeInventory();
				main.SosoKillPots.put(player, false);
				if (ActionsSosos.contains(player)) main.playerlg.get(player.getName()).setHasUsedPower(true);
				if (!ActionsSosos.contains(player)) ActionsSosos.add(player);
				
			} else if (current.getType().equals(Material.ARROW)) {
				Player grailled = null;
				for (Player p : main.players) if (main.playerlg.get(p.getName()).isLGTargeted()) grailled = p;
				Inventory binv = Bukkit.createInventory(null, InventoryType.BREWING, "�6Inv " + Roles.SORCI�RE.getDisplayName());
				binv.setItem(1, main.getItem(Material.BARRIER, "�c�lAnnuler", Collections.singletonList("�7N'effectue pas l'action en cours.")));
				
				if (grailled != null && main.SosoR�zPots.get(player))binv.setItem(3, NightRunnable.getHeadItem(grailled, "�c" + main.getPlayerNameByAttributes(grailled, player), Arrays.asList("�7En cette nuit�e, �c" + main.getPlayerNameByAttributes(grailled, player) + "�7 a �t� englouti par les loups.", "�7Vous pouvez le r�ssusciter et/ou tuer quelqu'un d'autre.")));
				if (grailled == null)binv.setItem(3, main.getItem(Material.SKULL_ITEM, "�cPersonne", Arrays.asList("�7En cette nuit�e, personne n'a �t� attaqu�.", "�7Vous pouvez tout de m�me tuer quelqu'un.")));
				if (!main.SosoR�zPots.get(player))binv.setItem(3, main.getItem(Material.BARRIER, "�cPlus de potions", Collections.singletonList("�cVous n'avez plus de potion pour �ar�ssuciter.")));
				
				if (grailled != null && main.SosoR�zPots.get(player)) {
					Potion potr�z = new Potion(PotionType.INSTANT_HEAL, 1);
					ItemStack itr�z = potr�z.toItemStack(1);
					ItemMeta itr�zm = itr�z.getItemMeta();
					itr�zm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
					itr�zm.setDisplayName("�aR�ssusciter �c" + main.getPlayerNameByAttributes(grailled, player));
					itr�zm.setLore(Arrays.asList("�7R�ssucite le joueur �c" + main.getPlayerNameByAttributes(grailled, player) + "�7.", "�7Il ne sera donc pas �limin�.", "", "�b>>Clique pour s�lectionner"));
					itr�z.setItemMeta(itr�zm);
					
					binv.setItem(0, itr�z);
				}
					
				if (main.SosoKillPots.get(player)) {
					Potion potkill = new Potion(PotionType.INSTANT_DAMAGE, 1);
					ItemStack itkill = potkill.toItemStack(1);
					ItemMeta itkillm = itkill.getItemMeta();
					itkillm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
					itkillm.setDisplayName("�4Tuer un joueur");
					itkillm.setLore(Arrays.asList("�7Tue un autre joueur de la partie.", "�7On le retrouvera mort au petit matin.", "", "�b>>Clique pour s�lectionner"));
					itkill.setItemMeta(itkillm);
					
					binv.setItem(2, itkill);
				}
				
				player.openInventory(binv);
			}
			
		}
		
	}
	
	
	@EventHandler
	public void onPretreInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.PR�TRE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow  + "�f" + main.getPlayerNameByAttributes(p, player) + "�e a re�u de l'eau b�nite !");
				plg.setPretreThrower(player);
				
				playerlg.setUsedDefinitivePower(true);
				player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	
	@EventHandler
	public void onN�croInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.N�CROMANCIEN.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow  + "�1�l" + main.getPlayerNameByAttributes(p, player) + " �9 sera r�ssucit� au d�but du jour !");
				plg.setN�croTargeted(true);
				
				playerlg.setHasUsedPower(true);
				playerlg.setUsedDefinitivePower(true);
				player.getInventory().setItem(18, main.getItem(Material.STONE_BUTTON, "", null));
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	
	@EventHandler
	public void onDictaInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.DICTATEUR.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.STAINED_CLAY)) {
				if (current.getDurability() == 5) {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�aVous allez effectuer un coup d'�tat pendant le jour.");
					main.playerlg.get(player.getName()).setInCoupDEtat(true);
				} else
					player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous n'effectuerez pas de coup d'�tat pendant le jour.");
				main.playerlg.get(player.getName()).setHasUsedPower(true);
				player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
				player.closeInventory();
			}
		}
	}
	
	
	@EventHandler
	public void onMamieInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.MAMIE_GRINCHEUSE.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				if (playerlg.getLastInterdit() != null)
					if (playerlg.getLastInterdit().equals(p)) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�cVous avez d�j� s�lectionn� �d" + p.getName() + " �cau dernier tour.");
						return;
					} else {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�d" + p.getName() + " �cne pourra pas voter pendant ce tour.");
						plg.setMamieTargeted(true);
					}
				else {
					player.sendMessage(main.getPrefix() + main.SendArrow + "�d" + p.getName() + " �cne pourra pas voter pendant ce tour.");
					plg.setMamieTargeted(true);
				}
				
				playerlg.setLastInterdit(p);
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
	
	@EventHandler
	public void onCorbeauInv(InventoryClickEvent ev) {
		Player player = (Player) ev.getWhoClicked();
		Inventory inv = ev.getInventory();
		ItemStack current = ev.getCurrentItem();
		
		if (current == null) return;
		
		if (!main.isCycle(Gcycle.NUIT)) return;
		
		if (inv.getName().equals("�6Inv " + Roles.CORBEAU.getDisplayName())) {
			ev.setCancelled(true);
			
			if (current.getType().equals(Material.SKULL_ITEM)) {
				SkullMeta itm = (SkullMeta) current.getItemMeta();
				Player p = Bukkit.getPlayer(itm.getOwner());
				PlayerLG playerlg = main.playerlg.get(player.getName());
				PlayerLG plg = main.playerlg.get(p.getName());
				
				player.sendMessage(main.getPrefix() + main.SendArrow + "�8" + p.getName() + " �faura �e2 votes�f au d�but du jour.");
				plg.setCorbeauTargeted(true);
				
				playerlg.setHasUsedPower(true);
				player.closeInventory();
				
			} else if (current.getType().equals(Material.BARRIER)) {
				player.closeInventory();
				main.playerlg.get(player.getName()).setHasUsedPower(true);
			}
			
			player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
		}
	}
	
		
		@EventHandler
		public void onJDFInv(InventoryClickEvent ev) {
			Player player = (Player) ev.getWhoClicked();
			Inventory inv = ev.getInventory();
			ItemStack current = ev.getCurrentItem();
			
			if (current == null) return;
			
			if (!main.isCycle(Gcycle.NUIT)) return;
			
			if (inv.getName().equals("�6Inv " + Roles.JOUEUR_DE_FL�TE.getDisplayName())) {
				ev.setCancelled(true);
			
				if (current.getType().equals(Material.SKULL_ITEM)) {
					
					SkullMeta itm = (SkullMeta) current.getItemMeta();
					Player p = Bukkit.getPlayer(itm.getOwner());
					List<Player> charmeds = new ArrayList<>();
					StringBuilder scharmes = new StringBuilder();
					if (p.equals(player)) {
						player.sendMessage(main.getPrefix() + main.SendArrow + "�dVous ne pouvez pas vous charmer vous m�me !");
						player.playSound(player.getLocation(), Sound.ITEM_BREAK, 10, 1);
						return;
					}
					
					main.playerlg.get(p.getName()).setCharmed(true);
					for (Player p2 : main.players) if (main.playerlg.get(p2.getName()).isCharmed()) charmeds.add(p2);
					for (Player p2 : charmeds) {
						if (scharmes.toString().equals("")) {
							scharmes = new StringBuilder("�5" + p2.getName());
						} else scharmes.append("�d, �5").append(p2.getName());
					}
					if (JDFHadCharmedSomeone.contains(player)) {
						main.playerlg.get(player.getName()).setHasUsedPower(true);
						player.closeInventory();
						JDFHadCharmedSomeone.remove(player);
						player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
						player.sendMessage(main.getPrefix() + main.SendArrow + "�dListe des Charm�s : " + scharmes);
					} else JDFHadCharmedSomeone.add(player);
					inv.remove(current);
					p.sendMessage(main.getPrefix() + main.SendArrow + "�dLe " + Roles.JOUEUR_DE_FL�TE.getDisplayName() + " �dvous a charm�. Liste des charm�s : " + scharmes);
					
					player.sendMessage(main.getPrefix() + main.SendArrow + "�dA l'aide de votre fl�te et de sa m�lodie, vous charmez �5" + main.getPlayerNameByAttributes(p, player) + "�d.");
					
				}
				 else if (current.getType().equals(Material.BARRIER)) {
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				}
				
			}
		}
		
		@EventHandler
		public void onPyroInv(InventoryClickEvent ev) {
			Player player = (Player) ev.getWhoClicked();
			Inventory inv = ev.getInventory();
			ItemStack current = ev.getCurrentItem();
			
			if (current == null) return;
			
			if (!main.isCycle(Gcycle.NUIT)) return;
			
			if (inv.getName().equals("�6Inv " + Roles.PYROMANE.getName())) {
				ev.setCancelled(true);
				
				if (current.getType().equals(Material.FLINT_AND_STEEL)) {
					
					for (PlayerLG plg : main.playerlg.values())
						if (plg.isHuil�())
							plg.setPyroTargeted(true);
					
					player.sendMessage(main.getPrefix() + main.SendArrow + "�6Vous avez br�l� tous les joueurs huil�s.");
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				}
				else if (current.getType().equals(Material.LAVA_BUCKET)) {
					
					if (main.isType(Gtype.R�UNION)) {
						Inventory invPyro = Bukkit.createInventory(null, 27, "�6Inv Choose" + Roles.PYROMANE.getName());
						for (Player p : main.players)
							if (!p.equals(player))
								invPyro.addItem(NightRunnable.getHeadItem(p, "�6" + p.getName(), Arrays.asList("�7Recouvre �6" + p.getName() + " �7d'essence.", "�7Il saura qu'il a �t� huil�.")));
						invPyro.setItem(26, main.getItem(Material.BARRIER, "�c�lAnnuler", Collections.singletonList("�7N'effectue pas l'action en cours.")));
						player.openInventory(invPyro);
					} else {
						for (Player p : main.players)
							if (!main.playerlg.get(p.getName()).isCharmed())
								player.showPlayer(p);
						
						player.sendMessage(main.getPrefix() + main.SendArrow + "�6Vous avez "+player.getLevel()+" secondes pour huiler 2 personnes. Pour faire cela, il suffit de cliquer sur les joueurs voulus.");
						player.getInventory().setItem(8, main.getItem(Material.BARRIER, "�cAnnuler", Collections.singletonList("�7Annule l'action en cours.")));
					}
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
				}
				else if (current.getType().equals(Material.BARRIER)) {
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				}
			}
			
			else if (inv.getName().equals("�6Inv Choose" + Roles.PYROMANE.getName())) {
				ev.setCancelled(true);
				
				if (current.getType().equals(Material.SKULL_ITEM)) {
					Player p = Bukkit.getPlayer(((SkullMeta) current.getItemMeta()).getOwner());
					
					main.playerlg.get(p.getName()).setCharmed(true);
					if (PyroHadChoosedSomeone.contains(player)) {
						main.playerlg.get(player.getName()).setHasUsedPower(true);
						player.closeInventory();
						PyroHadChoosedSomeone.remove(player);
						player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					} else PyroHadChoosedSomeone.add(player);
					inv.remove(current);
					p.sendMessage(main.getPrefix() + main.SendArrow + "�eLe " + Roles.PYROMANE.getDisplayName() + " �evous a huil�.");
					
					player.sendMessage(main.getPrefix() + main.SendArrow + "�eA l'aide de votre fid�le essence, vous huilez �6" + main.getPlayerNameByAttributes(p, player) + "�e.");
				}
				else if (current.getType().equals(Material.BARRIER)) {
					player.playSound(player.getLocation(), Sound.IRONGOLEM_WALK, 10, 2f);
					player.closeInventory();
					main.playerlg.get(player.getName()).setHasUsedPower(true);
				}
			}
		}
		
		@EventHandler
		public void onSoeurChat(AsyncPlayerChatEvent ev) {
			Player player = ev.getPlayer();
			String msg = ev.getMessage();
			
			if (!main.isCycle(Gcycle.NUIT)) return;
			
			if (!main.isDisplayState(DisplayState.NUIT_SOEUR)) return;
			
			if (!main.players.contains(player)) return;
			
			if (main.playerlg.get(player.getName()).isRole(Roles.SOEUR) && !main.playerlg.get(player.getName()).isNoctaTargeted()) {
				
				ev.setCancelled(true);
				for (Player p : main.playerlg.get(player.getName()).getsoeur()) {
					p.sendMessage(Roles.SOEUR.getDisplayName() + " �d" + player.getName() + " �f: " + msg);
				}
				player.sendMessage(Roles.SOEUR.getDisplayName() + " �d" + player.getName() + " �f: " + msg);
				
			}
		}
		
		@EventHandler
		public void onFr�reChat(AsyncPlayerChatEvent ev) {
			Player player = ev.getPlayer();
			String msg = ev.getMessage();
			
			if (!main.isCycle(Gcycle.NUIT)) return;
			
			if (!main.isDisplayState(DisplayState.NUIT_FRERE)) return;
			
			if (!main.players.contains(player)) return;
			
			if (main.playerlg.get(player.getName()).isRole(Roles.FR�RE) && !main.playerlg.get(player.getName()).isNoctaTargeted()) {
				
				ev.setCancelled(true);
				for (Player p : main.playerlg.get(player.getName()).getfr�re()) {
					p.sendMessage(Roles.FR�RE.getDisplayName() + " �d" + player.getName() + " �f: " + msg);
				}
				player.sendMessage(Roles.FR�RE.getDisplayName() + " �d" + player.getName() + " �f: " + msg);
				
			}
		}
		
		
		
		@EventHandler
		public void onChamanChat(AsyncPlayerChatEvent ev) {
			Player player = ev.getPlayer();
			String msg = ev.getMessage();
			
			if (!main.isCycle(Gcycle.NUIT)) return;
			
			if (!main.AliveRoles.containsKey(Roles.CHAMAN)) return;
			
			
			if (main.spectators.contains(player)) {
				main.sendRoleMessage("�7�lSpectateur " + main.SendArrow + "�f" + msg, Roles.CHAMAN);
			}
			
			if (main.paroleChaman) {
				if (main.players.contains(player)) {
					if (main.playerlg.get(player.getName()).isRole(Roles.CHAMAN)) {
						
						for (Player spec : main.spectators)
							spec.sendMessage(main.getPrefix() + main.SendArrow + Roles.CHAMAN.getDisplayName() + main.SendArrow + "" + msg);
						player.sendMessage(Roles.CHAMAN.getDisplayName() + "�9" + player.getName() + main.SendArrow + msg);
						
					}
				}
			}
		}
		
		
		
		private void exchangeCamps(PlayerLG p1lg, PlayerLG p2lg, Roles r2) {
			p1lg.setRole(r2);
			if (r2.getCamp().equals(p2lg.getCamp()))
				p1lg.setCamp(r2.getCamp());
			else
				if (!p2lg.isInfected()) {
					if (r2.getCamp().equals(RCamp.ANGE) || r2.getCamp().equals(RCamp.MERCENAIRE))
						if (main.days == 0)
							p1lg.setCamp(r2.getCamp());
						else
							p1lg.setCamp(RCamp.VILLAGE);
					else if (r2.getCamp().equals(RCamp.CHIEN_LOUP))
						p1lg.setCamp(p2lg.getChoosenCampCL());
					else if (r2.getCamp().equals(RCamp.VOLEUR))
						p1lg.setCamp(RCamp.VILLAGE);
				} else {
					if (r2.getCamp().equals(RCamp.VILLAGE))
						p1lg.setCamp(RCamp.VILLAGE);
					else if (r2.getCamp().equals(RCamp.CHIEN_LOUP))
						p1lg.setCamp(p2lg.getChoosenCampCL());
					else if (r2.getCamp().equals(RCamp.ANGE) || r2.getCamp().equals(RCamp.MERCENAIRE))
						if (main.days == 0)
							p1lg.setCamp(r2.getCamp());
						else
							p1lg.setCamp(RCamp.VILLAGE);
					else
						p1lg.setCamp(r2.getCamp());
				}
			
			if (p1lg.isInfected() && !p1lg.isCamp(RCamp.LOUP_GAROU_BLANC)) p1lg.setCamp(RCamp.LOUP_GAROU);
		}
		
		

}
