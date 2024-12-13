package com.tlecourt.aoc_2024.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle3_2 extends AbstractPuzzle {

	public Puzzle3_2() {
		super(3, 2);
	}

	@Override
	protected String solve(List<String> input) {
		final Pattern multiplicationPattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
		final Pattern doDontPattern = Pattern.compile("do(n't)?\\(\\)");

		// Enable multiplications at first.
		boolean currentEnabled = true;
		boolean nextEnabled = true;

		long result = 0;

		for (String line : input) {
			// Keep track of which index is the next change in enabling.
			int nextDoFlagIndex = 0;
			int lastMultiMatchIndex = 0;
			Matcher multiMatcher = multiplicationPattern.matcher(line);
			Matcher doDontMatcher = doDontPattern.matcher(line);

			// Loop through the loop until we have multiplication matches.
			while (multiMatcher.find(lastMultiMatchIndex)) {
				lastMultiMatchIndex = multiMatcher.end();
				// Loop through do/don't matches until we reach don't find one or reach one
				// after the last multimatch index.
				while (nextDoFlagIndex <= lastMultiMatchIndex && doDontMatcher.find(nextDoFlagIndex)) {
					currentEnabled = nextEnabled;
					nextEnabled = doDontMatcher.group().equals("do()");
					nextDoFlagIndex = doDontMatcher.end();
				}

				Boolean appliedEnabled = null;
				appliedEnabled = nextDoFlagIndex <= lastMultiMatchIndex ? nextEnabled : currentEnabled;
				if (appliedEnabled) {
					result += Long.parseLong(multiMatcher.group(1)) * Long.parseLong(multiMatcher.group(2));
				}
			}
		}

		return Long.toString(result);
	}

}
