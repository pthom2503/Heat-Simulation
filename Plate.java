package demoPack;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import demoPack.TempAdjust;

public class Plate extends TempAdjust implements ActionListener, ItemListener {
	// variables of the GUI components
	JTextField tField, bField, lField, rField, dimRowField, dimRowPoint, dimColField, dimColPoint, countField,
			countFieldPoint, tempField, xFieldPoint, yFieldPoint, difFieldLR, difFieldTB;
	JButton runButton;
	int dRow, dCol, count, x, y, dRowPoint, dColPoint, difLR, difTB;
	String defaultTemp = "0";
	boolean point = false;
	BigDecimal[][] finalPlate;
	JCheckBox countCheckBox = new JCheckBox("Use set number of runs", false);
	JCheckBox checkBox = new JCheckBox("Use set number of runs", false);
	JCheckBox xyCheckBox = new JCheckBox("Non-center point heat source", false);
	JCheckBox difCheckBox = new JCheckBox("Diffuse heat source", false);
	JComboBox cb;
	JPanel cards; // a panel that uses CardLayout
	final static String EDGEPANEL = "Card with Edge Heat";
	final static String POINTPANEL = "Card with Point Heat";
	JLabel center = new JLabel(" ");
	JLabel xLabel = new JLabel(" ");
	JLabel yLabel = new JLabel(" ");
	JLabel diffuse = new JLabel(" ");
	JLabel difLabelLR = new JLabel(" ");
	JLabel difLabelTB = new JLabel(" ");

	public void addComponentToPane(Container pane) {
		// Adds the JComboBox to choose which version of the interface to
		// display
		JPanel comboBoxPane = new JPanel(); // use FlowLayout
		String comboBoxItems[] = { EDGEPANEL, POINTPANEL };
		cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addItemListener(this);
		comboBoxPane.add(cb);

		// JPanel for edge heat: Includes input locations for each side's temp,
		// dimensions, and desired number of runs
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
		countField.setEnabled(false);
		countCheckBox.addItemListener(this);

		sidesPanel.add(countCheckBox);
		sidesPanel.add(countField);

		// button to run simulation with given data (Row 8)
		runButton = new JButton("Run");
		sidesPanel.add(runButton);
		// if the actionListener is activated, opens a new JFrame with the
		// resulting plate with heat distribution
		runButton.addActionListener(this);
		// JPanel for point heat: Includes input locations for temp, dimensions,
		// desired number of runs, and location of heat
		JPanel pointPanel = new JPanel(new GridLayout(11, 2, 10, 2));
		sidesPanel.setBorder(BorderFactory.createTitledBorder("Input Plate Information: "));

		// Top temp (Row 1)
		pointPanel.add(new JLabel("Temperature: "));
		tempField = new JTextField(10);
		pointPanel.add(tempField);

		// Rows (Row 2)
		pointPanel.add(new JLabel("  Rows: "));
		dimRowPoint = new JTextField(10);
		pointPanel.add(dimRowPoint);

		// Columns (Row 3)
		pointPanel.add(new JLabel("  Columns: "));
		dimColPoint = new JTextField(10);
		pointPanel.add(dimColPoint);

		// Number of runs to do (Row 4)
		// if checkBox is enabled, will use number of runs given, else will run
		// until temp stabilizes
		countFieldPoint = new JTextField(10);
		countFieldPoint.setEnabled(false);
		// adds enable/disable of corresponding text boxes
		checkBox.addItemListener(this);
		xyCheckBox.addItemListener(this);
		pointPanel.add(checkBox);
		pointPanel.add(countFieldPoint);
		// Center/noncenter heat (Row 5)
		pointPanel.add(xyCheckBox);
		pointPanel.add(center);

		// X location of point (Row 6)
		// X coordinate from top left corner if the user did not select for
		// center heating
		pointPanel.add(xLabel);
		xFieldPoint = new JTextField(10);
		xFieldPoint.setEnabled(false);
		pointPanel.add(xFieldPoint);

		// Y location of point (Row 7)
		// Y coordinate from top left corner if the user did not select for
		// center heating
		pointPanel.add(yLabel);
		yFieldPoint = new JTextField(10);
		yFieldPoint.setEnabled(false);
		pointPanel.add(yFieldPoint);

		// Diffuse heat source (Row 8)
		difCheckBox.addItemListener(this);
		pointPanel.add(difCheckBox);
		pointPanel.add(diffuse);

		// length of top side of heat source (Row 9)
		pointPanel.add(difLabelLR);
		difFieldLR = new JTextField(10);
		difFieldLR.setEnabled(false);
		pointPanel.add(difFieldLR);

		// length of left side of heat source (Row 10)
		pointPanel.add(difLabelTB);
		difFieldTB = new JTextField(10);
		difFieldTB.setEnabled(false);
		pointPanel.add(difFieldTB);

		// Button to run simulation with given data (Row 11)
		runButton = new JButton("Run");
		pointPanel.add(runButton);
		// if the actionListener is activated, opens a new JFrame with the
		// resulting plate with heat distribution
		runButton.addActionListener(this);

		// Create the panel that contains the two different interfaces
		cards = new JPanel(new CardLayout());
		cards.add(sidesPanel, EDGEPANEL);

		cards.add(pointPanel, POINTPANEL);

		pane.add(comboBoxPane, BorderLayout.PAGE_START);
		pane.add(cards, BorderLayout.CENTER);
	}

