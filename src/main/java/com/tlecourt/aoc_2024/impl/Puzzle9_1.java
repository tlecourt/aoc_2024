package com.tlecourt.aoc_2024.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle9_1 extends AbstractPuzzle {

	public Puzzle9_1() {
		super(9, 1);
	}

	@Override
	public String solve(List<String> lines) {
		long result = 0;

		String input = lines.get(0);
		List<Integer> fileBlocks = new LinkedList<>();
		List<Integer> emptyBlocks = new LinkedList<>();

		for (int i = 0; i < input.length(); i++) {
			if (i % 2 == 0) {
				fileBlocks.add(Integer.parseInt(Character.toString(input.charAt(i))));
			} else {
				emptyBlocks.add(Integer.parseInt(Character.toString(input.charAt(i))));
			}
		}

		List<Integer> orderedMemory = new ArrayList<>();

		int blockPosition = 0;
		int processedLeftFileBlocks = 0;
		boolean isOnFile = true;
		while (!fileBlocks.isEmpty()) {
			if (isOnFile) {
				int remainingBlocks = fileBlocks.get(0);
				if (remainingBlocks > 0) {
					// add to checksum.
					result += blockPosition * processedLeftFileBlocks;
					orderedMemory.add(processedLeftFileBlocks);
					// Move to next position.
					blockPosition++;
					// Remove one block for the file.
					fileBlocks.set(0, --remainingBlocks);
				} else {
					// If no remaining block for the current file, increase number of processed
					// files.
					processedLeftFileBlocks++;
					fileBlocks.remove(0);
					// Then switch to filling empties mode.
					isOnFile = false;
				}
			} else {
				int remainingEmpties = emptyBlocks.get(0);
				if (remainingEmpties > 0) {
					// Get the last file block
					int remainingBlocksForLastFile = fileBlocks.get(fileBlocks.size() - 1);
					if (remainingBlocksForLastFile > 0) {
						int fileId = fileBlocks.size() - 1 + processedLeftFileBlocks;
						result += fileId * blockPosition;
						orderedMemory.add(fileId);
						blockPosition++;
						fileBlocks.set(fileBlocks.size() - 1, --remainingBlocksForLastFile);
						emptyBlocks.set(0, --remainingEmpties);
					} else {
						fileBlocks.remove(fileBlocks.size() - 1);
					}
				} else {
					emptyBlocks.remove(0);
					isOnFile = true;
				}
			}
		}
		System.out.println(orderedMemory);
		return Long.toString(result);
	}

}
