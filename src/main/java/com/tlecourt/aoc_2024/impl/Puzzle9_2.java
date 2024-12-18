package com.tlecourt.aoc_2024.impl;

import java.util.LinkedList;
import java.util.List;

import com.tlecourt.aoc_2024.base.AbstractPuzzle;

public class Puzzle9_2 extends AbstractPuzzle {

	public Puzzle9_2() {
		super(9, 1);
	}

	@Override
	public String solve(List<String> lines) {
		long result = 0;

		String input = lines.get(0);
		List<BlockGroup> blocks = new LinkedList<>();

		long fileId = 0;
		for (int i = 0; i < input.length(); i++) {
			final int groupSize = Integer.parseInt(Character.toString(input.charAt(i)));
			if (i % 2 == 0) {
				blocks.add(new FileBlockGroup(fileId++, groupSize));
			} else {
				// only add empty block if its size is greater than 0.
				if (groupSize > 0) {
					blocks.add(new EmptyBlockGroup(groupSize));
				}
			}
		}

		List<BlockGroup> processedWithoutEmpties = new LinkedList<>();

		List<FileBlockGroup> fileBlocks = blocks.stream().filter(FileBlockGroup.class::isInstance)
				.map(FileBlockGroup.class::cast).toList().reversed();
		System.out.println("File blocks: " + fileBlocks.size());
		int processed = 0;
		for (FileBlockGroup fileBlock: fileBlocks) {
			// Cleanup until first empty block;
			for (int i = 0; i < blocks.size(); i++) {
				if (blocks.get(i) instanceof EmptyBlockGroup && blocks.get(i).getSize() > 0) {
					processedWithoutEmpties.addAll(blocks.subList(0, i));
					blocks = blocks.subList(i, blocks.size());
					break;
				}
			}

//			printMemory(0, processedWithoutEmpties, blocks);
			int fileBlockIdx = blocks.indexOf(fileBlock);
			for (int i = 0; i < blocks.size(); i++) {
				final BlockGroup blockGroup = blocks.get(i);
				if (blockGroup instanceof EmptyBlockGroup && blockGroup.getSize() >= fileBlock.getSize() && i < fileBlockIdx) {
					blocks.remove(fileBlock);
					// Moved block leaves empty spaces
					// No need to merge the empty blocks because we're not going back to the
					// right-most blocks for other iterations.
					EmptyBlockGroup formerFileSpace = new EmptyBlockGroup(fileBlock.getSize());
					// Index moved because we inserted the file earlier in the list.
					blocks.add(fileBlockIdx, formerFileSpace);
					if (blockGroup.getSize() - fileBlock.getSize() == 0) {
						blocks.remove(i);
					} else {
						((EmptyBlockGroup) blockGroup).setSize(blockGroup.getSize() - fileBlock.getSize());
					}
					blocks.add(i, fileBlock);
					break;
				} else if (i >= fileBlockIdx) {
					break;
				}
			}
			System.out.println("Processed: " + (++processed));
		}

		result = printMemory(result, processedWithoutEmpties, blocks);

		return Long.toString(result);
	}

	private long printMemory(long result, List<BlockGroup> processedWithoutEmpties, List<BlockGroup> blocks) {
		int position = 0;
		for (BlockGroup block : processedWithoutEmpties) {
//			for (int i = 0; i < block.getSize(); i++) {
//				if (block instanceof EmptyBlockGroup) {
//					System.err.print('.');
//				} else {
//					System.err.print(((FileBlockGroup) block).getFileId());
//				}
//			}
			result += block.calculateChecksum(position);
			position += block.getSize();
		}
		for (BlockGroup block : blocks) {
//			for (int i = 0; i < block.getSize(); i++) {
//				if (block instanceof EmptyBlockGroup) {
//					System.err.print('.');
//				} else {
//					System.err.print(((FileBlockGroup) block).getFileId());
//				}
//			}
			result += block.calculateChecksum(position);
			position += block.getSize();
		}
//		System.err.println();
		return result;
	}

	private static abstract class BlockGroup {
		protected int size;

		protected BlockGroup(int size) {
			this.size = size;
		}

		public int getSize() {
			return this.size;
		}

		public abstract long calculateChecksum(int position);
	}

	private static class FileBlockGroup extends BlockGroup {
		private final long fileId;

		public FileBlockGroup(long fileId, int size) {
			super(size);
			this.fileId = fileId;
		}

		public long getFileId() {
			return this.fileId;
		}

		@Override
		public long calculateChecksum(int position) {
			long result = 0;
			for (int i = 0; i < this.size; i++) {
				result += (position + i) * fileId;
			}
			return result;
		}
	}

	private static class EmptyBlockGroup extends BlockGroup {
		public EmptyBlockGroup(int size) {
			super(size);
		}

		public void setSize(int size) {
			this.size = size;
		}

		@Override
		public long calculateChecksum(int position) {
			return 0;
		}
	}

}
