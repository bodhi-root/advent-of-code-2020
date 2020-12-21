package day19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	
	/**
	 * A rule has an ID and is able to match a substring from a character array.
	 */
	static abstract class Rule {
		
		String id;
		
		public Rule(String id) {
			this.id = id;
		}
		
		/**
		 * The rule will attempt to match characters beginning at the given index.
		 * If successful, it will return the number of characters matched. If not,
		 * it will return -1.
		 */
		public abstract int match(char [] chars, int index);
		
	}
	
	/**
	 * Match a constant value. Our input rules only have individual characters
	 * but we made this so it can do strings of any lengths.
	 */
	static class Constant extends Rule {
		
		char [] pattern;
		
		public Constant(String id, char [] pattern) {
			super(id);
			this.pattern = pattern;
		}
		
		public int match(char [] chars, int index) {
			if (index + pattern.length > chars.length)
				return -1;
			
			for (int i=0; i<pattern.length; i++) {
				if (chars[index+i] != pattern[i])
					return -1;
			}
			
			return pattern.length;
		}
		
	}
	
	/**
	 * Matches a list of rules in order.  An array of rule IDs is stored.
	 * These Rules will be looked up at runtime.
	 * 
	 * This does not extend Rule (because it is often used as an option
	 * in the Options rule and might not always have its own ID).
	 */
	static class Reference {
		
		Input input;
		String [] ruleIds;
		
		public Reference(Input input, String [] ruleIds) {
			this.input = input;
			this.ruleIds = ruleIds;
		}
		
		public int match(char [] chars, int index) {
			int len = 0;
			for (int i=0; i<ruleIds.length; i++) {
				Rule rule = input.getRule(ruleIds[i]);
				int matchLen = rule.match(chars, index + len);
				if (matchLen < 0)
					return -1;
				
				len += matchLen;
			}
			return len;
		}
		
	}
	
	/**
	 * Tries to match one rule from a list of rules. The individual options
	 * are always Reference rules.  The first rule that matches wins.
	 * 
	 * NOTE: Only matching the first rule could cause problems.  This was not
	 * the case in Part A but it did pop up in Part B. For an improved solution
	 * see Main2.java.
	 */
	static class Options extends Rule {
		
		Reference [] rules;
		
		public Options(String id, Reference [] rules) {
			super(id);
			this.rules = rules;
		}
		
		//POTENTIAL PROBLEM HERE IF MULTIPLE PATTERNS MATCH:
		//(This did not occur in part 1 but it did occur in part 2)
		public int match(char [] chars, int index) {
			int matchLen = -1;
			
			for (int i=0; i<rules.length; i++) {
				int len = rules[i].match(chars, index);
				if (len > 0) {
					if (matchLen < 0)
						matchLen = len;
					else
						System.err.println("WARNING: Multiple matches on rule: " + id);
				}
			}
			
			return matchLen;
		}
		
	}
	
	static class Input {
		
		Map<String, Rule> rules = new HashMap<>();
		List<String> lines = new ArrayList<>();
		
		public Rule getRule(String id) {
			Rule rule = rules.get(id);
			if (rule == null)
				throw new IllegalArgumentException("No such rule: " + id);
			
			return rule;
		}
		
		public void addRule(Rule rule) {
			rules.put(rule.id, rule);
		}
		
		public static Input parseFromFile(File file) throws IOException {
			Input input = new Input();
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				String line;
				
				//read rules:
				while ((line = in.readLine()) != null) {
					if (line.trim().isEmpty())
						break;
					
					Rule rule = input.parseRule(line);
					input.addRule(rule);
				}
				
				//read input:
				while ((line = in.readLine()) != null) {
					input.lines.add(line);
				}
			}
			finally {
				in.close();
			}
			
			return input;
		}
		
		public Rule parseRule(String line) {
			String [] parts = line.split(":", 2);
			String id = parts[0];
			
			String remainder = parts[1].trim();
			if (remainder.charAt(0) == '"') {
				return new Constant(id, new char [] {remainder.charAt(1)});
			} else {
				
				String [] options = remainder.split("\\|");
				Reference [] rules = new Reference[options.length];
				for (int i=0; i<options.length; i++) {
					String [] ruleIds = options[i].trim().split("\\s+");
					rules[i] = new Reference(this, ruleIds);
				}
				
				return new Options(id, rules);
			}
		}
		
		public int countMatches(String id) {
			Rule rule = getRule(id);
			int count = 0;
			
			for (String line : this.lines) { 
				int len = rule.match(line.toCharArray(), 0);
				if (len == line.length()) {
					System.out.println("Match: " + line);
					count++;
				} else {
					System.out.println("No Match : " + line + " (len = " + len + ")");
				}
			}
			
			System.out.println("Match Count: " + count);
			return count;
		}
		
	}
	
	public static void testPart1() throws Exception {
		Input input = Input.parseFromFile(new File("files/day19/test.txt"));
		input.countMatches("0");
	}
	
	public static void solvePart1() throws Exception {
		Input input = Input.parseFromFile(new File("files/day19/input.txt"));
		input.countMatches("0");
	}
	
	public static void testPart2() throws Exception {
		Input input = Input.parseFromFile(new File("files/day19/test2.txt"));
		input.addRule(input.parseRule("8: 42 | 42 8"));
		input.addRule(input.parseRule("11: 42 31 | 42 11 31"));
		input.countMatches("0");
	}
	
	public static void solvePart2() throws Exception {
		//unable to solve part 2 with this code (see: Main2.java)
	}
	
	public static void main(String [] args) {
		try {
			testPart1();
			solvePart1();
			//testPart2();
			//solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
