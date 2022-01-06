package fr.neyuux.lgthierce.role;

import java.util.Arrays;

public enum Roles {

	LOUP_GAROU("Loup-Garou", "�c�lLoup-Garou", "�fVous �tes �c�lLoup-Garou�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	GRAND_MECHANT_LOUP("Grand M�chant Loup", "�4�lGrand M�chant �c�lLoup", "�fVous �tes �4�lGrand M�chant �c�lLoup�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer. Mais apr�s cela, vous vous r�veillez une deuxi�me fois pour �9d�vorer une deuxi�me personne�f...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	INFECT_PERE_DES_LOUPS("Infect P�re Des Loups", "�2�lInfect �8�lP�re �cdes �lLoups", "�fVous �tes �2�lInfect �8�lP�re �cdes �lLoups�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Chaque nuit, vous vous r�unissez avec vos comp�res �fLoups pour d�cider d'une victime � �liminer. Apr�s cela, vous vous r�veillez et �9choisissez si votre victime deviendra �c�lLoup-Garou�f (elle gardera �galement ses pouvoirs de villageois si elle en a un).", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU),
	CHIEN_LOUP("Chien-Loup", "�a�lChien�e-�c�lLoup", "�fVous �tes �a�lChien�e-�c�lLoup�f, au d�but de la partie, vous allez devoir choisir entre devenir �c�lLoup-Garou �fou �e�lSimple �a�lVillageois�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.CHIEN_LOUP),
	ENFANT_SAUVAGE("Enfant Sauvage", "�6�lEnfant Sauvage", "�fVous �tes �6�lEnfant Sauvage�f, vous allez, au d�but de la partie, devoir choisir votre ma�tre. Si celui-ci �9meurt�f, vous devenez un �c�lLoup-Garou�f. Tant que cela ne s'est pas pass�, votre but est d'�liminer est �9d'�liminer tous les loups-garous (ou r�les solos)�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	LOUP_GAROU_BLANC("Loup-Garou Blanc", "�c�lLoup-Garou �f�lBlanc", "�fVous �tes �c�lLoup-Garou �f�lBlanc�f, votre objectif est de �9terminer la partie seul�f. Pour les autres �c�lLoups-Garous�f, vous apparaissez comme leur co�quipier : attention � ne pas �tre d�couvert...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.LOUP_GAROU_BLANC),
	VOLEUR("Voleur", "�3�lVoleur", "�fVous �tes �3�lVoleur�f, au d�but de la partie, vous allez devoir �9choisir �fentre les deux r�les qui n'ont pas �t� distribu� (ou en choisir aucun)...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VOLEUR),
	ANGE("Ange", "�b�lAnge", "�fVous �tes �b�lAnge�f, votre but est de vous faire lyncher(tuer) par le village au premier tour. Si vous r�ussissez : la victoire sera votre, mais dans le cas contraire : vous deviendrez �e�lSimple �a�lVillageois�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.ANGE),
	COMEDIEN("Com�dien", "�5�lCom�dien", "�fVous �tes �5�lCom�dien�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Au d�but de la partie, vous obtiendrez �93 pouvoirs de r�les villageois�f que vous pourrez utiliser une fois chacun pendant la partie.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SERVANTE_DEVOUEE("Servante D�vou�e", "�d�lServante �5D�vou�e", "�fVous �tes �d�lServante �5D�vou�e�f, vous n'avez pas r�ellement d'objectif. Pendant la partie : lorsque quelqu'un mourra au vote, vous aurez 10 secondes pour choisir de �9prendre son r�le�f ou non. (Vous deviendrez donc le r�le que ce joueur incarnait)", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	JOUEUR_DE_FLUTE("Joueur de Fl�te", "�5�lJoueur de Fl�te", "�fVous �tes �5�lJoueur de Fl�te�f, votre objectif est de gagner la partie (wow). Vous pouvez remporter celle-ci en �9enchantant tous les joueurs�f avec votre fl�te. Chaque nuit, vous pouvez enchanter jusqu'� 2 personnes.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.JOUEUR_DE_FLUTE),
	CUPIDON("Cupidon", "�9�lCupi�d�ldon", "�fVous �tes �9�lCupi�d�ldon�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Au d�but de la partie, vous pouvez �9s�lectionner 2 joueurs�f pour qu'ils deviennent le �d�lCouple�f de cette partie. Ils devront gagner ensemble ou avec leur camp d'origine (s'ils sont ensemble) ; et si l'un d'entre eux meurt, l'autre se suicidera d'un chagrin d'amour.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	CHASSEUR("Chasseur", "�2�lChasseur", "�fVous �tes �2�lChasseur�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). A votre mort, vous d�gainerez votre fusil et avec la derni�re balle de votre chargeur, vous pourrez �9emmener quelqu'un dans la mort avec vous�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	VOYANTE("Voyante", "�d�lV�5�lo�d�ly�5�la�d�ln�5�lt�d�le", "�fVous �tes �d�lV�5�lo�d�ly�5�la�d�ln�5�lt�d�le�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez �9apprendre le r�le d'un joueur�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SORCIERE("Sorci�re", "�5�lSorci�re", "�fVous �tes �5�lSorci�re�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Vous poss�dez 2 �2potions�f : une �apotion de vie�f�o(qui vous permettera de r�ssuciter un joueur, dont le nom vous sera donn�, d�vor� par les Loups)�f et une �4potion de mort�f�o(qui vous permettera de tuer un joueur de votre choix)�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	PETITE_FILLE("Petite Fille", "�9�lPetite �b�lFille", "�fVous �tes �9�lPetite �b�lFille�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Chaque nuit, � la lev�e des Loups-Garous : vous pourrez �9espionner leurs messages�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SALVATEUR("Salvateur", "�e�lSalvateur", "�fVous �tes �e�lSalvateur�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Chaque nuit, vous pourrez �9prot�ger�f un joueur de l'attaque des Loups. Cependant, vous ne pouvez pas prot�ger deux fois la m�me personne.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	IDIOT_DU_VILLAGE("Idiot du Village", "�d�lIdiot �e�ldu Village", "�fVous �tes �d�lIdiot �e�ldu Village�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, une fois dans la partie, le village d�cide de vous pendre, ils �9reconna�tront votre b�tise�f. Vous ne mourrez donc pas, mais �9vous ne pourrez plus voter�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	BOUC_EMISSAIRE("Bouc �missaire", "�c�lBouc �a�l�missaire", "�fVous �tes �c�lBouc �a�l�missaire�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, pendant la partie, il y a �galit� dans les votes, �9vous mourrez�f. (�8�l�n�m�oGROSSE VICTIME�f)", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	ANCIEN("Ancien", "�7�lAncien", "�fVous �tes �7�lAncien�f, votre but est d'�liminer tous les �c�lLoups-Garous�f (ou r�les solos). Si, pendant une nuit, les Loups d�cident de vous attaquer, vous �9survivez�f �o(utilisable qu'une seule fois)�f. Cependant si le village vous �liminent pendant le jour, tous les villageois �4perdront leurs pouvoirs�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	RENARD("Renard", "�6�lRenard", "�fVous �tes �6�lRenard�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pouvez choisir d'utiliser votre pouvoir : si vous l'utilisez, vous devrez s�lectionner un groupe de 3 personnes voisines en choisissant son joueur central. Si parmis ce groupe il se trouve un �c�lLoup-Garou�f, vous �9gardez votre pouvoir�f. Par contre, s'il n'y en a aucun, vous �9perdez votre pouvoir�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SOEUR("Soeur", "�d�lSoeur", "�fVous �tes �d�lSoeur�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Pendant la partie, votre soeur sera votre co�quipi�re, vous pouvez donc �9lui faire confiance�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	FRERE("Fr�re", "�3�lFr�re", "�fVous �tes �3�lFr�re�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Pendant la partie, vos deux fr�res seront vos co�quipiers, vous pouvez donc �9leur faire confiance�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	MONTREUR_D$OURS("Montreur d'Ours", "�6�lMontreur d'Ours", "�fVous �tes �6�lMontreur d'Ours�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). A chaque matin�e, votre ours grognera si un ou deux de vos voisins est un �c�lLoup-Garou�f.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	CHEVALIER_A_L$EPEE_ROUILLEE("Chevalier a l'�p�e Rouill�e", "�7�lChevalier �e� l'�7�p�e �6�lRouill�e", "�fVous �tes �7�lChevalier �e� l'�7�p�e �6�lRouill�e�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Si vous mourrez par les loups, le premier Loup � votre gauche�o(r�union)�f ou en dessous dans le tab�o(libre)�f tombera gravement malade : �9il mourra�f au tour suivant.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	VILLAGEOIS_VILLAGEOIS("Villageois-Villageois", "�a�lVillageois�e-�a�lVillageois", "�fVous �tes �a�lVillageois�e-�a�lVillageois�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Vous n'avez pas de pouvoir particulier, cependant, �2tout le monde connait votre identit�f...", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	SIMPLE_VILLAGEOIS("Simple Villageois", "�e�lSimple �a�lVillageois", "�fVous �tes �e�lSimple �a�lVillageois�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous n'avez �9aucun pouvoir�f particulier.", RDeck.LOUP_GAROU_DE_THIERCELIEUX, RCamp.VILLAGE),
	
	CHAPERON_ROUGE("Chaperon Rouge", "�b�lChaperon �c�lRouge", "�fVous �tes �b�lChaperon �c�lRouge�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tant que qu'un chasseur sera encore pr�sent dans la partie, les Loups ne �9pourront pas vous tuer�f.", RDeck.WOLFY, RCamp.VILLAGE),
	DICTATEUR("Dictateur", "�4�lDicta�2�lteur", "�fVous �tes �4�lDicta�2�lteur�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez faire un �9coup d'�tat �fet prendre le contr�le du vote. Vous serez le seul � pouvoir voter ; si vous tuez un membre du village, vous mourrez, sinon, vous obtiendrez le r�le de �bMaire�f.", RDeck.WOLFY, RCamp.VILLAGE),
	FOSSOYEUR("Fossoyeur", "�8�lFossoyeur", "�fVous �tes �8�lFossoyeur�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). A votre mort, vous pourrez choisir un joueur ; et dans le chat, sera indiqu� le pseudo de ce joueur et le pseudo d'un autre joueur d'un �9camp diff�rent�f du premier.", RDeck.WOLFY, RCamp.VILLAGE),
	MERCENAIRE("Mercenaire", "�c�lMerce�5�lnaire", "�fVous �tes �c�lMerce�5�lnaire�f, le premier jour, votre objectif est d'�liminer la cible qui vous est attribu�e. Si vous y parvenez, vous gagnez seul la partie instantan�ment. Sinon, vous devenez �e�lSimple �a�lVillageois�f.", RDeck.WOLFY, RCamp.MERCENAIRE),
	
	ANKOU("Ankou", "�6�lA�c�ln�f�lk�7�lo�8�lu", "�fVous �tes �6�lA�c�ln�f�lk�7�lo�8�lu�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois que vous mourrez, vous pouvez �9continuer de voter�f pendant deux tours maximum depuis le cimeti�re � l'aide de la commande �e/ankou�f. Votre vote ne sera pas visible des joueurs mais sera comptabilis� et participera � l'�limination d'un joueur de jour tout en voyant les morts parler.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	CHAMAN("Chaman", "�b�lCha�a�lman", "�fVous �tes �b�lCha�a�lman�f, votre but est d'�liminer tous les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pour �9voir les messages des morts�f.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	CORBEAU("Corbeau", "�8�lC�f�lo�8�lr�f�lb�8�le�f�la�8�lu", "�fVous �tes �8�lC�f�lo�8�lr�f�lb�8�le�f�la�8�lu�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez d�signer un joueur qui �9recevra 2 votes�f au petit matin...", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	NOCTAMBULE("Noctambule", "�9�lNoctambule", "�fVous �tes �9�lNoctambule�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous devez choisir un joueur chez qui �9dormir�f, ce joueur conna�tra alors votre identit� mais est priv� de ses pouvoirs pour la nuit.", RDeck.LOUP_GAROU_EN_LIGNE, RCamp.VILLAGE),
	
	DETECTIVE("D�tective", "�7�lD�tective", "�fVous �tes �7�lD�tective�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous serez appel� pour �9enqu�ter�f sur deux joueurs : vous saurez s'ils sont du m�me camp ou non.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	DUR_A_CUIRE("Dur A Cuire", "�c�lDur �c� �6�lCuire", "�fVous �tes �c�lDur � �6�lCuire�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lorque les Loups voudront vous tuer, vous survivrez jusqu'au jour suivant.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	ENCHANTEUR("Enchanteur", "�5�lEnchanteur", "�fVous �tes �5�lEnchanteur�f, votre objectif est d'�liminer tous les innocents (ceux qui ne sont pas du camp des �c�lLoups-Garous�f) ! Vous ne connaissez pas les autres Loups. Chaque nuit, vous pourrez enchanter un joueur et d�couvrir s'il agit d'une voyante ou un Loup.", RDeck.WEREWOLF_ONLINE, RCamp.LOUP_GAROU),
	FILLE_DE_JOIE("Fille de Joie", "�d�lFille de Joie", "�fVous �tes �d�lFille de Joie�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pouvez aller ken un joueur. Si ce joueur est un Loup ou est mang� par les Loups, vous �9mourrez�f. Si les Loups essaient de vous tuer pendant que vous �tes chez quelqu'un d'autre, vous �9survivez�f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	GARDE_DU_CORPS("Garde du Corps", "�7�lGarde �e�ldu Corps", "�fVous �tes �7�lGarde �e�ldu Corps�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pouvez choisir de �9prot�ger�f un joueur. S'il est cens� mourir pendant la nuit, vous �9mourrez�f � sa place.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	HUMAIN_MAUDIT("Humain Maudit", "�e�lHumain �4�lMaudit", "�fVous �tes �e�lHumain �4�lMaudit�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Si vous vous faites cibler par les Loups, vous ne mourrez pas et devenez l'un d'entre-eux.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	JUMEAU("Jumeau", "�5�lJumeau", "�fVous �tes �5�lJumeau�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lors de la premi�re nuit, vous devrez choisir un joueur. Lorsque ce joueur mourra, vous obtiendrez son r�le.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	MAMIE_GRINCHEUSE("Mamie Grincheuse", "�d�lMamie �c�lGrincheuse", "�fVous �tes �d�lMamie �c�lGrincheuse�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez choisir un joueur, l'emp�chant de voter au jour suivant ; mais vous ne pouvez pas s�lectionner deux fois de suite la m�me personne.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	MACON("Ma�on", "�6�lMa�on", "�fVous �tes �6�lMa�on�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous et les autres �6�lMa�ons�f vous reconnaissez entre-vous �8(car vous vous appellez tous Ricardo)�f ; vous pouvez donc avoir confiance en eux.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	NECROMANCIEN("N�cromancien", "�9�lN�cromancien", "�fVous �tes �9�lN�gromancien�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez �9r�cussiter�f un joueur. S'il avait un pouvoir, il le perd et devient �e�lSimple �a�lVillageois�f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PACIFISTE("Pacifiste", "�d�lPacifiste", "�fVous �tes �dPacifiste�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez r�v�ler le r�le d'un joueur et emp�cher tous les joueurs de voter ce jour l�.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PETITE_FILLE2("Petite Fille (WO)", "�9�lPetite �b�lFille �0�oWO", "�fVous �tes �9�lPetite �b�lFille�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous pourrez ouvrir les yeux, si vous le faites, vous aurez 20% de chance de �9trouver un Loup�f et 5% de chance de �9mourir�f.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PORTEUR_DE_L$AMULETTE("Porteur de L'Amulette", "�6�lPorteur de �d�lL'Amulette", "�fVous �tes �6�lPorteur de �d�lL'Amulette�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous ne pouvez pas mourir des Loups.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PRESIDENT("Pr�sident", "�e�lPr�sident", "�fVous �tes �e�lPr�sident�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Tous le monde connait votre identit�, mais si vous mourrez, le �9village a perdu�f. Vous poss�dez �galement le r�le de maire s'il est activ�.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PRETRE("Pr�tre", "�e�lP�f�lr�e�l�f�lt�e�lr�f�le", "�fVous �tes �e�lP�f�lr�e�l�f�lt�e�lr�f�le, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Vous poss�dez une fiole d'eau b�nite et chaque nuit, vous pourrez choisir de l'utiliser en ciblant un joueur : si vous le faites, si ce joueur est Loup, il �9mourra�f sinon, vous mourrez.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	PYROMANE("Pyromane", "�c�lP�6�ly�c�lr�6�lo�c�lm�6�la�c�ln�6�le", "�fVous �tes �c�lP�6�ly�c�lr�6�lo�c�lm�6�la�c�ln�6�le�f, votre but est d'�liminer tous les joueurs de la partie et de, par cons�quent, de gagner seul. Chaque nuit, vous pourrez d�cider d'enrober un joueur d'essence ou de mettre le feu � tous les joueurs d�j� huil�s...", RDeck.WEREWOLF_ONLINE, RCamp.PYROMANE),
	VILAIN_GARCON("Vilain Gar�on", "�c�lVilain �b�lGar�on", "�fVous �tes �c�lVilain �b�lGar�on�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Une fois dans la partie, vous pourrez �changer les r�les de deux personnes.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	VOYANTE_APPRENTIE("Voyante Apprentie", "�dV�5o�dy�5a�dn�5t�de �a�lApprentie", "�fVous �tes �d�lV�5�lo�d�ly�5�la�d�ln�5�lt�d�le �a�lApprentie�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Lorsque qu'un joueur qui est voyante mourra, vous prendrez sa place et son r�le.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE),
	VOYANTE_D$AURA("Voyante d'Aura", "�dV�5o�dy�5a�dn�5t�de �4�ld'Aura", "�fVous �tes �d�lV�5�lo�d�ly�5�la�d�ln�5�lt�d�le �4�ld'Aura�f, votre but est d'�liminer les �c�lLoups-Garous �f(ou r�les solos). Chaque nuit, vous vous r�veillerez et d�couvrez si un joueur que vous choisirez est Loup ou non.", RDeck.WEREWOLF_ONLINE, RCamp.VILLAGE)
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
