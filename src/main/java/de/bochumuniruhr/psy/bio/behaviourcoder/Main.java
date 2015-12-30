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
import de.bochumuniruhr.psy.bio.behaviourcoder.gui.video.VideoListener;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.ExcelWriter;
import de.bochumuniruhr.psy.bio.behaviourcoder.io.FileChooser;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.TrialValidator;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.validation.ValidationError;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main implements VideoListener {

	private LocationPanel locationTimerPanel;
	private TimedBehaviourPanel actionTimerPanel;
	private InstantBehaviourPanel counterPanel;
	private DetailsPanel detailsPanel;
	private StatusPanel statusPanel;
	private FileChooser fileChooser;
	private GlobalKeyPressHandler globalKeyHandler;
	private JLabel mirrorLabel;
	private InfoPanel infoPanel;
	private MediaControlPanel mediaControlPanel;
	private VLCVideoPanel vlcVideoPanel;
	private boolean foundVlc;
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Trial trial;

	private int DEFAULT_TIME_LIMIT = 120;

	private JFrame frame;

	public static void main(String[] args) {
		System.setProperty("jna.library.path", "C:\\Program Files\\VideoLAN\\VLC\\");
		
		//boolean found = new NativeDiscovery(new CustomNativeDiscoveryStrategy()).discover();
		final boolean found = new NativeDiscovery().discover();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main(found);
			}
		});
	}

	public Main(boolean found) {
		this.foundVlc = true;
		KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		globalKeyHandler = new GlobalKeyPressHandler(currentKeyboardFocusManager, this);
		
		trial = new Trial(DEFAULT_TIME_LIMIT, Arrays.asList(new Location("Close"), new Location("Far")),
				Arrays.asList("Trial Type", "Rooster ID", "Session Number", "Trial Number", "Section Number", "Mirror?"),
				Arrays.asList(new Constraint("BM", "NM", "BS", "NS", "WM", "WS"),
						new Constraint("554", "570", "547", "538", "546", "551", "559", "548", "544", "578", "579", "542", "567"),
						new Constraint(), new Constraint(), new Constraint(),
						new Constraint("0", "1")));
		setupBehaviours();
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
	
	private void setupBehaviours(){
		trial.addInstantBehaviour(new InstantBehaviour("Peck", Color.YELLOW, trial));
		trial.addInstantBehaviour(new InstantBehaviour("Hackles", Color.GREEN, trial));
		trial.addInstantBehaviour(new InstantBehaviour("Attack", Color.RED, trial));
		trial.addInstantBehaviour(new InstantBehaviour("Crouch", Color.CYAN, trial));
		
		trial.addTimedBehaviour(new TimedBehaviour("Following", null, trial));
		trial.addTimedBehaviour(new TimedBehaviour("Facing Away", null, trial));
		trial.addTimedBehaviour(new TimedBehaviour("Grooming Mark", null, trial));
		trial.addTimedBehaviour(new TimedBehaviour("Grooming Other", null, trial));
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
		actionTimerPanel = new TimedBehaviourPanel(trial, Arrays.asList('q', 'w', 'e', 'r'));
		locationTimerPanel = new LocationPanel(trial);
		counterPanel = new InstantBehaviourPanel(trial,
				Arrays.asList('p', 'o', 'i', 'u'), Arrays.asList(';', 'l', 'k', 'j'));
		vlcVideoPanel = new VLCVideoPanel(trial);
		mediaControlPanel = new MediaControlPanel(trial);
		infoPanel = new InfoPanel(trial, statusPanel);
		detailsPanel = new DetailsPanel(trial);
		fileChooser = new FileChooser(statusPanel);


		trial.addListener(vlcVideoPanel);
		trial.addListener(mediaControlPanel);
		trial.addListener(infoPanel);
		trial.addListener(locationTimerPanel);
		trial.addListener(statusPanel);

		vlcVideoPanel.addVideoListener(mediaControlPanel);
		vlcVideoPanel.addVideoListener(trial);
		vlcVideoPanel.addVideoListener(this);

		mediaControlPanel.addMediaControlListener(vlcVideoPanel);

		globalKeyHandler.register(counterPanel);
		globalKeyHandler.register(actionTimerPanel);
		
		mirrorLabel = new JLabel("Mirror / Divider");
		mirrorLabel.setFont(new Font("Arial", Font.BOLD, 15));
		mirrorLabel.setHorizontalAlignment(JLabel.CENTER);
		mirrorLabel.setBackground(Color.BLACK);
		mirrorLabel.setOpaque(true);
		mirrorLabel.setForeground(Color.WHITE);
		
		GridBagConstraints videoPanelConstraints = createConstraints(0, 4, 30, 11, GridBagConstraints.BOTH);
		videoPanelConstraints.weightx = 1;
		videoPanelConstraints.weighty = 1;
		videoPanelConstraints.anchor = GridBagConstraints.CENTER;
		mainPanel.add(vlcVideoPanel, videoPanelConstraints);

		GridBagConstraints infoPanelConstraints = createConstraints(0, 0, 30, 2, GridBagConstraints.HORIZONTAL);
		infoPanelConstraints.weightx = 1;
		mainPanel.add(infoPanel, infoPanelConstraints);

		mainPanel.add(detailsPanel, createConstraints(0, 2, 30, 2, GridBagConstraints.HORIZONTAL));

		mainPanel.add(mediaControlPanel, createConstraints(0, 15, 30, 3, GridBagConstraints.HORIZONTAL));

		GridBagConstraints mirrorLabelConstraints = createConstraints(8, 18, 16, 1, GridBagConstraints.HORIZONTAL);
		mirrorLabelConstraints.weightx = 1;
		mainPanel.add(mirrorLabel, mirrorLabelConstraints);

		GridBagConstraints locationPanelConstraints = createConstraints(8, 19, 16, 12, GridBagConstraints.BOTH);
		locationPanelConstraints.weightx = 1;
		mainPanel.add(locationTimerPanel, locationPanelConstraints);

		mainPanel.add(statusPanel, createConstraints(0, 31, 30, 1, GridBagConstraints.HORIZONTAL));

		GridBagConstraints counterPanelConstraints = createConstraints(23, 19, 7, 12, GridBagConstraints.VERTICAL);
		counterPanelConstraints.anchor = GridBagConstraints.LINE_END;
		mainPanel.add(counterPanel, counterPanelConstraints);

		mainPanel.add(actionTimerPanel, createConstraints(0, 19, 7, 12, GridBagConstraints.BOTH)); // 0, 17

		mainPanel.validate();

		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);

	}

	private JMenuBar configureJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu programMenu = configureProgramMenu();
		menuBar.add(programMenu);
		
		JMenu helpMenu = configureHelpMenu();
		menuBar.add(helpMenu);
		
		return menuBar;
	}

	private JMenu configureHelpMenu() {
		JMenu menu = new JMenu("Help");
		
		JMenuItem help = new JMenuItem("Version");
		help.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Version: 0.2");
			}});
		menu.add(help);
		return menu;
	}

	private JMenu configureProgramMenu() {
		JMenu menu = new JMenu("Program");

		JMenuItem reset = new JMenuItem("Reset");
		menu.add(reset);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				counterPanel.resetAll();
				detailsPanel.resetAll();
				trial.reset();
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
		return menu;
	}

	private void showSaveDialog() {
		File file = chooseFile("Save cancelled.");
		if (file != null) {
			save(file);
		}
	}

	private void showLoadVideoDialog() {
		File file = chooseFile("Open video cancelled.");
		if (file != null) {
			vlcVideoPanel.openVideo(file);
		}
	}
	
	private File chooseFile(String cancelMessage){
		int returnVal = fileChooser.showSaveDialog(frame);

		if (returnVal == FileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			statusPanel.setMessage(cancelMessage);
			return null;
		}
	}
	
	private void save(File file) {
		ExcelWriter writer = new ExcelWriter(file);
		try {
			List<ValidationError> validationMessages = TrialValidator.validate(trial);
			
			if (validationMessages.isEmpty()) {
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
	}
	
	private JFrame configureJFrame() {
		JFrame frame = new JFrame("Behaviour Coder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onVideoLoaded(double videoLength) {}

	@Override
	public void onVideoPositionChange(long videoPosition) {}

	@Override
	public void onVideoStart() {}

	@Override
	public void onVideoStop() {}

	@Override
	public void onVideoPercentThroughChange(int videoTime) {}
	
	/*
	 * James's Abstraction Functions
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
