package testing.comp3111;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the DataTable class.
 *
 * You'll be writing tests here for the Unit Testing lab!
 *
 * @author victorkwan
 *
 */
public class DataTableTest {
	DataColumn testDataColumn;
	DataColumn testDataColumn1;

	@BeforeEach
	void init() {
		testDataColumn = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,2,3});
		testDataColumn1 = new DataColumn(DataType.TYPE_STRING,new String[] {"a","b"});
	}
	@Test
	void testGetNumRow_Empty() {
		DataTable dataTable = new DataTable();
		assertEquals(0, dataTable.getNumRow());
	}

	@Test
	void testGetNumRow_NonEmpty() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testColumn", new DataColumn());

		assertEquals(0, dataTable.getNumRow());
	}
	@Test
	void testGetNumCol_NonEmpty() throws DataTableException {
	//	DataColumn testDataColumn = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,2,3});
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);

		int numCol = dataTable.getNumCol();
		assertEquals(1,numCol);
	}
	@Test
	void testGetCol_NonExistent() throws DataTableException {
	//	DataColumn testDataColumn = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,2,3});
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);

		DataColumn dataColumn = dataTable.getCol("testStringColumn");
		assertEquals(null,dataColumn);
	}

	@Test
	void testGetCol_AlreadyExists() throws DataTableException {
	//	DataColumn testDataColumn = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,2,3});
		DataTable dataTable = new DataTable();
		dataTable.addCol("testGetColumn", testDataColumn);

		DataColumn dataColumn = dataTable.getCol("testGetColumn");
		assertEquals(testDataColumn,dataColumn);
	}

	@Test
	void testAddCol_AlreadyExists() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);

		assertThrows(DataTableException.class,()->dataTable.addCol("testNumberColumn", testDataColumn));
	}
	@Test
	void testAddCol_NonExistent() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,7,8});
		dataTable.addCol("testNumberColumn2", testDataColumn2);

		int numCol = dataTable.getNumCol();
		assertEquals(2,numCol);
		//assertThrows(DataTableException.class,()->dataTable.addCol("testNumberColumn", new DataColumn()));
	}

	@Test
	void testAddCol_WrongSize() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,7,8,9});

		assertThrows(DataTableException.class,()->dataTable.addCol("testNumberColumn2", testDataColumn2));
	}

	@Test
	void testRomoveCol_NonExistent() throws DataTableException {
		DataTable dataTable = new DataTable();

		assertThrows(DataTableException.class,()->dataTable.removeCol("testNumberColumn2"));
	}

	@Test
	void testRomoveCol_AlreadyExists() throws DataTableException {
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		dataTable.removeCol("testNumberColumn");
		int numCol = dataTable.getNumCol();
		assertEquals(0,numCol);
	}

	@Test
	void testConversionConstructor0() throws DataTableException, FileNotFoundException {
		File file = new File("test0.csv");
		DataTable dataTable = new DataTable(file);
		int numCol = dataTable.getNumCol();
		assertEquals(3,numCol);

		int numRow = dataTable.getNumRow();
		assertEquals(3,numRow);

	}
	@Test
	void testConversionConstructor1() throws DataTableException, FileNotFoundException {
		File file = new File("testnull.csv");
		assertThrows(FileNotFoundException.class,()->new DataTable(file));

	}

	@Test
	void testConversionConstructor2() throws DataTableException, FileNotFoundException {
		File file = new File("test1.csv");
		DataTable dataTable = new DataTable(file);
		int numCol = dataTable.getNumCol();
		assertEquals(6,numCol);

		int numRow = dataTable.getNumRow();
		assertEquals(4,numRow);

	}

	@Test
	void testConversionConstructor3() throws DataTableException, FileNotFoundException {
		File file = new File("test3.csv");
		DataTable dataTable = new DataTable(file);
		int numCol = dataTable.getNumCol();
		assertEquals(4,numCol);

		int numRow = dataTable.getNumRow();
		assertEquals(3,numRow);

	}

	@Test
	void testConversionConstructor4() throws DataTableException, FileNotFoundException {
		File file = new File("test2.csv");
		DataTable dataTable = new DataTable(file);
		int numCol = dataTable.getNumCol();
		assertEquals(2,numCol);

		int numRow = dataTable.getNumRow();
		assertEquals(3,numRow);

	}

	@Test
	void testfillNumColByMean()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);

		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Object[] {1,"",8.8});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		dataTable.fillNumCol("testNumberColumn2", DataTable.FillType.byMean);
		Object[] test = dataTable.getCol("testNumberColumn2").getData();
	//	Object[] expected = new Object[]{1.5,5.0,8.5};
		assertEquals(4.9,test[1]);
	}

	@Test
	void testfillNumColByZero()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Object[] {1,"",8.8});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		dataTable.fillNumCol("testNumberColumn2", DataTable.FillType.byZero);
		Object[] test = dataTable.getCol("testNumberColumn2").getData();
	//	Object[] expected = new Object[]{1.5,5.0,8.5};
		assertEquals(0,test[1]);
	}

	@Test
	void testfillNumColByMedium1()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Object[] {1.5,"",8.5});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		dataTable.fillNumCol("testNumberColumn2", DataTable.FillType.byMedium);
		Object[] test = dataTable.getCol("testNumberColumn2").getData();
	//	Object[] expected = new Object[]{1.5,5.0,8.5};
		assertEquals(5.0,test[1]);
	}

	@Test
	void testfillNumColByMedium2()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Object[] {1,"",""});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		dataTable.fillNumCol("testNumberColumn2", DataTable.FillType.byMedium);
		Object[] test = dataTable.getCol("testNumberColumn2").getData();
	//	Object[] expected = new Object[]{1.5,5.0,8.5};
		assertEquals(1.0,test[1]);
	}

	@Test
	void testfillNumColNonExistent()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_NUMBER,new Object[] {1,"",""});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		assertThrows(DataTableException.class,()->dataTable.fillNumCol("testNumberColumn3", DataTable.FillType.byMedium));
	}

	@Test
	void testfillNumColWrongType()throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		DataColumn testDataColumn2 = new DataColumn(DataType.TYPE_STRING,new Object[] {"name","id","age"});
		dataTable.addCol("testNumberColumn2", testDataColumn2);
		assertThrows(DataTableException.class,()->dataTable.fillNumCol("testNumberColumn2", DataTable.FillType.byMedium));
	}


	@Test
	void testgetKeys() throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		Set<String> s = dataTable.getKeys();
		Set<String> s1 = new HashSet<String>();
		s1.add("testNumberColumn");
		assertEquals(s,s1);

	}

	@Test
	void testgetNumericColomnNamesandchangdcandadd() throws DataTableException{
		DataTable dataTable = new DataTable();
		dataTable.addCol("testNumberColumn", testDataColumn);
		dataTable.add("testNumberColumn1", testDataColumn1);
		assertEquals(dataTable.getNumericColomnNames().get(0),"testNumberColumn");

		DataTable dataTable1 = new DataTable();
		assertEquals(dataTable1.getNumericColomnNames().isEmpty(),true);

		DataTable dataTable2 = new DataTable();
		dataTable2.changdc(null);
		assertEquals(dataTable2.getNumericColomnNames().isEmpty(),true);

	}

	@Test
	void testgetdc() throws DataTableException{
		DataTable dataTable = new DataTable();
		assertEquals(dataTable.getdc().isEmpty(),true);
	}



}
