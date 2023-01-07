package fr.neyuux.lg.items.menus.roleinventories;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.inventories.roleinventories.PyromaneInv;
import fr.neyuux.lg.items.menus.ReturnArrowItemStack;
import fr.neyuux.lg.roles.classes.Pyromane;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.stream.Collectors;

public class PyromanePutOilItemStack extends CustomItemStack {

    private final Runnable callback;
    private final Pyromane pyromane;
    private final PyromaneInv lastInv;

    public PyromanePutOilItemStack(Pyromane pyromane, Runnable callback, PyromaneInv lastInv) {
        super(Material.LAVA_BUCKET, 1, "§6§lMettre de l'huile sur 2 joueurs");
        this.pyromane = pyromane;
        this.lastInv = lastInv;

        this.setLore("§eMettre de huile sur un joueur", "§eL'ajoutera à la liste des joueurs que.", "§evous pourrez brûler au prochain tour.", "", "§7>>Clique pour sélectionner");

        this.callback = callback;

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        PlayerLG playerLG = PlayerLG.createPlayerLG(player);
        GameLG game = LG.getInstance().getGame();
        final int[] oilplayers = {0};

        pyromane.getOiledPlayers().forEach(oiledLG->((Player)player).hidePlayer(oiledLG.getPlayer()));

        if (game.getGameType().equals(GameType.MEETING)) {
            playerLG.sendMessage(LG.getPrefix() + pyromane.getActionMessage());

            playerLG.getCache().put("unclosableInv", false);

            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    if (oil(choosen, playerLG, oilplayers[0])) {
                        pyromane.turnFinished(playerLG);
                        callback.run();

                    } else oilplayers[0]++;
                }
            });

        } else if (game.getGameType().equals(GameType.FREE)) {
            ChoosePlayerInv inv = new ChoosePlayerInv(this.getDisplayName(), playerLG, game.getAliveExcept(playerLG).stream().filter(oiledPlayerLG-> oiledPlayerLG.getCache().has("pyromaneOiled")).collect(Collectors.toList()), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§6Voulez-vous mettre de l'huile sur " + paramPlayerLG.getNameWithAttributes(playerLG) + "§6 ?", "§9Il sera ajouté à la liste des huilés.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    if(oil(choosenLG, playerLG, oilplayers[0])) {
                        playerLG.getCache().put("unclosableInv", false);
                        playerLG.getPlayer().closeInventory();
                        playerLG.setSleep();
                        callback.run();

                    } else oilplayers[0]++;
                }
            });
            inv.setItem(inv.getSize() - 1, new ReturnArrowItemStack(lastInv));
            inv.open(player);
            playerLG.getCache().put("unclosableInv", true);
        }
    }

    private boolean oil(PlayerLG choosen, PlayerLG playerLG, int count) {
        if (choosen == null) return true;
        RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(pyromane, choosen);

        Bukkit.getPluginManager().callEvent(roleChoiceEvent);
        if (roleChoiceEvent.isCancelled()) return true;

        choosen.getCache().put("pyromaneOiled", playerLG);

        playerLG.sendMessage(LG.getPrefix() + "§6Tu as huilé " + choosen.getNameWithAttributes(playerLG) + "§6.");
        GameLG.playPositiveSound(playerLG.getPlayer());
        return count != 1;
    }
}