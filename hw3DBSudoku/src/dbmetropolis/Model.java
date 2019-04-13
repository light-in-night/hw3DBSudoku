package dbmetropolis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Implementation of the Model in the MVC
 * design pattern. 
 * This model uses AbstractTableModel as a basis.
 * Model updates it's contents whenever a new searuch query
 * happens.
 * Model stores it's data in the form of a 2d array of Objects.
 * Model returns it's data (Strings and Integers) by pointers.
 * Data returned by the model is immutable.
 * 
 * @author User Tornike Onoprishvili
 *
 */
public class Model extends AbstractTableModel implements IModel {
	/**
	 * Make a copy of db connection info for later use.
	 */
	private final String SERVER = MyDBInfo.MYSQL_DATABASE_SERVER;
	private final String ACCOUNT = MyDBInfo.MYSQL_USERNAME;
	private final String PASSWORD = MyDBInfo.MYSQL_PASSWORD;
	private final String DATABASE = MyDBInfo.MYSQL_DATABASE_NAME;

	/**
	 * Data is stored in 2d List Array of Objects.
	 */
	private List<List<Object>> data;
	
	/**
	 * Creates a new Model object
	 * opens SQL connection for the first time.
	 */
	public Model() {
		data = new ArrayList<List<Object>>();
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	
	/**
	 * Creates and returns a database connection
	 * @return Connection to MySql database
	 * @throws SQLException if something goes wrong with connection
	 * 	SQLException is thrown.
	 */
	private Connection getConnection() throws SQLException {
		Connection con =  DriverManager.getConnection("jdbc:mysql://" + SERVER, ACCOUNT, PASSWORD);
		return con;
	}

	@Override
	public int getColumnCount() {
		try(Connection con = getConnection()) {
			Statement stment = con.createStatement();
			stment.executeQuery("USE " + DATABASE);
			ResultSet rs = stment.executeQuery("SELECT * FROM metropolises");
			ResultSetMetaData rsmd = rs.getMetaData();
			return rsmd.getColumnCount();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIdx, int colIdx) {
		return data.get(rowIdx).get(colIdx);
	}
		
	/**
	 * Returns a correct sql query with given parameters
	 * This query intends to add new entry to db
	 * @param met metroplis name
	 * @param cont continent name
	 * @param pop population count
	 * @return returns a string for adding a new entry
	 * 	to the db
	 */
	private PreparedStatement getUpdatePreparedStatment(Connection con, String m, String c, Integer pop) throws SQLException {
		String res = "INSERT INTO metropolises VALUES ( ?, ?, ? )";
		PreparedStatement ps = con.prepareStatement(res);
		
		ps.setString(1, m);
		ps.setString(2, c);
		ps.setLong(3, new Long(pop == null? 0 : pop));
		return ps;
	}
	
	/**
	 * Returns a correct sql query with given parameters
	 * this query searches for results in db
	 * @param met metropolis name
	 * @param cont continent name
	 * @param pop population count
	 * @param exact true = search for Exact match else partial match
	 * @param larget true = more than specified pop else less than or equal to pop.
	 */
	private PreparedStatement getSearchPreparedStatment(Connection con, String m, String c, Integer pop, boolean ex, boolean larger) throws SQLException {
		String base = "Select * From metropolises where";
		
		String cond1 = (m.isEmpty() ? " TRUE " : " metropolis " + (ex?"=":"LIKE") + " ? ") + " AND ";
		String cond2 = (c.isEmpty() ? " TRUE " : " continent " + (ex?"=":"LIKE") + " ? ") + " AND ";
		String cond3 = (pop == null ? " TRUE " : " population " + (larger?">":"<=") + " ? ") + " ; ";
		
		String res = base + cond1 + cond2 + cond3;
		
		PreparedStatement ps = con.prepareStatement(res);
		
		int currIndex = 0;
		if(!m.isEmpty()) {
			currIndex++;
			String regexM = ex ? m : "%" + m + "%";
			ps.setString(currIndex, regexM);
		}
		
		if(!c.isEmpty()) {
			currIndex++;
			String regexC = ex ? c : "%" + c + "%";
			ps.setString(currIndex, regexC);
		}
		
		if(pop != null) {
			currIndex++;
			ps.setLong(currIndex, new Long(pop));
		}
		return ps;
	}
	
	/**
	 * Returns a correct sql query with given parameters
	 * this query searches for results in db. 
	 * 
	 * searches return results even for empty strings.
	 * 
	 * this is used by void addData method, to get newly added rows from the
	 * db.
	 * 
	 * @param met metropolis name
	 * @param cont continent name
	 * @param pop population count
	 * @param exact true = search for Exact match else partial match
	 * @param larget true = more than specified pop else less than or equal to pop.
	 */
	private PreparedStatement getExactSearchPreparedStatement(Connection con, String m, String c, Integer pop) throws SQLException {
		String base = "Select * From metropolises where";
		String cond1 = " metropolis = ? AND";
		String cond2 = " continent = ? AND";
		String cond3 = " population = ? ;";
		String res = base + cond1 + cond2 + cond3;
		
		PreparedStatement psSearch = con.prepareStatement(res);
		
		psSearch.setString(1, m);
		psSearch.setString(2, c);
		psSearch.setLong(3, new Long(pop == null? 0 : pop));
		
		return psSearch;
	}
	
	@Override
	public void searchForData(String m, String c, Integer pop, boolean ex, boolean lt) {
		try(Connection con = getConnection()) {
			Statement stment = con.createStatement();
			stment.executeQuery("USE " + DATABASE);
			
			ResultSet rs = getSearchPreparedStatment(con, m, c, pop, ex, lt).executeQuery();
			 
	        data.clear();
			while (rs.next()) {
	          List<Object> ls = new ArrayList<Object>();
	          ls.add(rs.getString("metropolis"));
	          ls.add(rs.getString("continent"));
	          ls.add(rs.getInt("population"));
	          data.add(ls);
			}
			fireTableDataChanged();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}
	
	@Override
	public void addData(String m, String c, Integer pop) {
		try(Connection con = getConnection()) {
			Statement stment = con.createStatement();
			stment.executeQuery("USE " + DATABASE);
			
			getUpdatePreparedStatment(con, m, c, pop).executeUpdate();
			
			ResultSet rs = getExactSearchPreparedStatement(con,m,c,pop).executeQuery();
			 
	        data.clear();
			while (rs.next()) {
	          List<Object> ls = new ArrayList<Object>();
	          ls.add(rs.getString("metropolis"));
	          ls.add(rs.getString("continent"));
	          ls.add(rs.getInt("population"));
	          data.add(ls);
			}
			fireTableDataChanged();
		} catch (SQLException exc) {
			exc.printStackTrace();
		} 
	}
	
	/**
	 * Retrieves all data from the db.
	 * Updates the model data.
	 */
	@Override
	public void searchAll() {
		try(Connection con = getConnection()) {
			Statement stment = con.createStatement();
			stment.executeQuery("USE " + DATABASE);
			ResultSet rs = stment.executeQuery("SELECT * FROM metropolises");
	        data.clear();
			while (rs.next()) {
	          List<Object> ls = new ArrayList<Object>();
	          ls.add(rs.getString("metropolis"));
	          ls.add(rs.getString("continent"));
	          ls.add(rs.getInt("population"));
	          data.add(ls);
			}
			fireTableDataChanged();
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
	}

	
	@Override
	public String getColumnName(int idx) {
		try(Connection con = getConnection()) {
			Statement stment = con.createStatement();
			stment.executeQuery("USE " + DATABASE);
			ResultSet rs = stment.executeQuery("SELECT * FROM metropolises");
			ResultSetMetaData rsmd = rs.getMetaData();
			return rsmd.getColumnName(idx+1);
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		return "Error While loading";
	}
}
