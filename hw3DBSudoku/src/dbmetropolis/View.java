package dbmetropolis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 * This class displays the GUI and
 * register listeners for update events
 * implements IView interface and this is how
 * this class should be used.
 * @author User
 *
 */
public class View extends JFrame implements IView {
	/**
	 * ivars for SudokuFrame Class
	 */
	private static final String[] populationPulldown = 
			new String[] {"Population larger than", "Smaller than or equal to"};
	private static final String[] matchPulldown = 
			new String[] {"Exact match", "Partial match"};
	private final int TEXT_FIELD_LENGTH = 16;
	
	private JTextField metropJTF;
	private JTextField continJTF;
	private JTextField populJTF;
	
	private JButton addBtn;
	private JButton searchBtn;
	
	//private JComboBox<E> searchOptsCb;
	private JTable table;
	
	private JComboBox<String> populationCbox;
	private JComboBox<String> matchCbox; 
	
	private IModel model;

	/**
	 * Constructor initializes all Component ivars
	 * adds ActionListeners to them and
	 * creates a pleasant layout.
	 * @param m 
	 */
	public View(IModel model) {
		super("Mertopolis Viewer");
		
		this.model = model;
		this.model.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				updateTable();
			}
		});
		
		setLayout(new BorderLayout(4,4));
		setLocationByPlatform(true);
		
		JPanel upperPanel = new JPanel(new FlowLayout());
		setupSearchPanel(upperPanel);
		add(upperPanel, BorderLayout.PAGE_START);
		
		JScrollPane tablePanel = new JScrollPane();
		table = new JTable(model);
		tablePanel.setViewportView(table);
		add(tablePanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel(new FlowLayout());
		setupControlPanel(controlPanel);
		add(controlPanel, BorderLayout.LINE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
	
	/**
	 * updates and redraws the table.
	 */
	private void updateTable() {
		table.setModel(model);
	}

	/**
	 * adds all control surfaces to given panel.
	 * @param panel panel to add control surfaces to
	 */
	private void setupSearchPanel(JPanel panel) {
		metropJTF = new JTextField(TEXT_FIELD_LENGTH);
		continJTF = new JTextField(TEXT_FIELD_LENGTH);
		populJTF = new JTextField(TEXT_FIELD_LENGTH);
		
		panel.add(new JLabel("Metropolis: "));
		panel.add(metropJTF);
		panel.add(new JLabel("Continent: "));
		panel.add(continJTF);
		panel.add(new JLabel("Population: "));
		panel.add(populJTF);
	}
	
	/**
	 * adds all control surfaces to given panel.
	 * @param controlPanel panel to add control surfaces to
	 */
	private void setupControlPanel(JPanel controlPanel) {
		controlPanel.setLayout(new FlowLayout());
		
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1,20,20));
		addBtn = new JButton("Add");
		searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchTable();
			}
		});JPanel searchOptionsPanel = new JPanel(new GridLayout(2,1,20,20));
		searchOptionsPanel.setBorder(new TitledBorder("Search Options"));

		buttonPanel.add(addBtn);
		buttonPanel.add(searchBtn);
		wrapper.add(buttonPanel);
		
		populationCbox = new JComboBox<String>(populationPulldown);
		matchCbox = new JComboBox<String>(matchPulldown);
		searchOptionsPanel.add(populationCbox);
		searchOptionsPanel.add(matchCbox);
		wrapper.add(searchOptionsPanel);
		
		controlPanel.add(wrapper);
	}
	
	private void searchTable() {
		model.searchForData(metropJTF.getText(),
				continJTF.getText(),
				getPopulation(),
				matchCbox.getSelectedIndex() == 0,
				populationCbox.getSelectedIndex() == 0);
	} 
	
	/**
	 * Adds action listener to view. 
	 * @param srchActionListener action to perform when search button is clicked
	 */
	@Override
	public void addSearchListener(ActionListener srchActionListener) {
		searchBtn.addActionListener(srchActionListener);
	}
	
	/**
	 * Adds action listener to view. 
	 * @param addActionListener action to perform when add button is clicked
	 */
	@Override
	public void addAddListener(ActionListener addActionListener) {
		addBtn.addActionListener(addActionListener);
	}
	
	/**
	 * getter of metropolices JTextField value
	 * @return string written in metropolices JTextField
	 */
	@Override
	public String getMetropolice() {
		return metropJTF.getText();
	}
	
	/**
	 * getter of continets JTextField value
	 * @return string written in continents JTextField
	 */
	@Override
	public String getContinent() {
		return continJTF.getText();
	}
	
	/**
	 * getter of population JTextField value
	 * @return string written in population JTextField
	 */
	@Override
	public Integer getPopulation() {
		Integer population = null;
		
		try {
			population = Integer.parseInt(populJTF.getText());
		} catch (NumberFormatException ex) {
			//ex.printStackTrace();
		}
		
		return population;
	}

}
