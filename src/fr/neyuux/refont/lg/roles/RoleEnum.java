package fr.neyuux.refont.lg.roles;

import fr.neyuux.refont.lg.roles.classes.*;

public enum RoleEnum {

LOUP_GAROU(LoupGarou.class),
GRAND_MECHANT_LOUP(GrandMechantLoup.class),
CHIEN_LOUP(ChienLoup.class),
INFECT_PERE_DES_LOUPS(InfectPereDesLoups.class),
ENFANT_SAUVAGE(EnfantSauvage.class),
LOUP_GAROU_BLANC(LoupGarouBlanc.class),
VOLEUR(Voleur.class),
ANGE(Ange.class),
COMEDIEN(Comedien.class),
SERVANTE_DEVOUEE(ServanteDevouee.class),
JOUEUR_DE_FLUTE(JoueurDeFlute.class),
CUPIDON(Cupidon.class),
CHASSEUR(Chasseur.class),
VOYANTE(Voyante.class),
SORCIERE(Sorciere.class),
PETITE_FILLE(PetiteFille.class),
SALVATEUR(Salvateur.class),
IDIOT_DU_VILLAGE(IdiotDuVillage.class),
BOUC_EMISSAIRE(BoucEmissaire.class),
ANCIEN(Ancien.class),
RENARD(Renard.class),
SOEUR(Soeur.class),
FRERE(Frere.class),
MONTREUR_D_OURS(MontreurDOurs.class),
CHEVALIER_A_L_EPEE_ROUILLEE(ChevalierALEpeeRouillee.class),
VILLAGEOIS_VILLAGEOIS(VillageoisVillageois.class),
SIMPLE_VILLAGEOIS(SimpleVillageois.class),

CHAPERON_ROUGE(ChaperonRouge.class),
DICTATEUR(Dictateur.class),
FOSSOYEUR(Fossoyeur.class),
MERCENAIRE(Mercenaire.class),

ANKOU(Ankou.class),
CHAMAN(Chaman.class),
CORBEAU(Corbeau.class),
NOCTAMBULE(Noctambule.class),

DETECTIVE(Detective.class),
DUR_A_CUIRE(DurACuire.class),
ENCHANTEUR(Enchanteur.class),
FILLE_DE_JOIE(FilleDeJoie.class),
GARDE_DU_CORPS(GardeDuCorps.class),
HUMAIN_MAUDIT(HumainMaudit.class),
JUMEAU(Jumeau.class),
MAMIE_GRINCHEUSE(MamieGrincheuse.class),
MACON(Macon.class),
NECROMANCIEN(Necromancien.class),
PACIFISTE(Pacifiste.class),
PETITE_FILLE_WO(PetiteFilleWO.class),
PORTEUR_DE_L_AMULETTE(PorteurDeLAmulette.class),
PRESIDENT(President.class),
PRETRE(Pretre.class),
PYROMANE(Pyromane.class),
VILAIN_GARCON(VilainGarcon.class),
VOYANTE_APPRENTIE(VoyanteApprentie.class),
VOYANTE_D_AURA(VoyanteDAura.class);

    RoleEnum(Class<? extends Role> clazz) {
        this.roleClass = clazz;
    }

    private final Class<? extends Role> roleClass;


    public Class<? extends Role> getRoleClass() {
        return roleClass;
    }
}
