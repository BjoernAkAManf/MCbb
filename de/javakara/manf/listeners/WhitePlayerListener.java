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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.javakara.manf.mcbb.MCbb;
import de.javakara.manf.mcbb.State;
import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;
import de.javakara.manf.software.User;

	public class WhitePlayerListener implements Listener {
		public MCbb plugin;

		public WhitePlayerListener(MCbb instance) {
			plugin = instance;
		}

		 @EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerLogin(PlayerLoginEvent event) {
			if (plugin.ac == State.On) {
				User user = ForumSoftware.getUser(event.getPlayer().getName());
				Software d = ForumSoftware.getSoftwareObject();
				if(!d.getRegistrationValue(user)){
					event.setKickMessage(MCbb.lang.get("System.info.whitelist"));
					event.setResult(Result.KICK_OTHER);
				}
			}
		}
}
