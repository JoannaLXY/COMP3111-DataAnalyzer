package core.comp3111;

import java.io.Serializable;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartData implements Serializable{
	/**
	 * Attributes
	 */
	private static final long serialVersionUID = 1L;
	private Number[] xValues;
	private Number[] yValues;
	private String seriesName;
	private String labelX;
	private String labelY;
	/**
	 * Create a LineChartData object containing the data of a line chart.
	 * @param x
	 * 			- the x axis values of a line chart
	 * @param y
	 * 			- the y axis values of a line chart
	 * @param s
	 * 			- the name of the series data
	 * @param lx
	 * 			- the label of the x axis
	 * @param ly
	 * 			- the label of the y axis
	 */
	public LineChartData(Number[] x, Number[] y, String s, String lx, String ly){
		xValues = x;
		yValues = y;
		seriesName = s;
		labelX = lx;
		labelY = ly;
	}
	
	/**
	 * Accessor
	 * @return the x axis values of the line chart
	 */
	public Number[] getX() {
		return xValues;
	}
	
	/**
	 * Accessor
	 * @return the y axis values of the line chart
	 */
	public Number[] getY() {
		return yValues;
	}
	
	/**
	 * Accessor
	 * @return the name of the series data of the line chart
	 */
	public String getSeriesName() {
		return seriesName;
	}
	
	/**
	 * Accessor
	 * @return the label of the x axis
	 */
	public String getLabelX() {
		return labelX;
	}
	
	/**
	 * Accessor
	 * @return the label of the y axis
	 */
	public String getLabelY() {
		return labelY;
	}
}
