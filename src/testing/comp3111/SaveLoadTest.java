package testing.comp3111;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;
import core.comp3111.LineChartData;
import core.comp3111.PieChartData;
import core.comp3111.Save_Load;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SaveLoadTest {
	Number[] xValues = {1,2,3};
	Number[] yValues = {1,2,3};
	String seriesName = "test";
	String labelX = "testX";
	String labelY = "testY";
	
	Number[] nValues = {1,2,3};
	String[] tValues = {"t1", "t2", "t3"};
	
	Number[] xValues_A = {1,2,3};
	Number[] yValues_A = {1,2,3};
	String seriesName_A = "test";
	String labelX_A = "testX";
	String labelY_A = "testY";
	
	HashMap<String, LineChartData> ld = new HashMap<>();
	HashMap<String, PieChartData> pd = new HashMap<>();
	HashMap<String, LineChartData> ad = new HashMap<>();
	HashMap<String, DataTable> dtd = new HashMap<>();
	
	DataColumn dc = new DataColumn(DataType.TYPE_NUMBER,new Number[] {1,2,3});
	DataTable dt = new DataTable();
	LineChartData l;
	PieChartData p;
	LineChartData a;
	
	File file = new File("unitTest.comp3111");
	File exFile = new File("exception.comp3111");
	
	@BeforeEach
	void init() throws DataTableException {
		l = new LineChartData(xValues, yValues, seriesName, labelX, labelY);
		p = new PieChartData(nValues, tValues);
		a = new LineChartData(xValues_A, yValues_A, seriesName_A, labelX_A, labelY_A);
		dt.addCol("test", dc);
		ld.put("l", l);
		pd.put("p", p);
		ad.put("a", a);
		dtd.put("dt", dt);
	}
	@Test
	void testLineChartDataConstructor(){
		assertEquals(xValues, l.getX());
		assertEquals(yValues, l.getY());
		assertEquals(seriesName, l.getSeriesName());
		assertEquals(labelX, l.getLabelX());
		assertEquals(labelY, l.getLabelY());
	}
	@Test
	void testPieChartDataConstructor() {
		assertEquals(nValues, p.getN());
		assertEquals(tValues, p.getT());
	}
	@Test
	void testSaveAndLoad() throws IOException, ClassNotFoundException {
		Save_Load sl = new Save_Load();
		sl.save(dtd, ld, pd, ad, file);
		HashMap<String, LineChartData> line = sl.loadLineChart(file);
		HashMap<String, PieChartData> pie = sl.loadPieChart(file);
		HashMap<String, LineChartData> animated = sl.loadAnimatedChart(file);
		HashMap<String, DataTable> datatable = sl.load(file);
		
		Set<Map.Entry<String, DataTable>> ds = datatable.entrySet();
		Iterator<Map.Entry<String, DataTable>> dIt = ds.iterator();
		DataTable t = null;
		String dn = "";
		while(dIt.hasNext()) {
			Map.Entry<String, DataTable> e = dIt.next();
			t = (DataTable) e.getValue();
			dn = e.getKey();
		}
		assertEquals("dt", dn);
		Number[] testN = (Number[]) t.getCol("test").getData();
		Number[] loadN = (Number[]) dc.getData();
		for(int i = 0;i < testN.length; i++) {
			assertEquals(testN[i], loadN[i]);
		}
		
		
		Set<Map.Entry<String, LineChartData>> ls = line.entrySet();
		Iterator<Map.Entry<String, LineChartData>> lIt = ls.iterator();
		LineChartData lineData = null;
		String ln = "";
		while(lIt.hasNext()) {
			Map.Entry<String, LineChartData> e = lIt.next();
			lineData = (LineChartData) e.getValue();
			ln = e.getKey();
		}
		assertEquals("l", ln);
		assertEquals(seriesName, lineData.getSeriesName());
		assertArrayEquals(xValues, lineData.getX());
		assertArrayEquals(yValues, lineData.getY());
		assertEquals(labelX, lineData.getLabelX());
		assertEquals(labelY, lineData.getLabelY());
		
		Set<Map.Entry<String, PieChartData>> ps = pie.entrySet();
		Iterator<Map.Entry<String, PieChartData>> pIt = ps.iterator();
		PieChartData pieData = null;
		String n = "";
		while(pIt.hasNext()) {
			Map.Entry<String, PieChartData> e = pIt.next();
			pieData = (PieChartData) e.getValue();
			n = e.getKey();
		}
		assertEquals("p", n);
		assertArrayEquals(nValues, pieData.getN());
		assertArrayEquals(tValues, pieData.getT());
		
		Set<Map.Entry<String, LineChartData>> as = animated.entrySet();
		Iterator<Map.Entry<String, LineChartData>> aIt = as.iterator();
		LineChartData animatedData = null;
		while(aIt.hasNext()) {
			animatedData = (LineChartData) aIt.next().getValue();
		}
		assertEquals(seriesName_A, animatedData.getSeriesName());
		assertArrayEquals(xValues_A, animatedData.getX());
		assertArrayEquals(yValues_A, animatedData.getY());
		assertEquals(labelX_A, animatedData.getLabelX());
		assertEquals(labelY_A, animatedData.getLabelY());
	}
}
