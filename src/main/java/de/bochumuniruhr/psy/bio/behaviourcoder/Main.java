package de.bochumuniruhr.psy.bio.behaviourcoder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.FileChooser;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyPressHandler;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.config.TrialCreationFrame;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.InfoPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.details.DetailsPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.instant.InstantBehaviourPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location.LocationPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.timed.TimedBehaviourPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.MediaControlPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VLCVideoPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.ExcelWriter;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.TrialValidator;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.ValidationError;
import lombok.extern.slf4j.Slf4j;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * Main class for BehaviourCoder, constructs the entire GUI.
 */
@Slf4j
public class Main {

	/**
	 * The panel that allows locations to be chosen.
	 */
	private LocationPanel locationPanel;
	
	/**
	 * The panel that allows timed behaviours to be toggled.
	 */
	private TimedBehaviourPanel timedBehaviourPanel;
	
	/**
	 * The panel that allows instant behaviours to be triggered.
	 */
	private InstantBehaviourPanel instantBehaviourPanel;
	
	/**
	 * The panel that provides the details of the trial.
	 */
	private DetailsPanel detailsPanel;
	
	/**
	 * The bar used to display status messages.
	 */
	private StatusPanel statusBar;
	
	/**
	 * The file chooser to select videos and save files.
	 */
	private FileChooser fileChooser;
	
	/**
	 * The listener for all key presses.
	 */
	private GlobalKeyPressHandler globalKeyHandler;
	
	/**
	 * The panel that shows information about the trial.
	 */
	private InfoPanel infoPanel;
	
	/**
	 * The panel that provides control over the video.
	 */
	private MediaControlPanel mediaControlPanel;
	
	/**
	 * The panel that displays the video.
	 */
	private VLCVideoPanel vlcVideoPanel;
	
	/**
	 * Whether VLC was found.
	 */
	private boolean foundVlc;
	
	/**
	 * The current trial.
	 */
	private Trial trial;

	/**
	 * The default time limit for a trial.
	 */
	private int DEFAULT_TIME_LIMIT = 120;

	/**
	 * The main window
	 */
	private JFrame frame;
	
	private File trialFile;
	
	private File video;
	
	private boolean unsaved;

