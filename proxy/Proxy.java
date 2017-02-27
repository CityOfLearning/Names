package com.dyn.render.proxy;

import java.util.Map;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;
import com.dyn.fixins.blocks.redstone.proximity.ProximityBlockTileEntity;
import com.dyn.fixins.blocks.redstone.timer.TimerBlockTileEntity;

import net.minecraft.entity.EntityLivingBase;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void handleErrorMessage(String error, String code, int line);

	public void init();

	public boolean isDialogInterfaceOpen();

	public void openEditDialogInterface(DialogBlockTileEntity block);

	public void openSetProximityInterface(ProximityBlockTileEntity block);

	public void openSetTimerInterface(TimerBlockTileEntity block);

	public void toggleDialogHud(EntityLivingBase entity, boolean state, String text, int duration, boolean interupt);

	public void toggleRenderProgramInterface(boolean state);
}