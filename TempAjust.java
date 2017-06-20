package demoPack;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TempAdjust {
	// takes the double 2d arrays old and new and iterates through the
	// temperature change
	// prints final array of heat distribution
	public static void iterator(BigDecimal[][] oldPlate, BigDecimal[][] newPlate, int count) {
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
	while (!done(runs, oldPlateCopy, newPlate, count)) {
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
		public static boolean done(int runs, BigDecimal[][] older, BigDecimal[][] newer, int count) {
			boolean isDone = true;
			if (count != 0) {
				if (runs != count) {
					isDone = false;
					return isDone;
				}
			} else if (runs <= 1) {
				isDone = false;
				return isDone;
			} else {
				for (int k = 0; k < older.length; k++) {
					for (int m = 0; m < older.length; m++) {
						if (!older[k][m].equals(newer[k][m]))
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
					oldPlate[i][j] = oldPlate[i][j].setScale(2, RoundingMode.HALF_UP);
				}
			}

			return (oldPlate);
		}

	}
