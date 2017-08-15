package demoPack; 

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import demoPack.TempAdjust;

public class Plate extends TempAdjust implements ActionListener, ItemListener {
	// Private variables of the GUI components
	JTextField tField, bField, lField, rField, dimRowField, dimColField, countField, countFieldCent, tempField;
	JButton runButton;
	int dRow, dCol, count;
	String defaultTemp = "0";
	boolean center = false;
	BigDecimal[][] finalPlate;
	JComboBox cb;
	JPanel cards; // a panel that uses CardLayout
	final static String EDGEPANEL = "Card with Edge Heat";
	final static String CENTPANEL = "Card with Center Heat";

	public void addComponentToPane(Container pane) {
		// Put the JComboBox in a JPanel to get a nicer look.
		JPanel comboBoxPane = new JPanel(); // use FlowLayout
		String comboBoxItems[] = { EDGEPANEL, CENTPANEL };
		cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener(this);
		comboBoxPane.add(cb);

		// Create the "cards".
		// JPanel for the text fields
		JPanel sidesPanel = new JPanel(new GridLayout(8, 2, 10, 2));
		sidesPanel.setBorder(BorderFactory.createTitledBorder("Input Plate Information: "));

		// Top temp (Row 1)
		sidesPanel.add(new JLabel("  Top Temperature: "));
		tField = new JTextField(10);
		sidesPanel.add(tField);

		// Bot temp (Row 2)
		sidesPanel.add(new JLabel("  Bottom Temperature: "));
		bField = new JTextField(10);
		sidesPanel.add(bField);

		// Left temp (Row 3)
		sidesPanel.add(new JLabel("  Left Temperature: "));
		lField = new JTextField(10);
		sidesPanel.add(lField);

		// Right temp (Row 4)
		sidesPanel.add(new JLabel("  Right Temperature: "));
		rField = new JTextField(10);
		sidesPanel.add(rField);

		// Rows (Row 5)
		sidesPanel.add(new JLabel("  Rows: "));
		dimRowField = new JTextField(10);
		sidesPanel.add(dimRowField);

		// Columns (Row 6)
		sidesPanel.add(new JLabel("  Columns: "));
		dimColField = new JTextField(10);
		sidesPanel.add(dimColField);

		// Number of runs to do (Row 7)
		// if checkBox is enabled, will use number of runs given, else will run
		// until temp stabilizes
		countField = new JTextField(10);
		JCheckBox countCheckBox = new JCheckBox("Use set number of runs", true);
		countCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					countField.setEnabled(true);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					countField.setEnabled(false);
					countField.setText("");
				}

