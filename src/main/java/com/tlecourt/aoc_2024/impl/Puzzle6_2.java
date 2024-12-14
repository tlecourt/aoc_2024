package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle6_2 extends AbstractPuzzle {

	public Puzzle6_2() {
		super(6, 1);
	}

	@Override
	protected String solve(List<String> input) {
		long result = 0;

		final int width = input.get(0).length();
		final int height = input.size();

		char[][] table = new char[width][height];

		int startR = 0;
		int startC = 0;
		// fill table with input. Not efficient but it'll make the code nicer to test
		// directions.
		for (int row = 0; row < height; row++) {
			String data = input.get(row);
			for (int col = 0; col < width; col++) {
				final char character = data.charAt(col);
				if (character == '^') {
					startR = row;
					startC = col;
				}
				table[row][col] = character;
			}
		}

		final int startRow = startR;
		final int startCol = startC;

		Set<Position> visitedPath = buildVisitedPath(table, startRow, startCol, width, height);

		// Build the set of available positions to place an obstacle. It's the visited
		// places, except for the starting position.
		visitedPath.remove(new Position(startRow, startCol));

		final AtomicInteger atomicRes = new AtomicInteger(0);
		List<CompletableFuture<Boolean>> pendingTests = new ArrayList<>();
		try (ExecutorService es = Executors.newFixedThreadPool(10, Thread.ofVirtual().factory())) {
			for (Position pos : visitedPath) {
				final CompletableFuture<Boolean> pendingTest = CompletableFuture
						.supplyAsync(() -> isValidBlockingPosition(table, startRow, startCol, width, height, pos), es);
				pendingTest.thenAccept(val -> {
					if (val.booleanValue()) {
						atomicRes.incrementAndGet();
					}
				});
				pendingTests
						.add(pendingTest);
			}
		}

		CompletableFuture.allOf(pendingTests.toArray(new CompletableFuture[pendingTests.size()])).join();

		return Integer.toString(atomicRes.get());

//		result = visitedPath.parallelStream()
//				.filter(position -> isValidBlockingPosition(table, startRow, startCol, width, height, position))
//				.count();
//
//		return Long.toString(result);
	}

	private Set<Position> buildVisitedPath(final char[][] table, final int startRow, final int startCol,
			final int width,
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
//			System.err.println(table[nextRow][nextCol]);
			if (table[nextRow][nextCol] == '#') {
				int previousRowIncrement = rowIncrement;
				int previousColIncrement = colIncrement;
				rowIncrement = previousRowIncrement != 0 ? 0 : (previousColIncrement == 1 ? 1 : -1);
				colIncrement = colIncrement != 0 ? 0 : (previousRowIncrement == 1 ? -1 : 1);
//				System.out.println("Found obstacle. New direction: " + rowIncrement + " - " + colIncrement);
			} else {
				// If it's not an obstacle, move and add position.
				currentRow = currentRow + rowIncrement;
				currentCol = currentCol + colIncrement;
				visitedPositions.add(new Position(currentRow, currentCol));
			}
		}

		return visitedPositions;
	}

	private boolean isValidBlockingPosition(final char[][] table, final int startRow, final int startCol,
			final int width,
			final int height, Position blockingPosition) {
		int currentRow = startRow;
		int currentCol = startCol;

		int rowIncrement = -1;
		int colIncrement = 0;

		List<Position> guardPath = new LinkedList<>();
		// Add starting position
		guardPath.add(new Position(startRow, startCol));

		// While the guard's next position is in the grid.
		while (currentRow + rowIncrement >= 0 && currentRow + rowIncrement < height && currentCol + colIncrement >= 0
				&& currentCol + colIncrement < width) {
			// Check if the next position is an obstacle.
			int nextRow = currentRow + rowIncrement;
			int nextCol = currentCol + colIncrement;
//			System.err.println(table[nextRow][nextCol]);
			if (table[nextRow][nextCol] == '#'
					// Check for the block we've positioned as well as '#'
					|| nextRow == blockingPosition.row() && nextCol == blockingPosition.col) {
				int previousRowIncrement = rowIncrement;
				int previousColIncrement = colIncrement;
				rowIncrement = previousRowIncrement != 0 ? 0 : (previousColIncrement == 1 ? 1 : -1);
				colIncrement = colIncrement != 0 ? 0 : (previousRowIncrement == 1 ? -1 : 1);
//				System.out.println("Found obstacle. New direction: " + rowIncrement + " - " + colIncrement);
			} else {
				// If it's not an obstacle, move and add position.
				currentRow = currentRow + rowIncrement;
				currentCol = currentCol + colIncrement;
				guardPath.add(new Position(currentRow, currentCol));

				// After a movement, check if the last 2 positions have already been found
				// previously in the same order. So we need at least 4 positions in theory.
				if (guardPath.size() >= 4) {
					Position penultimatePos = guardPath.get(guardPath.size() - 2);
					Position latestPos = guardPath.get(guardPath.size() - 1);

					for (int i = 0; i < guardPath.size() - 3; i++) {
						if (guardPath.get(i).equals(penultimatePos) && guardPath.get(i + 1).equals(latestPos)) {
							System.err.println("Done testing position - Success");
							return true;
						}
					}
				}
			}
		}

		System.err.println("Done testing position - Failure");
		// If we exit the loop, it means the guard left the grid, and the block is not
		// in a valid position.
		return false;
	}

	private record Position(int row, int col) {
	};

}
