package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;

public class ParametersGlobalItemStack extends CustomItemStack {

    public ParametersGlobalItemStack() {
        super(Material.WATER_LILY, 1, "§a§lParamètres Globaux de la Partie");

        this.setLore("§fPermet de changer les", "§foptions globalles de la partie.");

    }

}
