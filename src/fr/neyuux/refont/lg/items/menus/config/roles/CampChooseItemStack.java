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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CampChooseItemStack extends CustomItemStack {

    private final Decks deck;
    private final Camps camp;

    public CampChooseItemStack(Decks deck, Camps camp) {
        super(Material.WOOL, 1);

        this.deck = deck;
        this.camp = camp;

        if (camp.equals(Camps.LOUP_GAROU)) {
            this.setDamage(14);
            this.setDisplayName("§cCamp des §lLoups-Garous §b" + deck.getAlias());
            this.setLore("§4Affiche les rôles", "§4du camp des loups.", "§7>>Cliquer pour afficher.");
        } else if (camp.equals(Camps.VILLAGE)){
            this.setDamage(5);
            this.setDisplayName("§aCamp du §lVillage §b" + deck.getAlias());
            this.setLore("§2Affiche les rôles", "§2du camp du village.", "§7>>Cliquer pour afficher.");
        } else {
            this.setDamage(1);
            this.setDisplayName("§6Camp des §lAutres §b" + deck.getAlias());
            this.setLore("§eAffiche les rôles qui ne", "§esont ni du village ni des loups.", "§7>>Cliquer pour afficher.");
        }

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        RolesListRolesInv newInv = new RolesListRolesInv(deck, camp);
        newInv.registerItems();

        if (newInv.getItemsMap().size() == 1) {
            GameLG.playNegativeSound((Player) player);
            player.sendMessage(LG.getPrefix() + "§cLe deck §b" + deck.getName() + " §cne contient pas de rôle du camp " + camp.getColor() + "§l" + camp.getName() + " §c!");
        } else
            newInv.open(player);
    }

}
