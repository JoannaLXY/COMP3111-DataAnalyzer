package ui.comp3111;

import core.comp3111.DataColumn;
import core.comp3111.DataTable;
import core.comp3111.DataTableException;
import core.comp3111.DataType;
import core.comp3111.LineChartData;
import core.comp3111.PieChartData;
import core.comp3111.Save_Load;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Collections;//
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.Iterator;
/**
 * The Main class of this GUI application
 *
 * @author cspeter
 *
 */
public class Main extends Application {

	// Attribute: DataTable
	// In this sample application, a single data table is provided
	// You need to extend it to handle multiple data tables
	// Hint: Use java.util.List interface and its implementation classes (e.g.
	// java.util.ArrayList)
	private HashMap<String, DataTable> DataTableList = new HashMap<String, DataTable>();
//	private ArrayList<DataTable> DataTableList = new ArrayList<DataTable>();
	private DataTable templateDataTable = null;
	private String templateSeriesName = null;
	// Attributes: Scene and Stage
	private static final int SCENE_NUM = 6;//
	private static final int SCENE_MAIN_SCREEN = 0;
	private static final int SCENE_LINE_CHART = 1;
	private static final int SCENE_PIE_CHART = 2;
	private static final int SCENE_ANIMATED_CHART = 3;
	private static final int SCENE_CHOS_FILTER = 4;//
	private static final int SCENE_CHOS_SPLIT = 5;//
	private static final String[] SCENE_TITLES = { "COMP3111 Chart - [CodingMaster]", "Chart Screen","Pie Screen","Animated Chart","Filtering numeric data","Randomly split datasset" };
	private Stage stage = null;
	private Scene[] scenes = null;

	// To keep this application more structural,
	// The following UI components are used to keep references after invoking
	// createScene()

	// Screen 1: paneMainScreen
	private Menu menuFile;
    private MenuItem importF;		// Import *.csv data set
    private MenuItem exportF;		// Export *.csv data set
    private MenuItem save;			// Save current project
    private MenuItem load;			// Load an existing project
	private Button btLineChart, btPieChart, btAnimatedChart, btChosFilter, btChosSplit;//
	private Label lbDataTable, lbMainScreenTitle, lbDataFandT;
	private ListView<String> listData;
	private ListView<String> listChart;
	private ObservableList<String> itemsData;
	private ObservableList<String> itemsChart;
	private HashMap<String, Chart> chart = new HashMap<String, Chart>();
	private HashMap<String, LineChart<Number, Number>> animated = new HashMap<>();
	private String chartName;
	private HashMap<String, LineChartData> lineChartData = new HashMap<>();
	private HashMap<String, PieChartData> pieChartData = new HashMap<>();
	private HashMap<String, LineChartData> animatedChartData = new HashMap<>();
	// initialize the observable bounds of the X axis of animated chart.
	private int MAX_xAxis = -10000000;
	private int MAX = -10000000;
	// Screen 2: paneSampleLineChartScreen
	private LineChart<Number, Number> lineChart = null;
	private NumberAxis xAxis = null;
	private NumberAxis yAxis = null;
	private Button btLineChartBackMain = null;
	private ComboBox<String> comboBoxX = new ComboBox<String>();
	private ComboBox<String> comboBoxY = new ComboBox<String>();
	private HashMap<String, DataColumn> dataX = new HashMap<String, DataColumn>();
	private HashMap<String, DataColumn> dataY = new HashMap<String, DataColumn>();


	//Screen 3: paneSamplePieChartScreen
	private PieChart pieChart = null;
	private Button btPieChartBackMain = null;
	private ComboBox<String> comboBoxT = new ComboBox<String>();
	private ComboBox<String> comboBoxN = new ComboBox<String>();
	private HashMap<String, DataColumn> dataT = new HashMap<String, DataColumn>();
	private HashMap<String, DataColumn> dataN = new HashMap<String, DataColumn>();

	//Screen 4: paneAnimatedChartScreen
	private LineChart<Number, Number> animatedChart = null;
	private NumberAxis xAxis_A = null;
	private NumberAxis yAxis_A = null;
	private Button btLineChartBackMain_A = null;
	private ComboBox<String> comboBoxX_A = new ComboBox<String>();
	private ComboBox<String> comboBoxY_A = new ComboBox<String>();
	private HashMap<String, DataColumn> dataX_A = new HashMap<String, DataColumn>();
	private HashMap<String, DataColumn> dataY_A = new HashMap<String, DataColumn>();

	//Screen 5: choices of filtering numeric data
	private Button btChosFilterBackMain = null;
	private Button btChosFilterConfirm = null;
	private ChoiceBox<String> cbColomn = null;
	private ChoiceBox<String> cbSign = null;
	private ChoiceBox<String> cbFReplace = null;
	private Label chosnum,chosColomn, chosSign, chosFReplace;
	private TextField numInput = null;




	//Screen 6: choices of randomly split dataset
	//to do
	private Button btChosSplitBackMain = null;
	private Button btChosSplitConfirm = null;
	private ChoiceBox<String> cbSReplace = null;
	private TextField snumInput = null;
	private Label chosPer, chosSReplace;


	/**
	 * create all scenes in this application
   * @throws DataTableException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void initScenes() throws DataTableException, ClassNotFoundException, IOException {
		scenes = new Scene[SCENE_NUM];
		scenes[SCENE_MAIN_SCREEN] = new Scene(paneMainScreen(), 400, 500);
		scenes[SCENE_LINE_CHART] = new Scene(paneLineChartScreen(), 800, 600);
		scenes[SCENE_PIE_CHART] = new Scene(panePieChartScreen(),800,600);
		scenes[SCENE_ANIMATED_CHART] = new Scene(paneAnimatedChartScreen(), 800, 600);
		scenes[SCENE_CHOS_FILTER] = new Scene(paneChosFilterScreen(), 300, 400);//
		scenes[SCENE_CHOS_SPLIT] = new Scene(paneChosSplitScreen(), 300, 400);//
		for (Scene s : scenes) {
			if (s != null)
				// Assumption: all scenes share the same stylesheet
				s.getStylesheets().add("Main.css");
		}
	}

	/**
	 * This method will be invoked after createScenes(). In this stage, all UI
	 * components will be created with a non-NULL references for the UI components
	 * that requires interaction (e.g. button click, or others).
	 */
	private void initEventHandlers() {
		initMainScreenHandlers();
		initLineChartScreenHandlers();
		initPieChartScreenHandlers();
		initAnimatedChartScreenHandlers();
		initChosFilterHandlers();//
		initChosSplitHandlers();//
	}

