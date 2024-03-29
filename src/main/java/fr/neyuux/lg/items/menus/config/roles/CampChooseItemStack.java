package fr.neyuux.lg.items.menus.config.roles;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.LG;
import fr.neyuux.lg.inventories.config.roles.RolesListRolesInv;
import fr.neyuux.lg.roles.Camps;
import fr.neyuux.lg.roles.Decks;
import fr.neyuux.lg.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CampChooseItemStack extends CustomItemStack {

    private final Decks deck;
    private final Camps camp;

    public CampChooseItemStack(Decks deck, Camps camp) {
        super(Material.WOOL, 1);

        this.deck = deck;
        this.camp = camp;

        if (camp.equals(Camps.LOUP_GAROU)) {
            this.setDamage(14);
            this.setDisplayName("�cCamp des �lLoups-Garous �b" + deck.getAlias());
            this.setLore("�4Affiche les r�les", "�4du camp des loups.", "�7>>Cliquer pour afficher.");
        } else if (camp.equals(Camps.VILLAGE)){
            this.setDamage(5);
            this.setDisplayName("�aCamp du �lVillage �b" + deck.getAlias());
            this.setLore("�2Affiche les r�les", "�2du camp du village.", "�7>>Cliquer pour afficher.");
        } else {
            this.setDamage(1);
            this.setDisplayName("�6Camp des �lAutres �b" + deck.getAlias());
            this.setLore("�eAffiche les r�les qui ne", "�esont ni du village ni des loups.", "�7>>Cliquer pour afficher.");
        }

        addItemInList(this);
    }

    @Override
    public void use(HumanEntity player, Event event) {
        RolesListRolesInv newInv = new RolesListRolesInv(deck, camp);
        newInv.registerItems();

        if (newInv.getItemsMap().size() == 1) {
            GameLG.playNegativeSound((Player) player);
            player.sendMessage(LG.getPrefix() + "�cLe deck �b" + deck.getName() + " �cne contient pas de r�le du camp " + camp.getColor() + "�l" + camp.getName() + " �c!");
        } else {
            newInv.getItemsMap().clear();
            newInv.open(player);
        }
    }

}
