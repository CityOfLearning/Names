package com.dyn.render.proxy;

import java.util.Collections;
import java.util.Map;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;

import net.minecraft.entity.EntityLivingBase;

public class Server implements Proxy {

	@Override
	public Map<String, ?> getKeyBindings() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public void init() {

	}

	@Override
	public boolean isDialogInterfaceOpen() {
		return false;
	}

	@Override
	public void openEditDialogInterface(DialogBlockTileEntity block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleDialogHud(EntityLivingBase entity, boolean state, String text, int duration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		// TODO Auto-generated method stub

	}

}