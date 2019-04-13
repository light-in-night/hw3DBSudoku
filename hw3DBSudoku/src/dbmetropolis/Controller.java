package dbmetropolis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class manages connection between
 * IView implementing class and
 * IModel implementing class.
 * @author User Tornike Onorpshvili
 *
 */
public class Controller implements IController {
	IView view;
	IModel model; 
	
	/**
	 * Creates a new Controller object
	 * @param v view part of the MVC design
	 * @param m model part of the MVC design
	 */
	public Controller(IView v, IModel m) {
		view = v;
		model = m;
		view.addAddListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addRoutine();
			}
			
		});
	}
	
	/**
	 * called whenever adding something to 
	 * the model is required.
	 */
	private void addRoutine() {
		String metropolice = view.getMetropolice();
		String continent = view.getContinent();
		Integer population = view.getPopulation();
		model.addData(metropolice, continent, population);
	}

}
