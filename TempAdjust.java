package demoPack;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JFrame;

public class TempAdjust extends JFrame {
	// takes the double 2d arrays old and new and iterates through the
	// temperature change
	// returns 2D BigDecimal array of final heat distribution
	public static BigDecimal[][] iterator(BigDecimal[][] oldPlate, BigDecimal[][] newPlate, int count, boolean point,
			int x, int y, int difLR, int difTB) {
		int runs = 0;
		int dL = (int)Math.ceil((difLR)/2);
		int dR = (int)Math.floor((difLR-1)/2);
		int dT = (int)Math.ceil((difTB)/2);
		int dB = (int)Math.floor((difTB-1)/2);
		boolean dif = false;
		BigDecimal two = new BigDecimal("2");
		BigDecimal three = new BigDecimal("3");
		BigDecimal four = new BigDecimal("4");
		two = two.setScale(2, RoundingMode.HALF_UP);
		three = three.setScale(2, RoundingMode.HALF_UP);
		four = four.setScale(2, RoundingMode.HALF_UP);
		// makes acopy of the older version for later comparison
		BigDecimal[][] oldPlateCopy = new BigDecimal[oldPlate.length][oldPlate[0].length];
		for (int k = 0; k < oldPlate.length; k++) {
			for (int m = 0; m < oldPlate[k].length; m++) {
				oldPlateCopy[k][m] = oldPlate[k][m];
			}
		}
		// while the wanted num of iterations hasn't been reached, if a user
		// entered a number OR temp isn't stable

		while (!done(runs, oldPlateCopy, newPlate, count)) {
			// for each point on the plate, average surrounding temps for
			// new temp

			// if the plate is being heated from the edges:
			if (!point) {
				for (int i = 1; i <= oldPlate.length - 2; i++) {
					for (int j = 1; j <= oldPlate[0].length - 2; j++) {

						newPlate[i][j] = (oldPlate[i + 1][j].add(oldPlate[i - 1][j]).add(oldPlate[i][j + 1])
								.add(oldPlate[i][j - 1])).divide(four);
						newPlate[i][j] = newPlate[i][j].setScale(2, RoundingMode.HALF_UP);

					}
				}

				// if the plate has a point heat source
			} else {

				for (int i = 0; i < oldPlate.length; i++) {
					for (int j = 0; j < oldPlate[i].length; j++) {
						// don't adjust if it is the heat source
						for(int k = x - dL; k <= x + dR; k++){
							for(int m = y - dT; m <= y + dB; m++){
								if(i == k && j == m){
									dif = true;
								}
							}
						}
						
						// sides cases
						if(!dif) {
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
								// corner cases
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
						dif = false;
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
		// edge heats
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
		plate[0][plate[0].length - 1] = (top.add(right)).divide(two);
		plate[plate.length - 1][0] = (bot.add(left)).divide(two);
		plate[plate.length - 1][plate[0].length - 1] = (bot.add(right)).divide(two);
		plate[0][0] = plate[0][0].setScale(2, RoundingMode.HALF_UP);
		plate[0][plate[0].length - 1] = plate[0][plate[0].length - 1].setScale(2, RoundingMode.HALF_UP);
		plate[plate.length - 1][0] = plate[plate.length - 1][0].setScale(2, RoundingMode.HALF_UP);
		plate[plate.length - 1][plate[0].length - 1] = plate[plate.length - 1][plate[0].length - 1].setScale(2,
				RoundingMode.HALF_UP);
		return plate;

	}

	public static BigDecimal[][] initializePoint(BigDecimal[][] plate, BigDecimal temp, int x, int y, int difLR, int difTB) {
		int dL = (int)Math.ceil((difLR)/2);
		int dR = (int)Math.floor((difLR-1)/2);
		int dT = (int)Math.ceil((difTB)/2);
		int dB = (int)Math.floor((difTB-1)/2);
		BigDecimal two = new BigDecimal("2");
		two = two.setScale(2, RoundingMode.HALF_UP);

		// sets all interior values on the plate to 0
		for (int i = 0; i < plate.length; i++) {
			for (int j = 0; j < plate[i].length; j++) {
				plate[i][j] = new BigDecimal("0");
				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);
			}
		}
		// sets the point heat source to the specified temp

		for(int i = x - dL; i <= x + dR; i++){
			for(int j = y - dT; j <= y + dB; j++){
				if(i < 0 || j < 0 || i >= plate.length || j >=plate[0].length){
				}
				else{
				plate[i][j] = temp;
				plate[i][j] = plate[i][j].setScale(2, RoundingMode.HALF_UP);
				}
				}
			}
		
		
		return plate;

	}

	// takes the number of runs completed and returns true if equal to the
	// number of runs wanted
	// if num of runs not given by user initially, returns true if all points in
	// the older and newer versions of the plate are equal (to 2 decimal places)
	public static boolean done(int runs, BigDecimal[][] older, BigDecimal[][] newer, int count) {
		boolean isDone = true;
		if (count != 0) {
			if (runs != count) {
				isDone = false;
			}
		} else if (runs <= 1) {
			isDone = false;
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
