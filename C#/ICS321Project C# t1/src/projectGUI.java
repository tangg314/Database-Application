import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class projectGUI implements ActionListener, ItemListener {

	private static Connection sharedConnection;
	private JFrame frame;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					projectGUI window = new projectGUI();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Connect to the database
	 */	
	public static Connection createOrAccessConnection(String user,String pass){ 
		Connection connect = sharedConnection;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			if(connect == null)
			{ 
				connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/321project",user,pass);
				if(sharedConnection == null ) 
				{ 
					sharedConnection = connect; 
				}  
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
	public projectGUI() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		//Frame
		frame = new JFrame();
		frame.setBounds(100, 100, 550, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//Connect to the database
		con = createOrAccessConnection("lapho", "12345");

		//Default data for cmbYear
		statement = con.prepareStatement("SELECT DISTINCT year FROM laptop ORDER BY year");					
		result = statement.executeQuery();

		while(result.next())
		{
			yearList.add(result.getString(1));
		}	

		//Default data for cmbModel
		statement = con.prepareStatement("SELECT DISTINCT lmodel FROM laptop ORDER BY lmodel");					
		result = statement.executeQuery();

		while(result.next())
		{
			modelList.add(result.getString(1));
		}

		//Default data for cmbProcessor
		statement = con.prepareStatement("SELECT DISTINCT pmodel FROM laptop ORDER BY pmodel");					
		result = statement.executeQuery();

		while(result.next())
		{
			processorList.add(result.getString(1));
		}

		//ComboBox(Year)
		lbl1 = new JLabel("Year:");
		lbl1.setBounds(21, 11, 64, 20);
		frame.getContentPane().add(lbl1);

		cmbYear = new JComboBox<Object>(yearList.toArray());
		cmbYear.insertItemAt("Select One", 0);
		cmbYear.setSelectedIndex(0);
		cmbYear.addItemListener(this);
		cmbYear.setBounds(21, 31, 145, 20);
		frame.getContentPane().add(cmbYear);

		//ComboBox(Model)
		lbl2 = new JLabel("Model:");
		lbl2.setBounds(21, 62, 64, 20);
		frame.getContentPane().add(lbl2);

		cmbModel = new JComboBox<Object>(modelList.toArray());
		cmbModel.insertItemAt("Select One", 0);
		cmbModel.setSelectedIndex(0);
		cmbModel.addItemListener(this);
		cmbModel.setBounds(21, 83, 145, 20);
		frame.getContentPane().add(cmbModel);

		//ComboBox(Processor)
		lbl3 = new JLabel("Processor:");
		lbl3.setBounds(21, 114, 64, 20);
		frame.getContentPane().add(lbl3);

		cmbProcessor = new JComboBox<Object>(processorList.toArray());
		cmbProcessor.insertItemAt("Select One", 0);
		cmbProcessor.setSelectedIndex(0);
		cmbProcessor.addItemListener(this);
		cmbProcessor.setBounds(21, 136, 145, 20);		
		frame.getContentPane().add(cmbProcessor);

		//Label (Selected Model)
		lbl4 = new JLabel("Selected Model:");
		lbl4.setBounds(305, 31, 145, 20);
		frame.getContentPane().add(lbl4);

		lblSelectedModel = new JLabel("None");
		lblSelectedModel.setBounds(305, 51, 145, 20);
		frame.getContentPane().add(lblSelectedModel);

		//Label (Selected Processor)
		lbl5 = new JLabel("Selected Processor:");
		lbl5.setBounds(305, 83, 145, 20);
		frame.getContentPane().add(lbl5);

		lblSelectedProcessor = new JLabel("None");
		lblSelectedProcessor.setBounds(305, 103, 145, 20);
		frame.getContentPane().add(lblSelectedProcessor);

		//Button (Search)
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(this);
		btnSearch.setBounds(122, 175, 84, 34);
		frame.getContentPane().add(btnSearch);

		//Button (Reset)
		btnReset = new JButton("Reset");
		btnReset.addActionListener(this);
		btnReset.setBounds(328, 175, 84, 34);
		frame.getContentPane().add(btnReset);

		//TextArea
		txaSearchResult = new JTextArea();
		txaSearchResult.setEditable(false);
		txaSearchResult.setBounds(21, 220, 490, 330);
		frame.getContentPane().add(txaSearchResult);

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
					con = createOrAccessConnection("lapho", "12345");

					//Get model data from database
					statement = con.prepareStatement("SELECT DISTINCT lmodel FROM laptop WHERE year = " + selectedYear);
					result = statement.executeQuery();

					while(result.next())
					{
						modelList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("SELECT DISTINCT pmodel FROM laptop WHERE year = " + selectedYear);
					result = statement.executeQuery();

					while(result.next())
					{
						processorList.add(result.getString(1));
					}

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
					con = createOrAccessConnection("lapho", "12345");

					//Get year data from database
					statement = con.prepareStatement("SELECT DISTINCT year FROM laptop WHERE lmodel = '" + selectedModel + "'");
					result = statement.executeQuery();

					while(result.next())
					{
						yearList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("SELECT DISTINCT pmodel FROM laptop WHERE lmodel = '" + selectedModel + "'");
					result = statement.executeQuery();

					while(result.next())
					{
						processorList.add(result.getString(1));
					}

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
					con = createOrAccessConnection("lapho", "12345");

					//Get year data from database
					statement = con.prepareStatement("SELECT DISTINCT year FROM laptop WHERE pmodel = '" + selectedProcessor + "'");
					result = statement.executeQuery();

					while(result.next())
					{
						yearList.add(result.getString(1));
					}

					//Get processor data from database
					statement = con.prepareStatement("SELECT DISTINCT lmodel FROM laptop WHERE pmodel = '" + selectedProcessor + "'");
					result = statement.executeQuery();

					while(result.next())
					{
						modelList.add(result.getString(1));
					}
					
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
				//Get the model and processor
				selectedModel = lblSelectedModel.getText();
				selectedProcessor = lblSelectedProcessor.getText();

				//Connect to database
				con = createOrAccessConnection("lapho", "12345");

				try {
					//Get selected processor information
					statement = con.prepareStatement("SELECT name, pmodel, speed, cache, instruction, cores FROM processor WHERE pmodel = '"
							+ selectedProcessor + "'");
					result = statement.executeQuery();

					serachResult = "Current CPU:\nProcessor\tModel\tSpeed\tCache\tInstruction\tCores\n";

					//Insert the current processor information into string
					while(result.next())
					{
						serachResult = serachResult + result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3)
								+ "\t" + result.getString(4) + "\t" + result.getString(5) + "\t" + result.getString(6) + "\n";
					}

					//Get the available upgrade processor information
					statement = con.prepareStatement("SELECT name, pmodel, speed, cache, instruction, cores FROM processor WHERE speed > "
							+ "(SELECT speed FROM processor WHERE pmodel = '" + selectedProcessor + "') AND (IFNULL(socket1, ' ') = (SELECT socket "
							+ "FROM laptop WHERE lmodel = '" + selectedModel + "' AND pmodel = '" + selectedProcessor + "') OR IFNULL(socket2"
							+ ", ' ') = (SELECT socket FROM laptop WHERE lmodel = '" + selectedModel + "' AND pmodel = '" + selectedProcessor
							+ "')) AND (IFNULL(chipset1, ' ') = (SELECT chipset FROM laptop WHERE lmodel = '" + selectedModel + "' AND pmodel "
							+ "= '" + selectedProcessor + "') OR IFNULL(chipset2, ' ') = (SELECT chipset FROM laptop WHERE lmodel = '" 
							+ selectedModel + "' AND pmodel = '" + selectedProcessor + "'))");
					result = statement.executeQuery();

					serachResult = serachResult + "\nAvailable upgrade:\nProcessor\tModel\tSpeed\tCache\tInstruction\tCores\n";

					//Insert the available upgrade processor information
					while(result.next())
					{
						serachResult = serachResult + result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3)
								+ "\t" + result.getString(4) + "\t" + result.getString(5) + "\t" + result.getString(6) + "\n";
					}	

					//Insert the string into TextArea
					txaSearchResult.setText(serachResult);

					reset();

				} catch (SQLException e1) {
					e1.printStackTrace();
				}								
			}
		}
		//Button (Reset)
		if(e.getSource() == btnReset){
			reset();
			txaSearchResult.setText("");
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
			con = createOrAccessConnection("lapho", "12345");

			//Default data for cmbYear
			statement = con.prepareStatement("SELECT DISTINCT year FROM laptop ORDER BY year");					
			result = statement.executeQuery();

			while(result.next())
			{
				yearList.add(result.getString(1));
			}	

			//Default data for cmbModel
			statement = con.prepareStatement("SELECT DISTINCT lmodel FROM laptop ORDER BY lmodel");					
			result = statement.executeQuery();

			while(result.next())
			{
				modelList.add(result.getString(1));
			}

			//Default data for cmbProcessor
			statement = con.prepareStatement("SELECT DISTINCT pmodel FROM laptop ORDER BY pmodel");					
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
