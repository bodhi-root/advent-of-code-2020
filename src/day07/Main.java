package day07;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.FileUtil;

public class Main {
	
	static class InnerBag {
		
		String color;
		int quantity;
		
		public InnerBag(String color, int quantity) {
			this.color = color;
			this.quantity = quantity;
		}
		
	}
	
	static class Bag {
		
		String color;
		List<InnerBag> innerBags = new ArrayList<>();
		
		public Bag(String color) {
			this.color = color;
		}
		
		public boolean contains(String innerColor) {
			for (InnerBag inner : innerBags) {
				if (inner.color.equals(innerColor))
					return true;
			}
			return false;
		}
		
		public static Bag parseFrom(String line) {
			int index = line.indexOf("bags contain");
			if (index < 0)
				throw new IllegalArgumentException();
			
			if (line.endsWith("."))
				line = line.substring(0,line.length()-1);
			
			String color = line.substring(0, index-1).trim();
			Bag bag = new Bag(color);
			
			String suffix = line.substring(index+"bags contain".length()+1).trim();
			if (!suffix.equals("no other bags")) {
				String [] parts = suffix.split(",");
				for (int i=0; i<parts.length; i++) {
					String innerBagText = parts[i].trim();
					if (innerBagText.endsWith("bag"))
						innerBagText = innerBagText.substring(0, innerBagText.length()-3).trim();
					else if (innerBagText.endsWith("bags"))
						innerBagText = innerBagText.substring(0, innerBagText.length()-4).trim();
					else
						throw new IllegalArgumentException();
					
					String [] parts2 = innerBagText.split("\\s+", 2);
					int quantity = Integer.parseInt(parts2[0]);
					String innerColor = parts2[1];
					
					bag.innerBags.add(new InnerBag(innerColor, quantity));
				}
			}
			
			return bag;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append(color);
			s.append(" {");
			for (int i=0; i<innerBags.size(); i++) {
				InnerBag inner = innerBags.get(i);
				if (i > 0)
					s.append(",");
				s.append(inner.color).append("=").append(inner.quantity);
			}
			s.append("}");
			return s.toString();
		}
		
	}
	
	public static List<Bag> parseInput(File file) throws IOException {
		List<Bag> bags = new ArrayList<>();
		
		List<String> lines = FileUtil.readLinesFromFile(file);
		for (String line : lines) {
			Bag bag = Bag.parseFrom(line);
			bags.add(bag);
		}
		
		return bags;
	}
	
	protected static void addContainingColorsToSet(Set<String> set, String color, List<Bag> bags) {
		for (Bag bag : bags) {
			if (bag.contains(color)) {
				if (set.add(bag.color))
					addContainingColorsToSet(set, bag.color, bags);
			}
		}
	}
	
	public static void solvePart1() throws Exception {
		List<Bag> bags = parseInput(new File("files/day07/input.txt"));
		//System.out.println(bags.get(0));
		
		/*
		Set<String> colors = new HashSet<>();
		for (Bag bag : bags) {
			if (bag.contains("shiny gold"))
				colors.add(bag.color);
		}
		System.out.println(colors.size() + " colors directly");
		*/
		
		Set<String> colors = new HashSet<>();
		addContainingColorsToSet(colors, "shiny gold", bags);
		System.out.println(colors.size() + " colors in total");
	}
	
	protected static int countBagsInside(String color, Map<String, Bag> bags) {
		Bag bag = bags.get(color);
		if (bag.innerBags.isEmpty())
			return 0;
		
		int count = 0;
		for (InnerBag inner : bag.innerBags) {
			count += inner.quantity + (inner.quantity * countBagsInside(inner.color, bags));
		}
		return count;
	}
	
	public static void solvePart2() throws Exception {
		List<Bag> bags = parseInput(new File("files/day07/input.txt"));
		
		Map<String, Bag> bagsByColor = new HashMap<>(bags.size());
		for (Bag bag : bags)
			bagsByColor.put(bag.color, bag);
		
		int count = countBagsInside("shiny gold", bagsByColor);
		System.out.println(count);
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
