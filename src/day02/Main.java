package day02;

import java.io.File;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class Rule {
		
		char targetChar;
		int min;
		int max;
		
		public Rule(char targetChar, int min, int max) {
			this.targetChar = targetChar;
			this.min = min;
			this.max = max;
		}
		
		public boolean isValid(String password) {
			char [] chars = password.toCharArray();
			
			int count = 0;
			for (int i=0; i<chars.length; i++) {
				if (chars[i] == targetChar)
					count++;
			}
			
			return (count >= min && count <= max);
		}
		
		/**
		 * Rule is of the form: "1-3 a"
		 */
		public static Rule parseRule(String text) {
			
			String [] parts = text.split("\\s+");
			String repText = parts[0];
			
			String [] repTextParts = repText.split("\\-");
			int min = Integer.parseInt(repTextParts[0]);
			int max = Integer.parseInt(repTextParts[1]);
			
			char targetChar = parts[1].charAt(0);
			
			return new Rule(targetChar, min, max);
		}
		
	}
	
	static class Rule2 {
		
		char targetChar;
		int [] positions;
		
		public Rule2(char targetChar, int [] positions) {
			this.targetChar = targetChar;
			this.positions = positions;
		}
		
		public boolean isValid(String password) {
			int count = 0;
			for (int i=0; i<positions.length; i++) {
				if (password.charAt(positions[i]) == targetChar)
					count++;
			}
			
			return (count == 1);
		}
		
		/**
		 * Rule is of the form: "1-3 a"
		 */
		public static Rule2 parseRule(String text) {
			
			String [] parts = text.split("\\s+");
			String repText = parts[0];
			
			String [] repTextParts = repText.split("\\-");
			int pos1 = Integer.parseInt(repTextParts[0]);
			int pos2 = Integer.parseInt(repTextParts[1]);
			
			char targetChar = parts[1].charAt(0);
			
			return new Rule2(targetChar, new int [] {pos1, pos2});
		}
		
	}
	
	public static void solvePart1() throws Exception {
		//lines are of the form: 1-3 a: abcde
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day02/input.txt"));

		int validCount = 0;
		
		for (String line: lines) {
			String [] parts = line.split(":");

			Rule rule = Rule.parseRule(parts[0]);
			String password = parts[1];

			if (rule.isValid(password))
				validCount++;
		}
		
		System.out.println(validCount + " (out of " + lines.size() + ") passwords are valid");
	}
	
	public static void solvePart2() throws Exception {
		//lines are of the form: 1-3 a: abcde
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day02/input.txt"));

		int validCount = 0;

		for (String line: lines) {
			String [] parts = line.split(":");

			Rule2 rule = Rule2.parseRule(parts[0]);
			String password = parts[1];

			if (rule.isValid(password))
				validCount++;
		}

		System.out.println(validCount + " (out of " + lines.size() + ") passwords are valid");
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
