package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.lg.roles.classes.*;

public enum RoleNightOrder {
    VOLEUR(Voleur.class),
    CUPIDON(Cupidon.class),
    ENFANTSAUVAGE(EnfantSauvage.class),
    CHIENLOUP(ChienLoup.class),
    JUMEAU(Jumeau.class),
    NOCTAMBULE(Noctambule.class),
    COMEDIEN(Comedien.class),
    VOYANTE(Voyante.class),
    VOYANTEDAURA(VoyanteDAura.class),
    DETECTIVE(Detective.class),
    RENARD(Renard.class),
    PRONOSTIQUEUR(Pronostiqueur.class),
    PACIFISTE(Pacifiste.class),
    FILLEDEJOIE(FilleDeJoie.class),
    GARDEDUCORPS(GardeDuCorps.class),
    SALVATEUR(Salvateur.class),
    ASSASSIN(Assassin.class),
    LOUPGAROU(LoupGarou.class),
    INFECTPEREDESLOUPS(InfectPereDesLoups.class),
    GRANDMECHANTLOUP(GrandMechantLoup.class),
    LOUPGAROUBLANC(LoupGarouBlanc.class),
    PETITEFILLEWO(PetiteFilleWO.class),
    SORCIERE(Sorciere.class),
    CHASSEURDEVAMPIRE(ChasseurDeVampire.class),
    VAMPIRE(Vampire.class),
    BOUFFON(Bouffon.class),
    PRETRE(Pretre.class),
    NECROMANCIEN(Necromancien.class),
    VILAINGARCON(VilainGarcon.class),
    DICTATEUR(Dictateur.class),
    MAMIEGRINCHEUSE(MamieGrincheuse.class),
    CORBEAU(Corbeau.class),
    PIRATE(Pirate.class),
    JOUEURDEFLUTE(JoueurDeFlute.class),
    PYROMANE(Pyromane.class),
    SOEUR(Soeur.class),
    FRERE(Frere.class);

    RoleNightOrder(Class<? extends Role> roleClass) {
        this.roleClass = roleClass;
    }

    private final Class<? extends Role> roleClass;

    public Class<? extends Role> getRoleClass() {
        return roleClass;
    }
}
