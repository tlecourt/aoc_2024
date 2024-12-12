package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle2_2 extends AbstractPuzzle {

	public Puzzle2_2() {
		super(2, 1);
	}

	@Override
	protected String solve(List<String> input) {
		return Long.toString(input.stream().map(Report::new).filter(Report::isSafe).count());
	}

	private static class Report {
		private List<Integer> levels;
		private boolean originalLevels = true;

		public Report(String unparsedLevels) {
			this.levels = Arrays.asList(unparsedLevels.split(" ")).stream().map(Integer::parseInt)
					.collect(Collectors.toList());
			this.originalLevels = true;
		}

		private Report(List<Integer> original) {
			this.levels = new ArrayList<>(original);
		}

		public void removeLevel(int index) {
			this.levels.remove(index);
			this.originalLevels = false;
		}

		public boolean isSafe() {
			Boolean previousAscending = null;
			for (int i = 0; i < levels.size() - 1; i++) {
				Integer first = levels.get(i);
				Integer second = levels.get(i + 1);

				Integer difference = second - first;
				if (difference == 0) {
					if (this.originalLevels) {
						boolean ret = isOneDampenedReportSafe(i);
						System.err.println(this + (ret ? "- Safe" : " - Unsafe"));
						return ret;
					}
					System.err.println(this + "- Unsafe");
					return false;
				} else if (Math.abs(difference) > 3) {
					if (this.originalLevels) {
						boolean ret = isOneDampenedReportSafe(i);
						System.err.println(this + (ret ? "- Safe" : " - Unsafe"));
						return ret;
					}
					System.err.println(this + "- Unsafe");
					return false;
				} else if (previousAscending != null && previousAscending.booleanValue() && difference < 0
						|| previousAscending != null && !previousAscending.booleanValue() && difference > 0) {
					if (this.originalLevels) {
						boolean ret = isOneDampenedReportSafe(i);
						System.err.println(this + (ret ? "- Safe" : " - Unsafe"));
						return ret;
					}
					System.err.println(this + "- Unsafe");
					return false;
				}
				previousAscending = difference < 0 ? Boolean.FALSE : Boolean.TRUE;
			}

			System.err.println(this + "- Safe");
			return true;
		}

		private boolean isOneDampenedReportSafe(int firstReportIndex) {
			// There might be a previous report which made it unsafe if the first result is
			// the wrong one. Try removing it if the first report index is 1.
			if (firstReportIndex == 1) {
				Report veryFirstReportRemoved = new Report(this.levels);
				veryFirstReportRemoved.removeLevel(0);
				if (veryFirstReportRemoved.isSafe()) {
					return true;
				}
			}
			Report firstUpdate = new Report(this.levels);
			firstUpdate.removeLevel(firstReportIndex);
			Report secondUpdate = new Report(this.levels);
			secondUpdate.removeLevel(firstReportIndex + 1);
			return firstUpdate.isSafe() || secondUpdate.isSafe();
		}

		public String toString() {
			return (this.originalLevels ? "Original - " : "Updated - ") + this.levels;
		}
	}

}
