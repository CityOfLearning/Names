package com.dyn.render.proxy;

import java.util.Collections;
import java.util.Map;

public class Server implements Proxy {

	@Override
	public Map<String, ?> getKeyBindings() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public void init() {

	}

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		// TODO Auto-generated method stub
		
	}

}