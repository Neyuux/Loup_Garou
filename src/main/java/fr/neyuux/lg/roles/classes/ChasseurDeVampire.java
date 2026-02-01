package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.enums.GameType;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.inventories.roleinventories.ChoosePlayerInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

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
        super.onPlayerNightTurn(playerLG, callback);

        if (LG.getInstance().getGame().getGameType().equals(GameType.MEETING)) {
            playerLG.setChoosing(choosen -> {
                if (choosen != null && choosen != playerLG) {
                    checkVampire(choosen, playerLG);

                    super.onPlayerTurnFinish(playerLG);
                    callback.run();
                }
            });
        } else if (LG.getInstance().getGame().getGameType().equals(GameType.FREE)) {
           ChoosePlayerInv.getInventory(this.getDisplayName(), playerLG, LG.getInstance().getGame().getAliveExcept(playerLG), new ChoosePlayerInv.ActionsGenerator() {

                @Override
                public String[] generateLore(PlayerLG paramPlayerLG) {
                    return new String[] {"§aVoulez-vous §2vérifier " + paramPlayerLG.getNameWithAttributes(playerLG) + "§a ?", "§aLe cas échéant, il sera §céliminé§a de la partie.", "", "§7>>Clique pour choisir"};
                }

                @Override
                public void doActionsAfterClick(PlayerLG choosenLG) {
                    checkVampire(choosenLG, playerLG);

                    
                    LG.closeSmartInv(playerLG.getPlayer());
                    playerLG.setSleep();
                    callback.run();
                }
            }).open(playerLG.getPlayer());
            
        }
    }

    private void checkVampire(PlayerLG choosen, PlayerLG playerLG) {
        if (choosen == null) return;

        if (choosen.getCamp().equals(Camps.VAMPIRE)) {
            RoleChoiceEvent roleChoiceEvent = new RoleChoiceEvent(this, choosen);

            Bukkit.getPluginManager().callEvent(roleChoiceEvent);
            if (roleChoiceEvent.isCancelled()) return;

            LG.getInstance().getGame().kill(choosen);

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
    

    @EventHandler
    public void onVampireDeath(RoleChoiceEvent ev) {
        Role role = ev.getRole();

        if (role instanceof Vampire && ev.getChoosen().getRole() instanceof ChasseurDeVampire) {
            GameLG game = LG.getInstance().getGame();
            PlayerLG lastVampire = Vampire.getLastVampire();

            for (PlayerLG playerLG : game.getPlayersByRole(Vampire.class)) {
                playerLG.sendMessage(LG.getPrefix() + "§cVous avez tenté d'éliminer un " + this.getDisplayName() + "§c. Étant expérimenté, il survit et élimine le plus jeune d'entre-vous, §5§l" + lastVampire.getName() + "§c.");
                playerLG.sendTitle("§cMauvaise pioche !", "§cVous avez attaqué un " + this.getDisplayName()+ " §c!", 10, 90, 10);
            }

            game.kill(lastVampire);
            ev.setCancelled(true);
        }
    }
}
