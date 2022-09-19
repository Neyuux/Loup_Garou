package fr.neyuux.refont.lg.items.menus.config.players;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.config.players.PlayerConfigInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class SetPlayerToSpectatorItemStack extends CustomItemStack {

    private final PlayerLG playerLG;

    public SetPlayerToSpectatorItemStack(PlayerLG playerLG) {
        super(Material.GHAST_TEAR, 1, "§fMettre §b" + playerLG.getName() + " §fen §7Spectateur§f.");
        this.playerLG = playerLG;

        this.setLore("", "Actuel » PH", "", ">>Clique pour modifier");
        if (playerLG.isSpectator()) this.setLoreLine(1, "§bActuel » §aOui");
        else this.setLoreLine(1, "§bActuel » §cNon");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Player entityPlayer = (Player)player;
        if (playerLG.isSpectator()) {
            LG.getInstance().getGame().removeSpectator(playerLG);

            playerLG.sendMessage(LG.getPrefix() + entityPlayer.getDisplayName() + " §9vous a retiré du mode §7§lSpectateur§9.");
            entityPlayer.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §cretiré§7 du mode §lSpectateur§7.");
        } else {
            LG.getInstance().getGame().setSpectator(playerLG);

            entityPlayer.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §amit§7 en §lSpectateur§7.");
            playerLG.sendMessage(LG.getPrefix() + "§b" + entityPlayer.getDisplayName() + "7 vous a mis en mode §lSpectateur§7.");
        }
        this.updateInInv(((InventoryEvent)event).getInventory());
    }
}
