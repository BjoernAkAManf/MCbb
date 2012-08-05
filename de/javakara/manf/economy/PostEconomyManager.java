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
package de.javakara.manf.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import de.javakara.manf.api.Config;
import de.javakara.manf.api.ConfigItems;

public class PostEconomyManager {
	private HashMap<String,Integer> posts = new HashMap<String,Integer>();
	private ArrayList<Reward> rewards = new ArrayList<Reward>();
	private Reward defaultReward;
	
	public PostEconomyManager(Plugin p){
		List<String> l = Config.getStringList(ConfigItems.ECONOMY_REWARD); 
		for(int i = 0;i<l.size();i++){
			String[] args = l.get(i).split("|");
			if(args.length == 3) {
				int id = Integer.valueOf(args[0]);
				RewardType type = Reward.convertStringToType(args[1]);
				Reward r = null;
				switch(type){
					case money:
						double amount = Double.valueOf(args[2]);
						r = new Reward(amount);
						rewards.add(id, r);
					break;
					
					default:
					break;
				}
				defaultReward = r;
			}else{
				System.out.println("Error in loading Economy Rewards: " + l.get(i));
				Bukkit.getPluginManager().disablePlugin(p);
			}
		}
	}
	
	public void save(){
		posts.clear();
	}
	
	public void load(){
		
	}
	
	public Reward getRewardForPost(int i){
		Reward r = rewards.get(i);
		if(r != null){
			return r;
		}
		return defaultReward;
	}
}