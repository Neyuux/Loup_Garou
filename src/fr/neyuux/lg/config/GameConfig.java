package fr.neyuux.lg.config;

import fr.neyuux.lg.GameLG;
import fr.neyuux.lg.items.menus.config.parameters.*;
import fr.neyuux.lg.roles.Role;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameConfig {

    private final GameLG gameLG;

    private final List<Constructor<? extends Role>> addedRoles = new ArrayList<>();

    private final Parameter dayCycle = new Parameter(true, ParameterType.GLOBAL);

    private final Parameter chatLG = new Parameter(true, ParameterType.GLOBAL);

    private final Parameter mayor = new Parameter(true, ParameterType.GLOBAL);

    private final Parameter randomCouple = new Parameter(false, ParameterType.GLOBAL);

    private final Parameter mayorSuccession = new Parameter(MayorSuccession.CHOOSE, ParameterType.GLOBAL);

    private final Parameter cupiWinWithCouple = new Parameter(false, ParameterType.ROLE);

    private final Parameter comedianPowers = new Parameter(new ArrayList<>(Arrays.asList(ComedianPowers.VOYANTE, ComedianPowers.MONTREUR_D_OURS, ComedianPowers.ANCIEN)), ParameterType.ROLE);

    private final Parameter wildChildRandomModel = new Parameter(false, ParameterType.ROLE);

    private final Parameter cupiInCouple = new Parameter(false, ParameterType.ROLE);

    private final Parameter chamanChat = new Parameter(false, ParameterType.ROLE);

    private final Parameter chattyVoyante = new Parameter(false, ParameterType.ROLE);

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

    public void registerParameters() {
        this.getDayCycle().setItem(new ParameterDayCycleItemStack());

        this.getChatLG().setItem(new ParameterLGChatItemStack());

        this.getMayor().setItem(new ParameterMayorItemStack());

        this.getRandomCouple().setItem(new ParameterRandomCoupleItemStack());

        this.getMayorSuccession().setItem(new ParameterMayorSuccessionItemStack());

        this.getCupiWinWithCouple().setItem(new ParameterCupiWinWithCoupleItemStack());

        this.getComedianPowers().setItem(new ParameterComedianPowersItemStack());

        this.getWildChildRandomModel().setItem(new ParameterWildChildRandomModelItemStack());

        this.getCupiInCouple().setItem(new ParameterCupiInCoupleItemStack());

        this.getChamanChat().setItem(new ParameterChamanChatItemStack());

        this.getChattyVoyante().setItem(new ParameterChattyVoyanteItemStack());
    }
}

