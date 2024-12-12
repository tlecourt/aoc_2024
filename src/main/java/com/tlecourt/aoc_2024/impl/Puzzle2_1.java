package com.tlecourt.aoc_2024.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle2_1 extends AbstractPuzzle {

	public Puzzle2_1() {
		super(2, 1);
	}

	@Override
	protected String solve(List<String> input) {
		return Long.toString(input.stream().map(Report::new).filter(Report::isSafe).count());
	}

	private static class Report {
		private final List<Integer> levels;
		private final boolean ascending;

		public Report(String unparsedLevels) {
			this.levels = Arrays.asList(unparsedLevels.split(" ")).stream().map(Integer::parseInt)
					.collect(Collectors.toList());
			this.ascending = levels.size() < 2 ? true : levels.get(0) < levels.get(1);
		}

		public boolean isSafe() {
			for (int i = 0; i < levels.size() - 1; i++) {
				Integer first = levels.get(i);
				Integer second = levels.get(i + 1);

				Integer difference = second - first;
				if (difference == 0) {
					return false;
				} else if (Math.abs(difference) > 3) {
					return false;
				} else if (this.ascending && difference < 0 || !this.ascending && difference > 0) {
					return false;
				}
			}

			return true;
		}
	}

}
