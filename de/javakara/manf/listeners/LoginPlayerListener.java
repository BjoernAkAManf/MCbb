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

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.javakara.manf.mcbb.MCbb;
import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;

public class LoginPlayerListener implements Listener, CommandExecutor {
	/** All Players that are secured */
	private static ArrayList<String> protectedOnlinePlayers = new ArrayList<String>();
	/** All Players that are logged in */
	private static ArrayList<String> loggedInPlayers = new ArrayList<String>();
	/** The ForumSoftware that is used*/
	private static String forumSoftware;
	/** The plugin config*/
	private static FileConfiguration config;
	
	/**
	 * Initialise the 'Login'-System
	 * @param software the software used to 
	 * @param config the plugin config
	 */
	public void initialise(FileConfiguration config){
		LoginPlayerListener.config = config;
		LoginPlayerListener.forumSoftware = config.getString("mysql.forumtype");
	}
	
	/************
	 * Saves the Player from beeing kicked by a 'Cracked Client'
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerProtection(PlayerPreLoginEvent event) {
		if (protectedOnlinePlayers.contains(event.getName())) {
			event.setKickMessage(MCbb.lang.get("System.login.already"));
			event.setResult(Result.KICK_OTHER);
		}
	}

	/*********************************************
	 * Adds the Player to the List if joins the Server
	 *********************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoins(PlayerJoinEvent event) {
		protectedOnlinePlayers.add(event.getPlayer().getName());
	}

	/*********************************************
	 * Removes the Player if he logs out
	 *********************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerLeaves(PlayerQuitEvent event) {
		protectedOnlinePlayers.remove(event.getPlayer().getName());
	}

	/**
	 * Manages the Command for Login/Logout
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		Player p = null;
		p = (Player) sender;
		// If Player is null we can exit
		// Only Player can 'Login'/'Logout'
		if (p != null) {
			// Splits commands into 'Login'/'Logout' section
			if (label.equals("login")) {
				//needs atleast one Arguement(Password)
				if(args.length >= 1){
					// Login Section
					// Password check here
					Software player = ForumSoftware.getSoftwareObject(forumSoftware, sender.getName(),config);
					boolean passwordIsCorrect = player.isPasswordCorrect(args[0]);
					// Password check ended
					// If password was correct add to loggedInPlayers
					// If not kick the Player.
					if (passwordIsCorrect) {
						// Adds Player to ArrayList
						loggedInPlayers.add(sender.getName());
						sender.sendMessage(MCbb.lang.get("System.login.success"));
					} else {
						// Kicks the Player
						p.kickPlayer(MCbb.lang.get("System.login.wrongpw"));
					}
				}
			} else if (label.equals("logout")) {
				// Logout Section
				loggedInPlayers.remove(sender.getName());
			}
			return true;
		} else {
			// Console cannot 'Login'/'Logout'
			System.out.println(MCbb.lang.get("System.command.noconsole"));
			return false;
		}
	}

	/**********************************************************************
	 * This event guarantee that player inventories cannot modified if the
	 * player is not logged in. Here item pickup
	 * 
	 * @param event
	 *********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void itemPickupEvent(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		if (!loggedInPlayers.contains(p.getName())) {
			p.sendMessage(MCbb.lang.get("System.login.pleaselogin"));
			event.setCancelled(true);
		}
	}

	/**********************************************************************
	 * This event guarantee that player inventories cannot modified if the
	 * player is not logged in. Here item drops
	 * 
	 * @param event
	 **********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void dropItemEvent(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if (!loggedInPlayers.contains(p.getName())) {
			p.sendMessage(MCbb.lang.get("System.login.pleaselogin"));
			event.setCancelled(true);
		}
	}

	/**********************************************************************
	 * Player cannot chat, unless they logged in
	 * 
	 * @param event
	 **********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerChatEvent(PlayerChatEvent event) {
		Player p = event.getPlayer();
		if (!loggedInPlayers.contains(p.getName())) {
			p.sendMessage(MCbb.lang.get("System.login.pleaselogin"));
			event.setCancelled(true);
		}
	}

	/**********************************************************************
	 * Player cannot interact, unless they logged in
	 * 
	 * @param event
	 **********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerInteractEvent(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (!loggedInPlayers.contains(p.getName())) {
			p.sendMessage(MCbb.lang.get("System.login.pleaselogin"));
			event.setCancelled(true);
		}
	}

	/**********************************************************************
	 * Player cannot use any command, unless they logged in
	 * 
	 * @param event
	 **********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerTriesCommandEvent(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if (!loggedInPlayers.contains(p.getName())) {
			p.sendMessage(MCbb.lang.get("System.login.pleaselogin"));
			event.setCancelled(true);
		}
	}

	/**********************************************************************
	 * Player cannot hurt others or be hurt by anything, unless they logged in
	 * 
	 * @param event
	 **********************************************************************/
	@EventHandler(priority = EventPriority.NORMAL)
	public void entityDamageEvent(EntityDamageEvent event) {
		if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
			EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) event;
			Player damager = null;
			// Is the Damager a Player
			if (ed.getDamager() instanceof Player) {
				damager = (Player) ed.getDamager();
				// Is the Damaging Player logged in?
				if (loggedInPlayers.contains(damager.getName())) {
					// Cancel damage, because player is not logged in
					damager.sendMessage(MCbb.lang
							.get("System.login.pleaselogin"));
					event.setCancelled(true);
					return;
				}
			}
			Player victim = null;
			if (ed.getEntity() instanceof Player) {
				victim = (Player) ed.getEntity();
				if (loggedInPlayers.contains(victim.getName())) {
					if (damager != null) {
						// Notifies damager if victim is logggin in
						damager.sendMessage(MCbb.lang
								.get("System.login.onloginprocess"));
					}
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}