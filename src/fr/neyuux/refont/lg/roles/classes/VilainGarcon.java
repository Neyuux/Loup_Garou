package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

public class VilainGarcon extends Role {

    public VilainGarcon(GameLG gameLG) {
        super(gameLG);
    }

    @Override
    public String getDisplayName() {
        return "§c§lVilain §b§lGarçon";
    }

    @Override
    public String getConfigName() {
        return "Vilain Garcon";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Camps getBaseCamp() {
        return null;
    }

    @Override
    public Decks getDeck() {
        return null;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public String getActionMessage() {
        return null;
    }
}