	// display GUI
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Heat Simulation");
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

	// main method
	public static void main(String[] args) {

		createAndShowGUI();
	}

	// called when run button is pushed-calls methods in TempAdjust and actually
	// runs simulation
	@Override
	public void actionPerformed(ActionEvent e) {
		String sTop = "", sBot = "", sLeft = "", sRight = "", sTemp = "";
		BigDecimal top, bot = null, left = null, right = null, temp = null;
		int red, green, blue;
		JLabel pointData;
		JPanel grid = new JPanel();
		JFrame frame = new JFrame("Heat Distribution");

		// adds default case in case the blanks are left empty
		// if not point heat, cases for sides temps and the version of counts
		// used here
		if (!point) {
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

			// dimensions if blank or out of bounds
			if (!dimRowField.getText().equals(""))

			{
				try {
					dRow = Integer.parseInt(dimRowField.getText());
				} catch (Exception ex) {
					dRow = 3;
				}
			} else
				dRow = 3;
			if (dRow < 1) {
				dRow = 3;
			}

			if (!dimColField.getText().equals("")) {
				try {
					dCol = Integer.parseInt(dimColField.getText());
				} catch (Exception ex) {
					dCol = 3;
				}
			} else
				dCol = 3;

			if (dCol < 1) {
				dCol = 3;
			}

			// verifies that all entered values are valid options, then
			// if not point heat, assigns value to the exterior temp variables

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
			finalPlate = iterator(oldPlateSides, newPlateSides, count, point, 0, 0, 0, 0);

			// final display frame
			grid.setLayout(new GridLayout(dRow, dCol));
			// conditional accounts for the 2 added rows and columns used in
			// edge
			// heat

			for (int i = 1; i <= dRow; i++) {
				for (int j = 1; j <= dCol; j++) {
					pointData = new JLabel(finalPlate[i][j] + "\t");
					blue = green = 0;
					// sets the color gradient of the text based on the
					// temperature (from black at 0 to red at 100+)
					red = (int) (finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() * 2.55);
					// ensures the red value is within acceptable range
					if (red > 255) {
						red = 255;
						green = (int) (((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 100) * 2.55)
								/ 2);
						if (green > 127.5) {
							green = (int) ((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 200)
									* 2.55);
							if (green > 255) {
								green = 255;
								blue = (int) ((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 300)
										* 2.55);
								if (blue > 255)
									blue = 255;
							}
						}
					}
					if (red < 0) {
						red = 0;
					}

					pointData.setForeground(new Color(red, green, blue));
					grid.add(pointData);
				}

			}
		}

		// if it IS point heat, temp, heat location, and version count cases
		else {
			if (!tempField.getText().equals("")) {
				sTemp = tempField.getText();
			} else
				sTemp = defaultTemp;

			if (!countFieldPoint.getText().equals("")) {
				try {
					count = Integer.parseInt(countFieldPoint.getText());
				} catch (Exception ex) {
					count = 0;
				}
			} else
				count = 0;

			if (!xFieldPoint.getText().equals("")) {
				try {
					x = Integer.parseInt(xFieldPoint.getText());
				} catch (Exception ex) {
					x = -1;
				}
			} else
				x = -1;

			if (!yFieldPoint.getText().equals("")) {
				try {
					y = Integer.parseInt(yFieldPoint.getText());
				} catch (Exception ex) {
					y = -1;
				}
			} else
				y = -1;

			if (!difFieldLR.getText().equals("")) {
				try {
					difLR = Integer.parseInt(difFieldLR.getText());
				} catch (Exception ex) {
					difLR = 1;
				}
			} else
				difLR = 1;

			if (!difFieldTB.getText().equals("")) {
				try {
					difTB = Integer.parseInt(difFieldTB.getText());
				} catch (Exception ex) {
					difTB = 1;
				}
			} else
				difTB = 1;

			// dimensions if blank or out of bounds
			if (!dimRowPoint.getText().equals(""))

			{
				try {
					dRowPoint = Integer.parseInt(dimRowPoint.getText());
				} catch (Exception ex) {
					dRowPoint = 3;
				}
			} else
				dRowPoint = 3;
			if (dRowPoint < 1) {
				dRowPoint = 3;
			}

			if (!dimColPoint.getText().equals("")) {
				try {
					dColPoint = Integer.parseInt(dimColPoint.getText());
				} catch (Exception ex) {
					dColPoint = 3;
				}
			} else
				dColPoint = 3;

			if (dColPoint < 1) {
				dColPoint = 3;
			}

			// makes a final plate for display purposes
			// for point heat, assigns temp value and double checks location of
			// heat
			// source for issue
			try {
				temp = new BigDecimal(sTemp);
			} catch (Exception ex) {
				sTemp = defaultTemp;
				temp = new BigDecimal(sTemp);
			}
			if (x < 0 || x >= dRowPoint)
				x = dRowPoint / 2;
			if (y < 0 || y >= dColPoint)
				y = dColPoint / 2;
			// checks size of heat source to ensure it exists
			if (difLR < 1)
				difLR = 1;
			if (difTB < 1)
				difTB = 1;
			// creates the plates
			BigDecimal[][] oldPlatePoint = new BigDecimal[dRowPoint][dColPoint];
			BigDecimal[][] newPlatePoint = new BigDecimal[dRowPoint][dColPoint];
			// sets edge temperatures for plates
			oldPlatePoint = initializePoint(oldPlatePoint, temp, y, x, difLR, difTB);
			// final plate for display purposes
			finalPlate = new BigDecimal[dRowPoint][dColPoint];
			finalPlate = initializePoint(newPlatePoint, temp, y, x, difLR, difTB);
			finalPlate = iterator(oldPlatePoint, newPlatePoint, count, point, x, y, difLR, difTB);

			// final display frame
			grid.setLayout(new GridLayout(dRowPoint, dColPoint));
			// conditional accounts for the 2 added rows and columns used in
			// edge
			// heat

			for (int i = 0; i < dRowPoint; i++) {
				for (int j = 0; j < dColPoint; j++) {
					blue = green = 0;
					pointData = new JLabel(finalPlate[i][j] + "\t");
					// sets the color gradient of the text based on the
					// temperature (from black at 0 to red at 100+)
					red = (int) (finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() * 2.55);
					// ensures the red value is within acceptable range
					if (red > 255) {
						red = 255;
						green = (int) (((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 100) * 2.55)
								/ 2);
						if (green > 127.5) {
							green = (int) ((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 200)
									* 2.55);
							if (green > 255) {
								green = 255;
								blue = (int) ((finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact() - 300)
										* 2.55);
								if (blue > 255)
									blue = 255;
							}
						}
					}
					if (red < 0) {
						red = 0;
					}

					pointData.setForeground(new Color(red, green, blue));
					grid.add(pointData);
				}
			}

		}
		frame.setContentPane(grid);
		frame.getContentPane().setBackground(Color.lightGray);
		frame.pack();
		frame.setVisible(true);

	}

