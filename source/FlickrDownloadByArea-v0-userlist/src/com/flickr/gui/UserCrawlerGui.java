package com.flickr.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

import com.flickr.flickr.BBox;
import com.flickr.function.Flag;
import com.flickr.function.Sleep;
import com.flickr.search.MainSearchThread;
import javax.swing.JCheckBox;

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
		setBounds(100, 100, 500, 660);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 153, 204));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSouthwestLatitude = new JLabel("Min (SouthWest) Latitude:");
		lblSouthwestLatitude.setBounds(10, 10, 160, 14);
		contentPane.add(lblSouthwestLatitude);
		
		JLabel lblSouthwestLongitude = new JLabel("Min (SouthWest) Longitude:");
		lblSouthwestLongitude.setBounds(10, 45, 160, 14);
		contentPane.add(lblSouthwestLongitude);
		
		textFieldMinLat = new JTextField();
		textFieldMinLat.setBounds(180, 5, 60, 25);
		contentPane.add(textFieldMinLat);
		textFieldMinLat.setColumns(10);
		
		textFieldMinLong = new JTextField();
		textFieldMinLong.setColumns(10);
		textFieldMinLong.setBounds(180, 40, 60, 25);
		contentPane.add(textFieldMinLong);
		
		JLabel lblMaxnortheastLatitude = new JLabel("Max (NorthEast) Latitude:");
		lblMaxnortheastLatitude.setBounds(255, 10, 160, 14);
		contentPane.add(lblMaxnortheastLatitude);
		
		textFieldMaxLat = new JTextField();
		textFieldMaxLat.setColumns(10);
		textFieldMaxLat.setBounds(425, 5, 60, 25);
		contentPane.add(textFieldMaxLat);
		
		JLabel lblMaxnortheastLongitude = new JLabel("Max (NorthEast) Longitude:");
		lblMaxnortheastLongitude.setBounds(255, 45, 160, 14);
		contentPane.add(lblMaxnortheastLongitude);
		
		textFieldMaxLong = new JTextField();
		textFieldMaxLong.setColumns(10);
		textFieldMaxLong.setBounds(425, 40, 60, 25);
		contentPane.add(textFieldMaxLong);
		
		JButton btnStartButton = new JButton("Start Crawling");
		btnStartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BBox bbox=new BBox(Double.parseDouble(textFieldMinLong.getText()), Double.parseDouble(textFieldMinLat.getText()), Double.parseDouble(textFieldMaxLong.getText()), Double.parseDouble(textFieldMaxLat.getText()));
				
				(new BackgroundSearch(bbox,chckbxGetfromdb.isSelected())).execute();
				
				(new BackgroundProgressBarUpdater(progressBarBBox,progressBarEntry)).execute();
				
				(new BackgroundTextFieldUpdater(textFieldSystemMessage,textFieldTotalEntrySearched,textFieldTotalUserCrawled,textFieldLastUserCrawled)).execute();
				
				(new BackgroundPanelUpdater(panelBBoxMap,bbox)).execute();
			}
			
		});
		btnStartButton.setBounds(383, 110, 101, 25);
		contentPane.add(btnStartButton);
		
		textFieldSystemMessage = new JTextField();
		textFieldSystemMessage.setBackground(new Color(50, 205, 50));
		textFieldSystemMessage.setEditable(false);
		textFieldSystemMessage.setBounds(125, 110, 231, 25);
		contentPane.add(textFieldSystemMessage);
		textFieldSystemMessage.setColumns(10);
		
		JLabel lblSystemMode = new JLabel("System Message:");
		lblSystemMode.setBounds(10, 115, 115, 14);
		contentPane.add(lblSystemMode);
		
		progressBarBBox = new JProgressBar();
		progressBarBBox.setToolTipText("BBox Crawling Now/ Center Point");
		progressBarBBox.setBounds(10, 145, 475, 30);
		progressBarBBox.setStringPainted(true);
		contentPane.add(progressBarBBox);
		
		progressBarEntry = new JProgressBar();
		progressBarEntry.setToolTipText("Searched / Total Entry Count");
		progressBarEntry.setBounds(10, 440, 475, 30);
		progressBarEntry.setStringPainted(true);
		contentPane.add(progressBarEntry);
		
		JLabel lblTotalEntrySearched = new JLabel("Total Entry Searched:");
		lblTotalEntrySearched.setBounds(10, 485, 133, 14);
		contentPane.add(lblTotalEntrySearched);
		
		JLabel lblTotalUserCrawled = new JLabel("Total User Crawled:");
		lblTotalUserCrawled.setBounds(10, 520, 133, 14);
		contentPane.add(lblTotalUserCrawled);
		
		JLabel lblLastUserCrawled = new JLabel("Last User Crawled:");
		lblLastUserCrawled.setBounds(10, 555, 133, 14);
		contentPane.add(lblLastUserCrawled);
		
		panelBBoxMap = new JPanel();
		panelBBoxMap.setBounds(10, 180, 475, 256);
		contentPane.add(panelBBoxMap);
		
		textFieldTotalEntrySearched = new JTextField();
		textFieldTotalEntrySearched.setEditable(false);
		textFieldTotalEntrySearched.setColumns(10);
		textFieldTotalEntrySearched.setBackground(SystemColor.menu);
		textFieldTotalEntrySearched.setBounds(153, 480, 168, 25);
		contentPane.add(textFieldTotalEntrySearched);
		
		textFieldTotalUserCrawled = new JTextField();
		textFieldTotalUserCrawled.setEditable(false);
		textFieldTotalUserCrawled.setColumns(10);
		textFieldTotalUserCrawled.setBackground(SystemColor.info);
		textFieldTotalUserCrawled.setBounds(153, 515, 168, 25);
		contentPane.add(textFieldTotalUserCrawled);
		
		textFieldLastUserCrawled = new JTextField();
		textFieldLastUserCrawled.setEditable(false);
		textFieldLastUserCrawled.setColumns(10);
		textFieldLastUserCrawled.setBackground(new Color(250, 240, 230));
		textFieldLastUserCrawled.setBounds(153, 550, 332, 25);
		contentPane.add(textFieldLastUserCrawled);
		
		JLabel lblMaxWorkerCount = new JLabel("Max Worker Count:");
		lblMaxWorkerCount.setBounds(10, 80, 160, 14);
		contentPane.add(lblMaxWorkerCount);
		
		textFieldMaxWorker = new JTextField();
		textFieldMaxWorker.setColumns(10);
		textFieldMaxWorker.setBounds(180, 74, 60, 25);
		contentPane.add(textFieldMaxWorker);
		
		JButton btnSetWorker = new JButton("Set Worker");
		btnSetWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BackgroundWorkerChange task = new BackgroundWorkerChange(Integer.parseInt(textFieldMaxWorker.getText())); //***//
				task.execute(); //***//
			}
		});
		btnSetWorker.setBounds(255, 74, 101, 25);
		contentPane.add(btnSetWorker);
		
		lblErkamUzun = new JLabel("Erkam Uzun");
		lblErkamUzun.setBounds(399, 607, 85, 14);
		contentPane.add(lblErkamUzun);
		
		chckbxGetfromdb = new JCheckBox("GetFromDB");
		chckbxGetfromdb.setBounds(383, 80, 102, 23);
		contentPane.add(chckbxGetfromdb);
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
			MainSearchThread mainSearchThread=new MainSearchThread(bbox,isDB);
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
}
