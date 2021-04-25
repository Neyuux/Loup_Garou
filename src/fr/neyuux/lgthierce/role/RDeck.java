package fr.neyuux.lgthierce.role;

public enum RDeck {
	
	LOUP_GAROU_DE_THIERCELIEUX("Philippe des Palières & Hervé Marly", "LG-Thiercelieux"),
	WOLFY("wolfy.fr", "Wolfy"),
	LOUP_GAROU_EN_LIGNE("loups-garous-en-ligne.com", "LGEL"),
	WEREWOLF_ONLINE("Philipp Eichhorn", "Werewolf")
	;
	
	RDeck(String crédit, String alias) {
		this.crédit = crédit;
		this.alias = alias;
	}
	
	private String crédit;
	private String alias;
	
	public String getCreator() {
		return this.crédit;
	}
	
	public String getAlias() {
		return this.alias;
	}
	public static final RDeck getByAlias(String alias) {
		RDeck d = null;
		
		for (RDeck decks : RDeck.values()) {
			if (decks.getAlias().equals(alias)) d = decks;
		}
		
		return d;
	}
}
