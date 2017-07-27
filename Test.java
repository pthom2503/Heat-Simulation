import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import demoPack.TempAdjust;


public class Test extends TempAdjust implements ActionListener {
	// Private variables of the GUI components
	JTextField tField, bField, lField, rField, dimRowField, dimColField, countField;
	JButton runButton;
	int dRow, dCol, count;
	String defaultTemp = "0";

	// Constructor to set up all the GUI components
	public Test() {
		// JPanel for the text fields
		JPanel tfPanel = new JPanel(new GridLayout(8, 2, 10, 2));
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

		// Rows (Row 5)
		tfPanel.add(new JLabel("  Rows: "));
		dimRowField = new JTextField(10);
		tfPanel.add(dimRowField);
		
		// Columns (Row 6)
				tfPanel.add(new JLabel("  Columns: "));
				dimColField = new JTextField(10);
				tfPanel.add(dimColField);

		// Number of runs to do (Row 7)
		// if checkBox is enabled, will use number of runs given, else will run
		// until temp stabilizes
		countField = new JTextField(10);
		JCheckBox countCheckBox = new JCheckBox("Use set number of runs", false);
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

		tfPanel.add(countCheckBox);
		tfPanel.add(countField);

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

				if (!dimRowField.getText().equals("")) {
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
				
				if (!countField.getText().equals("")) {
					try {
						count = Integer.parseInt(countField.getText());
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
				BigDecimal[][] oldPlate = new BigDecimal[dRow + 2][dCol + 2];
				BigDecimal[][] newPlate = new BigDecimal[dRow + 2][dCol + 2];
				// sets edge temperatures for plates
				oldPlate = initialize(oldPlate, top, bot, left, right);
				BigDecimal[][] finalPlate = new BigDecimal[dRow + 2][dCol + 2];
				// makes a final plate for display purposes
				finalPlate = initialize(newPlate, top, bot, left, right);
	
				finalPlate = iterator(oldPlate, newPlate, count);

				// final display frame
				JFrame frame = new JFrame("Heat Distribution");
				
				JPanel grid = new JPanel();
				grid.setLayout(new GridLayout(dRow, dCol));
				for (int i = 1; i <= dRow; i++) {
					for (int j = 1; j <= dCol; j++) {
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
