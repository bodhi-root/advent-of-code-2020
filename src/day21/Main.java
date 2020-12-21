package day21;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static class Entry {
		
		List<String> ingredients = new ArrayList<>();
		List<String> allergens = new ArrayList<>();
		
		public static Entry parseFrom(String line) {
			int index = line.indexOf(" (contains");
			String [] ingredients = line.substring(0,index).trim().split("\\s+");
			String [] allergens = line.substring(index+" (contains".length(), line.length()-1).trim().split(",\\s+");
			
			Entry entry = new Entry();
			for (String x : ingredients)
				entry.ingredients.add(x);
			for (String x : allergens)
				entry.allergens.add(x);
			
			return entry;
		}
		
		public boolean hasAllergen(String allergen) {
			return allergens.contains(allergen);
		}
		
	}
	
	static class Input {
		
		List<Entry> entries = new ArrayList<>();
		
		public static Input readFromFile(File file) throws IOException {
			Input input = new Input();
			
			List<String> lines = FileUtil.readLinesFromFile(file);
			for (String line : lines)
				input.entries.add(Entry.parseFrom(line));
			
			return input;
		}
		
		public Set<String> getAllergenSet() {
			Set<String> allergens = new HashSet<>();
			for (Entry entry : entries)
				allergens.addAll(entry.allergens);
			
			return allergens;
		}
		
		/**
		 * Return a map of possible ingredients that could be each allergen.
		 * The map is keyed by allergen.  The value is the list of possible
		 * ingredients.  These are formed by looking at every Entry that
		 * contains the given allergen and noting the ingredients.  The intersection
		 * of all ingredient lists that contain the allergen is the set of
		 * possibilities.
		 */
		public Map<String, Set<String>> getAllergenPossibilities() {
			Set<String> allergens = getAllergenSet();
			
			Map<String, Set<String>> map = new HashMap<>();
			for (String allergen : allergens) {
				Set<String> ingredients = null;
				for (Entry entry : entries) {
					if (entry.hasAllergen(allergen)) {
						if (ingredients == null) {
							ingredients = new HashSet<>();
							ingredients.addAll(entry.ingredients);
						} else {
							ingredients.retainAll(entry.ingredients);
						}
					}
				}
				map.put(allergen,  ingredients);
			}
			return map;
		}
		
	}
	
	public static void runPart1(File file) throws IOException {
		Input input = Input.readFromFile(file);
		Map<String, Set<String>> possibilityMap = input.getAllergenPossibilities();
		
		System.out.println("Possibilities:");
		Set<String> possibilitySet = new HashSet<>();
		
		for (Map.Entry<String, Set<String>> e : possibilityMap.entrySet()) {
			String allergen = e.getKey();
			Set<String> ingredients = e.getValue();
			possibilitySet.addAll(ingredients);
			
			StringBuilder s = new StringBuilder();
			s.append(allergen).append(" : ");
			Iterator<String> iIngredients = ingredients.iterator();
			while (iIngredients.hasNext()) {
				s.append(iIngredients.next());
				if (iIngredients.hasNext())
					s.append(", ");
			}
			System.out.println(s.toString());
		}
		
		//count non-possible ingredients:
		int count = 0;
		for (Entry entry : input.entries) {
			for (String ingredient : entry.ingredients) {
				if (!possibilitySet.contains(ingredient))
					count++;
			}
		}
		System.out.println("Non-possible ingredient count: " + count);
	}
	
	public static void testPart1() throws Exception {
		runPart1(new File("files/day21/test.txt"));
	}
	
	public static void solvePart1() throws Exception {
		runPart1(new File("files/day21/input.txt"));
	}
	
	public static void solvePart2() throws Exception {
		//eggs : jbbsjh
		//shellfish : vnjxjg, cpttmnv
		//nuts : cpttmnv, jbbsjh
		//sesame : vnjxjg, tdmqcl, jbbsjh
		//wheat : mzqjxq, vnjxjg, jbbsjh, fbtqkzc
		//soy : nlph, mzqjxq, vnjxjg, jbbsjh
		//peanuts : ccrbr, jbbsjh, fbtqkzc
		//dairy : jbbsjh, fbtqkzc
		
		//manually, we can infer:
		//dairy : fbtqkzc
		//eggs: jbbsjh
		//nuts : cpttmnv
		//peanuts : ccrbr
		//sesame : tdmqcl
		//shellfish : vnjxjg
		//soy : nlph
		//wheat : mzqjxq
		
		//output: fbtqkzc,jbbsjh,cpttmnv,ccrbr,tdmqcl,vnjxjg,nlph,mzqjxq
		
		//but if we wanted to do this programmatically:
		
		Input input = Input.readFromFile(new File("files/day21/input.txt"));
		Map<String, Set<String>> possibilityMap = input.getAllergenPossibilities();
		
		//iterate through map. any entry that has only 1 possible value can be assigned.
		//when a match is found, remove that as a possibility for all other entries.
		//repeat this until the map is empty.
		List<Translation> results = new ArrayList<>();
		while (!possibilityMap.isEmpty()) {
			
			int removedCount = 0;
			
			List<String> allergens = new ArrayList<>(possibilityMap.keySet());	//create copy, since we'll modify Map
			for (String allergen : allergens) {
				Set<String> ingredients = possibilityMap.get(allergen);
				if (ingredients.size() == 1) {
					String ingredient = ingredients.iterator().next();
					results.add(new Translation(allergen, ingredient));
					
					possibilityMap.remove(allergen);
					removedCount++;
					
					for (Set<String> values : possibilityMap.values())
						values.remove(ingredient);
				} else if (ingredients.size() == 0) {
					throw new IllegalStateException("Invalid input. Allergen " + allergen + " has no possible ingredients");
				}
			}
			
			if (removedCount == 0)
				throw new IllegalStateException("Unable to get list down to 1-to-1 mapping");
		}
		
		System.out.println("Results:");
		for (Translation t : results)
			System.out.println(t.allergen + " = " + t.ingredient);
		
		//sorty by allergen:
		results.sort(new Comparator<Translation>() {
			public int compare(Translation o1, Translation o2) {
				return o1.allergen.compareTo(o2.allergen);
			}
		});
		
		//get result:
		StringBuilder s = new StringBuilder();
		for (int i=0; i<results.size(); i++) {
			if (i > 0)
				s.append(",");
			s.append(results.get(i).ingredient);
		}
		System.out.println("RESULT: " + s.toString());
	}
	
	static class Translation {
		String allergen;
		String ingredient;
		
		public Translation(String allergen, String ingredient) {
			this.allergen = allergen;
			this.ingredient = ingredient;
		}
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
