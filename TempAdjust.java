package demoPack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JFrame;

public class TempAdjust extends JFrame {
	// takes the double 2d arrays old and new and iterates through the
	// temperature change
	// returns 2D BigDecimal array of final heat distribution
	public static BigDecimal[][] iterator(BigDecimal[][] oldPlate, BigDecimal[][] newPlate, int count, boolean center) {
		int runs = 0;
		int rowCent = oldPlate.length / 2;
		int colCent = oldPlate[0].length / 2;
		BigDecimal two = new BigDecimal("2");
		BigDecimal three = new BigDecimal("3");
		BigDecimal four = new BigDecimal("4");
		long x;
		two = two.setScale(2, RoundingMode.HALF_UP);
		three = three.setScale(2, RoundingMode.HALF_UP);
		four = four.setScale(2, RoundingMode.HALF_UP);
		BigDecimal[][] oldPlateCopy = new BigDecimal[oldPlate.length][oldPlate[0].length];
		for (int k = 0; k < oldPlate.length; k++) {
			for (int m = 0; m < oldPlate[k].length; m++) {
				oldPlateCopy[k][m] = oldPlate[k][m];
			}
		}
		boolean sideTriggered = false;
		boolean cornerTriggered = false;
		// while the wanted num of iterations hasn't been reached, if a user
		// entered a number OR temp isn't stable

		while (!done(runs, oldPlateCopy, newPlate, count)) {
			// for each point on the plate, average surrounding temps for
			// new
			// temp
			if (!center) {
				for (int i = 1; i <= oldPlate.length - 2; i++) {
					for (int j = 1; j <= oldPlate[0].length - 2; j++) {

						newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j + 1])
								.add(oldPlate[i][j - 1])).divide(four);
						newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

					}
				}
			} else {

				for (int i = 0; i < oldPlate.length; i++) {
					for (int j = 0; j < oldPlate[i].length; j++) {
						if (i == rowCent && j == colCent) {
						} else {
							if (i == 0 && j != 0 && j != oldPlate[0].length - 1) {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i][j + 1]).add(oldPlate[i][j - 1]))
										.divide(three, 2, RoundingMode.HALF_UP);
							} else if (i == oldPlate.length - 1 && j != 0 && j != oldPlate[0].length - 1) {
								newPlate[i][j] = (oldPlate[i - 1][j].add(oldPlate[i][j + 1]).add(oldPlate[i][j - 1]))
										.divide(three, 2, RoundingMode.HALF_UP);
							} else if (j == 0 && i != 0 && i != oldPlate.length - 1) {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j + 1]))
										.divide(three, 2, RoundingMode.HALF_UP);
							} else if (j == oldPlate[0].length - 1 && i != 0 && i != oldPlate.length - 1) {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j - 1]))
										.divide(three, 2, RoundingMode.HALF_UP);

							} else if (i == 0 && j == 0) {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i][j + 1])).divide(two);
								newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

							} else if (i == 0 && j == oldPlate[0].length - 1) {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i][j - 1])).divide(two);
								newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

							} else if (j == 0 && i == oldPlate.length - 1) {
								newPlate[i][j] = (oldPlate[i - 1][j].add(oldPlate[i][j + 1])).divide(two);
								newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

							} else if (i == oldPlate.length - 1 && j == oldPlate[0].length - 1) {
								newPlate[i][j] = (oldPlate[i - 1][j].add(oldPlate[i][j - 1])).divide(two);
								newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

							} else {
								newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j + 1])
										.add(oldPlate[i][j - 1])).divide(four);
								newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);
							}
						}
						x = newPlate[i][j].longValue();
					}
				}
			}
			runs++;
			// change new iteration of temps to the base (old)
			for (int k = 0; k < oldPlate.length; k++) {
				for (int m = 0; m < oldPlate[k].length; m++) {
					oldPlateCopy[k][m] = oldPlate[k][m];
				}
			}
			oldPlate = swap(oldPlate, newPlate);

		}
		return newPlate;
	}

	// takes the 2D double array plate and double values and returns the 2D
	// array
	// with edge values in place and 0 values in all other locations
	public static BigDecimal[][] initializeSides(BigDecimal[][] plate, BigDecimal top, BigDecimal bot, BigDecimal left,
			BigDecimal right) {
		BigDecimal two = new BigDecimal("2");
		two = two.setScale(2, RoundingMode.HALF_UP);
		int rows = plate.length;
		int columns = plate[0].length;

		for (int i = 0; i < plate.length; i++) {
			for (int j = 0; j < plate[i].length; j++) {
				if (i == 0) {
					plate[i][j] = top;
				} else if (i == plate.length - 1) {
					plate[i][j] = bot;
				} else if (j == 0) {
					plate[i][j] = left;
				}

				else
					plate[i][j] = right;

				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);

			}
		}

		// sets all interior values on the plate to 0
		for (int i = 1; i < plate.length - 1; i++) {
			for (int j = 1; j < plate[i].length - 1; j++) {
				plate[i][j] = new BigDecimal("0");
				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}
		// sets the corner temp values as the average of the two sides meeting
		// at that corner

		plate[0][0] = (top.add(left)).divide(two);
		plate[0][columns - 1] = (top.add(right)).divide(two);
		plate[rows - 1][0] = (bot.add(left)).divide(two);
		plate[rows - 1][columns - 1] = (bot.add(right)).divide(two);
		plate[0][0] = plate[0][0].setScale(2, RoundingMode.HALF_UP);
		plate[0][columns - 1] = plate[0][columns - 1].setScale(2, RoundingMode.HALF_UP);
		plate[rows - 1][0] = plate[rows - 1][0].setScale(2, RoundingMode.HALF_UP);
		plate[rows - 1][columns - 1] = plate[rows - 1][columns - 1].setScale(2, RoundingMode.HALF_UP);
		return plate;

	}

	public static BigDecimal[][] initializeCent(BigDecimal[][] plate, BigDecimal temp) {
		BigDecimal two = new BigDecimal("2");
		two = two.setScale(2, RoundingMode.HALF_UP);
		int rows = plate.length;
		int columns = plate[0].length;

		// sets all interior values on the plate to 0
		for (int i = 0; i < plate.length; i++) {
			for (int j = 0; j < plate[i].length; j++) {
				plate[i][j] = new BigDecimal("0");
				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}
		// sets the corner temp values as the average of the two sides meeting
		// at that corner

		plate[rows / 2][columns / 2] = temp;
		plate[rows / 2][columns / 2] = plate[rows / 2][columns / 2].setScale(2, RoundingMode.HALF_UP);
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
				for (int m = 0; m < older[0].length; m++) {
					if (!older[k][m].equals(newer[k][m])) {
						isDone = false;
						return isDone;
					}
				}
			}
		}
		return isDone;
	}

	// takes 2 double arrays and replaces the value of "old" with the current
	// values in "new"
	public static BigDecimal[][] swap(BigDecimal[][] oldPlate, BigDecimal[][] newPlate) {
		for (int i = 0; i < oldPlate.length; i++) {
			for (int j = 0; j < oldPlate[i].length; j++) {
				oldPlate[i][j] = newPlate[i][j];
				oldPlate[i][j] = oldPlate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}

		return (oldPlate);
	}

}
