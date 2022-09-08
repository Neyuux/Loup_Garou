package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.MayorSuccession;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParameterMayorSuccessionItemStack extends CustomItemStack {

    private final Parameter mayorSuccessionParameter;

    public ParameterMayorSuccessionItemStack() {
        super(Material.ARMOR_STAND, 1, "§9Succession du maire");
        this.mayorSuccessionParameter = LG.getInstance().getGame().getConfig().getMayorSuccession();

        this.setLore("§fChange le type de succession du maire", "", "§bValeur : " + mayorSuccessionParameter.getVisibleValue(), ((MayorSuccession)mayorSuccessionParameter.getValue()).getDescription(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        mayorSuccessionParameter.setValue(MayorSuccession.getNext(((MayorSuccession)mayorSuccessionParameter.getValue()).getId()));
        this.setLoreLine(4, "§bValeur : " + mayorSuccessionParameter.getVisibleValue());
        this.setLoreLine(5, ((MayorSuccession)mayorSuccessionParameter.getValue()).getDescription());
    }
}
