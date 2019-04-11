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
	public IMyTableModel getModel() {
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
	
	@Override
	public IMyTableModel getModel(String met, String cont, int pop, boolean exact, boolean larger) {
		try(Connection con = getConnection();) {
			String condition1 = "metropolis";
			if(!exact) {
				condition1 += " LIKE \"%"+met+"%\" ";
			} else {
				condition1 += " = \"" + met + "\" ";
			}
			
			
			String condition2 = "continent";
			if(!exact) {
				condition2 += " LIKE \"%"+cont+"%\" ";
			} else {
				condition2 += "= \"" + cont + "\" ";
			}
			
			String condition3 = " population ";
			if(larger) {
				condition3 += " > " + Integer.toString(pop) + " ";
			} else {
				condition3 += " <= " + Integer.toString(pop) + " ";
			}
			
			String fullQuery = "SELECT * FROM metropolises " +
						" WHERE " + condition1 +
							 " AND " + condition2 +
							 " AND " + condition3 +" ;";
			
			Statement statement = con.createStatement();
			statement.executeQuery("USE " + DATABASE);
			ResultSet rs = statement.executeQuery(fullQuery);
			return new MyTableModel(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new MyTableModel();
	}
}
