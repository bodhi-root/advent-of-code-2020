package day12;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static enum Direction {NORTH, EAST, SOUTH, WEST};
	
	static Direction [] ALL_DIRECTIONS = Direction.values();
	
	static class Ship {
		
		int x = 0;
		int y = 0;
		
		Direction dir = Direction.EAST;
		
		public void turnLeft(int units) {
			if (units % 90 != 0)
				throw new IllegalArgumentException();
			
			int turns = units / 90;
			
			int newIndex = dir.ordinal() - turns;
			while (newIndex < 0)
				newIndex += ALL_DIRECTIONS.length;
			
			dir = ALL_DIRECTIONS[newIndex];
		}
		public void turnRight(int units) {
			if (units % 90 != 0)
				throw new IllegalArgumentException();
			
			int turns = units / 90;
			
			dir = ALL_DIRECTIONS[(dir.ordinal() + turns) % ALL_DIRECTIONS.length];
		}
		public void move(Direction dir, int units) {
			switch(dir) {
			case EAST:  x += units; break;
			case WEST:  x -= units; break;
			case NORTH: y += units; break;
			case SOUTH: y -= units; break;
			}
		}
		public void moveForward(int units) {
			move(dir, units);
		}
		
		public void moveBy(int dx, int dy) {
			x += dx;
			y += dy;
		}
		
	}
	
	static class Waypoint {
		
		int x;
		int y;
		
		public Waypoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void rotateLeft() {
			//R = [0 -1]
			//    [1  0]
			int xNew = -y;
			int yNew = x;
			
			x = xNew;
			y = yNew;
		}
		public void rotateRight() {
			//R = [0  1]
			//    [-1 0]
			int xNew = y;
			int yNew = -x;
			
			x = xNew;
			y = yNew;
		}
		
		public void rotateLeft(int units) {
			if (units % 90 != 0)
				throw new IllegalArgumentException();
			
			int turns = units / 90;
			for (int i=0; i<turns; i++)
				rotateLeft();
		}
		public void rotateRight(int units) {
			if (units % 90 != 0)
				throw new IllegalArgumentException();
			
			int turns = units / 90;
			for (int i=0; i<turns; i++)
				rotateRight();
		}
		public void move(Direction dir, int units) {
			switch(dir) {
			case EAST:  x += units; break;
			case WEST:  x -= units; break;
			case NORTH: y += units; break;
			case SOUTH: y -= units; break;
			}
		}
		
	}
	
	public static void run(List<String> commands) {
		Ship ship = new Ship();
		
		for (String line : commands) {
			char action = line.charAt(0);
			int units = Integer.parseInt(line.substring(1));
			
			switch(action) {
			case 'N': ship.move(Direction.NORTH, units); break;
			case 'E': ship.move(Direction.EAST, units); break;
			case 'S': ship.move(Direction.SOUTH, units); break;
			case 'W': ship.move(Direction.WEST, units); break;
			case 'L': ship.turnLeft(units); break;
			case 'R': ship.turnRight(units); break;
			case 'F': ship.moveForward(units); break;
			default: throw new IllegalArgumentException("Invalid line: " + line);
			}
		}
		
		System.out.println("(" + ship.x + "," + ship.y + ") = " + (Math.abs(ship.x) + Math.abs(ship.y)));
	}
	
	public static void testPart1() throws Exception {
		List<String> lines = new ArrayList<>();
		lines.add("F10");
		lines.add("N3");
		lines.add("F7");
		lines.add("R90");
		lines.add("F11");
		
		run(lines);
	}
	
	public static void solvePart1() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day12/input.txt"));
		run(lines);
	}
	
	public static void run2(List<String> commands, boolean print) {
		Ship ship = new Ship();
		Waypoint point = new Waypoint(10, 1);
		
		for (String command : commands) {
			char action = command.charAt(0);
			int units = Integer.parseInt(command.substring(1));
			
			switch(action) {
			case 'N': point.move(Direction.NORTH, units); break;
			case 'E': point.move(Direction.EAST, units); break;
			case 'S': point.move(Direction.SOUTH, units); break;
			case 'W': point.move(Direction.WEST, units); break;
			case 'L': point.rotateLeft(units); break;
			case 'R': point.rotateRight(units); break;
			case 'F': ship.moveBy(point.x * units, point.y * units); break;
			default: throw new IllegalArgumentException("Invalid line: " + command);
			}
			
			if (print) {
				System.out.println(command);
				System.out.println("Ship = (" + ship.x + "," + ship.y + ") Waypoint = (" + point.x + "," + point.y + ")");
			}
		}
		
		System.out.println("(" + ship.x + "," + ship.y + ") = " + (Math.abs(ship.x) + Math.abs(ship.y)));
	}
	
	public static void testPart2() throws Exception {
		List<String> lines = new ArrayList<>();
		lines.add("F10");
		lines.add("N3");
		lines.add("F7");
		lines.add("R90");
		lines.add("F11");
		
		run2(lines, true);
	}
	
	public static void solvePart2() throws Exception {
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day12/input.txt"));
		run2(lines, false);
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
