package fr.neyuux.refont.lg.items.menus.config.roles;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.inventories.config.roles.RoleDecksInv;
import fr.neyuux.refont.lg.inventories.config.roles.RolesCampChooseInv;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DecksItemStack extends CustomItemStack {

    private final Decks deck;

    public DecksItemStack(Decks deck) {
        super(Material.WOOL, 1);

        this.deck = deck;

        if (this.isUsed()) {
            this.setDamage(1);
            this.setDisplayName("§6§l" + deck.getName());
            this.setLore("§6Ce deck est §lUtilisé§6.", "", "§f§lCrédit : " + "§e" + deck.getCreator(), "", "§7>>Cliquez pour ouvrir ce deck");
        } else {
            this.setDamage(5);
            this.setDisplayName("§a§l" + deck.getName());
            this.setLore("§aCe deck est §lUtilisable§a.", "", "§f§lCrédit : " + "§e" + deck.getCreator(), "", "§7>>Cliquez pour ouvrir ce deck");
        }

        this.setLore("§fPermet de gérer les", "§frôles de la partie.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new RolesCampChooseInv(deck).open(player);
    }


    private boolean isUsed() {
        for (Constructor<? extends Role> roleConstructor : LG.getInstance().getGame().getConfig().getAddedRoles()) {
            try {
                if (((Constructor<Role>)roleConstructor).newInstance().getDeck().equals(deck)) return true;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                Bukkit.broadcastMessage(LG.getPrefix() + "§4[§cErreur§4] §cUne erreur s'est produite dans la création des items des decks !");
            }
        }
        return false;
    }

}
