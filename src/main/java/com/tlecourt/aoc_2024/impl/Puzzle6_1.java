package com.tlecourt.aoc_2024.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle6_1 extends AbstractPuzzle {

	public Puzzle6_1() {
		super(6, 1);
	}

	@Override
	protected String solve(List<String> input) {
		int result = 0;

		final int width = input.get(0).length();
		final int height = input.size();

		char[][] table = new char[width][height];

		int startRow = 0;
		int startCol = 0;

		// fill table with input. Not efficient but it'll make the code nicer to test
		// directions.
		for (int row = 0; row < height; row++) {
			String data = input.get(row);
			for (int col = 0; col < width; col++) {
				final char character = data.charAt(col);
				if (character == '^') {
					startRow = row;
					startCol = col;
				}
				table[row][col] = character;
			}
		}

		result = countPath(table, startRow, startCol, width, height);

		return Integer.toString(result);
	}

	private int countPath(final char[][] table, final int startRow, final int startCol, final int width,
			final int height) {
		int currentRow = startRow;
		int currentCol = startCol;

		int rowIncrement = -1;
		int colIncrement = 0;

		Set<Position> visitedPositions = new HashSet<>();
		// Add the guard's original position as visited.
		visitedPositions.add(new Position(currentRow, currentCol));

		// While the guard's next position is in the grid.
		while (currentRow + rowIncrement >= 0 && currentRow + rowIncrement < height && currentCol + colIncrement >= 0
				&& currentCol + colIncrement < width) {

			// Check if the next position is an obstacle.
			int nextRow = currentRow + rowIncrement;
			int nextCol = currentCol + colIncrement;
			System.err.println(table[nextRow][nextCol]);
			if (table[nextRow][nextCol] == '#') {
				int previousRowIncrement = rowIncrement;
				int previousColIncrement = colIncrement;
				rowIncrement = previousRowIncrement != 0 ? 0 : (previousColIncrement == 1 ? 1 : -1);
				colIncrement = colIncrement != 0 ? 0 : (previousRowIncrement == 1 ? -1 : 1);
				System.out.println("Found obstacle. New direction: " + rowIncrement + " - " + colIncrement);
			} else {
				// If it's not an obstacle, move and add position.
				currentRow = currentRow + rowIncrement;
				currentCol = currentCol + colIncrement;
				visitedPositions.add(new Position(currentRow, currentCol));
			}
		}

		return visitedPositions.size();
	}

	private record Position(int row, int col) {
	};

}
