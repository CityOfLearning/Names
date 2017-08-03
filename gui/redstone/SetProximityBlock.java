package com.dyn.render.gui.redstone;

import java.awt.Color;

import com.dyn.fixins.blocks.redstone.proximity.ProximityBlockTileEntity;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.messages.MessageClientUpdateTileEntity;
import com.forgeessentials.commons.EnumMobType;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.DropDown;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.show.Show;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

public class SetProximityBlock extends Show {

	ProximityBlockTileEntity block;

	private int x1, y1, z1, x2, y2, z2;

	private EnumMobType validMob;

	public SetProximityBlock(ProximityBlockTileEntity block) {
		title = "Edit Dialog Block";
		this.block = block;
		x1 = block.getCorner1().getX();
		y1 = block.getCorner1().getY();
		z1 = block.getCorner1().getZ();
		x2 = block.getCorner2().getX();
		y2 = block.getCorner2().getY();
		z2 = block.getCorner2().getZ();
		validMob = block.getValidMob();
	}

	@Override
	public void setup() {

		registerComponent(
				new TextLabel((int) (width * .28), (int) (height * .3), 120, 20, Color.black, "Set Proximity Area:")
						.setMultilined(false));

		registerComponent(new TextLabel((int) (width * .29), (int) (height * .375), 30, 20, Color.black, "W:"));

		registerComponent(
				new TextBox((int) (width * .32), (int) (height * .35), 45, 20).setText("" + block.getCorner1().getX())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									x1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + x1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .435), (int) (height * .375), 30, 20, Color.black, "Dn:"));

		registerComponent(
				new TextBox((int) (width * .475), (int) (height * .35), 45, 20).setText("" + block.getCorner1().getY())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									y1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + y1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .59), (int) (height * .375), 30, 20, Color.black, "N:"));

		registerComponent(
				new TextBox((int) (width * .62), (int) (height * .35), 45, 20).setText("" + block.getCorner1().getZ())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									z1 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + z1);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .29), (int) (height * .475), 30, 20, Color.black, "E:"));

		registerComponent(
				new TextBox((int) (width * .32), (int) (height * .45), 45, 20).setText("" + block.getCorner2().getX())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									x2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + x2);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .435), (int) (height * .475), 30, 20, Color.black, "Up:"));

		registerComponent(
				new TextBox((int) (width * .475), (int) (height * .45), 45, 20).setText("" + block.getCorner2().getY())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									y2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + y2);
								}
							}
						}));

		registerComponent(new TextLabel((int) (width * .59), (int) (height * .475), 30, 20, Color.black, "S:"));

		registerComponent(
				new TextBox((int) (width * .62), (int) (height * .45), 45, 20).setText("" + block.getCorner2().getZ())
						.setTextChangedListener((TextBox textbox, String previousText) -> {
							if (!previousText.isEmpty()) {
								try {
									z2 = Integer.parseInt(previousText);
								} catch (Exception e) {
									textbox.setText("" + z2);
								}
							}
						}));

		registerComponent(
				new TextLabel((int) (width * .225), (int) (height * .55), 120, 60, Color.black, "Entitiy Trigger Type")
						.setMultilined(false));

		registerComponent(new DropDown<EnumMobType>((int) (width * .25), (int) (height * .6), 80, 20, "Entity Types")
				.addAll(EnumMobType.values())
				.setItemSelectedListener((DropDown<EnumMobType> dropdown, String selected) -> {
					validMob = dropdown.getElement(selected).getValue();
				}));

		registerComponent(new Button((int) (width * .45), (int) (height * .6), 120, 20, "Update Proximity Block")
				.setClickListener(btn -> {
					block.setCorners(new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
					block.setValidMob(validMob);
					NBTTagCompound tag = new NBTTagCompound();
					block.writeToNBT(tag);
					NetworkManager.sendToServer(new MessageClientUpdateTileEntity(block.getPos(), tag));
					getStage().close();
				}));

		// The background
		registerComponent(new Picture((int) (width * .2), (int) (height * .25), (int) (width * .6), (int) (height * .5),
				new ResourceLocation("dyn", "textures/gui/background.png")));
	}
}
