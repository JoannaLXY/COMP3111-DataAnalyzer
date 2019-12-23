package testing.comp3111;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import core.comp3111.DataColumn;
import core.comp3111.DataType;

/**
 * A sample DataColumn test case written using JUnit. It achieves 100% test
 * coverage on the DataColumn class
 *
 * @author cspeter
 *
 */
class DataColumnTest {

	@Test
	void testCoverageEmptyDataColumnConstructor() {

		DataColumn dc = new DataColumn();
		assertEquals (dc.getSize(), 0);

	}

	@Test
	void testCoverageNonEmptyDataColumnConstructor() {

		Number[] arr = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn dc = new DataColumn(DataType.TYPE_NUMBER, arr);
		assertEquals (dc.getSize(), 5);

	}

	@Test
	void testCoverageGetDataAndType() {

		DataColumn dc = new DataColumn();
		assertEquals (dc.getTypeName(), "");
		assertArrayEquals (dc.getData(), null);

	}

	@Test
	void testCoverageGetDataAndTypeNonEmpty() {

		Number[] arr = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn dc = new DataColumn(DataType.TYPE_NUMBER, arr);
		assertEquals (dc.getTypeName(), "java.lang.Number");
		assertArrayEquals(dc.getData(), arr);

	}
	@Test
	void testContains() {
		String[] arr = new String[] { "a","b","c","d" };
		DataColumn dc = new DataColumn(DataType.TYPE_STRING, arr);
		assertEquals(dc.contains("a"),true);
		assertEquals(dc.contains("e"),false);
	}

	@Test
	void testgetOneData() {
		Number[] arr = new Integer[] { 1, 2, 3, 4, 5 };
		DataColumn dc = new DataColumn(DataType.TYPE_NUMBER, arr);
		int m = (int)(dc.getOneData(1));

		assertEquals (m,2);
		assertEquals ((int)(dc.getOneData(2)),3);
	}
	@Test
	void testchangeData() {
		Number[] arr1 = new Integer[] { 1, 2, 3, 4, 5 };
		Number[] arr = new Integer[] {};
		DataColumn dc = new DataColumn(DataType.TYPE_NUMBER, arr);
		dc.changeData(arr1);
		assertEquals ((int)(dc.getOneData(2)),3);
		assertEquals ((int)(dc.getOneData(1)),2);

	}

}
