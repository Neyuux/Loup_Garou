package fr.neyuux.refont.lg.roles.classes;

import fr.neyuux.refont.lg.roles.Camps;
import fr.neyuux.refont.lg.roles.Decks;
import fr.neyuux.refont.lg.roles.Role;

public class Vampire extends Role {

    @Override
    public String getDisplayName() {
        return "§5§lVampire";
    }

    @Override
    public String getConfigName() {
        return "Vampire";
    }

    @Override
    public String getDeterminingName() {
        return "des " + this.getDisplayName() + "s";
    }

    @Override
    public int getMaxNumber() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "§fVous êtes "+this.getDisplayName()+", votre but est d'éliminer tous les joueurs qui ne sont pas des Vampires. Chaque nuit vous vous réveillez pour §9mordre§f un joueur. Cette morsure §9transformera§f le joueur en Vampire une nuit sur deux, ou sinon §9tuera§f le joueur.";
    }

    @Override
    public Camps getBaseCamp() {
        return Camps.VAMPIRE;
    }

    @Override
    public Decks getDeck() {
        return Decks.LEOMELKI;
    }

    @Override
    public int getTimeout() {
        return 35;
    }

    @Override
    public String getActionMessage() {
        return "§fVous avez §d" + this.getTimeout() + " secondes §fpour voter pour choisir qui mordre.";
    }

    
}
