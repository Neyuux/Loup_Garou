package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
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
        super(Material.EYE_OF_ENDER, 1, "�b�lChercher des Loups-Garous");

        this.setLore("�9Se r�veille pour chercher des Loups-Garous.", "�9Il y a 20% de chance que vous en �atrouviez �9un.", "�9Cependant, vous avez �galement 5% de chance de �cmourir�9.", "", "�7>>Clique pour s�lectionner");

        this.callback = callback;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> lgs = game.getLGs(true);
        Random random = new Random();
        boolean findLG = random.nextDouble() * 100 <= 20;
        boolean getKilled = random.nextDouble() * 100 <= 5;

        if (findLG || getKilled) {
            if (findLG) {
                PlayerLG foundLG = lgs.get(random.nextInt(lgs.size()));

                playerLG.sendTitle("�e�l" + foundLG.getName() + "�c est �lLoup-Garou�c !", "�bVous avez trouv� un loup.", 10, 80, 10);
                player.sendMessage(LG.getPrefix() + "�bAu loin, vous apercevez �4�l" + foundLG.getName() + " �bd�vorer un villageois...");
                ((Player) player).playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 8f, 1.1f);
            }

            if (getKilled) {
                game.kill(playerLG);

                playerLG.sendTitle("�c�lLes Loups-Garous vous ont trouv� !", "�cVous mourrez ce soir...", 10, 90, 10);
                player.sendMessage(LG.getPrefix() + "�cLes Loups-Garous vous ont trouv� ! Vous ne survivez pas cette nuit.");
                GameLG.playNegativeSound((Player) player);
            }
        } else {
            playerLG.sendTitle("�a�lRien.", "�bVous n'avez rien trouv� ce soir.", 10, 90, 10);
            player.sendMessage(LG.getPrefix() + "�bLors de votre escapade nocturne, vous n'avez rien aper�u.");
            GameLG.playPositiveSound((Player) player);
        }

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}