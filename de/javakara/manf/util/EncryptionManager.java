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

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * Provides all Encryption Methods used to verify Passwords All Encryption
 * Methods for phpbb are done by lars
 * http://larsho.blogspot.de/2008/02/passwords-in-phpbb-3.html
 * 
 * @author manf
 */
public class EncryptionManager {
	private static String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	/**
	 * Addapted from md5 method
	 * @param input
	 * @return
	 */
	public static String sha1(String input) {
		try {
			byte[] bytes = input.getBytes("ISO-8859-1");
			MessageDigest md5er = MessageDigest.getInstance("SHA1");
			byte[] hash = md5er.digest(bytes);
			return bytes2hex(hash);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Addapted from md5 method
	 * @param input
	 * @return
	 */
	public static String sha256(String input) {
		try {
			byte[] bytes = input.getBytes("ISO-8859-1");
			MessageDigest md5er = MessageDigest.getInstance("SHA-256");
			byte[] hash = md5er.digest(bytes);
			return bytes2hex(hash);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * @author lars
	 * @param data
	 * @return
	 */
	public static String md5(String data) {
		try {
			byte[] bytes = data.getBytes("ISO-8859-1");
			MessageDigest md5er = MessageDigest.getInstance("MD5");
			byte[] hash = md5er.digest(bytes);
			return bytes2hex(hash);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @author lars
	 * @param password
	 * @param setting
	 * @return
	 */
	public static String _hash_crypt_private(String password, String setting) {
		String output = "*";

		// Check for correct hash
		if (!setting.substring(0, 3).equals("$H$"))
			return output;

		int count_log2 = itoa64.indexOf(setting.charAt(3));
		if (count_log2 < 7 || count_log2 > 30)
			return output;

		int count = 1 << count_log2;
		String salt = setting.substring(4, 12);
		if (salt.length() != 8)
			return output;

		String m1 = md5(salt + password);
		String hash = pack(m1);
		do {
			hash = pack(md5(hash + password));
		} while (--count > 0);

		output = setting.substring(0, 12);
		output += _hash_encode64(hash, 16);

		return output;
	}

	/**
	 * @author lars
	 * @param bytes
	 * @return
	 */
	static String bytes2hex(byte[] bytes) {
		StringBuffer r = new StringBuffer(32);
		for (int i = 0; i < bytes.length; i++) {
			String x = Integer.toHexString(bytes[i] & 0xff);
			if (x.length() < 2)
				r.append("0");
			r.append(x);
		}
		return r.toString();
	}

	/**
	 * @author lars Encode Hash
	 */
	static String _hash_encode64(String input, int count) {
		String output = "";
		int i = 0;

		do {
			int value = input.charAt(i++);
			output += itoa64.charAt(value & 0x3f);

			if (i < count)
				value |= input.charAt(i) << 8;

			output += itoa64.charAt((value >> 6) & 0x3f);

			if (i++ >= count)
				break;

			if (i < count)
				value |= input.charAt(i) << 16;

			output += itoa64.charAt((value >> 12) & 0x3f);

			if (i++ >= count)
				break;

			output += itoa64.charAt((value >> 18) & 0x3f);
		} while (i < count);

		return output;
	}

	/**
	 * @author lars
	 * @param hex
	 * @return
	 */
	static String pack(String hex) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < hex.length(); i += 2) {
			char c1 = hex.charAt(i);
			char c2 = hex.charAt(i + 1);
			char packed = (char) (hexToInt(c1) * 16 + hexToInt(c2));
			buf.append(packed);
		}
		return buf.toString();
	}

	/**
	 * @author lars
	 * @param ch
	 * @return
	 */
	static int hexToInt(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';

		ch = Character.toUpperCase(ch);
		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 0xA;

		throw new IllegalArgumentException("Not a hex character: " + ch);
	}
}