package com.tlecourt.aoc_2024.base;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class AbstractPuzzle implements Puzzle {

	private int day;
	private int number;

	protected AbstractPuzzle(final int day, final int number) {
		this.day = day;
		this.number = number;
	}

	@Override
	public String execute() {
		final String filename = String.format("/com/tlecourt/aoc_2024/input_%d", this.day);
		try {
			List<String> input = Files.readAllLines(Path.of(this.getClass().getResource(filename).toURI()));
			return this.solve(input);
		} catch (IOException | URISyntaxException e) {
			System.err.println("Failed to open input file: " + e.getMessage());
		}
		return null;
	}

	protected abstract String solve(List<String> input);

	public String toString() {
		return String.format("Day %d - Number %d", this.day, this.number);
	}

}
