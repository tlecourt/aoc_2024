package com.tlecourt.aoc_2024.impl;

import java.util.List;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle4_1 extends AbstractPuzzle {

	public Puzzle4_1() {
		super(4, 1);
	}

	@Override
	protected String solve(List<String> input) {
		final String word = "XMAS";
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

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				// Only test if the character is the same as the beginning of the word.
				if (table[row][col] == word.charAt(0)) {
					result += validDirectionsFromStart(table, word, width, height, row, col);
				}
			}
		}

		return Integer.toString(result);
	}

	private int validDirectionsFromStart(final char[][] table, final String word, final int width, final int height,
			final int row, final int col) {
		System.out.println("Starting position: " + row + " - " + col);
		final int wordLength = word.length();
		int validDirections = 0;
		for (int i = -1; i <= 1; i++) {
			// Avoid directions we can't go too.
			if (i < 0 && row < wordLength - 1 || i > 0 && row > height - wordLength) {
				// No need to test, as we're too close to the edge to go left or right.
				continue;
			}
			for (int j = -1; j <= 1; j++) {
				// Avoid directions we can't go too.
				if (j < 0 && col < wordLength - 1 || j > 0 && col > width - wordLength) {
					// No need to test, as we're too close to the edge to go left or right.
					continue;
				}
				// Do nothing when no direction is given.
				if (i == 0 && j == 0) {
					continue;
				}

				try {
					int posX = row;
					int posY = col;
					StringBuilder test = new StringBuilder();
					System.out.println(posX + " - " + posY);
					test.append(table[posX][posY]);
					while (test.length() < wordLength && word.startsWith(test.toString())) {
						posX += i;
						posY += j;
						System.out.println("New pos: " + posX + " - " + posY);
						test.append(table[posX][posY]);
					}
					if (word.equals(test.toString())) {
						validDirections++;
					} else {
						System.err.println("Invalid string: " + test.toString());
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// Do nothing.
					System.err.println(
							"Shouldn't have reached this part. Just a safety net back when the condition to not go through a loop was wrong. Helped me get the actual result earlier.");
				}
			}
		}
		System.out.println("Valid words from position: " + row + " - " + col + " - " + validDirections);
		return validDirections;
	}

}
