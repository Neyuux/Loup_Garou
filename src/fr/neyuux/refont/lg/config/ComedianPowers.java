package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.roles.Role;

import java.lang.reflect.Constructor;

public enum ComedianPowers {

    VOYANTE("Voyante"),
    ANCIEN("Ancien"),
    MONTREUR_D_OURS("MontreurDOurs"),
    PETITE_FILLE("PetiteFille"),
    RENARD("Renard"),
    SALVATEUR("Salvateur"),
    SORCIERE("Sorciere");


    ComedianPowers(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }

}
