package day14;

import java.io.File;
import java.util.List;

import common.FileUtil;

public class Main {
	
	public static void runPart1(List<String> commands) {
		Computer computer = new Computer();
		for (String command : commands)
			computer.run(command, true);
		
		System.out.println("TOTAL = " + computer.getRegisterSum());
	}
	
	public static void testPart1() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day14/test.txt"));
		runPart1(lines);
	}
	
	public static void solvePart1() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day14/input.txt"));
		runPart1(lines);
	}
	
	public static void runPart2(List<String> commands) {
		Computer2 computer = new Computer2();
		for (String command : commands)
			computer.run(command, true);
		
		System.out.println("TOTAL = " + computer.getRegisterSum());
	}
	
	public static void testPart2() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day14/test2.txt"));
		runPart2(lines);
	}
	
	public static void solvePart2() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day14/input.txt"));
		runPart2(lines);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
