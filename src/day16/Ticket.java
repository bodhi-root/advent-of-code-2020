package day16;

public class Ticket {

	public int [] values;
	
	public Ticket(int [] values) {
		this.values = values;
	}
	
	public static Ticket parseFrom(String text) {
		String [] parts = text.split(",");
		int [] values = new int[parts.length];
		for (int i=0; i<parts.length; i++)
			values[i] = Integer.parseInt(parts[i]);
		
		return new Ticket(values);
	}
	
}
