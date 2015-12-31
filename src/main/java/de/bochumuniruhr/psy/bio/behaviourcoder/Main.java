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
import java.util.Arrays;
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

import de.bochumuniruhr.psy.bio.behaviourcoder.gui.FileChooser;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.GlobalKeyPressHandler;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.SoundMaker;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.advisory.StatusPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.InfoPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.details.DetailsPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.instant.InstantBehaviourPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.location.LocationPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.trial.timed.TimedBehaviourPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.MediaControlPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VLCVideoPanel;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.ExcelWriter;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.TrialValidator;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.ValidationError;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 * Main class for BehaviourCoder, constructs the entire GUI.
 */
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
	 * The logger for this class.
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
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

	public static void main(String[] args) {
		//TODO: Create cross platform solution
		System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC\\");
		
		final boolean found = new NativeDiscovery().discover();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main(found);
			}
		});
	}

	/**
	 * Creates the main window.
	 * 
	 * @param found - whether VLC was found
	 */
	public Main(boolean found) {
		this.foundVlc = true;
		KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		globalKeyHandler = new GlobalKeyPressHandler(currentKeyboardFocusManager, this);
		
		setupTrial();
		setupUI();
	}

	/**
	 * Saves a file. 
	 */
	public void save() {
		showSaveDialog();
	}
	
	/**
	 * TODO: Replace with trial load and trial create
	 * 
	 * Setups the trial to match previous versions.
	 */
	private void setupTrial(){
		trial = new Trial(DEFAULT_TIME_LIMIT, Arrays.asList(new Location("Close"), new Location("Far")),
				Arrays.asList("Trial Type", "Rooster ID", "Session Number", "Trial Number", "Section Number", "Mirror?"),
				Arrays.asList(new Constraint("BM", "NM", "BS", "NS", "WM", "WS"),
						new Constraint("554", "570", "547", "538", "546", "551", "559", "548", "544", "578", "579", "542", "567"),
						new Constraint(), new Constraint(), new Constraint(),
						new Constraint("0", "1")));
		
		trial.addInstantBehaviour(new InstantBehaviour("Peck", Color.YELLOW, trial, true));
		trial.addInstantBehaviour(new InstantBehaviour("Hackles", Color.GREEN, trial, true));
		trial.addInstantBehaviour(new InstantBehaviour("Attack", Color.RED, trial, true));
		trial.addInstantBehaviour(new InstantBehaviour("Crouch", Color.CYAN, trial, true));
		
		trial.addTimedBehaviour(new TimedBehaviour("Following", null, trial, true));
		trial.addTimedBehaviour(new TimedBehaviour("Facing Away", null, trial, true));
		trial.addTimedBehaviour(new TimedBehaviour("Grooming Mark", null, trial, true));
		trial.addTimedBehaviour(new TimedBehaviour("Grooming Other", null, trial, true));
	}

	/**
	 * Setups the GUI.
	 */
	private void setupUI() {
		setupUIComponents();
		if (foundVlc) { 
			logger.info("Found VLC version: " + LibVlc.INSTANCE.libvlc_get_version());
		} else { 
			JOptionPane.showMessageDialog(frame, "VLC not found automatically. Please choose the VLC library from the menu.");
			logger.info("VLC not found");
		}
	}

	/**
	 * Setups the GUI components.
	 */
	private void setupUIComponents() {
		setNimbusLookAndFeel();

		//Create frame
		frame = configureJFrame();

		//Create the menu bar
		JMenuBar menuBar = configureJMenuBar();
		frame.setJMenuBar(menuBar);

		//Create the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		//Create each internal panel
		statusBar = new StatusPanel(frame);
		timedBehaviourPanel = new TimedBehaviourPanel(trial, Arrays.asList('q', 'w', 'e', 'r'));
		locationPanel = new LocationPanel(trial);
		instantBehaviourPanel = new InstantBehaviourPanel(trial,
				Arrays.asList('p', 'o', 'i', 'u'), Arrays.asList(';', 'l', 'k', 'j'));
		vlcVideoPanel = new VLCVideoPanel(trial.getDetails());
		mediaControlPanel = new MediaControlPanel();
		infoPanel = new InfoPanel(trial, statusBar);
		detailsPanel = new DetailsPanel(trial);
		
		//Create the file chooser
		fileChooser = new FileChooser(frame);

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
		
		//Add the video panel
		GridBagConstraints videoPanelConstraints = createConstraints(0, 4, 30, 11, GridBagConstraints.BOTH);
		videoPanelConstraints.weightx = 1;
		videoPanelConstraints.weighty = 1;
		videoPanelConstraints.anchor = GridBagConstraints.CENTER;
		mainPanel.add(vlcVideoPanel, videoPanelConstraints);

		//Add the information panel
		GridBagConstraints infoPanelConstraints = createConstraints(0, 0, 30, 2, GridBagConstraints.HORIZONTAL);
		infoPanelConstraints.weightx = 1;
		mainPanel.add(infoPanel, infoPanelConstraints);

		//Add the details panel
		mainPanel.add(detailsPanel, createConstraints(0, 2, 30, 2, GridBagConstraints.HORIZONTAL));

		//Add the media control panel
		mainPanel.add(mediaControlPanel, createConstraints(0, 15, 30, 3, GridBagConstraints.HORIZONTAL));

		//Create and add the mirror label
		JLabel mirrorLabel = new JLabel("Mirror / Divider");
		mirrorLabel.setFont(new Font("Arial", Font.BOLD, 15));
		mirrorLabel.setHorizontalAlignment(JLabel.CENTER);
		mirrorLabel.setBackground(Color.BLACK);
		mirrorLabel.setOpaque(true);
		mirrorLabel.setForeground(Color.WHITE);
		GridBagConstraints mirrorLabelConstraints = createConstraints(8, 18, 16, 1, GridBagConstraints.HORIZONTAL);
		mirrorLabelConstraints.weightx = 1;
		mainPanel.add(mirrorLabel, mirrorLabelConstraints);

		//Add the location panel
		GridBagConstraints locationPanelConstraints = createConstraints(8, 19, 16, 12, GridBagConstraints.BOTH);
		locationPanelConstraints.weightx = 1;
		mainPanel.add(locationPanel, locationPanelConstraints);

		//Add the status bar
		mainPanel.add(statusBar, createConstraints(0, 31, 30, 1, GridBagConstraints.HORIZONTAL));

		//Add the instant behaviour panel
		GridBagConstraints instantBehaviourPanelConstraints = createConstraints(23, 19, 7, 12, GridBagConstraints.VERTICAL);
		instantBehaviourPanelConstraints.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(instantBehaviourPanel, instantBehaviourPanelConstraints);

		//Add the timed behaviour panel
		mainPanel.add(timedBehaviourPanel, createConstraints(0, 19, 7, 12, GridBagConstraints.BOTH)); // 0, 17

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
				JOptionPane.showMessageDialog(frame, "Version: 0.3");
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

		//Add the option to reset
		JMenuItem reset = new JMenuItem("Reset");
		menu.add(reset);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detailsPanel.resetAll();
				trial.reset();
			}
		});

		//Add option to open a video
		JMenuItem chooseVideo = new JMenuItem("Open video");
		menu.add(chooseVideo);
		chooseVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showLoadVideoDialog();
			}
		});
		
		//Add option to save a trial
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog();
			}
		});
		menu.add(save);

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
	 * Chooses a file to be saved.
	 */
	private void showSaveDialog() {
		File file = fileChooser.chooseSpreadsheet();

		if (file != null) {
			save(file);
		} else {
			statusBar.setMessage("Save cancelled.");
		}
	}

	/**
	 * Chooses a video to be loaded.
	 */
	private void showLoadVideoDialog() {	
		File file = fileChooser.chooseVideo();

		if (file != null) {
			vlcVideoPanel.openVideo(file);
		} else {
			statusBar.setMessage("Open video  cancelled.");
		}
	}
	
	/**
	 * Saves the trial to a spreadsheet.
	 * 
	 * @param file - the XLS file to save to
	 */
	private void save(File file) {
		ExcelWriter writer = new ExcelWriter(file);
		try {
			List<ValidationError> validationMessages = TrialValidator.validate(trial);
			if (validationMessages.isEmpty()) {
				//If valid trial data than save
				writer.write(trial);
				statusBar.setMessage("Saved OK");
				SoundMaker.playSave();
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
