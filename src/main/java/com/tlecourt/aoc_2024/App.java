package com.tlecourt.aoc_2024;

import com.tlecourt.aoc_2024.base.Puzzle;
import com.tlecourt.aoc_2024.impl.Puzzle1_2;

public class App {
	public static void main(String[] args) {
		Puzzle puzzle = new Puzzle1_2();
		System.out.println("Solving puzzle for: " + puzzle);
		puzzle.execute();
	}
}
