package fr.neyuux.refont.lg.inventories.config.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.refont.lg.items.menus.config.roles.RoleAddingItemStack;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.AbstractCustomInventory;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RolesListRolesInv extends AbstractCustomInventory {

    private final Decks deck;
    private final Camps camp;

    public RolesListRolesInv(Decks deck, Camps camp) {
        super("§6" + deck.getAlias() + "§b" + camp.getName(), 45);
        this.deck = deck;
        this.camp = camp;

        this.adaptIntToInvSize(this.getRolesInDeckCamp().size());
    }

    @Override
    public void registerItems() {
        this.setItem(this.getSize() - 1, new ReturnArrowItemStack(new RolesCampChooseInv(deck)));

        for (Role role : this.getRolesInDeckCamp()) {
            this.addItem(new RoleAddingItemStack(role));
        }
    }



    private List<Role> getRolesInDeckCamp() {
        List<Role> list = new ArrayList<>();
        GameLG game = LG.getInstance().getGame();

        try {
            for (Constructor<? extends Role> roleConstructor : LG.getInstance().getRoles().values()) {
                Role role = ((Constructor<Role>)roleConstructor).newInstance(game);

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
