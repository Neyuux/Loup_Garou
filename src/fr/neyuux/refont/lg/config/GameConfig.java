package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.GameLG;
import fr.neyuux.refont.lg.items.menus.config.parameters.*;
import fr.neyuux.refont.lg.roles.Role;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameConfig {

    private final GameLG gameLG;

    private final List<Constructor<? extends Role>> addedRoles = new ArrayList<>();

    private final Parameter dayCycle = new Parameter(new ParameterDayCycleItemStack(), true, ParameterType.GLOBAL);

    private final Parameter chatLG = new Parameter(new ParameterLGChatItemStack(), true, ParameterType.GLOBAL);

    private final Parameter mayor = new Parameter(new ParameterMayorItemStack(), true, ParameterType.GLOBAL);

    private final Parameter randomCouple = new Parameter(new ParameterRandomCoupleItemStack(), false, ParameterType.GLOBAL);

    private final Parameter mayorSuccession = new Parameter(new ParameterMayorSuccessionItemStack(), MayorSuccession.CHOOSE, ParameterType.GLOBAL);

    private final Parameter cupiWinWithCouple = new Parameter(new ParameterCupiWinWithCoupleItemStack(), false, ParameterType.ROLE);

    private final Parameter comedianPowers = new Parameter(new ParameterComedianPowersItemStack(), Arrays.asList(ComedianPowers.VOYANTE, ComedianPowers.MONTREUR_D_OURS, ComedianPowers.ANCIEN), ParameterType.ROLE);

    private final Parameter wildChildRandomModel = new Parameter(new ParameterWildChildRandomModelItemStack(), false, ParameterType.ROLE);

    private final Parameter cupiInCouple = new Parameter(new ParameterCupiInCoupleItemStack(), false, ParameterType.ROLE);

    private final Parameter chamanChat = new Parameter(new ParameterChamanChatItemStack(), false, ParameterType.ROLE);

    private final Parameter chattyVoyante = new Parameter(new ParameterChattyVoyanteItemStack(), false, ParameterType.ROLE);

    public GameConfig(GameLG game) {
        this.gameLG = game;
    }



    public GameLG getGameLG() {
        return gameLG;
    }

    public Parameter getDayCycle() {
        return dayCycle;
    }

    public Parameter getChatLG() {
        return chatLG;
    }

    public Parameter getMayor() {
        return mayor;
    }

    public Parameter getRandomCouple() {
        return randomCouple;
    }

    public Parameter getMayorSuccession() {
        return mayorSuccession;
    }

    public Parameter getCupiWinWithCouple() {
        return cupiWinWithCouple;
    }

    public Parameter getComedianPowers() {
        return comedianPowers;
    }

    public Parameter getWildChildRandomModel() {
        return wildChildRandomModel;
    }

    public Parameter getCupiInCouple() {
        return cupiInCouple;
    }

    public Parameter getChamanChat() {
        return chamanChat;
    }

    public Parameter getChattyVoyante() {
        return chattyVoyante;
    }

    public List<Constructor<? extends Role>> getAddedRoles() {
        return addedRoles;
    }

    public int getNumberOfRole(Constructor<? extends Role> constructor) {
        int i = 0;
        for (Constructor<? extends Role> constructor2 : this.getAddedRoles())
            if (constructor.equals(constructor2))i++;
        return i;
    }
}

