package fr.neyuux.lgthierce.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.neyuux.lgthierce.Gcycle;
import fr.neyuux.lgthierce.Gstate;
import fr.neyuux.lgthierce.Index;

public class LGAutoStop extends BukkitRunnable {
	
	private int timer = 30;
	private Index main;
	
	public LGAutoStop(Index main) {
		this.main = main;
	}

	@Override
	public void run() {

		if (main.players.size() + main.spectators.size() != Bukkit.getOnlinePlayers().size()) {
			main.setState(Gstate.PREPARING);
			this.timer = 30;
			cancel();
			return;
		}
		
		if (!main.isState(Gstate.FINISH)) {
			this.timer = 30;
			cancel();
			return;
		}
		
		
		if (timer == 30) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
			main.setCycle(Gcycle.NONE);
		}
		
		if (timer == 15) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
		}
		
		if (timer == 10) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
		}
		
		if (timer <= 5 && timer > 1) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLa map va se reset dans §c§l" + timer + " §r§csecondes §e!");
		}
		
		if (timer == 1) {
			Bukkit.broadcastMessage(main.getPrefix() + main.SendArrow + "§eLa map va se reset dans §c§l" + timer + " §r§cseconde §e!");	
		}
		
		if (timer == 0) {
			main.rel();
			cancel();
		}
		
		for (Player player : Bukkit.getOnlinePlayers()) player.setLevel(timer);
		timer--;
	}

}
