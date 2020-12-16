package day16;

/**
 * All of the validators seem to follow this rule, so let's just code
 * it directly.  We can parse a rule of the form:
 * 
 * 45-744 or 758-971
 */
public class DefaultValidator implements Validator {

	int low1;
	int high1;
	
	int low2;
	int high2;
	
	public DefaultValidator(int low1, int high1, int low2, int high2) {
		this.low1 = low1;
		this.high1 = high1;
		this.low2 = low2;
		this.high2 = high2;
	}
	
	public boolean isValid(int value) {
		return (value >= low1 && value <= high1) ||
				(value >= low2 && value <= high2);
	}
	
	public static DefaultValidator parseFrom(String text) {
		String [] parts = text.split("\\s+");
		String [] parts1 = parts[0].split("-");
		String [] parts2 = parts[2].split("-");
		return new DefaultValidator(
				Integer.parseInt(parts1[0]),
				Integer.parseInt(parts1[1]),
				Integer.parseInt(parts2[0]),
				Integer.parseInt(parts2[1])
				);
	}
	
}
