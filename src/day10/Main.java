package day10;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import common.FileUtil;

public class Main {
	
	public static int [] readInput() throws IOException {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day10/input.txt"));
		int [] values = new int[lines.size()];
		for (int i=0; i<values.length; i++)
			values[i] = Integer.parseInt(lines.get(i));
		return values;
	}
	
	public static void solvePart1() throws Exception {
		int [] values = readInput();
		Arrays.sort(values);
		
		int oneGapCount = 0;
		int threeGapCount = 0;
		
		for (int i=1; i<values.length; i++) {
			int gap = values[i] - values[i-1];
			if (gap == 1)
				oneGapCount++;
			else if (gap == 3)
				threeGapCount++;
		}
		
		//add beginning and end:
		if (values[0] == 1)
			oneGapCount++;
		else if (values[0] == 3)
			threeGapCount++;
		
		threeGapCount++;	//end is always 3 higher
		
		System.out.println("Connectors: " + values.length);
		System.out.println("Your Device: " + (values[values.length-1] + 3));
		
		System.out.println("1-gap: " + oneGapCount);
		System.out.println("3-gap: " + threeGapCount);
		System.out.println("Product: " + (oneGapCount * threeGapCount));
	}
	
	public static int countTrue(boolean [] values) {
		int count = 0;
		for (int i=0; i<values.length; i++) {
			if (values[i])
				count++;
		}
		return count;
	}
	
	public static boolean isValid(int [] values) {
		for (int i=1; i<values.length; i++) {
			if ((values[i] - values[i-1]) > 3)
				return false;
		}
		return true;
	}
	
	/**
	 * Returns the number of valid chains that can be constructed from values[from] 
	 * to values[to] leaving the endpoints fixed and without violating the 3-gap
	 * max rule.
	 */
	public static long getPermutations(int [] values, int from, int to) {
		
		int innerLength = to - from - 1;
		if (innerLength == 0)
			return 1;
		
		long perms = 0;
		
		boolean [] include = new boolean[innerLength];
		boolean done = false;
		while (!done) {
			
			int len = countTrue(include) + 2;
			int [] x = new int[len];
			x[0] = values[from];
			x[len-1] = values[to];
			int nextIndex = 1;
			for (int i=0; i<include.length; i++) {
				if (include[i])
					x[nextIndex++] = values[from+i+1];
			}
			
			if (isValid(x))
				perms++;
			
			//increment include buffer (just like binary addition, but left-to-right)
			for (int i=0; i<include.length; i++) {
				if (include[i] == false) {
					include[i] = true;
					break;
				} else {
					include[i] = false;
					if (i == include.length - 1) {
						done = true;
						break;
					}
				}
			}
			
		}
				
		return perms;
	}
	
	public static void solvePart2() throws Exception {
		int [] values = readInput();
		Arrays.sort(values);
		/*
		for (int i=0; i<values.length; i++) {
			int gap = (i == 0) ? values[i] : values[i] - values[i-1];
			System.out.println(values[i] + "(" + gap + ")");
		}
		*/
		
		//add endpoints:
		int [] newValues = new int[values.length+2];
		newValues[0] = 0;
		System.arraycopy(values, 0, newValues, 1, values.length);
		newValues[newValues.length-1] = values[values.length-1] + 3;
		
		values = newValues;
		
		//calculate gaps:
		int [] gaps = new int[values.length-1];
		for (int i=1; i<values.length; i++)
			gaps[i-1] = values[i] - values[i-1];
			
		//neighbors with a gap of 3 must be fixed in place
		boolean [] fixed = new boolean[values.length];
		fixed[0] = true;	//first endpoint must be fixed
		for (int i=1; i<values.length; i++) {
			if (gaps[i-1] == 3) {
				fixed[i-1] = true;
				fixed[i] = true;
			}
		}
		//fixed[fixed.length-1] = true;	//last one is always fixed
		
		int fixedCount = 0;
		for (int i=0; i<fixed.length; i++) {
			if (fixed[i])
				fixedCount++;
		}
		System.out.println("Fixed = " + fixedCount + " (of " + fixed.length + ")");
		
		//print chain:
		printChain(values, fixed, 0, values.length-1);
		System.out.println();
		
		//calculate subchain permutations
		long product = 1;
		for (int i=0; i<values.length; i++) {
			if (fixed[i]) {
				for (int j=i+1; j<values.length; j++) {
					if (fixed[j]) {
						
						System.out.println("Subchain: ");
						printChain(values, fixed, i, j);
						
						long permutations = getPermutations(values, i, j);
						System.out.println("Permutations = " + permutations);
						System.out.println();
						
						product *= permutations;
						
						//break loop:
						j = values.length;
					}
				}
			}
		}
		
		System.out.println("Permutations: " + product);
	}
	
	public static void printChain(int [] values, boolean [] fixed, int from, int to) {
		StringBuilder line1 = new StringBuilder();
		StringBuilder line2 = new StringBuilder();
		for (int i=from; i<=to; i++) {
			if (i > from) {
				line1.append("-");
				line2.append(" ");
			}
			String txt = String.valueOf(values[i]);
			line1.append(txt);
			char line2char = (fixed[i]) ? '*' : ' ';
			for (int j=0; j<txt.length(); j++)
				line2.append(line2char);
		}
		System.out.println(line1.toString());
		System.out.println(line2.toString());
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
