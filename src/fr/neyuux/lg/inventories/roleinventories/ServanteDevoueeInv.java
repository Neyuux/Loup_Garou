package fr.neyuux.lg.inventories.roleinventories;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ServanteDevoueeTakeRoleItemStack;
import fr.neyuux.lg.roles.classes.ServanteDevouee;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;

public class ServanteDevoueeInv extends AbstractCustomInventory {

    private final PlayerLG choosen;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public ServanteDevoueeInv(ServanteDevouee servanteDevouee, PlayerLG playerLG, PlayerLG choosen, Runnable callback) {
        super(servanteDevouee.getDisplayName(), 27);
        this.choosen = choosen;

        this.callback = callback;
        this.playerLG = playerLG;
    }

    @Override
    public void registerItems() {
        this.setAllCorners((byte)14);

        this.setItem(11, new ServanteDevoueeTakeRoleItemStack(choosen, callback));
        this.setItem(13, new CustomItemStack(Material.SKULL_ITEM, 1, choosen.getNameWithAttributes(playerLG) + " §cva mourir.").setDamage(3).setSkullOwner(choosen.getName()).setLore("§eCe joueur va mourir car il a obtenu", "§ele plus de voix au vote du village. Vous", "§epouvez choisir de prendre son rôle avant qu'il ne meure.", "§eIl sera annoncé comme "+ this.getName() + " à sa mort.", "", "§a>>Cliquez sur le bouton §lVERT §apour prendre son rôle", "§c>>Cliquez sur le bouton rouge pour ne §lRIEN§c faire"));
        this.setItem(15, new CancelBarrierItemStack(new ChoosePlayerInv.ActionsGenerator() {
            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                return new String[0];
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                playerLG.getCache().put("unclosableInv", false);
                playerLG.getPlayer().closeInventory();
                LG.getInstance().getGame().cancelWait();
                callback.run();
            }
        }));
    }
}