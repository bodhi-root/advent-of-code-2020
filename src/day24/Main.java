package day24;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class Main {
	
	/**
	 * Location representing the center of a hexagon. We imagine the coordinates
	 * such that stepping east or west will move x +2 or -2.  Stepping northeast
	 * or northwest will move y + 1 and x +1 or -1.  This works out so that stepping
	 * "ne" and then "se" is equivalent to one step "e".
	 */
	static class Location {
		
		int x;
		int y;
		
		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Object o) {
			if (o instanceof Location) {
				Location that = (Location)o;
				return (this.x == that.x && this.y == that.y);
			}
			return false;
		}
		public int hashCode() {
			return x ^ 7 + y;
		}
		
		public Location step(String dir) {
			if (dir.equals("e"))
				return new Location(x+2, y);
			if (dir.equals("w"))
				return new Location(x-2, y);
			if (dir.equals("ne"))
				return new Location(x+1, y+1);
			if (dir.equals("nw"))
				return new Location(x-1, y+1);
			if (dir.equals("se"))
				return new Location(x+1, y-1);
			if (dir.equals("sw"))
				return new Location(x-1, y-1);
			
			throw new IllegalArgumentException("Invalid direction: " + dir);
		}
		
	}
	
	static class State {
		
		boolean isBlack;
		
		public State(boolean isBlack) {
			this.isBlack = isBlack;
		}
		public State() {
			this(false);
		}
		
		public void flip() {
			this.isBlack = !this.isBlack;
		}
		
	}
	
	public static Location textToLocation(String text) {
		int x = 0;
		int y = 0;
		
		char [] chars = text.toCharArray();
		char ch, ch2;
		int nextIndex = 0;
		while (nextIndex < chars.length) {
			ch = chars[nextIndex++];
			if (ch == 'e')
				x += 2;
			else if (ch == 'w')
				x -= 2;
			else if (ch == 'n') {
				y++;
				ch2 = chars[nextIndex++];
				
				if (ch2 == 'e') {
					x++;
				} else if (ch2 == 'w') {
					x--;
				} else {
					throw new IllegalArgumentException("Unexpected char: " + ch2);
				}
				
			} else if (ch == 's') {
				y--;
				ch2 = chars[nextIndex++];
				
				if (ch2 == 'e') {
					x++;
				} else if (ch2 == 'w') {
					x--;
				} else {
					throw new IllegalArgumentException("Unexpected char: " + ch2);
				}
				
			} else {
				throw new IllegalArgumentException("Invalid char: " + ch);
			}
		}
		
		return new Location(x, y);
	}
	
	static class Floor {
		
		Map<Location, State> map = new HashMap<>();
		
		public State get(Location loc) {
			State state = map.get(loc);
			if (state == null) {
				state = new State();
				map.put(loc, state);
			}
			return state;
		}
		
		public int countBlackTiles() {
			int countBlack = 0;
			for (State state : map.values()) {
				if (state.isBlack)
					countBlack++;
			}
			return countBlack;
		}
		
		public int countBlackTilesAround(Location loc) {
			int count = 0;
			for (String dir : DIRS) {
				if (get(loc.step(dir)).isBlack)
					count++;
			}
			return count;
		}
		
		static String [] DIRS = new String[] {"e","w","nw","ne","sw","se"};
		
		public Floor step() {
			Floor newFloor = new Floor();
			
			//get all locations we've seen
			List<Location> locations = new ArrayList<>(map.keySet());
			
			//call get() on all adjacent tiles to make sure they're included in our map
			for (Location loc : locations) {
				for (String dir : DIRS)
					get(loc.step(dir));
			}
			
			//here's the tiles we need to examine:
			locations = new ArrayList<>(map.keySet());
			for (Location loc : locations) {
				
				State state = get(loc);
				State newState = state;
				
				int count = countBlackTilesAround(loc);
				if (state.isBlack && (count == 0 || count > 2))
					newState = new State(false);
				else if (!state.isBlack && count == 2)
					newState = new State(true);
				
				//set black tiles (no need to set white)
				if (newState.isBlack)
					newFloor.get(loc).isBlack = true;
			}
		
			return newFloor;
		}
		
	}
	
	public static void doPart1(File file) throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(file);
		
		Floor floor = new Floor();
		for (String line : lines) {
			Location loc = textToLocation(line);
			floor.get(loc).flip();
		}
		
		System.out.println(floor.countBlackTiles() + " black tiles");
	}
	
	public static void testPart1() throws Exception {
		doPart1(new File("files/day24/test.txt"));
	}
	
	public static void solvePart1() throws Exception {
		doPart1(new File("files/day24/input.txt"));
	}
	
	public static void doPart2(File file) throws Exception {
		//initialize floor (same as part 1):
		List<String> lines = FileUtil.readLinesFromFile(file);
		
		Floor floor = new Floor();
		for (String line : lines) {
			Location loc = textToLocation(line);
			floor.get(loc).flip();
		}
		
		for (int i=0; i<100; i++)
			floor = floor.step();
		
		System.out.println(floor.countBlackTiles() + " black tiles");
	}
	
	public static void testPart2() throws Exception {
		doPart2(new File("files/day24/test.txt"));
	}
	
	public static void solvePart2() throws Exception {
		doPart2(new File("files/day24/input.txt"));
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
