package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.LG;
import fr.neyuux.refont.lg.roles.Role;

import java.lang.reflect.Constructor;

public enum ComedianPowers {

    VOYANTE(LG.getInstance().getRoles().get("Voyante")),
    ANCIEN(LG.getInstance().getRoles().get("Ancien")),
    MONTREUR_D_OURS(LG.getInstance().getRoles().get("MontreurDOurs")),
    PETITE_FILLE(LG.getInstance().getRoles().get("PetiteFille")),
    RENARD(LG.getInstance().getRoles().get("Renard")),
    SALVATEUR(LG.getInstance().getRoles().get("Salvateur")),
    SORCIERE(LG.getInstance().getRoles().get("Sorciere"));


    ComedianPowers(Constructor<? extends Role> role) {
        this.role = role;
    }

    private final  Constructor<? extends Role> role;

    public Constructor<? extends Role> getRole() {
        return role;
    }


    public static ComedianPowers getByRole(Constructor<? extends Role> role) {
        for (ComedianPowers power : ComedianPowers.values())
            if (role.equals(power.getRole()))
                return power;

        return null;
    }
}
