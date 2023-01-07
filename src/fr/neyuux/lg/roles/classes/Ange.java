package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.WinCamps;
import fr.neyuux.lg.event.DayEndEvent;
import fr.neyuux.lg.event.NightStartEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

import java.util.Collections;

public class Ange extends Role {

    @Override
    public String getDisplayName() {
        return "§b§lAnge";
    }

    @Override
    public String getConfigName() {
        return "Ange";
    }

    @Override
    public String getDeterminingName() {
        return "de l'" + this.getDisplayName();
    }

    @Override
    public int getMaxNumber() {
        return -1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est de vous faire voter par le village au premier tour. Si vous réussissez : la victoire sera votre, mais dans le cas contraire : vous deviendrez §e§lSimple §a§lVillageois§f.";
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
        return -1;
    }

    @Override
    public String getActionMessage() {
        return "";
    }

    
    @EventHandler
    public void onDayEndElimination(DayEndEvent ev) {
        GameLG game = LG.getInstance().getGame();
        PlayerLG killedLG = ev.getKilledLG();

        if (ev.getKilledLG().getRole() instanceof Ange && game.getDay() == 1)
            game.win(WinCamps.CUSTOM, Collections.singletonList(killedLG));
    }

    @EventHandler
    public void onNightStart(NightStartEvent ev) {
        GameLG game = LG.getInstance().getGame();
        if (game.getNight() == 2) {
            for (PlayerLG playerLG : game.getPlayersByRole(this.getClass())) {
                SimpleVillageois sv = new SimpleVillageois();
                playerLG.joinRole(sv);
                if (playerLG.getCamp().equals(this.getBaseCamp())) playerLG.setCamp(sv.getBaseCamp());
                playerLG.getCache().put("angeAtStart", this);
            }
        }
    }
}
