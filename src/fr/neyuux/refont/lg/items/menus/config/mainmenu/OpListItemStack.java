package fr.neyuux.refont.lg.items.menus.config.mainmenu;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;

import java.util.ArrayList;

public class OpListItemStack extends CustomItemStack {

    public OpListItemStack() {
        super(Material.SIGN, 1, "§cListe des §lConfigurateurs");

        ArrayList<String> namesList = new ArrayList<>();

        for (PlayerLG playerLG : LG.getInstance().getGame().getOPs())
            namesList.add(playerLG.getDisplayName());

        this.setLore(namesList);
    }

}
