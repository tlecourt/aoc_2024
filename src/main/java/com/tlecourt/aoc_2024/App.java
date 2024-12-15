package com.tlecourt.aoc_2024;

import com.tlecourt.aoc_2024.base.Puzzle;
import com.tlecourt.aoc_2024.impl.Puzzle9_1;

public class App {
	public static void main(String[] args) {
		Puzzle puzzle = new Puzzle9_1();
		System.out.println("Solving puzzle for: " + puzzle);
		String result = puzzle.execute();
		System.out.println(result);
	}
}
