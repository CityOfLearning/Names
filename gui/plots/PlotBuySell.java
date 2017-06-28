package com.dyn.render.gui.plots;

import java.util.ArrayList;
import java.util.List;

import com.dyn.DYNServerConstants;
import com.forgeessentials.client.hud.PlotsRenderer;
import com.forgeessentials.commons.selections.PlotArea;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.list.ScrollableDisplayList;
import com.rabbit.gui.component.list.entries.ListEntry;
import com.rabbit.gui.component.list.entries.StringEntry;
import com.rabbit.gui.show.Show;

import net.minecraft.util.EnumChatFormatting;

public class PlotBuySell extends Show {

	public PlotBuySell() {
		setBackground(new DefaultBackground());
	}

	@Override
	public void setup() {
		List<ListEntry> myplots = new ArrayList();
		myplots.add(new StringEntry("--My Plots--"));
		if (PlotsRenderer.plots.containsKey(1)) {
			for (PlotArea plot : PlotsRenderer.plots.get(1)) {
				myplots.add(new StringEntry(plot.getName()));
			}
		}

		registerComponent(new ScrollableDisplayList(width / 6, (int) (height * .2), width / 3, (int) (height * .33), 15,
				myplots));

		List<ListEntry> saleplots = new ArrayList();
		saleplots.add(new StringEntry("--Plots for Sale--"));
		if (PlotsRenderer.plots.containsKey(0)) {
			for (PlotArea plot : PlotsRenderer.plots.get(0)) {
				saleplots.add(new StringEntry(plot.getName() + " - " + String.valueOf('\u20b7')
						+ EnumChatFormatting.GOLD + (plot.getXLength() * plot.getZLength())));
			}
		}

		registerComponent(new ScrollableDisplayList(width / 6, (int) (height * .57), width / 3, (int) (height * .33),
				15, saleplots));

		registerComponent(new Button((int) (width * .55), (int) (height * .7), (int) (width * .2), 20, "Buy Plot"));

		registerComponent(new Button((int) (width * .55), (int) (height * .8), (int) (width * .2), 20, "Sell Plot"));

		registerComponent(new Picture(width / 8, (int) (height * .15), (int) (width * (6.0 / 8.0)), (int) (height * .8),
				DYNServerConstants.BG1_IMAGE));
	}

}
