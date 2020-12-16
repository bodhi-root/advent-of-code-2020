package day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	
	/**
	 * Returns the last index in 'values' that matches the target value, searching
	 * only between the indexes of 'earliest' and 'latest' (inclusive).
	 */
	protected static int lastIndexOf(List<Long> values, Long target, int earliest, int latest) {
		for (int i=latest; i>=earliest; i--) {
			if (values.get(i).equals(target))
				return i;
		}
		return -1;
	}
	
	/**
	 * Runs a naive searcher for Part 1.  This builds the complete list in memory
	 * and just searches for the last occurrence of each value whenver it comes up.
	 */
	public static void run(String input, int targetSize) {
		String [] parts = input.split(",");
		List<Long> values = new ArrayList<>();
		for (String part : parts)
			values.add(Long.valueOf(part));
		
		Long lastValue;
		Long newValue;
		int index;
		while (values.size() < targetSize) {
			if (values.size() % 10000 == 0)
				System.out.println(values.size());
			
			lastValue = values.get(values.size()-1);
			index = lastIndexOf(values, lastValue, 0, values.size()-2);
			
			if (index < 0)
				newValue = Long.valueOf(0);
			else
				newValue = Long.valueOf(values.size()-index-1);
			
			values.add(newValue);
		}
		
		lastValue = values.get(values.size()-1);
		System.out.println("Last Value: " + lastValue);
	}
	
	/**
	 * Optimized implementation for Part 2.  Instead of storing the complete
	 * list in memory we store a Map that has each value associated with the
	 * last index at which it appeared.  This avoids storing unnecessary 
	 * information in memory and makes the lookups for the last index 
	 * immediate rather than having to search through a list of values.
	 */
	public static void runFaster(String input, int targetSize) {
		String [] parts = input.split(",");
		
		Long lastValue;
		int size = 0;
		
		//NOTE: we'll always hold lastValue separate without putting it into the list
		lastValue = Long.valueOf(parts[0]);
		size++;
		
		Map<Long, Integer> map = new HashMap<>(10000);
		for (int i=1; i<parts.length; i++) {
			map.put(lastValue, i-1);
			lastValue = Long.valueOf(parts[i]);
			size++;
		}
		
		Integer lastIndex;
		Long newValue;
		while (size < targetSize) {
			//if (size % 10000 == 0)
			//	System.out.println(size);
			
			lastIndex = map.get(lastValue);
			
			if (lastIndex == null)
				newValue = Long.valueOf(0);
			else
				newValue = Long.valueOf(size - lastIndex.intValue() - 1);
			
			map.put(lastValue, size-1);
			lastValue = newValue;
			size++;
		}
		
		System.out.println("Last Value: " + lastValue);
	}
	
	public static void testPart1() {
		run("0,3,6", 2020);
		runFaster("0,3,6", 2020);
	}
	
	public static void solvePart1() throws Exception {
		run("12,20,0,6,1,17,7", 2020);
		runFaster("12,20,0,6,1,17,7", 2020);
	}
	
	public static void solvePart2() throws Exception {
		runFaster("12,20,0,6,1,17,7", 30000000);
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
