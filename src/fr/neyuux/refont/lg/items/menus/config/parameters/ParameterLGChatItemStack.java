package fr.neyuux.refont.lg.items.menus.config.parameters;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.config.Parameter;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class ParameterLGChatItemStack extends CustomItemStack {

    private final Parameter chatLGParameter;

    public ParameterLGChatItemStack() {
        super(Material.SIGN, 1, "§cChat des Loups");
        this.chatLGParameter = LG.getInstance().getGame().getConfig().getChatLG();

        this.setLore("§fActive ou non le chat", "§fentre loups-garous la nuit.", "", "§bValeur : " + chatLGParameter.getVisibleValue(), "", "§7>>Clique pour modifier");
    }

    @Override
    public void use(HumanEntity player, Event event) {
        chatLGParameter.setValue(!(boolean)chatLGParameter.getValue());
        this.setLoreLine(3, "§bValeur : " + chatLGParameter.getVisibleValue());
    }
}
