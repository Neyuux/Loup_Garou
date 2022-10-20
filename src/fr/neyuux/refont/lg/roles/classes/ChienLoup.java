package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class ChienLoup extends Role {

    @Override
    public String getDisplayName() {
        return "§a§lChien§e-§c§lLoup";
    }

    @Override
    public String getConfigName() {
        return "Chien-Loup";
    }

    @Override
    public String getDeterminingName() {
        return null;
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, au début de la partie, vous allez devoir choisir entre devenir §c§lLoup-Garou §fou §e§lSimple §a§lVillageois§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §e" + getTimeout() + " secondes§f pour choisir votre camp.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {


        playerLG.setChoosing(choosen -> {
            if (choosen != null && choosen != playerLG) {
                LG.getInstance().getGame().getKilledPlayers().add(choosen);
                choosen.getCache().put("killedby", "assassin");

                playerLG.sendMessage(LG.getPrefix() + "§1Tu as assassiné " + choosen.getNameWithAttributes(playerLG) + "§1.");
                GameLG.playPositiveSound(playerLG.getPlayer());

                super.onPlayerTurnFinish(playerLG);
                callback.run();
            }
        });
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
}
