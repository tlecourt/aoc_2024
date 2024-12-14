package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle5_2 extends AbstractPuzzle {

	public Puzzle5_2() {
		super(5, 2);
	}

	@Override
	protected String solve(List<String> input) {
		int result = 0;

		List<Rule> rules = new ArrayList<>();
		List<PrintOutput> records = new ArrayList<>();

		input.stream().forEach(s -> {
			if (s.contains("|")) {
				String[] vals = s.split("\\|");
				rules.add(new Rule(Integer.parseInt(vals[0]), Integer.parseInt(vals[1])));
			} else if (s.contains(",")) {
				records.add(new PrintOutput(s));
			}
		});

		result = records.stream().filter(output -> !output.isValid(rules)).map(output -> {
			output.fixItself(rules);
			return output.getEffectiveValue();
		}).reduce(0, Integer::sum);

		return Integer.toString(result);
	}

	private static record Rule(int before, int after) {
	};

	private static class PrintOutput {
		private final List<Integer> results;

		public PrintOutput(String str) {
			this.results = Arrays.asList(str.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
		}

		public boolean isValid(List<Rule> rules) {
			System.out.println("Testing results: " + results);
			List<Rule> validRules = rules.stream()
					.filter(rule -> results.contains(rule.before()) && results.contains(rule.after())).toList();
			System.out.println("Applying rules: " + validRules);

			for (int i = 0; i < results.size(); i++) {
				final int val = results.get(i);
				final int testIndex = i;
				Boolean isValid = validRules.stream().filter(rule -> rule.before() == val || rule.after() == val)
						.map(rule -> {
							if (rule.before() == val) {
								return results.indexOf(rule.after()) > testIndex;
							} else {
								return results.indexOf(rule.before()) < testIndex;
							}
						}).reduce(true, Boolean::logicalAnd);
				if (!isValid) {
					return false;
				}
			}
			System.out.println("Considered valid: " + results);
			return true;
		}

		public void fixItself(List<Rule> rules) {
			System.err.println("Original results: " + this.results);
			List<Rule> validRules = rules.stream()
					.filter(rule -> results.contains(rule.before()) && results.contains(rule.after())).toList();

			boolean doContinue = true;
			do {
				final Set<Rule> violatedRules = new HashSet<>();
				results.stream().forEach(val -> {
					int idx = results.indexOf(val);
					violatedRules.addAll(validRules.stream().filter(rule -> rule.before() == val || rule.after() == val)
							.filter(rule -> {
								if (rule.before() == val) {
									return results.indexOf(rule.after()) < idx;
								} else {
									return results.indexOf(rule.before()) > idx;
								}
							}).toList());
				});

				for (Rule violation : violatedRules) {
					this.results.remove(this.results.indexOf(violation.before()));
					this.results.add(this.results.indexOf(violation.after()), violation.before());
				}

				doContinue = !violatedRules.isEmpty();
				if (doContinue) {
					System.out.println("Testing results: " + results);
					System.out.println("Violated rules: " + violatedRules);
				}
			} while (doContinue);

			System.out.println("Fixed Version: " + this.results);
		}

		public int getEffectiveValue() {
			return results.get(results.size() / 2);
		}
	}

}
