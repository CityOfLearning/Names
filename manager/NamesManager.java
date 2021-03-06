package com.dyn.render.manager;

import java.util.HashMap;
import java.util.Map;

public class NamesManager {
	// key is minecraft username and value is dyn hash code
	private static Map<String, Integer> Minecraft2HashKey = new HashMap<>();
	// key is dyn hash code and value is dyn username
	private static Map<Integer, String> HashKey2DYNUser = new HashMap<>();
	// key is dyn hash code and value is minecraft username
	private static Map<Integer, String> HashKey2MinecraftUsername = new HashMap<>();

	public static void addUsername(String mc_name, String dyn_name) {

		// minecraft username is used as key to get dyn hash code
		// dyn hash code is used as key to get dyn username from
		// DYNHashCode2DYNUser map
		// it is ok to generate the key this way as the mc_name is always unique
		int hashkey = generateHashKey(dyn_name, mc_name);
		Minecraft2HashKey.put(mc_name, hashkey);
		HashKey2DYNUser.put(hashkey, dyn_name);
		// this should provide minecraft username
		HashKey2MinecraftUsername.put(hashkey, mc_name);
	}

	// generates a hash code by combining the hash code of the dyn username and
	// the hash code of the minecraft username. Hash code is used a key to look
	// up dyn username
	// and minecraft username
	private static int generateHashKey(String dyn_user, String mc_username) {
		return dyn_user.hashCode() + mc_username.hashCode();
	}

	public static String getDYNUsername(String mc_name) throws NullPointerException {
		// can return null so its unsafe to assume non null
		return HashKey2DYNUser.get(Minecraft2HashKey.get(mc_name));
	}

	public static int getHashKey(String mc_name) {
		return Minecraft2HashKey.get(mc_name);
	}

	public static String getMCUsername(int dyn_hashkey) {
		// can return null so its unsafe to assume non null
		return HashKey2MinecraftUsername.get(dyn_hashkey);
	}

	public static void setUsername(String mc_name, String dyn_name) {
		if (Minecraft2HashKey.containsKey(mc_name)) {
			int hashkey = Minecraft2HashKey.get(mc_name);
			// remove the old entries
			HashKey2DYNUser.remove(hashkey);
			HashKey2MinecraftUsername.remove(hashkey);

			hashkey = generateHashKey(dyn_name, mc_name);
			// replace the old key
			Minecraft2HashKey.replace(mc_name, hashkey);
			// add the new key
			HashKey2DYNUser.put(hashkey, dyn_name);
			HashKey2MinecraftUsername.put(hashkey, mc_name);
		} else {
			addUsername(mc_name, dyn_name);
		}

	}
}
