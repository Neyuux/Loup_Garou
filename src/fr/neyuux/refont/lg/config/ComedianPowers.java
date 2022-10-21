package fr.neyuux.refont.lg.config;

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
