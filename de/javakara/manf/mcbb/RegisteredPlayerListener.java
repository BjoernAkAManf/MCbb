package de.javakara.manf.mcbb;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;

	public class RegisteredPlayerListener implements Listener {
		public MCbb plugin;
		
		public RegisteredPlayerListener(MCbb instance) {
			plugin = instance;
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerJoin(PlayerJoinEvent event) {
			Software u;
			u = ForumSoftware.getSoftwareObject(plugin.getConfig().getString("mysql.forumtype"), event.getPlayer().getName(), plugin.getConfig());
			if(u.getRegistrationValue(true)){
				if(plugin.ac == State.On)
					if(MCbb.permission != null)
						System.out.println("Starting SQL-GroupSync");
						if(plugin.getConfig().getString("general.syncGroups").equals("true"))
							if(!(MCbb.permission.playerInGroup((String)null, event.getPlayer().getName(), u.getForumGroup()))){
								p(event.getPlayer(),u.getForumGroup());
								event.getPlayer().sendMessage("[MCbb] Your Group was set to: " +  u.getForumGroup());
							}
					if(MCbb.economy != null)
						MCbb.economy.depositPlayer(event.getPlayer().getName(), (u.getNewPosts() * plugin.getConfig().getInt("economy.moneyPerPost")));
				info(event.getPlayer());
			}
			else
				plugin.grey.add(event.getPlayer());
			if(!(event.getPlayer().hasPermission("mcbb.user.join") || event.getPlayer().hasPermission("mcbb.vip.override"))){
				event.getPlayer().kickPlayer(MCbb.lang.get("System.error.noPerm"));
			}
		}
		public  void info(Player p){
			if (p.hasPermission("mcbb.maintainer.update"))
				if(plugin.Version.isOutdated())
					p.sendMessage(MCbb.lang.get("System.info.newUpdate"));
		}
		public void p(Player p,String o)
		{
			MCbb.permission.playerAddGroup((String)null,p.getName(),o);
			for(String s : MCbb.permission.getGroups())
			{
				if(!o.equals(s))
					if(MCbb.permission.playerInGroup((String)null,p.getName(), s)){
						System.out.println("|" + s + "|");
						MCbb.permission.playerRemoveGroup((String)null,p.getName(), s);
					}
					
			}
		}
}
