package fr.neyuux.lg.roles;

import fr.neyuux.lg.roles.classes.*;

public enum RoleNightOrder {
    VOLEUR(Voleur.class, RecurrenceType.FIRST_NIGHT),
    CUPIDON(Cupidon.class, RecurrenceType.FIRST_NIGHT),
    ENFANTSAUVAGE(EnfantSauvage.class, RecurrenceType.FIRST_NIGHT),
    CHIENLOUP(ChienLoup.class, RecurrenceType.FIRST_NIGHT),
    JUMEAU(Jumeau.class, RecurrenceType.FIRST_NIGHT),
    NOCTAMBULE(Noctambule.class, RecurrenceType.EACH_NIGHT),
    COMEDIEN(Comedien.class, RecurrenceType.EACH_NIGHT),
    VOYANTE(Voyante.class, RecurrenceType.EACH_NIGHT),
    VOYANTEDAURA(VoyanteDAura.class, RecurrenceType.EACH_NIGHT),
    DETECTIVE(Detective.class, RecurrenceType.EACH_NIGHT),
    RENARD(Renard.class, RecurrenceType.EACH_NIGHT),
    PRONOSTIQUEUR(Pronostiqueur.class, RecurrenceType.EACH_NIGHT),
    PACIFISTE(Pacifiste.class, RecurrenceType.EACH_NIGHT),
    FILLEDEJOIE(FilleDeJoie.class, RecurrenceType.EACH_NIGHT),
    GARDEDUCORPS(GardeDuCorps.class, RecurrenceType.EACH_NIGHT),
    SALVATEUR(Salvateur.class, RecurrenceType.EACH_NIGHT),
    ASSASSIN(Assassin.class, RecurrenceType.EACH_NIGHT),
    LOUPGAROU(LoupGarou.class, RecurrenceType.EACH_NIGHT),
    INFECTPEREDESLOUPS(InfectPereDesLoups.class, RecurrenceType.EACH_NIGHT),
    GRANDMECHANTLOUP(GrandMechantLoup.class, RecurrenceType.EACH_NIGHT),
    LOUPGAROUBLANC(LoupGarouBlanc.class, RecurrenceType.ONE_OUT_OF_TWO),
    PETITEFILLEWO(PetiteFilleWO.class, RecurrenceType.EACH_NIGHT),
    CHAMAN(Chaman.class, RecurrenceType.EACH_NIGHT),
    SORCIERE(Sorciere.class, RecurrenceType.EACH_NIGHT),
    CHASSEURDEVAMPIRE(ChasseurDeVampire.class, RecurrenceType.EACH_NIGHT),
    VAMPIRE(Vampire.class, RecurrenceType.EACH_NIGHT),
    BOUFFON(Bouffon.class, RecurrenceType.EACH_NIGHT),
    PRETRE(Pretre.class, RecurrenceType.EACH_NIGHT),
    NECROMANCIEN(Necromancien.class, RecurrenceType.EACH_NIGHT),
    VILAINGARCON(VilainGarcon.class, RecurrenceType.EACH_NIGHT),
    DICTATEUR(Dictateur.class, RecurrenceType.EACH_NIGHT),
    MAMIEGRINCHEUSE(MamieGrincheuse.class, RecurrenceType.EACH_NIGHT),
    CORBEAU(Corbeau.class, RecurrenceType.EACH_NIGHT),
    PIRATE(Pirate.class, RecurrenceType.EACH_NIGHT),
    JOUEURDEFLUTE(JoueurDeFlute.class, RecurrenceType.EACH_NIGHT),
    PYROMANE(Pyromane.class, RecurrenceType.EACH_NIGHT),
    SOEUR(Soeur.class, RecurrenceType.EACH_NIGHT),
    FRERE(Frere.class, RecurrenceType.EACH_NIGHT);

    RoleNightOrder(Class<? extends Role> roleClass, RecurrenceType recurrenceType) {
        this.roleClass = roleClass;
        this.recurrenceType = recurrenceType;
    }

    private final Class<? extends Role> roleClass;

    private final RecurrenceType recurrenceType;

    public Class<? extends Role> getRoleClass() {
        return roleClass;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public static boolean contains(Class<? extends Role> roleClass) {
        for (RoleNightOrder rno : values())
            if (roleClass.equals(rno.getRoleClass()))
                return true;
        return false;
    }

    public enum RecurrenceType {
        FIRST_NIGHT,
        ONE_OUT_OF_TWO,
        EACH_NIGHT
    }
}
