using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Data.Common;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Diagnostics;

namespace ICS321Project
{
    public partial class CPUUpgrade : Form
    {
        private MySqlConnection connection;
        private String server;
        private String database;
        private String uid;
        private String password;
        private String getYearSQL;
        private String getModelSQL;
        private String getProcessorSQL;
        private String getCurrentConfig;
        private String selectedModel = "NONE";
        private String selectedProcessor = "NONE";
        private MySqlDataAdapter yearAdapter;
        private MySqlDataAdapter modelAdapter;
        private MySqlDataAdapter processorAdapter;
        private MySqlDataAdapter processorUpgradesAdapter;
        private MySqlDataAdapter currentConfigAdapter;
        private DataSet yearDS;
        private DataSet modelDS;
        private DataSet processorDS;
        private DataTable upgradeTable;
        private DataTable currentConfigTable;
        private BindingSource bsource;
        private TimeSpan ts1;
        private TimeSpan ts2;
        private TimeSpan ts3;
        private TimeSpan ts4;
        private TimeSpan ts5;
        private TimeSpan ts6;
       
        private bool triggerEvent = false;

        private void databaseConnection()
        {
            //Connection time
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start();
            server = "98.155.138.248";
            database = "321 pro1";
            uid = "321";
            password = "LaptopUps";
            string connectionString;
            connectionString = "SERVER=" + server + ";" + "DATABASE=" +
            database + ";" + "UID=" + uid + ";" + "PASSWORD=" + password + ";";

            connection = new MySqlConnection(connectionString);
            stopWatch.Stop();
            TimeSpan ts = stopWatch.Elapsed;
            Console.WriteLine("Time connecting to database: " + ts.Milliseconds + " ms");
        }

        public CPUUpgrade()
        {
            InitializeComponent();
        }

        private void CPUUpgrade_Load(object sender, EventArgs e)
        {
            this.databaseConnection();

            //Time1
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start();
            getYearSQL = "SELECT DISTINCT year FROM laptop ORDER BY year";
            getModelSQL = "SELECT DISTINCT lmodel FROM laptop ORDER BY lmodel";
            getProcessorSQL = "SELECT DISTINCT pmodel FROM laptop ORDER BY pmodel";

            yearAdapter = new MySqlDataAdapter(getYearSQL, connection);
            modelAdapter = new MySqlDataAdapter(getModelSQL, connection);
            processorAdapter = new MySqlDataAdapter(getProcessorSQL, connection);

            yearDS = new DataSet();
            modelDS = new DataSet();
            processorDS = new DataSet();
            try
            {
                yearAdapter.Fill(yearDS);
                modelAdapter.Fill(modelDS);
                processorAdapter.Fill(processorDS);
                stopWatch.Stop();
                ts1 = stopWatch.Elapsed;

                cmbYear.DisplayMember = "year";
                cmbYear.DataSource = yearDS.Tables[0];

                cmbModel.DisplayMember = "lmodel";
                cmbModel.DataSource = modelDS.Tables[0];

                cmbProcessor.DisplayMember = "pmodel";
                cmbProcessor.DataSource = processorDS.Tables[0];

                cmbYear.SelectedIndex = -1;
                cmbModel.SelectedIndex = -1;
                cmbProcessor.SelectedIndex = -1;

                lblSelectedModel.Text = selectedModel;
                lblSelectedProcessor.Text = selectedProcessor;

                triggerEvent = true;
            }

            catch (MySqlException ex)
            {

            }
        }

        private void cmbYear_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (triggerEvent == true)
            {
                triggerEvent = false;

                //Time 2
                Stopwatch stopWatch = new Stopwatch();
                stopWatch.Start(); 
                getModelSQL = "SELECT DISTINCT lmodel FROM laptop WHERE year = " + cmbYear.Text;
                getProcessorSQL = "SELECT DISTINCT pmodel FROM laptop WHERE year = " + cmbYear.Text;

                modelAdapter = new MySqlDataAdapter(getModelSQL, connection);
                processorAdapter = new MySqlDataAdapter(getProcessorSQL, connection);

                modelDS = new DataSet();
                processorDS = new DataSet();

                try
                {
                    modelAdapter.Fill(modelDS);
                    processorAdapter.Fill(processorDS);
                    stopWatch.Stop();
                    ts2 = stopWatch.Elapsed;

                    cmbModel.DisplayMember = "lmodel";
                    cmbModel.DataSource = modelDS.Tables[0];
                    cmbProcessor.DisplayMember = "pmodel";
                    cmbProcessor.DataSource = processorDS.Tables[0];

                    cmbModel.SelectedIndex = -1;
                    cmbProcessor.SelectedIndex = -1;
                }

                catch(MySqlException ex)
                {

                }

                triggerEvent = true;
            }
        }

