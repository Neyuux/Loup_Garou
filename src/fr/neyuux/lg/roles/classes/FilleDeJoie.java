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
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class FilleDeJoie extends Role {

    @Override
    public String getDisplayName() {
        return "§d§lFille de Joie";
    }

    @Override
    public String getConfigName() {
        return "Fille de Joie";
    }

    @Override
    public String getDeterminingName() {
        return "de la " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez aller ken un joueur. Si ce joueur est un Loup ou est mangé par les Loups, vous §9mourrez§f. Si les Loups essaient de vous tuer pendant que vous êtes chez quelqu'un d'autre, vous §9survivez§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d" + this.getTimeout() + " secondes§f pour choisir chez qui aller cette nuit.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    fuck(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§dVoulez-vous §cbaiser §e" + paramPlayerLG.getNameWithAttributes(playerLG) + "§dce soir ?", "§dS'il est Loup-Garou ou visé", "§dpar ceux-ci cette nuit, vous§cmourrez§d.", "§dSi les Loups vous choisissent cette nuit, vous §asurvivrez§d.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    fuck(choosenLG, playerLG);

                    playerLG.getCache().put("unclosableInv", false);
                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private void fuck(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return;

        playerLG.getCache().put("filleDeJoieInHomeOf", choosen);

        playerLG.sendMessage(LG.getPrefix() + "§dTu baises §e" + choosen.getNameWithAttributes(playerLG) + "§d ce soir.");
        GameLG.playPositiveSound(playerLG.getPlayer());

        if (LG.getInstance().getGame().getLGs(true).contains(choosen)) {
            playerLG.sendMessage(LG.getPrefix() + "§cEn allant chez lui, tu t'aperçois qu'il est Loup-Garou. Vous ne survivrez pas cette nuit");
            choosen.sendMessage(LG.getPrefix() + "§dTu découvres §e" + playerLG.getNameWithAttributes(choosen) + "§d qui s'introduit chez toi pour passer du bon temps. Tu décides donc de le tuer.");
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getCache().put("unclosableInv", false);
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }


    @EventHandler
    public void onCloseFilleDeJoieInv(InventoryCloseEvent ev) {
        Inventory inv = ev.getInventory();
        HumanEntity player = ev.getPlayer();
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        if (inv.getName().equals(this.getDisplayName()) && (boolean)playerLG.getCache().get("unclosableInv")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerLG.getCache().put("unclosableInv", false);
                    player.openInventory(inv);
                    playerLG.getCache().put("unclosableInv", true);
                }
            }.runTaskLater(LG.getInstance(), 1L);
        }
    }

    @EventHandler
    public void onLGChoice(RoleChoiceEvent ev) {
        PlayerLG choosenLG = ev.getChoosen();
        GameLG game = LG.getInstance().getGame();

        for (PlayerLG fdjLG : game.getPlayersByRole(this.getClass()))
            if (ev.getRole() instanceof LoupGarou)
                if (fdjLG.equals(choosenLG)) {
                    if (fdjLG.getCache().has("filleDeJoieInHomeOf")) {
                        ev.setCancelled(true);
                    }
                } else if (fdjLG.getCache().has("filleDeJoieInHomeOf") && fdjLG.getCache().get("filleDeJoieInHomeOf").equals(choosenLG)) {
                    LG.getInstance().getGame().kill(fdjLG);
                    game.getLGs(true).forEach(playerLG -> playerLG.sendMessage(LG.getPrefix() + "§dEn vous rendant chez §e" + choosenLG.getName() + "§d, vous découvrez " + fdjLG.getNameWithAttributes(playerLG) + "§d à poil. Vous la tuez donc au passage."));
                }
    }
}
