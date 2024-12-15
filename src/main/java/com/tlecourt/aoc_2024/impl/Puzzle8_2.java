package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle8_2 extends AbstractPuzzle {

	public Puzzle8_2() {
		super(8, 1);
	}

	@Override
	protected String solve(List<String> input) {
		long result = 0;

		final int width = input.get(0).length();
		final int height = input.size();

		char[][] table = new char[width][height];

		List<Antenna> antennas = new ArrayList<>();

		// fill table with input. Not efficient but it'll make the code nicer to test
		// directions.
		for (int row = 0; row < height; row++) {
			String data = input.get(row);
			for (int col = 0; col < width; col++) {
				final char character = data.charAt(col);
				table[row][col] = character;
				if (character != '.') {
					antennas.add(new Antenna(row, col, character));
				}
			}
		}

		Set<Antenna> distinctAntinodes = new HashSet<>();
		for (int i = 0; i < antennas.size() - 1; i++) {
			for (int j = i + 1; j < antennas.size(); j++) {
				distinctAntinodes.addAll(antennas.get(i).getAntinodes(antennas.get(j), width, height));
			}
		}

		result = distinctAntinodes.stream().filter(antenna -> {
			// Keep antennas from within the mapped area.
			return antenna.getRow() >= 0 && antenna.getRow() < height && antenna.getCol() >= 0
					&& antenna.getCol() < width;
		}).count();

		return Long.toString(result);
	}

	private static class Antenna {
		private final int row;
		private final int col;
		private final char frequency;

		public Antenna(final int row, final int col, final char frequency) {
			this.row = row;
			this.col = col;
			this.frequency = frequency;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public char getFrequency() {
			return frequency;
		}

		public List<Antenna> getAntinodes(Antenna other, final int width, final int height) {
			if (this.frequency != other.getFrequency()) {
				return Collections.emptyList();
			}

			List<Antenna> antinodes = new ArrayList<>();

			// Build antinodes on one side
			int offsetRow = this.row - other.getRow();
			int offsetCol = this.col - other.getCol();

			int newRow = this.row;
			int newCol = this.col;
			while (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
				antinodes.add(new Antenna(newRow, newCol, this.frequency));
				newRow += offsetRow;
				newCol += offsetCol;
			}

			newRow = other.row;
			newCol = other.col;
			// Reset values so we go the other way.
			while (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width) {
				antinodes.add(new Antenna(newRow, newCol, this.frequency));
				newRow -= offsetRow;
				newCol -= offsetCol;
			}

			return antinodes;
		}

		public String toString() {
			return String.format("Row %d - Col %d - Frequency %s", this.row, this.col, this.frequency);
		}

		@Override
		public int hashCode() {
			return Objects.hash(col, row);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Antenna other = (Antenna) obj;
			return col == other.col && row == other.row;
		}
	}

}
