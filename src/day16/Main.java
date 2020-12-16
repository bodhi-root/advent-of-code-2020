package day16;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
	
	public static boolean [] runPart1(Input input) {
		boolean [] valid = new boolean[input.nearbyTickets.size()];
		Arrays.fill(valid, true);
		
		int errorCount = 0;
		int sumOfErrors = 0;
		
		for (int ticketIndex=0; ticketIndex<input.nearbyTickets.size(); ticketIndex++) {
			Ticket ticket = input.nearbyTickets.get(ticketIndex);
			for (int i=0; i<ticket.values.length; i++) {
				boolean isValid = false;
				for (TicketField field : input.ticketFields) {
					if (field.isValid(ticket.values[i])) {
						isValid = true;
						break;
					}
				}
				if (!isValid) {
					System.out.println("Error in ticket " + ticketIndex + ". Bad value: " + ticket.values[i]);
					errorCount++;
					sumOfErrors += ticket.values[i];
					
					valid[ticketIndex] = false;
				}
			}
		}
		
		System.out.println("Error Count = " + errorCount);
		System.out.println("Sum of Errors = " + sumOfErrors);
		
		return valid;
	}
	
	public static void testPart1() throws Exception {
		Input input = Input.readFrom(new File("files/day16/test.txt"));
		runPart1(input);
	}
	
	public static void solvePart1() throws Exception {
		Input input = Input.readFrom(new File("files/day16/input.txt"));
		runPart1(input);
	}
	
	@SuppressWarnings("unchecked")
	public static void runPart2(Input input) {
		//remove invalid records:
		boolean [] valid = runPart1(input);
		for (int i=valid.length-1; i>=0; i--) {
			if (!valid[i])
				input.nearbyTickets.remove(i);
		}
		
		//find candidates for each column:
		int numColumns = input.yourTicket.values.length;
		Set<TicketField> [] possibleFields = new Set[numColumns];
		
		for (int colIndex=0; colIndex<numColumns; colIndex++) {
			possibleFields[colIndex] = new HashSet<>();
			
			for (int fieldIndex=0; fieldIndex<input.ticketFields.size(); fieldIndex++) {
				TicketField field = input.ticketFields.get(fieldIndex);
				boolean isValid = true;
				for (int ticketIndex=0; ticketIndex<input.nearbyTickets.size(); ticketIndex++) {
					Ticket ticket = input.nearbyTickets.get(ticketIndex);
					if (!field.isValid(ticket.values[colIndex])) {
						isValid = false;
						break;
					}
				}
				
				if (isValid) {
					System.out.println("Column " + colIndex + " = " + field.name);
					possibleFields[colIndex].add(field);
				}
			}
		}
		
		//build final list:
		//iterate through and assign field where they are constrained to 1 value.
		//remove these as options from other fields, and repeat until done.
		TicketField [] fields = new TicketField[numColumns];
		while (true) {
			int addedCols = 0;
			for (int i=0; i<fields.length; i++) {
				if (fields[i] == null && possibleFields[i].size() == 1) {
					//assign value:
					TicketField field = possibleFields[i].iterator().next();
					fields[i] = field;
					
					//remove from all others:
					for (int j=0; j<fields.length; j++) {
						if (i == j)
							continue;
						
						possibleFields[j].remove(field);
					}
					
					addedCols++;
				}
			}
			
			if (addedCols == 0)
				break;
		}
		
		//print result:
		System.out.println();
		System.out.println("Your Ticket:");
		
		long product = 1;
		for (int i=0; i<fields.length; i++) {
			System.out.println(fields[i].name + " = " + input.yourTicket.values[i]);
			
			if (fields[i].name.startsWith("departure"))
				product *= input.yourTicket.values[i];
		}
		
		System.out.println();
		System.out.println("Product = " + product);
	}
	
	public static void testPart2() throws Exception {
		Input input = Input.readFrom(new File("files/day16/test.txt"));
		runPart2(input);
	}
	
	public static void solvePart2() throws Exception {
		Input input = Input.readFrom(new File("files/day16/input.txt"));
		runPart2(input);
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
