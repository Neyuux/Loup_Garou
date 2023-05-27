package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.inventories.config.parameters.ParametersGlobalInv;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParametersGlobalItemStack extends CustomItemStack {

    public ParametersGlobalItemStack() {
        super(Material.WATER_LILY, 1, "§a§lParamètres Globaux de la Partie");

        this.setLore("§fPermet de changer les", "§foptions globalles de la partie.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ParametersGlobalInv().open(player);
    }
}
