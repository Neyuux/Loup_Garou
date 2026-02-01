package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.SorciereInv;
import fr.neyuux.lg.roles.classes.LoupGarou;
import fr.neyuux.lg.roles.classes.Sorciere;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class SorciereReviveItemStack extends CustomItemStack {

    private final PlayerLG targetLG;
    private final Sorciere witch;
    private final Runnable callback;

    public SorciereReviveItemStack(Runnable callback, PlayerLG playerLG, Sorciere sorciere) {
        super(Material.POTION, 1, "§a§lRevive " + LoupGarou.getLastTargetedByLG().getNameWithAttributes(playerLG));

        this.callback = callback;
        this.targetLG = LoupGarou.getLastTargetedByLG();
        this.witch = sorciere;

        Potion potion = new Potion(1);

        this.setLore("§7Vous permet de soigner le corps de §e" + targetLG.getName() + "§7.", "§7Il ne saura pas que vous l'avez réssucité.", "§cVous ne pouvez utiliser ce pouvoir qu'une seule fois.");

        this.addGlowEffect();

        potion.setSplash(true);
        potion.setType(PotionType.JUMP);
        this.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        potion.apply(this);

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        this.witch.setHealPot(false);
        LG.getInstance().getGame().getKilledPlayers().remove(this.targetLG);
        playerLG.sendMessage(LG.getPrefix() + "§aVous avez réssucité §e§l" + targetLG.getNameWithAttributes(playerLG) + " §aavec succès.");
        GameLG.playPositiveSound((Player) player);
        
        SorciereInv.getInventory(this.witch, playerLG, this.callback).open((Player) player);

        if (!this.witch.hasKillPot()) {
            LG.closeSmartInv(playerLG.getPlayer());
            playerLG.setSleep();
            callback.run();
        }
    }
}