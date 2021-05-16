package fr.neyuux.lgthierce.commands;

import fr.neyuux.lgthierce.LG;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class SpecExecutor implements Listener {

	private final LG main;
	
	public SpecExecutor(LG main) {
		this.main = main;
	}
	
	@EventHandler
	public void onSpecInv(InventoryOpenEvent ev) {
		Inventory inv = ev.getInventory();
		if (inv.getName().equals("commandespec")) {
		
		Player sender = Bukkit.getPlayer(inv.getItem(0).getItemMeta().getDisplayName());
		String arg0 = inv.getItem(1).getItemMeta().getDisplayName();
		
		if (arg0.equalsIgnoreCase("list")) {
			
			StringBuilder specs = new StringBuilder();
			for (Player player : main.spectators) {
				if (specs.toString().equalsIgnoreCase("")) {
					specs = new StringBuilder("§b" + player.getName());
				} else specs.append("§7, §b").append(player.getName());
			}
			sender.sendMessage(main.getPrefix() + main.SendArrow + "§7Liste des §lspectateurs §7: " + specs);
			
		}
		
		
		else if (arg0.equalsIgnoreCase("off")) {
			
			if (sender == null) {
				return;
			}

			if (main.spectators.contains(sender)) {
				main.spectators.remove(sender);
				sender.getInventory().clear();
				sender.setMaxHealth(20);
				sender.setHealth(20);
				for (PotionEffect pe : sender.getActivePotionEffects()) {
					sender.removePotionEffect(pe.getType());
				}
				PotionEffect saturation = new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, true, false);
				sender.addPotionEffect(saturation);
				main.clearArmor(sender);
				sender.setDisplayName(sender.getName());
				sender.setPlayerListName(sender.getName());
				sender.setGameMode(GameMode.ADVENTURE);
				sender.teleport(new Location(Bukkit.getWorld("LG"), 494, 12.2, 307, 0f, 0f));
				
				sender.getInventory().setItem(1, main.getItem(Material.GHAST_TEAR, "§7§lDevenir Spectateur", Arrays.asList("§fFais devenir le joueur", "§fspectateur de la partie.", "§b>>Clique droit")));
				sender.getInventory().setItem(5, main.getItem(Material.EYE_OF_ENDER, "§a§lJouer", Arrays.asList("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit")));
				
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam("AGJoueur").addEntry(sender.getName());
				main.setPlayerGrade(sender);
				
				for (Entry<String, List<UUID>> en : main.getGrades().entrySet())
					if (en.getValue().contains(sender.getUniqueId()))
						sender.getInventory().setItem(6, main.config.getComparator());
			} else sender.sendMessage(main.getPrefix() + main.SendArrow + "§cTu n'es pas spectateur, §4DOMMAGE ESSAYE DE TROUVER UN AUTRE BUG, §lBOOMER§c.");
				
				
		}
		
		
		
		else if (arg0.equalsIgnoreCase("on")) {
			
			if (!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + main.SendArrow + "§cTu dois être un joueur pour faire cette commande !");
			}

			if (!main.spectators.contains(sender)) {
				main.players.remove(sender);
				main.spectators.add(sender);
				
				sender.getInventory().clear();
				sender.setGameMode(GameMode.SPECTATOR);
				sender.setDisplayName("§8[§7Spectateur§8]" + sender.getName());
				sender.setPlayerListName(sender.getDisplayName());
				Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(sender.getName()).removeEntry(sender.getName());
				sender.sendMessage(main.getPrefix() + main.SendArrow + "§9Votre mode de jeu a été établi en §7spectateur§9.");
				sender.sendMessage("§cPour se retirer du mode §7spectateur §c, faire la commande : §e§l/spec off§c.");

			} else sender.sendMessage(main.getPrefix() + main.SendArrow + "§cT'es déjà un spectateur ! §4DOMMAGE §lBOOMER");
			
		} 
		
		
		else if (arg0.equalsIgnoreCase("help")) sender.sendMessage(main.getPrefix() + main.SendArrow + "§fAide pour la commmande §aspec§f :\n§e§l/spec on §8(Vous met en spectateur)§r\n§e§l/spec off §8(Vous retire du mode spectateur)§r\n§e§l/spec list §8(Affiche la liste des spectateurs)");
		
		
		else {
			sender.sendMessage(main.getPrefix() + main.SendArrow + "§cArgument inconnu pour la commande §e/spec §c: Essayez de faire §a/spec help §c!");
		}
		
		for (Player p : Bukkit.getOnlinePlayers())
			main.updateScoreboard(p);
		
		}
	}
	
}
