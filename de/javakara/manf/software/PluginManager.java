package de.javakara.manf.software;

import java.io.File;

import de.javakara.manf.util.pluginloader.JarClassLoader;

public final class PluginManager {
	private static Software software;

	public static void load(String pluginFolder, String softwareName) {
		JarClassLoader jarLoader = new JarClassLoader(pluginFolder
				+ File.separator + softwareName + ".jar");
		Class<?> c = null;
		try {
			c = jarLoader.loadClass("Software", true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Object o = null;
		try {
			o = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if (o instanceof Software) {
			software = (Software) o;
			System.out.println(software.getName() + "found and used");
		}
	}
}