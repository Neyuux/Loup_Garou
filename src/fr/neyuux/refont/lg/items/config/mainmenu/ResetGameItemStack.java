package fr.neyuux.refont.lg.items.config.mainmenu;

import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;

public class ResetGameItemStack extends CustomItemStack {

    public ResetGameItemStack() {
        super(Material.BARRIER, 1, "�bReset la Map");

        this.setLore("�fPermet de reset", "�fla map.");
    }

    @Override
    public void use(HumanEntity player, ClickType clickType) {
        //TODO open reset game inv
    }
}
