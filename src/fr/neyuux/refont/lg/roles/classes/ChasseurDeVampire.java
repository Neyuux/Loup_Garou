package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.GameType;
import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.PlayerLG;
import fr.neyuux.refont.lg.inventories.roleinventories.RoleChoosePlayerInv;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

import java.util.List;

public class ChasseurDeVampire extends Role {

    @Override
    public String getDisplayName() {
        return "§a§lChasseur de §5§lVampire";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez vérifier si un joueur est vampire. S'il l'est, vous le purifirez.";
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
        return "§fVous avez §a " + this.getTimeout() + " secondes§f pour examiner un joueur.";
    }


    @Override
    protected void onPlayerNightTurn(PlayerLG playerLG, Runnable callback) {
        if (LG.getInstance().getGame().getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    checkVampire(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (LG.getInstance().getGame().getGameType().equals(GameType.FREE)) {
            new RoleChoosePlayerInv(this.getDisplayName(), playerLG, new RoleChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§aVoulez-vous §2vérifier " + paramPlayerLG.getNameWithAttributes(playerLG) + "§a ?", "§aLe cas échéant, il sera §céliminé§a de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    checkVampire(choosenLG, playerLG);

                    playerLG.getPlayer().closeInventory();
                    playerLG.setSleep();
                    callback.run();
                }
            });
        }
    }

    private void checkVampire(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen.getCamp().equals(Camps.VAMPIRE)) {
            LG.getInstance().getGame().getKilledPlayers().add(choosen);

            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "§a est un §5§lVampire§a, tu décides donc de §cl'éliminer§a.");
            GameLG.playPositiveSound(playerLG.getPlayer());
        } else {
            playerLG.sendMessage(LG.getPrefix() + choosen.getNameWithAttributes(playerLG) + "§c n'est pas un §5§lVampire§c.");
            GameLG.playNegativeSound(playerLG.getPlayer());
        }
    }

    @Override
    protected void onPlayerTurnFinish(PlayerLG playerLG) {
        super.onPlayerTurnFinish(playerLG);
        playerLG.sendMessage(LG.getPrefix() + "§cTu as mis trop de temps à choisir !");
    }
}
