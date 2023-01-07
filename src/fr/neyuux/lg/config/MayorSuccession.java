package fr.neyuux.lg.config;

public enum MayorSuccession {
    CHOOSE(1, "�bSuccession", "�7�o(Le maire d�c�d� choisit son successseur.)"),
    RANDOM(2, "�7Al�atoire", "�7�o(Le prochain maire est al�atoire)"),
    REVOTE(3, "�eRevote", "�7�o(Le prochain maire est � nouveau vot�)"),
    NONE(4, "�cAucun", "�7�o(Le maire n'aura aucun h�ritier)");

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
