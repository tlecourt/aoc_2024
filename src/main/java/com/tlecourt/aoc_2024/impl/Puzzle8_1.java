package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle8_1 extends AbstractPuzzle {

	public Puzzle8_1() {
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
				distinctAntinodes.addAll(antennas.get(i).getAntinodes(antennas.get(j)));
			}
		}

		result = distinctAntinodes.stream().filter(antenna -> {
			// Keep antennas from within the mapped area.
			if (antenna.getRow() >= 0 && antenna.getRow() < height && antenna.getCol() >= 0
					&& antenna.getCol() < width) {
				System.out.println("Valid: " + antenna);
				return true;
			}
			return false;
//			return antenna.getRow() >= 0 && antenna.getRow() < height && antenna.getCol() >= 0
//					&& antenna.getCol() < width;
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

		public List<Antenna> getAntinodes(Antenna other) {
			if (this.frequency != other.getFrequency()) {
				return Collections.emptyList();
			}

			return Arrays.asList(
					new Antenna(this.row + this.row - other.getRow(), this.col + this.col - other.getCol(),
							this.frequency),
					new Antenna(other.getRow() + other.getRow() - this.row, other.getCol() + other.getCol() - this.col,
							this.frequency));
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
