package core.comp3111;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.chart.Chart;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class Save_Load {
	
	/**
	 * Serialize the data of the DataTables, line charts, pie charts, animated charts
	 * to a external .comp3111 file
	 * @param dt
	 * 			- the HashMap object storing all DataTables
	 * @param line
	 * 			- the HashMap object storing all line charts
	 * @param pie
	 * 			- the HashMap object storing all pie charts
	 * @param animated
	 * 			- the HashMap object storing all animated charts
	 * @param file
	 * 			- the file to save the data
	 * @throws IOException
	 * 			- throws IOException when the selected file is invalid or not existent
	 */
	public void save(HashMap<String, DataTable> dt, HashMap<String, LineChartData> line, HashMap<String, PieChartData> pie, HashMap<String, LineChartData> animated, File file) throws IOException{
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dt);
			out.writeObject(line);
			out.writeObject(pie);
			out.writeObject(animated);
			out.close();
			fileOut.close();
			System.out.printf("Project Saved");
	}
	
	/**
	 * Deserialize the data of DataTables from an external .comp3111 file
	 * @param file
	 * 			- the file to retrieve data from
	 * @return a HashMap storing the data of all DataTables
	 * @throws IOException
	 * 			- throws IOException when the file is invalid or not existent
	 * @throws ClassNotFoundException
	 * 			- throws ClassNotFoundException when the cast between objects 
	 * 			  is invalid
	 */
	public HashMap<String, DataTable> load(File file) throws IOException, ClassNotFoundException{
	    HashMap<String, DataTable> dt = null;
	         FileInputStream fis = new FileInputStream(file);
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         dt = (HashMap<String, DataTable>) ois.readObject();
	         ois.close();
	         fis.close();
	     
	      System.out.println("Deserialized Project");
	      return dt;
	  }
	
	/**
	 * Deserialize the data of line charts from an external .comp3111 file
	 * @param file
	 * 			- the file to retrieve data from
	 * @return a HashMap storing the data of all line charts
	 * @throws IOException
	 * 			- throws IOException when the file is invalid or not existent
	 * @throws ClassNotFoundException
	 * 			- throws ClassNotFoundException when the cast between objects 
	 * 			  is invalid
	 */
	public HashMap<String, LineChartData> loadLineChart(File file) throws ClassNotFoundException, IOException {
		HashMap<String, LineChartData> line = null;
	         FileInputStream fileIn = new FileInputStream(file);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         in.readObject();
	         line = (HashMap<String, LineChartData>) in.readObject();
	         in.close();
	         fileIn.close();
	         return line;
	     
	}
	
	/**
	 * Deserialize the data of pie charts from an external .comp3111 file
	 * @param file
	 * 			- the file to retrieve data from
	 * @return a HashMap storing the data of all pie charts
	 * @throws IOException
	 * 			- throws IOException when the file is invalid or not existent
	 * @throws ClassNotFoundException
	 * 			- throws ClassNotFoundException when the cast between objects 
	 * 			  is invalid
	 */
	public HashMap<String, PieChartData> loadPieChart(File file) throws IOException, ClassNotFoundException {
		HashMap<String, PieChartData> pie = null;
	         FileInputStream fileIn = new FileInputStream(file);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         in.readObject();
	         in.readObject();
	         pie = (HashMap<String, PieChartData>) in.readObject();
	         in.close();
	         fileIn.close();
	         return pie;
	}
	
	/**
	 * Deserialize the data of animated charts from an external .comp3111 file
	 * @param file
	 * 			- the file to retrieve data from
	 * @return a HashMap storing the data of all animated charts
	 * @throws IOException
	 * 			- throws IOException when the file is invalid or not existent
	 * @throws ClassNotFoundException
	 * 			- throws ClassNotFoundException when the cast between objects 
	 * 			  is invalid
	 */
	public HashMap<String, LineChartData> loadAnimatedChart(File file) throws ClassNotFoundException, IOException {
		HashMap<String, LineChartData> animated = null;
	         FileInputStream fileIn = new FileInputStream(file);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         in.readObject();
	         in.readObject();
	         in.readObject();
	         animated = (HashMap<String, LineChartData>) in.readObject();
	         in.close();
	         fileIn.close();
	         return animated;
	}
		
}
