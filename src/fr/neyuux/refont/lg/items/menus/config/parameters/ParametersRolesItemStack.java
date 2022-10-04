package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.inventories.config.parameters.ParametersRolesInv;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParametersRolesItemStack extends CustomItemStack {

    public ParametersRolesItemStack() {
        super(Material.PAINTING, 1, "�6�lParam�tres des R�les de la Partie");

        this.setLore("�fPermet de changer les", "�foptions des r�les de la partie.");

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        new ParametersRolesInv().open(player);
    }
}
