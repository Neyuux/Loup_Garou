package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ServanteDevoueeTakeRoleItemStack;
import fr.neyuux.lg.roles.classes.ServanteDevouee;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ServanteDevoueeInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14).setDisplayName("§f"));


    private final PlayerLG choosen;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public ServanteDevoueeInv(PlayerLG playerLG, PlayerLG choosen, Runnable callback) {
        this.choosen = choosen;

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(2, 0, GLASS_PANE);
        contents.set(2, 1, GLASS_PANE);
        contents.set(2, 7, GLASS_PANE);
        contents.set(2, 8, GLASS_PANE);

        CancelBarrierItemStack cancel = new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                
                LG.closeSmartInv(playerLG.getPlayer());
                LG.getInstance().getGame().cancelWait();
                callback.run();
            }
        });

        contents.set(1, 6, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        ServanteDevoueeTakeRoleItemStack take = new ServanteDevoueeTakeRoleItemStack(choosen, callback);
        contents.set(1, 2, ClickableItem.of(take, ev -> take.use(ev.getWhoClicked(), ev)));

        contents.set(1, 4, ClickableItem.empty(new CustomItemStack(Material.SKULL_ITEM, 1, choosen.getNameWithAttributes(playerLG) + " §cva mourir.").setDamage(3).setSkullOwner(choosen.getName()).setLore("§eCe joueur va mourir car il a obtenu", "§ele plus de voix au vote du village. Vous", "§epouvez choisir de prendre son rôle avant qu'il ne meure.", "§eIl sera annoncé comme "+ playerLG.getRole().getDisplayName() + "§e à sa mort.", "", "§a>>Cliquez sur le bouton VERT pour §lPRENDRE§a son rôle", "§c>>Cliquez sur le bouton rouge pour ne §lRIEN§c faire")));
    }


    public static SmartInventory getInventory(ServanteDevouee servanteDevouee, PlayerLG playerLG, PlayerLG choosen, Runnable callback) {
        return SmartInventory.builder()
                .id("roleinv_servante")
                .provider(new ServanteDevoueeInv(playerLG, choosen, callback))
                .size(3, 9)
                .title(servanteDevouee.getDisplayName())
                .closeable(false)
                .build();
    }
}