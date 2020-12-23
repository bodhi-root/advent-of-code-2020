package day23;

import java.util.ArrayList;
import java.util.List;

public class Main {
	
	static class Game {
		
		List<Integer> cups = new ArrayList<>();
		int nextIndex = 0;
		
		List<Integer> buffer = new ArrayList<>();
		
		public void step() {
			//current cup:
			int currentCup = cups.get(nextIndex);
			
			//remove 3 cups:
			int toRemove = (nextIndex + 1) % cups.size();
			for (int i=0; i<3; i++)
				buffer.add(cups.remove(toRemove >= cups.size() ? 0 : toRemove));
			
			//find destination cup:
			int destinationCup = currentCup - 1;
			while (cups.indexOf(destinationCup) < 0) {
				destinationCup--;
				if (destinationCup < 0)
					destinationCup += 10;
			}
			int destinationCupIndex = cups.indexOf(destinationCup);
			
			//add cups back:
			int toAdd = destinationCupIndex + 1;
			for (int i=0; i<3; i++)
				cups.add(toAdd+i, buffer.get(i));
			
			int currentCupIndex = cups.indexOf(currentCup);
			nextIndex = (currentCupIndex + 1) % cups.size();
			//nextIndex = (nextIndex + 1) % cups.size();
			
			//return buffer to original state:
			buffer.clear();
		}
		
		public String getResult() {
			int offset = cups.indexOf(1);
			StringBuilder s = new StringBuilder();
			for (int i=1; i<cups.size(); i++) {
				s.append(cups.get((offset+i) % cups.size()));
			}
			return s.toString();
		}
		
		public long getResultPart2() {
			int offset = cups.indexOf(1);
			
			int nextIndex = offset + 1;
			if (nextIndex >= cups.size())
				nextIndex = 0;
			
			long value1 = cups.get(nextIndex);
			
			nextIndex++;
			if (nextIndex >= cups.size())
				nextIndex = 0;
			
			long value2 = cups.get(nextIndex);
			
			return value1 * value2;
		}
		
		public static Game parseFrom(String input) {
			Game game = new Game();
			
			char [] chars = input.toCharArray();
			for (int i=0; i<chars.length; i++)
				game.cups.add(chars[i] - '0');
			
			return game;
		}
		
		public void expandToOneMillion() {
			int maxValue = 0;
			for (int i=0; i<cups.size(); i++)
				maxValue = Math.max(cups.get(i), maxValue);
			
			int toAdd = 1000000 - cups.size();
			int nextValue = maxValue + 1;
			for (int i=0; i<toAdd; i++) {
				cups.add(nextValue++);
			}
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<cups.size(); i++) {
				if (i > 0)
					s.append(" ");
				if (i == nextIndex)
					s.append("(");
				
				s.append(cups.get(i).intValue());
				
				if (i == nextIndex)
					s.append(")");
			}
			return s.toString();
		}
		
		public String toString(int len) {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<len; i++) {
				if (i > 0)
					s.append(" ");
				if (i == nextIndex)
					s.append("(");
				
				s.append(cups.get(i).intValue());
				
				if (i == nextIndex)
					s.append(")");
			}
			return s.toString();
		}
		
	}
	
	public static void testPart1() throws Exception {
		Game game = Game.parseFrom("389125467");
		System.out.println(game.toString());
		System.out.println();
		
		for (int i=0; i<10; i++) {
			game.step();
			System.out.println(game.toString());
			System.out.println();
		}
		
		System.out.println("RESULT: " + game.getResult());
	}
	
	public static void solvePart1() throws Exception {
		Game game = Game.parseFrom("487912365");
		for (int i=0; i<100; i++)
			game.step();
		
		System.out.println("RESULT: " + game.getResult());
	}
	
	public static void testPart2() throws Exception {
		Game game = Game.parseFrom("389125467");
		game.expandToOneMillion();
		
		System.out.println(game.toString(100));
		System.out.println();
		
		//print first 50 steps and look for patterns:
		for (int i=0; i<50; i++) {
			game.step();
			System.out.println(game.toString(100));
			System.out.println();
		}
		
		//when nextValue = 999,999
		//next 3: 1,000,000 3 4
		//destination value: 999,998
		
		//state: 999,995 999,999 (6) 7 9 1 10 12 13 14 16 17 18
		//There are some patterns, but I couldn't find any way to solve it this way
		//I ended up writing Main2.java instead.
		
		/*
		//WARNING: This runs forever
		for (int i=0; i<10000000; i++) {
			if (i % 100000 == 0)
				System.out.println(i);
			game.step();
		}
		*/
		
		System.out.println("RESULT: " + game.getResultPart2());
	}
	
	public static void solvePart2() throws Exception {
		Game game = Game.parseFrom("487912365");
		game.expandToOneMillion();
		long time0 = System.currentTimeMillis();
		
		//WARNING: This runs forever
		// the first 100k take about 3 minutes
		for (int i=0; i<100; i++) {
			for (int j=0; j<100000; j++)
				game.step();
			
			System.out.println(i + " (" + (System.currentTimeMillis() - time0) + ")");
		}
		
		System.out.println("RESULT: " + game.getResultPart2());
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
