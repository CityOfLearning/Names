package com.dyn.names;

import com.dyn.names.proxy.Proxy;
import com.dyn.names.reference.MetaData;
import com.dyn.names.reference.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class NamesUI {
	@Mod.Instance(Reference.MOD_ID)
	public static NamesUI instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;

	@Mod.Metadata(Reference.MOD_ID)
	public ModMetadata metadata;

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		metadata = MetaData.init(metadata);

		proxy.init();
	}
}
