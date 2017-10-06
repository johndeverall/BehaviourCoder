package de.bochumuniruhr.psy.bio.behaviourcoder.gui.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.bochumuniruhr.psy.bio.behaviourcoder.Main;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.InstantBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Location;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TimedBehaviour;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.Trial;
import de.bochumuniruhr.psy.bio.behaviourcoder.model.TrialDetails.Constraint;

@SuppressWarnings("serial")
public class TrialCreationFrame extends JFrame {

	private static final String EXPERIMENT_TRIAL_DETAILS = "Experiment Trial Details: Subject, Identifying Information, Family Group, Context, etc.";
	private static final String COUNT_A_BEHAVIOUR = "Count a behaviour";
	private static final String LOCATIONS = "Locations";
	private static final String TIME_A_BEHAVIOUR = "Time a behaviour";
	private final JPanel panel;
	private Map<String, JComboBox<String>> boxes;
	private Map<String, JButton[]> components;
	private Map<String, BehaviourDetails> timed;
	private Map<String, BehaviourDetails> instant;
	private Map<String, Detail> details;
	

	/**
	 * The default time limit for a trial.
	 */
	private int DEFAULT_TIME_LIMIT = 120;
	
	public TrialCreationFrame(final JFrame parent, final Main main) {
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		boxes = new HashMap<>();
		components = new HashMap<>();
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 1.0;
		cons.weighty = 1.0;
		panel = new JPanel();
		getContentPane().add(panel, cons);
		cons.weightx = 0.0;
		cons.weighty = 0.0;
		panel.setLayout(new GridBagLayout());
		
		timed = new HashMap<String, BehaviourDetails>();
		instant = new HashMap<String, BehaviourDetails>();
		details = new HashMap<String, Detail>();
		final TrialCreationFrame that = this;
		
		createConfigGroup(LOCATIONS, 0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(
						that,
	                    "Location Name",
	                    "Create Location",
	                    JOptionPane.PLAIN_MESSAGE);
				if (s != null && !s.equals("")){
					JComboBox<String> box = boxes.get(LOCATIONS);
					box.addItem(s);
					box.setSelectedIndex(box.getItemCount()-1);
					
					if (box.getItemCount() == 1){
						for (JButton button : components.get(LOCATIONS)){
							button.setEnabled(true);
						}
					}
				}
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(
						that,
						"Location Name",
		                "Edit Location",
		                JOptionPane.PLAIN_MESSAGE,
		                null, null, boxes.get(LOCATIONS).getSelectedItem());
				if (s != null && !s.equals("")){
					JComboBox<String> box = boxes.get(LOCATIONS);
					int i = box.getSelectedIndex();
					box.removeItemAt(i);
					box.insertItemAt(s, i);
					box.setSelectedIndex(i);
				}
			}
		}, cons);
		
		createConfigGroup(TIME_A_BEHAVIOUR, 2, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				new BehaviourConfigFrame(that, false);
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				BehaviourDetails details = timed.get(boxes.get(TIME_A_BEHAVIOUR).getSelectedItem());
				new BehaviourConfigFrame(that, false, details.name, details.associateLocations,
						details.color, details.keys);
			}
		}, cons);
		
		createConfigGroup(COUNT_A_BEHAVIOUR, 4, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				new BehaviourConfigFrame(that, true);
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				BehaviourDetails details = instant.get(boxes.get(COUNT_A_BEHAVIOUR).getSelectedItem());
				new BehaviourConfigFrame(that, true, details.name, details.associateLocations,
						details.color, details.keys);
			}
		}, cons);
		
		createConfigGroup(EXPERIMENT_TRIAL_DETAILS, 6, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				new DetailsConfigFrame(that);
			}
		}, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				that.setAlwaysOnTop(false);
				that.setEnabled(false);
				Detail det = details.get(boxes.get(EXPERIMENT_TRIAL_DETAILS).getSelectedItem());
				new DetailsConfigFrame(that, det.name, det.cons);
			}
		}, cons);
		
		setConstraints(cons, 2, 8, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				that.dispose();
				parent.setAlwaysOnTop(true);
				parent.setAlwaysOnTop(false);
				parent.setEnabled(true);
				main.onTrialCreationCancel();
			}
		});
		panel.add(cancel, cons);
		
		setConstraints(cons, 3, 8, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton add = new JButton("Create");
		add.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Location> locs = new ArrayList<Location>();
				JComboBox<String> locations = boxes.get(LOCATIONS);
				JComboBox<String> ins = boxes.get(COUNT_A_BEHAVIOUR);
				JComboBox<String> tim = boxes.get(TIME_A_BEHAVIOUR);
				for (int i = 0; i < locations.getItemCount(); ++i){
					locs.add(new Location(locations.getItemAt(i)));
				}
				
				List<String> dets = new ArrayList<String>();
				List<Constraint> cons = new ArrayList<Constraint>();
				for (Detail d : details.values()){
					dets.add(d.name);
					cons.add(new Constraint(d.cons));
				}
				
				if (locs.size() == 0){
					locations.setBackground(Color.PINK);
					return;
				} else if (dets.size() == 0) {
					boxes.get(EXPERIMENT_TRIAL_DETAILS).setBackground(Color.PINK);
					return;
				} else if (ins.getItemCount() == 0){
					ins.setBackground(Color.PINK);
					return;
				} else if (tim.getItemCount() == 0){
					tim.setBackground(Color.PINK);
					return;
				}
				
				Trial trial = new Trial(DEFAULT_TIME_LIMIT, locs, dets, cons);
				
				List<Character> insIncKeys = new ArrayList<Character>();
				List<Character> insDecKeys = new ArrayList<Character>();
				List<Character> timKeys = new ArrayList<Character>();
				for (int i = 0; i < ins.getItemCount(); ++i){
					BehaviourDetails bd = instant.get(ins.getItemAt(i));
					trial.addInstantBehaviour(new InstantBehaviour(bd.name, bd.color, trial, bd.associateLocations));
					insIncKeys.add(bd.keys[0]);
					insDecKeys.add(bd.keys[1]);
				}
				
				for (int i = 0; i < tim.getItemCount(); ++i){
					BehaviourDetails bd = timed.get(tim.getItemAt(i));
					trial.addTimedBehaviour(new TimedBehaviour(bd.name, bd.color, trial, bd.associateLocations));
					timKeys.add(bd.keys[0]);
				}

				that.dispose();
				parent.setAlwaysOnTop(true);
				parent.setAlwaysOnTop(false);
				parent.setEnabled(true);
				main.onTrialCreate(trial, insIncKeys, insDecKeys, timKeys);
			}
		});
		panel.add(add, cons);
		
		pack();
		
		setVisible(true);
		
		setLocation(parent.getX() + parent.getWidth()/2 - getWidth(),
				parent.getY() + parent.getHeight()/2 - getHeight());
		setAlwaysOnTop(true);
		parent.setEnabled(false);
		setSize(new Dimension(getSize().width + 100, getSize().height));
	}
	
	private void createConfigGroup(final String title, int row,ActionListener create,
			ActionListener edit, GridBagConstraints cons){
		setConstraints(cons, 0, row, 4, 1, GridBagConstraints.NONE);
		panel.add(new JLabel(title), cons);
		
		final JButton[] modifyButtons = new JButton[2];
		
		setConstraints(cons, 0, row + 1, 1, 1, GridBagConstraints.NONE);
		JButton createButton = new JButton("New");
		createButton.addActionListener(create);
		panel.add(createButton, cons);
		
		setConstraints(cons, 1, row + 1, 1, 1, GridBagConstraints.HORIZONTAL);
		cons.weightx = 1.0;
		JComboBox<String> box = new JComboBox<String>();
		boxes.put(title, box);
		panel.add(box, cons);
		
		setConstraints(cons, 2, row + 1, 1, 1, GridBagConstraints.NONE);
		cons.weightx = 0.0;
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(edit);
		editButton.setEnabled(false);
		panel.add(editButton, cons);
		modifyButtons[0] = editButton;
		
		setConstraints(cons, 3, row + 1, 1, 1, GridBagConstraints.NONE);
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> box = boxes.get(title);
				String s = (String) box.getSelectedItem();

				if (s != null && !s.equals("")){
					box.removeItem(s);
				}
				if (box.getItemCount() == 0){
					for (JButton button : modifyButtons){
						button.setEnabled(false);
					}
				}
			}
		});
		removeButton.setEnabled(false);
		panel.add(removeButton, cons);
		modifyButtons[1] = removeButton;
		
		components.put(title, modifyButtons);
	}
	
	void onCancel(){
		setAlwaysOnTop(true);
		setEnabled(true);
	}
	
	void onAccept(boolean isInstant, String name, boolean associate, Color color, char[] keys){
		setAlwaysOnTop(true);
		setEnabled(true);
		JComboBox<String> box;
		if (isInstant){
			box = boxes.get(COUNT_A_BEHAVIOUR);
			if (!instant.containsKey(name)){
				box.addItem(name);
				box.setSelectedItem(name);
			}
			instant.put(name, new BehaviourDetails(name, associate, color, keys));
			
			if (box.getItemCount() == 1){
				for (JButton button : components.get(COUNT_A_BEHAVIOUR)){
					button.setEnabled(true);
				}
			}
		} else {
			box = boxes.get(TIME_A_BEHAVIOUR);
			if (!timed.containsKey(name)){
				box.addItem(name);
				box.setSelectedItem(name);
			}
			timed.put(name, new BehaviourDetails(name, associate, color, keys));
			
			if (box.getItemCount() == 1){
				for (JButton button : components.get(TIME_A_BEHAVIOUR)){
					button.setEnabled(true);
				}
			}
		}
	}

	public void onDetailsAccept(String name, String[] list) {
		setAlwaysOnTop(true);
		setEnabled(true);
		JComboBox<String> box;
		box = boxes.get(EXPERIMENT_TRIAL_DETAILS);
		if (!details.containsKey(name)){
			box.addItem(name);
			box.setSelectedItem(name);
		}
		details.put(name, new Detail(name, list));
		
		if (box.getItemCount() == 1){
			for (JButton button : components.get(EXPERIMENT_TRIAL_DETAILS)){
				button.setEnabled(true);
			}
		}
	}
	
	private void setConstraints(GridBagConstraints cons, int x, int y, int width, int height, int fill){
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = width;
		cons.gridheight = height;
		cons.fill = fill;
	}
	
	private class BehaviourDetails {
		public final String name;
		public final boolean associateLocations;
		public final Color color;
		public final char[] keys;
		
		private BehaviourDetails(String name, boolean associateLocations, Color color, char[] keys){
			this.name = name;
			this.associateLocations = associateLocations;
			this.color = color;
			this.keys = keys;
		}
	}
	
	private class Detail {
		public final String name;
		public final String[] cons;
		
		private Detail(String name, String[] cons){
			this.cons = cons;
			this.name = name;
		}
	}
}
