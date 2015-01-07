package database;

public class DBQueries {

	public static final String TRUNCATE_DATACENTER = "delete from datacenter";
	public static final String TRUNCATE_USER = "delete from user";
	public static final String TRUNCATE_ASSIGNEDVMS = "delete from AssignedIDC";

	public static final String INSERT_USER = "insert into user(user_id, name, email, phone, location) values(?,?,?,?,?)";
	public static final String INSERT_DATACENTER = "insert into datacenter(dc_id, availablevms, location, ram, storage, diameter) values(?,?,?,?,?,?)";

	public static final String SELECT_DATACENTER = "Select * from datacenter";
	public static final String UPDATE_DATACENTER = "update datacenter set availablevms=? where dc_id=?";
	public static final String INSERT_ASSIGNEDIDC = "insert into AssignedIDC(user_id,dc_id,id,ram,storage,assigneddate) values(?,?,?,?,?,?)";
	public static final String SELECT_USER_ID = "select user_id from user";
	public static final String SELECT_LOCATIONOFUSER = "select * from user where user_id=?";

	public static final String SELECT_ASSIGNEDVMS = "select * from AssignedIDC";

}
