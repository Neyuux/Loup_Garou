package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.event.DayStartEvent;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;
import java.util.Random;

public class Mercenaire extends Role {

    @Override
    public String getDisplayName() {
        return "§c§lMerce§5§lnaire";
    }

    @Override
    public String getConfigName() {
        return "Mercenaire";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, le premier jour, votre objectif est d'éliminer la cible qui vous est attribuée. Si vous y parvenez, vous gagnez seul la partie instantanément. Sinon, vous devenez §e§lSimple §a§lVillageois§f.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.AUTRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WOLFY;
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
    public void onDayStart(DayStartEvent ev) {
        GameLG game = LG.getInstance().getGame();
        List<PlayerLG> choosable = game.getAlive();

        for (PlayerLG playerLG : game.getPlayersByRole(this.getClass())) {
            PlayerLG targetLG = choosable.get(new Random().nextInt(choosable.size()));

            choosable.remove(targetLG);
            playerLG.sendTitle("§c§l" + targetLG.getNameWithAttributes(playerLG), "§c§lest votre cible pour ce vote !", 10, 50, 10);
            playerLG.sendMessage(LG.getPrefix() + "§5§l" + targetLG.getNameWithAttributes(playerLG) + "§c est votre §5§lCible §cpour ce tour ! Convainquez les autres de le voter pour gagner la partie.");
            targetLG.getCache().put("mercenaireTarget", playerLG);
        }
    }

    //TODO onTargetDeath Victory
}
