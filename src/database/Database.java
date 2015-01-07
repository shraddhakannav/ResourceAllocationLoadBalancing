package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import locationaware.IDC;

public class Database {
	public static Database database;
	private Connection connection;

	public static Database getInstance() {
		if (database == null) {
			database = new Database();

			try {

				Class.forName("com.mysql.jdbc.Driver").newInstance();
				database.connection = DriverManager
						.getConnection("jdbc:mysql://localhost/ralb?"
								+ "user=root&password=root");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return database;
	}

	public Connection getConnection() {
		return connection;
	}

	public void updateDataCenters(List<IDC> success) {
		try {
			for (IDC idc : success) {
				PreparedStatement stmt = this.connection
						.prepareStatement(DBQueries.UPDATE_DATACENTER);
				stmt.setInt(1, idc.getAvailableVMs());
				stmt.setInt(2, idc.getId());
				int noOfRows = stmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int counter = 1;

	public void updateAssignedVMs(List<IDC> success, int userid) {
		try {
			for (IDC idc : success) {
				PreparedStatement stmt = this.connection
						.prepareStatement(DBQueries.INSERT_ASSIGNEDIDC);
				stmt.setInt(1, userid);
				stmt.setInt(2, idc.getId());
				stmt.setInt(3, counter);
				stmt.setDouble(4, idc.getRam());
				stmt.setDouble(5, idc.getStorage());
				stmt.setDate(6, new java.sql.Date(System.currentTimeMillis()));
				stmt.execute();
				counter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<IDC> getAllIDCs() {

		ArrayList<IDC> allIDCs = new ArrayList<IDC>();
		try {
			ResultSet resultSet = this.getConnection().createStatement()
					.executeQuery(DBQueries.SELECT_DATACENTER);

			while (resultSet.next()) {
				IDC idc = new IDC();
				idc.setId(resultSet.getInt("dc_id"));
				idc.setAvailableVMs(resultSet.getInt("availablevms"));
				idc.setRam(resultSet.getDouble("ram"));
				idc.setStorage(resultSet.getDouble("storage"));
				idc.setDiameter(resultSet.getDouble("diameter"));

				String location = resultSet.getString("location");
				String[] point = location.split(",");
				int x = Integer.parseInt(point[0]);
				int y = Integer.parseInt(point[1]);

				idc.setLocation(new Point(x, y));
				allIDCs.add(idc);
			}

			return allIDCs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getRandomUserid() {
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		try {
			ResultSet resultSet = this.getConnection().createStatement()
					.executeQuery(DBQueries.SELECT_USER_ID);
			while (resultSet.next()) {
				int userid = resultSet.getInt("user_id");
				userIds.add(userid);
			}
			if (userIds.size() > 0) {
				Random random = new Random();
				Integer randomInt = userIds.get(random.nextInt(userIds.size()));
				return randomInt;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Point getRandomUseridLocation(int userid) {
		try {
			PreparedStatement pstmt = this.getConnection().prepareStatement(
					DBQueries.SELECT_LOCATIONOFUSER);
			pstmt.setInt(1, userid);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				String string = resultSet.getString("location");
				String[] location = string.split(",");
				int x = Integer.parseInt(location[0]);
				int y = Integer.parseInt(location[1]);
				Point p = new Point(x, y);
				return p;
			}
			return new Point(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return new Point(0, 0);
		}
	}

	public void doCleanup() {
		try {
			Statement stmt = this.getConnection().createStatement();
			stmt.execute(DBQueries.TRUNCATE_USER);
			stmt.execute(DBQueries.TRUNCATE_DATACENTER);
			stmt.execute(DBQueries.TRUNCATE_ASSIGNEDVMS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doUserInitialization(List<User> userList) {
		try {
			PreparedStatement stmt = this.connection
					.prepareStatement(DBQueries.INSERT_USER);

			for (User user : userList) {
				stmt.setInt(1, user.getUser_id());
				stmt.setString(2, user.getName());
				stmt.setString(3, user.getEmailid());
				stmt.setString(4, user.getPhone());
				stmt.setString(5, user.getLocation());

				stmt.execute();
				stmt.clearParameters();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doCloudsIntilization(List<IDC> idcList) {
		try {
			PreparedStatement stmt = this.connection
					.prepareStatement(DBQueries.INSERT_DATACENTER);

			for (IDC idc : idcList) {
				stmt.setInt(1, idc.getId());
				stmt.setInt(2, idc.getAvailableVMs());
				stmt.setString(3, idc.getLocation().x + ","
						+ idc.getLocation().y);
				stmt.setDouble(4, idc.getRam());
				stmt.setDouble(5, idc.getStorage());
				stmt.setDouble(6, idc.getDiameter());

				stmt.execute();
				stmt.clearParameters();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAssignedVMs() {

		try {
			ResultSet resultSet = this.getConnection().createStatement()
					.executeQuery(DBQueries.SELECT_ASSIGNEDVMS);

			while (resultSet.next()) {
				System.out.print(resultSet.getInt("id") + "\t");
				System.out.print(resultSet.getInt("user_id") + "\t");
				System.out.println(resultSet.getInt("dc_id"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
