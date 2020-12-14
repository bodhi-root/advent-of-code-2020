package day14;

import java.util.ArrayList;
import java.util.List;

/**
 * "Floating" mask as used in Part B. The 0's leave input bits
 * unchanged.  The 1's force the input bits to 1. The X's become
 * "floating" bits that will take on all possible values.
 * 
 * 000000000000000000000000000000X1001X
 * 
 * An "orMask" with 1's in the right place will accomplish the 1s rule.
 * The zero rule can be ignored.  We'll use ForcingMasks to accomplish
 * setting every possible value on the floating bits.
 */
public class FloatingMask {
	
	String text;
	long orMask;
	
	List<ForcingMask> forcingMasks = new ArrayList<>();
	
	public FloatingMask(String text) {
		this.text = text;
		
		this.orMask = 0;
		
		List<String> forcingMaskText = new ArrayList<>();
		forcingMaskText.add("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		                    
		char [] chars = text.toCharArray();
		char ch;
		for (int i=0; i<chars.length; i++) {
			ch = chars[i];	//left to right
			if (ch == '1') {
				orMask = orMask | (1L << (chars.length-i-1));
			} else if (ch == 'X') {
				
				List<String> newMaskList = new ArrayList<>();
				for (String maskText : forcingMaskText) {
					char [] maskChars = maskText.toCharArray();
					maskChars[i] = '0';
					newMaskList.add(new String(maskChars));
					maskChars[i] = '1';
					newMaskList.add(new String(maskChars));
				}
				forcingMaskText = newMaskList;
				System.out.println("list size = " + forcingMaskText.size());
				
			}
		}
		
		if (forcingMaskText.size() > 1) {
			for (String maskText : forcingMaskText)
				forcingMasks.add(new ForcingMask(maskText));
		}
		System.out.println("list size = " + forcingMaskText.size());
	}
	
	public long [] getValuesFor(long value) {
		value = value | orMask;
		
		if (forcingMasks.isEmpty())
			return new long [] {value};
		
		long [] output = new long[forcingMasks.size()];
		for (int i=0; i<output.length; i++)
			output[i] = forcingMasks.get(i).apply(value);
		return output;
	}
	
}
