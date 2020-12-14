package day14;

/**
 * This mask can be used to force bits to be certain values. For example:
 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X forces bit 2 to be zero and bit 7
 * to be 1. The rest of the bits are left unchanged.
 */
public class ForcingMask {
	
	String text;
	long orMask;
	long andMask;
	
	public ForcingMask(String text) {
		this.text = text;
		
		this.andMask = ~0;	//all 1's
		this.orMask = 0;
		
		char [] chars = text.toCharArray();
		char ch;
		for (int i=0; i<chars.length; i++) {
			ch = chars[chars.length-i-1];	//right to left
			if (ch == '0') {
				andMask = andMask & ~(1L << i);
			}
			else if (ch == '1') {
				orMask = orMask | (1L << i);
			}
		}
	}
	
	public long apply(long value) {
		return (value & andMask) | orMask;
	}
	
}
