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
