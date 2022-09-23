package fr.neyuux.refont.lg.commands;

import fr.neyuux.refont.lg.GameState;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class LGCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String helpMessage = "§fAide pour la commande §c"+alias+"§f :§r\n§c/"+alias+" spec §a<on/off/add/remove/list/clear>\n§c/"+alias+" compo" +
                "\n§c/"+alias+" ankou §a[joueur]\n§c/" + alias + " op §a<add/remove/list>\n§c/"+alias+ " play §a<list/add/remove>";

        if (args.length > 0) {
            switch(args[0].toLowerCase()) {
                case "spec":
                case "spectateur":
                case "spectator":
                    final String helpspecmessage = "§6La commande spec gérer les spectateurs de la partie.\nArgument possibles : \n" +
                            "§con §6: Devient spectateur\n§coff §6: Retire le mode spectateur\n" +
                            "§clist §6: Affiche la liste des spectateurs.\n" +
                            "§cadd §e<joueur>§6: Ajoute un joueur au mode spectateur\n" +
                            "§cremove §e<joueur>§6: Retire un joueur au mode spectateur\n" +
                            "§cclear §6: Retire tous les joueurs du mode spectateur";
                    if (args.length == 1) {
                        sender.sendMessage(LG.getPrefix() + helpspecmessage);
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "on":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                                    if (!playerLG.isSpectator()) LG.getInstance().getGame().setSpectator(playerLG);
                                }
                             break;
                            case "off":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                                    if (!playerLG.isSpectator()) LG.getInstance().getGame().removeSpectator(playerLG);
                                }
                            break;
                            case "list":
                                sender.sendMessage(LG.getPrefix() + "§7Liste des Spectateurs :");
                                LG.getInstance().getGame().getSpectators().forEach(playerLG -> sender.sendMessage(" §7 - §f" + playerLG.getName()));
                            break;
                            case "add":
                                if (!this.checkHuman(sender) || LG.getInstance().getGame().getOPs().contains((HumanEntity) sender)) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (!playerLG.isSpectator()) {

                                                LG.getInstance().getGame().setSpectator(playerLG);
                                                sender.sendMessage(LG.getPrefix() + "§b" + player.getName() + " §7a bien été §amit§7 en §lSpectateur§7.");
                                                player.sendMessage(LG.getPrefix() + "§b" + sender.getName() + "§7 vous a mis en mode §lSpectateur§7.");

                                            } else sender.sendMessage(LG.getPrefix() + "§cCe joueur est déjà en mode §7§lSpectateur §c!");
                                        } else sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpspecmessage);
                                } else sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                            break;
                            case "remove":
                                if (!this.checkHuman(sender) || LG.getInstance().getGame().getOPs().contains((HumanEntity) sender)) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (playerLG.isSpectator()) {

                                                LG.getInstance().getGame().removeSpectator(playerLG);
                                                sender.sendMessage(LG.getPrefix() + player.getDisplayName() + " §9vous a retiré du mode §7§lSpectateur§9.");
                                                player.sendMessage(LG.getPrefix() + "§b" + sender.getName() + " §7a bien été §cretiré§7 du mode §lSpectateur§7.");

                                            } else sender.sendMessage(LG.getPrefix() + "§cCe joueur n'est pas en mode §7§lSpectateur §c!");
                                        } else sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpspecmessage);
                                } else sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                            break;
                            case "clear":
                                if (!this.checkHuman(sender) || LG.getInstance().getGame().getOPs().contains((HumanEntity) sender)) {

                                    Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §cretire tous les joueurs du mode §7§lSpectateur §c!");
                                    for (PlayerLG playerLG : PlayerLG.getPlayersMap().values())
                                        if (playerLG.isSpectator()) LG.getInstance().getGame().removeSpectator(playerLG);
                                }
                                    break;

                            default:
                                sender.sendMessage(LG.getPrefix() + helpspecmessage);
                            break;
                        }
                    }
                break;

                case "compo":
                case "roles":
                case "composition":
                    if (LG.getInstance().getGame().getGameState() != GameState.PLAYING) {
                        sender.sendMessage(LG.getPrefix() + );
                    }

                break;

                default:
                    sender.sendMessage(LG.getPrefix() + "§cCommande introuvable !");
                    sender.sendMessage(LG.getPrefix() + helpMessage);
                break;
            }
        } else {
            sender.sendMessage(LG.getPrefix() + helpMessage);
        }

        return true;
    }

    private boolean checkHuman(CommandSender sender) {
        if (sender instanceof HumanEntity)
            return true;
        else {
            sender.sendMessage(LG.getPrefix() + "§4[§cErreur§4]§c Vous devez être un joueur pour effectuer cette commande.");
            return false;
        }
    }
}
