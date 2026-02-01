package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.inventories.roleinventories.InfectPereDesLoupsInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import lombok.Setter;
import org.bukkit.Bukkit;

public class InfectPereDesLoups extends Role {

    @Setter
    private boolean infect;

    @Override
    public String getDisplayName() {
        return "§2§lInfect Père §c§ldes Loups";
    }

    @Override
    public String getConfigName() {
        return "Infect Pere des Loups";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Après cela, vous vous réveillez et §9choisissez si votre victime deviendra §c§lLoup-Garou§f (elle gardera également ses pouvoirs de villageois si elle en a un).";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.LOUP_GAROU;
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
        return "§fVous avez §c" + this.getTimeout() + " secondes §fpour choisir d'infecter ou non le joueur ciblé cette nuit.";
    }


    @Override
    public void onNightTurn(Runnable callback) {
        if (this.hasInfected()) {
            LG.getInstance().getGame().wait(this.getTimeout(), () -> {

            }, (currentPlayer, secondsLeft) -> LG.getPrefix() + "Au tour " + this.getDeterminingName(), true);
            Bukkit.getScheduler().runTaskLater(LG.getInstance(), () -> {
                LG.getInstance().getGame().cancelWait();
                callback.run();
            }, (LG.RANDOM.nextInt(7) + 5) * 20L);
        } else {
            super.onNightTurn(callback);
        }
    }

    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        super.onPlayerNightTurn(playerLG, callback);
        InfectPereDesLoupsInv.getInventory(this, playerLG, callback).open(playerLG.getPlayer());
        
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }

    public boolean hasInfected() {
        return this.infect;
    }

}
