package day17;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.FileUtil;

public class World3D {
	
	/** Map of 0,1 values keyed by "x:y:z" */
	Map<String, Integer> cubes = new HashMap<>(10000);
	
	/** min/max boundaries of active cubes */
	int minX = 0;
	int maxX = 0;
	int minY = 0;
	int maxY = 0;
	int minZ = 0;
	int maxZ = 0;
	
	public void set(int x, int y, int z, int value) {
		String key = x + ":" + y + ":" + z;
		cubes.put(key, value);
		
		//System.out.println("Set " + x + "," + y + "," + z + " = " + value);
		
		if (value > 0) {
			minX = Math.min(minX, x);
			maxX = Math.max(maxX, x);
			minY = Math.min(minY, y);
			maxY = Math.max(maxY, y);
			minZ = Math.min(minZ, z);
			maxZ = Math.max(maxZ, z);
		}
	}
	
	public int get(int x, int y, int z) {
		String key = x + ":" + y + ":" + z;
		Integer value = cubes.get(key);
		return (value == null) ? 0 : value.intValue();
	}
	
	public long getActiveCubeCount() {
		long sum = 0;
		for (Integer value : cubes.values())
			sum += value.intValue();
		return sum;
	}
	
	public int getSurroundingActiveCount(int x, int y, int z) {
		int sum = 0;
		
		for (int dx=-1; dx<=1; dx++) {
			for (int dy=-1; dy<=1; dy++) {
				for (int dz=-1; dz<=1; dz++) {
					
					if (dx == 0 &&
						dy == 0 &&
						dz == 0)
						continue;
					
					sum += get(x+dx, y+dy, z+dz);
				}
			}
		}
		
		return sum;
	}
	
	public World3D step() {
		World3D world = new World3D();
		
		for (int x=minX-1; x<=maxX+1; x++) {
			for (int y=minY-1; y<=maxY+1; y++) {
				for (int z=minZ-1; z<=maxZ+1; z++) {
					
					int state = get(x,y,z);
					int count = getSurroundingActiveCount(x,y,z);
					
					if (state == 1) {
						int newState = (count == 2 || count == 3) ? 1 : 0;
						world.set(x, y, z, newState);
					} else {
						int newState = (count == 3) ? 1 : 0;
						world.set(x, y, z, newState);
					}
					
				}
			}
		}
		
		return world;
	}		
	
	public static World3D loadFrom(File file) throws IOException {
		World3D world = new World3D();
		
		List<String> lines = FileUtil.readLinesFromFile(file);
		for (int y=0; y<lines.size(); y++) {
			char [] chars = lines.get(y).toCharArray();
			for (int x=0; x<chars.length; x++) {
				if (chars[x] == '#')
					world.set(x, y, 0, 1);
			}
		}
		
		return world;
	}
	
	public void print(PrintStream out) {
		for (int z=minZ; z<=maxZ; z++) {
			printLayer(out, z);
			out.println();
		}
	}
	
	public void printLayer(PrintStream out, int z) {
		out.println("z = " + z);
		out.println("(minX, minY) = (" + minX + "," + minY + ")");
		
		StringBuilder s = new StringBuilder();
		int value;
		for (int y=minY; y<=maxY; y++) {
			s.setLength(0);
			for (int x=minX; x<=maxX; x++) {
				value = get(x,y,z);
				s.append(value > 0 ? '#' : '.');
			}
			out.println(s.toString());
		}
	}
	
}

