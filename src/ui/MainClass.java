package ui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import locationaware.IDC;
import database.Database;
import database.User;

public class MainClass {

	private static final int NoOfClouds = 5;
	private static final int NoOfUsers = 10;

	public static void main(String[] args) {

		Database instance = Database.getInstance();
		instance.doCleanup();

		List<User> userList = createListOfUsers();
		instance.doUserInitialization(userList);

		List<IDC> idcList = createListOfClouds();
		instance.doCloudsIntilization(idcList);

		RequestGenerator rg = new RequestGenerator();
		rg.generateAndProcessRequests();

		instance.getAssignedVMs();

	}

	private static List<User> createListOfUsers() {
		ArrayList<User> userList = new ArrayList<User>();
		Random randomGenerator = new Random();

		for (int i = 1; i <= NoOfUsers; i++) {
			User user = new User();
			user.setUser_id(i);
			user.setName("USER:" + i);
			user.setEmailid("user." + i + "@sjsu.edu");
			user.setPhone("999-999-99" + i);

			int x = randomGenerator.nextInt(100);
			int y = randomGenerator.nextInt(100);
			user.setLocation(x + "," + y);
			userList.add(user);
		}
		return userList;
	}

	private static List<IDC> createListOfClouds() {
		ArrayList<IDC> idcList = new ArrayList<IDC>();
		Random randomGenerator = new Random();

		for (int i = 1; i <= NoOfClouds; i++) {
			IDC idc = new IDC();
			idc.setId(i);
			idc.setAvailableVMs((randomGenerator.nextInt(10) + 1) * 10);
			idc.setRam(randomGenerator.nextInt(10) + 1);
			idc.setStorage(randomGenerator.nextInt(1000) + 1);
			int x = randomGenerator.nextInt(100);
			int y = randomGenerator.nextInt(100);
			idc.setLocation(new Point(x, y));

			idc.setDiameter(randomGenerator.nextInt(1000) + 1);
			idcList.add(idc);
		}
		return idcList;
	}
}
