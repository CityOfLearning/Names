package com.dyn.render.manager;

import java.util.ArrayList;
import java.util.List;

import com.dyn.render.hud.notifications.AchievementPlusNotification;
import com.dyn.render.hud.notifications.INotification;
import com.dyn.render.hud.notifications.RequirementNotification;

import net.minecraft.client.Minecraft;

public class NotificationsManager {
	private static final List<INotification> notificationList = new ArrayList<INotification>();

	public static void addAchievementNotification(String title, String subTitle) {
		notificationList.add(new AchievementPlusNotification(Minecraft.getMinecraft(), title, subTitle));
	}

	public static void addRequirementNotification(String title, String subTitle) {
		notificationList.add(new RequirementNotification(Minecraft.getMinecraft(), title, subTitle));
	}

	public static void removeNotification(INotification notification) {
		notificationList.remove(notification);
	}

	public static void renderNotifications() {
		for (int i = 0; i < notificationList.size(); i++) {
			notificationList.get(i).drawNotification(i);
		}
	}
}
