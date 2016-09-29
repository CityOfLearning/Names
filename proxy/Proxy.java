package com.dyn.render.proxy;

import java.util.Map;

//import com.dyn.render.gui.turtle.ProgrammingInterface;
//import com.dyn.robot.entity.EntityRobot;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void init();

	public void renderGUI();

	public void toggleRenderProgramInterface(boolean state);
//	
//	public ProgrammingInterface getProgrammingInterface();
//	
//	public void createNewProgrammingInterface(EntityRobot robot);
//	
//	public void openRobotInterface();
}