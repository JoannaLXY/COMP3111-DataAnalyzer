package core.comp3111;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.io.Serializable;
/**
 * 2D array of data values with the following requirements: (1) There are 0 to
 * many columns (2) The number of row for each column is the same (3) 2 columns
 * may have different type (e.g. String and Number). (4) A column can be
 * uniquely identified by its column name (5) add/remove a column is supported
 * (6) Suitable exception handling is implemented
 *
 * @author cspeter
 *
 */
public class DataTable implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Construct - Create an empty DataTable
	 */
	public DataTable() {

		// In this application, we use HashMap data structure defined in
		// java.util.HashMap
		dc = new HashMap<String, DataColumn>();
	}

//TODO
	/**
   * Construct - Create an DataTable with csv type file
	 * 
	 * @param file
	 *            - the file to import
	 * @throws FileNotFoundException - if file not found
	 * @throws DataTableException 
	 */
	@SuppressWarnings("resource")
	public DataTable(File file) throws FileNotFoundException, DataTableException {
		dc = new HashMap<String, DataColumn>();
		Scanner scanIn = null;
		int RowNum = 0;
		int ColNum = 0;
		String inputLine = "";
		//File file = new File(filename);
		ArrayList<String[]> data = new ArrayList<String[]>();

		scanIn = new Scanner(file);
		while(scanIn.hasNext()) {
			inputLine = scanIn.nextLine();
			data.add(inputLine.split(","));
		}
		RowNum = data.size();
		String[] title = data.get(0);
		ColNum = title.length;
		for(int i=0; i<ColNum; i++) {
			//check data type
			String typename = "";
			int j=1;
			String[] row = data.get(j);

			row = data.get(j);
			while((row[i].length()==0) && (j<(RowNum-1))){
				j = j+1;
				row = data.get(j);
			}
			char[] singledata = row[i].toCharArray();
			for(int k = 0;k<row[i].length();k++) {
				if((singledata[k]<'0' || singledata[k]>'9') && singledata[k]!='.') {
					typename = DataType.TYPE_STRING;
					break;
				}
			}
			if(typename == "") {
				typename = DataType.TYPE_NUMBER;
			}

			//construct data column array
			if(typename==DataType.TYPE_NUMBER) {
				Number[] dataArray = new Number[RowNum-1];
				for(int k = 1; k < RowNum; k++) {
					row = data.get(k);
					if(row.length<ColNum) {
						String[] newRow = new String[ColNum];
						for(int m = 0;m<(row.length);m++) {
							newRow[m] = row[m];
						}
						for(int m = (row.length);m<(ColNum);m++) {
							newRow[m] = "";
						}

						data.set(k, newRow);
					}
					row = data.get(k);
					if(row[i].isEmpty()==false) {

						dataArray[k-1] = (Number)Double.parseDouble(row[i]);
					}
					else {
						dataArray[k-1] = null;
					}
				}
				DataColumn dataCol = new DataColumn(typename, dataArray);
				addCol(title[i],dataCol);
			}
			else {
				String[] dataArray = new String[RowNum-1];
				for(int k = 1; k < RowNum; k++) {
					row = data.get(k);
					if(row.length<ColNum) {
						String[] newRow = new String[ColNum];
						for(int m = 0;m<(row.length);m++) {
							newRow[m] = row[m];
						}
						for(int m = (row.length);m<(ColNum);m++) {
							newRow[m] = "";
						}

						data.set(k, newRow);
					}
					row = data.get(k);
					dataArray[k-1] = row[i];

				}
				//construct dataCol
				DataColumn dataCol = new DataColumn(typename, dataArray);
				addCol(title[i],dataCol);
			}


			}


	}

	/**
	 * Fill a data column with missing data.
	 *
	 * @param colName
	 *            - name of the column. It should be a unique identifier
	 * @param type
	 *            - the type to fill the missing data.
	 * @throws DataTableException
	 *             - It throws DataTableException if a column is not exist
	 *
	 */
	public void fillNumCol(String colName, FillType type) throws DataTableException {
		if (!containsColumn(colName)) {
			throw new DataTableException("fillNumCol: The column doesn't exist");
		}
		if (getCol(colName).getTypeName()!=DataType.TYPE_NUMBER) {
			throw new DataTableException("fillNumCol: The column is not numeric type");
		}
		DataColumn dataColumn = getCol(colName);
		int numRow = getNumRow();
		if(type==FillType.byZero) {
			Object[] data = dataColumn.getData();
			for(int i=0; i<numRow;i++) {
				if(data[i]==null || data[i]=="") {
					Number t = 0;
					data[i] = t;
				}
			}
		}
		else if(type==FillType.byMean) {
			Object[] data = dataColumn.getData();

			double sum  =  0;
			int count = 0;
			for(int i=0; i<numRow;i++) {
				if(data[i]!=null && data[i]!="") {
						sum = sum + ((Number)data[i]).doubleValue();
						count = count+1;
				}
			}
			for(int i=0; i<numRow;i++) {
				if(data[i]==null || data[i]=="") {
					data[i] = (Number)(sum/count);
				}
			}
		}
		else if(type==FillType.byMedium) {
			Object[] data = dataColumn.getData();
			Vector<Double> numData = new Vector<Double>();
			//ArrayList<double> numData = new ArrayList<double>();
			for(int i=0; i<numRow;i++) {
				if(data[i]!=null && data[i]!="") {
					//if(data[i].getClass()==Number.class) {
						numData.addElement((Double)((Number)data[i]).doubleValue());
					//}

				}
			}
			Collections.sort(numData);
			double medium;
			int middle = numData.size()/2;
		    if (numData.size()%2 == 1) {
		        medium = numData.elementAt(middle);
		    } else {
		       medium =(numData.get(middle)+numData.get(middle-1))/2.0;
		    }
		    for(int i=0; i<numRow;i++) {
				if(data[i]==null || data[i]=="") {
					data[i] = (Number)medium;
				}
			}
		}


	}



	/**
	 * Add a data column to the table.
	 *
	 * @param colName
	 *            - name of the column. It should be a unique identifier
	 * @param newCol
	 *            - the data column
	 * @throws DataTableException
	 *             - It throws DataTableException if a column is already exist, or
	 *             the row size does not match.
	 */
	public void addCol(String colName, DataColumn newCol) throws DataTableException {
		if (containsColumn(colName)) {
			throw new DataTableException("addCol: The column already exists");
		}

		int curNumCol = getNumCol();
		if (curNumCol == 0) {
			dc.put(colName, newCol); // add the column
			return; // exit the method
		}

		// If there is more than one column,
		// we need to ensure that all columns having the same size

		int curNumRow = getNumRow();
		if (newCol.getSize() != curNumRow) {
			throw new DataTableException(String.format(
					"addCol: The row size does not match: newCol(%d) and curNumRow(%d)", newCol.getSize(), curNumRow));
		}

		dc.put(colName, newCol); // add the mapping
	}

	/**
	 * Remove a column from the data table
	 *
	 * @param colName
	 *            - The column name. It should be a unique identifier
	 * @throws DataTableException
	 *            - It throws DataTableException if the column does not exist
	 */
	public void removeCol(String colName) throws DataTableException {
		if (containsColumn(colName)) {
			dc.remove(colName);
			return;
		}
		throw new DataTableException("removeCol: The column does not exist");
	}

	/**
	 * Get the DataColumn object based on the give colName. Return null if the
	 * column does not exist
	 *
	 * @param colName
	 *            The column name
	 * @return DataColumn reference or null
	 */
	public DataColumn getCol(String colName) {
		if (containsColumn(colName)) {
			return dc.get(colName);
		}
		return null;
	}

	/**
	 * Get all keys. Return null if no
	 * column in the dataset
	 *
	 * @return Set of key string or null
	 */
	public Set<String> getKeys() {
		return dc.keySet();
	}


	/**
	 * Check whether the column exists by the given column name
	 * 
	 * @param colName - name of the column
	 * @return true if the column exists, false otherwise
	 */
	public boolean containsColumn(String colName) {
		return dc.containsKey(colName);
	}

	/**
	 * Return the number of column in the data table
	 *
	 * @return the number of column in the data table
	 */
	public int getNumCol() {
		return dc.size();
	}

	/**
	 * Return the number of row of the data table. This data structure ensures that
	 * all columns having the same number of row
	 *
	 * @return the number of row of the data table
	 */
	public int getNumRow() {
		if (dc.size() <= 0)
			return dc.size();

		// Pick the first entry and get its size
		// assumption: For DataTable, all columns should have the same size
		Map.Entry<String, DataColumn> entry = dc.entrySet().iterator().next();
		return dc.get(entry.getKey()).getSize();
	}

	// attribute: A java.util.Map interface
	// KeyType: String
	// ValueType: DataColumn

	//search the numeric colomns' names
	public ArrayList<String> getNumericColomnNames() {
		ArrayList<String> names = new ArrayList<String>();
		if(dc == null) {
			return names;
		}
		for(Map.Entry<String, DataColumn> entry:dc.entrySet()){
		    if(entry.getValue().getTypeName().equals(DataType.TYPE_NUMBER))  {
		    	names.add(entry.getKey());
		    }

		}
		return names;
	}



	public Map<String, DataColumn> getdc(){
		return dc;
	}

	public void changdc(Map<String, DataColumn> a) {
		dc = a;
	}
	
	public void add(String a,DataColumn b) {
		DataColumn bb = new DataColumn();
		bb.set(b.getTypeName(), b.getData().clone());
		dc.put(a, bb);
	}

	private Map<String, DataColumn> dc;


	public enum FillType {byZero,byMean,byMedium};
}
