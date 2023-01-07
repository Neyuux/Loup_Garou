package fr.neyuux.lg.items.hotbar;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;

public class JoinGameEnderBallItemStack extends CustomItemStack {

   public JoinGameEnderBallItemStack() {
       super(Material.EYE_OF_ENDER, 1, "§a§lJouer");

       this.setLore("§fMets le joueur dans la liste", "§fdes participants de la partie", "§b>>Clique droit");

       addItemInList(this);
   }

    @Override
    public void use(HumanEntity player, Event event) {
        LG.getInstance().getGame().joinGame(PlayerLG.createPlayerLG(player));
    }
}
