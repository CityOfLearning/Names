package com.dyn.render.hud;

import com.dyn.DYNServerMod;

public interface IHud {

	public static void draw() {
		DYNServerMod.logger.info("Subclass did not override super method Draw of interfrace Hud");
	}

	public static void drawScaledTexturedRect(int x, int y, int z, int width, int height) {
		DYNServerMod.logger.info("Subclass did not override super method drawScaledTexturedRect of interfrace Hud");
	}

	public static void updateWindowScale() {
		DYNServerMod.logger.info("Subclass did not override super method updateWindowScale of interfrace Hud");
	}
}
