package day17;

import java.io.File;

public class Main {
	
	
	public static void testPart1() throws Exception {
		World3D world = World3D.loadFrom(new File("files/day17/test.txt"));
		world.print(System.out);
		
		for (int i=0; i<1; i++) {
			world = world.step();
			
			world.print(System.out);
			//System.out.println(i + ":" + world.getActiveCubeCount());
			//System.out.println();
		}
		
		System.out.println("Active Cubes = " + world.getActiveCubeCount());
	}
	
	public static void solvePart1() throws Exception {
		World3D world = World3D.loadFrom(new File("files/day17/input.txt"));
		world.print(System.out);
		
		for (int i=0; i<6; i++)
			world = world.step();
		
		System.out.println("Active Cubes = " + world.getActiveCubeCount());
	}
	
	public static void testPart2() throws Exception {
		World4D world = World4D.loadFrom(new File("files/day17/test.txt"));
		world.print(System.out);
		
		for (int i=0; i<6; i++) {
			world = world.step();
			
			//world.print(System.out);
			//System.out.println(i + ":" + world.getActiveCubeCount());
			//System.out.println();
		}
		
		System.out.println("Active Cubes = " + world.getActiveCubeCount());
	}
	
	public static void solvePart2() throws Exception {
		World4D world = World4D.loadFrom(new File("files/day17/input.txt"));
		world.print(System.out);
		
		for (int i=0; i<6; i++)
			world = world.step();
		
		System.out.println("Active Cubes = " + world.getActiveCubeCount());
	}
	
	public static void main(String [] args) {
		try {
			testPart1();
			//solvePart1();
			//testPart2();
			//solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
