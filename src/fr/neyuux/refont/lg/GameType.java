package fr.neyuux.refont.lg;

public enum GameType {

    FREE("§eLibre"),
    MEETING("§dRéunion"),
    NONE("§cPas encore choisi");

    GameType(String name) {
        this.name = name;
    }

    private final String name;


    public String getName() {
        return name;
    }
}
