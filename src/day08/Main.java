package day08;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static class Computer {
		
		long accumulator = 0;
		
		List<String> program;
		int nextCommand = 0;
		
		public void setProgram(List<String> program) {
			this.program = program;
			this.nextCommand = 0;
		}
		
		public void acc(int value) {
			this.accumulator += value;
		}
		
		public boolean step() {
			if (nextCommand < 0 || nextCommand >= program.size())
				return false;
			
			String commandText = program.get(nextCommand);
			int jumpTo = nextCommand + 1;
			
			String [] parts = commandText.split("\\s+");
			String action = parts[0];
			int value = Integer.parseInt(parts[1]);
			
			if (action.equals("acc")) {
				acc(value);
			} else if (action.equals("jmp")) {
				jumpTo = nextCommand + value;
			} else if (action.equals("nop")) {
				//do nothing
			} else {
				throw new IllegalStateException("Invalid input: " + commandText);
			}
			
			this.nextCommand = jumpTo;
			return true;
		}
		
	}
	
	public static void solvePart1() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day08/input.txt"));
		Computer computer = new Computer();
		computer.setProgram(program);
		
		Set<Integer> commands = new HashSet<>();
		while (commands.add(computer.nextCommand)) {
			computer.step();
		}
		
		System.out.println("Accumulator = " + computer.accumulator);
	}
	
	public static boolean isInfiniteLoop(List<String> program) {
		Computer computer = new Computer();
		computer.setProgram(program);
		
		Set<Integer> commands = new HashSet<>();
		while (commands.add(computer.nextCommand)) {
			if (!computer.step()) {
				System.out.println("Accumulator = " + computer.accumulator);
				return false;
			}
		}
		
		return true;
	}
	
	public static void solvePart2() throws Exception {
		List<String> program = FileUtil.readLinesFromFile(new File("files/day08/input.txt"));
		
		for (int i=0; i<program.size(); i++) {
			String commandText = program.get(i);
			String [] parts = commandText.split("\\s+", 2);
			String action = parts[0];
			
			String newCommand = null;
			if (action.equals("nop")) {
				newCommand = "jmp " + parts[1];
			} else if (action.equals("jmp")) {
				newCommand = "nop " + parts[1];
			}
			
			if (newCommand != null) {
				List<String> newProgram = new ArrayList<>(program);
				newProgram.set(i, newCommand);
				if (!isInfiniteLoop(newProgram)) {
					System.out.println("Solution found by changing line: " + i);
				}
			}
		}
		
	}
	
	public static void main(String [] args) {
		try {
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
