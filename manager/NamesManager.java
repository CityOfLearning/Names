package com.dyn.names.manager;

import java.util.HashMap;
import java.util.Map;

public class NamesManager {
	// key is minecraft username and value is dyn hash code
		private static Map<String, Integer> Minecraft2HashKey = new HashMap<String, Integer>();
		// key is dyn hash code and value is dyn username
		private static Map<Integer, String> HashKey2DYNUser = new HashMap<Integer, String>();
		// key is dyn hash code and value is minecraft username
		private static Map<Integer, String> HashKey2MinecraftUsername = new HashMap<Integer, String>();
		
		
		// generates a hash code by combining the hash code of the dyn username and
		// the hash code of the minecraft username.  Hash code is used a key to look up dyn username
		// and minecraft username
		private static int generateHashKey(String dyn_user, String mc_username) {
			if ((dyn_user == null || dyn_user.isEmpty()) || 
				(mc_username == null || mc_username.isEmpty())) { 
					throw new IllegalArgumentException(); 
			}
			return dyn_user.hashCode() + mc_username.hashCode();
		}
		
		public static void addUsername(String mc_name, String dyn_name) throws IllegalArgumentException {
			
			// minecraft username is used as key to get dyn hash code 
			// dyn hash code is used as key to get dyn username from DYNHashCode2DYNUser map
			//it is ok to generate the key this way as the mc_name is always unique
			int hashkey = generateHashKey(dyn_name, mc_name);
			Minecraft2HashKey.put(mc_name, hashkey);
			HashKey2DYNUser.put(hashkey, dyn_name);
			// this should provide minecraft username
			HashKey2MinecraftUsername.put(hashkey, mc_name);
		}

		public static String getDYNUsername(String mc_name) throws NullPointerException{
			// can return null so its unsafe to assume non null
			return HashKey2DYNUser.get(Minecraft2HashKey.get(mc_name));
		}

		public static String getMCUsername(int dyn_hashkey) {
			// can return null so its unsafe to assume non null
			return HashKey2MinecraftUsername.get(dyn_hashkey);
		}
		
		public static int getHashKey(String mc_name){
			return Minecraft2HashKey.get(mc_name);
		}

		public static void init() {

		}
}
