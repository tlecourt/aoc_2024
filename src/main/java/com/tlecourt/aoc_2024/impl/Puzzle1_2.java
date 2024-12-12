package com.tlecourt.aoc_2024.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle1_2 extends AbstractPuzzle {

	public Puzzle1_2() {
		super(1, 2);
	}

	@Override
	public void solve(List<String> lines) {
		List<Integer> leftIDs = new LinkedList<>();

		Map<Integer, Integer> occurrences = new HashMap<>();

		final Pattern linePattern = Pattern.compile("([0-9]+)\\s+([0-9]+)");
		for (String line : lines) {
			Matcher matcher = linePattern.matcher(line);
			if (!matcher.find()) {
				System.err.println("Failed to parse line");
				continue;
			}

			leftIDs.add(Integer.parseInt(matcher.group(1)));
			final int occurrence = Integer.parseInt(matcher.group(2));
			occurrences.compute(occurrence, (key, value) -> value == null ? 1 : ++value);
		}

		int similarityScore = 0;
		for (Integer locationId : leftIDs) {
			similarityScore += locationId * occurrences.getOrDefault(locationId, 0);
		}

		System.out.println("Result: " + similarityScore);
	}

}
