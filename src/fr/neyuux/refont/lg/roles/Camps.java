package fr.neyuux.refont.lg.roles;

public enum Camps {

    VILLAGE("Village"),
    LOUP_GAROU("LG"),
    VAMPIRE("Vampire"),
    AUTRE("Autres")
    ;



    private final String name;

    Camps(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
