package fr.neyuux.refont.lg.items.menus.roleinventories;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.classes.InfectPereDesLoups;
import fr.neyuux.refont.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class InfectPereDesLoupsInfectItemStack extends CustomItemStack {

    private final Runnable callback;
    private final PlayerLG choosenLG;

    public InfectPereDesLoupsInfectItemStack(Runnable callback, PlayerLG choosenLG) {
        super(Material.STAINED_CLAY, 1, "§2§lInfecter ");

        if (choosenLG != null) {
            this.setDamage(5);
            this.setDisplayName("§2§lInfecter " + choosenLG.getDisplayName());
            this.setLore("§eFaire un coup d'état vous permet", "§ed'être le seul à pouvoir voter.", "§eSi vous votez pour un §aVillageois§e,", "§evous vous suiciderez le lendemain.", "§eSinon, vous deviendrez maire du village.", "", "§7>>Clique pour sélectionner");

        } else {
            this.setDamage(14);
            this.setDisplayName("§c§lPersonne n'est mort cette nuit");
            this.setLore("§eVous ne pouvez pas infecter ce tour" + "§e car les loups n'ont tué aucun joueur.");
        }


        this.callback = callback;
        this.choosenLG = choosenLG;

        addItemInList(this);
    }


    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        if (choosenLG != null) {

            playerLG.setCanUsePowers(false);
            playerLG.sendMessage(LG.getPrefix() + "§2Vous avez §4infecté §2" + choosenLG.getNameWithAttributes(playerLG) + " !");
            GameLG.playPositiveSound((Player) player);

            choosenLG.joinCamp(Camps.LOUP_GAROU);
            choosenLG.getCache().put("infected", playerLG);
            choosenLG.sendMessage(LG.getPrefix() + "§cVous avez été infecté par " + playerLG.getRole().getDisplayName().substring(3) + ". Votre nouvel objectif est de gagner avec les Loups-Garou et d'éliminer les Villageois. Vous pourrez vous réveiller chaque nuit pour tuer un villageois, cependant, vous gardez vos pouvoirs.");
            choosenLG.getPlayer().playSound(choosenLG.getLocation(), Sound.WOLF_GROWL, 8f, 1.3f);

            ((InfectPereDesLoups)playerLG.getRole()).setLastTargetedByLG(null);

            LG.getInstance().getGame().getLGs().forEach(PlayerLG::updateGamePlayerScoreboard);
        }

        playerLG.getCache().put("unclosableInv", false);
        player.closeInventory();
        playerLG.setSleep();
        callback.run();
    }
}