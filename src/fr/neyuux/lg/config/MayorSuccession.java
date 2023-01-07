package fr.neyuux.lg.config;

public enum MayorSuccession {
    CHOOSE(1, "§bSuccession", "§7§o(Le maire décédé choisit son successseur.)"),
    RANDOM(2, "§7Aléatoire", "§7§o(Le prochain maire est aléatoire)"),
    REVOTE(3, "§eRevote", "§7§o(Le prochain maire est à nouveau voté)"),
    NONE(4, "§cAucun", "§7§o(Le maire n'aura aucun héritier)");

    MayorSuccession(int id, String name, String description) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    private final int id;

    private final String name;

    private final String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }


    public static MayorSuccession getById(int id) {
        for (MayorSuccession mayorSuccession : MayorSuccession.values())
            if (mayorSuccession.getId() == id)
                return mayorSuccession;
        return null;
    }

    public static MayorSuccession getNext(int id) {
        if (id == MayorSuccession.values().length) return getById(1);
        else return getById(id + 1);
    }
}