	/**
	 * Initialize the event handlers of the line chart screen.
	 * Save the current chart when back to menu.
	 */
	private void initLineChartScreenHandlers() {

		// click handler
		btLineChartBackMain.setOnAction(e -> {
			if(comboBoxX.getValue()!=null && comboBoxY.getValue()!=null) {
			NumberAxis txAxis = new NumberAxis();
			NumberAxis tyAxis = new NumberAxis();
			LineChart<Number,Number> tempChart = new LineChart<Number, Number>(txAxis, tyAxis);
			XYChart.Series tseries = new XYChart.Series();
			tseries.setName(templateSeriesName);
    		// populating the series with data
			// As we have checked the type, it is safe to downcast to Number[]
			Number[] xValues = (Number[]) dataX.get(comboBoxX.getValue()).getData();
			Number[] yValues = (Number[]) dataY.get(comboBoxY.getValue()).getData();

			// In DataTable structure, both length must be the same
			int len = xValues.length;

			for (int i = 0; i < len; i++) {
				tseries.getData().add(new XYChart.Data(xValues[i], yValues[i]));
			}

			// add the new series as the only one series for this line chart
			tempChart.getData().add(tseries);
			itemsChart.add(chartName);
			chart.put(chartName, tempChart);
			LineChartData d = new LineChartData(xValues, yValues, templateSeriesName, xAxis.getLabel(), yAxis.getLabel());
			lineChartData.put(chartName, d);
			}
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});

		comboBoxX.valueProperty().addListener(new ChangeListener<String>() {
	            @Override
	            public void changed(ObservableValue ov, String t, String t1) {
	            	lineChart.setVisible(true);
	            	xAxis.setLabel(t1);
	            	// defining a series
	            	XYChart.Series series = new XYChart.Series();
					series.setName(templateSeriesName);
	            	if(comboBoxY.getValue()!=null && comboBoxX.getValue()!=null && dataX.get(t1).getTypeName().equals(DataType.TYPE_NUMBER) && dataY.get(comboBoxY.getValue()).getTypeName().equals(DataType.TYPE_NUMBER)) {
		            	// populating the series with data
	            		// As we have checked the type, it is safe to downcast to Number[]
						Number[] xValues = (Number[]) dataX.get(t1).getData();
						Number[] yValues = (Number[]) dataY.get(comboBoxY.getValue()).getData();

						// In DataTable structure, both length must be the same
						int len = xValues.length;

						for (int i = 0; i < len; i++) {
							series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
						}


	            	}
	            	// clear all previous series
					lineChart.getData().clear();

					// add the new series as the only one series for this line chart
					lineChart.getData().add(series);

	            }
	        });
		comboBoxY.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
            	lineChart.setVisible(true);
            	yAxis.setLabel(t1);
            	// defining a series
				XYChart.Series series = new XYChart.Series();
				series.setName(templateSeriesName);

            	if(comboBoxY.getValue()!=null && comboBoxX.getValue()!=null && dataX.get(comboBoxX.getValue()).getTypeName().equals(DataType.TYPE_NUMBER) && dataY.get(t1).getTypeName().equals(DataType.TYPE_NUMBER)) {
		        // populating the series with data
				// As we have checked the type, it is safe to downcast to Number[]
				Number[] xValues = (Number[]) dataX.get(comboBoxX.getValue()).getData();
				Number[] yValues = (Number[]) dataY.get(t1).getData();

				// In DataTable structure, both length must be the same
				int len = xValues.length;

				for (int i = 0; i < len; i++) {
					series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
				}
            	}
            	// clear all previous series
				lineChart.getData().clear();

				// add the new series as the only one series for this line chart
				lineChart.getData().add(series);

            }
        });
	}

	/**
	 * Initialize the event handlers of the pie chart screen.
	 * Save the current chart when back to menu.
	 */
	private void initPieChartScreenHandlers() {

		// click handler
		btPieChartBackMain.setOnAction(e -> {
			if(comboBoxN.getValue()!=null&&comboBoxT.getValue()!=null) {
				ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
	        	// populating the series with data
				// As we have checked the type, it is safe to downcast to Number[]
				Number[] nValues = (Number[]) dataN.get(comboBoxN.getValue()).getData();
				String[] tValues = (String[]) dataT.get(comboBoxT.getValue()).getData();

				// In DataTable structure, both length must be the same
				int len = tValues.length;

				for (int i = 0; i < len; i++) {
					pieData.add(new PieChart.Data(tValues[i],nValues[i].doubleValue()));
				}

	        	PieChart tempChart = new PieChart();
				tempChart.setData(pieData);
				itemsChart.add(chartName);
				chart.put(chartName, tempChart);
				PieChartData d = new PieChartData(nValues, tValues);
				pieChartData.put(chartName, d);
			}
			putSceneOnStage(SCENE_MAIN_SCREEN);
		});


		comboBoxN.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {

            	pieChart.setVisible(true);
            	ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            	if(comboBoxN.getValue()!=null && comboBoxT.getValue()!=null && dataN.get(t1).getTypeName().equals(DataType.TYPE_NUMBER) && dataT.get(comboBoxT.getValue()).getTypeName().equals(DataType.TYPE_STRING)) {
            		// populating the series with data
    				// As we have checked the type, it is safe to downcast to Number[]
    				Number[] nValues = (Number[]) dataN.get(t1).getData();
    				String[] tValues = (String[]) dataT.get(comboBoxT.getValue()).getData();

    				// In DataTable structure, both length must be the same
    				int len = tValues.length;

    				for (int i = 0; i < len; i++) {
    					pieData.add(new PieChart.Data(tValues[i],nValues[i].doubleValue()));
    				}
            	}
            	// clear all previous series
				pieChart.getData().clear();
				pieChart.setData(pieData);
            }
		});

		comboBoxT.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {

            	pieChart.setVisible(true);
             	ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            	if(comboBoxN.getValue()!=null && comboBoxT.getValue()!=null && dataN.get(comboBoxN.getValue()).getTypeName().equals(DataType.TYPE_NUMBER) && dataT.get(t1).getTypeName().equals(DataType.TYPE_STRING)) {

    				// populating the series with data
    				// As we have checked the type, it is safe to downcast to Number[]
    				Number[] nValues = (Number[]) dataN.get(comboBoxN.getValue()).getData();
    				String[] tValues = (String[]) dataT.get(t1).getData();

    				// In DataTable structure, both length must be the same
    				int len = nValues.length;


    				for (int i = 0; i < len; i++) {
    					pieData.add(new PieChart.Data(tValues[i],nValues[i].doubleValue()));
    				}
    			}
            	// clear all previous series
				pieChart.getData().clear();
				pieChart.setData(pieData);
            }
		});


	}

 /**
	 * Initialize the event handlers of the animated chart screen.
	 * Save the current chart when back to menu.
	 */
	private void initAnimatedChartScreenHandlers() {
		// click handler
				btLineChartBackMain_A.setOnAction(e -> {
					if(comboBoxX_A.getValue()!=null && comboBoxY_A.getValue()!=null) {
						NumberAxis txAxis = new NumberAxis();
						NumberAxis tyAxis = new NumberAxis();
						LineChart<Number,Number> tempChart = new LineChart<Number, Number>(txAxis, tyAxis);
						XYChart.Series tseries = new XYChart.Series();
						tseries.setName(templateSeriesName);
			    		// populating the series with data
						// As we have checked the type, it is safe to downcast to Number[]
						Number[] xValues = (Number[]) dataX_A.get(comboBoxX_A.getValue()).getData();
						Number[] yValues = (Number[]) dataY_A.get(comboBoxY_A.getValue()).getData();

						// In DataTable structure, both length must be the same
						int len = xValues.length;

						for (int i = 0; i < len; i++) {
							tseries.getData().add(new XYChart.Data(xValues[i], yValues[i]));
						}

						// add the new series as the only one series for this line chart
						tempChart.getData().add(tseries);
						itemsChart.add(chartName);
						animated.put(chartName, tempChart);
						// save the data of the current animated chart to animatedChartData.
						LineChartData d = new LineChartData(xValues, yValues, templateSeriesName, xAxis_A.getLabel(), yAxis_A.getLabel());
						animatedChartData.put(chartName, d);
						}
						putSceneOnStage(SCENE_MAIN_SCREEN);
						timer.stop();
						MAX = -10000000;
						MAX_xAxis = -10000000;
				});

				comboBoxX_A.valueProperty().addListener(new ChangeListener<String>() {
			            @Override
			            public void changed(ObservableValue ov, String t, String t1) {
			            	xAxis_A.setLabel(t1);
			            	if(comboBoxY_A.getValue()!=null && comboBoxX_A.getValue()!=null && dataX_A.get(t1).getTypeName().equals(DataType.TYPE_NUMBER) && dataY_A.get(comboBoxY_A.getValue()).getTypeName().equals(DataType.TYPE_NUMBER)) {
				            	// defining a series
								XYChart.Series series = new XYChart.Series();
								series.setName(templateSeriesName);

								// populating the series with data
								// As we have checked the type, it is safe to downcast to Number[]
								Number[] xValues = (Number[]) dataX_A.get(t1).getData();
								Number[] yValues = (Number[]) dataY_A.get(comboBoxY_A.getValue()).getData();

								// In DataTable structure, both length must be the same
								int len = xValues.length;
								MAX = -10000000;


								for (int i = 0; i < len; i++) {
									if(xValues[i].intValue() >= MAX) {
										MAX = xValues[i].intValue();
									}
									series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
								}
								// initialize the attributes of the observable X axis.
								MAX_xAxis = MAX / 3;
								animatedChart.setAnimated(false);
								xAxis_A.setUpperBound(MAX_xAxis);
								xAxis_A.setLowerBound(0);
								xAxis_A.setTickUnit(MAX_xAxis / 10);
								// clear all previous series
								animatedChart.getData().clear();

								// add the new series as the only one series for this line chart
								animatedChart.getData().add(series);

							    timer.start();
			            	}

			            }
			        });
				comboBoxY_A.valueProperty().addListener(new ChangeListener<String>() {
		            @Override
		            public void changed(ObservableValue ov, String t, String t1) {
		            	yAxis_A.setLabel(t1);
		            	if(comboBoxY_A.getValue()!=null && comboBoxX_A.getValue()!=null && dataX_A.get(comboBoxX_A.getValue()).getTypeName().equals(DataType.TYPE_NUMBER) && dataY_A.get(t1).getTypeName().equals(DataType.TYPE_NUMBER)) {

		            	// defining a series
						XYChart.Series series = new XYChart.Series();
						series.setName(templateSeriesName);

						// populating the series with data
						// As we have checked the type, it is safe to downcast to Number[]
						Number[] xValues = (Number[]) dataX_A.get(comboBoxX_A.getValue()).getData();
						Number[] yValues = (Number[]) dataY_A.get(t1).getData();

						// In DataTable structure, both length must be the same
						int len = xValues.length;


						for (int i = 0; i < len; i++) {
							if(xValues[i].intValue() >= MAX) {
								MAX = xValues[i].intValue();
							}
							series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
						}
						// initialize the attributes of the observable X axis.
						MAX_xAxis = MAX / 3;
						animatedChart.setAnimated(false);
						xAxis_A.setUpperBound(MAX_xAxis);
						xAxis_A.setLowerBound(0);
						xAxis_A.setTickUnit(MAX_xAxis / 10);
						// clear all previous series
						animatedChart.getData().clear();

						// add the new series as the only one series for this line chart
						animatedChart.getData().add(series);

					    timer.start();
		            	}

		            }
		        });
	}
	private void initChosFilterHandlers() {
		btChosFilterBackMain.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
			});
		btChosFilterConfirm.setOnAction(e -> {
			String num = isInt(numInput,numInput.getText());
			if(num == "f") {
				//
			}
			else {
				Double n = Double.parseDouble(numInput.getText());
				String col = cbColomn.getValue();
				String sign = cbSign.getValue();
				String rep = cbFReplace.getValue();

				DataTable backup = new DataTable();
				for(Map.Entry<String, DataColumn> entry:templateDataTable.getdc().entrySet()) {
				    backup.add(entry.getKey(), entry.getValue());
				}

				DataTable temp1 = new DataTable();
				for(Map.Entry<String, DataColumn> entry:templateDataTable.getdc().entrySet()) {
				    temp1.add(entry.getKey(), entry.getValue());
				}

				List<Integer> numToRemove = new ArrayList<Integer>();
//				Map<String, DataColumn> t = new HashMap<String, DataColumn>();
//				t.putAll(templateDataTable.getdc());
//				temp1.changdc(t);
				if(sign == ">") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) <= n) {
							numToRemove.add(i);
						}
					}
				}
				if(sign == ">=") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) < n) {
							numToRemove.add(i);
						}
					}
				}
				if(sign == "==") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) != n) {
							numToRemove.add(i);
						}
					}
				}
				if(sign == "!=") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) == n) {
							numToRemove.add(i);
						}
					}
				}
				if(sign == "<=") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) > n) {
							numToRemove.add(i);
						}
					}
				}
				if(sign == "<") {
					for(int i = 0; i < temp1.getNumRow(); i++) {
						if(templateDataTable.getdc().get(col).getOneData(i) >= n) {
							numToRemove.add(i);
						}
					}
				}
				Collections.sort(numToRemove);


				for(int j = numToRemove.size()-1; j >= 0 ; j--) {
					for(Map.Entry<String, DataColumn> entry:temp1.getdc().entrySet()) {
						if(entry.getValue().getTypeName().equals(DataType.TYPE_NUMBER)) {
							Number[] tempdata1= new Number[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove.get(j)) {
							        tempdata1[p++] =(Number) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);

						}
						else {
							String[] tempdata1= new String[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove.get(j)) {
							        tempdata1[p++] = (String) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);
						}



//						Object[] tempdata = entry.getValue().getData();
//						if(tempdata.length==1) {tempdata = null;}else {
//						System.arraycopy(tempdata, numToRemove.get(j) + 1, tempdata, numToRemove.get(j), tempdata.length - 1 - numToRemove.get(j));
//						}entry.getValue().changeData(tempdata);
					}
				}

				if(rep == "Replace the current dataset") {
					DataTableList.replace(templateSeriesName, temp1);
					templateDataTable = temp1;
				}else {
					String ne = "new";
					String o = "new";
					int p = 0;
					while(DataTableList.containsKey(ne+templateSeriesName)) {
						p++;
						ne = o + p;
					}

					DataTableList.put(ne+templateSeriesName, temp1);
		        	listData.getItems().add(ne+templateSeriesName);
		        	DataTableList.replace(templateSeriesName, backup);
		        	templateDataTable = backup;
				}


				putSceneOnStage(SCENE_MAIN_SCREEN);
			}
		});
	}

	private void initChosSplitHandlers() {
		btChosSplitBackMain.setOnAction(e -> {
			putSceneOnStage(SCENE_MAIN_SCREEN);
			});
		btChosSplitConfirm.setOnAction(e -> {
			String num = isInt(snumInput,snumInput.getText());
			String nu = isInt100(snumInput,snumInput.getText());
			String rep = cbSReplace.getValue();
			if(num == "f" || nu == "f") {
				//
			}
			else {
				Double per = Double.parseDouble(snumInput.getText());
				String replace = cbSReplace.getValue();

				int numneed = (int)(per*templateDataTable.getNumRow()/100);
				Random r = new Random();
				List<Integer> numToRemove = new ArrayList<Integer>();
				List<Integer> numToRemove2 = new ArrayList<Integer>();
				for(int i = 0; i < templateDataTable.getNumRow(); i++) {
					numToRemove2.add(i);
				}

				for(int i = 0; i < numneed; i++) {
					while(true) {
						int ran = r.nextInt(templateDataTable.getNumRow());
						boolean torf = true;
						for(int j = 0; j < numToRemove.size(); j++) {
							if(ran == numToRemove.get(j)) {
								torf = false;
							}
						}
						if(torf == true) {
							numToRemove.add(ran);
							numToRemove2.remove(ran);
							break;
						}
					}
				}
				Collections.sort(numToRemove);
				Collections.sort(numToRemove2);

				DataTable backup1 = new DataTable();
				for(Map.Entry<String, DataColumn> entry:templateDataTable.getdc().entrySet()) {
				    backup1.add(entry.getKey(), entry.getValue());
				}

				DataTable backup2 = new DataTable();
				for(Map.Entry<String, DataColumn> entry:templateDataTable.getdc().entrySet()) {
				    backup2.add(entry.getKey(), entry.getValue());
				}

				for(int j = numToRemove.size()-1; j >= 0 ; j--) {
					for(Map.Entry<String, DataColumn> entry:backup1.getdc().entrySet()) {
						if(entry.getValue().getTypeName().equals(DataType.TYPE_NUMBER)) {
							Number[] tempdata1= new Number[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove.get(j)) {
							        tempdata1[p++] =(Number) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);

						}
						else {
							String[] tempdata1= new String[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove.get(j)) {
							        tempdata1[p++] = (String) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);
						}
					}
				}

				for(int j = numToRemove2.size()-1; j >= 0 ; j--) {
					for(Map.Entry<String, DataColumn> entry:backup2.getdc().entrySet()) {
						if(entry.getValue().getTypeName().equals(DataType.TYPE_NUMBER)) {
							Number[] tempdata1= new Number[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove2.get(j)) {
							        tempdata1[p++] =(Number) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);

						}
						else {
							String[] tempdata1= new String[entry.getValue().getSize()-1];
							int p = 0;
							for (int i = 0; i < entry.getValue().getSize(); i++) {//
							    if (i != numToRemove2.get(j)) {
							        tempdata1[p++] = (String) entry.getValue().getData()[i];
							    }
							}
							entry.getValue().changeData(tempdata1);
						}
					}
				}

				String ne = "new";
				String o = "new";
				int p = 0;
				while(DataTableList.containsKey(ne+templateSeriesName)) {
					p++;
					ne = o + p;
				}

				DataTableList.put(ne+templateSeriesName, backup1);
	        	listData.getItems().add(ne+templateSeriesName);

				String ne2 = "new";
				String o2 = "new";
				int p2 = 0;
				while(DataTableList.containsKey(ne2+templateSeriesName)) {
					p2++;
					ne2 = o2 + p2;
				}

				DataTableList.put(ne2+templateSeriesName, backup2);
	        	listData.getItems().add(ne2+templateSeriesName);

	        	if(replace == "Replace the current dataset"){
	        		DataTableList.remove(templateSeriesName);
	        		listData.getItems().remove(templateSeriesName);
		}




				putSceneOnStage(SCENE_MAIN_SCREEN);
			}
		});


		// to do
	}

	/**
	 * Construct a timer to move the animated line chart successively.
	 */
	AnimationTimer timer = new AnimationTimer() {
		private long lastUpdate = 0;
		public void handle(long now) {
			if(now - lastUpdate >= 18000000) {
				updateDisplayBounds();
				lastUpdate = now;
			}
		}
	};

	/**
	 * This function updates the observable bounds of
	 * the X axis of the animated line chart.
	 */
	private void updateDisplayBounds() {
	    // update
		if(xAxis_A.getUpperBound() >= MAX) {
			xAxis_A.setUpperBound(MAX_xAxis);
			xAxis_A.setLowerBound(0);
		}
		else {
	    xAxis_A.setUpperBound(xAxis_A.getUpperBound() + (double)MAX / 100);
	    xAxis_A.setLowerBound(xAxis_A.getLowerBound() + (double)MAX / 100);
		}
	}

	/**
	 * Populate template data table values to the chart view
	 */
	private void populateDataTableValuesToLineChart(String seriesName) {
		dataX.clear();
		dataY.clear();
		comboBoxX.getItems().clear();
		comboBoxY.getItems().clear();
		Set<String> key= templateDataTable.getKeys();
	//	int i = 0;
		for(String s : key) {
			if(templateDataTable.getdc().get(s).getTypeName().equals(DataType.TYPE_NUMBER)) {
				dataX.put(s, templateDataTable.getdc().get(s));
				dataY.put(s, templateDataTable.getdc().get(s));
				comboBoxX.getItems().add(s);
				comboBoxY.getItems().add(s);
			}
		}


	}


	/**
	 * Populate template data table values to the chart view
	 */
	private void populateDataTableValuesToPieChart(String seriesName) {

		dataT.clear();
		dataN.clear();
		comboBoxT.getItems().clear();
		comboBoxN.getItems().clear();
		Set<String> key= templateDataTable.getKeys();
	//	int i = 0;
		for(String s : key) {
			if(templateDataTable.getdc().get(s).getTypeName().equals(DataType.TYPE_NUMBER)) {
				DataColumn temp = templateDataTable.getdc().get(s);
				Number[] tempdata = (Number[]) temp.getData();
				boolean neg = false;
				for(Number i: tempdata) {
					if(i!=null &&i.doubleValue()<0) {
						neg = true;
						break;
					}
				}
				if(neg == false){
				dataN.put(s, templateDataTable.getdc().get(s));
				comboBoxN.getItems().add(s);
				}
			}
			else if(templateDataTable.getdc().get(s).getTypeName().equals(DataType.TYPE_STRING)) {
				dataT.put(s, templateDataTable.getdc().get(s));
				comboBoxT.getItems().add(s);
			}

		}

		//pieChart.setTitle(seriesName+" Pie Chart");
	}

	/**
	 * Populate template data table values to the chart view
	 */
	private void populateDataTableValuesToAnimatedChart(String seriesName) {

		dataX_A.clear();
		dataY_A.clear();
		comboBoxX_A.getItems().clear();
		comboBoxY_A.getItems().clear();
		Set<String> key= templateDataTable.getKeys();
	//	int i = 0;
		for(String s : key) {
			if(templateDataTable.getdc().get(s).getTypeName().equals(DataType.TYPE_NUMBER)) {
				dataX_A.put(s, templateDataTable.getdc().get(s));
				dataY_A.put(s, templateDataTable.getdc().get(s));
				comboBoxX_A.getItems().add(s);
				comboBoxY_A.getItems().add(s);
			}
		}


	}

	private void populateDataTableValuesToChosFilter(String seriesName) {
		if(templateDataTable == null) {
			return;
		}
		cbColomn.getSelectionModel().clearSelection();
		cbColomn.getItems().clear();
		ArrayList<String> name = new ArrayList<String>();
		name = templateDataTable.getNumericColomnNames();
		for(int i = 0; i < name.size(); i++) {
			cbColomn.getItems().add(name.get(i));
		}
		cbColomn.setValue(name.get(0));
	}

	private void populateDataTableValuesToChosSplit(String seriesName) {
		//to do
	}

	/**
	 * Initialize event handlers of the main screen.
	 */
	private void initMainScreenHandlers() {

		// click handler

		listData.setOnMouseClicked(new EventHandler<MouseEvent>() {
			 @Override
			    public void handle(MouseEvent click) {
				 if (click.getClickCount() == 1) {
	             //Use ListView's getSelected Item
					 templateSeriesName = listData.getSelectionModel().getSelectedItem();
					 //TODO
					 //check null
	             //use this to do whatever you want to. Open Link etc.
					 if(DataTableList.get(templateSeriesName)!=null) {
               templateDataTable = DataTableList.get(templateSeriesName);
	             lbDataTable.setText(String.format("DataTable: %d rows, %d columns", templateDataTable.getNumRow(),
 					templateDataTable.getNumCol()));

				 populateDataTableValuesToLineChart(templateSeriesName);
				 populateDataTableValuesToPieChart(templateSeriesName);
				 populateDataTableValuesToAnimatedChart(templateSeriesName);
				 populateDataTableValuesToChosFilter(templateSeriesName);
				 populateDataTableValuesToChosSplit(templateSeriesName);

				 }
				 }
			 }
		});

		// handler of the chart list
		listChart.setOnMouseClicked(new EventHandler<MouseEvent>() {
			 @Override
			    public void handle(MouseEvent click) {
				 if (click.getClickCount() == 2) {
	             //Use ListView's getSelected Item
//					 if(chartName == listChart.getSelectionModel().getSelectedItem()) duplicate = true;
					 chartName = listChart.getSelectionModel().getSelectedItem();
					 lineChart.setVisible(true);
					 pieChart.setVisible(true); 
	             //use this to do whatever you want to. Open Link etc.
					 if(chart.get(chartName)!=null) {
						 if(chart.get(chartName).getClass() == LineChart.class) {
							 dataX.clear();
							 dataY.clear();
							 comboBoxX.getItems().clear();
							 comboBoxY.getItems().clear();
							 LineChartData d = lineChartData.get(chartName);
							 xAxis.setLabel(d.getLabelX());
							 yAxis.setLabel(d.getLabelY());
							 lineChart.getData().clear();
							 lineChart.setAnimated(false);
							 lineChart.getData().addAll(((LineChart<Number, Number>) chart.get(chartName)).getData());
							 lineChart.setTitle(chartName+" Line Chart ");
							 putSceneOnStage(SCENE_LINE_CHART);
						 }
						 else if(chart.get(chartName).getClass() == PieChart.class) {
							 dataT.clear();
							 dataN.clear();
							 comboBoxT.getItems().clear();
							 comboBoxN.getItems().clear();
							 pieChart.getData().clear();
							 pieChart.setAnimated(false);
							 pieChart.getData().addAll(((PieChart) chart.get(chartName)).getData());
							 pieChart.setTitle(chartName+" Pie Chart ");
							 putSceneOnStage(SCENE_PIE_CHART);
						 }
					 }
					 else if(animated.get(chartName)!=null) {
						 dataX_A.clear();
						 dataY_A.clear();
						 comboBoxX_A.getItems().clear();
						 comboBoxY_A.getItems().clear();
						 LineChartData d = animatedChartData.get(chartName);
						 xAxis_A.setLabel(d.getLabelX());
						 yAxis_A.setLabel(d.getLabelY());
						 Number[] xValues = d.getX();
						 int len = xValues.length;
						 for (int i = 0; i < len; i++) {
								if(xValues[i].intValue() >= MAX) {
									MAX = xValues[i].intValue();
								}
							}
							MAX_xAxis = MAX / 3;
							animatedChart.setAnimated(false);
							xAxis_A.setUpperBound(MAX_xAxis);
							xAxis_A.setLowerBound(0);
							xAxis_A.setTickUnit(MAX_xAxis / 10);

						 animatedChart.getData().clear();
						 animatedChart.getData().addAll(((LineChart<Number, Number>) animated.get(chartName)).getData());
						 animatedChart.setTitle(chartName+" Animated Chart ");
						 timer.start();
						 putSceneOnStage(SCENE_ANIMATED_CHART);
					 }
				}
			 }
		});



		// click handler
		btLineChart.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("SampleChart");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Please enter the Chart name:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				if(itemsChart.contains(result.get())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Look, there is an error!");
					alert.setContentText("Ooops, name already exists!");

					alert.showAndWait();
				}
				else{
					chartName = result.get();
					lineChart.setTitle(result.get() + " Line Chart");
					lineChart.setVisible(false);
					plotLineChart();
				}
			}


		});

		btPieChart.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("SampleChart");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Please enter the Chart name:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				if(itemsChart.contains(result.get())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Look, there is an error!");
					alert.setContentText("Ooops, name already exists!");

					alert.showAndWait();
				}
				else{
					chartName = result.get();
					pieChart.setTitle(result.get() + " Pie Chart");
					pieChart.setVisible(false);
					plotPieChart();
				}

			}
		});
		btAnimatedChart.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog("SampleChart");
			dialog.setTitle("Text Input Dialog");
			dialog.setHeaderText("Look, a Text Input Dialog");
			dialog.setContentText("Please enter the Chart name:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				if(itemsChart.contains(result.get())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Look, there is an error!");
					alert.setContentText("Ooops, name already exists!");

					alert.showAndWait();
				}
				else{
					chartName = result.get();
					animatedChart.setTitle(result.get() + " Animated Chart");
					plotAnimatedChart();
				}
			}


		});
		btChosFilter.setOnAction(e -> {
			if(templateSeriesName == null) {
				alertbox("ERROR","please select the datatable");
			}
			if(templateDataTable!=null && templateDataTable.getNumericColomnNames() != null) {
			if(templateDataTable.getNumericColomnNames().isEmpty()) {
				alertbox("ERROR","there isn't any numeric colomn in the selected datatable");
			}
			else {
				putSceneOnStage(SCENE_CHOS_FILTER);
			}}

		});
		btChosSplit.setOnAction(e ->{
			if(templateSeriesName == null) {
				alertbox("ERROR","please select the datatable");
			}
			else {
				putSceneOnStage(SCENE_CHOS_SPLIT);
			}
		});

	}

	/**
	 * This function checks if the selected DataTable contains enough
	 * data to draw a line chart. Prompt an alert information
	 * or put up the line chart scene.
	 */
	private void plotLineChart() {

		// Ensure both columns exist and the type is number
		if (dataX.size()>=2) {
			putSceneOnStage(SCENE_LINE_CHART);
		}
		else if(templateDataTable!=null){
			if(dataX.size()==0 && dataY.size()==0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, select a dataset first!");

				alert.showAndWait();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, no enough dataset to draw a line chart!");

				alert.showAndWait();
			}

		}
		else {
			putSceneOnStage(SCENE_LINE_CHART);
		}
	}

	/**
	 * This function checks if the selected DataTable contains enough
	 * data to draw a pie chart. Prompt an alert information
	 * or put up the pie chart scene.
	 */
	private void plotPieChart() {
		// Ensure both columns exist and the type is number
		if (dataT.size()>=1 && dataN.size()>=1) {
			putSceneOnStage(SCENE_PIE_CHART);
		}
		else if(templateDataTable!=null){
			if(dataT.size()==0 && dataN.size()==0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, select a dataset first!");

				alert.showAndWait();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, no enough dataset to draw a pie chart!");

				alert.showAndWait();
			}
		}
		else {
			putSceneOnStage(SCENE_PIE_CHART);
		}
}
 /**
	 * This function checks if the selected DataTable contains enough
	 * data to draw an animated line chart. Prompt an alert information
	 * or put up the animated chart scene.
	 */
	private void plotAnimatedChart() {

		// Ensure both columns exist and the type is number
		if (dataX_A.size()>=2) {
			putSceneOnStage(SCENE_ANIMATED_CHART);
		}
		else if(templateDataTable!=null){
			if(dataX_A.size()==0 && dataY_A.size()==0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, select a dataset first!");

				alert.showAndWait();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, there is an error!");
				alert.setContentText("Ooops, no enough dataset to draw an animatd line chart!");

				alert.showAndWait();
			}

		}
		else {
			putSceneOnStage(SCENE_ANIMATED_CHART);
		}
	}

	/**
	 * Create the line chart screen and layout its UI components
	 *
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneLineChartScreen() {

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		btLineChartBackMain = new Button("Back");
		xAxis.setLabel("undefined");
		yAxis.setLabel("undefined");
		lineChart.setTitle("An empty line chart");


		Label x = new Label("X:");
		Label y = new Label("Y:");
		// Layout the UI components
	    HBox hb = new HBox(20);
		hb.getChildren().addAll(x,comboBoxX,y,comboBoxY);
		hb.setAlignment(Pos.CENTER);

		VBox container = new VBox(20);
		container.getChildren().addAll(lineChart,hb, btLineChartBackMain);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}


	/**
	 * Create the pie chart screen and layout its UI components
	 *
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane panePieChartScreen() {
		pieChart = new PieChart();
		pieChart.setLabelsVisible(true);
		btPieChartBackMain = new Button("Back");

		Label t = new Label("T:");
		Label n = new Label("N:");
		// Layout the UI components
	    HBox hb = new HBox(20);
		hb.getChildren().addAll(t,comboBoxT,n,comboBoxN);
		hb.setAlignment(Pos.CENTER);

		VBox container = new VBox(20);
		container.getChildren().addAll(pieChart,hb, btPieChartBackMain);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * Create the Animated chart screen and layout its UI components
	 *
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneAnimatedChartScreen() {

		xAxis_A = new NumberAxis();
		yAxis_A = new NumberAxis();
		animatedChart = new LineChart<Number, Number>(xAxis_A, yAxis_A);

		btLineChartBackMain_A = new Button("Back");

		xAxis_A.setLabel("undefined");
		yAxis_A.setLabel("undefined");
		animatedChart.setTitle("An empty line chart");
		xAxis_A.setForceZeroInRange(false);
		xAxis_A.setAutoRanging(false);
		xAxis_A.setTickLabelsVisible(true);
		xAxis_A.setTickMarkVisible(true);
		xAxis_A.setMinorTickVisible(true);

		Label x = new Label("X:");
		Label y = new Label("Y:");
		// Layout the UI components
	    HBox hb = new HBox(20);
		hb.getChildren().addAll(x,comboBoxX_A,y,comboBoxY_A);
		hb.setAlignment(Pos.CENTER);

		VBox container = new VBox(20);
		container.getChildren().addAll(animatedChart,hb, btLineChartBackMain_A);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");

		return pane;
	}
	private Pane paneChosFilterScreen() {

		numInput = new TextField();
		btChosFilterBackMain = new Button("Back");
		btChosFilterConfirm = new Button("Confirm");
		cbColomn = new ChoiceBox<>();
		cbSign = new ChoiceBox<>();
		cbSign.getItems().add(">");
		cbSign.getItems().add(">=");
		cbSign.getItems().add("==");
		cbSign.getItems().add("!=");
		cbSign.getItems().add("<=");
		cbSign.getItems().add("<");
		cbFReplace = new ChoiceBox<>();
		cbFReplace.getItems().add("Replace the current dataset");
		cbFReplace.getItems().add("create a new dataset");
		cbSign.setValue(">");
		cbFReplace.setValue("Replace the current dataset");
		chosnum = new Label("type an integer");
		chosColomn = new Label("choose colomn");
		chosSign = new Label("choose operator");
		chosFReplace = new Label("how to deal with the new datatable");
		Label e = new Label("");
		Label em = new Label("");
		Label emp = new Label("");


		HBox hb = new HBox(120);
		hb.getChildren().addAll(btChosFilterBackMain,btChosFilterConfirm);
		hb.setAlignment(Pos.CENTER);

		HBox hd = new HBox(50);
		hd.getChildren().addAll(chosColomn,chosSign);
		hd.setAlignment(Pos.CENTER);

		HBox he = new HBox(80);
		he.getChildren().addAll(cbColomn,cbSign);
		he.setAlignment(Pos.CENTER);

		VBox container = new VBox(20);
		container.getChildren().addAll(chosnum,numInput,hd,he,chosFReplace,cbFReplace,emp,em,e,hb);
		container.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply CSS to style the GUI components
		pane.getStyleClass().add("screen-background");
		return pane;
	}
	private Pane paneChosSplitScreen() {
		btChosSplitBackMain = new Button("Back");
		btChosSplitConfirm = new Button("Confirm");
		cbSReplace = new ChoiceBox<>();
		cbSReplace.getItems().add("Replace the current dataset");
		cbSReplace.getItems().add("create a new dataset");
		cbSReplace.setValue("Replace the current dataset");
		snumInput = new TextField();
		Label chosPer = new Label("choose the percentage of split");
		Label chosSReplace = new Label("how to deal with the new datatable");
		Label e = new Label("");
		Label em = new Label("");
		Label emp = new Label("");

		HBox hl = new HBox(150);
		hl.getChildren().addAll(btChosSplitBackMain,btChosSplitConfirm);
		hl.setAlignment(Pos.CENTER);

		VBox container = new VBox(20);
		container.getChildren().addAll(chosPer,snumInput,chosSReplace,cbSReplace,e,em,emp,hl);
		container.setAlignment(Pos.CENTER);


		BorderPane pane = new BorderPane();
		pane.setCenter(container);
		return pane;
	}

	/**
	 * Creates the main screen and layout its UI components
	 *
	 * @return a Pane component to be displayed on a scene
	 */
	private Pane paneMainScreen() throws DataTableException, FileNotFoundException, ClassNotFoundException, IOException{

		lbMainScreenTitle = new Label("COMP3111 Chart");
		lbDataFandT = new Label("Data Filtering&Transformation");

		MenuBar menuBar = new MenuBar();
        // --- Menu File
        menuFile = new Menu("File");
        /**
         * Import the data set from a .csv file.
         */
        importF = new MenuItem("Import");
        importF.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	   final FileChooser fileChooser = new FileChooser();
            	   configureFileChooser(fileChooser, "*.csv");
                   File file = fileChooser.showOpenDialog(stage);
                   if (file != null) {
                	   if(itemsData.contains(file.getName())) {
       					Alert alert = new Alert(AlertType.ERROR);
       					alert.setTitle("Error Dialog");
       					alert.setHeaderText("Look, there is an error!");
       					alert.setContentText("Ooops, file already exists!");

       					alert.showAndWait();
       				}
                	else {
                	    try {
                	    	importFile(file);
						} catch (FileNotFoundException | DataTableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                   }

            }
 });

       /**
         * Export the data in a DataTable to a .csv file.
         */
        exportF = new MenuItem("Export");
        exportF.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		if(templateDataTable==null) {
        			Alert alert = new Alert(AlertType.ERROR);
        			alert.setTitle("Error Dialog");
        			alert.setHeaderText("Look, there is an error!");
        			alert.setContentText("Ooops, no dataset is selected!");

        			alert.showAndWait();
        		}
        		else {
        		  FileChooser fileChooser = new FileChooser();
                  fileChooser.setTitle("Save File");
                  fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv", "*.csv"));
                  File file = fileChooser.showSaveDialog(stage);
                  if (file != null) {
                      try {
                          export(file);
                      } catch (IOException ex) {
                          System.out.println(ex.getMessage());
                      }
                  }
        		}
        	}

        });
        /**
         * Save the project (DataTables and charts) to a .comp3111 file.
         */
        save = new MenuItem("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		if(DataTableList == null) {
        			Alert alert = new Alert(AlertType.ERROR);
        			alert.setTitle("Error Dialog");
        			alert.setHeaderText("Look, there is an error!");
        			alert.setContentText("Ooops, nothing to save!");
        			alert.showAndWait();
        		}
        		else {
        			FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Project");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".comp3111", "*.comp3111"));
                    File file = fileChooser.showSaveDialog(stage);
                    if(file != null) {
                    	try {
                    		Save_Load saveProject = new Save_Load();
                    		saveProject.save(DataTableList, lineChartData, pieChartData, animatedChartData, file);
                    	} catch (IOException ex) {
                    		System.out.println(ex.getMessage());
                    	}
                    }
        		}
        	}
        });
        /**
         * Load a project from a .comp3111 file, reconstruct the DataTables and charts from the data.
         */
        load = new MenuItem("Load");
        load.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent t) {
        		final FileChooser fileChooser = new FileChooser();
         	    configureFileChooser(fileChooser, ".comp3111");
                File file = fileChooser.showOpenDialog(stage);
                if(file != null) {
                   	try {
                   		Save_Load sl = new Save_Load();
                   		// Load DataTable
                   		DataTableList.clear();
                   		DataTableList = sl.load(file);
                   		listData.getItems().clear();

                   		Set<Map.Entry<String, DataTable>> dtSet = DataTableList.entrySet();
                   		Iterator<Map.Entry<String, DataTable>> dtIt = dtSet.iterator();
                   		while(dtIt.hasNext()) {
                   			Map.Entry<String, DataTable> e = (Map.Entry<String, DataTable>) dtIt.next();
                   			listData.getItems().add(e.getKey());
                   		}
                      // Clear the chart list
                      listChart.getItems().clear();
                   		// Load line chart
                   		lineChartData.clear();
                   		lineChartData = sl.loadLineChart(file);
                   		Set<Map.Entry<String, LineChartData>> lSet = lineChartData.entrySet();
                   		Iterator<Map.Entry<String, LineChartData>> lIt = lSet.iterator();
                   		while(lIt.hasNext()) {
                   			Map.Entry<String, LineChartData> e = (Map.Entry<String, LineChartData>) lIt.next();
                   			LineChartData data = e.getValue();
                   			NumberAxis txAxis = new NumberAxis();
                   			NumberAxis tyAxis = new NumberAxis();
                   			txAxis.setLabel(data.getLabelX());
                   			tyAxis.setLabel(data.getLabelY());
                   			LineChart<Number,Number> tempChart = new LineChart<Number, Number>(txAxis, tyAxis);
                   			XYChart.Series<Number, Number> tseries = new XYChart.Series<>();
                   			tseries.setName(data.getSeriesName());
                   			Number[] xValues = data.getX();
                   			Number[] yValues = data.getY();
                   			int len = xValues.length;

                   			for (int i = 0; i < len; i++) {
                   				tseries.getData().add(new XYChart.Data<Number, Number>(xValues[i], yValues[i]));
                   			}

                   			tempChart.getData().add(tseries);

                			itemsChart.add(e.getKey());
                			chart.put(e.getKey(), tempChart);
                   		}
                   		// Load pie chart
                   		pieChartData.clear();
                   		pieChartData = sl.loadPieChart(file);
                   		Set<Map.Entry<String, PieChartData>> pSet = pieChartData.entrySet();
                   		Iterator<Map.Entry<String, PieChartData>> pIt = pSet.iterator();
                   		while(pIt.hasNext()) {
                   			Map.Entry<String, PieChartData> e = (Map.Entry<String, PieChartData>) pIt.next();
                   			PieChartData data = e.getValue();
                   			ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            	        	// populating the series with data
            				// As we have checked the type, it is safe to downcast to Number[]
            				Number[] nValues = data.getN();
            				String[] tValues = data.getT();

            				// In DataTable structure, both length must be the same
            				int len = tValues.length;

            				for (int i = 0; i < len; i++) {
            					pieData.add(new PieChart.Data(tValues[i],nValues[i].doubleValue()));
            				}
            				PieChart tempChart = new PieChart(pieData);
                			itemsChart.add(e.getKey());
                			chart.put(e.getKey(), tempChart);
                   		}
                   		// Load animated chart
                   		animatedChartData.clear();
                   		animatedChartData = sl.loadAnimatedChart(file);
                   		Set<Map.Entry<String, LineChartData>> aSet = animatedChartData.entrySet();
                   		Iterator<Map.Entry<String, LineChartData>> aIt = aSet.iterator();
                   		while(aIt.hasNext()) {
                   			Map.Entry<String, LineChartData> e = (Map.Entry<String, LineChartData>) aIt.next();
                   			LineChartData data = e.getValue();
                   			NumberAxis txAxis = new NumberAxis();
                   			NumberAxis tyAxis = new NumberAxis();
                   			txAxis.setLabel(data.getLabelX());
                   			tyAxis.setLabel(data.getLabelY());
                   			LineChart<Number,Number> tempChart = new LineChart<Number, Number>(txAxis, tyAxis);
                   			XYChart.Series<Number, Number> tseries = new XYChart.Series<>();
                   			tseries.setName(data.getSeriesName());
                   			Number[] xValues = data.getX();
                   			Number[] yValues = data.getY();
                   			int len = xValues.length;

                   			for (int i = 0; i < len; i++) {
                   				tseries.getData().add(new XYChart.Data<Number, Number>(xValues[i], yValues[i]));
                   			}

                   			tempChart.getData().add(tseries);
                			itemsChart.add(e.getKey());
                			animated.put(e.getKey(), tempChart);
                   		}

                   	} catch (IOException ex) {
                   		System.out.println(ex.getMessage());
                   	} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
        	}
        });
        menuFile.getItems().addAll(importF);
        menuFile.getItems().addAll(exportF);
        menuFile.getItems().addAll(save);
        menuFile.getItems().addAll(load);
        menuBar.getMenus().addAll(menuFile);
        menuBar.setMaxSize(100, 40);
        HBox hca = new HBox(20);
		hca.setAlignment(Pos.TOP_LEFT);
		hca.getChildren().addAll(menuBar);

		// Layout the UI components
		//draw chart button
		btLineChart = new Button("Line Chart");
		btPieChart = new Button("Pie Chart");
		btAnimatedChart = new Button("Animated Chart");
		btChosFilter = new Button("Filtering numeruc data");//
		btChosSplit = new Button("Randomly split dataset");//
		lbDataTable = new Label("DataTable: empty");

		HBox hcc = new HBox(20);
		hcc.setAlignment(Pos.CENTER);
		hcc.getChildren().addAll(btLineChart, btPieChart, btAnimatedChart);

		HBox hcd = new HBox(20);//
		hcd.setAlignment(Pos.CENTER);//
		hcd.getChildren().addAll(btChosFilter, btChosSplit);//

		//lists
        listData = new ListView<String>();
        itemsData =FXCollections.observableArrayList();

        listData.setItems(itemsData);
        listData.setPrefWidth(300);
        listData.setPrefHeight(100);


        listChart = new ListView<String>();
        itemsChart =FXCollections.observableArrayList();
        listChart.setItems(itemsChart);
        listChart.setPrefWidth(300);
        listChart.setPrefHeight(100);

        HBox hcb = new HBox(20);
		hcb.setAlignment(Pos.CENTER);
		hcb.getChildren().addAll(listData,listChart);

		VBox container = new VBox(20);

		container.getChildren().addAll(hca,lbMainScreenTitle, hcc,lbDataFandT,hcd,lbDataTable,new Separator(),hcb);//
		container.setAlignment(Pos.TOP_CENTER);

		BorderPane pane = new BorderPane();
		pane.setCenter(container);

		// Apply style to the GUI components
		btLineChart.getStyleClass().add("menu-button");
		btPieChart.getStyleClass().add("menu-button");
		btAnimatedChart.getStyleClass().add("menu-button");
		btChosSplit.getStyleClass().add("menu-button");
		btChosFilter.getStyleClass().add("menu-button");
		lbMainScreenTitle.getStyleClass().add("menu-title");
		lbDataFandT.getStyleClass().add("menu-title");
		pane.getStyleClass().add("screen-background");

		return pane;
	}

	/**
	 * This method is used to pick anyone of the scene on the stage. It handles the
	 * hide and show order. In this application, only one active scene should be
	 * displayed on stage.
	 *
	 * @param sceneID
	 *            - The sceneID defined above (see SCENE_XXX)
	 */
	private void putSceneOnStage(int sceneID) {

		// ensure the sceneID is valid
		if (sceneID < 0 || sceneID >= SCENE_NUM)
			return;

		stage.hide();
		stage.setTitle(SCENE_TITLES[sceneID]);
		stage.setScene(scenes[sceneID]);
		stage.setResizable(true);
		stage.show();
	}

	/**
	 * All JavaFx GUI application needs to override the start method You can treat
	 * it as the main method (i.e. the entry point) of the GUI application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {

			stage = primaryStage; // keep a stage reference as an attribute
			initScenes(); // initialize the scenes
			initEventHandlers(); // link up the event handlers
			putSceneOnStage(SCENE_MAIN_SCREEN); // show the main screen

		} catch (Exception e) {

			e.printStackTrace(); // exception handling: print the error message on the console
		}
	}

	/**
	 * main method - only use if running via command line
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

 /**
	 * This function constructs a user interface for the user to select a file
	 * in the system.
	 * @param fileChooser
	 * @param extension
	 */
	 private static void configureFileChooser(
        final FileChooser fileChooser, String extension) {
            fileChooser.setTitle("View File");
            fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(extension, "*"+extension)
            );
    }



