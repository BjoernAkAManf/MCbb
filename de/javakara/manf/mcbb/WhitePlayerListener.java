	package de.javakara.manf.mcbb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;

	public class WhitePlayerListener implements Listener {
		public MCbb plugin;

		public WhitePlayerListener(MCbb instance) {
			plugin = instance;
		}

		 @EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerLogin(PlayerLoginEvent event) {
			if (plugin.ac == State.On) {
				Software d;
				d = ForumSoftware.getSoftwareObject(plugin.getConfig().getString("mysql.forumtype"), event.getPlayer().getName(), plugin.getConfig());
				//d = new ForumUser(event.getPlayer().getName(), plugin.getConfig(),false);
				if(!d.getRegistrationValue(false))
					event.setKickMessage(MCbb.lang.get("System.info.whitelist"));
					event.setResult(Result.KICK_OTHER);
			}
		}
}
