package day16;

public class TicketField {

	String name;
	Validator validator;
	
	public TicketField(String name, Validator validator) {
		this.name = name;
		this.validator = validator;
	}
	
	public boolean isValid(int value) {
		return validator.isValid(value);
	}
	
	
	public static TicketField parseFrom(String text) {
		//example: departure time: 45-744 or 758-971
		String [] parts = text.split(":");
		return new TicketField(parts[0], DefaultValidator.parseFrom(parts[1].trim()));
	}
	
}