	// changes in the version of interface selected or checking/unchecking any
	// of the checkboxes
	@Override
	public void itemStateChanged(ItemEvent evt) {
		// if the user changes which interface is selected, displays the
		// appropriate interface and updates the value of point
		if (evt.getSource() == cb) {
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, (String) evt.getItem());
			if (cb.getSelectedIndex() == 0) {
				point = false;
				dimRowPoint.setText("");
				dimColPoint.setText("");
				tempField.setText("");
				checkBox.setSelected(false);
				xyCheckBox.setSelected(false);
				difCheckBox.setSelected(false);
			} else {
				point = true;
				tField.setText("");
				bField.setText("");
				lField.setText("");
				rField.setText("");
				dimRowField.setText("");
				dimColField.setText("");
				countCheckBox.setSelected(false);
			}
		}
		// checks/unchecks number of runs checkbox on point
		// heat-enables/disables text box
		else if (evt.getSource() == checkBox) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
				countFieldPoint.setEnabled(true);
			} else if (evt.getStateChange() == ItemEvent.DESELECTED) {
				countFieldPoint.setEnabled(false);
				countFieldPoint.setText("");
			}
			validate();
			repaint();
		}
		// checks/unchecks number of runs checkbox on edge heat-enables/disables
		// text box
		else if (evt.getSource() == countCheckBox) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
				countField.setEnabled(true);
			} else if (evt.getStateChange() == ItemEvent.DESELECTED) {
				countField.setEnabled(false);
				countField.setText("");
			}
			validate();
			repaint();
		}
		// checks/unchecks diffuse heat checkbox
		// henables/disables text boxes and associated labels
		else if (evt.getSource() == difCheckBox) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
				difFieldLR.setEnabled(true);
				difFieldTB.setEnabled(true);
				difLabelLR.setText("Enter length of left side");
				difLabelTB.setText("Enter length of top side");
				diffuse.setText("Even entries will center up and left");
			} else if (evt.getStateChange() == ItemEvent.DESELECTED) {
				difFieldLR.setEnabled(false);
				difFieldTB.setEnabled(false);
				difFieldLR.setText("");
				difFieldTB.setText("");
				difLabelLR.setText(" ");
				difLabelTB.setText(" ");
				diffuse.setText(" ");
			}
			validate();
			repaint();
		}
		// checks/unchecks non-center heat checkbox
		// enables/disables text boxes and associated labels
		else {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
				xFieldPoint.setEnabled(true);
				yFieldPoint.setEnabled(true);
				center.setText("Enter x and y coords of source:");
				xLabel.setText("X-Position from top left (0 start)");
				yLabel.setText("Y-Position from top left (0 start)");

			} else if (evt.getStateChange() == ItemEvent.DESELECTED) {
				xFieldPoint.setEnabled(false);
				xFieldPoint.setText("");
				yFieldPoint.setEnabled(false);
				yFieldPoint.setText("");
				center.setText(" ");
				xLabel.setText(" ");
				yLabel.setText(" ");
			}

			validate();
			repaint();
		}
	}

}
