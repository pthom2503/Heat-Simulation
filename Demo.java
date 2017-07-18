package demoPack;

import java.util.Scanner;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;


public class Demo extends TempAdjust{

	// number of iterations to run


	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// default vals for if a user forgets a flag
		BigDecimal top = new BigDecimal("0");
		top = top.setScale(2, RoundingMode.HALF_UP);
		BigDecimal bot = new BigDecimal("0");
		bot = bot.setScale(2, RoundingMode.HALF_UP);
		BigDecimal left = new BigDecimal("0");
		left = left.setScale(2, RoundingMode.HALF_UP);
		BigDecimal right = new BigDecimal("0");
		right = right.setScale(2, RoundingMode.HALF_UP);
		int d = 3;
		int x = 0;
		int count = 0;
		String arg;

		// processes each of the flags and assigns the value after them to the
		// corresponding variable
		// if a value after a flag is missing, the resulting error will be of
		// type mismatch as it tries to cast the next flag to the value
		// or, if at end, out of bounds
		while (x < args.length && args[x].startsWith("-")) {
			arg = args[x++];
			switch (arg) {
			case "-d":
				try {
					d = Math.abs(Integer.parseInt(args[x++]));
				} catch (NumberFormatException e) {
					System.out.println("Warning-attepmt to initialize dimensions to non-number value");
					System.out.println("Initializing dimensions to default size of " + d);
				}
				break;
			case "-t":
				try {
					top = new BigDecimal(args[x++]);
					top = top.setScale(2, RoundingMode.HALF_UP);
				} catch (NumberFormatException e) {
					System.out.println("Warning-attempt to initialize top temp to non-number value");
					System.out.println("Initializing temp to default of " + top);
				}
				break;
			case "-b":
				try{
				bot = new BigDecimal(args[x++]);
				bot = bot.setScale(2, RoundingMode.HALF_UP);
				}
				catch(NumberFormatException e){
					System.out.println("Warning-attempt to initialize bottom temp to non-number value");
					System.out.println("Initializing temp to default of "+bot);
				}
				break;
			case "-l":
				try{
				left = new BigDecimal(args[x++]);
				left = left.setScale(2, RoundingMode.HALF_UP);
				}
				catch(NumberFormatException e){
					System.out.println("Warning-attempt to initialize left temp to non-number value");
					System.out.println("Initializing temp to default of " + left);
				}
				break;
			case "-r":
				try{
				right = new BigDecimal(args[x++]);
				right = right.setScale(2, RoundingMode.HALF_UP);
				}
				catch(NumberFormatException e){
					System.out.println("Warning-attepmt to initialize right temp to non-number value");
					System.out.println("Initializing temp to default of " + right);
				}
				break;
			case "-c":
				try{
					count = Math.abs(Integer.parseInt(args[x++]));
				}
				catch(Exception e){
					System.out.println("Warning-attempt to initialize number of iterations to non-number value");
					System.out.println("Defaulting to temperature stablization as cut off");
				}
				break;
			default:
				System.err.println("Illegal option " + arg);
				break;
			}
		}

		BigDecimal[][] oldPlate = new BigDecimal[d + 2][d + 2];
		BigDecimal[][] newPlate = new BigDecimal[d + 2][d + 2];
		// sets edge temperatures for plates
		oldPlate = initialize(oldPlate, top, bot, left, right);
		BigDecimal[][] finalPlate = new BigDecimal[d + 2][d + 2]; 
		finalPlate = initialize(newPlate, top, bot, left, right);
		
		finalPlate = iterator(oldPlate, newPlate, count);
		// shows initial edge temperatures
		JFrame frame = new JFrame("Heat simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(d, d));
		for (int i = 1; i < d + 1; i++) {
			for (int j = 1; j < d + 1; j++) {
				grid.add(new JLabel(finalPlate[i][j] + "\t"));
			}
			frame.add(grid);
			 frame.pack();
		        frame.setVisible(true);
			
		}
	}

	
}
