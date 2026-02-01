package fr.neyuux.lg.inventories.config.roles;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.roles.RoleAddingItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RolesListRolesInv implements InventoryProvider {

    private final Decks deck;
    private final Camps camp;

    public RolesListRolesInv(Decks deck, Camps camp) {
        this.deck = deck;
        this.camp = camp;
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        contents.set(LG.adaptIntToInvSize(getRolesInDeckCamp(deck, camp).size()) - 1, 8, ClickableItem.of(new ReturnArrowItemStack(), ev -> RolesCampChooseInv.getInventory(deck).open((Player) ev.getWhoClicked())));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        List<Role> rolesInDeckCamp = getRolesInDeckCamp(deck, camp);
        for (int i = 0; i < rolesInDeckCamp.size(); i++) {
            Role role = rolesInDeckCamp.get(i);

            RoleAddingItemStack addingItemStack = new RoleAddingItemStack(role);
            contents.set(i / 9, i % 9, ClickableItem.of(addingItemStack, ev -> addingItemStack.use(ev.getWhoClicked(), ev)));
        }
    }


    public static SmartInventory getInventory(Decks deck, Camps camp) {
        return SmartInventory.builder()
                .id("roles_list")
                .provider(new RolesListRolesInv(deck, camp))
                .size(LG.adaptIntToInvSize(getRolesInDeckCamp(deck, camp).size()), 9)
                .title("§bRôles §l" + camp.getName() + " §6" + deck.getAlias())
                .closeable(true)
                .build();
    }



    public static List<Role> getRolesInDeckCamp(Decks deck, Camps camp) {
        List<Role> list = new ArrayList<>();

        try {
            for (Constructor<? extends Role> roleConstructor : LG.getInstance().getRoles().values()) {
                Role role = ((Constructor<Role>)roleConstructor).newInstance();

                if (role.getDeck().equals(deck)) {
                    if (role.getBaseCamp().equals(camp)) list.add(role);
                    else if (role.getBaseCamp().equals(Camps.VAMPIRE) && camp.equals(Camps.AUTRE)) list.add(role);
                }
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cUne erreur s'est produite dans la création des items des decks !");
        }

        return list;
    }
}
