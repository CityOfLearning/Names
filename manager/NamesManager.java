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
	// key is minecraft username and value is dyn hash code
	private static Map<String, Integer> Minecraft2DYNUsername = new HashMap<String, Integer>();
	// key is dyn hash code and value is dyn username
	private static Map<Integer, String> DYNHashCode2DYNUser = new HashMap<Integer, String>();
	// key is dyn hash code and value is minecraft username
	private static Map<Integer, String> DYN2MinecraftUsername = new HashMap<Integer, String>();
	
	
	// generates a hash code by combining the hash code of the dyn username and
	// the hash code of the dyn password.  Hash code is used a key to look up dyn username
	// and minecraft username
	public static Integer getDYNHashKey(String dyn_user, String dyn_pass) {
		if ((dyn_user == null || dyn_user.isEmpty()) || 
			(dyn_pass == null || dyn_pass.isEmpty())) { 
				throw new IllegalArgumentException(); 
		}
		return dyn_user.hashCode() + dyn_pass.hashCode();
	}
	
	public static void addUsername(String mc_name, String dyn_name, Integer dyn_hashkey) {
		
		// minecraft username is used as key to get dyn hash code 
		// dyn hash code is used as key to get dyn username from DYNHashCode2DYNUser map
		Minecraft2DYNUsername.put(mc_name, dyn_hashkey);
		DYNHashCode2DYNUser.put(dyn_hashkey, dyn_name);
		// this should provide minecraft username
		DYN2MinecraftUsername.put(dyn_hashkey, mc_name);
	}

	/***  dyn username not used as key anymore
	* public static boolean containsDynName(String dyn_name) {
	*	 return DYN2MinecraftUsername.containsKey(dyn_name);
	* }
	***/

	public static String getDYNUsername(String mc_name) {
		// can return null so its unsafe to assume non null
		if(mc_name == null || mc_name.isEmpty()) throw new NullPointerException();
		Integer dyn_hashkey = Minecraft2DYNUsername.get(mc_name);
		return DYNHashCode2DYNUser.get(dyn_hashkey);
	}

	public static String getMCUsername(Integer dyn_hashkey) {
		// can return null so its unsafe to assume non null
		if(dyn_hashkey == null || dyn_hashkey.equals(0)) throw new NullPointerException();
		return DYN2MinecraftUsername.get(dyn_hashkey);
	}

	public static void init() {
		String lines = "";

		try {
			URL url = new URL("https://dl.dropboxusercontent.com/u/33377940/MinecraftAccounts.csv");

			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((lines = br.readLine()) != null) {

				// use comma as separator
				String[] line = lines.split(",");

				if ((line.length > 4) && 
					(line[0].trim() != null) && !line[0].trim().isEmpty() &&
					(line[1].trim() != null) && !line[1].trim().isEmpty())   {
						Integer dyn_hashkey = getDYNHashKey(line[0].trim(), line[1].trim());
						addUsername(line[4].trim(), line[0].trim(), dyn_hashkey);
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

						if ((line.length > 4) && 
							(line[0].trim() != null) && !line[0].trim().isEmpty() &&
							(line[1].trim() != null) && !line[1].trim().isEmpty())   {
								Integer dyn_hashkey = getDYNHashKey(line[0].trim(), line[1].trim());
								addUsername(line[4].trim(), line[0].trim(), dyn_hashkey);
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
