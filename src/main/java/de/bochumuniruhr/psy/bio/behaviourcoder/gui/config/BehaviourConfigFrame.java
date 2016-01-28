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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class BehaviourConfigFrame extends JFrame {
	
	public BehaviourConfigFrame(TrialCreationFrame parent, boolean isInstant){
		this(parent, isInstant, null, false, null, null);
	}
	
	public BehaviourConfigFrame(final TrialCreationFrame parent, final boolean isInstant,
			String na, boolean al, Color col, char[] ks){
		final BehaviourConfigFrame that = this;
		setTitle((na == null) ? "Create Behaviour" : "Edit Behaviour");
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
		
		setConstraints(cons, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL);
		panel.add(new JLabel("Name"), cons);
		setConstraints(cons, 1, 0, 2, 1, GridBagConstraints.HORIZONTAL);
		final JTextField name = new JTextField();
		if (na != null){
			name.setText(na);
		}
		panel.add(name, cons);
		
		setConstraints(cons, 0, 1, 2, 1, GridBagConstraints.HORIZONTAL);
		panel.add(new JLabel("Associate with locations?"), cons);
		setConstraints(cons, 2, 1, 1, 1, GridBagConstraints.HORIZONTAL);
		final JCheckBox associateLocations = new JCheckBox();
		associateLocations.setSelected(al);
		panel.add(associateLocations, cons);
		
		final JColorChooser color = new JColorChooser();
		setConstraints(cons, 0, 2, 2, 1, GridBagConstraints.HORIZONTAL);
		panel.add(new JLabel("Colour"), cons);
		setConstraints(cons, 2, 2, 1, 1, GridBagConstraints.HORIZONTAL);
		final JCheckBox useColor = new JCheckBox();
		if (col != null){
			useColor.setSelected(true);
			color.setColor(col);
		}
		useColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED){
					color.setEnabled(false);
				} else {
					color.setEnabled(true);
				}
			}
		});
		panel.add(useColor, cons);
		setConstraints(cons, 0, 3, 3, 1, GridBagConstraints.HORIZONTAL);
		color.setEnabled(false);
		for(AbstractColorChooserPanel p: color.getChooserPanels()){
            if (!"Swatches".equals(p.getDisplayName())) {
            	color.removeChooserPanel(p);
            }
		}
		panel.add(color, cons);

		setConstraints(cons, 0, 4, 1, 1, GridBagConstraints.HORIZONTAL);
		panel.add(new JLabel("Increment Key"), cons);
		final JTextField inc = new JTextField();
		inc.setDocument(new LimitDocument());
		inc.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				inc.setText(inc.getText().toLowerCase());
			}
		});
		if (ks != null){
			inc.setText("" + ks[0]);
		}
		setConstraints(cons, 2, 4, 1, 1, GridBagConstraints.HORIZONTAL);
		panel.add(inc, cons);
		
		final JTextField dec = new JTextField();
		if (isInstant){
			setConstraints(cons, 0, 5, 1, 1, GridBagConstraints.HORIZONTAL);
			panel.add(new JLabel("Decrement Key"), cons);
			dec.setDocument(new LimitDocument());
			dec.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					dec.setText(dec.getText().toLowerCase());
				}
			});
			if (ks != null){
				dec.setText("" + ks[0]);
			}
			setConstraints(cons, 2, 5, 1, 1, GridBagConstraints.HORIZONTAL);
			panel.add(dec, cons);
		}
		setConstraints(cons, 1, 6, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.onCancel();
				that.dispose();
			}
		});
		panel.add(cancel, cons);
		
		setConstraints(cons, 2, 6, 1, 1, GridBagConstraints.HORIZONTAL);
		JButton add = new JButton((na == null) ? "Add" : "Edit");
		add.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (name.getText() == null && "".equals(name.getText())){
					name.setBackground(Color.PINK);
					return;
				}
				String n = name.getText();
				
				boolean associate = associateLocations.isSelected();
				
				Color c = null;
				if (useColor.isSelected()){
					if (color.getColor() == null){
						return;
					}
					c = color.getColor();
				}
				
				if (inc.getText() == null && "".equals(inc.getText())){
					inc.setBackground(Color.PINK);
					return;
				}
				if (isInstant){
					if (dec.getText() == null && "".equals(dec.getText())){
						dec.setBackground(Color.PINK);
						return;
					}
					parent.onAccept(isInstant, n, associate, c,
							new char[]{inc.getText().charAt(0), dec.getText().charAt(0)});
				} else {
					parent.onAccept(isInstant, n, associate, c,
							new char[]{inc.getText().charAt(0)});
				}
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
	
	private class LimitDocument extends PlainDocument {

        @Override
        public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= 1) {
                super.insertString(offset, str, attr);
            }
        }       

    }
}
