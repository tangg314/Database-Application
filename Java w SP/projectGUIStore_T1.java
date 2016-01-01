import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JPanel;


public class projectGUIStore implements ActionListener, ItemListener {

	private static Connection sharedConnection;
	private JFrame frame1;
	private JLabel lblSelectedModel;
	private JLabel lblSelectedProcessor;
	private JLabel lbl1;
	private JLabel lbl2;
	private JLabel lbl3;
	private JLabel lbl4;
	private JLabel lbl5;
	private JButton btnSearch;
	private JButton btnReset;
	private JTextArea txaSearchResult;
	private JTextArea txtrCurrentLaptopConfiguration;
	static List<String> yearList = new ArrayList<String>();
	static List<String> modelList = new ArrayList<String>();
	static List<String> processorList = new ArrayList<String>();
	static List<String> searchList = new ArrayList<String>();
	static String selectedYear = "";
	static String selectedModel = "";
	static String selectedProcessor = "";
	static String serachResult = "";
	static JComboBox<Object> cmbYear;
	static JComboBox<Object> cmbModel;
	static JComboBox<Object> cmbProcessor;
	static Connection con;
	static PreparedStatement statement;
	static ResultSet result;
	static boolean triggerEvent = false;
	long starttime1 = 0;
	long endtime1 = 0;
	long totaltime1 = 0;
	long starttime2 = 0;
	long endtime2 = 0;
	long totaltime2 = 0;
	long starttime3 = 0;
	long endtime3 = 0;
	long totaltime3 = 0;
	long starttime4 = 0;
	long endtime4 = 0;
	long totaltime4 = 0;
	long starttime5 = 0;
	long endtime5 = 0;
	long totaltime5 = 0;
	
	//Current laptop config box default phrase.
	String currentLap = "";
	
	//Number of rows in Upgrade results model.
	int rowNum = 0;
	
	//CHANGE USERNAME, PASSWORD, DATABASE.
	private String username = "321";
	private String password = "LaptopUps";
	private static String database = "jdbc:mysql://98.155.138.248/321 pro1";
	
	//Upgrade results table
	private JTable table;
	
	//Current Laptop config table
	private JTable table_1;
	
	//Upgrade results table
	static Object[][] databaseInfo;
	static Object[] columns = {"Processor", "Model", "Speed", "Cache", "Instruction", "Cores"};
			
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					projectGUIStore window = new projectGUIStore();
					window.frame1.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	//Models MySQL data for Upgrade results table.
	static DefaultTableModel dTableModel = new DefaultTableModel(databaseInfo, columns)
	{
		public Class getColumnClass(int column)
		{
			Class returnValue;
			
			if((column >= 0) && (column < getColumnCount()))
			{
				returnValue = getValueAt(0, column).getClass();
			}
			
			else
			{
				returnValue = Object.class;
			}
			
			return returnValue;
		}
	};

	static DefaultTableModel dTableModel2 = new DefaultTableModel(databaseInfo, columns);
	
	

	/**
	 * Connect to the database
	 */	
	public static Connection createOrAccessConnection(String user,String pass){ 
		long startTime = System.nanoTime();
		
		Connection connect = sharedConnection;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			if(connect == null)
			{ 
				connect = DriverManager.getConnection(database,user,pass);
				if(sharedConnection == null ) 
				{ 
					sharedConnection = connect; 
				}  
				
				long endTime = System.nanoTime();
				
				System.out.println("Time connecting to database: " + (endTime - startTime) / 1000000 + " ms");
			}
			return connect;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public projectGUIStore() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		//Frame
		frame1 = new JFrame("LENOVO: PROCESSOR UPGRADES (SP)");
		frame1.setBounds(100, 100, 550, 600);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);

		//Connect to the database
		con = createOrAccessConnection(username, password);

		//Default data for cmbYear
		starttime1 = System.nanoTime();
		statement = con.prepareStatement("{call cmbYear()}");					
		result = statement.executeQuery();

		while(result.next())
		{
			yearList.add(result.getString(1));
		}	

		//Default data for cmbModel
		statement = con.prepareStatement("{call cmbModel()}");					
		result = statement.executeQuery();

		while(result.next())
		{
			modelList.add(result.getString(1));
		}

		//Default data for cmbProcessor
		statement = con.prepareStatement("{call cmbProcessor()}");					
		result = statement.executeQuery();

