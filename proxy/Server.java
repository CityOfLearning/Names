package com.dyn.render.proxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dyn.fixins.blocks.decision.DecisionBlockTileEntity;
import com.dyn.fixins.blocks.dialog.DialogBlockTileEntity;
import com.dyn.fixins.blocks.redstone.proximity.ProximityBlockTileEntity;
import com.dyn.fixins.blocks.redstone.timer.TimerBlockTileEntity;
import com.dyn.server.database.DBManager;
import com.dyn.server.network.NetworkManager;
import com.dyn.server.network.packets.client.SyncSkinsMessage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class Server implements Proxy {

	private Map<String, String> textureName = new HashMap();

	@Override
	public Map<String, ?> getKeyBindings() {
		return Collections.EMPTY_MAP;
	}

	@Override
	public void handleErrorMessage(String error, String code, int line) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean isDialogInterfaceOpen() {
		return false;
	}

	@SubscribeEvent
	public void loginEvent(PlayerEvent.PlayerLoggedInEvent event) {
		Runnable task = () -> {
			// this blocks and so we gotta thread it
			String texture = DBManager.getPlayerSkin(event.player.getName()).trim();
			if ((texture != null) && !texture.isEmpty()) {
				textureName.put(event.player.getName(), texture);
				NetworkManager.sendToAll(new SyncSkinsMessage(event.player.getName(), texture));
			}
		};
		new Thread(task).start();

		for (String user : MinecraftServer.getServer().getAllUsernames()) {
			if (textureName.containsKey(user)) {
				NetworkManager.sendTo(new SyncSkinsMessage(user, textureName.get(user)), (EntityPlayerMP) event.player);
			}
		}
	}

	@Override
	public void openDecisionGui(EntityLivingBase entity, DecisionBlockTileEntity decisionBlockTileEntity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openEditDecisionBlock(DecisionBlockTileEntity decisionBlock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openEditDialogInterface(DialogBlockTileEntity block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openSetProximityInterface(ProximityBlockTileEntity block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openSetTimerInterface(TimerBlockTileEntity block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPlayerSkinTextureName(EntityPlayer player, String texture) {
		if (textureName.containsKey(player.getName())) {
			textureName.replace(player.getName(), texture);
		} else {
			textureName.put(player.getName(), texture);
		}
	}

	@Override
	public void toggleDialogHud(EntityLivingBase entity, boolean state, String text, int duration, boolean interupt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleRenderProgramInterface(boolean state) {
		// TODO Auto-generated method stub

	}

}