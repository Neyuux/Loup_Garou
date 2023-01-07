package fr.neyuux.lg.roles.classes;

import fr.neyuux.lg.LG;
import fr.neyuux.lg.PlayerLG;
import fr.neyuux.lg.event.RoleChoiceEvent;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.roles.Role;
import org.bukkit.event.EventHandler;

public class HumainMaudit extends Role {

    @Override
    public String getDisplayName() {
        return "§e§lHumain §c§lMaudit";
    }

    @Override
    public String getConfigName() {
        return "Humain Maudit";
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
        return "§fVous êtes "+this.getDisplayName()+"§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Si vous vous faites cibler par les Loups, vous ne mourrez pas et devenez l'un d'entre-eux.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VILLAGE;
    }

    @Override
    public Decks getDeck() {
        return Decks.WEREWOLF_ONLINE;
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
    public void onLGTurn(RoleChoiceEvent ev) {
        PlayerLG choosenLG = ev.getChoosen();
        if (ev.getRole() instanceof LoupGarou && choosenLG.getRole() instanceof HumainMaudit && choosenLG.getCamp() != Camps.LOUP_GAROU) {
            ev.setCancelled(true);
            choosenLG.setCamp(Camps.LOUP_GAROU);
            choosenLG.sendMessage(LG.getPrefix() + "§cVous avez été attaqué par les Loups cette nuit. Vous devenez donc l'un d'entre eux.");
            LG.getInstance().getGame().sendMessage(LoupGarou.class, LG.getPrefix() + "§c§l" + choosenLG.getName() + " §cest en fait humain maudit. Il se transforme donc un Loup-Garou suite à votre attaque.");
        }
    }
}
