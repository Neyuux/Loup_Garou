package fr.neyuux.lg.commands;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.VoteLG;
import fr.neyuux.lg.enums.GameState;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.inventories.config.ConfigurationInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.roles.classes.Ankou;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LGCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        String helpMessage = "§fAide pour la commande §c"+alias+"§f :§r\n§c/"+alias+" spec §a<on/off/add/remove/list/clear>\n§c/"+alias+" compo" +
                "\n§c/"+alias+" ankou §a[joueur]\n§c/" + alias + " op §a<add/remove/list>\n§c/"+alias+ " players §a<list/add/remove>";
        GameLG game = LG.getInstance().getGame();

        if (args.length > 0) {
            switch(args[0].toLowerCase()) {
                case "spec":
                case "spectateur":
                case "spectator":
                    final String helpspecmessage = "§6La commande spec permet de gérer les spectateurs de la partie.\nArgument possibles : \n" +
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
                                    GameLG.playPositiveSound(playerLG.getPlayer());
                                }
                             break;
                            case "off":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                                    if (playerLG.isSpectator()) LG.getInstance().getGame().removeSpectator(playerLG);
                                    GameLG.playPositiveSound(playerLG.getPlayer());
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
                        sender.sendMessage(LG.getPrefix() + "§eListe des rôles présents dans la composition :");

                        for (Camps camp : Camps.values()) {

                            sender.sendMessage(" §0§l\u25a0 " + camp.getColor() + "§l" + camp.getName() + " §7 :");

                            for (Decks decks : Decks.values())
                                for (Constructor<? extends Role> constructor : LG.getInstance().getGame().getConfig().getAddedRoles())
                                    try {
                                        Role role = ((Constructor<Role>) constructor).newInstance();
                                        if (role.getBaseCamp().equals(camp) && role.getDeck().equals(decks)) sender.sendMessage("  " + camp.getColor() + "§l- " + role.getDisplayName());
                                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                                        e.printStackTrace();
                                        Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cUne erreur s'est produite dans la création des items des decks !");
                                    }

                            sender.sendMessage(" ");
                        }
                    } else {
                        sender.sendMessage(LG.getPrefix() + "§eListe des rôles encore présents dans la partie :");

                        boolean b = false;
                        for (Camps camp : Camps.values()) {

                            for (Role role : LG.getInstance().getGame().getAliveRoles())
                                if (role.getBaseCamp().equals(camp)) {
                                    if (!b) {
                                        sender.sendMessage(" §0§l\u25a0 " + camp.getColor() + "§l" + camp.getName() + " §7 :");
                                        b = true;
                                    }
                                    sender.sendMessage("  " + camp.getColor() + "§l- " + role.getDisplayName());
                                }

                            sender.sendMessage(" ");
                        }
                    }

                break;

                case "ankou":
                    final String helpankoumessage = "§6La commande ankou permet de voter lorsque l'Ankou est mort.\nArgument possibles : \n" +
                            "§a[joueur] §6: Joueur choisi\n" +
                            "§creset §6: Permet de reset son vote";
                    VoteLG vote = game.getVote();

                    if (game.getGameState().equals(GameState.PLAYING)) {
                        if (this.checkHuman(sender)) {
                            PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                            if (playerLG.getRole() instanceof Ankou) {
                                if (playerLG.isDead() && vote.getVoters().contains(playerLG)) {
                                    if (args.length > 1) {
                                        playerLG.callbackChoice(PlayerLG.createPlayerLG(Bukkit.getPlayer(args[1])));
                                    } else
                                        sender.sendMessage(LG.getPrefix() + helpankoumessage);
                                } else
                                    sender.sendMessage(LG.getPrefix() + "§cVous ne pouvez pas encore utiliser cette commande !");
                            } else
                                sender.sendMessage(LG.getPrefix() + "§cCe n'est pas votre rôle.");
                        } else
                            sender.sendMessage(LG.getPrefix() + "§cVous devez être un joueur pour effectuer cette commande.");
                    } else
                        sender.sendMessage(LG.getPrefix() + "§cLa partie n'est même pas commencée !");
                break;

                case "op":
                case "operator":
                case "operateur":
                    final String helpopcommand = "§6La commande op permet de gérer les gérants de la partie.\nArgument possibles : \n" +
                            "§clist §6: Affiche la liste des op.\n" +
                            "§cadd §e<joueur>§6: OP un joueur\n" +
                            "§cremove §e<joueur>§6: DéOP un joueur\n";
                    if (args.length == 1) {
                        sender.sendMessage(LG.getPrefix() + helpopcommand);
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "list":
                                sender.sendMessage(LG.getPrefix() + "§cListe des OPs :");
                                LG.getInstance().getGame().getOPs().forEach(playerLG -> sender.sendMessage(" §c - §f" + playerLG.getName()));
                                break;
                            case "add":
                                if (sender.isOp()) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (!playerLG.isOP()) {

                                                LG.getInstance().getGame().OP(playerLG);
                                                Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §ca OP §5§l" + player.getName() + "§c.");

                                            } else
                                                sender.sendMessage(LG.getPrefix() + "§cCe joueur est déjà §5§lOP §c!");
                                        } else
                                            sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpopcommand);
                                } else
                                    sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                                break;
                            case "remove":
                                if (sender.isOp()) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (playerLG.isOP()) {

                                                LG.getInstance().getGame().unOP(playerLG);
                                                Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §ca déOP §5§l" + player.getName() + "§c.");

                                            } else
                                                sender.sendMessage(LG.getPrefix() + "§cCe joueur n'est pas §5§lOP §c!");
                                        } else
                                            sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpopcommand);
                                } else
                                    sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                                break;

                            default:
                                sender.sendMessage(LG.getPrefix() + helpopcommand);
                            break;
                        }
                    }
                break;

                case "players":
                case "player":
                case "playing":
                    final String helpplayerscommand = "§6La commande players permet de gérer les participants de la partie.\nArgument possibles : \n" +
                            "§clist §6: Affiche la liste des participants.\n" +
                            "§cadd §e<joueur>§6: Ajoute un joueur à la liste des participants\n" +
                            "§cremove §e<joueur>§6: Retire un joueur de la liste des participants\n";
                    if (args.length == 1) {
                        sender.sendMessage(LG.getPrefix() + helpplayerscommand);
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "list":
                                sender.sendMessage(LG.getPrefix() + "§aListe des participants :");
                                LG.getInstance().getGame().getPlayersInGame().forEach(playerLG -> sender.sendMessage(" §a - §f" + playerLG.getName()));
                                break;
                            case "add":
                                if (!this.checkHuman(sender) || LG.getInstance().getGame().getOPs().contains((HumanEntity) sender)) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (!playerLG.isInGame()) {

                                                if (LG.getInstance().getGame().joinGame(playerLG))
                                                    Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §aa ajouté §e§l" + player.getDisplayName() + "§a à la liste des participants.");
                                                else
                                                    sender.sendMessage(LG.getPrefix() + "§cCe joueur ne peut pas rejoindre la partie si le type de jeu n'est pas paramétré.");
                                            } else
                                                sender.sendMessage(LG.getPrefix() + "§cCe joueur est déjà dans la liste des participants !");
                                        } else
                                            sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpplayerscommand);
                                } else
                                    sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                                break;
                            case "remove":
                                if (!this.checkHuman(sender) || LG.getInstance().getGame().getOPs().contains((HumanEntity) sender)) {
                                    if (args.length > 2) {

                                        Player player = Bukkit.getPlayer(args[2]);
                                        if (player != null) {
                                            PlayerLG playerLG = PlayerLG.createPlayerLG(player);

                                            if (playerLG.isInGame()) {

                                                LG.getInstance().getGame().leaveGame(playerLG);
                                                Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §ca retiré §e§l" + player.getDisplayName() + "§c de la liste des participants.");

                                            } else
                                                sender.sendMessage(LG.getPrefix() + "§cCe joueur n'est pas dans la liste des participants !");
                                        } else
                                            sender.sendMessage(LG.getPrefix() + "§cLe joueur §4\"§e" + args[2] + "§4\" §cn'existe pas.");
                                    } else sender.sendMessage(LG.getPrefix() + helpplayerscommand);
                                } else
                                    sender.sendMessage(LG.getPrefix() + "§cVous n'avez pas la permission d'exécuter cette commande.");
                                break;

                            case "on":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                                    if (!playerLG.isInGame()) LG.getInstance().getGame().joinGame(playerLG);
                                }
                                break;
                            case "off":
                                if(this.checkHuman(sender)) {
                                    PlayerLG playerLG = PlayerLG.createPlayerLG((HumanEntity) sender);

                                    if (playerLG.isInGame()) LG.getInstance().getGame().leaveGame(playerLG);
                                }
                                break;
                            default:
                                sender.sendMessage(LG.getPrefix() + helpplayerscommand);
                                break;
                        }
                    }
                break;
                case "addspawn":
                    if (checkHuman(sender)) {
                        HumanEntity human = (HumanEntity) sender;
                        PlayerLG playerLG = PlayerLG.createPlayerLG(human);
                        GameType gt = LG.getInstance().getGame().getGameType();

                        if (playerLG.isOP()) {
                            if (gt != GameType.NONE) {
                                Location loc = human.getLocation();
                                List<Object> list = (List<Object>) LG.getInstance().getConfig().getList("spawns." + gt);

                                list.add(Arrays.asList((double) loc.getBlockX(), loc.getY(), (double) loc.getBlockZ(), (double) loc.getYaw(), (double) loc.getPitch()));

                                LG.getInstance().saveConfig();
                                sender.sendMessage(LG.getPrefix() + "§aLa position a bien été ajoutée !");
                                GameLG.playPositiveSound(playerLG.getPlayer());
                            } else {
                                sender.sendMessage(LG.getPrefix() + "§cVous devez choisir le type de jeu avant de créer des emplacements !");
                                GameLG.playNegativeSound(playerLG.getPlayer());
                            }

                        } else {
                            sender.sendMessage(LG.getPrefix() + "§cVous devez être OP pour ajouter des emplacements !");
                            GameLG.playNegativeSound(playerLG.getPlayer());
                        }
                    }
                break;
                case "alive":
                    if (game.getGameState().equals(GameState.PLAYING)) {
                        List<String> list = new ArrayList<>();
                        game.getAlive().forEach(playerLG -> list.add(playerLG.getName()));

                        list.sort(String.CASE_INSENSITIVE_ORDER);

                        sender.sendMessage(list.stream().map(s -> "§6§l- §6" + s + "\n").collect(Collectors.joining("", LG.getPrefix() + "§eListe des joueurs encore vie : \n", "")));
                    }
                break;
                case "end":
                case "reset":
                case "stop":
                    if (sender.isOp()) {
                        Bukkit.broadcastMessage(LG.getPrefix() + "§b" + sender.getName() + " §ea reset le jeu !");
                        LG.getInstance().newGame();
                    } else
                        sender.sendMessage(LG.getPrefix() + "§cVous devez être OP pour effectuer cette commande.");
                break;
                case "admin":
                    if (sender.isOp()) {
                        if (checkHuman(sender))
                            ConfigurationInv.INVENTORY.open((Player) sender);
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
