package day01;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.FileUtil;

public class Main {

	public static int [] readInput() throws IOException {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day01/input.txt"));
		int [] values = new int[lines.size()];
		for (int i=0; i<values.length; i++) {
			values[i] = Integer.parseInt(lines.get(i));
		}
		return values;
	}
	
	public static void solvePart1() throws Exception {
		int [] values = readInput();
		
		for (int i=0; i<values.length; i++) {
			for (int j=i+1; j<values.length; j++) {
				if (values[i] + values[j] == 2020) {
					int product = values[i] * values[j];
					System.out.println(values[i] + " * " + values[j] + " = " + product);
				}
			}
		}
	}
	
	public static void solvePart2() throws Exception {
		int [] values = readInput();
		
		for (int i=0; i<values.length; i++) {
			for (int j=i+1; j<values.length; j++) {
				for (int k=j+1; k<values.length; k++) {
					
					int sum = values[i] + values[j] + values[k];
					if (sum == 2020) {
						int product = values[i] * values[j] * values[k];;
						System.out.println(values[i] + " * " + values[j] + " * " + values[k] + " = " + product);
					}
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
