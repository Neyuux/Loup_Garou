package fr.neyuux.lg.inventories.config.roles;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.items.menus.config.roles.RoleAddingItemStack;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import fr.neyuux.lg.utils.AbstractCustomInventory;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RolesListRolesInv extends AbstractCustomInventory {

    private final Decks deck;
    private final Camps camp;

    public RolesListRolesInv(Decks deck, Camps camp) {
        super("�bR�les �l" + camp.getName() + " �6" + deck.getAlias(), 45);
        this.deck = deck;
        this.camp = camp;

        this.adaptIntToInvSize(getRolesInDeckCamp(deck, camp).size());
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new ReturnArrowItemStack(new RolesCampChooseInv(deck)));

        for (Role role : getRolesInDeckCamp(deck, camp))
            this.addItem(new RoleAddingItemStack(role));
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
            Bukkit.broadcastMessage(LG.getPrefix() + "�4[�cErreur�4] �cUne erreur s'est produite dans la cr�ation des items des decks !");
        }

        return list;
    }
}
