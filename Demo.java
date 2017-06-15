package demoPack;

import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Demo {

	// number of iterations to run
	static int COUNT = 0;

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
				d = Integer.parseInt(args[x++]);
				break;
			case "-t":
				top = new BigDecimal(args[x++]);
				top = top.setScale(2, RoundingMode.HALF_UP);
				break;
			case "-b":
				bot = new BigDecimal(args[x++]);
				bot = bot.setScale(2, RoundingMode.HALF_UP);
				break;
			case "-l":
				left = new BigDecimal(args[x++]);
				left = left.setScale(2, RoundingMode.HALF_UP);
				break;
			case "-r":
				right = new BigDecimal(args[x++]);
				break;
			case "-c":
				COUNT = Integer.parseInt(args[x++]);
				right = right.setScale(2, RoundingMode.HALF_UP);
				break;
			default:
				System.err.println("ParseCmdLine: illegal option " + arg);
				break;
			}
		}

		BigDecimal[][] oldPlate = new BigDecimal[d + 2][d + 2];
		BigDecimal[][] newPlate = new BigDecimal[d + 2][d + 2];
		// sets edge temperatures for plates
		oldPlate = initialize(oldPlate, top, bot, left, right);
		newPlate = initialize(newPlate, top, bot, left, right);
		// shows initial edge temperatures
		for (int i = 0; i <= d + 1; i++) {
			for (int j = 0; j <= d + 1; j++) {
				System.out.print(oldPlate[i][j] + "\t");
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
	public static void iterator(BigDecimal[][] oldPlate, BigDecimal[][] newPlate) {
		int runs = 0;
		int d = oldPlate.length - 2;
		BigDecimal four = new BigDecimal("4");
		four = four.setScale(2, RoundingMode.HALF_UP);
		BigDecimal[][] oldPlateCopy = new BigDecimal[oldPlate.length][oldPlate.length];
		for (int k = 0; k < oldPlate.length; k++) {
			for (int m = 0; m < oldPlate.length; m++) {
				oldPlateCopy[k][m] = oldPlate[k][m];
			}
		}
		// while the wanted num of iterations hasn't been reached, if a user
		// entered a number OR temp isn't stable
		while (!done(runs, oldPlateCopy, newPlate)) {
			// for each point on the plate, average surrounding temps for new
			// temp
			for (int i = 1; i <= d; i++) {
				for (int j = 1; j <= d; j++) {
					newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j + 1])
							.add(oldPlate[i][j - 1])).divide(four);
					newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);
				}
			}
			runs++;
			// change new iteration of temps to the base (old)
			for (int k = 0; k < oldPlate.length; k++) {
				for (int m = 0; m < oldPlate.length; m++) {
					oldPlateCopy[k][m] = oldPlate[k][m];
				}
			}
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
	public static BigDecimal[][] initialize(BigDecimal[][] plate, BigDecimal top, BigDecimal bot, BigDecimal left,
			BigDecimal right) {
		BigDecimal two = new BigDecimal("2");
		two = two.setScale(2, RoundingMode.HALF_UP);
		for (int i = 0; i < plate.length; i++) {
			plate[0][i] = top;
			plate[plate.length - 1][i] = bot;
			plate[i][0] = left;
			plate[i][plate.length - 1] = right;
			plate[0][i] = plate[0][i].setScale(2, RoundingMode.HALF_UP);
			plate[plate.length - 1][i] = plate[plate.length - 1][i].setScale(2, RoundingMode.HALF_UP);
			plate[i][0] = plate[i][0].setScale(2, RoundingMode.HALF_UP);
			plate[i][plate.length - 1] = plate[i][plate.length - 1].setScale(2, RoundingMode.HALF_UP);
		}
		for (int i = 1; i < plate.length - 1; i++) {
			for (int j = 1; j < plate.length - 1; j++) {
				plate[i][j] = new BigDecimal("0");
				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}

		plate[0][0] = (top.add(left)).divide(two);
		plate[0][plate.length - 1] = (top.add(right)).divide(two);
		plate[plate.length - 1][0] = (bot.add(left)).divide(two);
		plate[plate.length - 1][plate.length - 1] = (bot.add(right)).divide(two);
		plate[0][0] = plate[0][0].setScale(2, RoundingMode.HALF_UP);
		plate[0][plate.length - 1] = plate[0][plate.length - 1].setScale(2, RoundingMode.HALF_UP);
		plate[plate.length - 1][0] = plate[plate.length - 1][0].setScale(2, RoundingMode.HALF_UP);
		plate[plate.length - 1][plate.length - 1] = plate[plate.length - 1][plate.length - 1].setScale(2,
				RoundingMode.HALF_UP);
		return plate;

	}

	// takes the number of runs completed and returns true if equal to the
	// number of runs wanted
	public static boolean done(int runs, BigDecimal[][] older, BigDecimal[][] newer) {
		boolean isDone = true;
		if (COUNT != 0) {
			if (runs != COUNT) {
				isDone = false;
				return isDone;
			}
		} else if (runs <= 1) {
			isDone = false;
			return isDone;
		} else {
			for (int k = 0; k < older.length; k++) {
				for (int m = 0; m < older.length; m++) {
					if (older[k][m] != newer[k][m])
						isDone = false;
				}
			}
		}
		return isDone;
	}

	// takes 2 double arrays and replaces the value of "old" with the current
	// values in "new"
	public static BigDecimal[][] swap(BigDecimal[][] oldPlate, BigDecimal[][] newPlate) {
		for (int i = 0; i < oldPlate.length; i++) {
			for (int j = 0; j < oldPlate.length; j++) {
				oldPlate[i][j] = newPlate[i][j];
				oldPlate[i][j]=oldPlate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}

		return (oldPlate);
	}

}
