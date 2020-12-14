package day14;

import java.util.HashMap;
import java.util.Map;

public class Computer2 {
	
	static class Register {
		long value = 0;
	}
	
	FloatingMask mask = null;
	Map<Long, Register> registers = new HashMap<>();
	
	public Register getRegister(long offset) {
		Register register = registers.get(offset);
		if (register == null) {
			register = new Register();
			registers.put(offset, register);
		}
		return register;
	}
	
	public void set(long offset, long value, boolean verbose) {
		System.out.println("Setting register " + offset + " to " + value);
		long [] indexes = mask.getValuesFor(offset);
		for (int i=0; i<indexes.length; i++) {
			getRegister(indexes[i]).value = value;
			if (verbose)
				System.out.println(" => Register " + indexes[i] + " set to " + value);
		}
	}
	
	public long getRegisterSum() {
		long value = 0;
		for (Register register : registers.values())
			value += register.value;
		return value;
	}
	
	public void run(String command, boolean verbose) {
		if (command.startsWith("mask")) {
			String value = command.split("\\s+")[2];
			this.mask = new FloatingMask(value);
			
			if(verbose)
				System.out.println("Mask set to: " + value);
		}
		else if (command.startsWith("mem")) {
			String [] parts = command.split("\\s+");
			long index = Long.parseLong(parts[0].substring("mem[".length(), parts[0].length()-1));
			long value = Long.parseLong(parts[2]);
			set(index, value, verbose);
		} 
		else {
			throw new IllegalArgumentException(command);
		}
	}
	
}
