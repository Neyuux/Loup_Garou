package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Bouffon extends Role {

    private static final List<PlayerLG> needToPlay = new ArrayList<>();


    @Override
    public String getDisplayName() {
        return "§d§lBouffon";
    }

    @Override
    public String getConfigName() {
        return "Bouffon";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est de vous faire voter par le village. Si vous réussissez : vous aurez la possibilité de tuer un des joueurs qui a voté pour vous.";
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
        return 20;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d " + this.getTimeout() + " secondes§f pour tuer un joueur.";
    }


    public void onNightTurn(Runnable callback) {
        GameLG game = LG.getInstance().getGame();

        game.cancelWait();

        if (needToPlay.isEmpty()) {
            callback.run();
            return;
        }

        PlayerLG playerLG = needToPlay.remove(0);


        playerLG.getPlayer().setGameMode(GameMode.ADVENTURE);
        playerLG.setWake();

        game.wait(this.getTimeout(), () -> {
            this.onPlayerTurnFinish(playerLG);
            this.onNightTurn(callback);
         }, (currentPlayer, secondsLeft) -> (currentPlayer == playerLG) ? "§9§lA toi de jouer !" : "§9§lAu tour " + this.getDeterminingName());

        playerLG.sendMessage("" + this.getActionMessage());
        this.onPlayerNightTurn(playerLG, () -> this.onNightTurn(callback));
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        playerLG.getPlayer().setGameMode(GameMode.SPECTATOR);
        super.onPlayerTurnFinish(playerLG);
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        List<PlayerLG> choosable = new ArrayList<>();
        for (PlayerLG voterLG : LG.getInstance().getGame().getPlayersInGame())
            if (!voterLG.isDead()) //TODO hasvotedForBouffon
                choosable.add(voterLG);

        playerLG.setChoosing(choosen -> {
            if (choosen != null)
                if (choosable.contains(choosen)) {
                    LG.getInstance().getGame().getKilledPlayers().add(choosen);

                    playerLG.sendMessage(LG.getPrefix() + "§7Tu décides de hanter " + choosen.getNameWithAttributes(playerLG) + "§7.");
                    GameLG.playPositiveSound(playerLG.getPlayer());

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                } else {
                    playerLG.sendMessage(LG.getPrefix() + "§cCe joueur n'a pas voté pour vous !");
                    GameLG.playNegativeSound(playerLG.getPlayer());
                }
            });
    }

}
