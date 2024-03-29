package core.comp3111;
import java.io.Serializable;
/**
 * DataColumn - A column of data. This class will be used by DataTable. It
 * stores the data values (data) and the its type (typeName). String constants
 * of type name are defined in DataType.
 *
 * @author cspeter
 *
 */
public class DataColumn implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor. Create an empty data column
	 */
	public DataColumn() {
		data = null;
		typeName = "";
	}

	/**
	 * Constructor. Create a data column by giving the typename and array of Object
	 *
	 * @param typeName
	 *            - defined in DataType. Should be matched with the type of the
	 *            array element
	 * @param values
	 *            - any Java Object array
	 */
	public DataColumn(String typeName, Object[] values) {
		set(typeName, values);
	}

	/**
	 * Associate a Java Object array (with the correct typeName) to DataColumn
	 *
	 * @param typeName
	 *            - defined in DataType. Should be matched with the type of the
	 *            array element
	 * @param values
	 *            - any Java Object array
	 */
	public void set(String typeName, Object[] values) {
		this.typeName = typeName;
		data = values;
	}

	/**
	 * Get the data array
	 *
	 * @return The Object[]. Developers need to downcast it based on the type name
	 */
	public Object[] getData() {
		return data;
	}

	/**
	 * Get the type name
	 *
	 * @return the type name, defined in DataType
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Get the number of elements in the data array
	 *
	 * @return 0 if data is null. Otherwise, length of the data array
	 */
	public int getSize() {
		if (data == null)
			return 0;
		return data.length;
	}
	/**
	 * Check whether certain element is in the array
	 * @param s
	 *    		- the string to be checked
	 * @return true if there exists. Otherwise, return false
	 */
	public boolean contains(String s) {
		for(int i = 0; i<data.length;i++) {
			if (data[i] == s) {
				return true;
			}
		}
		return false;

	}

	public double getOneData(int i) {

			double m = ((Number)data[i]).doubleValue();
			return m;

	}

	public void changeData(Object[] a) {
		data = a;
	}
	
	
	




	// attributes
	private Object[] data;
	private String typeName;

}
