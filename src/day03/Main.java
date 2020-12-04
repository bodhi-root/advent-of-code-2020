package day03;

import java.io.File;
import java.io.IOException;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static class Map {
		
		char [][] map;
		int height;
		int width;
		
		public Map(char [][] map) {
			this.map = map;
			this.height = map.length;
			this.width = map[0].length;
		}
		
		public static Map loadFromFile(File file) throws IOException {
			List<String> lines = FileUtil.readLinesFromFile(file);
			
			int height = lines.size();
			int width = lines.get(0).length();
			
			char [][] map = new char[height][width];
			for (int i=0; i<height; i++) {
				String line = lines.get(i);
				char [] chars = line.toCharArray();
				System.arraycopy(chars, 0, map[i], 0, width);
			}
			
			return new Map(map);
		}
		
		public int testSlope(int di, int dj) {
			int treeCount = 0;
			
			int i = 0;
			int j = 0;
			
			do {
				System.out.println(i + ", " + j + " => " + i + ", " + (j%width) + " = " + map[i][j%width]);
		    	if (map[i][j%width] == '#')
		    		treeCount++;
		    	
				i += di;
				j += dj;
			} while(i < this.height);
			
		    System.out.println(treeCount + " trees were encountered");
		    return treeCount;
		}
		
	}
	
	
	public static void solvePart1() throws Exception {
		Map map = Map.loadFromFile(new File("files/day03/input.txt"));
		map.testSlope(1, 3);
	}
	
	public static void solvePart2() throws Exception {
		Map map = Map.loadFromFile(new File("files/day03/input.txt"));
		
		long answer = 1L *	//ensure multiplication as Long to prevent overflow
				      map.testSlope(1, 1) *	//79
			 	      map.testSlope(1, 3) * //234
				      map.testSlope(1, 5) * //72
				      map.testSlope(1, 7) *	//91
				      map.testSlope(2, 1);	//48
		
		System.out.println("PRODUCT = " + answer);
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
