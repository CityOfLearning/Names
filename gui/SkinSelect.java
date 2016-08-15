package com.dyn.render.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import com.dyn.render.manager.SkinManager;
import com.dyn.server.database.DBManager;
import com.dyn.server.packets.PacketDispatcher;
import com.dyn.server.packets.server.SyncSkinsServerMessage;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.display.EntityComponent;
import com.rabbit.gui.component.display.Picture;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.component.list.DisplayList;
import com.rabbit.gui.component.list.ScrollableDisplayList;
import com.rabbit.gui.component.list.entries.ListEntry;
import com.rabbit.gui.component.list.entries.SelectStringEntry;
import com.rabbit.gui.component.list.entries.StringEntry;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.entity.EntityCustomNpc;

public class SkinSelect extends Show {

	private String root;
	public AssetsBrowser assets;
	private HashSet<String> dataFolder;
	protected HashSet<String> dataTextures;
	private ScrollableDisplayList skinList;
	private SelectStringEntry selectedEntry;
	private EntityCustomNpc entity;

	public SkinSelect() {
		setBackground(new DefaultBackground());
		dataFolder = new HashSet<String>();
		dataTextures = new HashSet<String>();
		root = AssetsBrowser.getRoot("dyn:textures/skins/");
		assets = new AssetsBrowser(root, new String[] { "png" });
		title = "Skin Select Gui";
		// for now we use the npc interface but preferably we can make a lighter
		// version
		entity = new EntityCustomNpc(Minecraft.getMinecraft().theWorld);
		String texture = SkinManager.getSkinTexture(Minecraft.getMinecraft().thePlayer);
		if (texture != null) {
			entity.display.setSkinTexture(texture);
		} else {
			entity.display.setSkinPlayer(Minecraft.getMinecraft().thePlayer.getDisplayNameString());
		}
	}

	private void entryClicked(SelectStringEntry entry, DisplayList list, int mouseX, int mouseY) {
		selectedEntry = entry;
		if (dataTextures.contains(entry.getTitle()) && (entry.getTitle() != null)) {
			entity.display.setSkinTexture(assets.getAsset(entry.getTitle()));
			entity.textureLocation = null;
		}
	}

	@Override
	public void onClose() {
		entity.delete();
	}

	@Override
	public void setup() {
		super.setup();

		dataFolder.clear();
		Vector<String> list = new Vector<String>();
		for (String folder : assets.folders) {
			list.add("/" + folder);
			dataFolder.add("/" + folder);
		}
		for (String texture : assets.files) {
			list.add(texture);
			dataTextures.add(texture);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

		registerComponent(
				new TextLabel(width / 3, (int) (height * .1), width / 3, 20, "Select A Skin", TextAlignment.CENTER));

		// the side buttons

		// components
		ArrayList<ListEntry> skinAssets = new ArrayList<ListEntry>();

		skinAssets.add(new StringEntry("--Available Skins--"));
		for (String s : list) {
			skinAssets.add(new SelectStringEntry(s, (SelectStringEntry entry, DisplayList dlist, int mouseX,
					int mouseY) -> entryClicked(entry, dlist, mouseX, mouseY)));
		}

		skinList = new ScrollableDisplayList((int) (width * .175), (int) (height * .2), width / 3, 140, 15, skinAssets);
		skinList.setId("skins");
		registerComponent(skinList);

		registerComponent(
				new EntityComponent((int) (width * .7), (int) (height * .85), width / 3, 100, entity, 0, 2.75f));

		registerComponent(new Button((int) (width * .175), (int) (height * .8), width / 3, 20, "Set this as my skin")
				.setClickListener(btn -> {
					// this is where we send a notificaiton to the server to
					// render the chosen skin
					if ((selectedEntry != null) && !selectedEntry.getTitle().isEmpty()) {
						SkinManager.setSkinTexture(Minecraft.getMinecraft().thePlayer,
								assets.getAsset(selectedEntry.getTitle()));
						PacketDispatcher
								.sendToServer(new SyncSkinsServerMessage(Minecraft.getMinecraft().thePlayer.getName(),
										assets.getAsset(selectedEntry.getTitle())));
						DBManager.setPlayerSkin(Minecraft.getMinecraft().thePlayer.getDisplayNameString(),
								assets.getAsset(selectedEntry.getTitle()));
					}
				}));

		// The background
		registerComponent(new Picture(width / 8, (int) (height * .05), (int) (width * (6.0 / 8.0)), (int) (height * .9),
				new ResourceLocation("dyn", "textures/gui/background3.png")));
	}
}
