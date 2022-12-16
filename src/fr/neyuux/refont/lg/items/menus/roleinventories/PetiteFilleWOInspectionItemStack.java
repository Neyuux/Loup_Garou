package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;
import java.util.Random;

public class PetiteFilleWOInspectionItemStack extends CustomItemStack {

    private final Runnable callback;

    public PetiteFilleWOInspectionItemStack(Runnable callback) {
        super(Material.EYE_OF_ENDER, 1, "§b§lChercher des Loups-Garous");

        this.setLore("§9Se réveille pour chercher des Loups-Garous.", "§9Il y a 20% de chance que vous en §atrouviez §9un.", "§9Cependant, vous avez également 5% de chance de §cmourir§9.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> lgs = game.getLGs(true);
        Random random = new Random();
        int findLG = random.nextInt(4);
        int getKilled = random.nextInt(19);

        if (findLG == 0 || getKilled == 0) {
            if (findLG == 0) {
                PlayerLG foundLG = lgs.get(random.nextInt(lgs.size()));

                playerLG.sendTitle("§e§l" + foundLG.getName() + "§c est §lLoup-Garou§c !", "§bVous avez trouvé un loup.", 10, 80, 10);
                player.sendMessage(LG.getPrefix() + "§bAu loin, vous apercevez §4§l" + foundLG.getName() + " §bdévorer un villageois...");
                ((Player) player).playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 8f, 1.1f);
            }

            if (getKilled == 0) {
                game.kill(playerLG);

                playerLG.sendTitle("§c§lLes Loups-Garous vous ont trouvé !", "§cVous mourrez ce soir...", 10, 90, 10);
                player.sendMessage(LG.getPrefix() + "§cLes Loups-Garous vous ont trouvé ! Vous ne survivez pas cette nuit.");
                GameLG.playNegativeSound((Player) player);
            }
        }

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}