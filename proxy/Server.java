package com.dyn.render.proxy;

import java.util.Collections;
import java.util.Map;

//import com.dyn.render.gui.turtle.ProgrammingInterface;
//import com.dyn.robot.entity.EntityRobot;

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
//
//	@Override
//	public ProgrammingInterface getProgrammingInterface() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void createNewProgrammingInterface(EntityRobot robot) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void openRobotInterface() {
//		// TODO Auto-generated method stub
//		
//	}

}