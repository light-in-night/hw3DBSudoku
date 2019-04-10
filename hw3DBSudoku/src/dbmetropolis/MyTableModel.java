package dbmetropolis;

import java.sql.ResultSet;
import java.util.*;

import javax.swing.table.AbstractTableModel;

/**
 * Custom table model created for this
 * project. It is used to save 3 
 * columns, metropolis (string)
 * continent (string) and population (int)
 * @author User Tornike onoprishvili
 *
 */
public class MyTableModel extends AbstractTableModel {
	
	/**
	 * Main data storage.
	 */
	List<List<Object>> data = new ArrayList<List<Object>>();
	
	public MyTableModel() {
		
	}
	
	public MyTableModel(ResultSet rs) {
		
	}

	@Override
	public int getColumnCount() {
		return data.get(0).size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

}
