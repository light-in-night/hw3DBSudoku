package dbmetropolis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

import javax.swing.table.TableModel;

public class Model implements IModel {
	
	private final String SERVER = MyDBInfo.MYSQL_DATABASE_SERVER;
	private final String ACCOUNT = MyDBInfo.MYSQL_USERNAME;
	private final String PASSWORD = MyDBInfo.MYSQL_PASSWORD;
	private final String DATABASE = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private List<ActionListener> listeners;
	
	/**
	 * Creates a new Model object
	 * opens SQL connection for the first time.
	 */
	public Model() {
		listeners = new ArrayList<ActionListener>();
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");

//            Statement stmt = con.createStatement();
//            stmt.executeQuery("USE " + DATABASE);
//            ResultSet rs = stmt.executeQuery("SELECT * FROM metropolises");
//
//            // Sample Access Looking for Specific Item
//            rs.absolute(3);
//            System.out.println(rs.getString("metropolis"));
//
//            // Sample Loop Access
//            while (rs.next()) {
//                String s = rs.getString("metropolis");
//                int i = rs.getInt("population");
//                System.out.println(s + "\t" + i);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	@Override
	public void addNewEntry(String metropolice, String continent, int population) {
		try(Connection con = getConnection();) {
			Statement statement = con.createStatement();
			statement.executeQuery("USE " + DATABASE);
			int result = statement.executeUpdate(
							"INSERT INTO metropolises " +
							"VALUES ( \" " + metropolice +" \",\" "+ continent + " \" , " + Integer.toString(population) + " )");
			updateAction();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates and returns a database connection
	 * @return Connection to MySql database
	 * @throws SQLException if something goes wrong with connection
	 * 	SQLException is thrown.
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection
        ("jdbc:mysql://" + SERVER, ACCOUNT, PASSWORD);
	}
	
	/**
	 * Tries to close the connection and 
	 * ignores any exceptions that might come up
	 * @param connection connection to close.
	 */
	private void closeQuitely(Connection connection) {
		try {connection.close();} catch (Exception e) {/*Ignore Exception*/}
	}

	@Override
	public void addUpdateListener(ActionListener updateListener) {
		listeners.add(updateListener);
	}
	
	/**
	 * fires all listeners.
	 * is used when database has been updated.
	 */
	private void updateAction() {
		listeners.forEach(listener -> listener.actionPerformed(null));
	}

	@Override
	public MyTableModel getModel() {
		try(Connection con = getConnection();) {
			Statement statement = con.createStatement();
			statement.executeQuery("USE " + DATABASE);
			ResultSet rs = statement.executeQuery( "SELECT * FROM metropolises;");
			return new MyTableModel(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new MyTableModel();
	}
}
