package fr.neyuux.refont.lg.roles;

public enum Camps {

    VILLAGE("Village", "§a"),
    LOUP_GAROU("LG", "§c"),
    VAMPIRE("Vampire", "§5"),
    AUTRE("Autres", "§6")
    ;



    private final String name;
    private final String color;

    Camps(String name, String color) {
        this.name = name;
        this.color = color;
    }


    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
}
