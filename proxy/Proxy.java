package com.dyn.render.proxy;

import java.util.Map;

import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void init();

	public boolean isDialogInterfaceOpen();

	public void openEditDialogInterface(DialogBlockTileEntity block);

	public void toggleDialogHud(boolean state, String text, int duration);

	public void toggleRenderProgramInterface(boolean state);

}