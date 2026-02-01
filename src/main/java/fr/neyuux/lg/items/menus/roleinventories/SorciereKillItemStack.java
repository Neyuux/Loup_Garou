package fr.neyuux.lg.items.menus.roleinventories;

import fr.minuskube.inv.SmartInventory;
import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.inventories.roleinventories.SorciereInv;
import fr.neyuux.lg.roles.classes.Sorciere;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class SorciereKillItemStack extends CustomItemStack {

    private final Sorciere witch;
    private final Runnable callback;
    private final PlayerLG playerLG;

    public SorciereKillItemStack(Runnable callback, PlayerLG playerLG, Sorciere sorciere) {
        super(Material.POTION, 1, "§c§lTuer un joueur");

        this.callback = callback;
        this.witch = sorciere;
        this.playerLG = playerLG;

        Potion potion = new Potion(1);

        this.setLore("§7Vous ouvre un inventaire vous permettant", "§7d'éliminer le joueur de votre choix.", "§cVous pouvez utiliser ce pouvoir qu'une seule fois.", "", "§7>>Clique pour ouvrir");

        this.addGlowEffect();

        potion.setType(PotionType.INSTANT_DAMAGE);
        potion.setSplash(true);
        this.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        potion.apply(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        GameLG game = LG.getInstance().getGame();

        SmartInventory inv = ChoosePlayerInv.getInventory(this.witch.getDisplayName() + " §cPotion de Mort", this.playerLG, game.getAliveExcept(this.playerLG), new ChoosePlayerInv.ActionsGenerator() {

            @Override
            public String[] generateLore(PlayerLG paramPlayerLG) {
                if (paramPlayerLG == null) return new String[0];
                return new String[] {"§cVoulez-vous §cempoisonner " + paramPlayerLG.getNameWithAttributes(playerLG) + "§c ?", "§cIl ne se réveillera pas.", "", "§7>>Clique pour choisir"};
            }

            @Override
            public void doActionsAfterClick(PlayerLG choosenLG) {
                SorciereKillItemStack.this.witch.setKillPot(false);
                game.kill(choosenLG);
                playerLG.sendMessage(LG.getPrefix() + "§cVous avez empoisonné §e§l" + choosenLG.getNameWithAttributes(playerLG) + " §cavec succès.");
                GameLG.playPositiveSound((Player) player);

                
                LG.closeSmartInv(playerLG.getPlayer());
                playerLG.setSleep();
                callback.run();
            }
        }, SorciereInv.getInventory(this.witch, this.playerLG, this.callback));

        
        inv.open((Player) player);
        
    }
}