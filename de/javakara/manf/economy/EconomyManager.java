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

import net.milkbowl.vault.economy.Economy;

public class EconomyManager {
	private static Economy economy;
	
	public static void initialise(Economy economy){
		EconomyManager.economy = economy;
	}
	
	public static boolean isInitialised(){
		return (economy != null);
	}
	public static void modifyPlayer(String playerName,double amount){
		if(amount == 0.0){
			return;
		}
		
		if(amount > 0.0){
			economy.depositPlayer(playerName, amount);
		}else{
			economy.withdrawPlayer(playerName, amount);
		}
	}

}
