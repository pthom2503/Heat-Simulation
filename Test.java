import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import demoPack.TempAdjust;

@SuppressWarnings("serial")
public class Test extends TempAdjust implements ActionListener {
	// Private variables of the GUI components
	JTextField tField, bField, lField, rField, dField, iField;
	JButton runButton;
	JTextArea tArea;
	JFormattedTextField formattedField;
	JTextField tft, tfb, tfl, tfr;
	JLabel tLab, bLab, lLab, rLab, dLab;
	int d, count;
	String defaultTemp = "0";

	// Constructor to set up all the GUI components
	public Test() {
		// JPanel for the text fields
		JPanel tfPanel = new JPanel(new GridLayout(7, 2, 10, 2));
		tfPanel.setBorder(BorderFactory.createTitledBorder("Input Plate Information: "));

		// Top temp (Row 1)
		tfPanel.add(new JLabel("  Top Temperature: "));
		tField = new JTextField(10);
		tfPanel.add(tField);

		// Bot temp (Row 2)
		tfPanel.add(new JLabel("  Bottom Temperature: "));
		bField = new JTextField(10);
		tfPanel.add(bField);

		// Left temp (Row 3)
		tfPanel.add(new JLabel("  Left Temperature: "));
		lField = new JTextField(10);
		tfPanel.add(lField);

		// Right temp (Row 4)
		tfPanel.add(new JLabel("  Right Temperature: "));
		rField = new JTextField(10);
		tfPanel.add(rField);

		// Dimensions (Row 5)
		tfPanel.add(new JLabel("  Dimension: "));
		dField = new JTextField(10);
		tfPanel.add(dField);

		// Number of runs to do (Row 6)
		// if checkBox is enabled, will use number of runs given, else will run
		// until temp stabilizes
		iField = new JTextField(10);
		JCheckBox checkBox = new JCheckBox("Use set number of runs", false);
		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					iField.setEnabled(true);

				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					iField.setEnabled(false);
					iField.setText("");
				}

				validate();
				repaint();
			}
		});

		tfPanel.add(checkBox);
		tfPanel.add(iField);

		// button to run simulation with given data
		runButton = new JButton("Run");
		tfPanel.add(runButton);
		// if the actionListener is activated, opens a new JFrame with the
		// resulting plate with heat distribution
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sTop, sBot, sLeft, sRight;
				BigDecimal top, bot, left, right;
				int red;

				// adds default case in case the blanks are left empty
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

				if (!dField.getText().equals("")) {
					try {
						d = Integer.parseInt(dField.getText());
					} catch (Exception ex) {
						d = 3;
					}
				} else
					d = 3;

				if (!iField.getText().equals("")) {
					try {
						count = Integer.parseInt(iField.getText());
					} catch (Exception ex) {
						count = 0;
					}
				} else
					count = 0;

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
				BigDecimal[][] oldPlate = new BigDecimal[d + 2][d + 2];
				BigDecimal[][] newPlate = new BigDecimal[d + 2][d + 2];
				// sets edge temperatures for plates
				oldPlate = initialize(oldPlate, top, bot, left, right);
				BigDecimal[][] finalPlate = new BigDecimal[d + 2][d + 2];
				// makes a final plate for display purposes
				finalPlate = initialize(newPlate, top, bot, left, right);

				finalPlate = iterator(oldPlate, newPlate, count);

				// final display frame
				JFrame frame = new JFrame("Heat Distribution");
				
				JPanel grid = new JPanel();
				grid.setLayout(new GridLayout(d, d));
				for (int i = 1; i < d + 1; i++) {
					for (int j = 1; j < d + 1; j++) {
						JLabel pointData = new JLabel(finalPlate[i][j] + "\t");
						
						//sets the color gradient of the text based on the temperature
						red = (int) (finalPlate[i][j].setScale(0, RoundingMode.DOWN).intValueExact()*2.55);
						
						//ensures the red value is within acceptable range
						if(red>255){
							red=255;
						}
						if(red<0){
							red=0;
						}
						
						pointData.setForeground(new Color(red, 0, 0));
						grid.add(pointData);
					}
					frame.setContentPane(grid);
					frame.getContentPane().setBackground(Color.lightGray);
					frame.pack();
					frame.setVisible(true);

				}
			}

		});

		// Setup the content-pane of JFrame in BorderLayout
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout(5, 5));
		cp.add(tfPanel, BorderLayout.NORTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Heat Distribution Simultation");
		setSize(450, 350);
		setVisible(true);
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Run GUI codes in Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Test(); // Let the constructor do the job
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
