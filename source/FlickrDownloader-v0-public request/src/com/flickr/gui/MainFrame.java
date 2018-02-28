package com.flickr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.flickr.db.WorldCity;
import com.flickr.db.WorldCityDB;
import com.flickr.function.Flag;
import com.flickr.search.MainSearchThread;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> countryComboBox;
	private JPanel contentPane;
	private JTextField workerCountTextField;
	private JTextField photoCountTextField;
	private JTextField userCountTextField;
	private JTextField cityCountTextField;
	private JTextField lastPhotoTextField;
	private JTextField lastUserTextField;
	private JTextField finUserTextField;
	private JTextField finCityTextField;

	private Vector<WorldCity> countryVector;
	private Vector<String> countryNameVector;
 	private JButton btnCityWorker;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 571, 414);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCountryCode = new JLabel("Country Code:");
		lblCountryCode.setBounds(20, 10, 113, 14);
		contentPane.add(lblCountryCode);
		
		JLabel lblWorkerCount = new JLabel("Worker Thread Count");
		lblWorkerCount.setBounds(20, 45, 113, 14);
		contentPane.add(lblWorkerCount);
		
		JLabel lblTotalPhotoCount = new JLabel("Total Photo Count:");
		lblTotalPhotoCount.setBounds(20, 140, 113, 14);
		contentPane.add(lblTotalPhotoCount);
		
		JLabel lblTotalUserCount = new JLabel("Total User Count:");
		lblTotalUserCount.setBounds(20, 175, 113, 14);
		contentPane.add(lblTotalUserCount);
		
		JLabel lblTotalCityCount = new JLabel("Total City Count:");
		lblTotalCityCount.setBounds(20, 210, 113, 14);
		contentPane.add(lblTotalCityCount);
		
		JLabel lblLastPhotoCrawled = new JLabel("Last Photo Crawled:");
		lblLastPhotoCrawled.setBounds(20, 245, 120, 14);
		contentPane.add(lblLastPhotoCrawled);
		
		JLabel lblLastUserCrawled = new JLabel("Last User Crawled:");
		lblLastUserCrawled.setBounds(20, 280, 113, 14);
		contentPane.add(lblLastUserCrawled);
		
		JLabel lblLastUserFinished = new JLabel("Last User Finished:");
		lblLastUserFinished.setBounds(20, 315, 113, 14);
		contentPane.add(lblLastUserFinished);
		
		JLabel lblLastCityFinished = new JLabel("Last City Finished:");
		lblLastCityFinished.setBounds(20, 350, 113, 14);
		contentPane.add(lblLastCityFinished);
		
		countryVector=(new WorldCityDB()).getCountryList();
		countryNameVector=new Vector<String>();
		for(WorldCity country:countryVector){
			countryNameVector.add(country.getCountryName());
		}
		final JButton btnStartCrawl = new JButton("Start Crawling!");
		btnStartCrawl.addActionListener(
				new ActionListener()
				{
					public void actionPerformed( ActionEvent event )
					{
						btnStartCrawl.setEnabled(false);
						ArrayList<JTextField> textFieldArray=new ArrayList<JTextField>();
						textFieldArray.add(photoCountTextField);
						textFieldArray.add(userCountTextField);
						textFieldArray.add(cityCountTextField);
						textFieldArray.add(lastPhotoTextField);
						textFieldArray.add(lastUserTextField);
						textFieldArray.add(finUserTextField);
						textFieldArray.add(finCityTextField);
						BackgroundSearch task = new BackgroundSearch(countryVector.get(countryComboBox.getSelectedIndex()).getCountryCode(), textFieldArray); //***//
						task.execute(); //***//
					}
				});
		btnStartCrawl.setBounds(20, 105, 363, 25);
		contentPane.add(btnStartCrawl);
		
		countryComboBox = new JComboBox<String>(countryNameVector);
		countryComboBox.setSelectedIndex(250);
		countryComboBox.setBounds(150, 5, 233, 25);
		contentPane.add(countryComboBox);
		
		workerCountTextField = new JTextField();
		workerCountTextField.setBounds(150, 40, 75, 25);
		contentPane.add(workerCountTextField);
		workerCountTextField.setColumns(10);
		
		photoCountTextField = new JTextField();
		photoCountTextField.setBackground(Color.WHITE);
		photoCountTextField.setEditable(false);
		photoCountTextField.setBounds(150, 135, 233, 25);
		contentPane.add(photoCountTextField);
		photoCountTextField.setColumns(10);
		
		userCountTextField = new JTextField();
		userCountTextField.setBackground(Color.WHITE);
		userCountTextField.setEditable(false);
		userCountTextField.setBounds(150, 170, 233, 25);
		contentPane.add(userCountTextField);
		userCountTextField.setColumns(10);
		
		cityCountTextField = new JTextField();
		cityCountTextField.setBackground(Color.WHITE);
		cityCountTextField.setEditable(false);
		cityCountTextField.setBounds(150, 205, 233, 25);
		contentPane.add(cityCountTextField);
		cityCountTextField.setColumns(10);
		
		lastPhotoTextField = new JTextField();
		lastPhotoTextField.setBackground(Color.WHITE);
		lastPhotoTextField.setEditable(false);
		lastPhotoTextField.setBounds(150, 240, 233, 25);
		contentPane.add(lastPhotoTextField);
		lastPhotoTextField.setColumns(10);
		
		lastUserTextField = new JTextField();
		lastUserTextField.setBackground(Color.WHITE);
		lastUserTextField.setEditable(false);
		lastUserTextField.setBounds(150, 275, 233, 25);
		contentPane.add(lastUserTextField);
		lastUserTextField.setColumns(10);
		
		finUserTextField = new JTextField();
		finUserTextField.setBackground(Color.WHITE);
		finUserTextField.setEditable(false);
		finUserTextField.setBounds(150, 310, 233, 25);
		contentPane.add(finUserTextField);
		finUserTextField.setColumns(10);
		
		finCityTextField = new JTextField();
		finCityTextField.setBackground(Color.WHITE);
		finCityTextField.setEditable(false);
		finCityTextField.setBounds(150, 345, 233, 25);
		contentPane.add(finCityTextField);
		finCityTextField.setColumns(10);
		
		btnCityWorker = new JButton("Set Worker Count");
		btnCityWorker.addActionListener(
				new ActionListener()
				{
					public void actionPerformed( ActionEvent event )
					{
						BackgroundWorkerChange task = new BackgroundWorkerChange(Integer.parseInt(workerCountTextField.getText())); //***//
						task.execute(); //***//
					}
				});
		btnCityWorker.setBounds(230, 40, 153, 25);
		contentPane.add(btnCityWorker);
	}
	
	public class BackgroundSearch extends SwingWorker<String, Object>{

		private String countryCode;
		private JTextField photoCountTextField;
		private JTextField userCountTextField;
		private JTextField cityCountTextField;
		private JTextField lastPhotoTextField;
		private JTextField lastUserTextField;
		private JTextField finUserTextField;
		private JTextField finCityTextField;
		
		public BackgroundSearch(String countryCode, ArrayList<JTextField> textFieldArray){
			this.countryCode=countryCode;
			this.photoCountTextField=textFieldArray.get(0);
			this.userCountTextField=textFieldArray.get(1);
			this.cityCountTextField=textFieldArray.get(2);
			this.lastPhotoTextField=textFieldArray.get(3);
			this.lastUserTextField=textFieldArray.get(4);
			this.finUserTextField=textFieldArray.get(5);
			this.finCityTextField=textFieldArray.get(6);
		}
		
		@Override
		protected String doInBackground() throws Exception {
			searchActivater();
			printActivater();
			return null;
		}
		
		protected void done(){
			System.exit(DISPOSE_ON_CLOSE);
		}
		
		private void searchActivater(){
			MainSearchThread mainSearchThread=new MainSearchThread(countryCode);
			new Thread(mainSearchThread).start();
		}
		
		@SuppressWarnings("static-access")
		private void printActivater(){
			while(true){
				printStat();
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void printStat(){
			this.photoCountTextField.setText(Flag.totalPhoto+"");
			this.userCountTextField.setText(Flag.totalUser+"");
			this.cityCountTextField.setText(Flag.totalCity+"");
			this.lastPhotoTextField.setText(Flag.lastInsertPhoto+"");
			this.lastUserTextField.setText(Flag.lastInsertUser+"");
			this.finUserTextField.setText(Flag.lastFinUser+"");
			this.finCityTextField.setText(Flag.lastFinCity+"");
			
		}
		
	}
	
	
	public class BackgroundWorkerChange extends SwingWorker<String, Object>{
		private int workerCount;
		public BackgroundWorkerChange(int workerCount){
			this.workerCount=workerCount;
		}
		@Override
		protected String doInBackground() throws Exception {
			Flag.maxPhotoWorker=workerCount;
			return null;
		}
		
	}
}
