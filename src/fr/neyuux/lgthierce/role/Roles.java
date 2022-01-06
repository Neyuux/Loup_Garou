package fr.neyuux.lgthierce.role;

import java.util.Arrays;

public enum Roles {

	LOUP_GAROU("Loup-Garou", "§c§lLoup-Garou", "§fVous êtes §c§lLoup-Garou§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	GRAND_MECHANT_LOUP("Grand Méchant Loup", "§4§lGrand Méchant §c§lLoup", "§fVous êtes §4§lGrand Méchant §c§lLoup§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Mais après cela, vous vous réveillez une deuxième fois pour §9dévorer une deuxième personne§f...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	INFECT_PERE_DES_LOUPS("Infect Père Des Loups", "§2§lInfect §8§lPère §cdes §lLoups", "§fVous êtes §2§lInfect §8§lPère §cdes §lLoups§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Chaque nuit, vous vous réunissez avec vos compères §fLoups pour décider d'une victime à éliminer. Après cela, vous vous réveillez et §9choisissez si votre victime deviendra §c§lLoup-Garou§f (elle gardera également ses pouvoirs de villageois si elle en a un).", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	CHIEN_LOUP("Chien-Loup", "§a§lChien§e-§c§lLoup", "§fVous êtes §a§lChien§e-§c§lLoup§f, au début de la partie, vous allez devoir choisir entre devenir §c§lLoup-Garou §fou §e§lSimple §a§lVillageois§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.CHIEN_LOUP),
	ENFANT_SAUVAGE("Enfant Sauvage", "§6§lEnfant Sauvage", "§fVous êtes §6§lEnfant Sauvage§f, vous allez, au début de la partie, devoir choisir votre maître. Si celui-ci §9meurt§f, vous devenez un §c§lLoup-Garou§f. Tant que cela ne s'est pas passé, votre but est d'éliminer est §9d'éliminer tous les loups-garous (ou rôles solos)§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	LOUP_GAROU_BLANC("Loup-Garou Blanc", "§c§lLoup-Garou §f§lBlanc", "§fVous êtes §c§lLoup-Garou §f§lBlanc§f, votre objectif est de §9terminer la partie seul§f. Pour les autres §c§lLoups-Garous§f, vous apparaissez comme leur coéquipier : attention à ne pas être découvert...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU_BLANC),
	VOLEUR("Voleur", "§3§lVoleur", "§fVous êtes §3§lVoleur§f, au début de la partie, vous allez devoir §9choisir §fentre les deux rôles qui n'ont pas été distribué (ou en choisir aucun)...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VOLEUR),
	ANGE("Ange", "§b§lAnge", "§fVous êtes §b§lAnge§f, votre but est de vous faire lyncher(tuer) par le village au premier tour. Si vous réussissez : la victoire sera votre, mais dans le cas contraire : vous deviendrez §e§lSimple §a§lVillageois§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.ANGE),
	COMEDIEN("Comédien", "§5§lComédien", "§fVous êtes §5§lComédien§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous obtiendrez §93 pouvoirs de rôles villageois§f que vous pourrez utiliser une fois chacun pendant la partie.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SERVANTE_DEVOUEE("Servante Dévouée", "§d§lServante §5Dévouée", "§fVous êtes §d§lServante §5Dévouée§f, vous n'avez pas réellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de §9prendre son rôle§f ou non. (Vous deviendrez donc le rôle que ce joueur incarnait)", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	JOUEUR_DE_FLUTE("Joueur de Flûte", "§5§lJoueur de Flûte", "§fVous êtes §5§lJoueur de Flûte§f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en §9enchantant tous les joueurs§f avec votre flûte. Chaque nuit, vous pouvez enchanter jusqu'à 2 personnes.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.JOUEUR_DE_FLUTE),
	CUPIDON("Cupidon", "§9§lCupi§d§ldon", "§fVous êtes §9§lCupi§d§ldon§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Au début de la partie, vous pouvez §9sélectionner 2 joueurs§f pour qu'ils deviennent le §d§lCouple§f de cette partie. Ils devront gagner ensemble ou avec leur camp d'origine (s'ils sont ensemble) ; et si l'un d'entre eux meurt, l'autre se suicidera d'un chagrin d'amour.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	CHASSEUR("Chasseur", "§2§lChasseur", "§fVous êtes §2§lChasseur§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). A votre mort, vous dégainerez votre fusil et avec la dernière balle de votre chargeur, vous pourrez §9emmener quelqu'un dans la mort avec vous§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	VOYANTE("Voyante", "§d§lV§5§lo§d§ly§5§la§d§ln§5§lt§d§le", "§fVous êtes §d§lV§5§lo§d§ly§5§la§d§ln§5§lt§d§le§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez §9apprendre le rôle d'un joueur§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SORCIERE("Sorcière", "§5§lSorcière", "§fVous êtes §5§lSorcière§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Vous possèdez 2 §2potions§f : une §apotion de vie§f§o(qui vous permettera de réssuciter un joueur, dont le nom vous sera donné, dévoré par les Loups)§f et une §4potion de mort§f§o(qui vous permettera de tuer un joueur de votre choix)§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	PETITE_FILLE("Petite Fille", "§9§lPetite §b§lFille", "§fVous êtes §9§lPetite §b§lFille§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Chaque nuit, à la levée des Loups-Garous : vous pourrez §9espionner leurs messages§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SALVATEUR("Salvateur", "§e§lSalvateur", "§fVous êtes §e§lSalvateur§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Chaque nuit, vous pourrez §9protéger§f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas protéger deux fois la même personne.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	IDIOT_DU_VILLAGE("Idiot du Village", "§d§lIdiot §e§ldu Village", "§fVous êtes §d§lIdiot §e§ldu Village§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, une fois dans la partie, le village décide de vous pendre, ils §9reconnaîtront votre bêtise§f. Vous ne mourrez donc pas, mais §9vous ne pourrez plus voter§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	BOUC_EMISSAIRE("Bouc Émissaire", "§c§lBouc §a§lÉmissaire", "§fVous êtes §c§lBouc §a§lÉmissaire§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, pendant la partie, il y a égalité dans les votes, §9vous mourrez§f. (§8§l§n§m§oGROSSE VICTIME§f)", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	ANCIEN("Ancien", "§7§lAncien", "§fVous êtes §7§lAncien§f, votre but est d'éliminer tous les §c§lLoups-Garous§f (ou rôles solos). Si, pendant une nuit, les Loups décident de vous attaquer, vous §9survivez§f §o(utilisable qu'une seule fois)§f. Cependant si le village vous éliminent pendant le jour, tous les villageois §4perdront leurs pouvoirs§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	RENARD("Renard", "§6§lRenard", "§fVous êtes §6§lRenard§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez sélectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un §c§lLoup-Garou§f, vous §9gardez votre pouvoir§f. Par contre, s'il n'y en a aucun, vous §9perdez votre pouvoir§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SOEUR("Soeur", "§d§lSoeur", "§fVous êtes §d§lSoeur§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Pendant la partie, votre soeur sera votre coéquipière, vous pouvez donc §9lui faire confiance§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	FRERE("Frère", "§3§lFrère", "§fVous êtes §3§lFrère§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Pendant la partie, vos deux frères seront vos coéquipiers, vous pouvez donc §9leur faire confiance§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	MONTREUR_D$OURS("Montreur d'Ours", "§6§lMontreur d'Ours", "§fVous êtes §6§lMontreur d'Ours§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). A chaque matinée, votre ours grognera si un ou deux de vos voisins est un §c§lLoup-Garou§f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	CHEVALIER_A_L$EPEE_ROUILLEE("Chevalier a l'Épée Rouillée", "§7§lChevalier §eà l'§7Épée §6§lRouillée", "§fVous êtes §7§lChevalier §eà l'§7Épée §6§lRouillée§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Si vous mourrez par les loups, le premier Loup à votre gauche§o(réunion)§f ou en dessous dans le tab§o(libre)§f tombera gravement malade : §9il mourra§f au tour suivant.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	VILLAGEOIS_VILLAGEOIS("Villageois-Villageois", "§a§lVillageois§e-§a§lVillageois", "§fVous êtes §a§lVillageois§e-§a§lVillageois§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Vous n'avez pas de pouvoir particulier, cependant, §2tout le monde connait votre identité§f...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SIMPLE_VILLAGEOIS("Simple Villageois", "§e§lSimple §a§lVillageois", "§fVous êtes §e§lSimple §a§lVillageois§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous n'avez §9aucun pouvoir§f particulier.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	
	CHAPERON_ROUGE("Chaperon Rouge", "§b§lChaperon §c§lRouge", "§fVous êtes §b§lChaperon §c§lRouge§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Tant que qu'un chasseur sera encore présent dans la partie, les Loups ne §9pourront pas vous tuer§f.", RDeck.WOLFY, RCamp.VILLAGE),
	DICTATEUR("Dictateur", "§4§lDicta§2§lteur", "§fVous êtes §4§lDicta§2§lteur§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez faire un §9coup d'état §fet prendre le contrôle du vote. Vous serez le seul à pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le rôle de §bMaire§f.", RDeck.WOLFY, RCamp.VILLAGE),
	FOSSOYEUR("Fossoyeur", "§8§lFossoyeur", "§fVous êtes §8§lFossoyeur§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). A votre mort, vous pourrez choisir un joueur ; et dans le chat, sera indiqué le pseudo de ce joueur et le pseudo d'un autre joueur d'un §9camp différent§f du premier.", RDeck.WOLFY, RCamp.VILLAGE),
	MERCENAIRE("Mercenaire", "§c§lMerce§5§lnaire", "§fVous êtes §c§lMerce§5§lnaire§f, le premier jour, votre objectif est d'éliminer la cible qui vous est attribuée. Si vous y parvenez, vous gagnez seul la partie instantanément. Sinon, vous devenez §e§lSimple §a§lVillageois§f.", RDeck.WOLFY, RCamp.MERCENAIRE),
	
	ANKOU("Ankou", "§6§lA§c§ln§f§lk§7§lo§8§lu", "§fVous êtes §6§lA§c§ln§f§lk§7§lo§8§lu§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois que vous mourrez, vous pouvez §9continuer de voter§f pendant deux tours maximum depuis le cimetière à l'aide de la commande §e/ankou§f. Votre vote ne sera pas visible des joueurs mais sera comptabilisé et participera à l'élimination d'un joueur de jour tout en voyant les morts parler.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	CHAMAN("Chaman", "§b§lCha§a§lman", "§fVous êtes §b§lCha§a§lman§f, votre but est d'éliminer tous les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pour §9voir les messages des morts§f.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	CORBEAU("Corbeau", "§8§lC§f§lo§8§lr§f§lb§8§le§f§la§8§lu", "§fVous êtes §8§lC§f§lo§8§lr§f§lb§8§le§f§la§8§lu§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez désigner un joueur qui §9recevra 2 votes§f au petit matin...", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	NOCTAMBULE("Noctambule", "§9§lNoctambule", "§fVous êtes §9§lNoctambule§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous devez choisir un joueur chez qui §9dormir§f, ce joueur connaîtra alors votre identité mais est privé de ses pouvoirs pour la nuit.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	
	DETECTIVE("Détective", "§7§lDétective", "§fVous êtes §7§lDétective§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous serez appelé pour §9enquêter§f sur deux joueurs : vous saurez s'ils sont du même camp ou non.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	DUR_A_CUIRE("Dur A Cuire", "§c§lDur §cà §6§lCuire", "§fVous êtes §c§lDur à §6§lCuire§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lorque les Loups voudront vous tuer, vous survivrez jusqu'au jour suivant.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	ENCHANTEUR("Enchanteur", "§5§lEnchanteur", "§fVous êtes §5§lEnchanteur§f, votre objectif est d'éliminer tous les innocents (ceux qui ne sont pas du camp des §c§lLoups-Garous§f) ! Vous ne connaissez pas les autres Loups. Chaque nuit, vous pourrez enchanter un joueur et découvrir s'il agit d'une voyante ou un Loup.", RDeck.WEREWOLF_ONLINE, RCamp.LOUP_GAROU),
	FILLE_DE_JOIE("Fille de Joie", "§d§lFille de Joie", "§fVous êtes §d§lFille de Joie§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez aller ken un joueur. Si ce joueur est un Loup ou est mangé par les Loups, vous §9mourrez§f. Si les Loups essaient de vous tuer pendant que vous êtes chez quelqu'un d'autre, vous §9survivez§f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	GARDE_DU_CORPS("Garde du Corps", "§7§lGarde §e§ldu Corps", "§fVous êtes §7§lGarde §e§ldu Corps§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pouvez choisir de §9protéger§f un joueur. S'il est censé mourir pendant la nuit, vous §9mourrez§f à sa place.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	HUMAIN_MAUDIT("Humain Maudit", "§e§lHumain §4§lMaudit", "§fVous êtes §e§lHumain §4§lMaudit§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Si vous vous faites cibler par les Loups, vous ne mourrez pas et devenez l'un d'entre-eux.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	JUMEAU("Jumeau", "§5§lJumeau", "§fVous êtes §5§lJumeau§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lors de la première nuit, vous devrez choisir un joueur. Lorsque ce joueur mourra, vous obtiendrez son rôle.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	MAMIE_GRINCHEUSE("Mamie Grincheuse", "§d§lMamie §c§lGrincheuse", "§fVous êtes §d§lMamie §c§lGrincheuse§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez choisir un joueur, l'empêchant de voter au jour suivant ; mais vous ne pouvez pas sélectionner deux fois de suite la même personne.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	MACON("Maçon", "§6§lMaçon", "§fVous êtes §6§lMaçon§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous et les autres §6§lMaçons§f vous reconnaissez entre-vous §8(car vous vous appellez tous Ricardo)§f ; vous pouvez donc avoir confiance en eux.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	NECROMANCIEN("Nécromancien", "§9§lNécromancien", "§fVous êtes §9§lNégromancien§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez §9récussiter§f un joueur. S'il avait un pouvoir, il le perd et devient §e§lSimple §a§lVillageois§f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PACIFISTE("Pacifiste", "§d§lPacifiste", "§fVous êtes §dPacifiste§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez révéler le rôle d'un joueur et empêcher tous les joueurs de voter ce jour là.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PETITE_FILLE2("Petite Fille (WO)", "§9§lPetite §b§lFille §0§oWO", "§fVous êtes §9§lPetite §b§lFille§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous pourrez ouvrir les yeux, si vous le faites, vous aurez 20% de chance de §9trouver un Loup§f et 5% de chance de §9mourir§f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PORTEUR_DE_L$AMULETTE("Porteur de L'Amulette", "§6§lPorteur de §d§lL'Amulette", "§fVous êtes §6§lPorteur de §d§lL'Amulette§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous ne pouvez pas mourir des Loups.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PRESIDENT("Président", "§e§lPrésident", "§fVous êtes §e§lPrésident§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Tous le monde connait votre identité, mais si vous mourrez, le §9village a perdu§f. Vous possédez également le rôle de maire s'il est activé.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PRETRE("Prêtre", "§e§lP§f§lr§e§lê§f§lt§e§lr§f§le", "§fVous êtes §e§lP§f§lr§e§lê§f§lt§e§lr§f§le, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Vous possédez une fiole d'eau bénite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il §9mourra§f sinon, vous mourrez.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PYROMANE("Pyromane", "§c§lP§6§ly§c§lr§6§lo§c§lm§6§la§c§ln§6§le", "§fVous êtes §c§lP§6§ly§c§lr§6§lo§c§lm§6§la§c§ln§6§le§f, votre but est d'éliminer tous les joueurs de la partie et de, par conséquent, de gagner seul. Chaque nuit, vous pourrez décider d'enrober un joueur d'essence ou de mettre le feu à tous les joueurs déjà huilés...", RDeck.WEREWOLF_ONLINE, RCamp.PYROMANE),
	VILAIN_GARCON("Vilain Garçon", "§c§lVilain §b§lGarçon", "§fVous êtes §c§lVilain §b§lGarçon§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Une fois dans la partie, vous pourrez échanger les rôles de deux personnes.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	VOYANTE_APPRENTIE("Voyante Apprentie", "§dV§5o§dy§5a§dn§5t§de §a§lApprentie", "§fVous êtes §d§lV§5§lo§d§ly§5§la§d§ln§5§lt§d§le §a§lApprentie§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Lorsque qu'un joueur qui est voyante mourra, vous prendrez sa place et son rôle.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	VOYANTE_D$AURA("Voyante d'Aura", "§dV§5o§dy§5a§dn§5t§de §4§ld'Aura", "§fVous êtes §d§lV§5§lo§d§ly§5§la§d§ln§5§lt§d§le §4§ld'Aura§f, votre but est d'éliminer les §c§lLoups-Garous §f(ou rôles solos). Chaque nuit, vous vous réveillerez et découvrez si un joueur que vous choisirez est Loup ou non.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE)
	;
	
	
	 Roles(String name, String displayname, String description, RDeck deck, RCamp camp) {
		    this.camp = camp;
		    this.name = name;
		    this.deck = deck;
		    this.displayName = displayname;
		    this.description = description;
	 }
	
	
	private final RCamp camp;
	
	private final String name;
	
	private final String displayName;
	
	private final String description;
	
	private final RDeck deck;
	
	
	public RCamp getCamp() {
		return this.camp;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	
	public static Roles getByDisplayName(String displayname) {
		return Arrays.stream(Roles.values()).filter(roles -> roles.getDisplayName().equals(displayname)).findFirst().orElse(null);
	}
	
	
	public String getDescription() {
		return this.description;
	}
	
	public RDeck getDeck() {
		return this.deck;
	}
	
}
