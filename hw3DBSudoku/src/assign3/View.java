package assign3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class View extends JFrame {
	/**
	 * ivars for SudokuFrame Class
	 */
	
	private JTextField metropJTF;
	private JTextField continJTF;
	private JTextField populJTF;
	
	private JButton addBtn;
	private JButton searchBtn;
	
	private JComboBox<E> searchOptsCb;
	
	/**
	 * Constructor initializes all Component ivars
	 * adds ActionListeners to them and
	 * creates a pleasant layout.
	 */
	public View() {
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
}
