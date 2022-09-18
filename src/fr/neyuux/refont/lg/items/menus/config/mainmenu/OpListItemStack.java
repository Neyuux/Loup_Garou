package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;

public class OpListItemStack extends CustomItemStack {

    public OpListItemStack() {
        super(Material.SIGN, 1, "�cListe des �lConfigurateurs");

        ArrayList<String> namesList = new ArrayList<>();

        for (HumanEntity human : LG.getInstance().getGame().getOPs())
            namesList.add("�c" + human.getName());

        this.setLore(namesList);
    }

}