        private void cmbModel_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (triggerEvent == true)
            {
                triggerEvent = false;

                //Time3
                Stopwatch stopWatch = new Stopwatch();
                stopWatch.Start();
                getYearSQL = "SELECT DISTINCT year FROM laptop WHERE lmodel = '" + cmbModel.Text + "'";
                getProcessorSQL = "SELECT DISTINCT pmodel FROM laptop WHERE lmodel = '" + cmbModel.Text + "'";

                yearAdapter = new MySqlDataAdapter(getYearSQL, connection);
                processorAdapter = new MySqlDataAdapter(getProcessorSQL, connection);

                yearDS = new DataSet();
                processorDS = new DataSet();

                yearAdapter.Fill(yearDS);
                processorAdapter.Fill(processorDS);
                stopWatch.Stop();
                ts3 = stopWatch.Elapsed;

                cmbYear.DisplayMember = "year";
                cmbYear.DataSource = yearDS.Tables[0];
                cmbProcessor.DisplayMember = "pmodel";
                cmbProcessor.DataSource = processorDS.Tables[0];

                cmbYear.SelectedIndex = -1;
                cmbProcessor.SelectedIndex = -1;

                selectedModel = cmbModel.Text;

                lblSelectedModel.Text = selectedModel;

                triggerEvent = true;
            }
        }

        private void cmbProcessor_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (triggerEvent == true)
            {
                triggerEvent = false;

                //Time4
                Stopwatch stopWatch = new Stopwatch();
                stopWatch.Start();
                getYearSQL = "SELECT DISTINCT year FROM laptop WHERE pmodel = '" + cmbProcessor.Text + "'";
                getModelSQL = "SELECT DISTINCT lmodel FROM laptop WHERE pmodel = '" + cmbProcessor.Text + "'";

                yearAdapter = new MySqlDataAdapter(getYearSQL, connection);
                modelAdapter = new MySqlDataAdapter(getModelSQL, connection);

                yearDS = new DataSet();
                modelDS = new DataSet();

                yearAdapter.Fill(yearDS);
                modelAdapter.Fill(modelDS);
                stopWatch.Stop();
                ts4 = stopWatch.Elapsed;

                cmbYear.DisplayMember = "year";
                cmbYear.DataSource = yearDS.Tables[0];
                cmbModel.DisplayMember = "lmodel";
                cmbModel.DataSource = modelDS.Tables[0];

                cmbYear.SelectedIndex = -1;
                cmbModel.SelectedIndex = -1;

                selectedProcessor = cmbProcessor.Text;

                lblSelectedProcessor.Text = selectedProcessor;

                triggerEvent = true;
            }
        }

        //Clicking search butotn, retrieves Processor upgrades and displays current laptop configuration.
        private void SearchButton(object sender, EventArgs e)
        {
            if (triggerEvent == true)
            {
            triggerEvent = true;

            //Time5
            Stopwatch stopWatch = new Stopwatch();
            stopWatch.Start(); 
            getProcessorSQL = "SELECT name, pmodel, speed, cache, instruction, cores " +
                              "FROM processor " +
                              "WHERE speed > (SELECT speed " +
                                              "FROM processor " +
                                              "WHERE pmodel = '" + selectedProcessor + "') " +
                               "AND " +
                               "(socket1 = (SELECT socket " +
                                            "FROM laptop " +
                                            "WHERE lmodel = '" + selectedModel + "' AND " +
                                                   "pmodel = '" + selectedProcessor + "') " +
                               "OR " +
                               "socket2 = (SELECT socket " +
                                          "FROM laptop " +
                                          "WHERE lmodel = '" + selectedModel + "' AND " +
                                                 "pmodel = '" + selectedProcessor + "')) " +
                               "AND " +
                               "(chipset1 = (SELECT chipset " +
                                            "FROM laptop " +
                                            "WHERE lmodel = '" + selectedModel + "' AND " +
                                                   "pmodel = '" + selectedProcessor + "') " +
                               "OR " +
                                                   "chipset2 = (SELECT chipset " +
                                                                "FROM laptop " +
                                                                "WHERE lmodel = '" + selectedModel + "' AND " +
                                                                       "pmodel = '" + selectedProcessor + "'));";

            processorUpgradesAdapter = new MySqlDataAdapter(getProcessorSQL, connection);
            upgradeTable = new DataTable();

            try 
            {
                processorUpgradesAdapter.Fill(upgradeTable);
                stopWatch.Stop();
                ts5 = stopWatch.Elapsed;

                upgradeTable.Columns[0].ColumnName = "Processor";
                upgradeTable.Columns[1].ColumnName = "Model";
                upgradeTable.Columns[2].ColumnName = "Speed";
                upgradeTable.Columns[3].ColumnName = "Cache";
                upgradeTable.Columns[4].ColumnName = "Instructions";
                upgradeTable.Columns[5].ColumnName = "Cores";

                bsource = new BindingSource();
                bsource.DataSource = upgradeTable;
                dataGridView1.DataSource = bsource;
                processorUpgradesAdapter.Update(upgradeTable);
            }

            catch(MySqlException ex)
            {

            }

            //Time6
            stopWatch = new Stopwatch();
            stopWatch.Start();
            CurrentConfigLabel.Text = selectedModel;
            getCurrentConfig = "SELECT name, pmodel, speed, cache, instruction, cores FROM processor WHERE pmodel = '"
                               + selectedProcessor + "'";

            currentConfigAdapter = new MySqlDataAdapter(getCurrentConfig, connection);
            currentConfigTable = new DataTable();

            try
            {
                currentConfigAdapter.Fill(currentConfigTable);
                stopWatch.Stop();
                ts6 = stopWatch.Elapsed;
                Console.WriteLine("Time from connecting to database till update the result: " + (ts1.Milliseconds +
                                  ts2.Milliseconds + ts3.Milliseconds + ts4.Milliseconds + ts5.Milliseconds + ts6.Milliseconds) + " ms");

                currentConfigTable.Columns[0].ColumnName = "Processor";
                currentConfigTable.Columns[1].ColumnName = "Model";
                currentConfigTable.Columns[2].ColumnName = "Speed";
                currentConfigTable.Columns[3].ColumnName = "Cache";
                currentConfigTable.Columns[4].ColumnName = "Instructions";
                currentConfigTable.Columns[5].ColumnName = "Cores";

                bsource = new BindingSource();
                bsource.DataSource = currentConfigTable;
                dataGridView2.DataSource = bsource;
                currentConfigAdapter.Update(currentConfigTable);
            }

            catch (MySqlException ex)
            {

            }

            triggerEvent = false;

            resetSelectFields();
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
        }

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
        }

        private void label6_Click(object sender, EventArgs e)
        {
        }

        private void ResetButton_Click(object sender, EventArgs e)
        {
            resetAll();
        }

        //Resets fields
        private void resetAll() 
        {
            //Clears fields
            upgradeTable.Clear();
            currentConfigTable.Clear();
            yearDS.Clear();
            modelDS.Clear();
            processorDS.Clear();
            selectedModel = "NONE";
            selectedProcessor = "NONE";
            CurrentConfigLabel.Text = "";

            //Repopulates fields
            getYearSQL = "SELECT DISTINCT year FROM laptop ORDER BY year";
            getModelSQL = "SELECT DISTINCT lmodel FROM laptop ORDER BY lmodel";
            getProcessorSQL = "SELECT DISTINCT pmodel FROM laptop ORDER BY pmodel";

            yearAdapter = new MySqlDataAdapter(getYearSQL, connection);
            modelAdapter = new MySqlDataAdapter(getModelSQL, connection);
            processorAdapter = new MySqlDataAdapter(getProcessorSQL, connection);

            yearDS = new DataSet();
            modelDS = new DataSet();
            processorDS = new DataSet();

            yearAdapter.Fill(yearDS);
            modelAdapter.Fill(modelDS);
            processorAdapter.Fill(processorDS);

            cmbYear.DisplayMember = "year";
            cmbYear.DataSource = yearDS.Tables[0];

            cmbModel.DisplayMember = "lmodel";
            cmbModel.DataSource = modelDS.Tables[0];

            cmbProcessor.DisplayMember = "pmodel";
            cmbProcessor.DataSource = processorDS.Tables[0];

            cmbYear.SelectedIndex = -1;
            cmbModel.SelectedIndex = -1;
            cmbProcessor.SelectedIndex = -1;

            lblSelectedModel.Text = selectedModel;
            lblSelectedProcessor.Text = selectedProcessor;

            triggerEvent = true;
        }

        private void resetSelectFields() 
        {
            //Clears fields
            yearDS.Clear();
            modelDS.Clear();
            processorDS.Clear();
            selectedModel = "NONE";
            selectedProcessor = "NONE";

            //Repopulates fields
            getYearSQL = "SELECT DISTINCT year FROM laptop ORDER BY year";
            getModelSQL = "SELECT DISTINCT lmodel FROM laptop ORDER BY lmodel";
            getProcessorSQL = "SELECT DISTINCT pmodel FROM laptop ORDER BY pmodel";

            yearAdapter = new MySqlDataAdapter(getYearSQL, connection);
            modelAdapter = new MySqlDataAdapter(getModelSQL, connection);
            processorAdapter = new MySqlDataAdapter(getProcessorSQL, connection);

            yearDS = new DataSet();
            modelDS = new DataSet();
            processorDS = new DataSet();

            yearAdapter.Fill(yearDS);
            modelAdapter.Fill(modelDS);
            processorAdapter.Fill(processorDS);

            cmbYear.DisplayMember = "year";
            cmbYear.DataSource = yearDS.Tables[0];

            cmbModel.DisplayMember = "lmodel";
            cmbModel.DataSource = modelDS.Tables[0];

            cmbProcessor.DisplayMember = "pmodel";
            cmbProcessor.DataSource = processorDS.Tables[0];

            cmbYear.SelectedIndex = -1;
            cmbModel.SelectedIndex = -1;
            cmbProcessor.SelectedIndex = -1;

            lblSelectedModel.Text = selectedModel;
            lblSelectedProcessor.Text = selectedProcessor;

            triggerEvent = true;
        }
    }
}
