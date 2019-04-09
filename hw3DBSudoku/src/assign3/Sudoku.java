package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	/**
	 * The Spot Inner class wraps the functionality
	 * on Sudoku matrix. 
	 * The underlying grid structure which it works on, however,
	 * can change.
	 * !Note: this class has a natural ordering that is inconsistent with equals!
	 * @author User Tornike Onoprishvili
	 */
	public class Spot implements Comparable<Spot> {
		private final int irow, jcol, sqIstart, sqJstart;
		private int lastValue;
		/**
		 * Creates a new spot with a given value
		 * calculates the boundaries of it's parent square
		 * @param val
		 */
		public Spot(int i, int j) {
			irow = i;
			jcol = j;
			int squaresSide = SIZE / PART;
			sqIstart = (irow / squaresSide) * squaresSide;
			sqJstart = (jcol / squaresSide) * squaresSide;
			lastValue = grid[irow][jcol];
		}
		
		/**
		 * Reverts the underlying value to
		 * the originally assigned value in the grid.
		 */
		public void undo() {
			grid[irow][jcol] = originalGrid[irow][jcol];
		}
		
		/**
		 * Returns true if the new value
		 * does not break Sudoku rules.
		 * @param newVal new value to check in this spot
		 * @return true if and only if the new value is
		 * 	applicable here.
		 */
		public boolean isGood(int newVal) {
			if(Arrays.stream(ALLOWED)
					.noneMatch(n -> n == newVal)) 
				return false;
			
			for(int i = 0; i < SIZE; i++) {
				if(grid[i][jcol] == newVal)
					return false;
			}
			
			for(int j = 0; j < SIZE; j++) {
				if(grid[irow][j] == newVal)
					return false;
			}
			
			for(int i = sqIstart; i < sqIstart+PART; i++) {
				for(int j = sqJstart; j < sqJstart+PART; j++) {
					if(grid[i][j] == newVal)
						return false;
				}
			}
			return true;
		}
		
		/**
		 * returns the number of solutions 
		 * for this spot.
		 * @return
		 */
		public int solutions() {
			if(isOriginal())
				return 0;
			int result = 0;
			for(int n : ALLOWED) {
				if(isGood(n))
					result++;
			}
			return result;
		}
		
		/**
		 * Accesses the value of spot.
		 * Change it to given value.
		 * @param val integer to save in this spot
		 */
		public void set(int val) {
			grid[irow][jcol] = val;
		}
		
		/**
		 * Returns the value residing at this spot 
		 * @return value of grid at that spot
		 */
		public int get() {
			return grid[irow][jcol];
		}
		

		/**
		 * @return the irow
		 */
		public int getRow() {
			return irow;
		}
		
		/**
		 * @return the jcol
		 */
		public int getColumn() {
			return jcol;
		}
		
		/**
		 * Is current spot on the original grid
		 * @return true if this spot was filled on original grid.
		 */
		public boolean isOriginal() {
			return originalGrid[irow][jcol] != EMPTY;
		}

		
		/**
		 * compareTo override, used to sort List of Spots for
		 * number of solutions they have. used as a heuristic for
		 * accelerating the process solution. 
		 * @param o the object to compare to
		 * @return an integer representing the result of comparison.
		 * 			specifically: 
		 * 			an integer more than 0 if this is more than o object
		 *   		0 if objects are  equals
		 *   		and less than 0 if this is less than o object
		 */
		@Override
		public int compareTo(Spot o) {
			return this.solutions() - o.solutions();
		}
	}
	
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	public static final int EMPTY = 0; //denotes empty spot
	public static final int[] ALLOWED = new int[]{1,2,3,4,5,6,7,8,9};
	
	private final int[][] originalGrid;
	private int[][] grid;
	private int[][] solvedGrid;
	
	long tickTocStart = 0;
	long tickTocEnd = 0;
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	/**
	 * Creates a new Sudoku instance.
	 * @param ints represents the sudoku grid. must be 9X9
	 */
	public Sudoku(int[][] ints) {
		grid = new int[SIZE][SIZE];
		originalGrid = new int[SIZE][SIZE];
		
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				grid[i][j] = ints[i][j];
				originalGrid[i][j] = ints[i][j];
			}
		}
	}
	
	/**
	 * utility method for copying a 2d integer array
	 * @param toCopy grid to duplicate
	 * @return newly created copy of the original grid.
	 */
	private int[][] duplicateGrid(int[][] toCopy) {
		int[][] newGrid = new int[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++) {
			System.arraycopy(toCopy[i], 0, newGrid[i], 0, SIZE);
		}
		return newGrid;
	}

	/**
	 * This recursive helper method
	 * calculates the number of all possible
	 * solutions to the given sudoku grid
	 * @param ls list of spots in grid. (better if ordered by available points)
	 * @param iat index of current spot, each recursive step increments this, or returns 
	 * 		value immediately.
	 * @return number of possible solutions to the given grid.
	 */
	private int solveUtil(List<Spot> ls, int iat) {
		if(iat >= ls.size()) {
			if(solvedGrid == null)
				solvedGrid = duplicateGrid(grid);
			return 1;
		}
		
		int result = 0;
		for(int n : ALLOWED) {
			Spot current = ls.get(iat);
			if(current.isGood(n)) {
				current.set(n);
				result += solveUtil(ls, iat+1);
				current.undo();
				if(result >= MAX_SOLUTIONS)
					break;
			}
		}
		return result;
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		tickTocStart = System.currentTimeMillis();
		int result = solveUtil(bySolutions(), 0);
		tickTocEnd = System.currentTimeMillis();
		return result;
	}

	/**
	 * Returns soution text.
	 * @return solution text as 9 centences
	 */
	public String getSolutionText() {
		return gridToText(solvedGrid);
	}
	
	/**
	 * Calculates elapsed time from some point
	 * @return elapsed time in ms.
	 */
	public long getElapsed() {
		return tickTocEnd - tickTocStart; // YOUR CODE HERE
	}
		
	/**
	 * Utility function, wraps every cell of the grid in
	 * Spot object.
	 *  
	 * @return array of spots, each one representing on cell
	 *  on sudoku grid
	 */
	private Spot[][] getSpots() {
		Spot[][] spots = new Spot[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				spots[i][j] = this.new Spot(i, j);
			}
		}
		return spots;
	}
	
	/**
	 * Returns Ordered list of Spots by their solve
	 * priority. more constrained goes to lesser 
	 * indices.
	 * @return ordered List of spots.
	 */
	private List<Spot> bySolutions() {
		List<Spot> ls = new ArrayList<Spot>();
		for(Spot[] spArr : getSpots() ) {
			for(Spot sp : spArr) {
				if(!sp.isOriginal())
					ls.add(sp);
			}
		}
		Collections.sort(ls);
		return ls;
	}
	
	/**
	 * Utility method, creates a string that represents 
	 * the grid
	 * @param inputGrid the grid to represent as String
	 * @return resulting String.
	 */
	private String gridToText(int[][] inputGrid) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				result.append(Integer.toString(inputGrid[i][j]));
				result.append(j == SIZE-1 ? "\n" : " ");
			}
		}
		return result.toString();
	}
	
	/**
	 * Returns the original grid
	 */
	@Override
	public String toString() {
		return gridToText(originalGrid);
	}
	
}
