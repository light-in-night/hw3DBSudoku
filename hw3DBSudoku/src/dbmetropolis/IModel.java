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
	IMyTableModel getModel();
	
	/**
	 * Gets all matching database results
	 * and returns them in the form of a MyTableModel
	 * @param met
	 * @param cont
	 * @param pop
	 * @param exact
	 * @param larger
	 * @return
	 */
	IMyTableModel getModel(String met, String cont, int pop, boolean exact, boolean larger);
}
