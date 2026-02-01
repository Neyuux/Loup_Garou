package fr.neyuux.lg.inventories.roleinventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.items.menus.CancelBarrierItemStack;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.roleinventories.ChoosePlayerItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public class ChoosePlayerInv implements InventoryProvider {

    private final PlayerLG receiverLG;
    private final ActionsGenerator generator;
    private List<PlayerLG> choosable;
    private SmartInventory returnInv;

    public ChoosePlayerInv(PlayerLG receiverLG, List<PlayerLG> choosable, ActionsGenerator generator) {
        this.receiverLG = receiverLG;
        this.generator = generator;
        this.choosable = choosable;
        if (choosable == null)
            this.choosable = LG.getInstance().getGame().getAlive();
    }

    public ChoosePlayerInv(PlayerLG receiverLG, List<PlayerLG> choosable, ActionsGenerator generator, SmartInventory returnInv) {
        this.receiverLG = receiverLG;
        this.generator = generator;
        this.choosable = choosable;
        this.returnInv = returnInv;
        if (choosable == null)
            this.choosable = LG.getInstance().getGame().getAlive();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        if (returnInv == null) {
            CancelBarrierItemStack cancel = new CancelBarrierItemStack(generator);
            contents.set(LG.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size()) - 1, 8, ClickableItem.of(cancel, ev -> cancel.use(ev.getWhoClicked(), ev)));
        } else {
            contents.set(LG.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size()) - 1, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> returnInv.open((Player) ev.getWhoClicked())));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        for (int i = 0; i < choosable.size(); i++) {
            ChoosePlayerItemStack choosePlayerItemStack = new ChoosePlayerItemStack(receiverLG, choosable.get(i), generator);
            contents.set(i / 9,  i % 9, ClickableItem.of(choosePlayerItemStack, ev -> choosePlayerItemStack.use(ev.getWhoClicked(), ev)));
        }
    }


    public static SmartInventory getInventory(String name, PlayerLG receiverLG, List<PlayerLG> choosable, ActionsGenerator generator) {
        return SmartInventory.builder()
                .id("chooseplayerinv")
                .provider(new ChoosePlayerInv(receiverLG, choosable, generator))
                .size(LG.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size()), 9)
                .title(name)
                .closeable(false)
                .build();
    }

    public static SmartInventory getInventory(String name, PlayerLG receiverLG, List<PlayerLG> choosable, ActionsGenerator generator, SmartInventory returnInv) {
        return SmartInventory.builder()
                .id("chooseplayerinv")
                .provider(new ChoosePlayerInv(receiverLG, choosable, generator, returnInv))
                .size(LG.adaptIntToInvSize(LG.getInstance().getGame().getAlive().size()), 9)
                .title(name)
                .closeable(false)
                .build();
    }

    public interface ActionsGenerator {
        String[] generateLore(PlayerLG paramPlayerLG1);
        void doActionsAfterClick(PlayerLG paramPlayerLG2);
    }
}
