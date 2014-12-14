package de.bochumuniruhr.psy.bio.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	private TimerPanel timerPanel;
	private CounterPanel counterPanel;
	private DetailsPanel detailsPanel;
	private StatusPanel statusPanel;
	private FileChooser fileChooser;
	private GlobalKeyPressHandler globalKeyHandler;

	private JFrame frame;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				Main main = new Main();
			} } );
	}

	public Main() {
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
	}

	private void setupUIComponents() {
		setLookAndFeel();

		frame = configureJFrame();

		JMenuBar menuBar = configureJMenuBar();
		frame.setJMenuBar(menuBar);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		statusPanel = new StatusPanel(frame);
		timerPanel = new TimerPanel(statusPanel);
		counterPanel = new CounterPanel(statusPanel);
		globalKeyHandler.register(counterPanel);
		detailsPanel = new DetailsPanel(statusPanel);
		fileChooser = new FileChooser(statusPanel);

		mainPanel.add(timerPanel, BorderLayout.CENTER);
		mainPanel.add(counterPanel, BorderLayout.LINE_END);
		mainPanel.add(detailsPanel, BorderLayout.PAGE_START);
		mainPanel.add(statusPanel, BorderLayout.SOUTH);

		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	private JMenuBar configureJMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Program");

		JMenuItem reset = new JMenuItem("Reset");
		menu.add(reset);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerPanel.resetAll();
				counterPanel.resetAll();
				detailsPanel.resetAll();
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

	private void save(File file) {
		ExcelWriter writer = new ExcelWriter(file);
		try {
			Trial trial = new Trial();
			List<ValidationError> validationMessages = detailsPanel.validateTrialData();
			validationMessages.addAll(timerPanel.validateTrialData());
			if (validationMessages.isEmpty()) {
				detailsPanel.populateTrial(trial);
				timerPanel.populateTrial(trial);
				counterPanel.populateTrial(trial);
				writer.write(trial);
				statusPanel.setMessage("Saved OK");
				SoundMaker.playSave();
			} else {
				statusPanel.showErrors(validationMessages);
				SoundMaker.playValidationError();
			}
		} catch (FileNotFoundException ex) {
			JOptionPane
					.showMessageDialog(frame,
							"Cannot save spreadsheet. Is it opened in another program?");
		}
	}

	private JFrame configureJFrame() {
		JFrame frame = new JFrame("Behaviour Coder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int height = 300;
		int width = 800;
		frame.setSize(width, height);

		BufferedImage image = null;
		try {
			image = ImageIO
					.read(ClassLoader.getSystemResource("FrameIcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.setIconImage(image);
		return frame;
	}

	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
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

}
