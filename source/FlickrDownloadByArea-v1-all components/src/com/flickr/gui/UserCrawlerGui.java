package com.flickr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.flickr.downloader.EU_DownloaderMain;
import com.flickr.flickr.BBox;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;
import com.flickr.photoSearch.MainPhotoSearchThread;
import com.flickr.userSearch.MainUserSearchThread;

public class UserCrawlerGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldMinLat;
	private JTextField textFieldMinLong;
	private JTextField textFieldMaxLat;
	private JTextField textFieldMaxLong;
	private JTextField textFieldSystemMessage;
	private JTextField textFieldTotalEntrySearched;
	private JTextField textFieldTotalUserCrawled;
	private JTextField textFieldLastUserCrawled;
	private JTextField textFieldMaxWorker;

	private JProgressBar progressBarBBox;
	private JProgressBar progressBarEntry;
	
	
	private JPanel panelBBoxMap;
	private JLabel lblErkamUzun;
	
	private JCheckBox chckbxGetfromdb;
	
	private JProgressBar progressBarUser;
	private JProgressBar progressBarPhoto;
	
	private JTextField textFieldTotalPhotoSearched;
	private JTextField textFieldTotalPhotoCrawled;
	private JTextField textFieldLastPhotoCrawled;
	private JPanel panel_1;
	private JLabel lblPhotoDownloader;
	private JButton btnStartDownloadingPhotos;
	private JProgressBar progressBarUserDownload;
	private JProgressBar progressBarPhotoDownload;
	
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
					UserCrawlerGui frame = new UserCrawlerGui();
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
	public UserCrawlerGui() {
		setTitle("User Crawler by Bounding Box");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 660);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 153, 204));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		
		JLabel lblSouthwestLatitude = new JLabel("Min (SouthWest) Latitude:");
		lblSouthwestLatitude.setBounds(10, 10, 160, 14);
		lblSouthwestLatitude.setForeground(Color.WHITE);
		
		JLabel lblSouthwestLongitude = new JLabel("Min (SouthWest) Longitude:");
		lblSouthwestLongitude.setBounds(10, 45, 160, 14);
		lblSouthwestLongitude.setForeground(Color.WHITE);
		
		textFieldMinLat = new JTextField();
		textFieldMinLat.setBounds(180, 5, 60, 25);
		textFieldMinLat.setColumns(10);
		
		textFieldMinLong = new JTextField();
		textFieldMinLong.setBounds(180, 40, 60, 25);
		textFieldMinLong.setColumns(10);
		
		JLabel lblMaxnortheastLatitude = new JLabel("Max (NorthEast) Latitude:");
		lblMaxnortheastLatitude.setBounds(255, 10, 160, 14);
		lblMaxnortheastLatitude.setForeground(Color.WHITE);
		
		textFieldMaxLat = new JTextField();
		textFieldMaxLat.setBounds(425, 5, 60, 25);
		textFieldMaxLat.setColumns(10);
		
		JLabel lblMaxnortheastLongitude = new JLabel("Max (NorthEast) Longitude:");
		lblMaxnortheastLongitude.setBounds(255, 45, 160, 14);
		lblMaxnortheastLongitude.setForeground(Color.WHITE);
		
		textFieldMaxLong = new JTextField();
		textFieldMaxLong.setBounds(425, 40, 60, 25);
		textFieldMaxLong.setColumns(10);
		
		JButton btnStartButton = new JButton("Start Crawling");
		btnStartButton.setBounds(383, 110, 101, 25);
		btnStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BBox bbox=new BBox(Double.parseDouble(textFieldMinLong.getText()), Double.parseDouble(textFieldMinLat.getText()), Double.parseDouble(textFieldMaxLong.getText()), Double.parseDouble(textFieldMaxLat.getText()));
				
				(new BackgroundSearch(bbox,chckbxGetfromdb.isSelected())).execute();
				
				(new BackgroundProgressBarUpdater(progressBarBBox,progressBarEntry)).execute();
				
				(new BackgroundTextFieldUpdater(textFieldSystemMessage,textFieldTotalEntrySearched,textFieldTotalUserCrawled,textFieldLastUserCrawled)).execute();
				
				(new BackgroundPanelUpdater(panelBBoxMap,bbox)).execute();
			}
			
		});
		
		textFieldSystemMessage = new JTextField();
		textFieldSystemMessage.setBounds(125, 110, 231, 25);
		textFieldSystemMessage.setBackground(new Color(50, 205, 50));
		textFieldSystemMessage.setEditable(false);
		textFieldSystemMessage.setColumns(10);
		
		JLabel lblSystemMode = new JLabel("System Message:");
		lblSystemMode.setBounds(10, 115, 115, 14);
		lblSystemMode.setForeground(Color.WHITE);
		
		progressBarBBox = new JProgressBar();
		progressBarBBox.setBounds(10, 145, 475, 30);
		progressBarBBox.setToolTipText("BBox Crawling Now/ Center Point");
		progressBarBBox.setStringPainted(true);
		
		progressBarEntry = new JProgressBar();
		progressBarEntry.setBounds(10, 440, 475, 30);
		progressBarEntry.setToolTipText("Searched / Total Entry Count");
		progressBarEntry.setStringPainted(true);
		
		JLabel lblTotalEntrySearched = new JLabel("Total Entry Searched:");
		lblTotalEntrySearched.setBounds(10, 485, 133, 14);
		lblTotalEntrySearched.setForeground(Color.WHITE);
		
		JLabel lblTotalUserCrawled = new JLabel("Total User Crawled:");
		lblTotalUserCrawled.setBounds(10, 520, 133, 14);
		lblTotalUserCrawled.setForeground(Color.WHITE);
		
		JLabel lblLastUserCrawled = new JLabel("Last User Crawled:");
		lblLastUserCrawled.setBounds(10, 555, 133, 14);
		lblLastUserCrawled.setForeground(Color.WHITE);
		
		panelBBoxMap = new JPanel();
		panelBBoxMap.setBounds(10, 180, 475, 256);
		
		textFieldTotalEntrySearched = new JTextField();
		textFieldTotalEntrySearched.setBounds(153, 480, 168, 25);
		textFieldTotalEntrySearched.setEditable(false);
		textFieldTotalEntrySearched.setColumns(10);
		textFieldTotalEntrySearched.setBackground(SystemColor.menu);
		
		textFieldTotalUserCrawled = new JTextField();
		textFieldTotalUserCrawled.setBounds(153, 515, 168, 25);
		textFieldTotalUserCrawled.setEditable(false);
		textFieldTotalUserCrawled.setColumns(10);
		textFieldTotalUserCrawled.setBackground(SystemColor.info);
		
		textFieldLastUserCrawled = new JTextField();
		textFieldLastUserCrawled.setBounds(153, 550, 332, 25);
		textFieldLastUserCrawled.setEditable(false);
		textFieldLastUserCrawled.setColumns(10);
		textFieldLastUserCrawled.setBackground(new Color(250, 240, 230));
		
		JLabel lblMaxWorkerCount = new JLabel("Max Worker Count:");
		lblMaxWorkerCount.setBounds(10, 80, 160, 14);
		lblMaxWorkerCount.setForeground(Color.WHITE);
		
		textFieldMaxWorker = new JTextField();
		textFieldMaxWorker.setBounds(180, 74, 60, 25);
		textFieldMaxWorker.setColumns(10);
		
		JButton btnSetWorker = new JButton("Set Worker");
		btnSetWorker.setBounds(255, 74, 101, 25);
		btnSetWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackgroundWorkerChange task = new BackgroundWorkerChange(Integer.parseInt(textFieldMaxWorker.getText())); //***//
				task.execute(); //***//
			}
		});
		
		lblErkamUzun = new JLabel("Erkam Uzun");
		lblErkamUzun.setBounds(399, 607, 85, 14);
		
		chckbxGetfromdb = new JCheckBox("GetFromDB");
		chckbxGetfromdb.setBounds(383, 80, 102, 23);
		
		JPanel panel = new JPanel();
		panel.setBounds(491, 0, 4, 621);
		panel.setBackground(Color.BLACK);
		
		JLabel lblPhotoCrawler = new JLabel("PHOTO CRAWLER");
		lblPhotoCrawler.setBounds(513, 11, 311, 15);
		lblPhotoCrawler.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPhotoCrawler.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhotoCrawler.setForeground(new Color(255, 255, 255));
		
		JButton btnPhotoCrawler = new JButton("Start Crawling Photos for User");
		btnPhotoCrawler.setBounds(513, 44, 311, 30);
		btnPhotoCrawler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new BackgroundPhotoSearch(progressBarUser, progressBarPhoto)).execute();
				(new BackgroundPhotoTextFieldUpdater(textFieldTotalPhotoSearched, textFieldTotalPhotoCrawled, textFieldLastPhotoCrawled)).execute();
			}
		});
		progressBarUser = new JProgressBar();
		progressBarUser.setBounds(513, 85, 311, 30);
		progressBarUser.setToolTipText("BBox Crawling Now/ Center Point");
		progressBarUser.setStringPainted(true);
		
		progressBarPhoto = new JProgressBar();
		progressBarPhoto.setBounds(513, 133, 311, 30);
		progressBarPhoto.setToolTipText("Searched / Total Entry Count");
		progressBarPhoto.setStringPainted(true);
		
		JLabel lblTotalPhotoSearched = new JLabel("Total Photo Searched:");
		lblTotalPhotoSearched.setBounds(513, 186, 133, 14);
		lblTotalPhotoSearched.setForeground(Color.WHITE);
		
		textFieldTotalPhotoSearched = new JTextField();
		textFieldTotalPhotoSearched.setBounds(656, 181, 168, 25);
		textFieldTotalPhotoSearched.setEditable(false);
		textFieldTotalPhotoSearched.setColumns(10);
		textFieldTotalPhotoSearched.setBackground(SystemColor.menu);
		
		JLabel lblTotalPhotoCrawled = new JLabel("Total Photo Crawled:");
		lblTotalPhotoCrawled.setBounds(513, 221, 133, 14);
		lblTotalPhotoCrawled.setForeground(Color.WHITE);
		
		textFieldTotalPhotoCrawled = new JTextField();
		textFieldTotalPhotoCrawled.setBounds(656, 216, 168, 25);
		textFieldTotalPhotoCrawled.setEditable(false);
		textFieldTotalPhotoCrawled.setColumns(10);
		textFieldTotalPhotoCrawled.setBackground(SystemColor.info);
		
		JLabel lblLastPhotoCrawled = new JLabel("Last Photo Crawled:");
		lblLastPhotoCrawled.setBounds(513, 256, 133, 14);
		lblLastPhotoCrawled.setForeground(Color.WHITE);
		
		textFieldLastPhotoCrawled = new JTextField();
		textFieldLastPhotoCrawled.setBounds(656, 251, 168, 25);
		textFieldLastPhotoCrawled.setEditable(false);
		textFieldLastPhotoCrawled.setColumns(10);
		textFieldLastPhotoCrawled.setBackground(new Color(250, 240, 230));
		contentPane.setLayout(null);
		contentPane.add(lblSouthwestLatitude);
		contentPane.add(textFieldMinLat);
		contentPane.add(lblMaxnortheastLatitude);
		contentPane.add(textFieldMaxLat);
		contentPane.add(lblSouthwestLongitude);
		contentPane.add(textFieldMinLong);
		contentPane.add(lblMaxnortheastLongitude);
		contentPane.add(textFieldMaxLong);
		contentPane.add(lblMaxWorkerCount);
		contentPane.add(textFieldMaxWorker);
		contentPane.add(btnSetWorker);
		contentPane.add(chckbxGetfromdb);
		contentPane.add(lblSystemMode);
		contentPane.add(textFieldSystemMessage);
		contentPane.add(btnStartButton);
		contentPane.add(progressBarBBox);
		contentPane.add(panelBBoxMap);
		contentPane.add(progressBarEntry);
		contentPane.add(lblTotalEntrySearched);
		contentPane.add(textFieldTotalEntrySearched);
		contentPane.add(lblTotalUserCrawled);
		contentPane.add(textFieldTotalUserCrawled);
		contentPane.add(lblLastUserCrawled);
		contentPane.add(textFieldLastUserCrawled);
		contentPane.add(panel);
		contentPane.add(lblLastPhotoCrawled);
		contentPane.add(textFieldLastPhotoCrawled);
		contentPane.add(lblTotalPhotoCrawled);
		contentPane.add(textFieldTotalPhotoCrawled);
		contentPane.add(lblPhotoCrawler);
		contentPane.add(btnPhotoCrawler);
		contentPane.add(progressBarPhoto);
		contentPane.add(progressBarUser);
		contentPane.add(lblTotalPhotoSearched);
		contentPane.add(textFieldTotalPhotoSearched);
		contentPane.add(lblErkamUzun);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.BLACK);
		panel_1.setBounds(495, 281, 340, 4);
		contentPane.add(panel_1);
		
		lblPhotoDownloader = new JLabel("PHOTO DOWNLOADER");
		lblPhotoDownloader.setHorizontalAlignment(SwingConstants.CENTER);
		lblPhotoDownloader.setForeground(Color.WHITE);
		lblPhotoDownloader.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPhotoDownloader.setBounds(505, 303, 311, 15);
		contentPane.add(lblPhotoDownloader);
		
		btnStartDownloadingPhotos = new JButton("Start Downloading Photos for User");
		btnStartDownloadingPhotos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				(new BackgroundDownloader(progressBarUserDownload, progressBarPhotoDownload)).execute();
			}
		});
		btnStartDownloadingPhotos.setBounds(513, 333, 311, 30);
		contentPane.add(btnStartDownloadingPhotos);
		
		progressBarUserDownload = new JProgressBar();
		progressBarUserDownload.setToolTipText("BBox Crawling Now/ Center Point");
		progressBarUserDownload.setStringPainted(true);
		progressBarUserDownload.setBounds(513, 374, 311, 30);
		contentPane.add(progressBarUserDownload);
		
		progressBarPhotoDownload = new JProgressBar();
		progressBarPhotoDownload.setToolTipText("Searched / Total Entry Count");
		progressBarPhotoDownload.setStringPainted(true);
		progressBarPhotoDownload.setBounds(513, 422, 311, 30);
		contentPane.add(progressBarPhotoDownload);
		final JToggleButton tglbtnPause = new JToggleButton("PAUSE");
		tglbtnPause.setFont(new Font("Tahoma", Font.BOLD, 11));
		tglbtnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tempSystemText = "";
				if(tglbtnPause.getText().equals("PAUSE")){
					tglbtnPause.setText("RESUME");
					Flag.pauseFlag=true;
					tempSystemText=Flag.systemMessageText;
					Flag.systemMessageText="SYSTEM PAUSED @ "+ Flag.ft.format(new Date());;
				}else{
					tglbtnPause.setText("PAUSE");
					Flag.pauseFlag=false;
					Flag.systemMessageText=tempSystemText;
				}
			}
		});
		tglbtnPause.setBounds(513, 591, 121, 30);
		contentPane.add(tglbtnPause);
	}
	
	public class BackgroundSearch extends SwingWorker<String, Object>{
		private BBox bbox;
		private boolean isDB;
		public BackgroundSearch(BBox bbox, boolean isDB){
			this.bbox=bbox;
			this.isDB=isDB;
		}
		@Override
		protected String doInBackground() throws Exception {
			MainUserSearchThread mainSearchThread=new MainUserSearchThread(bbox,isDB);
			new Thread(mainSearchThread).start();
			return null;
		}
		
	}
	
	public class BackgroundWorkerChange extends SwingWorker<String, Object>{
		private int workerCount;
		public BackgroundWorkerChange(int workerCount){
			this.workerCount=workerCount;
		}
		@Override
		protected String doInBackground() throws Exception {
			Flag.maxWorker=workerCount;
			return null;
		}
	}
	
	public class BackgroundProgressBarUpdater extends SwingWorker<String,Object>{
		private JProgressBar progressBarBBox;
		private JProgressBar progressBarEntry;
		
		public BackgroundProgressBarUpdater(JProgressBar progressBarBBox,JProgressBar progressBarEntry){
			this.progressBarBBox=progressBarBBox;
			this.progressBarEntry=progressBarEntry;
		}
		@Override
		protected String doInBackground() throws Exception {
			while(true){
				Sleep.sleep(100);
				progressBarBBox.setMaximum(Flag.totalBBox4Area);
				progressBarBBox.setValue(Flag.searchedBBox4Area);
				progressBarBBox.setString(Flag.bboxCrawlingNow);
				
				progressBarEntry.setMaximum(Flag.totalEntry4BBox);
				progressBarEntry.setValue(Flag.searchedEntry4BBox);
				progressBarEntry.setString(Flag.searchedEntry4BBox+"/"+Flag.totalEntry4BBox);
			}
		}
	}
	
	public class BackgroundTextFieldUpdater extends SwingWorker<String,Object>{
		private JTextField textFieldSystemMessage;
		private JTextField textFieldTotalEntrySearched;
		private JTextField textFieldTotalUserCrawled;
		private JTextField textFieldLastUserCrawled;
		
		public BackgroundTextFieldUpdater(JTextField textFieldSystemMessage,JTextField textFieldTotalEntrySearched,JTextField textFieldTotalUserCrawled,JTextField textFieldLastUserCrawled){
			this.textFieldSystemMessage=textFieldSystemMessage;
			this.textFieldTotalEntrySearched=textFieldTotalEntrySearched;
			this.textFieldTotalUserCrawled=textFieldTotalUserCrawled;
			this.textFieldLastUserCrawled=textFieldLastUserCrawled;
		}
		@Override
		protected String doInBackground() throws Exception {
			while(true){
				Sleep.sleep(1000);
				if(Flag.sleepFlag){
					textFieldSystemMessage.setBackground(Color.red);
					textFieldSystemMessage.setText(Flag.systemMessageText);
				}else{
					textFieldSystemMessage.setBackground(new Color(50, 205, 50));
					textFieldSystemMessage.setText("System is Working");
					
					textFieldTotalEntrySearched.setText(Flag.totalEntrySearched+"");
					textFieldTotalUserCrawled.setText(Flag.totalUserCrawled+"");
					textFieldLastUserCrawled.setText(Flag.lastUserCrawled);
				}
				
			}
		}
		
	}
	
	public class BackgroundPanelUpdater extends SwingWorker<String,Object>{

		private JPanel panelBBoxMap;
		private BBox bbox;
		public BackgroundPanelUpdater(JPanel panelBBoxMap,BBox bbox){
			this.panelBBoxMap=panelBBoxMap;
			this.bbox=bbox;
		}
		public int getBoundsZoomLevel(double latMax,double lngMax,double latMin,double lngMin, int width, int height) {  
			final int GLOBE_WIDTH = 256; // a constant in Google's map projection  
			final int ZOOM_MAX = 21;  
			double latFraction = (latRad(latMax) - latRad(latMin)) / Math.PI;  
			double lngDiff = lngMax - lngMin;  
			double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;  
			double latZoom = zoom(height, GLOBE_WIDTH, latFraction);  
			double lngZoom = zoom(width, GLOBE_WIDTH, lngFraction);  
			double zoom = Math.min(Math.min(latZoom, lngZoom),ZOOM_MAX);  
			return (int)(zoom);  
		}  
		private double latRad(double lat) {  
			double sin = Math.sin(lat * Math.PI / 180);  
			double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;  
			return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;  
		}  
		private double zoom(double mapPx, double worldPx, double fraction) {  
			final double LN2 = .693147180559945309417;  
			return (Math.log(mapPx / worldPx / fraction) / LN2);  
		}  
		@Override
		protected String doInBackground() {
			double latMax=bbox.getMaxLat(),latMin=bbox.getMinLat(),lngMax=bbox.getMaxLong(),lngMin=bbox.getMinLong();
			
			int zoomLevel=getBoundsZoomLevel(latMax,lngMax,latMin,lngMin,256,256);
			
			double latCenter=(latMax+latMin)/2, longCenter=(lngMax+lngMin)/2;
			String path;
			URL url;
			BufferedImage image;
			ImageIcon icon=new ImageIcon();
			JLabel label=new JLabel();
			
			String color= "0x"+Integer.toHexString((int)(Math.random()*16777215));
			
			while(Flag.bboxCenter==null){
				Sleep.sleep(200);
			}
			
			while(true){
				try{
//					path = "https://maps.googleapis.com/maps/api/staticmap?center="+latCenter+","+longCenter+"&zoom="+(zoomLevel)+"&size=475x250" +
//							"&markers=color:red%7Clabel:C%7C"+latCenter+","+longCenter+
//							"&markers=color:red%7Clabel:1%7C"+latMin+","+lngMin+
//							"&markers=color:red%7Clabel:2%7C"+latMin+","+lngMax+
//							"&markers=color:red%7Clabel:3%7C"+latMax+","+lngMax+
//							"&markers=color:red%7Clabel:4%7C"+latMax+","+lngMin+
//							"&markers=color:"+color+"%7Clabel:S%7C"+Flag.bboxCenter;
					
					path="https://maps.googleapis.com/maps/api/staticmap?center="+latCenter+","+longCenter+"&zoom="+(zoomLevel)+"&size=475x256" +
						 "&path=color:0x0000ff|weight:3|"
						 + latMin+","+lngMin+"|"
						 + latMin+","+lngMax+"|"
						 + latMax+","+lngMax+"|"
						 + latMax+","+lngMin+"|"
						 + latMin+","+lngMin
						 +"&markers=color:red%7Clabel:C%7C"+latCenter+","+longCenter
						 +"&markers=color:"+color+"%7Clabel:S%7C"+Flag.bboxCenter;
					url= new URL(path);
					image = ImageIO.read(url);
					icon.setImage(image);
					label.setIcon(icon);
					panelBBoxMap.removeAll();
					panelBBoxMap.revalidate();
					panelBBoxMap.add(label);
					panelBBoxMap.revalidate();
					panelBBoxMap.repaint();
					panelBBoxMap.setVisible(true);
					panelBBoxMap.revalidate();
					
					url=null;
				}catch(Exception e){}
				Sleep.sleep(60*1000);
				color= "0x"+Integer.toHexString((int)(Math.random()*16777215));
			}

			
		}
		
		
	}

	public class BackgroundPhotoSearch extends SwingWorker<String, Object>{

		private JProgressBar progressBarUser;
		private JProgressBar progressBarPhoto;
		
		public BackgroundPhotoSearch(JProgressBar progressBarUser,JProgressBar progressBarPhoto){
			this.progressBarUser=progressBarUser;
			this.progressBarPhoto=progressBarPhoto;
		}
		@Override
		protected String doInBackground() throws Exception {
			MainPhotoSearchThread mainPhotoSearchThread=new MainPhotoSearchThread();
			new Thread(mainPhotoSearchThread).start();
			
			while(true){
				Sleep.sleep(100);
				progressBarUser.setMaximum(Flag.totalUser);
				progressBarUser.setValue(Flag.totalUserCrawled);
				progressBarUser.setString(Flag.userCrawlingNow + "("+Flag.totalUserCrawled +" / "+Flag.totalUser+")");
				
				progressBarPhoto.setMaximum(Flag.totalPhoto4User);
				progressBarPhoto.setValue(Flag.searchedPhoto4User);
				progressBarPhoto.setString("("+Flag.searchedPhoto4User +" / "+Flag.totalPhoto4User+")");
			}
		}
		
	}
	
	public class BackgroundPhotoTextFieldUpdater extends SwingWorker<String, Object>{
		private JTextField textFieldTotalPhotoSearched;
		private JTextField textFieldTotalPhotoCrawled;
		private JTextField textFieldLastPhotoCrawled;
		
		public BackgroundPhotoTextFieldUpdater(JTextField textFieldTotalPhotoSearched,JTextField textFieldTotalPhotoCrawled,JTextField textFieldLastPhotoCrawled){
			this.textFieldTotalPhotoSearched=textFieldTotalPhotoSearched;
			this.textFieldTotalPhotoCrawled=textFieldTotalPhotoCrawled;
			this.textFieldLastPhotoCrawled=textFieldLastPhotoCrawled;
		}

		@Override
		protected String doInBackground() throws Exception {
			while(true){
				Sleep.sleep(1000);
				if(Flag.sleepFlag){
					textFieldSystemMessage.setBackground(Color.red);
					textFieldSystemMessage.setText(Flag.systemMessageText);
				}else{
					textFieldSystemMessage.setBackground(new Color(50, 205, 50));
					textFieldSystemMessage.setText("System is Working");
					
					textFieldTotalPhotoSearched.setText(Flag.totalPhotoSearched+"");
					textFieldTotalPhotoCrawled.setText(Flag.totalPhotoCrawled+"");
					textFieldLastPhotoCrawled.setText(Flag.lastPhotoCrawled);
				}
			}
		}
	}

	public class BackgroundDownloader extends SwingWorker<String, Object>{
		private JProgressBar progressBarUserDownload;
		private JProgressBar progressBarPhotoDownload;
		
		public BackgroundDownloader(JProgressBar progressBarUserDownload,JProgressBar progressBarPhotoDownload){
			this.progressBarUserDownload=progressBarUserDownload;
			this.progressBarPhotoDownload=progressBarPhotoDownload;
		}
		@Override
		protected String doInBackground() throws Exception {
			EU_DownloaderMain downloaderMain=new EU_DownloaderMain();
			new Thread(downloaderMain).start();
			
			while(true){
				Flag.getPausePerm();
				
				Sleep.sleep(100);
				progressBarUserDownload.setMaximum(Flag.totalUser);
				progressBarUserDownload.setValue(Flag.totalUserDownloaded);
				progressBarUserDownload.setString(Flag.userDownloadingNow + "("+Flag.totalUserDownloaded +" / "+Flag.totalUser+")");
				
				progressBarPhotoDownload.setMaximum(Flag.totalPhoto4User);
				progressBarPhotoDownload.setValue(Flag.downloadedPhoto4User);
				progressBarPhotoDownload.setString("("+Flag.downloadedPhoto4User +" / "+Flag.totalPhoto4User+")");
			}
		}
	}
}
