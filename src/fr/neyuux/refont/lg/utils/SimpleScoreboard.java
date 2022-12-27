package fr.neyuux.refont.lg.utils;

import com.google.common.base.Splitter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class SimpleScoreboard {
    private final Scoreboard scoreboard;
    private final Objective obj;

    private final Map<Integer, NameData> scoreMap = new HashMap<>();
    private final Map<String, Integer> nameMap = new HashMap<>();

    public SimpleScoreboard(String title, Player player) {
    	if (title.equals("§c§lLoups§e§l-§6§lGarous")) {
	    	Scoreboard news = Bukkit.getScoreboardManager().getNewScoreboard();
	    	for (Team t : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
	    		news.registerNewTeam(t.getName());
	    		news.getTeam(t.getName()).setDisplayName(t.getDisplayName());
	    		news.getTeam(t.getName()).setPrefix(t.getPrefix());
	    		news.getTeam(t.getName()).setSuffix(t.getSuffix());
	    		for (String s : t.getEntries())
	    			news.getTeam(t.getName()).addEntry(s);
	    	}
	    	player.setScoreboard(news);
    	}
        this.scoreboard = player.getScoreboard();
        if (scoreboard.getObjective((title.length() > 16 ? title.substring(0, 15) : title)) != null)
        	scoreboard.getObjective((title.length() > 16 ? title.substring(0, 15) : title)).unregister();
        obj = scoreboard.registerNewObjective((title.length() > 16 ? title.substring(0, 15) : title), "dummy");
        obj.setDisplayName(title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public void add(String text, Integer score) {
        //System.out.println("Adding: "+text+", "+score);
        if(!scoreMap.containsKey(score)) {
            String fix = fixDuplicates(text);
            nameMap.put(fix, score);
            NameData nameData = new NameData(fix);
            //System.out.println("Adding NameData - "+nameData.prefix+","+nameData.name+","+nameData.suffix+"/"+nameData.idx);
            scoreMap.put(score, nameData);

            Team t = scoreboard.registerNewTeam("teams-"+nameData.idx);

            if(nameData.prefix!=null)
                t.setPrefix(nameData.prefix);
                if(nameData.suffix!=null)
                    t.setSuffix(nameData.suffix);
            t.addEntry(nameData.name);

            obj.getScore(nameData.name).setScore(score);
        }
    }

    public void blankLine(int score) {
        this.add(ChatColor.RESET.toString(), score);
    }

    public void remove(Integer score) {
        if(scoreMap.containsKey(score)) {
            NameData nameData = scoreMap.get(score);
            scoreboard.getTeam("teams-"+nameData.idx).unregister();
            scoreboard.resetScores(nameData.name);
            scoreMap.remove(score);
            String text = nameData.name;
            if(nameData.prefix!=null){
                text = nameData.prefix+text;
                if(nameData.suffix!=null) {
                    text+=nameData.suffix;
                }
            }
            nameMap.remove(text);
        }
    }

    public void update(Integer score, String text) {
        remove(score);
        add(text, score);
    }

    public void destroy() {
        List<Team> teams = new ArrayList<>(scoreboard.getTeams());
        for(Team team:teams) {
            team.unregister();
        }
        List<String> players = new ArrayList<>(scoreboard.getEntries());
        for(String player:players) {
            scoreboard.resetScores(player);
        }
        obj.unregister();
        scoreMap.clear();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private String fixDuplicates(String text) {
        while (nameMap.containsKey(text))
            text += "§r";
        if (text.length() > 48)
            text = text.substring(0, 47);
        return text;
    }

    static int netIdx=0;
    class NameData {
        String prefix;
        String name;
        String suffix;
        int idx;

        public NameData(String text) {
            name = text;
            if (text.length() > 16) {
                Iterator<String> iterator = Splitter.fixedLength(16).split(text).iterator();
                prefix = iterator.next();
                name = iterator.next();
                if(text.length() > 32) {
                    suffix = iterator.next();
                }
            }
            idx = netIdx++;
        }
    }
}