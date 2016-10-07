package com.dyn.render.proxy;

import java.util.Map;

import com.dyn.robot.entity.EntityRobot;
import com.dyn.robot.gui.RobotProgrammingInterface;

public interface Proxy {

	public Map<String, ?> getKeyBindings();

	public void init();

	public void renderGUI();

	void toggleRenderProgramInterface(boolean state);

}