		while(result.next())
		{
			processorList.add(result.getString(1));
		}
		endtime1 = System.nanoTime();
		totaltime1 = endtime1 - starttime1;

		//ComboBox(Year)
		lbl1 = new JLabel("Year:");
		lbl1.setBounds(21, 11, 64, 20);
		frame1.getContentPane().add(lbl1);

		cmbYear = new JComboBox<Object>(yearList.toArray());
		cmbYear.insertItemAt("Select One", 0);
		cmbYear.setSelectedIndex(0);
		cmbYear.addItemListener(this);
		cmbYear.setBounds(21, 31, 145, 20);
		frame1.getContentPane().add(cmbYear);

		//ComboBox(Model)
		lbl2 = new JLabel("Model:");
		lbl2.setBounds(21, 62, 64, 20);
		frame1.getContentPane().add(lbl2);

		cmbModel = new JComboBox<Object>(modelList.toArray());
		cmbModel.insertItemAt("Select One", 0);
		cmbModel.setSelectedIndex(0);
		cmbModel.addItemListener(this);
		cmbModel.setBounds(21, 83, 145, 20);
		frame1.getContentPane().add(cmbModel);

		//ComboBox(Processor)
		lbl3 = new JLabel("Processor:");
		lbl3.setBounds(21, 114, 64, 20);
		frame1.getContentPane().add(lbl3);

		cmbProcessor = new JComboBox<Object>(processorList.toArray());
		cmbProcessor.insertItemAt("Select One", 0);
		cmbProcessor.setSelectedIndex(0);
		cmbProcessor.addItemListener(this);
		cmbProcessor.setBounds(21, 136, 145, 20);		
		frame1.getContentPane().add(cmbProcessor);

		//Label (Selected Model)
		lbl4 = new JLabel("Selected Model:");
		lbl4.setBounds(305, 31, 145, 20);
		frame1.getContentPane().add(lbl4);

		lblSelectedModel = new JLabel("None");
		lblSelectedModel.setBounds(305, 51, 145, 20);
		frame1.getContentPane().add(lblSelectedModel);

		//Label (Selected Processor)
		lbl5 = new JLabel("Selected Processor:");
		lbl5.setBounds(305, 83, 145, 20);
		frame1.getContentPane().add(lbl5);

		lblSelectedProcessor = new JLabel("None");
		lblSelectedProcessor.setBounds(305, 103, 145, 20);
		frame1.getContentPane().add(lblSelectedProcessor);

		//Button (Search)
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(this);
		btnSearch.setBounds(122, 175, 84, 34);
		frame1.getContentPane().add(btnSearch);

		//Button (Reset)
		btnReset = new JButton("Reset");
		btnReset.addActionListener(this);
		btnReset.setBounds(328, 175, 84, 34);
		frame1.getContentPane().add(btnReset);
		
		//Upgrade results table
		JTable table = new JTable(dTableModel);
		table.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(21, 332, 492, 218);
		frame1.getContentPane().add(scrollPane);
		
		//Current Laptop config table
		JScrollPane scrollPane_1;
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(21, 253, 492, 41);
		frame1.getContentPane().add(scrollPane_1);		
		table_1 = new JTable(dTableModel2);
		scrollPane_1.setViewportView(table_1);
		