	public static void main(String[] args) {

		String osName = System.getProperty("os.name").toLowerCase();

		
		if (osName.startsWith("win")) { 
		final boolean found = new NativeDiscovery(new BundledVLCLibsDiscoveryStrategy()).discover();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main(found);
			}
		});
		} else { 
			final boolean found = new NativeDiscovery().discover();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new Main(found);
				}
			});
		}
	}

	/**
	 * Creates the main window.
	 * 
	 * @param found - whether VLC was found
	 */
	public Main(boolean found) {
		this.foundVlc = true;
		unsaved = false;
		KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		globalKeyHandler = new GlobalKeyPressHandler(currentKeyboardFocusManager, this);
		
		trial = null;
		setupUI(null, null, null);
	}

	/**
	 * Saves a file. 
	 */
	public void save() {
		save(trialFile);
	}

	/**
	 * Setups the GUI.
	 */
	private void setupUI(List<Character> timedTriggerKeys, List<Character> instantIncrementKeys, List<Character> instantDecrementKeys) {
		setupUIComponents(timedTriggerKeys, instantIncrementKeys, instantDecrementKeys);
		if (foundVlc) { 
			log.info("Found VLC version: " + LibVlc.INSTANCE.libvlc_get_version());
		} else { 
			JOptionPane.showMessageDialog(frame, "VLC not found automatically. Please choose the VLC library from the menu.");
			log.info("VLC not found");
		}
	}

	/**
	 * Setups the GUI components.
	 */
	private void setupUIComponents(List<Character> timedTriggerKeys, List<Character> instantIncrementKeys, List<Character> instantDecrementKeys) {
		setNimbusLookAndFeel();

		//Create frame
		if (frame != null){
			frame.dispose();
		}
		frame = configureJFrame();

		//Create the menu bar
		JMenuBar menuBar = configureJMenuBar();
		frame.setJMenuBar(menuBar);

		//Create the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		//Create the file chooser
		fileChooser = new FileChooser(frame);

		//Create each internal panel
		statusBar = new StatusPanel(frame);
		vlcVideoPanel = new VLCVideoPanel((trial == null) ? null : trial.getDetails());
		mediaControlPanel = new MediaControlPanel();
		
		//Add the video panel
		GridBagConstraints videoPanelConstraints = createConstraints(0, 4, 30, 11, GridBagConstraints.BOTH);
		videoPanelConstraints.weightx = 1;
		videoPanelConstraints.weighty = 1;
		videoPanelConstraints.anchor = GridBagConstraints.CENTER;
		mainPanel.add(vlcVideoPanel, videoPanelConstraints);
		
		//Add the media control panel
		mainPanel.add(mediaControlPanel, createConstraints(0, 15, 30, 3, GridBagConstraints.HORIZONTAL));

		if (trial != null){
			//Create each internal panel
			timedBehaviourPanel = new TimedBehaviourPanel(trial, timedTriggerKeys);
			locationPanel = new LocationPanel(trial);
			instantBehaviourPanel = new InstantBehaviourPanel(trial,
					instantIncrementKeys, instantDecrementKeys);
			detailsPanel = new DetailsPanel(trial);
			infoPanel = new InfoPanel(trial, statusBar);
			
			//Add trial listeners
			trial.addListener(vlcVideoPanel);
			trial.addListener(mediaControlPanel);
			trial.addListener(infoPanel);
			trial.addListener(locationPanel);
			trial.addListener(statusBar);
			trial.addListener(instantBehaviourPanel);
			trial.addListener(timedBehaviourPanel);
			
			//Add video listeners
			vlcVideoPanel.addVideoListener(mediaControlPanel);
			vlcVideoPanel.addVideoListener(trial);
			vlcVideoPanel.addVideoListener(locationPanel);
			
			//Add media listeners
			mediaControlPanel.addMediaControlListener(vlcVideoPanel);
			
			//Add key listeners
			globalKeyHandler.register(instantBehaviourPanel);
			globalKeyHandler.register(timedBehaviourPanel);
			
			//Add the information panel
			GridBagConstraints infoPanelConstraints = createConstraints(0, 0, 30, 2, GridBagConstraints.HORIZONTAL);
			infoPanelConstraints.weightx = 1;
			mainPanel.add(infoPanel, infoPanelConstraints);
			
			//Add the details panel
			mainPanel.add(detailsPanel, createConstraints(0, 2, 30, 2, GridBagConstraints.HORIZONTAL));
	
			//Add the location panel
			GridBagConstraints locationPanelConstraints = createConstraints(8, 19, 16, 12, GridBagConstraints.BOTH);
			locationPanelConstraints.weightx = 1;
			mainPanel.add(locationPanel, locationPanelConstraints);
	
			//Add the instant behaviour panel
			GridBagConstraints instantBehaviourPanelConstraints = createConstraints(23, 19, 7, 12, GridBagConstraints.VERTICAL);
			instantBehaviourPanelConstraints.anchor = GridBagConstraints.LINE_END;
			mainPanel.add(instantBehaviourPanel, instantBehaviourPanelConstraints);
	
			//Add the timed behaviour panel
			mainPanel.add(timedBehaviourPanel, createConstraints(0, 19, 7, 12, GridBagConstraints.BOTH)); // 0, 17
		}
	
		//Create and add the mirror label
		/*JLabel mirrorLabel = new JLabel("Mirror / Divider");
		mirrorLabel.setFont(new Font("Arial", Font.BOLD, 15));
		mirrorLabel.setHorizontalAlignment(JLabel.CENTER);
		mirrorLabel.setBackground(Color.BLACK);
		mirrorLabel.setOpaque(true);
		mirrorLabel.setForeground(Color.WHITE);
		GridBagConstraints mirrorLabelConstraints = createConstraints(8, 18, 16, 1, GridBagConstraints.HORIZONTAL);
		mirrorLabelConstraints.weightx = 1;
		mainPanel.add(mirrorLabel, mirrorLabelConstraints);*/
	
		//Add the status bar
		mainPanel.add(statusBar, createConstraints(0, 31, 30, 1, GridBagConstraints.HORIZONTAL));

		//Prepare frame to be displayed
		mainPanel.validate();
		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);

	}

	/**
	 * Creates the menu bar.
	 * 
	 * @return The created menu bar.
	 */
	private JMenuBar configureJMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		//Note no OS check is needed as non apple systems will ignore this property
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		JMenu programMenu = configureProgramMenu();
		menuBar.add(programMenu);
		
		JMenu helpMenu = configureHelpMenu();
		menuBar.add(helpMenu);
		
		return menuBar;
	}

	/**
	 * Creates the help menu.
	 * 
	 * @return The created menu.
	 */
	private JMenu configureHelpMenu() {
		JMenu menu = new JMenu("Help");
		
		//Add option to display version
		JMenuItem help = new JMenuItem("Version");
		help.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Properties properties = new Properties();
				try {
					InputStream propertyStream = this.getClass().getResourceAsStream("/git.properties");
					properties.load(propertyStream);
					propertyStream.close();
				} catch (IOException e1) {
						log.error(e1.getMessage());
				}
				
				String commitHash = properties.getProperty("git.commit.id.describe-short");			
				String buildTime = properties.getProperty("git.build.time");
				String version = buildTime + "_" + commitHash;
				JOptionPane.showMessageDialog(frame, "Version: " + version + System.lineSeparator() + "Release date: " + buildTime);
			}});
		menu.add(help);
		
		return menu;
	}

	/**
	 * Creates the main menu.
	 * 
	 * @return The created menu.
	 */
	private JMenu configureProgramMenu() {
		JMenu menu = new JMenu("Main");
		final Main that = this;
		JPopupMenu.setDefaultLightWeightPopupEnabled(false); 
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		//Add the option to reset
		JMenuItem newTrial = new JMenuItem("Setup new experiment");
		menu.add(newTrial);
		newTrial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TrialCreationFrame(frame, that);
			}
		});
		
		JMenuItem loadTrial = new JMenuItem("Load experiment settings");
		menu.add(loadTrial);
		loadTrial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = fileChooser.chooseSpreadsheet("Load");

				if (file != null) {
					trialFile = file;
					
					ExcelWriter writer = new ExcelWriter(file);
					
					ExcelWriter.Config config = writer.load();
					trial = config.trial;
					
					setupUI(config.timedKeys, config.incInstantKeys, config.decInstantKeys);
				} else {
					statusBar.setMessage("Trial load cancelled.");
				}
			}
		});
		
		menu.addSeparator();

		//Add option to open a video
		JMenuItem newSession = new JMenuItem("Open new video for analysis");
		menu.add(newSession);
		newSession.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unsaved) {
					int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to start a new session and lose your current data?", "New Session",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.NO_OPTION){
						statusBar.setMessage("New session cancelled.");
						return;
					}
				}
				
				detailsPanel.resetAll();
				trial.reset();
				File file = fileChooser.chooseVideo();

				if (file != null) {
					video = file;
					vlcVideoPanel.openVideo(video);
					unsaved = true;
					statusBar.setMessage("New session started.");
				} else {
					statusBar.setMessage("New session cancelled.");
				}
			}
		});
		
		JMenuItem restartSession = new JMenuItem("Restart analysis");
		menu.add(restartSession);
		restartSession.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unsaved) {
					int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to restart and lose your current data?", "Restart Session",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.NO_OPTION){
						statusBar.setMessage("Session restart cancelled.");
						return;
					}
				}
				trial.reset();

				//Load video again
				if (video != null) {
					vlcVideoPanel.openVideo(video);
				}
				statusBar.setMessage("Session restarted.");
			}
		});
		
		//Add option to save a trial
		JMenuItem save = new JMenuItem("Save data");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(trialFile);
			}
		});
		menu.add(save);

		menu.addSeparator();
		
		//Add option to exit the program
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exit);
		return menu;
	}
	
	/**
	 * Saves the trial to a spreadsheet.
	 * 
	 * @param file - the XLS file to save to
	 */
	private void save(File file) {
		if (file == null) {
			statusBar.setMessage("No trial to save");
			return;
		}
		
		ExcelWriter writer = new ExcelWriter(file);
		try {
			List<ValidationError> validationMessages = TrialValidator.validate(trial);
			if (validationMessages.isEmpty()) {
				//If valid trial data than save
				writer.write(trial);
				statusBar.setMessage("Saved OK");
				SoundMaker.playSave();
				unsaved = false;
			} else {
				//Otherwise report errors
				statusBar.showErrors(validationMessages);
				SoundMaker.playValidationError();
			}
		} catch (FileNotFoundException ex) {
			//Alert user if the file could not be found
			JOptionPane.showMessageDialog(frame, "Cannot save spreadsheet. Is it opened in another program?");
		}
	}
	
	/**
	 * Creates the frame.
	 * 
	 * @return The created frame.
	 */
	private JFrame configureJFrame() {
		//Create the frame
		JFrame frame = new JFrame("Behaviour Coder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

		//Set the icon
		BufferedImage image = null;
		try {
			image = ImageIO.read(ClassLoader.getSystemResource("FrameIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setIconImage(image);
		
		return frame;
	}

	/**
	 * Tries to set the look and feel to Nimbus
	 */
	private void setNimbusLookAndFeel() {
		try {
			//Try to set the look and feel to Nimbus
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			//Otherwise try a cross platform look and feel
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void onTrialCreationCancel() {
		statusBar.setMessage("Trial creation cancelled.");
	}
	
	public void onTrialCreate(Trial trial, List<Character> insIncKeys, List<Character> insDecKeys, List<Character> timKeys) {	
		File file = fileChooser.chooseSpreadsheet("Save experiment configuration");

		if (file != null) {
			if (!file.getName().endsWith(".xls")) { 
				file = new File(file.getName() + ".xls");
			}
			trialFile = file;
			ExcelWriter writer = new ExcelWriter(file);
			writer.setup(trial, insIncKeys, insDecKeys, timKeys);
			this.trial = trial;
			setupUI(timKeys, insIncKeys, insDecKeys);
		} else {
			statusBar.setMessage("Trial creation cancelled.");
		}
	}
	
	/**
	 * Creates constraints for placing elements in the UI.
	 * 
	 * @param x - the x position in the grid
	 * @param y - the y position in the grid
	 * @param width - how many cells wide the element will be
	 * @param height - how many cells high the element will be
	 * @param fill - the GridBagConstraints value for how the element will expand
	 * @return The created constraints with the given values.
	 */
	private GridBagConstraints createConstraints(int x, int y, int width, int height, int fill){
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = x;
		cons.gridy = y;
		cons.gridheight = height;
		cons.gridwidth = width;
		cons.fill = fill;
		return cons;
	}
}
