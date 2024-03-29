package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.event.NightEndEvent;
import fr.neyuux.lg.event.PlayerEliminationEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.List;

public class ChevalierALEpeeRouillee extends Role {

    @Override
    public String getDisplayName() {
        return "�7�lChevalier � l'�p�e �6�lRouill�e";
    }

    @Override
    public String getConfigName() {
        return "Chevalier a l'�p�e Rouill�e";
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
        return "�fVous �tes "+this.getDisplayName()+"�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Si vous mourrez par les loups, le premier Loup dont le pseudo vous suit dans l'ordre alphab�tique�f tombera gravement malade : �9il mourra�f au tour suivant.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.THIERCELIEUX;
    }

    @Override
    public int getTimeout() {
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }


    @EventHandler
    public void onEliminationLG(PlayerEliminationEvent ev) {
        PlayerLG playerLG = ev.getEliminated();

        if (playerLG.getRole().equals(this) && LoupGarou.getLastTargetedByLG().equals(playerLG)) {
            List<String> list = LG.getInstance().getGame().getAliveAlphabecticlySorted();
            while (!list.get(0).equals(playerLG.getName())) {
                String s = list.remove(0);
                list.add(s);
            }

            for (String s : list) {
                PlayerLG sickLG = PlayerLG.createPlayerLG(Bukkit.getPlayer(s));

                if (!s.equals(playerLG.getName()) && sickLG.isLG()) {
                    sickLG.getCache().put("chevalierALEppeRouilleSick", playerLG);

                    sickLG.sendTitle("�cVous �tes empoisonn� !", "�6Vous avez �t� transperc� par l'�p�e du Chevalier � l'Ep�e Rouill�e.", 20, 60, 20);
                    ev.getMessagesToSend().put(sickLG, LG.getPrefix() + "�6En tuant le Chevalier � l'Ep�e Rouill�e, vous vous �tes bl�ss� sur son arme. Vous mourrez prochain tour.");
                    ev.getMessagesToSend().put(playerLG, LG.getPrefix() + "�6Vous avez empoisonn� �c�l" + sickLG.getName() + " �6en vous d�battant avant de mourir.");

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onNightEndEvent(NightEndEvent ev) {
        GameLG game = LG.getInstance().getGame();
        for (PlayerLG playerLG : game.getAlive())
            if (playerLG.getCache().has("chevalierALEppeRouilleSick"))
                game.kill(playerLG);
    }
    
}
