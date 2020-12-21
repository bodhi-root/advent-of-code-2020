package day19;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Modification of Main.java that overcomes the problems we had with our Option
 * matcher when it had multiple matches.  Instead of just picking one option and
 * proceeding, this allows us to explore all possible option paths.  This allows
 * us to solve Part B (in addition to Part A).
 * 
 */
public class Main2 {
	
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
		 * There may be multiple ways that the rule is able to match the input.
		 * We return an array containing the number of characters matched from all
		 * possible ways to match this rule. If no match is found, we return NULL.
		 */
		public abstract int [] match(char [] chars, int index);
		
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
		
		public int [] match(char [] chars, int index) {
			if (index + pattern.length > chars.length)
				return null;
			
			for (int i=0; i<pattern.length; i++) {
				if (chars[index+i] != pattern[i])
					return null;
			}
			
			return new int [] {pattern.length};
		}
		
	}
	
	/**
	 * Matches a list of rules in order.  An array of rule IDs is stored.
	 * These Rules will be looked up at runtime.
	 * 
	 * This does not extend Rule (because it is often used as an option
	 * in the Options rule and might not always have its own ID).
	 * 
	 * TODO: Rules don't need to store their IDs.  If we were to separate
	 *       IDs from Rules we could make a much more powerful language.
	 */
	static class Reference {
		
		Input input;
		String [] ruleIds;
		
		public Reference(Input input, String [] ruleIds) {
			this.input = input;
			this.ruleIds = ruleIds;
		}
		
		/**
		 * Since each Reference can return an array of options, we have to 
		 * implement some recursion here to explore all possible paths.
		 * A unique Set of lengths is maintained and returned.  (We don't
		 * care about duplicates since they would just slow down processing).
		 */
		public int [] match(char [] chars, int index) {
			Set<Integer> matches = new HashSet<>();
			extendMatches(matches, chars, index, 0, 0);
			
			int [] result = new int[matches.size()];
			Iterator<Integer> iResult = matches.iterator();
			for (int i=0; i<result.length; i++)
				result[i] = iResult.next().intValue();
			return result;
		}
		
		protected void extendMatches(Set<Integer> matchList, char [] chars, int index, int len, int ruleIdx) {
			String ruleId = this.ruleIds[ruleIdx];
			Rule rule = input.getRule(ruleId);
			
			int [] matches = rule.match(chars, index+len);
			if (matches == null)
				return;
			
			int nextIdx = ruleIdx+1;
			
			//we've matched everything: add final lengths:
			if (nextIdx == this.ruleIds.length) {
				for (int i=0; i<matches.length; i++)
					matchList.add(matches[i]+len);
			}
			//keep going:
			else {
				for (int i=0; i<matches.length; i++)
					extendMatches(matchList, chars, index, matches[i] + len, nextIdx);
			}
			
		}
		
	}
	
	/**
	 * Tries to match one rule from a list of rules. The individual options
	 * are always Reference rules.  An array of lengths is returned with one
	 * entry for each rule that matched.
	 */
	static class Options extends Rule {
		
		Reference [] rules;
		
		public Options(String id, Reference [] rules) {
			super(id);
			this.rules = rules;
		}
		
		public int [] match(char [] chars, int index) {
			int [][] matches = new int[rules.length][];
			
			for (int i=0; i<rules.length; i++) {
				matches[i] = rules[i].match(chars, index);
			}
			
			return flatten(matches);
		}
		
		/**
		 * Flattens the list of match lengths.  Each match[i] contains possible
		 * lengths that this rule can match.  The combined result for all options
		 * is a concatenation of all these arrays (with duplicates removed to
		 * improve downstream performance).
		 */
		protected int [] flatten(int [][] matches) {
			Set<Integer> set = new HashSet<>();
			
			for (int i=0; i<matches.length; i++) {
				if (matches[i] != null) {
					for (int j=0; j<matches[i].length; j++)
						set.add(matches[i][j]);
				}
			}
			
			int [] result = new int[set.size()];
			Iterator<Integer> iResult = set.iterator();
			for (int i=0; i<result.length; i++)
				result[i] = iResult.next().intValue();
			return result;
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
				int [] matchLens = rule.match(line.toCharArray(), 0);
				
				//see if any of these match the entire string
				boolean valid = false;
			    if (matchLens != null) {
			    	for (int i=0; i<matchLens.length; i++) {
			    		if (matchLens[i] == line.length()) {
			    			valid = true;
			    			break;
			    		}
			    	}
			    }
				
				if (valid) {
					System.out.println("Match: " + line);
					count++;
				} else {
					System.out.println("No Match : " + line + " (len = " + Arrays.toString(matchLens) + ")");
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
		Input input = Input.parseFromFile(new File("files/day19/input.txt"));
		input.addRule(input.parseRule("8: 42 | 42 8"));
		input.addRule(input.parseRule("11: 42 31 | 42 11 31"));
		input.countMatches("0");
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
