package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

/*

3 7 0 0 0 0 0 8 0 
0 0 1 0 9 3 0 0 0 
0 4 0 7 8 0 0 0 3 
0 9 3 8 0 0 0 1 2 
0 0 0 0 4 0 0 0 0 
5 2 0 0 0 6 7 9 0 
6 0 0 0 2 1 0 4 0 
0 0 0 5 3 0 9 0 0 
0 3 0 0 0 0 0 5 1

*/

/*
"1 6 4 0 0 0 0 0 2",
"2 0 0 4 0 3 9 1 0",
"0 0 5 0 8 0 4 0 7",
"0 9 0 0 0 6 5 0 0",
"5 0 0 1 0 2 0 0 8",
"0 0 8 9 0 0 0 3 0",
"8 0 9 0 4 0 2 0 0",
"0 7 3 5 0 9 0 0 1",
"4 0 0 0 0 0 6 7 9"
*/
 public class SudokuFrame extends JFrame {
	/**
	 * ivars for SudokuFrame Class
	 */
	private Sudoku sudoku;
	private JTextArea puzzleJTA;
	private JTextArea solutionJTA;
	private JButton checkBtn;
	private JCheckBox autocheckCkb;
	
	/**
	 * Constructor initializes all Component ivars
	 * adds ActionListeners to them and
	 * creates a pleasant layout.
	 */
	public SudokuFrame() {
		super("Sudoku Solver");
		
		setLocationByPlatform(true);
		puzzleJTA = new JTextArea(15,20);
		solutionJTA = new JTextArea(15,20);
		checkBtn = new JButton("Check");
		autocheckCkb = new JCheckBox("Autocheck?");
		
		checkBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkAction();
			}
		});
		puzzleJTA.setBorder(new TitledBorder("puzzle"));
		puzzleJTA.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkDocumentAction();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkDocumentAction();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkDocumentAction();
			}
		});
		
		solutionJTA.setBorder(new TitledBorder("solution"));
		solutionJTA.setEditable(false);
		autocheckCkb.setSelected(true);
		
		setLayout(new BorderLayout(4,4));
		
		add(puzzleJTA, BorderLayout.LINE_START);
		add(solutionJTA, BorderLayout.LINE_END);
		
		JPanel ctrPanel = new JPanel();
		ctrPanel.add(checkBtn);
		ctrPanel.add(autocheckCkb);
		add(ctrPanel, BorderLayout.PAGE_END);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	/**
	 * handles cases when user updates
	 * text in puzzleJTA.
	 */
	private void checkDocumentAction() {
		displayResults(getSolutionText());
	}
	
	/**
	 * handles cases when user presses
	 * text in checkBtn.
	 */
	private void checkAction() {
		displayResults(getSolutionText());
	}
	
	/**
	 * creates new sudoku object and
	 * returns the solved problem as a single String.
	 * @return String text representing solved 
	 * 	sudoku grid + statistics
	 */
	private String getSolutionText() {
		try {
			sudoku = new Sudoku(Sudoku.textToGrid(puzzleJTA.getText()));
			int count = sudoku.solve();
			return sudoku.getSolutionText() +
				"solutions:" + count + "\n" +
				"elapsed:" + sudoku.getElapsed() + "ms\n";
		} catch (Exception ex) {
			return "Parsing problem : \n" + ex.getMessage();
		}
	}
	
	/**
	 * Displays given string to the GUI.
	 * @param result the string to display on screen
	 */
	private void displayResults(String result)  {
		solutionJTA.setText(result);
	}
	
	/**
	 * Program entry point
	 * @param args system arguments
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
