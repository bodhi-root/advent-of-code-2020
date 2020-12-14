package day14;

import java.util.ArrayList;
import java.util.List;

public class Computer {
	
	static class Register {
		long value = 0;
	}
	
	ForcingMask mask = null;
	List<Register> registers = new ArrayList<>();
	
	public Register getRegister(int offset) {
		while(offset >= registers.size())
			registers.add(new Register());
		
		return registers.get(offset);
	}
	
	public void set(int offset, long value) {
		if (mask != null)
			value = mask.apply(value);
		
		getRegister(offset).value = value;
	}
	
	public long getRegisterSum() {
		long value = 0;
		for (Register register : registers)
			value += register.value;
		return value;
	}
	
	public void run(String command, boolean verbose) {
		if (command.startsWith("mask")) {
			String value = command.split("\\s+")[2];
			this.mask = new ForcingMask(value);
			
			if(verbose)
				System.out.println("Mask set to: " + value);
		}
		else if (command.startsWith("mem")) {
			String [] parts = command.split("\\s+");
			int index = Integer.parseInt(parts[0].substring("mem[".length(), parts[0].length()-1));
			long value = Long.parseLong(parts[2]);
			set(index, value);
			
			if (verbose)
				System.out.println("Register " + index + " set to " + value + " => " + getRegister(index).value);
		} 
		else {
			throw new IllegalArgumentException(command);
		}
	}
	
}
