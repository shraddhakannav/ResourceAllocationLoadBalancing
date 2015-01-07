package locationaware;

import java.awt.Point;
import java.util.List;

import database.Database;

public class AlgorithmManager {

	public static void runResourceAllocation(int userid, int noOfVms,
			int noOfIDC, Point userlocation) {

		Database instance = Database.getInstance();
		LocationAware locationAware = new LocationAware();

		List<IDC> allIDCs = instance.getAllIDCs();

		List<IDC> availableIDCs = locationAware.getAvailableIDCswithDiameter(
				allIDCs, noOfVms, noOfIDC);

		List<IDC> sortedIDCs = locationAware.getCenterOfGraph(userlocation,
				availableIDCs);

		List<IDC> success = locationAware.assignVMsPerIDC(sortedIDCs, noOfVms,
				noOfIDC);

		if (success != null) {
			instance.updateDataCenters(success);
			instance.updateAssignedVMs(success, userid);
		}
	}
}
