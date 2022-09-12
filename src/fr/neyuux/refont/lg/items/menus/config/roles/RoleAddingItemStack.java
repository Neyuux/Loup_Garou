package fr.neyuux.refont.lg.items.menus.config.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.refont.lg.inventories.config.roles.RolesCampChooseInv;
import fr.neyuux.refont.lg.inventories.config.roles.RolesListRolesInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RoleAddingItemStack extends CustomItemStack {

    private final Decks deck;
    private final Camps camp;

    public RoleAddingItemStack(Decks deck, Camps camp) {
        super(Material.STAINED_GLASS_PANE, 1);
        this.setDamage();

        this.deck = deck;
        this.camp = camp;

        if (camp.equals(Camps.LOUP_GAROU)) {
            this.setDamage(14);
            this.setDisplayName("�cCamp des �lLoups-Garous");
            this.setLore("�4Affiche les r�les", "�4du camp des loups.", "�7>>Cliquer pour afficher.");
        } else if (camp.equals(Camps.VILLAGE)){
            this.setDamage(5);
            this.setDisplayName("�aCamp du �lVillage");
            this.setLore("�2Affiche les r�les", "�2du camp du village.", "�7>>Cliquer pour afficher.");
        } else {
            this.setDamage(1);
            this.setDisplayName("�6Camp des �lAutres");
            this.setLore("�eAffiche les r�les", "�edu camp des autres r�les.", "�7>>Cliquer pour afficher.");
        }
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new RolesListRolesInv(deck, camp).open(player);
    }

}
