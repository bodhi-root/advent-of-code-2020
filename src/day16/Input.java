package day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Input {

	List<TicketField> ticketFields = new ArrayList<>();
	
	Ticket yourTicket;
	List<Ticket> nearbyTickets = new ArrayList<>();
	
	public static Input readFrom(File file) throws IOException {
		Input input = new Input();
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		try {
			String line;
			
			//read TicketFields until first empty line:
			while(true) {
				line = in.readLine();
				if (line == null)
					throw new IOException("Invalid file: premature EOF");
				
				line = line.trim();
				if (line.isEmpty())
					break;
				
				input.ticketFields.add(TicketField.parseFrom(line));
			}
			
			//read your ticket:
			line = in.readLine();
			if (!line.equals("your ticket:"))
				throw new IOException("Invalid file");
			
			input.yourTicket = Ticket.parseFrom(in.readLine());
			
			line = in.readLine();	//empty line
			
			//read nearby tickets:
			line = in.readLine();
			if (!line.equals("nearby tickets:"))
				throw new IOException("Invalid file");
			
			while ((line = in.readLine()) != null) {
				input.nearbyTickets.add(Ticket.parseFrom(line));
			}
		}
		finally {
			in.close();
		}
		
		return input;
	}
	
}
