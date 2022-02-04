package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.roles.Role;

import java.util.ArrayList;
import java.util.List;

public class GameConfig {

    private final GameLG gameLG;

    private final List<Role> addedRoles = new ArrayList<>();

    public GameConfig(GameLG game) {
        this.gameLG = game;
    }


}
