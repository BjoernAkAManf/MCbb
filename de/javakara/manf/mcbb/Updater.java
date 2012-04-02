package de.javakara.manf.mcbb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Updater {
	public static void download(MCbb f){
		//Not Implemented
	}
	
	public static boolean newUpdate(MCbb v,boolean o) {
		StringBuilder x = new StringBuilder();
		try {
			URL yahooURL = new URL(MCbb.lang.get("System.update.location"));
			Scanner in = new Scanner(yahooURL.openStream());
			if (in.hasNextLine()) {
				String line = in.nextLine();
				x.append(line);
			}
			in.close();
			if(o){
				System.out.println(MCbb.lang.get("System.update.version"));
				System.out.println(MCbb.lang.get("System.update.currversion") + v.Version.toString());
				System.out.println(MCbb.lang.get("System.update.latestversion") + x.toString());
			}
		} catch (MalformedURLException me) {
			System.err.println(me);

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		if(v.Version.toString().equals(x.toString()))
			return false;
		else
			return true;
	}
	public static String getVersion() {
		StringBuilder x = new StringBuilder();
		try {
			URL yahooURL = new URL(MCbb.lang.get("System.update.location"));
			Scanner in = new Scanner(yahooURL.openStream());
			if (in.hasNextLine()) {
				String line = in.nextLine();
				x.append(line);
			}
			in.close();
			return x.toString();
		} catch (MalformedURLException me) {
			System.err.println(me);

		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		return null;
	}
}
