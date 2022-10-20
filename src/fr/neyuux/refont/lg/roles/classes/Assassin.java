package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;

import java.util.List;

public class Assassin extends Role {
    @Override
    public String getDisplayName() {
        return "�1�lAssassin";
    }

    @Override
    public String getConfigName() {
        return "Assassin";
    }

    @Override
    public String getDeterminingName() {
        return "de l' " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les joueurs de la partie et de, par cons�quent, de gagner seul. Chaque nuit, vous pourrez assassiner quelqu'un. Vous ne pouvez pas �tre cibl� par les Loups-Garous";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 25;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �9" + this.getTimeout() + " secondes�f pour assassiner quelqu'un.";
    }

    


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        playerLG.setChoosing(choosen -> {
            if (choosen != null && choosen != playerLG) {
                LG.getInstance().getGame().getKilledPlayers().add(choosen);
                choosen.getCache().put("killedby", "assassin");

                playerLG.sendMessage(LG.getPrefix() + "�1Tu as assassin� " + choosen.getNameWithAttributes(playerLG) + "�1.");
                GameLG.playPositiveSound(playerLG.getPlayer());

                super.onPlayerTurnFinish(playerLG);
                callback.run();
            }
        });
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "�cTu as mis trop de temps � choisir !");
    }
}
