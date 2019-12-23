package testing.comp3111;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

import core.comp3111.DataType;

/*
 * Test for data type
 */
public class DataTypeTest {

	@Test
	public void testDataType() {
		assertEquals(DataType.TYPE_OBJECT.equals("java.lang.Object"),true);
		assertEquals(DataType.TYPE_NUMBER.equals("java.lang.Number"),true);
		assertEquals(DataType.TYPE_STRING.equals("java.lang.String"),true);
	}
}