package com.dyn.render.proxy;

import java.util.Map;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void init();

	public boolean isDialogInterfaceOpen();

	public void openEditDialogInterface(DialogBlockTileEntity block);

	public void toggleDialogHud(EntityLivingBase entity, boolean state, String text, int duration);

	public void toggleRenderProgramInterface(boolean state);

}