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

package de.javakara.manf.mcbb;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.javakara.manf.api.ForumSoftware;
import de.javakara.manf.api.Software;
import de.javakara.manf.api.User;
import de.javakara.manf.listeners.LoginPlayerListener;

public class MCbbCommands implements CommandExecutor {

	private MCbb plugin;

	public MCbbCommands(MCbb plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if(label.equalsIgnoreCase("mcbb")){
			if (args.length == 0)
				return sendHelp(sender);
			if (args.length == 1) {
				if (args[0].equals("on"))
					if (sender.hasPermission("mcbb.maintainer.on")) {
						return plugin.setOn();
					} else
						return permError(sender);
				if (args[0].equals("off"))
					if (sender.hasPermission("mcbb.maintainer.off")) {
						return plugin.setOff();
					} else
						return permError(sender);
				if (args[0].equals("status"))
					if (sender.hasPermission("mcbb.maintainer.status")) {
						return plugin.status(sender);
					} else
						return permError(sender);
			}
			if (plugin.ac == State.On) {
				if (args.length == 2) {
					if (sender.hasPermission("mcbb.user.lookup")) {
						if (args[0].equals("lookup")) {
							Software x = ForumSoftware.getSoftwareObject();
							User user = ForumSoftware.getUser(args[1]);
							//x = new ForumUser(, plugin.getConfig());
							// sender.sendMessage("Player Account Status: ");
							if (x.getRegistrationValue(user)) {
								sender.sendMessage("Active!");
								return true;
							}
							sender.sendMessage("Inactive!");
							return true;
						}
					} else
						return permError(sender);
				}
				sender.sendMessage("Unknown Command");
			}
			return sendHelp(sender);
		}else{
			return LoginPlayerListener.loginCommand((Player) sender, label, args);
		}	
	}
	

	public boolean sendHelp(CommandSender sender) {
		if (plugin.ac == State.On) {
			sender.sendMessage("This could be a Help!");
			sender.sendMessage("mcbb on -> Enables Plugin");
			sender.sendMessage("mcbb off -> Disable Plugin");
			sender.sendMessage("mcbb status -> Displays Status");
			sender.sendMessage("mcbb lookup [PLAYERNAME] -> Displays whether player has Forum Account or not");
			return true;
		}
		return false;
	}

	public boolean permError(CommandSender sender) {
		sender.sendMessage(MCbb.lang.get("System.error.noPerm"));
		return true;
	}
}