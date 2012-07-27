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

import org.bukkit.entity.Player;

public class Reward {
	private RewardType type;
	private double amount;
	
	public Reward(double amount){
		this.type = RewardType.money;
		this.amount = amount;
	}
	
	public void earn(Player p){
		switch(type){
		case money:
			EconomyManager.modifyPlayer(p.getName(), amount);
			this.type = RewardType.none;
			amount = 0;
			return;
		case item:
			System.out.println("[MCbb] NotSupportedRewardException: Item is not Implemented for Rewards!");
			return;
		case rank:
			System.out.println("[MCbb] NotSupportedRewardException: Rank is not Implemented for Rewards!");
			return;
		case none:
			return;
		}
	}

	public static RewardType convertStringToType(String s) {
		if(s.equalsIgnoreCase("m")){
			return RewardType.money;
		}
		if(s.equalsIgnoreCase("i")){
			return RewardType.item;
		}
		if(s.equalsIgnoreCase("r")){
			return RewardType.rank;
		}
		return RewardType.none;
	}
}

enum RewardType{
	money,item,rank,none
}
