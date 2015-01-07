package ui;

import java.awt.Point;
import java.util.Random;

import database.Database;
import locationaware.AlgorithmManager;

public class RequestGenerator {

	public int noOfRequests = 10;

	private static Random rn = new Random();

	public void generateAndProcessRequests() {
		for (int i = 0; i < noOfRequests; i++) {

			int userid = Database.getInstance().getRandomUserid();
			if (userid > 0) {
				int noOfvms = randomNoGenrator(1, 20);
				int noOfIDCs = randomNoGenrator(1, Math.min(5, noOfvms));

				int x = randomNoGenrator(0, 100);
				int y = randomNoGenrator(0, 100);

				Point p = Database.getInstance()
						.getRandomUseridLocation(userid);

				System.out.print("Request made by User" + userid + ":");
				System.out.println("\tVMs:" + noOfvms + ",IDCs:" + noOfIDCs);
				AlgorithmManager.runResourceAllocation(userid, noOfvms,
						noOfIDCs, p);
			} else
				System.out.println("User are not yet present");
		}
	}

	int randomNoGenrator(int minimum, int maximum) {
		int range = maximum - minimum + 1;
		int randomNum = rn.nextInt(range) + minimum;
		return randomNum;
	}
}
