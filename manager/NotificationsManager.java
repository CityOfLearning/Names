package com.dyn.render.manager;

import java.util.ArrayList;
import java.util.List;

import com.dyn.render.render.AchievementPlusNotification;
import com.dyn.render.render.INotification;
import com.dyn.render.render.RequirementNotification;

import net.minecraft.client.Minecraft;

public class NotificationsManager {
	private final List<INotification> notificationList = new ArrayList<INotification>();

	public NotificationsManager() {

	}

	public void addAchievementNotification(String title, String subTitle) {
		notificationList.add(new AchievementPlusNotification(Minecraft.getMinecraft(), title, subTitle));
	}

	public void addRequirementNotification(String title, String subTitle) {
		notificationList.add(new RequirementNotification(Minecraft.getMinecraft(), title, subTitle));
	}

	public void removeNotification(INotification notification) {
		notificationList.remove(notification);
	}

	public void renderNotifications() {
		for (int i = 0; i < notificationList.size(); i++) {
			notificationList.get(i).drawNotification(i);
		}
	}
}
