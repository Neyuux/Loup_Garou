package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class Renard extends Role {

    @Override
    public String getDisplayName() {
        return "§6§lRenard";
    }

    @Override
    public String getConfigName() {
        return "Renard";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez sélectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un §c§lLoup-Garou§f, vous §9gardez votre pouvoir§f. Par contre, s'il n'y en a aucun, vous §9perdez votre pouvoir§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §6" + this.getTimeout() + " secondes §fpour renifler quelqu'un.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        super.onPlayerNightTurn(playerLG, callback);

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null) {
                    flair(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, game.getAlive(), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§6Voulez-vous §5renifler " + paramPlayerLG.getNameWithAttributes(playerLG) + "§6 et ses deux voisins ?", "§6Cela vous permettra de savoir si un Loup-Garou", "§6se trouve parmis " + list3Players(paramPlayerLG, ChatColor.GOLD) + "§6.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    flair(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void flair(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        boolean hasLG = false;

        for (PlayerLG nearbyPlayer : choosen.get2NearbyPlayers(true)) if (nearbyPlayer.isLG()) hasLG = true;

        if (hasLG) {
            playerLG.sendMessage(LG.getPrefix() + "§aIl n'y a aucun Loups-Garous parmis " + list3Players(choosen, ChatColor.GREEN) + "§a !");
            GameLG.playPositiveSound(playerLG.getPlayer());
            playerLG.sendMessage(LG.getPrefix() + "§9Vous §cperdez §9votre pouvoir.");
            playerLG.setCanUsePowers(false);

        } else {
            playerLG.sendMessage(LG.getPrefix() + "§cUn Loup-Garou se trouve parmis " + list3Players(choosen, ChatColor.RED) + "§c.");
            playerLG.getPlayer().playSound(playerLG.getLocation(), Sound.WOLF_GROWL, 8f, 1.1f);
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
    


    private String list3Players(PlayerLG playerLG, ChatColor chatColor) {
        return "§e" + playerLG.getName() + chatColor + ", §e" + playerLG.get2NearbyPlayers(false).get(0).getName() + chatColor + " et §e" + playerLG.get2NearbyPlayers(false).get(1).getName();
    }
}
