package com.dyn.render.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rabbit.gui.utils.TextureHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class SkinManager {
	// key is minecraft username and value is the skin texture
	private static Map<String, UUID> dynPlayerSkin = new HashMap<String, UUID>();

	public static void addSkin(EntityPlayer player, String skin) {
		UUID textureId = UUID.randomUUID();
		// incase its a url the texture helper will sort it all out
		TextureHelper.addTexture(textureId, skin);
		dynPlayerSkin.put(player.getDisplayNameString(), textureId);
	}

	public static void addSkin(String player, ResourceLocation skin) {
		UUID textureId = UUID.randomUUID();
		// incase its a url the texture helper will sort it all out
		TextureHelper.addStaticTexture(textureId, skin);
		dynPlayerSkin.put(player, textureId);
	}

	public static void addSkin(String player, String skin) {
		UUID textureId = UUID.randomUUID();
		// incase its a url the texture helper will sort it all out
		TextureHelper.addTexture(textureId, skin);
		dynPlayerSkin.put(player, textureId);
	}

	public static boolean bindSkinTexture(EntityPlayer player) {
		if (dynPlayerSkin.containsKey(player.getDisplayNameString())) {
			TextureHelper.bindTexture(dynPlayerSkin.get(player.getDisplayNameString()));
			return true;
		}
		return false;
	}

	public static boolean bindSkinTexture(String player) {
		if (dynPlayerSkin.containsKey(player)) {
			TextureHelper.bindTexture(dynPlayerSkin.get(player));
			return true;
		}
		return false;
	}

	public static String getSkinTexture(EntityPlayer player) {
		if (dynPlayerSkin.containsKey(player.getName())) {
			// not sure we can do anything about dynamic textures currently
			if (TextureHelper.getDynamicTexture(dynPlayerSkin.get(player.getName())) == null) {
				return TextureHelper.getStaticTexture(dynPlayerSkin.get(player.getName())).toString();
			}
		}
		return null;
	}

	public static String getSkinTexture(String player) {
		if (dynPlayerSkin.containsKey(player)) {
			// not sure we can do anything about dynamic textures currently
			if (TextureHelper.getDynamicTexture(dynPlayerSkin.get(player)) == null) {
				return TextureHelper.getStaticTexture(dynPlayerSkin.get(player)).toString();
			}
		}
		return null;
	}

	public static boolean hasSkinTexture(EntityPlayer player) {
		return dynPlayerSkin.containsKey(player.getDisplayNameString());
	}

	public static boolean hasSkinTexture(String player) {
		return dynPlayerSkin.containsKey(player);
	}

	public static void removeSkinTexture(EntityPlayer player) {
		dynPlayerSkin.remove(player.getName());
	}

	public static void removeSkinTexture(String player) {
		dynPlayerSkin.remove(player);
	}

	public static void setSkinTexture(EntityPlayer player, String skin) {
		if (dynPlayerSkin.containsKey(player.getDisplayNameString())) {
			UUID textureId = UUID.randomUUID();
			// incase its a url the texture helper will sort it all out
			TextureHelper.addTexture(textureId, skin);
			dynPlayerSkin.replace(player.getDisplayNameString(), textureId);
		} else {
			addSkin(player, skin);
		}
	}

	public static void setSkinTexture(String player, String skin) {
		if (dynPlayerSkin.containsKey(player)) {
			UUID textureId = UUID.randomUUID();
			// incase its a url the texture helper will sort it all out
			TextureHelper.addTexture(textureId, skin);
			dynPlayerSkin.replace(player, textureId);
		} else {
			addSkin(player, skin);
		}
	}
}
