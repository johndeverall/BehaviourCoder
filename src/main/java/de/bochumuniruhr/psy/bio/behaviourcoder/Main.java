package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.counter.CounterPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.DetailsPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.details.ValidationError;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.ExcelWriter;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.FileChooser;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.action.ActionTimerPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.timer.location.LocationTimerPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.JavaFXVideoPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.MediaControlPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VLCVideoPanel;
//import nz.co.thescene.emailing.SMTPMailer;
import de.bochumuniruhr.psy.bio.behaviourcoder.video.VideoListener;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Main implements VideoListener {

	private LocationTimerPanel locationTimerPanel;
	private ActionTimerPanel actionTimerPanel;
	private CounterPanel counterPanel;
	private DetailsPanel detailsPanel;
	private StatusPanel statusPanel;
	private FileChooser fileChooser;
	private GlobalKeyPressHandler globalKeyHandler;
	//private JavaFXVideoPanel videoPanel;
	private JLabel mirrorLabel;
	private InfoPanel infoPanel;
	private MediaControlPanel mediaControlPanel;
	private VLCVideoPanel vlcVideoPanel;
	private boolean foundVlc;
	private Logger logger = Logger.getLogger(this.getClass());

	private int DEFAULT_TIME_LIMIT = 120;

	private JFrame frame;

	public static void main(String[] args) {

		//boolean found = new NativeDiscovery(new CustomNativeDiscoveryStrategy()).discover();
		final boolean found = new NativeDiscovery().discover();

		
//		 String vlcHome = "c:/thescene/BehaviourCoder/target/classes"; // Dir with vlc.dll and vlccore.dll
//	        NativeLibrary.addSearchPath(
//	            RuntimeUtil.getLibVlcLibraryName(), vlcHome
//	        );
//	        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//	        
		//System.out.println(LibVlc.INSTANCE.libvlc_get_version());
		//NativeLibrary.addSearchPath("", "c:/thescene/BehaviourCoder/target/classes");
		//new NativeDiscovery().discover();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Main app = new Main(found);
			}
		});
	}

	public Main(boolean found) {
		this.foundVlc = true;
		KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		globalKeyHandler = new GlobalKeyPressHandler(currentKeyboardFocusManager, this);
		setupUI();
	}

	public void save() {
		File file = fileChooser.getSelectedFile();
		if (file == null) {
			showSaveDialog();
		} else {
			save(file);
		}
	}

	private void setupUI() {
		setupUIComponents();
		if (foundVlc) { 
			logger.info("Found VLC version: " + LibVlc.INSTANCE.libvlc_get_version());
		} else { 
			JOptionPane.showMessageDialog(frame, "VLC not found automatically. Please choose the VLC library from the menu.");
			logger.info("VLC not found");
		}
	}

	private void setupUIComponents() {
		setNimbusLookAndFeel();

		frame = configureJFrame();

		JMenuBar menuBar = configureJMenuBar();
		frame.setJMenuBar(menuBar);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		statusPanel = new StatusPanel(frame);
		actionTimerPanel = new ActionTimerPanel(statusPanel, DEFAULT_TIME_LIMIT);
		locationTimerPanel = new LocationTimerPanel(statusPanel, DEFAULT_TIME_LIMIT);
		counterPanel = new CounterPanel(statusPanel);
		//videoPanel = new JavaFXVideoPanel();
		vlcVideoPanel = new VLCVideoPanel();
		mediaControlPanel = new MediaControlPanel();
		mediaControlPanel.addMediaControlListener(vlcVideoPanel);
		locationTimerPanel.addTrialSectionListener(actionTimerPanel);
		locationTimerPanel.addTrialSectionListener(counterPanel);
		locationTimerPanel.addTrialSectionListener(vlcVideoPanel);
		locationTimerPanel.addTrialSectionListener(mediaControlPanel);
		vlcVideoPanel.addVideoListener(mediaControlPanel);
		vlcVideoPanel.addVideoListener(locationTimerPanel);
		vlcVideoPanel.addVideoListener(counterPanel);
		vlcVideoPanel.addVideoListener(actionTimerPanel);
		vlcVideoPanel.addVideoListener(this);
		infoPanel = new InfoPanel(DEFAULT_TIME_LIMIT);
		infoPanel.addTrialSectionListener(locationTimerPanel);
		locationTimerPanel.addTrialSectionListener(infoPanel);
		globalKeyHandler.register(counterPanel);
		detailsPanel = new DetailsPanel(statusPanel);
		fileChooser = new FileChooser(statusPanel);

		mirrorLabel = new JLabel("Mirror / Divider");
		mirrorLabel.setFont(new Font("Arial", Font.BOLD, 15));
		mirrorLabel.setHorizontalAlignment(JLabel.CENTER);
		mirrorLabel.setBackground(Color.BLACK);
		mirrorLabel.setOpaque(true);
		mirrorLabel.setForeground(Color.WHITE);

		GridBagConstraints infoPanelConstraints = new GridBagConstraints();
		infoPanelConstraints.gridx = 0;
		infoPanelConstraints.gridy = 0;
		infoPanelConstraints.gridheight = 2;
		infoPanelConstraints.gridwidth = 30;
		infoPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		infoPanelConstraints.weightx = 1;
		mainPanel.add(infoPanel, infoPanelConstraints);

		GridBagConstraints detailsPanelConstraints = new GridBagConstraints();
		detailsPanelConstraints.gridx = 0;
		detailsPanelConstraints.gridy = 2;
		detailsPanelConstraints.gridheight = 2;
		detailsPanelConstraints.gridwidth = 30;
		detailsPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(detailsPanel, detailsPanelConstraints);

		GridBagConstraints videoPanelConstraints = new GridBagConstraints();
		videoPanelConstraints.gridx = 0;
		videoPanelConstraints.gridy = 4;
		videoPanelConstraints.gridheight = 11;
		videoPanelConstraints.gridwidth = 30;
		videoPanelConstraints.fill = GridBagConstraints.BOTH;
		videoPanelConstraints.weightx = 1;
		videoPanelConstraints.weighty = 1;
		videoPanelConstraints.anchor = GridBagConstraints.CENTER;
		//mainPanel.add(videoPanel, videoPanelConstraints);
		mainPanel.add(vlcVideoPanel, videoPanelConstraints);

		GridBagConstraints mediaControlPanelConstraints = new GridBagConstraints();
		mediaControlPanelConstraints.gridx = 8;
		mediaControlPanelConstraints.gridy = 15;
		mediaControlPanelConstraints.gridheight = 3;
		mediaControlPanelConstraints.gridwidth = 16;
		mediaControlPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mediaControlPanel, mediaControlPanelConstraints);

		GridBagConstraints mirrorLabelConstraints = new GridBagConstraints();
		mirrorLabelConstraints.gridx = 8;
		mirrorLabelConstraints.gridy = 18;
		mirrorLabelConstraints.gridwidth = 16;
		mirrorLabelConstraints.gridheight = 1;
		mirrorLabelConstraints.weightx = 1;
		mirrorLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(mirrorLabel, mirrorLabelConstraints);

		GridBagConstraints locationPanelConstraints = new GridBagConstraints();
		locationPanelConstraints.gridx = 8;
		locationPanelConstraints.gridy = 19;
		locationPanelConstraints.gridheight = 12;
		locationPanelConstraints.gridwidth = 16;
		locationPanelConstraints.weightx = 1;
		locationPanelConstraints.fill = GridBagConstraints.BOTH;
		mainPanel.add(locationTimerPanel, locationPanelConstraints);

		GridBagConstraints statusPanelConstraints = new GridBagConstraints();
		statusPanelConstraints.gridx = 0;
		statusPanelConstraints.gridy = 31;
		statusPanelConstraints.gridheight = 1;
		statusPanelConstraints.gridwidth = 30;
		statusPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(statusPanel, statusPanelConstraints);

		GridBagConstraints counterPanelConstraints = new GridBagConstraints();
		counterPanelConstraints.gridx = 23;
		counterPanelConstraints.gridy = 19;
		counterPanelConstraints.gridheight = 12;
		counterPanelConstraints.gridwidth = 7;
		counterPanelConstraints.anchor = GridBagConstraints.LINE_END;
		counterPanelConstraints.fill = GridBagConstraints.VERTICAL;
		mainPanel.add(counterPanel, counterPanelConstraints);

		GridBagConstraints actionTimerPanelConstraints = new GridBagConstraints();
		actionTimerPanelConstraints.gridx = 0;
		actionTimerPanelConstraints.gridy = 19;
		actionTimerPanelConstraints.gridheight = 12;
		actionTimerPanelConstraints.gridwidth = 7;
		actionTimerPanelConstraints.fill = GridBagConstraints.BOTH;
		mainPanel.add(actionTimerPanel, actionTimerPanelConstraints); // 0, 17

		mainPanel.validate();

		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);

	}

	private JMenuBar configureJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Program");

		JMenuItem reset = new JMenuItem("Reset");
		menu.add(reset);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationTimerPanel.resetAll();
				counterPanel.resetAll();
				detailsPanel.resetAll();
				actionTimerPanel.resetAll();
				infoPanel.resetAll();
				//videoPanel.resetAll();
				vlcVideoPanel.resetAll();
				mediaControlPanel.resetAll();
			}
		});

		JMenuItem chooseVideo = new JMenuItem("Open video");
		menu.add(chooseVideo);
		chooseVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLoadVideoDialog();
			}
		});
		
		JMenuItem findVlcLibrary = new JMenuItem("Find VLC Library");
		findVlcLibrary.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				showFindVlcLibraryDialog();
			}
		});
		menu.add(findVlcLibrary);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog();
			}
		});
		menu.add(save);

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exit);

		menuBar.add(menu);
		return menuBar;
	}

	private void showSaveDialog() {
		int returnVal = fileChooser.showSaveDialog(frame);

		if (returnVal == FileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			save(file);
		} else {
			statusPanel.setMessage("Save cancelled.");
		}
	}

	private void showLoadVideoDialog() {
		int returnVal = fileChooser.showOpenDialog(frame);

		if (returnVal == FileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			openVideo(file);
		} else {
			statusPanel.setMessage("Open video cancelled.");
		}
	}
	
	private void showFindVlcLibraryDialog() {
		fileChooser.setDialogTitle("Find libvlc");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnVal = fileChooser.showOpenDialog(frame);
		if (returnVal == FileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			initVlc(file);
		} else {
			statusPanel.setMessage("Open video cancelled.");
		}
	}


	private void initVlc(File file) {
		String canonicalPath = "C:/Program Files (x86)/VideoLAN/VLC";
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), canonicalPath);
        System.out.println(LibVlc.INSTANCE.libvlc_get_version());		
	}

	private void save(File file) {
		ExcelWriter writer = new ExcelWriter(file);
		try {
			TrialSection trial = new TrialSection();
			List<ValidationError> validationMessages = detailsPanel.validateTrialData();
			validationMessages.addAll(locationTimerPanel.validateTrialData());
			if (validationMessages.isEmpty()) {
				detailsPanel.populateTrial(trial);
				locationTimerPanel.populateTrial(trial);
				actionTimerPanel.populateTrial(trial);
				counterPanel.populateTrial(trial);
				infoPanel.populateTrial(trial);
				//videoPanel.populateTrial(trial);
				vlcVideoPanel.populateTrial(trial);
				writer.write(trial);
				statusPanel.setMessage("Saved OK");
				SoundMaker.playSave();
			} else {
				statusPanel.showErrors(validationMessages);
				SoundMaker.playValidationError();
			}
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(frame, "Cannot save spreadsheet. Is it opened in another program?");
		}
//		if (file.canRead()) {
//			emailResults(file);
//		}
	}
	
