package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a simple example of using MySQL Data-Base.
 * Use this example for writing solution. 
 * @author boaz.benmoshe
 *
 */
public class SimpleDB {
	public static final String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	public static final String jdbcUser="student";
	public static final String jdbcUserPassword="OOP2020student";
	public static int numOfGames;
	public static int highestLevel;
	public static int[] highestGrades;
	public static int[][] positions;
	private static HashMap <Integer,Integer[]> passedLevels= new HashMap<>();

	/**
	 * Simple main for demonstrating the use of the Data-base
	 * @param args
	 */
	public static void main(String[] args) {
		int id1 = 315242412;  // "dummy existing ID  
		int level = 16;
		//allUsers();
		//printLog();
		String kml = getKML(id1,level);
		System.out.println("***** KML file example: ******");
		System.out.println(kml.toString());
	}

	/**
	 * Initialize the Hash-Map with the levels conditions.
	 */
	private static void initPassedLevel() {

		passedLevels.put(0,new Integer[]{125,290});
		passedLevels.put(1,new Integer[] {436,580});
		passedLevels.put(3,new Integer[] {713,580});
		passedLevels.put(5,new Integer[] {570,500});
		passedLevels.put(9,new Integer[] {480,580});
		passedLevels.put(11,new Integer[] {1050,580});
		passedLevels.put(13,new Integer[] {310,580});
		passedLevels.put(16,new Integer[] {235,290});
		passedLevels.put(19,new Integer[] {250,580});
		passedLevels.put(20,new Integer[] {200,290});
		passedLevels.put(23,new Integer[] {1000,1140});

	}
	
	/** simply prints all the games as played by the users (in the database).
	 * 
	 */
	public static void printLog() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			String allCustomersQuery = "SELECT * FROM Logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);

			while(resultSet.next())
			{
				System.out.println("Id: " + resultSet.getInt("UserID")+","+resultSet.getInt("levelID")+","+resultSet.getInt("moves")+","+resultSet.getDate("time"));
			}
			resultSet.close();
			statement.close();		
			connection.close();		
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function returns the KML string as stored in the database (userID, level);
	 * @param id
	 * @param level
	 * @return
	 */
	public static String getKML(int id, int level) {
		String ans = null;
		String allCustomersQuery = "SELECT * FROM Users where userID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			if(resultSet!=null && resultSet.next()) {
				ans = resultSet.getString("kml_"+level);
			}
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
	/**
	 * This function returns the user statistics stored in the database 
	 * @param id represents the given user ID.
	 */
	public static void getDetails(int id){
		numOfGames=0;
		highestLevel=0;
		highestGrades=new int[24];
		initPassedLevel();
		boolean passed=true;
		for (int level : highestGrades) 
			highestGrades[level]=0;
		String allCustomersQuery = "SELECT * FROM Logs where UserID="+id+";";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);	
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				int currLevel=resultSet.getInt("levelID");
				if(passedLevels.containsKey(currLevel)&&(passedLevels.get(currLevel)[0]>resultSet.getInt("score")||
						passedLevels.get(currLevel)[1]<resultSet.getInt("moves"))) 
					passed=false;

				if(currLevel>highestLevel&&passed)
					highestLevel=currLevel;
				if(currLevel>=0&&currLevel<=highestLevel&&highestGrades[currLevel]<resultSet.getInt("score")&&passed)
					highestGrades[currLevel]=resultSet.getInt("score");
				numOfGames++;
				passed=true;
			}
			resultSet.close();
			statement.close();		
			connection.close();	
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}
	/**
	 * This function returns the user positions stored in the database 
	 * @param id represents the given user ID.
	 */
	public static void getPositions(int id){
		// init the current positions is these level to be 0.

		positions= new int[][] {{0,1,3,5,9,11,13,16,19,20,23},{0,0,0,0,0,0,0,0,0,0,0}};
		String allCustomersQuery2="SELECT * FROM Logs where levelID="+0+" order by score DESC;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);	
			Statement statement = connection.createStatement();
			ResultSet resultSet2=statement.executeQuery(allCustomersQuery2);

			int position;
			for(int i=0;i<positions[0].length;i++) {
				int currLevel=positions[0][i];
				ArrayList <Integer>ids= new ArrayList<>();
				position=1;
				allCustomersQuery2 = "SELECT * FROM Logs where levelID="+currLevel+" order by score DESC;";
				Class.forName("com.mysql.jdbc.Driver");
				resultSet2 = statement.executeQuery(allCustomersQuery2);
				while(resultSet2.next()&&resultSet2.getInt("score")>highestGrades[currLevel]) {
					if(!ids.contains(resultSet2.getInt("UserID"))) {// the user best score has already found
					if(!passedLevels.containsKey(currLevel)) 
						position++;
					else if(passedLevels.get(currLevel)[0]<=resultSet2.getInt("score")&&
							passedLevels.get(currLevel)[1]>=resultSet2.getInt("moves")) 
						position++;
					ids.add(resultSet2.getInt("UserID"));
					}
					
				}
				positions[1][i]=position;
			}
			resultSet2.close();
			statement.close();		
			connection.close();	

		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int allUsers() {
		int ans = 0;
		String allCustomersQuery = "SELECT * FROM Users;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);		
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				System.out.println("Id: " + resultSet.getInt("UserID"));
				ans++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		}
		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ans;
	}
}

