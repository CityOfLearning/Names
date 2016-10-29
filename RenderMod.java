package com.dyn.render;

import java.util.HashMap;
import java.util.Map;

import com.dyn.DYNServerMod;
import com.dyn.admin.AdminUI;
import com.dyn.mentor.MentorUI;
import com.dyn.render.proxy.Proxy;
import com.dyn.render.reference.MetaData;
import com.dyn.render.reference.Reference;
import com.dyn.student.StudentUI;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:dyn|server")
public class RenderMod {
	@Mod.Instance(Reference.MOD_ID)
	public static RenderMod instance;

	@SidedProxy(modId = Reference.MOD_ID, clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;

	public <T> Map<String, T> getKeyBindings() {
		Map<String, T> keys = new HashMap();
		Map<String, T> tempMap = (Map<String, T>) proxy.getKeyBindings();
		for (String key : tempMap.keySet()) {
			keys.put(key, tempMap.get(key));
		}
		switch (DYNServerMod.accessLevel) {
		case ADMIN:
			tempMap = (Map<String, T>) AdminUI.proxy.getKeyBindings();
			for (String key : tempMap.keySet()) {
				keys.put(key, tempMap.get(key));
			}
			break;
		case MENTOR:
			tempMap = (Map<String, T>) MentorUI.proxy.getKeyBindings();
			for (String key : tempMap.keySet()) {
				keys.put(key, tempMap.get(key));
			}
			break;
		case STUDENT:
			tempMap = (Map<String, T>) StudentUI.proxy.getKeyBindings();
			for (String key : tempMap.keySet()) {
				keys.put(key, tempMap.get(key));
			}
			break;
		default:
			break;
		}

		return keys;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MetaData.init(event.getModMetadata());

		proxy.init();
	}
}
