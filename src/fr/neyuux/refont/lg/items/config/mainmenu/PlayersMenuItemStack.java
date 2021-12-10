package fr.neyuux.refont.lg.items.config.mainmenu;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class PlayersMenuItemStack extends CustomItemStack {

    public PlayersMenuItemStack(String owner) {
        super(Material.SKULL, 1, "§6Joueurs");

        this.setDamage(3);
        this.setSkullOwner(owner);
        this.setLore("§fPermet de gérer", "§fles joueurs", "§f§o(spectateur, etc)");
    }

    @Override
    public void use(HumanEntity player, ClickType clickType) {
        //TODO open players inv
    }
}
