package dbmetropolis;

import java.awt.event.ActionListener;

/**
 * Defines how the view can be used
 * by client classes.
 * @author User Tornike onoprishvili
 *
 */
public interface IView {
	/**
	 * sets visibility of the JFrame
	 * @param b
	 */
	void setVisible(boolean b);
	/**
	 * Registers a listener that fires
	 * when search button is hit.
	 * @param act
	 */
	void addSearchListener(ActionListener act);
	
	/**
	 * Registers a listener that fires
	 * when add button is hit.
	 * @param act
	 */
	void addAddListener(ActionListener act);
	
	/**
	 * @return String value of metropolice text field.
	 */
	String getMetropolice();
	/**
	 * @return String value of continent text field.
	 */
	String getContinent();
	/**
	 * @return integer value of population text field.
	 * @throws NumberFormatException - if the string does not contain a parsable integer. 
	 */
	int getPopulation();
}
