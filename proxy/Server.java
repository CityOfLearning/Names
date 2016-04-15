package com.dyn.names.proxy;

import com.dyn.names.manager.NamesManager;

public class Server implements Proxy {

	@Override
	public void init() {
		NamesManager.init();
	}

	@Override
	public void renderGUI() {
		// TODO Auto-generated method stub

	}

}