package fr.neyuux.refont.lg.items.config.mainmenu;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class ParametersItemStack extends CustomItemStack {

    public ParametersItemStack() {
        super(Material.APPLE, 1, "§f§lParamètres de la Partie");

        this.setLore("§fPermet de changer les", "§foptions de la partie.");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        //TODO
    }
}
