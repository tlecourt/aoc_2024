package com.tlecourt.aoc_2024.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle7_1 extends AbstractPuzzle {

	public Puzzle7_1() {
		super(7, 1);
	}

	@Override
	protected String solve(List<String> input) {
		long result = 0;

		result = input.parallelStream().map(Equation::new).filter(Equation::testEquation).map(Equation::getResult)
				.reduce(0L,
				Long::sum);

		return Long.toString(result);
	}

	private static class Equation {
		private final long result;
		private final List<Long> factors = new LinkedList<>();

		public long getResult() {
			return this.result;
		}

		public Equation(final String line) {
			final int resultSeparator = line.indexOf(": ");
			this.result = Long.parseLong(line.substring(0, resultSeparator));
			for (String factorStr : line.substring(resultSeparator + 2).split(" ")) {
				factors.add(Long.valueOf(factorStr));
			}
		}

		public boolean testEquation() {
			List<List<BiFunction<Long, Long, Long>>> operatorsList = new LinkedList<List<BiFunction<Long, Long, Long>>>();
			for (int i = 0; i < factors.size() - 1; i++) {
				if (i == 0) {
					List<BiFunction<Long, Long, Long>> sumStartOperators = new LinkedList<>();
					sumStartOperators.add(Long::sum);
					List<BiFunction<Long, Long, Long>> mulStartOperators = new LinkedList<>();
					mulStartOperators.add(Math::multiplyExact);
					operatorsList.add(sumStartOperators);
					operatorsList.add(mulStartOperators);
				} else {
					List<List<BiFunction<Long, Long, Long>>> newOperationLists = new LinkedList<List<BiFunction<Long, Long, Long>>>();
					for (List<BiFunction<Long, Long, Long>> operators : operatorsList) {
						// Duplicate list because one needs the sum, one needs the
						List<BiFunction<Long, Long, Long>> duplicatedOperators = new LinkedList<>(operators);
						duplicatedOperators.add(Math::multiplyExact);
						newOperationLists.add(duplicatedOperators);
						operators.add(Long::sum);
					}
					// Then add all the created items
					operatorsList.addAll(newOperationLists);
				}
			}
			// Once all operators list are created go through each, and stop once one gets
			// to the correct result.
			for (List<BiFunction<Long, Long, Long>> operators : operatorsList) {
				long aggregate = factors.get(0);
				boolean isDone = false;
				for (int i = 0; i < operators.size(); i++) {
					aggregate = operators.get(i).apply(aggregate, factors.get(i + 1));

					// Stop early if result is too high.
					if (aggregate > this.result) {
						break;
					}
					if (i == operators.size() - 1) {
						isDone = true;
					}
				}

				// If we used all operators and the result is correct, then the equation is
				// valid for at least one set of operators.
				if (isDone && aggregate == this.result) {
					return true;
				}
			}

			// If no list of operators made it to the final result, then the equation is
			// invalid.
			return false;
		}
	}
}
