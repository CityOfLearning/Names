package com.dyn.render.proxy;

import java.util.Map;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void init();

	public void renderGUI();

	void toggleRenderProgramInterface(boolean state);

}