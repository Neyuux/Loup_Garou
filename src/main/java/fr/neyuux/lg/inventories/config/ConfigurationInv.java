package fr.neyuux.lg.inventories.config;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.inventories.config.parameters.ParametersInv;
import fr.neyuux.lg.inventories.config.players.ListPlayersInv;
import fr.neyuux.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConfigurationInv implements InventoryProvider {

    public static final ClickableItem GLASS_PANE = ClickableItem.empty(new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14).setDisplayName("§f"));

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("config_main_inv")
            .provider(new ConfigurationInv())
            .size(5, 9)
            .title("§c§lConfiguration")
            .closeable(true)
            .build();


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(0, 0, GLASS_PANE);
        contents.set(0, 1, GLASS_PANE);
        contents.set(0, 7, GLASS_PANE);
        contents.set(0, 8, GLASS_PANE);

        contents.set(1, 0, GLASS_PANE);
        contents.set(1, 8, GLASS_PANE);

        contents.set(3, 0, GLASS_PANE);
        contents.set(3, 8, GLASS_PANE);

        contents.set(4, 0, GLASS_PANE);
        contents.set(4, 1, GLASS_PANE);
        contents.set(4, 7, GLASS_PANE);
        contents.set(4, 8, GLASS_PANE);


        contents.set(3, 3, ClickableItem.of(new CustomItemStack(Material.SKULL_ITEM, 1, (byte)3)
                    .setDisplayName("§6Joueurs")
                    .setLore("§fPermet de gérer", "§fles joueurs", "§f§o(spectateur, etc)")
                    .setSkullOwner(player.getName())
            , onClick -> ListPlayersInv.INVENTORY.open((Player) onClick.getWhoClicked())));

        contents.set(3, 5, ClickableItem.of(new CustomItemStack(Material.BARRIER, 1)
                        .setDisplayName("§bReset la Map")
                        .setLore("§fPermet de reset", "§fla map.")
                , onClick -> ResetInv.INVENTORY.open(player)));

        contents.set(1, 4, ClickableItem.of(new CustomItemStack(Material.APPLE, 1)
                        .setDisplayName("§f§lParamètres de la Partie")
                        .setLore("§fPermet de changer les", "§foptions de la partie.")
                , onClick -> ParametersInv.INVENTORY.open(player)));

        contents.set(1, 6, ClickableItem.of(new CustomItemStack(Material.EMPTY_MAP, 1)
                        .setDisplayName("§6§lRôles")
                        .setLore("§fPermet de gérer les", "§frôles de la partie.")
                , onClick -> RoleDecksInv.INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        contents.set(4, 2, ClickableItem.empty(new CustomItemStack(Material.SIGN)
                .setDisplayName("§cListe des §lConfigurateurs")
                .setLore(LG.getInstance().getGame().getOPs().stream()
                        .map(human -> "§c" + human.getName())
                        .collect(Collectors.toCollection(ArrayList::new)))));

        contents.set(1, 2, ClickableItem.of(new CustomItemStack(Material.ITEM_FRAME, 1)
                        .setDisplayName("§2Changer le §lType §2de jeu")
                        .setLore("§fPermet de changer le", "§ftype de jeu de la partie.", "", "§eActuel : §c§l" + LG.getInstance().getGame().getGameType().getName())
                , onClick -> ChangeGameTypeInv.INVENTORY.open(player)));
    }

}
