package day11;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static enum SeatState {EMPTY, OCCUPIED};
	
	static class Seat {
		
		SeatState state = SeatState.EMPTY;
		
	}
	
	static class World {
		
		Seat [][] seats;
		int height;
		int width;
		
		public World(Seat [][] seats) {
			this.seats = seats;
			this.height = seats.length;
			this.width = seats[0].length;
		}
		
		public void print(PrintStream out) {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<seats.length; i++) {
				s.setLength(0);
				for (int j=0; j<seats[i].length; j++) {
					if (seats[i][j] == null)
						s.append('.');
					else {
						if (seats[i][j].state == SeatState.OCCUPIED)
							s.append('#');
						else
							s.append('L');
					}
				}
				out.println(s.toString());
			}
		}
		
		public int getOccupiedSeatCountAround(int i, int j) {
			int count = 0;
			int iTmp, jTmp;
			for (int di=-1; di<=1; di++) {
				for (int dj=-1; dj<=1; dj++) {
					
					if (di == 0 && dj == 0)
						continue;
					
					iTmp = i + di;
					jTmp = j + dj;
					
					if (iTmp >= 0 && iTmp < height &&
						jTmp >= 0 && jTmp < width) {
						if (seats[iTmp][jTmp] != null &&
							seats[iTmp][jTmp].state == SeatState.OCCUPIED)
							count++;
					}
					
				}
			}
			return count;
		}
		
		public int step() {
			int changes = 0;
			
			//calculate new states:
			SeatState [][] newStates = new SeatState[height][width];
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (seats[i][j] != null) {
						
						int count = getOccupiedSeatCountAround(i, j);
						if (seats[i][j].state == SeatState.EMPTY && count == 0) {
							newStates[i][j] = SeatState.OCCUPIED;
							changes++;
						}
						else if (seats[i][j].state == SeatState.OCCUPIED && count >= 4) {
							newStates[i][j] = SeatState.EMPTY;
							changes++;
						}
						else
							newStates[i][j] = seats[i][j].state;
						
					}
				}
			}
			
			//copy back into world:
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (seats[i][j] != null)
						seats[i][j].state = newStates[i][j];
				}
			}
			
			return changes;
		}
		
		public int getOccupiedSeatCountAround2(int i, int j) {
			int count = 0;
			
			int [][] increments = new int [][] {
				{0,1},
				{0,-1},
				{1,0},
				{-1,0},
				{1,1},
				{-1,1},
				{1,-1},
				{-1,-1}
			};
			
			for (int iInc=0; iInc<increments.length; iInc++) {
				int di = increments[iInc][0];
				int dj = increments[iInc][1];
				
				int iTmp = i + di;
				int jTmp = j + dj;
				
				while(iTmp >= 0 && iTmp < height &&
					  jTmp >= 0 && jTmp < width) {
					
					if (seats[iTmp][jTmp] != null) {
						if(seats[iTmp][jTmp].state == SeatState.OCCUPIED)
							count++;
						break;
					}
					
					iTmp += di;
					jTmp += dj;
				}
			}
			
			return count;
		}
		
		public int step2() {
			int changes = 0;
			
			//calculate new states:
			SeatState [][] newStates = new SeatState[height][width];
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (seats[i][j] != null) {
						
						int count = getOccupiedSeatCountAround2(i, j);
						if (seats[i][j].state == SeatState.EMPTY && count == 0) {
							newStates[i][j] = SeatState.OCCUPIED;
							changes++;
						}
						else if (seats[i][j].state == SeatState.OCCUPIED && count >= 5) {
							newStates[i][j] = SeatState.EMPTY;
							changes++;
						}
						else
							newStates[i][j] = seats[i][j].state;
						
					}
				}
			}
			
			//copy back into world:
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (seats[i][j] != null)
						seats[i][j].state = newStates[i][j];
				}
			}
			
			return changes;
		}
		
		public int getOccupiedSeatCount() {
			int count = 0;
			for (int i=0; i<seats.length; i++) {
				for (int j=0; j<seats[i].length; j++) {
					if (seats[i][j] != null && seats[i][j].state == SeatState.OCCUPIED)
						count++;
				}
			}
			return count;
		}
		
	}
	
	public static World loadInput(File file) throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(file);
		
		Seat [][] map = new Seat[lines.size()][lines.get(0).length()];
		for (int i=0; i<map.length; i++) {
			char [] chars = lines.get(i).toCharArray();
			for (int j=0; j<chars.length; j++) {
				switch(chars[j]) {
				case 'L': map[i][j] = new Seat(); break;
				case '#': map[i][j] = new Seat(); map[i][j].state = SeatState.OCCUPIED; break;
				case '.': break;
				default: throw new IllegalStateException("Invalid char: " + chars[j]);
				}
			}
		}
		return new World(map);
	}
	
	public static void testPart1() throws Exception {
		World world = loadInput(new File("files/day11/test.txt"));
		world.print(System.out);
		System.out.println();
		
		for (int i=0; i<5; i++) {
			world.step();
			world.print(System.out);
			System.out.println();
		}
		
		System.out.println(world.getOccupiedSeatCount());
	}
	
	public static void solvePart1() throws Exception {
		World world = loadInput(new File("files/day11/input.txt"));
		//world.print(System.out);
		
		while (world.step() > 0) {
			//do nothing
		}
		
		world.print(System.out);
		System.out.println();
		
		System.out.println(world.getOccupiedSeatCount());
	}
	
	public static void testPart2() throws Exception {
		World world = loadInput(new File("files/day11/test.txt"));
		world.print(System.out);
		System.out.println();
		
		for (int i=0; i<7; i++) {
			int changes = world.step2();
			System.out.println("Step " + (i+1) + ": " + changes + " changes");
			world.print(System.out);
			System.out.println();
		}
		
		System.out.println(world.getOccupiedSeatCount());
	}
	
	public static void solvePart2() throws Exception {
		World world = loadInput(new File("files/day11/input.txt"));
		//world.print(System.out);
		
		while (world.step2() > 0) {
			//do nothing
		}
		
		world.print(System.out);
		System.out.println();
		
		System.out.println(world.getOccupiedSeatCount());
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
