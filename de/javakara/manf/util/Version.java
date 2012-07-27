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

package de.javakara.manf.util;

import de.javakara.manf.mcbb.Updater;

public class Version {
	private int major, minor, patch, build;
	public Version(String v) {
		setV(v);
	}

	public void setV(String v) {
		String[] words = v.split("\\.");
		major = Integer.parseInt(words[0]);
		minor = Integer.parseInt(words[1]);
		patch = Integer.parseInt(words[2]);
		build = Integer.parseInt(words[3]);
	}

	public int getMajor() {
		return major;
	}
	public int getMinor() {
		return minor;
	}
	public int getPatch() {
		return patch;
	}
	public int getBuild() {
		return build;
	}
	@Override
	public String toString()
	{
		return major + "." + minor + "." + patch + "." + build ;
	}

	public boolean isOutdated()
	{
		return !(this.toString().equals(Updater.getVersion()));
	}
}
