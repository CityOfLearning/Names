package com.dyn.names.manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NamesManager {
	private static Map<String, String> Minecraft2DYNUsername = new HashMap<String, String>();
	private static Map<String, String> DYN2MinecraftUsername = new HashMap<String, String>();

	public static void addUsername(String mc_name, String dyn_name) {

		// System.out.println("Syncing Minecraft name " + mc_name + " with Dyn
		// Name " + dyn_name);
		// this one should be unique so we can overwrite the previous value
		Minecraft2DYNUsername.put(mc_name, dyn_name);
		// this one is trickier since we can have duplicates
		/*
		 * if (DYN2MinecraftUsername.containsKey(dyn_name)) {
		 * 
		 * we need some way of validating whether or not a new user is a
		 * returning user say that a user logs on twice with different minecraft
		 * usernames but the same dyn name in the same session
		 * 
		 * if (DYN2MinecraftUsername.get(dyn_name).equals(mc_name)) { // name is
		 * the same we should probably do nothing but a put is // harmless here
		 * DYN2MinecraftUsername.put(dyn_name, mc_name); } else { // how do we
		 * figure out who is what here } } else {
		 */
		// alright this is how its goona be
		DYN2MinecraftUsername.put(dyn_name, mc_name);
		// }
	}

	public static boolean containsDynName(String dyn_name) {
		return DYN2MinecraftUsername.containsKey(dyn_name);
	}

	public static String getDYNUsername(String mc_name) {
		// can return null so its unsafe to assume non null
		return Minecraft2DYNUsername.get(mc_name);
	}

	public static String getMCUsername(String dyn_name) {
		// can return null so its unsafe to assume non null
		return DYN2MinecraftUsername.get(dyn_name);
	}

	public static void init() {
		String lines = "";

		try {
			URL url = new URL("https://dl.dropboxusercontent.com/u/33377940/MinecraftAccounts.csv");

			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((lines = br.readLine()) != null) {

				// use comma as separator
				String[] line = lines.split(",");

				if ((line.length > 4) && (line[0].trim() != null) && !line[0].trim().isEmpty()) {
					addUsername(line[4].trim(), line[0].trim());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					URL url = new URL("https://dl.dropboxusercontent.com/u/33377940/MinecraftAccounts.csv");

					BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
					String lines = "";
					while ((lines = br.readLine()) != null) {

						// use comma as separator
						String[] line = lines.split(",");

						if ((line.length > 4) && (line[0].trim() != null) && !line[0].trim().isEmpty()) {
							NamesManager.addUsername(line[4].trim(), line[0].trim());
						}
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 30 * 1000, 10 * 60 * 1000); // lets try to update for 30 seconds
										// every 10 minutes
	}
}
