package com.tlecourt.aoc_2024.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle1_1 extends AbstractPuzzle {

	public Puzzle1_1() {
		super(1, 1);
	}

	@Override
	public void solve(List<String> lines) {
		List<Integer> leftIDs = new LinkedList<>();
		List<Integer> rightIDs = new LinkedList<>();

		final Pattern linePattern = Pattern.compile("([0-9]+)\\s+([0-9]+)");
		for (String line : lines) {
			Matcher matcher = linePattern.matcher(line);
			if (!matcher.find()) {
				System.err.println("Failed to parse line");
				continue;
			}

			leftIDs.add(Integer.parseInt(matcher.group(1)));
			rightIDs.add(Integer.parseInt(matcher.group(2)));
		}

		leftIDs.sort(Integer::compareTo);
		rightIDs.sort(Integer::compareTo);

		int distance = 0;
		for (int i = 0; i < leftIDs.size(); i++) {
			distance += Math.abs(rightIDs.get(i) - leftIDs.get(i));
		}

		System.out.println("Result: " + distance);
	}

}
