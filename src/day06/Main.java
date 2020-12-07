package day06;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	
	static class InputReader {
		
		BufferedReader in;
		
		public InputReader(File file) throws IOException {
			this.in = new BufferedReader(new FileReader(file));
		}
		
		public void close() throws IOException {
			in.close();
		}
		
		public List<String> readNextGroup() throws IOException {
			List<String> lines = new ArrayList<>();
			
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().isEmpty())
					break;
				
				lines.add(line);
			}
			
			if (lines.isEmpty())
				return null;
			
			return lines;
		}
		
	}
	
	public static int countUniqueAnswers(List<String> lines) {
		Set<Character> set = new HashSet<>(26);
		for (String line: lines) {
			char [] chars = line.toCharArray();
			for (int i=0; i<chars.length; i++)
				set.add(chars[i]);
		}
		return set.size();
	}
	
	public static int countEveryoneAnsweredYes(List<String> lines) {
		Set<Character> combined = null;
		
		for (String line: lines) {
			Set<Character> individual = new HashSet<>(26);
			char [] chars = line.toCharArray();
			for (int i=0; i<chars.length; i++)
				individual.add(chars[i]);
			
			if (combined == null)
				combined = individual;
			else
				combined.retainAll(individual);
		}
		
		return combined.size();
	}
	
	public static void solvePart1and2() throws Exception {
		int groupCount = 0;
		int totalScore1 = 0;
		int totalScore2 = 0;
		
		InputReader in = new InputReader(new File("files/day06/input.txt"));
		try {
			List<String> lines;
			while ((lines = in.readNextGroup()) != null) {
				groupCount++;
				totalScore1 += countUniqueAnswers(lines);
				totalScore2 += countEveryoneAnsweredYes(lines);
			}
		}
		finally {
			in.close();
		}
		
		System.out.println("Groups = " + groupCount);
		System.out.println("Score (Part 1) = " + totalScore1);
		System.out.println("Score (Part 2) = " + totalScore2);
	}
	
	public static void main(String [] args) {
		try {
			solvePart1and2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
