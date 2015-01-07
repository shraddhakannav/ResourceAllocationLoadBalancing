package locationaware;

import java.awt.Point;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import database.DBQueries;
import database.Database;

public class LocationAware {

	public LocationAware() {
		// TODO Auto-generated constructor stub
	}

	public List<IDC> getAvailableIDCswithDiameter(List<IDC> allIDCs,
			int requiredVMs, int noOfIDCs) {

		int vmsPerIDC = requiredVMs / noOfIDCs;
		ArrayList<IDC> availableIDCs = new ArrayList<IDC>();
		for (IDC idc : allIDCs) {
			if (idc.getAvailableVMs() > vmsPerIDC)
				availableIDCs.add(idc);
		}

		Collections.sort(availableIDCs, new Comparator<IDC>() {

			@Override
			public int compare(IDC idc1, IDC idc2) {
				if (idc1.getDiameter() >= idc2.getDiameter())
					return 1;
				else
					return -1;
			}
		});

		return availableIDCs;
	}

	public List<IDC> getCenterOfGraph(Point userLocation,
			List<IDC> availableIDCs) {
		for (IDC idc : availableIDCs) {
			double diameter = idc.getDiameter();
			Point location = idc.getLocation();

			double distance = getDistance(location, userLocation) + diameter;
			idc.setDistance(distance);
		}

		Collections.sort(availableIDCs, new Comparator<IDC>() {

			@Override
			public int compare(IDC idc1, IDC idc2) {
				if (idc1.getDistance() >= idc2.getDistance())
					return 1;
				else
					return -1;
			}
		});
		return availableIDCs;
	}

	public List<IDC> assignVMsPerIDC(List<IDC> sortedIDCs, int requiredVMs,
			int noOfIDCs) {

		ArrayList<IDC> finalizedIDCs = new ArrayList<IDC>();
		int remainingVMs = requiredVMs;
		int vmsPerIDC = requiredVMs / noOfIDCs;
		for (IDC idc : sortedIDCs) {
			int vms = idc.getAvailableVMs();
			if (vms >= vmsPerIDC) {
				vms -= vmsPerIDC;
				remainingVMs -= vmsPerIDC;
			} else if (vms >= remainingVMs) {
				vms -= remainingVMs;
				remainingVMs -= remainingVMs;
			}
			idc.setAvailableVMs(vms);
			finalizedIDCs.add(idc);
		}
		if (remainingVMs <= 0)
			return finalizedIDCs;
		else
			return null;
	}

	private double getDistance(Point location, Point userLocation) {
		// TODO Auto-generated method stub
		double value = 0.0;
		value += Math.abs(location.x - userLocation.x)
				+ Math.abs(location.y - userLocation.y);
		return value;
	}
}
