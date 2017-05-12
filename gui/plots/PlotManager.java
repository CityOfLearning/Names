package com.dyn.render.gui.plots;

import com.dyn.DYNServerConstants;
import com.dyn.DYNServerMod;
import com.google.common.collect.Lists;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.CheckBoxButton;
import com.rabbit.gui.component.control.DropDown;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.list.ScrollableDisplayList;
import com.rabbit.gui.show.Show;

public class PlotManager extends Show {

	public PlotManager() {
		setBackground(new DefaultBackground());
	}

	@Override
	public void setup() {
		registerComponent(new DropDown(width / 6, height / 5, width / 4, 15, "Plot Users Type",
				new String[] { "Guests", "Member", "Moderator" }));

		registerComponent(
				new ScrollableDisplayList(width / 6, (int) (height * .3), width / 3, 110, 15, Lists.newArrayList()));

		registerComponent(new CheckBoxButton((int) (width * .55), (int) (height * .22), (int) (width / 3.3), 20,
				"   Should Exclude", false));
		registerComponent(new CheckBoxButton((int) (width * .55), (int) (height * .32), (int) (width / 3.3), 20,
				"   Can Break Blocks", false));
		registerComponent(new CheckBoxButton((int) (width * .55), (int) (height * .42), (int) (width / 3.3), 20,
				"   Can Place Blocks", false));
		registerComponent(new CheckBoxButton((int) (width * .55), (int) (height * .52), (int) (width / 3.3), 20,
				"   Can Use items", false));
		registerComponent(new CheckBoxButton((int) (width * .55), (int) (height * .62), (int) (width / 3.3), 20,
				"   Can Interact", false));

		registerComponent(new Button(width / 6, (int) (height * .8), (int) (width * .2), 20, "Remove player"));

		registerComponent(new Button((int) (width * .4), (int) (height * .8), (int) (width * .2), 20, "Add Player"));

		registerComponent(new DropDown((int) (width * .62), (int) (height * .815), (int) (width * .24), 15, "Players",
				DYNServerMod.usernames));

		registerComponent(new Picture(width / 8, (int) (height * .15), (int) (width * (6.0 / 8.0)), (int) (height * .8),
				DYNServerConstants.BG1_IMAGE));
	}
}
