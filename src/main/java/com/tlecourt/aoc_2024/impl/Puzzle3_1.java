package com.tlecourt.aoc_2024.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle3_1 extends AbstractPuzzle {

	public Puzzle3_1() {
		super(3, 1);
	}

	@Override
	protected String solve(List<String> input) {
		final Pattern linePattern = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");

		long result = 0;

		for (String line : input) {
			int lastMatchIndex = 0;
			Matcher matcher = linePattern.matcher(line);
			while (matcher.find(lastMatchIndex)) {
				result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
				lastMatchIndex = matcher.end();
			}
		}

		return Long.toString(result);
	}

}
