package demoPack;

import java.util.Scanner;

public class Demo {

	// number of iterations to run
	static int COUNT = 5000;

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		// default vals for if a user forgets a flag
		double top = 0;
		double bot = 0;
		double left = 0;
		double right = 0;
		int d = 3;
		int x = 0, y;
		String arg;

		while (x < args.length && args[x].startsWith("-")) {
			arg = args[x++];
			switch (arg) {
			case "-d":
				System.out.println("Option d");
				d = Integer.parseInt(args[x]);
				break;
			case "-t":
				System.out.println("Option t");
				top = Double.parseDouble(args[x]);
				break;
			case "-b":
				System.out.println("Option b");
				bot = Double.parseDouble(args[x]);
				break;
			case "-l":
				System.out.println("Option l");
				left = Double.parseDouble(args[x]);
				break;
			case "-r":
				System.out.println("Option r");
				right = Double.parseDouble(args[x]);
				break;
			case "-i":
				System.out.println("Option i");
				COUNT = Integer.parseInt(args[x]);
			break;
			default:
				System.err.println("ParseCmdLine: illegal option " + arg);
				break;
			}
			x++;

		}

		double[][] oldPlate = new double[d + 2][d + 2];
		double[][] newPlate = new double[d + 2][d + 2];
		// sets edge temperatures for plates
		oldPlate = initialize(oldPlate, top, bot, left, right);
		newPlate = initialize(newPlate, top, bot, left, right);
		// shows initial edge temperatures
		for (int i = 0; i <= d + 1; i++) {
			for (int j = 0; j <= d + 1; j++) {
				System.out.print(newPlate[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		// runs simulation
		iterator(oldPlate, newPlate);

	}

	// takes the double 2d arrays old and new and iterates through the
	// temperature change
	// prints final array of heat distribution
	public static void iterator(double[][] oldPlate, double[][] newPlate) {
		int runs = 0;
		int d = oldPlate.length - 2;
		// while the wanted num of iterations hasn't been reached
		while (!done(runs)) {
			// for each point on the plate, average surrounding temps for new
			// temp
			for (int i = 1; i <= d; i++) {
				for (int j = 1; j <= d; j++) {
					newPlate[i][j] = (oldPlate[i + 1][j] + oldPlate[i - 1][j] + oldPlate[i][j + 1] + oldPlate[i][j - 1])
							/ 4.0;
				}
			}
			runs++;
			// change new iteration of temps to the base (old)
			oldPlate = swap(oldPlate, newPlate);
		}

		for (int i = 1; i <= d; i++) {
			for (int j = 1; j <= d; j++) {
				System.out.print(newPlate[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();

	}

	// takes the 2D double array plate and double values and returns the 2D
	// array
	// with edge values in place
	public static double[][] initialize(double[][] plate, double top, double bot, double left, double right) {
		for (int i = 0; i < plate.length; i++) {
			plate[0][i] = top;
			plate[plate.length - 1][i] = bot;
			plate[i][0] = left;
			plate[i][plate.length - 1] = right;
		}
		plate[0][0] = (top + left) / 2.0;
		plate[0][plate.length - 1] = (top + right) / 2.0;
		plate[plate.length - 1][0] = (bot + left) / 2.0;
		plate[plate.length - 1][plate.length - 1] = (bot + right) / 2.0;
		return plate;

	}

	// takes the number of runs completed and returns true if equal to the
	// number of runs wanted
	public static boolean done(int runs) {
		boolean isDone = false;
		if (runs == COUNT) {
			isDone = true;
		}
		return isDone;
	}

	// takes 2 double arrays and replaces the value of "old" with the current
	// values in "new"
	public static double[][] swap(double[][] oldPlate, double[][] newPlate) {
		for (int i = 0; i < oldPlate.length; i++) {
			for (int j = 0; j < oldPlate.length; j++) {
				oldPlate[i][j] = newPlate[i][j];
			}
		}

		return (oldPlate);
	}

}
