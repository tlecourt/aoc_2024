package com.tlecourt.aoc_2024.impl;

import java.util.List;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle4_2 extends AbstractPuzzle {

	public Puzzle4_2() {
		super(4, 1);
	}

	@Override
	protected String solve(List<String> input) {
		int result = 0;

		final int width = input.get(0).length();
		final int height = input.size();

		char[][] table = new char[width][height];
		// fill table with input. Not efficient but it'll make the code nicer to test
		// directions.

		for (int row = 0; row < height; row++) {
			String data = input.get(row);
			for (int col = 0; col < width; col++) {
				table[row][col] = data.charAt(col);
			}
		}

		for (int row = 1; row < height - 1; row++) {
			for (int col = 1; col < width - 1; col++) {
				// Hard-coding the word in the inner method.
				if (table[row][col] == 'A') {
					result += validDirectionsFromStart(table, width, height, row, col);
				}
			}
		}

		return Integer.toString(result);
	}

	private int validDirectionsFromStart(final char[][] table, final int width, final int height,
			final int row, final int col) {
		int validDirections = 0;
		// We already ignored the first and last rows and columns and we know the letter
		// at "row" and "col" is an 'A'.
		// Check both diagonals
		StringBuilder diag1 = new StringBuilder();
		StringBuilder diag2 = new StringBuilder();

		diag1.append(table[row - 1][col - 1]).append('A').append(table[row + 1][col + 1]);
		diag2.append(table[row + 1][col - 1]).append('A').append(table[row - 1][col + 1]);

		if ((diag1.toString().equals("MAS") || diag1.reverse().toString().equals("MAS"))
				&& (diag2.toString().equals("MAS") || diag2.reverse().toString().equals("MAS"))) {
			System.out.println("Valid 'A' at: " + row + " - " + col);
			validDirections++;
		}

		return validDirections;
	}

}
