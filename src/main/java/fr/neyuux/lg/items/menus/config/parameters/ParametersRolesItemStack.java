package fr.neyuux.lg.items.menus.config.parameters;

import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;

public class ParametersRolesItemStack extends CustomItemStack {

    public ParametersRolesItemStack() {
        super(Material.PAINTING, 1, "§6§lParamètres des Rôles de la Partie");

        this.setLore("§fPermet de changer les", "§foptions des rôles de la partie.");

    }
}
