package de.bochumuniruhr.psy.bio.behaviourcoder.gui.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;

@SuppressWarnings("serial")
public class DetailsConfigFrame extends JFrame {
	
	public DetailsConfigFrame(TrialCreationFrame parent){
		this(parent, null, null);
	}
	
	public DetailsConfigFrame(final TrialCreationFrame parent, String na, String[] whitelist){
		final DetailsConfigFrame that = this;
		setTitle((na == null) ? "Create detail" : "Edit detail");
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 1.0;
		cons.weighty = 1.0;
		JPanel panel = new JPanel();
		getContentPane().add(panel, cons);
		cons.weightx = 0.0;
		cons.weighty = 0.0;
		panel.setLayout(new GridBagLayout());
		
		setConstraints(cons, 0, 0, 2, 1, GridBagConstraints.HORIZONTAL);
		panel.add(new JLabel("Name"), cons);
		setConstraints(cons, 2, 0, 2, 1, GridBagConstraints.HORIZONTAL);
		final JTextField name = new JTextField();
		if (na != null){
			name.setText(na);
		}
		panel.add(name, cons);
		
		
		setConstraints(cons, 0, 1, 4, 1, GridBagConstraints.NONE);
		panel.add(new JLabel("Whitelist"), cons);
		
		final JButton[] modifyButtons = new JButton[2];
		
		final JComboBox<String> box = new JComboBox<String>();
		
		setConstraints(cons, 0, 2, 1, 1, GridBagConstraints.NONE);
		JButton createButton = new JButton("New");
		createButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) JOptionPane.showInputDialog(
						that,
	                    "Valid item",
	                    "Create valid item",
	                    JOptionPane.PLAIN_MESSAGE);
				if (s != null && !s.equals("")){
					box.addItem(s);
					box.setSelectedIndex(box.getItemCount()-1);
					
					if (box.getItemCount() == 1){
						for (JButton button : modifyButtons){
							button.setEnabled(true);
						}
					}
				}
			}
		});
		panel.add(createButton, cons);
		
		setConstraints(cons, 1, 2, 1, 1, GridBagConstraints.HORIZONTAL);
		cons.weightx = 1.0;
		panel.add(box, cons);
		if (whitelist != null && whitelist.length > 0){
			for (String w : whitelist){
				box.addItem(w);
			}
			box.setSelectedIndex(box.getItemCount()-1);
		}
		setConstraints(cons, 2, 2, 1, 1, GridBagConstraints.NONE);
		cons.weightx = 0.0;
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) JOptionPane.showInputDialog(
						that,
						"Location Name",
		                "Edit Location",
		                JOptionPane.PLAIN_MESSAGE,
		                null, null, box.getSelectedItem());
				if (s != null && !s.equals("")){
					int i = box.getSelectedIndex();
					box.removeItemAt(i);
					box.insertItemAt(s, i);
					box.setSelectedIndex(i);
				}
			}
		});
		if (whitelist == null || whitelist.length == 0){
			editButton.setEnabled(false);
		}
		panel.add(editButton, cons);
		modifyButtons[0] = editButton;
		
		setConstraints(cons, 3, 2, 1, 1, GridBagConstraints.NONE);
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
		if (whitelist == null || whitelist.length == 0){
			removeButton.setEnabled(false);
		}
		panel.add(removeButton, cons);
		modifyButtons[1] = removeButton;
		
		setConstraints(cons, 2, 3, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.onCancel();
				that.dispose();
			}
		});
		panel.add(cancel, cons);
		
		setConstraints(cons, 3, 3, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton add = new JButton((na == null) ? "Add" : "Edit");
		add.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (name.getText() == null && "".equals(name.getText())){
					name.setBackground(Color.PINK);
					return;
				}
				String n = name.getText();
				
				String[] list = new String[box.getItemCount()];;
				
				for (int i = 0; i < box.getItemCount(); ++i){
					list[i] = box.getItemAt(i);
				}
				parent.onDetailsAccept(n, list);
				that.dispose();
			}
		});
		panel.add(add, cons);
		
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.onCancel();
			}
		});
		setVisible(true);
		setAlwaysOnTop(true);

		setSize(new Dimension(getSize().width + 100, getSize().height));
		setLocation(parent.getX() + parent.getWidth()/2 - getWidth(),
				parent.getY() + parent.getHeight()/2 - getHeight());
	}
	
	
	private void setConstraints(GridBagConstraints cons, int x, int y, int width, int height, int fill){
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = width;
		cons.gridheight = height;
		cons.fill = fill;
	}
}