/**
 * This method is used to import a data set from a selected .csv file, and fill the missing data
 *  with a value selected by the user (by "0", medium or mean). Then put the converted data into
 *  the chart selecting user interface.
 * @param file
 * 			- The file(.csv) selected by the user to import data from.
 * @throws DataTableException
 * @throws FileNotFoundException
 */
    private void importFile(File file) throws DataTableException, FileNotFoundException {

           //desktop.open(file);
        	DataTable newDataTable = new DataTable(file);
        	Set<String> key = newDataTable.getKeys();
        	for(String s: key) {
        		DataColumn datacol = newDataTable.getdc().get(s);
        		if(datacol.getTypeName() == DataType.TYPE_NUMBER && (datacol.contains("") || datacol.contains(null))) {
        			List<String> choices = new ArrayList<>();
        			choices.add("By 0");
        			choices.add("By medium");
        			choices.add("By mean");

        			ChoiceDialog<String> dialog = new ChoiceDialog<>("By 0", choices);
        			dialog.setTitle("Choice Dialog");
        			dialog.setHeaderText("There are missing data in Column "+s);
        			dialog.setContentText("Choose your filling method:");

        			// Traditional way to get the response value.
        			Optional<String> result = dialog.showAndWait();
        			if(result.get()=="By 0") {
        				try {
							newDataTable.fillNumCol(s, DataTable.FillType.byZero);
						} catch (DataTableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        			else if(result.get()=="By medium") {
        				try {
							newDataTable.fillNumCol(s, DataTable.FillType.byMedium);
						} catch (DataTableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        			else if(result.get()=="By mean") {
							newDataTable.fillNumCol(s, DataTable.FillType.byMean);

        			}

        		}

        	}
        	DataTableList.put(file.getName(), newDataTable);

        	listData.getItems().add(file.getName());

    }


/**
 * Export a dataTable to a csv file
 *
 * @param file
 *            - the file that the dataTable to be exported to
 * @throws IOException
 *
 */
	private void export(File filename) throws IOException {
		int numCol = templateDataTable.getNumCol();
		int numRow = templateDataTable.getNumRow();
		String[][] dataset = new String[numRow+1][2*numCol];
		Set<String> key= templateDataTable.getKeys();
		int i = 0;
		for(String s : key) {
			dataset[0][2*i] = s;
			if(i<(numCol-1))
				dataset[0][2*i+1] = ",";
			else
				dataset[0][2*i+1] = "\n";
			DataColumn dataCol = templateDataTable.getdc().get(s);
			Object[] data = dataCol.getData();
			for(int j = 1;j<=numRow;j++) {
				dataset[j][2*i] =
						data[j-1].toString();
				if(i<(numCol-1))
					dataset[j][2*i+1] = ",";
				else
					dataset[j][2*i+1] = "\n";
			}
			i++;
		}


		FileWriter fileWriter = null;
        fileWriter = new FileWriter(filename);
        for(int m = 0;m<=numRow;m++) {
    	    for(int n = 0;n<(2*numCol);n++) {
    		    fileWriter.append(dataset[m][n]);
    	    }
        }

		fileWriter.close();
	}

	private static void alertbox(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		window.setMinHeight(100);
		Label label = new Label(message);
		Button closeButton = new Button("back");
		closeButton.setOnAction(e -> window.close());
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label,closeButton);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();


	}
	private String isInt(TextField input, String message) {
		try {
				int num = Integer.parseInt(input.getText());
				return input.getText();
		}catch(NumberFormatException e) {
			alertbox("ERROR", "The number input has to be an integer");
			return "f";
		}
	}
	private String isInt100(TextField input, String message) {
		try {
				int num = Integer.parseInt(input.getText());
				if(num < 0 || num > 100) {
					alertbox("ERROR", "The number input has to between 0 and 100");
					return "f";
				}
				return input.getText();
		}catch(NumberFormatException e) {
			return "f";
		}
	}
}

