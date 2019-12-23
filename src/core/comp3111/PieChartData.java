package core.comp3111;

import java.io.Serializable;

public class PieChartData implements Serializable{
	/**
	 * Attributes
	 */
	private static final long serialVersionUID = 1L;
	private Number[] nValues;
	private String[] tValues;
	
	/**
	 * Create a PieChartData object containing the data of a pie chart
	 * @param n
	 * 			- the array storing the number values of a pie chart
	 * @param t
	 * 			- the array storing the string values of a pie chart
	 */
	public PieChartData(Number[] n, String[] t){
		nValues = n;
		tValues = t;
	}
	
	/**
	 * Accessor
	 * @return the array storing the number values of the pie chart
	 */
	public Number[] getN() {
		return nValues;
	}
	
	/**
	 * Accessor
	 * @return the array storing the string values of the pie chart
	 */
	public String[] getT() {
		return tValues;
	}
}
