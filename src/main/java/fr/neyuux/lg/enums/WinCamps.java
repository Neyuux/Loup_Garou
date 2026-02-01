package fr.neyuux.lg.enums;

import fr.neyuux.lg.GameLG;
import lombok.Getter;

@Getter
public enum WinCamps {

    VILLAGE("§edu §lVillage", winners -> "§6§l§nNombre de survivants§f : §e" + winners.size()),
    LOUP_GAROU("§cdes §lLoups-Garous", winners -> "§6§l§nNombre de survivants§f : §e" + winners.size()),
    COUPLE("§ddu §lCouple", winners -> {
        StringBuilder builder = new StringBuilder("§6§l§nBravo à§f : ");
        winners.forEach(playerLG -> builder.append("§e§l").append(playerLG.getName()).append("§f, "));
        return builder.substring(0, builder.length() - 2);
    }),
    CUSTOM("", winners -> "§6§l§nBravo à§f : §e§l" + winners.get(0).getName()),
    NONE("§4de la §lMort", winners -> "§cVa falloir recruter pour reconstruire...");

    private final String determingName;
    private final GameLG.StringVictoryTitle victoryTitle;

    WinCamps(String determingName, GameLG.StringVictoryTitle victoryTitle) {
        this.determingName = determingName;
        this.victoryTitle = victoryTitle;
    }

}
