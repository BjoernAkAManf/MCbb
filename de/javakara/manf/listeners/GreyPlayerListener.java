/**************************************************************************
 * This file is part of MCbb.                                              
 * MCbb is free software: you can redistribute it and/or modify            
 * it under the terms of the GNU General Public License as published by    
 * the Free Software Foundation, either version 3 of the License, or       
 * (at your option) any later version.                                     
 * MCbb is distributed in the hope that it will be useful,                 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           
 * GNU General Public License for more details.                            
 * You should have received a copy of the GNU General Public License       
 * along with MCbb.  If not, see <http://www.gnu.org/licenses/>.           
 *************************************************************************/

package de.javakara.manf.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.Listener;

import de.javakara.manf.mcbb.MCbb;
import de.javakara.manf.mcbb.State;
import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;

public class GreyPlayerListener implements Listener{
	public MCbb plugin;

	public GreyPlayerListener(MCbb instance){
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event){
			if (plugin.ac == State.On) {
				Software player;
				System.out.println(plugin.getConfig().getString("mysql.forumtype"));
				player = ForumSoftware.getSoftwareObject(plugin.getConfig().getString("mysql.forumtype"), event.getPlayer().getName(), plugin.getConfig());
				if(player != null){
					if(player.getRegistrationValue(false)){
						System.out.println((MCbb.lang.get("System.info.auth")).replaceAll("<player>", event.getPlayer().getName()));
						plugin.grey.remove(event.getPlayer());
					}
				}
				else {
					plugin.grey.add(event.getPlayer());
				}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(plugin.getConfig().getString("general.greylist.protection.lootItems").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcbb.vip.override")))
				event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(plugin.getConfig().getString("general.greylist.protection.dropItems").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcbb.vip.override"))){
				event.getPlayer().sendMessage(MCbb.lang.get("System.info.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		if(plugin.getConfig().getString("general.greylist.protection.chat").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcbb.vip.override"))){
				event.getPlayer().sendMessage(MCbb.lang.get("System.info.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(plugin.getConfig().getString("general.greylist.protection.interact").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On&& !(event.getPlayer().hasPermission("mcbb.vip.override"))) {
				event.getPlayer().sendMessage(MCbb.lang.get("System.info.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(plugin.getConfig().getString("general.greylist.protection.command").equals("true"))
			if (plugin.grey.contains(event.getPlayer()) && plugin.ac == State.On && !(event.getPlayer().hasPermission("mcbb.vip.override"))) {
				event.getPlayer().sendMessage(MCbb.lang.get("System.info.registerFirst"));
				event.setCancelled(true);
			}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event){
		if(plugin.getConfig().getString("general.greylist.protection.damageEntities").equals("true"))
			if(plugin.ac == State.On) {
				if(!(plugin.getConfig().getBoolean("general.greylist.protection.damageEntities")))
					if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
						EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) event;
						Player p;
						if (ed.getDamager() instanceof Player) {
							p = (Player) ed.getDamager();
							if(plugin.grey.contains(p) && !(p.hasPermission("mcbb.vip.override"))){
								p.sendMessage(MCbb.lang.get("System.info.registerFirst"));
								event.setCancelled(true);
							}
						}
					}
			}
	}
}