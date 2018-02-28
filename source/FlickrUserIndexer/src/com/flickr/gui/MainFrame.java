package com.flickr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.flickr.db.WorldCityDB;
import com.flickr.flickr.WorldCity;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;
import com.flickr.search.MainSearchThread;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<WorldCity> countryVector;
	private Vector<String> countryNameVector;
	private JComboBox<String> countryComboBox;
	private JTextField workerCountTextField;
	
 	private JTextField messageTextField;
 	private JTextField sleepTextField;
	
	private JTextField totalPhotoInsertedTextField;
	private JTextField totalUserInsertedTextField;
	private JTextField totalPlaceFinishedTextField;
	private JTextField totalCityFinishedTextField;
	
	private JTextField lastPhotoInsertedTextField;
	private JTextField lastUserInsertedTextField;
	private JTextField lastPlaceFinishedTextField;
	private JTextField lastCityFinTextField;
	
	private JTextField totalPhotoSearchedTextField;
	private JTextField totalUserSearchedTextField;
	
	private JTextField cityCrawlingTextField;
	private JTextField placeCrawlingTextField;
	private JTextField userCrawlingTextField;
	
	private JProgressBar cityCrawlingProgressBar;
	private JProgressBar placeCrawlingProgressBar;
	private JProgressBar userCrawlingProgressBar;

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
		setBounds(100, 100, 802, 432);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 153, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCountryCode = new JLabel("Country Code:");
		lblCountryCode.setBounds(20, 10, 113, 14);
		contentPane.add(lblCountryCode);
		
		JLabel lblWorkerCount = new JLabel("Searcher Count:");
		lblWorkerCount.setBounds(20, 45, 113, 14);
		contentPane.add(lblWorkerCount);
		
		JLabel lblTotalPhotoCount = new JLabel("Total Photo Inserted:");
		lblTotalPhotoCount.setBounds(413, 10, 113, 14);
		contentPane.add(lblTotalPhotoCount);
		
		JLabel lblTotalUserCount = new JLabel("Total User Inserted:");
		lblTotalUserCount.setBounds(413, 45, 113, 14);
		contentPane.add(lblTotalUserCount);
		
		JLabel lblTotalCityCount = new JLabel("Total City Finished:");
		lblTotalCityCount.setBounds(413, 115, 113, 14);
		contentPane.add(lblTotalCityCount);
		
		JLabel lblLastPhotoCrawled = new JLabel("Last Photo Inserted:");
		lblLastPhotoCrawled.setBounds(413, 150, 120, 14);
		contentPane.add(lblLastPhotoCrawled);
		
		JLabel lblLastUserCrawled = new JLabel("User Crawling Now:");
		lblLastUserCrawled.setBounds(20, 335, 132, 14);
		contentPane.add(lblLastUserCrawled);
		
		JLabel lblLastUserFinished = new JLabel("Last User Inserted:");
		lblLastUserFinished.setBounds(413, 185, 113, 14);
		contentPane.add(lblLastUserFinished);
		
		JLabel lblLastCityFinished = new JLabel("Last City Finished:");
		lblLastCityFinished.setBounds(413, 255, 113, 14);
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
						JTextField[] textFieldArray = { messageTextField, sleepTextField, totalPhotoInsertedTextField,
								totalUserInsertedTextField, totalPlaceFinishedTextField, totalCityFinishedTextField, 
								lastPhotoInsertedTextField,	lastUserInsertedTextField, lastPlaceFinishedTextField, 
								lastCityFinTextField, totalPhotoSearchedTextField,totalUserSearchedTextField, 
								cityCrawlingTextField, placeCrawlingTextField, userCrawlingTextField };
						
						JProgressBar[] progressBarArray={ cityCrawlingProgressBar,placeCrawlingProgressBar,userCrawlingProgressBar};
						
						BackgroundSearch task = new BackgroundSearch(countryVector.get(countryComboBox.getSelectedIndex()).getCountryCode(), textFieldArray, progressBarArray); //***//
						task.execute(); //***//
					}
				});
		btnStartCrawl.setBounds(20, 75, 363, 25);
		contentPane.add(btnStartCrawl);
		
		countryComboBox = new JComboBox<String>(countryNameVector);
		countryComboBox.setSelectedIndex(250);
		countryComboBox.setBounds(150, 5, 233, 25);
		contentPane.add(countryComboBox);
		
		workerCountTextField = new JTextField();
		workerCountTextField.setHorizontalAlignment(SwingConstants.CENTER);
		workerCountTextField.setBounds(150, 40, 75, 25);
		contentPane.add(workerCountTextField);
		workerCountTextField.setColumns(10);
		
		totalPhotoInsertedTextField = new JTextField();
		totalPhotoInsertedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalPhotoInsertedTextField.setBackground(Color.WHITE);
		totalPhotoInsertedTextField.setEditable(false);
		totalPhotoInsertedTextField.setBounds(543, 5, 233, 25);
		contentPane.add(totalPhotoInsertedTextField);
		totalPhotoInsertedTextField.setColumns(10);
		
		totalUserInsertedTextField = new JTextField();
		totalUserInsertedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalUserInsertedTextField.setBackground(Color.WHITE);
		totalUserInsertedTextField.setEditable(false);
		totalUserInsertedTextField.setBounds(543, 40, 233, 25);
		contentPane.add(totalUserInsertedTextField);
		totalUserInsertedTextField.setColumns(10);
		
		totalCityFinishedTextField = new JTextField();
		totalCityFinishedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalCityFinishedTextField.setBackground(Color.WHITE);
		totalCityFinishedTextField.setEditable(false);
		totalCityFinishedTextField.setBounds(543, 110, 233, 25);
		contentPane.add(totalCityFinishedTextField);
		totalCityFinishedTextField.setColumns(10);
		
		lastPhotoInsertedTextField = new JTextField();
		lastPhotoInsertedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		lastPhotoInsertedTextField.setBackground(Color.WHITE);
		lastPhotoInsertedTextField.setEditable(false);
		lastPhotoInsertedTextField.setBounds(543, 145, 233, 25);
		contentPane.add(lastPhotoInsertedTextField);
		lastPhotoInsertedTextField.setColumns(10);
		
		userCrawlingTextField = new JTextField();
		userCrawlingTextField.setHorizontalAlignment(SwingConstants.CENTER);
		userCrawlingTextField.setBackground(Color.WHITE);
		userCrawlingTextField.setEditable(false);
		userCrawlingTextField.setBounds(150, 330, 233, 25);
		contentPane.add(userCrawlingTextField);
		userCrawlingTextField.setColumns(10);
		
		lastUserInsertedTextField = new JTextField();
		lastUserInsertedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		lastUserInsertedTextField.setBackground(Color.WHITE);
		lastUserInsertedTextField.setEditable(false);
		lastUserInsertedTextField.setBounds(543, 180, 233, 25);
		contentPane.add(lastUserInsertedTextField);
		lastUserInsertedTextField.setColumns(10);
		
		lastCityFinTextField = new JTextField();
		lastCityFinTextField.setHorizontalAlignment(SwingConstants.CENTER);
		lastCityFinTextField.setBackground(Color.WHITE);
		lastCityFinTextField.setEditable(false);
		lastCityFinTextField.setBounds(543, 250, 233, 25);
		contentPane.add(lastCityFinTextField);
		lastCityFinTextField.setColumns(10);
		
		JButton btnCityWorker = new JButton("Set Worker Count");
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
		
		messageTextField = new JTextField();
		messageTextField.setHorizontalAlignment(SwingConstants.LEFT);
		messageTextField.setText("message");
		messageTextField.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		messageTextField.setBackground(new Color(255, 250, 205));
		messageTextField.setEditable(false);
		messageTextField.setBounds(20, 145, 363, 35);
		contentPane.add(messageTextField);
		messageTextField.setColumns(10);
		
		sleepTextField = new JTextField();
		sleepTextField.setHorizontalAlignment(SwingConstants.CENTER);
		sleepTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sleepTextField.setText("System is Working!");
		sleepTextField.setBackground(new Color(50, 205, 50));
		sleepTextField.setEditable(false);
		sleepTextField.setBounds(20, 110, 363, 25);
		contentPane.add(sleepTextField);
		sleepTextField.setColumns(10);
		
		JLabel lblTotalPlaceInserted = new JLabel("Total Place Finished:");
		lblTotalPlaceInserted.setBounds(413, 80, 113, 14);
		contentPane.add(lblTotalPlaceInserted);
		
		totalPlaceFinishedTextField = new JTextField();
		totalPlaceFinishedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalPlaceFinishedTextField.setBackground(Color.WHITE);
		totalPlaceFinishedTextField.setEditable(false);
		totalPlaceFinishedTextField.setBounds(543, 75, 233, 25);
		contentPane.add(totalPlaceFinishedTextField);
		totalPlaceFinishedTextField.setColumns(10);
		
		JLabel lblLastPlaceInserted = new JLabel("Last Place Finished:");
		lblLastPlaceInserted.setBounds(413, 220, 113, 14);
		contentPane.add(lblLastPlaceInserted);
		
		lastPlaceFinishedTextField = new JTextField();
		lastPlaceFinishedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		lastPlaceFinishedTextField.setBackground(Color.WHITE);
		lastPlaceFinishedTextField.setEditable(false);
		lastPlaceFinishedTextField.setBounds(543, 215, 233, 25);
		contentPane.add(lastPlaceFinishedTextField);
		lastPlaceFinishedTextField.setColumns(10);
		
		JLabel lblTotalPhotoSearched = new JLabel("Total Photo Searched:");
		lblTotalPhotoSearched.setBounds(413, 310, 132, 14);
		contentPane.add(lblTotalPhotoSearched);
		
		totalPhotoSearchedTextField = new JTextField();
		totalPhotoSearchedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalPhotoSearchedTextField.setBackground(Color.WHITE);
		totalPhotoSearchedTextField.setEditable(false);
		totalPhotoSearchedTextField.setBounds(543, 305, 233, 25);
		contentPane.add(totalPhotoSearchedTextField);
		totalPhotoSearchedTextField.setColumns(10);
		
		JLabel lblTotalUserSearched = new JLabel("Total User Searched:");
		lblTotalUserSearched.setBounds(414, 345, 131, 14);
		contentPane.add(lblTotalUserSearched);
		
		totalUserSearchedTextField = new JTextField();
		totalUserSearchedTextField.setHorizontalAlignment(SwingConstants.CENTER);
		totalUserSearchedTextField.setBackground(Color.WHITE);
		totalUserSearchedTextField.setEditable(false);
		totalUserSearchedTextField.setBounds(543, 340, 233, 25);
		contentPane.add(totalUserSearchedTextField);
		totalUserSearchedTextField.setColumns(10);
		
		userCrawlingProgressBar = new JProgressBar();
		userCrawlingProgressBar.setBounds(20, 365, 363, 25);
		userCrawlingProgressBar.setStringPainted(true);
		contentPane.add(userCrawlingProgressBar);
		
		JLabel lblPlaceCrawlingNow = new JLabel("Place Crawling Now:");
		lblPlaceCrawlingNow.setBounds(20, 265, 132, 14);
		contentPane.add(lblPlaceCrawlingNow);
		
		placeCrawlingTextField = new JTextField();
		placeCrawlingTextField.setHorizontalAlignment(SwingConstants.CENTER);
		placeCrawlingTextField.setBackground(Color.WHITE);
		placeCrawlingTextField.setEditable(false);
		placeCrawlingTextField.setBounds(150, 260, 233, 25);
		contentPane.add(placeCrawlingTextField);
		placeCrawlingTextField.setColumns(10);
		
		placeCrawlingProgressBar = new JProgressBar();
		placeCrawlingProgressBar.setBounds(20, 295, 363, 25);
		placeCrawlingProgressBar.setStringPainted(true);
		contentPane.add(placeCrawlingProgressBar);
		
		JLabel lblCityCrawlingNow = new JLabel("City Crawling Now:");
		lblCityCrawlingNow.setBounds(20, 190, 132, 14);
		contentPane.add(lblCityCrawlingNow);
		
		cityCrawlingTextField = new JTextField();
		cityCrawlingTextField.setHorizontalAlignment(SwingConstants.CENTER);
		cityCrawlingTextField.setBackground(Color.WHITE);
		cityCrawlingTextField.setEditable(false);
		cityCrawlingTextField.setBounds(150, 185, 233, 25);
		contentPane.add(cityCrawlingTextField);
		cityCrawlingTextField.setColumns(10);
		
		cityCrawlingProgressBar = new JProgressBar();
		cityCrawlingProgressBar.setBounds(20, 220, 363, 25);
		cityCrawlingProgressBar.setStringPainted(true);
		contentPane.add(cityCrawlingProgressBar);
		
		JLabel lblErkamUzun = new JLabel("Erkam Uzun");
		lblErkamUzun.setBounds(720, 376, 56, 14);
		contentPane.add(lblErkamUzun);
	}
	
	public class BackgroundSearch extends SwingWorker<String, Object>{

		private String countryCode;
		
		private JTextField messageTextField;
	 	private JTextField sleepTextField;
		
//		private JTextField totalPhotoInsertedTextField;
		private JTextField totalUserInsertedTextField;
		private JTextField totalPlaceFinishedTextField;
		private JTextField totalCityFinishedTextField;
		
//		private JTextField lastPhotoInsertedTextField;
		private JTextField lastUserInsertedTextField;
		private JTextField lastPlaceFinishedTextField;
		private JTextField lastCityFinTextField;
		
//		private JTextField totalPhotoSearchedTextField;
		private JTextField totalUserSearchedTextField;
		
		private JTextField cityCrawlingTextField;
//		private JTextField placeCrawlingTextField;
//		private JTextField userCrawlingTextField;
		
		private JProgressBar cityCrawlingProgressBar;
//		private JProgressBar placeCrawlingProgressBar;
//		private JProgressBar userCrawlingProgressBar;
		
	    public BackgroundSearch(String countryCode, JTextField[] textFieldArray, JProgressBar[] progressBarArray){
			this.countryCode=countryCode;
			
			this.messageTextField=textFieldArray[0];
			this.sleepTextField=textFieldArray[1];
			
//			this.totalPhotoInsertedTextField=textFieldArray[2];
			this.totalUserInsertedTextField=textFieldArray[3];
			this.totalPlaceFinishedTextField=textFieldArray[4];
			this.totalCityFinishedTextField=textFieldArray[5];
			
//			this.lastPhotoInsertedTextField=textFieldArray[6];
			this.lastUserInsertedTextField=textFieldArray[7];
			this.lastPlaceFinishedTextField=textFieldArray[8];
			this.lastCityFinTextField=textFieldArray[9];
			
//			this.totalPhotoSearchedTextField=textFieldArray[10];
			this.totalUserSearchedTextField=textFieldArray[11];
			
			this.cityCrawlingTextField=textFieldArray[12];
//			this.placeCrawlingTextField=textFieldArray[13];
//			this.userCrawlingTextField=textFieldArray[14];
			
			this.cityCrawlingProgressBar=progressBarArray[0];
//			this.placeCrawlingProgressBar=progressBarArray[1];
//			this.userCrawlingProgressBar=progressBarArray[2];
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
		
		private void printActivater(){
			while(true){
				printStat();
				Sleep.sleep(1000);
			}
		}
		
		private void printStat(){
			if(Flag.sleepFlag){
				sleepTextField.setBackground(Color.red);
				sleepTextField.setText(Flag.sleepBoxText);
			}else{
				sleepTextField.setBackground(new Color(50, 205, 50));
				sleepTextField.setText("System is Working!");
				messageTextField.setText(Flag.messageBoxText);
				
				cityCrawlingTextField.setText(Flag.cityCrawlingNow+" ("+Flag.totalCityFinished+"/"+Flag.totalCity4Country+")");
				cityCrawlingProgressBar.setMaximum(Flag.totalPlace4City);
				cityCrawlingProgressBar.setValue(Flag.searchedPlace4City);
				cityCrawlingProgressBar.setString("Place "+Flag.searchedPlace4City+"/"+Flag.totalPlace4City);
				
//				placeCrawlingTextField.setText(Flag.placeCrawlingNow);
//				placeCrawlingProgressBar.setMaximum(Flag.totalUser4Place);
//				placeCrawlingProgressBar.setValue(Flag.searchedUser4Place);
//				placeCrawlingProgressBar.setString("User "+Flag.searchedUser4Place+"/"+Flag.totalUser4Place);
				
//				userCrawlingTextField.setText(Flag.userCrawlingNow);
//				userCrawlingProgressBar.setMaximum(Flag.totalPhoto4User);
//				userCrawlingProgressBar.setValue(Flag.searchedPhoto4User);
//				userCrawlingProgressBar.setString("Photo "+Flag.searchedPhoto4User+"/"+Flag.totalPhoto4User);
				
//				totalPhotoInsertedTextField.setText(Flag.totalPhotoInserted+"");
				totalUserInsertedTextField.setText(Flag.totalUserInserted+"");
				totalPlaceFinishedTextField.setText(Flag.totalPlaceFinished+"");
				totalCityFinishedTextField.setText(Flag.totalCityFinished+"");
				
//				lastPhotoInsertedTextField.setText(Flag.lastPhotoInserted);
				lastUserInsertedTextField.setText(Flag.lastUserInserted);
				lastPlaceFinishedTextField.setText(Flag.lastPlaceFinished);
				lastCityFinTextField.setText(Flag.lastCityFinished);
				
//				totalPhotoSearchedTextField.setText(Flag.totalPhotoSearched+"");
				totalUserSearchedTextField.setText(Flag.totalUserSearched+"");
				
			}
			
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