		//Current Laptop config title bar
		currentLap = "Current Laptop Configuration for: " ;
		txtrCurrentLaptopConfiguration = new JTextArea();
		txtrCurrentLaptopConfiguration.setText(currentLap);
		txtrCurrentLaptopConfiguration.setBounds(21, 232, 492, 22);
		frame1.getContentPane().add(txtrCurrentLaptopConfiguration);

			
		triggerEvent = true;

	}

	/**
	 * Update ComboBox
	 */	
	public void updateCmb(JComboBox<Object> cmb, List<String> list){
		cmb.removeAllItems();
		for(String item:list){
			cmb.addItem(item);
		}
	}

	/**
	 * Item selected in ComboBox
	 */	
	public void itemStateChanged(ItemEvent e){

		//If item in cmbYear is selected
		if(e.getSource() == cmbYear && triggerEvent == true){
			if (e.getStateChange() == ItemEvent.SELECTED) {
				triggerEvent = false;
				//Get the year selected
				JComboBox<?> newcb = (JComboBox<?>)e.getSource();
				selectedYear = (String)newcb.getSelectedItem();

				//Clear the list
				modelList.clear();
				processorList.clear();
				
				try {
					//Connect to database
					con = createOrAccessConnection(username, password);

					//Get model data from database
					starttime2 = System.nanoTime();
					statement = con.prepareStatement("{call getModel(?)}");
					statement.setString(1, selectedYear);
					result = statement.executeQuery();

					while(result.next())
					{
						modelList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("{call getProcessor(?)}");
					statement.setString(1, selectedYear);
					result = statement.executeQuery();

					while(result.next())
					{
						processorList.add(result.getString(1));
					}
					endtime2 = System.nanoTime();
					totaltime2 = endtime2 - starttime2;
					
					//Insert values into ComboBox
					updateCmb(cmbModel, modelList);
					updateCmb(cmbProcessor, processorList);
					
					cmbModel.insertItemAt("Select One", 0);
					cmbProcessor.insertItemAt("Select One", 0);

					//Starting index;
					cmbModel.setSelectedIndex(0);
					cmbProcessor.setSelectedIndex(0);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				triggerEvent = true;
			}
		}

		//If item in cmbModel is selected
		if(e.getSource() == cmbModel && triggerEvent == true){
			if (e.getStateChange() == ItemEvent.SELECTED) {

				triggerEvent = false;

				//Get the model selected
				JComboBox<?> newcb = (JComboBox<?>)e.getSource();
				selectedModel = (String)newcb.getSelectedItem();

				lblSelectedModel.setText(selectedModel);

				//Clear the list
				yearList.clear();
				processorList.clear();

				try {
					//Connect to database
					con = createOrAccessConnection(username, password);

					//Get year data from database
					starttime3 = System.nanoTime();
					statement = con.prepareStatement("{call getYear(?)}");
					statement.setString(1, selectedModel);
					result = statement.executeQuery();

					while(result.next())
					{
						yearList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("{call getProcessor3(?)}");
					statement.setString(1, selectedModel);
					result = statement.executeQuery();

					while(result.next())
					{
						processorList.add(result.getString(1));
					}
					endtime3 = System.nanoTime();
					totaltime3 = endtime3 - starttime3;
					
					
					//Insert values into ComboBox
					updateCmb(cmbYear, yearList);
					updateCmb(cmbProcessor, processorList);					
					cmbYear.insertItemAt("Select One", 0);
					cmbProcessor.insertItemAt("Select One", 0);

					//Starting index
					cmbYear.setSelectedIndex(0);
					cmbProcessor.setSelectedIndex(0);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				triggerEvent = true;
			}
		}

		//If item in cmbProcessor is selected
		if(e.getSource() == cmbProcessor && triggerEvent == true){
			if (e.getStateChange() == ItemEvent.SELECTED) {
				triggerEvent = false;
				//Get the model selected
				JComboBox<?> newcb = (JComboBox<?>)e.getSource();
				selectedProcessor = (String)newcb.getSelectedItem();

				lblSelectedProcessor.setText(selectedProcessor);

				//Clear the list
				yearList.clear();
				modelList.clear();

				try {
					//Connect to database
					con = createOrAccessConnection(username, password);

					//Get year data from database
					starttime4 = System.nanoTime();
					statement = con.prepareStatement("{call getYear2(?)}");
					statement.setString(1, selectedProcessor);
					result = statement.executeQuery();

					while(result.next())
					{
						yearList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("{call getProcessor2(?)}");
					statement.setString(1, selectedProcessor);
					result = statement.executeQuery();

					while(result.next())
					{
						modelList.add(result.getString(1));
					}
					endtime4 = System.nanoTime();
					totaltime4 = endtime4 - starttime4;
					
					//Insert values into ComboBox
					updateCmb(cmbYear, yearList);
					updateCmb(cmbModel, modelList);
					cmbYear.insertItemAt("Select One", 0);
					cmbModel.insertItemAt("Select One", 0);
					
					//Starting index
					cmbYear.setSelectedIndex(0);
					cmbModel.setSelectedIndex(0);

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				triggerEvent = true;
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		//Button (Search)
		if(e.getSource() == btnSearch){
			//Error message if no information for model and processor
			if(lblSelectedModel.getText().equals("None") || lblSelectedProcessor.getText().equals("None")){
				JOptionPane.showMessageDialog(null, "Must selected both model and processor", "Error!", JOptionPane.ERROR_MESSAGE);
			}
			else{
				//Clear Current Laptop config box
				dTableModel2.setRowCount(0);
				
				//Clear Update results table
				rowNum = dTableModel.getRowCount();			
				
				for(int i = 1; i <= rowNum; i++)
				{				
					dTableModel.removeRow(0);
				}
				
				//Clear Current Laptop config box
				dTableModel2.setRowCount(0);
				
				//Get the model and processor
				selectedModel = lblSelectedModel.getText();
				selectedProcessor = lblSelectedProcessor.getText();

				//Connect to database
				con = createOrAccessConnection(username, password);

				try {
					//Get selected processor information
					starttime5 = System.nanoTime();
					statement = con.prepareStatement("{call getProcessorInfo(?)}");
					statement.setString(1, selectedProcessor);
					result = statement.executeQuery();
					
					//Current laptop config results
					Object [] tempRow2;
					while(result.next())
					{
						tempRow2 = new Object[]{result.getString(1), result.getString(2), result.getString(3), result.getString(4), 
								               result.getString(5), result.getString(6)};
						
						dTableModel2.addRow(tempRow2);
					}

					//Prepare stored procedure call
					statement = con.prepareCall("{call UpProcessor(?, ?)}");
					
					//Set parameters					
					statement.setString(1, selectedProcessor);
					statement.setString(2, selectedModel);
					
					//Execute query
					result = statement.executeQuery();
														
					//Upgrade results
					Object [] tempRow;
					while(result.next())
					{
						tempRow = new Object[]{result.getString(1), result.getString(2), result.getString(3), result.getString(4), 
								               result.getString(5), result.getString(6)};
						
						dTableModel.addRow(tempRow);
					}
					endtime5 = System.nanoTime();
					System.out.println("Time from connecting to database till update the result (with store procedure): " + (totaltime1 + totaltime2 + totaltime3 + totaltime4 + totaltime5) / 1000000 + " ms");
					
					//Update current laptop config box
					txtrCurrentLaptopConfiguration.setText(currentLap + selectedModel);
					txtrCurrentLaptopConfiguration.setBounds(21, 232, 492, 22);
					frame1.getContentPane().add(txtrCurrentLaptopConfiguration);

					reset();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}								
			}
		}
		//Button (Reset)
		if(e.getSource() == btnReset){
			reset();
			
			//Clear Update results table
			rowNum = dTableModel.getRowCount();			
			
			for(int i = 1; i <= rowNum; i++)
			{				
				dTableModel.removeRow(0);
			}
			
			//Clear Current Laptop config box
			dTableModel2.setRowCount(0);
			
			//Update current laptop config box
			txtrCurrentLaptopConfiguration.setText(currentLap);
			txtrCurrentLaptopConfiguration.setBounds(21, 232, 492, 22);
			frame1.getContentPane().add(txtrCurrentLaptopConfiguration);
		}
	}
	
	public void reset(){
		try{

			triggerEvent = false;

			//Clear the list
			yearList.clear();
			modelList.clear();
			processorList.clear();

			//Connect to the database
			con = createOrAccessConnection(username, password);

			//Default data for cmbYear
			statement = con.prepareStatement("{call cmbYear()}");					
			result = statement.executeQuery();

			while(result.next())
			{
				yearList.add(result.getString(1));
			}	

			//Default data for cmbModel
			statement = con.prepareStatement("{call cmbModel()}");					
			result = statement.executeQuery();

			while(result.next())
			{
				modelList.add(result.getString(1));
			}

			//Default data for cmbProcessor
			statement = con.prepareStatement("{call cmbProcessor()}");					
			result = statement.executeQuery();

			while(result.next())
			{
				processorList.add(result.getString(1));
			}

			//Update ComboBox
			updateCmb(cmbYear, yearList);
			updateCmb(cmbModel, modelList);
			updateCmb(cmbProcessor, processorList);
			cmbYear.insertItemAt("Select One", 0);
			cmbModel.insertItemAt("Select One", 0);
			cmbProcessor.insertItemAt("Select One", 0);

			//Starting index
			cmbYear.setSelectedIndex(0);
			cmbModel.setSelectedIndex(0);
			cmbProcessor.setSelectedIndex(0);
			//Reset Label
			lblSelectedModel.setText("None");
			lblSelectedProcessor.setText("None");

		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}

		triggerEvent = true;
	}
}
