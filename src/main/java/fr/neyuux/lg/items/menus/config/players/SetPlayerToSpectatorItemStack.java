package fr.neyuux.lg.items.menus.config.players;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SetPlayerToSpectatorItemStack extends CustomItemStack {

    private final PlayerLG playerLG;

    public SetPlayerToSpectatorItemStack(PlayerLG playerLG) {
        super(Material.GHAST_TEAR, 1, "§fMettre §b" + playerLG.getName() + " §fen §7Spectateur§f.");
        this.playerLG = playerLG;

        this.setLore("", "Actuel » PH", "", "§7>>Clique pour modifier");
        if (playerLG.isSpectator()) this.setLoreLine(1, "§bActuel » §aOui");
        else this.setLoreLine(1, "§bActuel » §cNon");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        Player entityPlayer = (Player)player;
        Inventory inv = ((InventoryClickEvent) event).getInventory();
        int slot = CustomItemStack.getSlot(inv, this);

        if (playerLG.isSpectator()) {
            LG.getInstance().getGame().removeSpectator(playerLG);

            playerLG.sendMessage(LG.getPrefix() + entityPlayer.getDisplayName() + " §9vous a retiré du mode §7§lSpectateur§9.");
            entityPlayer.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §cretiré§7 du mode §lSpectateur§7.");
            this.setLoreLine(1, "§bActuel » §cNon");
        } else {
            LG.getInstance().getGame().setSpectator(playerLG);

            entityPlayer.sendMessage(LG.getPrefix() + "§b" + playerLG.getName() + " §7a bien été §amit§7 en §lSpectateur§7.");
            playerLG.sendMessage(LG.getPrefix() + "§b" + entityPlayer.getDisplayName() + "§7 vous a mis en mode §lSpectateur§7.");
            this.setLoreLine(1, "§bActuel » §aOui");
        }
        inv.setItem(slot, this);
    }
}
