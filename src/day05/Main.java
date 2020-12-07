package day05;

import java.io.File;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class Location {
		
		int row;
		int col;
		
		public Location(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public int getSeatId() {
			return row * 8 + col;
		}
		
		public static Location fromText(String text) {
			
			int rowLow = 0;
			int rowHigh = 127;
			int half = 64;
			
			for (int i=0; i<7; i++) {
				switch(text.charAt(i)) {
				case 'F':
					rowHigh -= half;
					break;
					
				case 'B':
					rowLow += half;
					break;
					
				default:
					throw new IllegalArgumentException("Expected 'F' or 'B'");
				}
				
				half /= 2;
			}
			
			int colLow = 0;
			int colHigh = 7;
			half = 4;
			
			for (int i=7; i<10; i++) {
				switch(text.charAt(i)) {
				case 'L':
					colHigh -= half;
					break;
					
				case 'R':
					colLow += half;
					break;
					
				default:
					throw new IllegalArgumentException("Expected 'L' or 'R'");
				}
				
				half /= 2;
			}
			
			if ((rowLow != rowHigh) || (colLow != colHigh))
				throw new IllegalStateException("Did not converge to unique row/col");
			
			return new Location(rowLow, colLow);
		}
		
		public String toString() {
			return new StringBuilder()
					.append("(row=").append(row)
					.append(",col=").append(col)
					.append(") id = ").append(getSeatId())
					.toString();
		}
		
	}
	
	public static void testPart1() throws Exception {
		System.out.println(Location.fromText("BFFFBBFRRR").toString()); //: row 70, column 7, seat ID 567.
		System.out.println(Location.fromText("FFFBBBFRRR").toString());	//: row 14, column 7, seat ID 119.
		System.out.println(Location.fromText("BBFFBBFRLL").toString()); //: row 102, column 4, seat ID 820.
	}
	
	public static void solvePart1() throws Exception {
		Location max = null;
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day05/input.txt"));
		for (String line : lines) {
			Location tmp = Location.fromText(line);
			if (max == null || tmp.getSeatId() > max.getSeatId())
				max = tmp;
		}
		
		System.out.println("Max Seat = " + max);
	}
	
	public static void solvePart2() throws Exception {
		
		boolean [] present = new boolean[823];
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day05/input.txt"));
		for (String line : lines) {
			Location tmp = Location.fromText(line);
			present[tmp.getSeatId()] = true;
		}
		
		for (int i=1; i<present.length-1; i++) {
			if (!present[i] & present[i-1] & present[i+1]) {
				System.out.println("Your Seat = " + i);
			}
		}
	}
	
	public static void main(String [] args) {
		try {
			testPart1();
			solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
