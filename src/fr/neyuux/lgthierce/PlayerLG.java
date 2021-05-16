package fr.neyuux.lgthierce;

import fr.neyuux.lgthierce.role.RCamp;
import fr.neyuux.lgthierce.role.Roles;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerLG {
	
	private final LG main;
	
	public PlayerLG(LG main, Player player) {
		this.main = main;
		this.player = player;
	}
	
	
	public Player player;
	
	private Block block = null;
	
	private RCamp camp = null;
	
	private Roles role = null;
	
	private Boolean hasUsedPower = false;
	
	private Boolean hasUsedDefinitivePower = false;
	
	private Boolean isMaire = Boolean.FALSE;
	
	private Player vote = null;
	
	private Boolean canVote = Boolean.TRUE;
	
	private int AnkouVotes = 2;
	
	private ArmorStand ArmorStand = null;
	
	private RCamp choosenCampCL = null;
	
	private Boolean isOnlyVisibleForLG = Boolean.FALSE;
	
	private Boolean isInCoupDEtat = Boolean.FALSE;
	
	private Boolean isVivant = Boolean.TRUE;
	
	private Boolean isInEqual = Boolean.FALSE;
	
	private Boolean isVoleur = Boolean.FALSE;
	
	private Boolean isComédien = Boolean.FALSE;
	
	private Boolean isServante = Boolean.FALSE;
	
	private Boolean isSalvated = Boolean.FALSE;
	
	private Boolean isCharmed = Boolean.FALSE;
	
	private Boolean isHuilé = Boolean.FALSE;
	
	private Boolean isInfected = Boolean.FALSE;
	
	private Boolean isSosoTargeted = Boolean.FALSE;
	
	private Boolean isNécroTargeted = Boolean.FALSE;
	
	private Boolean isLGTargeted = Boolean.FALSE;
	
	private Boolean isGMLTargeted = Boolean.FALSE;
	
	private Boolean isLGBTargeted = Boolean.FALSE;
	
	private Boolean isDayTargeted = Boolean.FALSE;
	
	private Boolean isPacifTargeted = Boolean.FALSE;
	
	private Boolean isMamieTargeted = Boolean.FALSE;
	
	private Boolean isCorbeauTargeted = Boolean.FALSE;
	
	private Boolean isNoctaTargeted = Boolean.FALSE;
	
	private Boolean isPyroTargeted = Boolean.FALSE;
	
	private Boolean isPF2Died = Boolean.FALSE;
	
	private Boolean isFDJDied = Boolean.FALSE;
	
	private int daysBeforeDieOfCEL = -1;
	
	private int daysBeforeDACDie = -1;
	
	private final List<Player> couple = new ArrayList<>();
	
	private Player lastInterdit = null;
	
	private Player otherPlayerHouse = player;
	
	private Player pretreThrower = null;
	
	private final List<Player> soeurOf = new ArrayList<>();
	
	private final List<Player> frèreOf = new ArrayList<>();
	
	private final List<Player> maitreof = new ArrayList<>();
	
	private final List<Player> targetOf = new ArrayList<>();
	
	private final List<Player> jumeauOf = new ArrayList<>();
	
	private Player protector = null;
	
	private int votes = 0;
	
	
	
	public void setCamp(RCamp camp) {
		if (camp.equals(RCamp.LOUP_GAROU) || camp.equals(RCamp.LOUP_GAROU_BLANC)) {
			main.setLGScoreboard(this.player);
			for (Player p : main.players) {
				if (main.playerlg.get(p.getName()).getCamp() != null)
					if (main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) || main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
						p.getScoreboard().getTeam("LG").addEntry(this.player.getName());
			}
			if (!main.isDisplayState(DisplayState.LECTURE_DES_ROLES) && !main.isDisplayState(DisplayState.DISTRIBUTION_DES_ROLES)) {
				main.sendRoleMessage(main.getPrefix() + main.SendArrow + "§c§l" + player.getName() + " §ca rejoint le camp des Loups !", Roles.LOUP_GAROU);

				player.getScoreboard().getTeam("LG").addEntry(player.getName());
				for (Player p : main.players) {
					PlayerLG plg = main.playerlg.get(p.getName());

					if (plg.getRole().getCamp().equals(RCamp.LOUP_GAROU) || plg.getRole().getCamp().equals(RCamp.LOUP_GAROU_BLANC))
						player.getScoreboard().getTeam("LG").addEntry(p.getName());
				}
			}
		}

		
	    this.camp = camp;
	 }
	
	public boolean isCamp(RCamp camp) {
	    return this.camp.equals(camp);
	  }
	
	public RCamp getCamp() {
		return this.camp;
	}
	
	
	public void setVote(Player vote) {
	    this.vote = vote;
	  }
	  
	public Player getVotedPlayer() {
	    return this.vote;
	}
	
	
	public void addVote() {
	    this.votes++;
		this.displayArmorStand();
	  }
	
	public void removeVote() {
		this.votes--;
		this.displayArmorStand();
	}
	
	public void resetVotes() {
		this.votes = 0;
		this.resetArmorStand();
	}
	
	public Boolean canVote() {
		return canVote;
	}

	public void setCanVote(Boolean canVote) {
		this.canVote = canVote;
	}
	
	public int getAnkouVotes() {
		return this.AnkouVotes;
	}
	
	public void removeAnkouVote() {
		this.AnkouVotes--;
	}
	
	public void addAnkouVote() {
		this.AnkouVotes++;
	}
	
	public void resetAnkouVotes() {
		this.AnkouVotes = 2;
	}

	public Boolean isInCoupDEtat() {
		return isInCoupDEtat;
	}

	public void setInCoupDEtat(Boolean isInCoupDEtat) {
		this.isInCoupDEtat = isInCoupDEtat;
	}

	public void setCELInfected() {
		if (this.daysBeforeDieOfCEL == -1) this.daysBeforeDieOfCEL = 2;
	}
	
	public void removeDayCELInfected() {
		if (this.daysBeforeDieOfCEL != -1) this.daysBeforeDieOfCEL--;
	}
	
	public void resetCELInfected() {
		this.daysBeforeDieOfCEL = -1;
	}
	
	public int getDayBeforeDieOfCEL() {
		return this.daysBeforeDieOfCEL;
	}
	

	public void setDACDying() {
		if (this.daysBeforeDACDie == -1) this.daysBeforeDACDie = 2;
	}
	
	public void removeDayDACDying() {
		if (this.daysBeforeDACDie != -1) this.daysBeforeDACDie--;
	}
	
	public void resetDACDying() {
		this.daysBeforeDACDie = -1;
	}
	
	public int getDayBeforeDieOfDAC() {
		return this.daysBeforeDACDie;
	}
	
	public int getVotes() {
		return this.votes;
	}
	
	
	public ArmorStand getArmorStand() {
		return ArmorStand;
	}

	private void setArmorStand(ArmorStand armorStand) {
		this.ArmorStand = armorStand;
	}

	public void setRole(Roles role) {
		
		if (role.equals(Roles.SORCIÈRE)) {
			main.SosoKillPots.put(player, true);
			main.SosoRézPots.put(player, true);
		}
		
		if (role.equals(Roles.VILLAGEOIS_VILLAGEOIS)) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§aLe " + role.getDisplayName() + " §ade la partie est " + player.getDisplayName());
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam("RVillageois").addEntry(player.getName());
			player.setDisplayName(player.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + player.getScoreboard().getEntryTeam(player.getName()).getSuffix());
			player.setPlayerListName(player.getDisplayName());
		}
		
		if (role.equals(Roles.PRÉSIDENT)) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§aLe " + role.getDisplayName() + " §ade la partie est " + player.getDisplayName());
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam("RPrésident").addEntry(player.getName());
			player.setDisplayName(player.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + player.getScoreboard().getEntryTeam(player.getName()).getSuffix());
			player.setPlayerListName(player.getDisplayName());
		}
		
		if (role.equals(Roles.SOEUR)) main.setSoeurScoreboard(player);
		if (role.equals(Roles.FRÈRE)) main.setFrèreScoreboard(player);
		if (role.equals(Roles.CUPIDON)) main.setCoupleScoreboard(player);
		
		if (role.equals(Roles.SERVANTE_DÉVOUÉE)) this.setServante(true);
		
		if (role.equals(Roles.COMÉDIEN)) {
			StringBuilder pouvoirs = new StringBuilder();
			for (Roles r : main.pouvoirsComédien) {
				if (pouvoirs.toString().equalsIgnoreCase("")) pouvoirs = new StringBuilder(r.getDisplayName());
				else pouvoirs.append("§f, ").append(r.getDisplayName());
			}
			player.sendMessage(main.getPrefix() + main.SendArrow + "§dVos pouvoirs : " + pouvoirs);
		}
		
		
		this.role = role;
	}
	
	public Roles getRole() {
	    return this.role;
	  }
	
	public Boolean isRole(Roles role) {
	    return this.role.equals(role);
	  }
	
	
	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public void setSalvation(Boolean salvation) {
	    this.isSalvated = salvation;
	  }
	
	public Boolean hasSalvation() {
		return this.isSalvated;
	}
	
	
	 public Boolean hasProtector() {
		return protector != null;
	}

	public void setProtected(Player protector) {
		this.protector = protector;
	}
	
	public Player getProtector() {
		return protector;
	}

	public Boolean isCharmed() {
		return isCharmed;
	}

	public void setCharmed(Boolean isCharmed) {
		this.isCharmed = isCharmed;
	}
	

	public Boolean isHuilé() {
		return isHuilé;
	}

	public void setHuilé(Boolean isHuilé) {
		this.isHuilé = isHuilé;
	}

	public Boolean isInfected() {
		return isInfected;
	}

	public void setInfected(Boolean isInfected) {
		this.isInfected = isInfected;
	}

	public Boolean isVivant() {
		return isVivant;
	}

	public void setVivant(Boolean isVivant) {
		if (!isVivant) this.resetArmorStand();
		this.isVivant = isVivant;
	}
	

	public Boolean isInEqual() {
		return isInEqual;
	}

	public void setInEqual(Boolean isInEqual) {
		this.isInEqual = isInEqual;
	}

	public Boolean isMaire() {
		return isMaire;
	}

	public void setMaire(Boolean isMaire) {
		this.isMaire = isMaire;
	}
	

	public void setVoleur(Boolean voleur) {
		  this.isVoleur = voleur;
	 }
	 
	 public Boolean isVoleur() {
		 return this.isVoleur;
	 }
	 

	 public void setComédien(Boolean comédien) {
		  this.isComédien = comédien;
	 }
	 
	 public Boolean isComédien() {
		 return this.isComédien;
	 }
	 
	 
	 public Boolean isServante() {
		return isServante;
	}

	public void setServante(Boolean isServante) {
		this.isServante = isServante;
	}

	public Player getLastInterdit() {
		return lastInterdit;
	}

	public void setLastInterdit(Player lastInterdit) {
		this.lastInterdit = lastInterdit;
	}

	public Player getOtherPlayerHouse() {
		return otherPlayerHouse;
	}

	public void setOtherPlayerHouse(Player otherPlayerHouse) {
		this.otherPlayerHouse = otherPlayerHouse;
	}

	public Player getPretreThrower() {
		return pretreThrower;
	}

	public void setPretreThrower(Player pretreThrower) {
		this.pretreThrower = pretreThrower;
	}

	public void addCouple(Player c) {
		main.setCoupleScoreboard(c);
		 this.couple.add(c);
	 }
	 
	 public void clearCouple() {
		    this.couple.clear();
	 }
	 
	 public void removeCouple(Player player) {
		    this.couple.remove(player);
	 }
	 
	 public List<Player> getCouple() {
		 return this.couple;
	 }
	 
	 
	 public void addmaitre(Player maitre) {
		    this.maitreof.add(maitre);
	 }
		  
	 public void removemaitre(Player maitre) {
		    this.maitreof.remove(maitre);
	 }
		  
	 public List<Player> getmaitre() {
		    return this.maitreof;
	 }
	 
	 
	 public void addsoeur(Player soeur) {
		    this.soeurOf.add(soeur);
	 }
		  
	 public void removesoeur(Player soeur) {
		    this.soeurOf.remove(soeur);
	 }
		  
	 public List<Player> getsoeur() {
		    return this.soeurOf;
	 }
	 
	 
	 public void addfrère(Player frère) {
		    this.frèreOf.add(frère);
	 }
		  
	 public void removefrère(Player frère) {
		    this.frèreOf.remove(frère);
	 }
		  
	 public List<Player> getfrère() {
		    return this.frèreOf;
	 }
	 
	 

	public RCamp getChoosenCampCL() {
		return choosenCampCL;
	}

	public void setChoosenCampCL(RCamp choosenCampCL) {
		this.choosenCampCL = choosenCampCL;
	}

	public Boolean hasUsedPower() {
		return hasUsedPower;
	}

	public void setHasUsedPower(Boolean hasUsedPower) {
		this.hasUsedPower = hasUsedPower;
	}

	public Boolean hasUsedDefinitivePower() {
		return hasUsedDefinitivePower;
	}

	public void setUsedDefinitivePower(Boolean hasUsedPower) {
		this.hasUsedDefinitivePower = hasUsedPower;
	}
	

	public Boolean isLGTargeted() {
		return isLGTargeted;
	}

	public void setLGTargeted(Boolean isLGTargeted) {
		this.isLGTargeted = isLGTargeted;
	}

	public Boolean isGMLTargeted() {
		return isGMLTargeted;
	}

	public void setGMLTargeted(Boolean isGMLTargeted) {
		this.isGMLTargeted = isGMLTargeted;
	}

	public Boolean isLGBTargeted() {
		return isLGBTargeted;
	}

	public void setLGBTargeted(Boolean isLGBTargeted) {
		this.isLGBTargeted = isLGBTargeted;
	}

	public Boolean isSosoTargeted() {
		return isSosoTargeted;
	}

	public void setSosoTargeted(Boolean isSosoTargeted) {
		this.isSosoTargeted = isSosoTargeted;
	}

	public Boolean isNécroTargeted() {
		return isNécroTargeted;
	}

	public void setNécroTargeted(Boolean isNécroTargeted) {
		this.isNécroTargeted = isNécroTargeted;
	}

	public Boolean isDayTargeted() {
		return isDayTargeted;
	}

	public void setDayTargeted(Boolean isDayTargeted) {
		this.isDayTargeted = isDayTargeted;
	}

	public Boolean isPacifTargeted() {
		return isPacifTargeted;
	}

	public void setPacifTargeted(Boolean isPacifTargeted) {
		this.isPacifTargeted = isPacifTargeted;
	}

	public Boolean isMamieTargeted() {
		return isMamieTargeted;
	}

	public void setMamieTargeted(Boolean isMamieTargeted) {
		this.isMamieTargeted = isMamieTargeted;
	}

	public Boolean isCorbeauTargeted() {
		return isCorbeauTargeted;
	}

	public void setCorbeauTargeted(Boolean isCorbeauTargeted) {
		this.isCorbeauTargeted = isCorbeauTargeted;
	}

	public Boolean isNoctaTargeted() {
		return isNoctaTargeted;
	}

	public void setNoctaTargeted(Boolean isNoctaTargeted) {
		this.isNoctaTargeted = isNoctaTargeted;
	}

	public Boolean isPyroTargeted() {
		return isPyroTargeted;
	}

	public void setPyroTargeted(Boolean isPyroTargeted) {
		this.isPyroTargeted = isPyroTargeted;
	}

	public Boolean isPF2Died() {
		return isPF2Died;
	}

	public void setPF2Died(Boolean isPF2Died) {
		this.isPF2Died = isPF2Died;
	}

	public Boolean isFDJDied() {
		return isFDJDied;
	}

	public void setFDJDied(Boolean isFDJDied) {
		this.isFDJDied = isFDJDied;
	}

	public List<Player> getTargetOf() {
		return targetOf;
	}
	
	
	public List<Player> getJumeauOf() {
		return jumeauOf;
	}

	public Player getPlayerOnCursor(List<Player> list) {
	    Location loc = this.player.getLocation();
	    if (loc.getPitch() > 60F && list.contains(this.player)) {
	      return this.player;
	    } 
	    for (int i = 0; i < 50; i++) {
	      loc.add(loc.getDirection());
	      for (Player player : list) {
	    	  PlayerLG playerlg = main.playerlg.get(player.getName());
	        if (player != this.player && playerlg.isVivant() && this.player.canSee(player) && distanceSquaredXZ(loc, player.getLocation()) < 0.35D && Math.abs(loc.getY() - player.getLocation().getY()) < 2.0D)
	          return player; 
	      }
	    }
	    return null;
	  }
	
	public static double distanceSquaredXZ(Location to, Location from) {
		return Math.pow(from.getX() - to.getX(), 2.0D) + Math.pow(from.getZ() - to.getZ(), 2.0D);
	}
	
	
	public List<Player> get2NearestPlayers() {
		List<Player> nearby = new ArrayList<>();
		
		if (main.isType(Gtype.LIBRE)) {
			List<String> splayers = new ArrayList<>();
			int i = 0;
			boolean isMo = false;
			for (Player player : main.players) splayers.add(player.getName());
			
			java.util.Collections.sort(splayers);
			while (!isMo) {
				if (splayers.get(i).equals(player.getName()))
					isMo = true;
				
				i++;
			}
			i--;
			
			int i2 = i - 1;
			if (i <= 0) i2 = splayers.size() - 1;
			int i3 = i + 1;
			if (i3 >= splayers.size()) i3 = 0;
			nearby.add(Bukkit.getPlayer(splayers.get(i3)));
			nearby.add(Bukkit.getPlayer(splayers.get(i2)));
			} else if (main.isType(Gtype.RÉUNION)) {
				int i = main.UsedBlockList.get(block);
				
				int i2 = i - 1;
				int i3 = i + 1;
				List<Integer> listi = new ArrayList<>(main.UsedBlockList.values());
				listi.sort(Collections.reverseOrder());
				
				if (i2 == 0)
					i2 = listi.get(0);
				if (i3 == 13)
					i3 = listi.get(listi.size() - 1);
				if (!listi.contains(i2))
					if (i == listi.get(listi.size() - 1))
						i2 = listi.get(0);
					else {
						i2 = i;
						for (Integer j : listi) {
							if (i2<i) continue;
							if (j<i) i2 = j;
						}
					}
				if (!listi.contains(i3))
					if (i == listi.get(0))
						i3 = listi.get(listi.size() - 1);
					else {
						List<Integer> listj = new ArrayList<>(listi);
						Collections.sort(listj);
						i3 = i;
						for (Integer j : listj) {
						    if (i<i3) continue;
						    if (i<j) i3 = j;
						}
					}
				
				for (Player p : main.players) {
					if (nearby.size() == 0)
						if (main.UsedBlockList.get(main.playerlg.get(p.getName()).getBlock()) == i2)
							nearby.add(p);
					else if (nearby.size() == 1)
						if (main.UsedBlockList.get(main.playerlg.get(p.getName()).getBlock()) == i3)
							nearby.add(p);
				}

				
			} else throw new NullPointerException("Le type de jeu n'est pas défini");
		
		return nearby;
	}
	
	
	private void displayArmorStand() {
		if (!main.isType(Gtype.RÉUNION)) return;
		String s = "";
		if (this.getVotes() != 1) s = "s";
		
		if (this.getVotes() == 0) {
			resetArmorStand();
			return;
		}
		
		if (this.getArmorStand() != null) {
			if (main.isDisplayState(DisplayState.VOTE) || main.isDisplayState(DisplayState.VOTE2)) this.getArmorStand().setCustomName("§6§l" + this.getVotes() + " §evote" + s);
			if (main.isDisplayState(DisplayState.ELECTIONS_DU_MAIRE)) this.getArmorStand().setCustomName("§3§l" + this.getVotes() + " §bvote" + s);
			if (main.isDisplayState(DisplayState.NUIT_LG)) this.getArmorStand().setCustomName("§4§l" + this.getVotes() + " §cvote" + s);
			return;
		}
		
		Entity e = player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARMOR_STAND);
		ArmorStand as = (ArmorStand) e;
		
		as.setCustomNameVisible(true);
		if (main.isDisplayState(DisplayState.VOTE) || main.isDisplayState(DisplayState.VOTE2)) as.setCustomName("§6§l" + this.getVotes() + " §evote" + s);
		if (main.isDisplayState(DisplayState.ELECTIONS_DU_MAIRE)) as.setCustomName("§3§l" + this.getVotes() + " §bvote" + s);
		if (main.isDisplayState(DisplayState.NUIT_LG)) as.setCustomName("§4§l" + this.getVotes() + " §cvote" + s);
		as.setGravity(false);
		as.setSmall(true);
		as.setVisible(false);
		
		if (isOnlyVisibleForLG) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(as.getEntityId());
			for (Player p : main.players)
				if (!main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) && !main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
	        		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
		
		this.setArmorStand(as);
	}
	
	private void resetArmorStand() {
		if (this.getArmorStand() != null) {
			this.getArmorStand().remove();
			this.setArmorStand(null);
		}
	}
	
	public void showArmorStandOnlyForLG() {
		this.isOnlyVisibleForLG = true;
		
		if (this.getArmorStand() == null) return;
		ArmorStand as = this.getArmorStand();
		
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(as.getEntityId());
		for (Player p : main.players)
			if (!main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU) && !main.playerlg.get(p.getName()).isCamp(RCamp.LOUP_GAROU_BLANC))
        		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void showArmorStandForAll() {
		this.isOnlyVisibleForLG = false;
		
		if (this.getArmorStand() == null) return;
		this.getArmorStand().remove();
		
		this.displayArmorStand();
	}

}
