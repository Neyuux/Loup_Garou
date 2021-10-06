package fr.neyuux.refont.old.lg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.neyuux.refont.old.lg.DisplayState;
import fr.neyuux.refont.old.lg.Gstate;
import fr.neyuux.refont.old.lg.LG;
import fr.neyuux.refont.old.lg.PlayerLG;
import fr.neyuux.refont.old.lg.role.Roles;

public class CommandAnkou implements CommandExecutor {
	
	private final LG main;
	public CommandAnkou(LG main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if (main.isState(Gstate.PLAYING)) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (main.playerlg.containsKey(player.getName())) {
					PlayerLG playerlg = main.playerlg.get(player.getName());
					if (playerlg.isRole(Roles.ANKOU) && !playerlg.isVivant()) {
						
						if (main.isDisplayState(DisplayState.VOTE) || main.isDisplayState(DisplayState.VOTE2)) {
							
							if (playerlg.getAnkouVotes() > 0) {
								if (args.length == 0) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§fAide pour la commande §a" + alias + "§f: §r\n§e/" + alias + " <pseudo> §cUtilise votre pouvoir contre un joueur. §r\n§e/" + alias + " reset §cReset votre vote.");
									return true;
								}
								if (args[0].equalsIgnoreCase("help")) {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§fAide pour la commande §a" + alias + "§f: §r\n§e/" + alias + " <pseudo> §cUtilise votre pouvoir contre un joueur. §r\n§e/" + alias + " reset §cReset votre vote.");
									return true;
								}
								
								if (args[0].equalsIgnoreCase("reset") && playerlg.hasUsedPower()) {
									playerlg.addAnkouVote();
									playerlg.setVote(null);
									playerlg.setHasUsedPower(false);
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVotre vote a été réintianilisé.");	
									return true;
								}
								
								if (!playerlg.hasUsedPower()) {
									Player p = Bukkit.getPlayer(args[0]);
									if (p == null) {
										player.sendMessage(main.getPrefix() + main.SendArrow + "§cLe joueur §4\"§e" + args[0] + "§4\"§c n'existe pas.");
										return true;
									}			
									
									playerlg.removeAnkouVote();
									playerlg.setVote(p);
									playerlg.setHasUsedPower(true);
									for (Player spec : main.spectators)
										spec.sendMessage(main.getPrefix() + main.SendArrow + "§cL'" + Roles.ANKOU.getDisplayName() + " §4" + player.getName() + " §ca choisi de voter §6" + p.getName() + "§c.");
								} else {
									player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà voté !");
									return true;
								}
							} else
								player.sendMessage(main.getPrefix() + main.SendArrow + "§cVous avez déjà voté 2 fois !");
							
						}
						
					} else {
						if (playerlg.isVivant())
							player.sendMessage(main.getPrefix() + main.SendArrow + "§cAttendez d'être mort avant d'effectuer cette commande !");
					}
				}
			}
		}
		
		return true;
	}
}