//	private void emailResults(File file) {
//		SMTPMailer mailer = new SMTPMailer("", "");
//		try {
//			mailer.send("johndeverall@gmail.com", "Rooster Spreadsheet Update",
//					"Please find the latest spreadsheet attached.");
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//	}

	private void openVideo(File file) {
		vlcVideoPanel.openVideo(file, statusPanel);
	}

	private JFrame configureJFrame() {
		JFrame frame = new JFrame("Behaviour Coder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		int height = 500;
//		int width = 1000;
//		frame.setSize(width, height);
		
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

		BufferedImage image = null;
		try {
			image = ImageIO.read(ClassLoader.getSystemResource("FrameIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.setIconImage(image);
		return frame;
	}

	private void setNimbusLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			setLookAndFeel();
		}
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoPositionChange(double videoPosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoError(String developerMessage) {
		String separator = System.getProperty("line.separator");
		String displayMessage = new StringBuilder()
				.append("The video file you tried opening could not be loaded." + separator)
				.append("It should be an MP4 with H.264/AVC video encoding and AAC audio encoding." + separator)
				.append("See http://docs.oracle.com/javafx/2/api/javafx/scene/media/package-summary.html#SupportedMediaTypes for details." + separator)
				.append(separator)
				.append("Developer message: " + developerMessage + separator)
				.toString();
		JOptionPane.showMessageDialog(frame, displayMessage);
	}

	@Override
	public void onVideoTimeChange(double videoTime) {
		// TODO Auto-generated method stub
		
	}

}
