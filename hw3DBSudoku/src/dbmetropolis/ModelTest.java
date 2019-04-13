/**
 * 
 */
package dbmetropolis;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author User
 *
 */
public class ModelTest {

	IModel imodel;
	Model model;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		imodel = new Model();
		model = new Model();
	}

	/**
	 * Test method for {@link dbmetropolis.Model#Model()}.
	 */
	@Test
	public void testModel() {
		Model testmodel = new Model();
		assertEquals(0,testmodel.getRowCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#getColumnCount()}.
	 */
	@Test
	public void testGetColumnCount() {
		assertEquals(3, model.getColumnCount());
		assertEquals(3, imodel.getColumnCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#getRowCount()}.
	 */
	@Test
	public void testGetRowCount() {
		model.searchForData("test", "test", 1, true, false);
		int count = model.getRowCount();
		model.addData("test", "test", 1);
		assertEquals(count+1, model.getRowCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#getValueAt(int, int)}.
	 */
	@Test
	public void testGetValueAt() {
		String testStr = "test_testGetValueAt";
		model.addData(testStr, testStr, 16);
		assertTrue(testStr.equals(model.getValueAt(0, 0)));
		assertTrue(testStr.equals(model.getValueAt(0, 1)));
		assertEquals(16, model.getValueAt(0, 2));
	}

	/**
	 * Test method for {@link dbmetropolis.Model#searchForData(java.lang.String, java.lang.String, java.lang.Integer, boolean, boolean)}.
	 */
	@Test
	public void testSearchForData() {
		String testStr = "test_testSearchForData";
		model.searchForData(testStr, testStr, 0, true, false);
		int currCount = model.getRowCount();
		model.addData(testStr, testStr, 0);
		assertEquals(currCount + 1, model.getRowCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#addData(java.lang.String, java.lang.String, java.lang.Integer)}.
	 */
	@Test
	public void testAddData() {
		String testStr = "test_testAddData";
		model.searchForData(testStr, testStr, 0, true, false);
		int currCount = model.getRowCount();
		int countItems = 5;
		for(int i = 0; i < countItems; i++) {
			model.addData(testStr, testStr, 0);
		}
		assertEquals(currCount + countItems, model.getRowCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#searchAll()}.
	 */
	@Test
	public void testSearchAll() {
		String testStr = "test_testSearchAll";
		
		model.searchAll();
		int currCount = model.getRowCount();
		int countItems = 5;
		
		for(int i = 0; i < countItems; i++) {
			model.addData(testStr, testStr, 0);
		}
		model.searchAll();
		assertEquals(currCount + countItems, model.getRowCount());
	}

	/**
	 * Test method for {@link dbmetropolis.Model#getColumnName(int)}.
	 */
	@Test
	public void testGetColumnNameInt() {
		model.searchAll();
		assertEquals("metropolis", model.getColumnName(0));
		assertEquals("continent", model.getColumnName(1));
		assertEquals("population", model.getColumnName(2));
	}
}
