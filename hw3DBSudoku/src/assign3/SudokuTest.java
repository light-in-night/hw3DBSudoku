/**
 * 
 */
package assign3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assign3.Sudoku.Spot;

/**
 * @author User
 *
 */
public class SudokuTest {
	
	Sudoku ez, med, hard, cust;
	int[][] customHardGrid;
	String expectedEasy,expectedEasySolution;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ez = new Sudoku(Sudoku.easyGrid);
		med = new Sudoku(Sudoku.mediumGrid);
		hard = new Sudoku(Sudoku.hardGrid);
		customHardGrid = Sudoku.stringsToGrid(
				"3 7 0 0 0 0 0 8 0",
				"0 0 1 0 9 3 0 0 0",
				"0 4 0 7 8 0 0 0 3",
				"0 9 3 8 0 0 0 1 2",
				"0 0 0 0 4 0 0 0 0",
				"5 2 0 0 0 6 7 9 0",
				"6 0 0 0 2 1 0 4 0",
				"0 0 0 5 3 0 9 0 0",
				"0 3 0 0 0 0 0 5 1");
		expectedEasy = "1 6 4 0 0 0 0 0 2\n" + 
				"2 0 0 4 0 3 9 1 0\n" + 
				"0 0 5 0 8 0 4 0 7\n" + 
				"0 9 0 0 0 6 5 0 0\n" + 
				"5 0 0 1 0 2 0 0 8\n" + 
				"0 0 8 9 0 0 0 3 0\n" + 
				"8 0 9 0 4 0 2 0 0\n" + 
				"0 7 3 5 0 9 0 0 1\n" + 
				"4 0 0 0 0 0 6 7 9\n";
		expectedEasySolution = 	"1 6 4 7 9 5 3 8 2\n" + 
								"2 8 7 4 6 3 9 1 5\n" + 
								"9 3 5 2 8 1 4 6 7\n" + 
								"3 9 1 8 7 6 5 2 4\n" + 
								"5 4 6 1 3 2 7 9 8\n" + 
								"7 2 8 9 5 4 1 3 6\n" + 
								"8 1 9 6 4 7 2 5 3\n" + 
								"6 7 3 5 2 9 8 4 1\n" + 
								"4 5 2 3 1 8 6 7 9\n";
		
		cust = new Sudoku(customHardGrid);
	}
	
	@Test
	public void solveTest1() {
		assertEquals(1, ez.solve());
		assertEquals(1, hard.solve());
		assertTrue(hard.getElapsed() > 0);
		//hard.new Spot(0, 0);
	}
	
	@Test
	public void solveTest2() {
		customHardGrid[0][1] = 0;
		cust = new Sudoku(customHardGrid);
		assertEquals(6, cust.solve());
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < Sudoku.SIZE; j++) {
				customHardGrid[i][j] = 0;
			}
		}
		
		cust = new Sudoku(customHardGrid);
		assertTrue(cust.solve() > 100);
		
	}
	
	@Test
	public void spotTest1() {
		Spot sp = hard.new Spot(0, 0);
		assertEquals(Sudoku.hardGrid[0][0], sp.get());
		hard.solve();
		assertEquals(Sudoku.hardGrid[0][0], sp.get());

		
		assertEquals(1, hard.new Spot(1,2).getRow());
		assertEquals(2, hard.new Spot(1,2).getColumn());
	}
	
	@Test
	public void spotTest2() {
		Spot sp = hard.new Spot(0, 0);
		assertFalse(hard.new Spot(1,2).isGood(0));
		assertTrue(hard.new Spot(1,2).isGood(2));
		assertTrue(hard.new Spot(1,2).isOriginal());
	}
	
	@Test
	public void solutionsTest() {
		Spot sp = hard.new Spot(0, 0);
		assertEquals(0, sp.solutions());
		sp = hard.new Spot(2, 0);
		assertEquals(2, sp.solutions());
	}
	
	@Test
	public void toStringTest() {

		assertEquals(expectedEasy, ez.toString());
		assertEquals(1, ez.solve());
		assertEquals(expectedEasy, ez.toString());
	}
	
	@Test
	public void textToGridTest() {
		int[][] ezGrid = Sudoku.textToGrid(expectedEasy);
		assertArrayEquals(Sudoku.easyGrid, ezGrid);
	}
	

	@Test(expected = RuntimeException.class)
	public void badTextToGridTest() {
		Sudoku.textToGrid("");
	}
	
	@Test
	public void getSolutionTextTest() {
		ez.solve();
		assertEquals(expectedEasySolution, ez.getSolutionText());
	}
	
	@Test
	public void mainCoverage() {
		Sudoku.main(new String[2]);
	}
}
