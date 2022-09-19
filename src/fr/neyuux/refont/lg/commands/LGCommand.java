package fr.neyuux.refont.lg.commands;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

public class LGCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String helpMessage = "§fAide pour la commande §c"+alias+"§f :§r\n§c/"+alias+" spec §a<on/off/add/remove/list/clear>\n§c/"+alias+" compo" +
                "\n§c/"+alias+" ankou §a[joueur]\n§c/" + alias + " op §a<add/remove/list>\n§c/"+alias+ " play §a<list/add/remove>";

        if (args.length > 0) {
            switch(args[0]) {
                case "spec":
                    final String helpspecmessage = "§6La commande spec gérer les spectateurs de la partie.\nArgument possibles : \n" +
                            "§con §6: Devient spectateur\n§coff §6: Retire le mode spectateur\n" +
                            "§clist §6: Affiche la liste des spectateurs.\n" +
                            "§cadd §6: Ajoute un joueur au mode spectateur\n" +
                            "§cremove§6: Retire un joueur au mode spectateur\n" +
                            "§cclear §6: Retire tous les joueurs du mode spectateur";
                    if (args.length == 1) {
                        sender.sendMessage(LG.getPrefix() + helpspecmessage);
                    } else {
                        switch (args[1]) {
                            case "on":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.
                                }
                             break;
                            default:
                                sender.sendMessage(LG.getPrefix() + helpspecmessage);
                            break;
                        }
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
