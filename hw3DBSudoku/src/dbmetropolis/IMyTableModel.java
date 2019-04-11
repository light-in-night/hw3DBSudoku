package dbmetropolis;

import javax.swing.table.TableModel;

public interface IMyTableModel extends TableModel {
	/**
	 * Defines a single row for our model
	 * @author User
	 *
	 */
	public static class Row {
		String metropolis;
		String continent;
		Integer populaiton;
		public Row(String m, String c, Integer p) {
			metropolis = m;
			continent = c;
			populaiton = p;
		}
		public Object[] getAsArray() {
			return new Object[] {metropolis, continent, populaiton};
		}
		public String[] getColumns() {
			return new String[] {"Metropolis", "Continent", "Population"};
		}
	}
	
	static final int COLUMN_COUNT = 3;
	Row getRowAt(int idx);
	void setRowAt(int idx, Row r);
	void addRow(Row r);
}