				validate();
				repaint();
			}
		});

		sidesPanel.add(countCheckBox);
		sidesPanel.add(countField);

		// button to run simulation with given data
		runButton = new JButton("Run");
		sidesPanel.add(runButton);
		// if the actionListener is activated, opens a new JFrame with the
		// resulting plate with heat distribution
		runButton.addActionListener(this);

		JPanel midPanel = new JPanel(new GridLayout(5, 2, 10, 2));
		sidesPanel.setBorder(BorderFactory.createTitledBorder("Input Plate Information: "));

		// Top temp (Row 1)
		midPanel.add(new JLabel("Temperature: "));
		tempField = new JTextField(10);
		midPanel.add(tempField);

		// Rows (Row 2)
		midPanel.add(new JLabel("  Rows: "));
		dimRowField = new JTextField(10);
		midPanel.add(dimRowField);

		// Columns (Row 3)
		midPanel.add(new JLabel("  Columns: "));
		dimColField = new JTextField(10);
		midPanel.add(dimColField);

		// Number of runs to do (Row 4)
		// if checkBox is enabled, will use number of runs given, else will run
		// until temp stabilizes
		countFieldCent = new JTextField(10);
		JCheckBox checkBox = new JCheckBox("Use set number of runs", true);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					countFieldCent.setEnabled(true);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					countFieldCent.setEnabled(false);
					countFieldCent.setText("");
				}

				validate();
				repaint();
			}
		});

		midPanel.add(checkBox);
		midPanel.add(countFieldCent);

		// button to run simulation with given data
		runButton = new JButton("Run");
		midPanel.add(runButton);
		// if the actionListener is activated, opens a new JFrame with the
		// resulting plate with heat distribution
		runButton.addActionListener(this);

		// Create the panel that contains the "cards".
		cards = new JPanel(new CardLayout());
		cards.add(sidesPanel, EDGEPANEL);

		cards.add(midPanel, CENTPANEL);

		pane.add(comboBoxPane, BorderLayout.PAGE_START);
		pane.add(cards, BorderLayout.CENTER);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("CardLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		Plate demo = new Plate();
		demo.addComponentToPane(frame.getContentPane());
		// Display the window.
		frame.setTitle("Heat Distribution Simultation");
		frame.setSize(450, 350);
		frame.pack();
		frame.setVisible(true);
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Run GUI codes in Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI(); // Let the constructor do the job
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String sTop = "", sBot = "", sLeft = "", sRight = "", sTemp = "";
		BigDecimal top, bot = null, left = null, right = null, temp = null;
		int red;
		JLabel pointData;

		// adds default case in case the blanks are left empty
		if (!center) {
			if (!tField.getText().equals("")) {
				sTop = tField.getText();
			} else
				sTop = defaultTemp;

				if (!bField.getText().equals("")) {

					sBot = bField.getText();
				} else
					sBot = defaultTemp;
				
				if (!lField.getText().equals("")) {
					sLeft = lField.getText();
				} else
					sLeft = defaultTemp;

				if (!rField.getText().equals("")) {
					sRight = rField.getText();
				} else
					sRight = defaultTemp;
				if (!countField.getText().equals("")) {
					try {
						count = Integer.parseInt(countField.getText());
					} catch (Exception ex) {
						count = 0;
					}
				} else
					count = 0;
		}

		else {
			if (!tempField.getText().equals("")) {
				sTemp = tempField.getText();
			} else
				sTemp = defaultTemp;
			
			if (!countFieldCent.getText().equals("")) {
				try {
					count = Integer.parseInt(countFieldCent.getText());
				} catch (Exception ex) {
					count = 0;
				}
			} else
				count = 0;
		}

		if (!dimRowField.getText().equals(""))

		{
			try {
				dRow = Integer.parseInt(dimRowField.getText());
			} catch (Exception ex) {
				dRow = 3;
			}
		} else
			dRow = 3;

		if (!dimColField.getText().equals("")) {
			try {
				dCol = Integer.parseInt(dimColField.getText());
			} catch (Exception ex) {
				dCol = 3;
			}
		} else
			dCol = 3;

		
		if (!center) {
			// verifies that all entered values are valid options, then
			// assigns value to the exterior temp variables
			try {
				top = new BigDecimal(sTop);
			} catch (Exception ex) {
				sTop = defaultTemp;
				top = new BigDecimal(sTop);
			}
			try {
				bot = new BigDecimal(sBot);
			} catch (Exception ex) {
				sBot = defaultTemp;
				bot = new BigDecimal(sBot);
			}

			try {
				left = new BigDecimal(sLeft);
			} catch (Exception ex) {
				sLeft = defaultTemp;
				left = new BigDecimal(sLeft);
			}
			try {
				right = new BigDecimal(sRight);
			} catch (Exception ex) {
				sLeft = defaultTemp;
				right = new BigDecimal(sRight);
			}

			// sets the precision to 2 decimal places and rounding to
			// normal rounding
			top = top.setScale(2, RoundingMode.HALF_UP);
			bot = bot.setScale(2, RoundingMode.HALF_UP);
			left = left.setScale(2, RoundingMode.HALF_UP);
			right = right.setScale(2, RoundingMode.HALF_UP);

			// creates the plates
			BigDecimal[][] oldPlateSides = new BigDecimal[dRow + 2][dCol + 2];
			BigDecimal[][] newPlateSides = new BigDecimal[dRow + 2][dCol + 2];
			// sets edge temperatures for plates
			oldPlateSides = initializeSides(oldPlateSides, top, bot, left, right);
			finalPlate = new BigDecimal[dRow + 2][dCol + 2];
			finalPlate = initializeSides(newPlateSides, top, bot, left, right);
			finalPlate = iterator(oldPlateSides, newPlateSides, count, center);
		}
		// makes a final plate for display purposes

		else {
			try {
				temp = new BigDecimal(sTemp);
			} catch (Exception ex) {
				sTemp = defaultTemp;
				temp = new BigDecimal(sTemp);
			}
			// creates the plates
			BigDecimal[][] oldPlateCenter = new BigDecimal[dRow][dCol];
			BigDecimal[][] newPlateCenter = new BigDecimal[dRow][dCol];
			// sets edge temperatures for plates
			oldPlateCenter = initializeCent(oldPlateCenter, temp);
			finalPlate = new BigDecimal[dRow][dCol];
			finalPlate = initializeCent(newPlateCenter, temp);
			finalPlate = iterator(oldPlateCenter, newPlateCenter, count, center);
		}

		// final display frame
		JFrame frame = new JFrame("Heat Distribution");

		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(dRow, dCol));
		if(center){
		for (int i = 0; i < dRow; i++) {
			for (int j = 0; j < dCol; j++) {
				pointData = new JLabel(finalPlate[i][j] + "\t");
				// sets the color gradient of the text based on the temperature
				red = (int) (finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() * 2.55);
				// ensures the red value is within acceptable range
				if (red > 255) {
					red = 255;
				}
				if (red < 0) {
					red = 0;
				}

				pointData.setForeground(new Color(red, 0, 0));
				grid.add(pointData);
			}
		}
		}
			else{
				for (int i = 1; i <= dRow; i++) {
					for (int j = 1; j <= dCol; j++) {
						pointData = new JLabel(finalPlate[i][j] + "\t");
						// sets the color gradient of the text based on the temperature
						red = (int) (finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() * 2.55);
						// ensures the red value is within acceptable range
						if (red > 255) {
							red = 255;
						}
						if (red < 0) {
							red = 0;
						}

						pointData.setForeground(new Color(red, 0, 0));
						grid.add(pointData);
					}
				
			

			
			}
			}
			frame.setContentPane(grid);
			frame.getContentPane().setBackground(Color.lightGray);
			frame.pack();
			frame.setVisible(true);

	
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, (String) evt.getItem());
		if (cb.getSelectedIndex() == 0) {
			center = false;
		} else
			center = true;
	}

}
