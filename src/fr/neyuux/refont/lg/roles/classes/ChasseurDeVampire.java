package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

import java.util.List;

public class ChasseurDeVampire extends Role {

    @Override
    public String getDisplayName() {
        return "�a�lChasseur de �5�lVampire";
    }

    @Override
    public String getConfigName() {
        return "Chasseur de Vampire";
    }

    @Override
    public String getDeterminingName() {
        return "du " + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez v�rifier si un joueur est vampire. S'il l'est, vous le purifirez.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "�fVous avez �a " + this.getTimeout() + " secondes�f pour examiner un joueur.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        playerLG.setChoosing(choosen -> {
            if (choosen != null && choosen != playerLG) {
                LG.getInstance().getGame().getKilledPlayers().add(choosen);

                playerLG.sendMessage(LG.getPrefix() + "�aTu �limines " + choosen.getNameWithAttributes(playerLG) + "�a.");
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
