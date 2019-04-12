package dbmetropolis;

import javax.swing.table.TableModel;

public interface IModel extends TableModel {
	/**
	 * Searches the data and sets the
	 * inner data structure to the contents of
	 * the query.
	 * 
	 * @param m metropolis name
	 * @param c continent name
	 * @param pop population count
	 * @param ex true = search for Exact match else partial match
	 * @param lt true = more than specified pop else less than or equal to pop.
	 */
	void searchForData(String m, String c, Integer pop, boolean ex, boolean lt);
	/**
	 * Adds a new entry in the DB
	 * @param m	metropolis name
	 * @param c continent name
	 * @param pop population count
	 */
	void addData(String m, String c, Integer pop);
	/**
	 * Gets all the data from the DB
	 */
	void searchAll();
}
