package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.old.lg.role.RDeck;

public enum Decks {

    THIERCELIEUX("Philippe des Palières & Hervé Marly", "Thiercelieux", "Thiercelieux"),
    WOLFY("wolfy.fr", "Wolfy", "Wolfy"),
    ONLINE("loups-garous-en-ligne.com", "LGEL", "Loup-Garou en Ligne"),
    WEREWOLF_ONLINE("Philipp Eichhorn", "Werewolf", "Werewolf Online"),
    LEOMELKI("Leomelki & Shytoos", "Leomelki", "Leomelki")
    ;

    Decks(String credit, String alias, String name) {
        this.credit = credit;
        this.alias = alias;
        this.name = name;
    }

    private final String credit;
    private final String alias;
    private final String name;

    public String getCreator() {
        return this.credit;
    }

    public String getAlias() {
        return this.alias;
    }
    public static Decks getByAlias(String alias) {
        Decks d = null;

        for (Decks decks : Decks.values()) {
            if (decks.getAlias().equals(alias)) d = decks;
        }

        return d;
    }

    public String getName() {
        return name;
    }
}
