package dbmetropolis;

import javax.swing.table.AbstractTableModel;

public class Main {
	public static void main(String[] args) {
		 
		IModel m = new Model();
		IView v = new View(m);
		IController c = new Controller(v,m);
		
		v.setVisible(true);
	}
}
