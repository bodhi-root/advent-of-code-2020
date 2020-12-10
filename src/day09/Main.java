package day09;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.FileUtil;

public class Main {
	
	public static long [] readInput() throws IOException {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day09/input.txt"));
		long [] values = new long[lines.size()];
		for (int i=0; i<values.length; i++)
			values[i] = Long.parseLong(lines.get(i));
		return values;
	}
	
	public static int findIndexOfFirstInvalidNumber(long [] values, int window) {
		
		long sum = 0;
		
		for (int idxValue=window; idxValue<values.length; idxValue++) {
			
			boolean isValid = false;
			
			for (int i=idxValue-window; i<idxValue; i++) {
				for (int j=i+1; j<idxValue; j++) {
					sum = values[i] + values[j];
					if (sum == values[idxValue]) {
						isValid = true;
						
						//break out of loop:
						i = idxValue;
						j = idxValue;
					}
				}
			}
			
			if (!isValid)
				return idxValue;
			
		}
		
		return -1;
	}
	
	public static void solvePart1() throws Exception {
		long [] values = readInput();
		int i = findIndexOfFirstInvalidNumber(values, 25);
		System.out.println("x[" + i + "] = " + values[i] + " is invalid");
	}
	
	static void printStatsForRange(long [] values, int start, int end) {
		long min = values[start];
		long max = values[start];
		for (int i=start+1; i<=end; i++) {
			min = Math.min(min, values[i]);
			max = Math.max(max, values[i]);
		}
		
		System.out.println("Min = " + min);
		System.out.println("Max = " + max);
		System.out.println("Min + Max = " + (min+max));
	}
	
	public static void solvePart2() throws Exception {
		long [] values = readInput();
		
		long targetSum = 15690279;
		
		//examine contiguous ranges beginning at i and running through j
		for (int i=0; i<values.length; i++) {
			long sum = 0;
			
			for (int j=i; j<values.length; j++) {
				sum += values[j];
				if (sum == targetSum) {
					printStatsForRange(values, i, j);
					
					//break loop:
					j = values.length;
				} else if (sum > targetSum) {
					
					//break loop:
					j = values.length;
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
