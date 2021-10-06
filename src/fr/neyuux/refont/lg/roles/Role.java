package fr.neyuux.refont.lg.roles;

import org.bukkit.entity.Player;

public abstract class Role {

    private final String displayName, scoreboardName, configName;
    private final String description;
    private final RoleEnum role;
    private final Camps baseCamp;
    private final Decks deck;

    protected Role(String displayName, String scoreboardName, String configName, String description, RoleEnum role, Camps baseCamp, Decks deck) {
        this.displayName = displayName;
        this.scoreboardName = scoreboardName;
        this.configName = configName;
        this.description = description;
        this.role = role;
        this.baseCamp = baseCamp;
        this.deck = deck;
    }

    public abstract void onDistribution(Player player);


    public String getDisplayName() {
        return displayName;
    }

    public String getScoreboardName() {
        return scoreboardName;
    }

    public String getConfigName() {
        return configName;
    }

    public String getDescription() {
        return description;
    }

    public RoleEnum getRole() {
        return role;
    }

    public Camps getBaseCamp() {
        return baseCamp;
    }

    public Decks getDeck() {
        return deck;
    }
}
