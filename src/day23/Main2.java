package day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rewrite of the Game from Main.java to use a linked list format instead
 * of an array.  This speeds things up significantly, allowing us to run
 * Part 2 in just a second or two (instead of hours).
 */
public class Main2 {
	
	/**
	 * Linked list entry. Like a doubly-linked list, we maintain a pointer to the
	 * piece before and after. We also maintain a pointer to the entry with the
	 * value just before ours. We also make both of these lists into a circle so
	 * we don't have to worry about end-of-list exceptions.
	 * 
	 * NOTE: I'm actually not sure we need the pointer to "previous".  A singly-linked
	 * list probably would have worked here.
	 */
	static class Entry {
		
		final int value;
		
		Entry previous;
		Entry next;
		
		Entry previousByValue;
		
		public Entry(int value) {
			this.value = value;
		}
		
		protected void connectNext(Entry next) {
			this.next = next;
			next.previous = this;
		}
		
	}
	
	/**
	 * Game object. We keep pointers to the node with value "1" (since we 
	 * need this to produce answers for both part A and B). We also
	 * keep track of the current node.  Much of the game logic is actually
	 * easier to implement using this structure.
	 */
	static class Game2 {
		
		Entry one;
		Entry current;
		
		List<Entry> removedBuffer = new ArrayList<>(3);
		
		public void step() {
			//remove 3 cups:
			removedBuffer.add(current.next);
			removedBuffer.add(current.next.next);
			removedBuffer.add(current.next.next.next);
			
			Entry after = current.next.next.next.next;
			current.connectNext(after);
			
			//find destination cup:
			Entry destination = current.previousByValue;
			while (removedBuffer.contains(destination))
				destination = destination.previousByValue;
			
			//add cups back:
			after = destination.next;
			destination.connectNext(removedBuffer.get(0));
			removedBuffer.get(removedBuffer.size()-1).connectNext(after);
			removedBuffer.clear();
			
			current = current.next;
		}
		
		public String getResult() {
			StringBuilder s = new StringBuilder();
			
			Entry next = one.next;
			while (next != one) {
				s.append(next.value);
				next = next.next;
			}
			
			return s.toString();
		}
		
		public long getResultPart2() {
			long value1 = one.next.value;
			long value2 = one.next.next.value;
			System.out.println("value1: " + value1);
			System.out.println("value2: " + value2);
			return value1 * value2;
		}
		
		public static Game2 parseFrom(String input, boolean expandToOneMillion) {
			Game2 game = new Game2();
			
			char [] chars = input.toCharArray();
			Entry [] entries = new Entry[chars.length];
			
			Map<Integer, Entry> entriesByValue = new HashMap<>();
			for (int i=0; i<chars.length; i++) {
				int value = chars[i] - '0';
				entries[i] = new Entry(value);
				entriesByValue.put(value, entries[i]);
			}
			
			//connect (next/prev):
			for (int i=1; i<chars.length; i++)
				entries[i-1].connectNext(entries[i]);
			
			entries[entries.length-1].connectNext(entries[0]);
			
			for (int i=0; i<entries.length; i++) {
				int prevValue = entries[i].value - 1;
				if (prevValue == 0)
					prevValue = entries.length;
				 
				entries[i].previousByValue = entriesByValue.get(prevValue);
			}
			
			//part B:
			if (expandToOneMillion) {
				//get entry with highest value:
				Entry maxValueEntry = entriesByValue.get(entries.length);
				
				//connect new entries:
				Entry lastEntryByValue = maxValueEntry;
				Entry lastEntryByLocation = entries[entries.length-1];
				
				for (int value=maxValueEntry.value+1; value<=1000000; value++) {
					Entry entry = new Entry(value);
					lastEntryByLocation.connectNext(entry);
					entry.previousByValue = lastEntryByValue;
					
					lastEntryByLocation = entry;
					lastEntryByValue = entry;
				}
				
				//join last entry first one to form circle
				lastEntryByLocation.connectNext(entries[0]);
				entriesByValue.get(1).previousByValue = lastEntryByValue;
			}
			
			game.one = entriesByValue.get(1);
			game.current = entries[0];
			return game;
		}
		
		public static Game2 parseFrom(String input) {
			return parseFrom(input, false);
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			
			s.append("(").append(current.value).append(")");
			int count = 1;
			
			Entry next = current.next;
			while (next != current) {
				s.append(" ").append(next.value);
				count++;
				
				if (count >= 50) {
					s.append(" ...");
					break;
				}
				
				next = next.next;
			}
			
			return s.toString();
		}
		
	}
	
	public static void testPart1() throws Exception {
		Game2 game = Game2.parseFrom("389125467");
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
		Game2 game = Game2.parseFrom("487912365");
		for (int i=0; i<100; i++)
			game.step();
		
		System.out.println("RESULT: " + game.getResult());
	}
	
	public static void testPart2() throws Exception {
		Game2 game = Game2.parseFrom("389125467", true);
		System.out.println(game.toString());
		
		for (int i=0; i<10000000; i++)
			game.step();
		
		System.out.println("RESULT: " + game.getResultPart2());
	}
	
	public static void solvePart2() throws Exception {
		Game2 game = Game2.parseFrom("487912365", true);
		
		for (int i=0; i<10000000; i++)
			game.step();
		
		System.out.println("RESULT: " + game.getResultPart2());
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
