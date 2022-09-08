package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParameterCupiWinWithCoupleItemStack extends CustomItemStack {

    private final Parameter cupiWinWithCouple;

    public ParameterCupiWinWithCoupleItemStack() {
        super(Material.TRIPWIRE_HOOK, 1, "§9§lCupi§d§lDon §fgagne avec son couple");
        this.cupiWinWithCouple = LG.getInstance().getGame().getConfig().getCupiWinWithCouple();

        this.setLore("§fActive ou non le fait que", "§fCupidon peut gagner avec son couple", "", "§bValeur : " + cupiWinWithCouple.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        cupiWinWithCouple.setValue(!(boolean)cupiWinWithCouple.getValue());
        this.setLoreLine(3, "§bValeur : " + cupiWinWithCouple.getVisibleValue());
    }
}
