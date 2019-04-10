package dbmetropolis;

import java.awt.event.ActionListener;
import javax.swing.table.*;

/**
 * Defines how model can be used.
 * @author User Tornike Onoprishvili
 *
 */
public interface IModel {
	/**
	 * Search modifiers
	 */
	public static final int EXACT_MATCH = 0;
	public static final int PARTIAL_MATCH = 1;
	public static final int POP_LARGER = 0;
	public static final int POP_SMALLER_OR_EQUAL = 1;
	
	/**
	 * Adds new metropolice entry to the model.
	 * @param metropolice string representing metropolice's name
	 * @param continent name of the continent
	 * @param population integer denoting the population count
	 */
	void addNewEntry(String metropolice, String continent, int population);
	/**
	 * notifies the registered users of this
	 * class that an update happened in the database
	 * @param updateListener listener object to register
	 */
	void addUpdateListener(ActionListener updateListener);
	
	/**
	 * Gets database results and 
	 * returns them in the form of a MyTableModel
	 * @return
	 */
	MyTableModel getModel();
	

}
