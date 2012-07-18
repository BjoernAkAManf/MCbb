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

import de.javakara.manf.software.ForumSoftware;
import de.javakara.manf.software.Software;

public class MCbbCommands implements CommandExecutor {

	private MCbb plugin;

	public MCbbCommands(MCbb plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
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
						Software x;
						x = ForumSoftware.getSoftwareObject(plugin.getConfig().getString("mysql.forumtype"),args[1], plugin.getConfig());
						//x = new ForumUser(args[1], plugin.getConfig());
						// sender.sendMessage("Player Account Status: ");
						if (x.getRegistrationValue(true)) {
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