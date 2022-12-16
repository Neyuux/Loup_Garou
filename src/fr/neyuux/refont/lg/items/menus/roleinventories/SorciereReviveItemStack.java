package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.classes.LoupGarou;
import fr.neyuux.refont.lg.roles.classes.Sorciere;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class SorciereReviveItemStack extends CustomItemStack {

    private final PlayerLG targetLG;
    private final Sorciere witch;
    private final Runnable callback;

    public SorciereReviveItemStack(Runnable callback, PlayerLG playerLG, Sorciere sorciere) {
        super(Material.POTION, 1, "§a§lRevive " + LoupGarou.getLastTargetedByLG().getNameWithAttributes(playerLG));

        this.callback = callback;
        this.targetLG = LoupGarou.getLastTargetedByLG();
        this.witch = sorciere;

        this.setLore("§7Vous permet de soigner le corps de §e" + targetLG.getName() + "§7.", "§7Il ne saura pas que vous l'avez réssucité.", "§cVous ne pouvez utiliser ce pouvoir qu'une seule fois.");
        this.addGlowEffect();
        PotionMeta meta = (PotionMeta) this.getItemMeta();
        meta.setMainEffect(PotionEffectType.JUMP);
        this.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);

        this.witch.setHealPot(false);
        LG.getInstance().getGame().getKilledPlayers().remove(this.targetLG);
        playerLG.sendMessage(LG.getPrefix() + "§aVous avez réssucité §e§l" + targetLG.getNameWithAttributes(playerLG) + " §aavec succès.");
        GameLG.playPositiveSound((Player) player);
        player.getOpenInventory().getTopInventory().remove(this);

        if (!this.witch.hasKillPot()) {
            playerLG.getCache().put("unclosableInv", false);
            playerLG.getPlayer().closeInventory();
            playerLG.setSleep();
            callback.run();
        }
    }
}