package day18;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

public class Main {
	
	static interface Term {
		
		long value();
		
	}
	
	static class Constant implements Term {
		
		long value;
		
		public Constant(long value) {
			this.value = value;
		}
		
		public long value() {
			return value;
		}
		
	}
	
	static class Addition implements Term {
		
		Term term1;
		Term term2;
		
		public Addition(Term term1, Term term2) {
			this.term1 = term1;
			this.term2 = term2;
		}
		
		public long value() {
			return term1.value() + term2.value();
		}
		
	}
	
	static class Multiplication implements Term {
		
		Term term1;
		Term term2;
		
		public Multiplication(Term term1, Term term2) {
			this.term1 = term1;
			this.term2 = term2;
		}
		
		public long value() {
			return term1.value() * term2.value();
		}
		
	}
	
	static enum Method {PART1, PART2};
	
	public static final char [] OPERATORS = new char [] {'+', '*'};
	
	public static Term parse(String text) {
		return parse(text, false);
	}
	
	public static Term parse(String text, boolean verbose) {
		return parse(text, Method.PART1, verbose);
	}
	
	public static Term parse(String text, Method method, boolean verbose) {
		text = text.replaceAll("\\s+", "");
		char [] chars = text.toCharArray();
		
		List<Term> terms = new ArrayList<>();
		List<Character> operators = new ArrayList<>();
		
		int nextIdx = 0;
		while (nextIdx < chars.length) {
	
			//parse Term:
			boolean isParen = chars[nextIdx] == '(';
		
			//term must begin with '(' or number:
			if (isParen) {
				
				int startIdx = nextIdx;
				
				//find closing brace:
				int depth = 1;
				while (depth > 0) {
					nextIdx++;
					if (chars[nextIdx] == '(')
						depth++;
					else if (chars[nextIdx] == ')')
						depth--;
				}
				
				String innerText = text.substring(startIdx+1, nextIdx);
				terms.add(parse(innerText, method, verbose));
				if (verbose)
					System.out.println("Added term: " + innerText);
				
				nextIdx++;
				
			} else {

				StringBuilder buff = new StringBuilder();
				while (nextIdx < chars.length && Character.isDigit(chars[nextIdx]))
					buff.append(chars[nextIdx++]);
				
				terms.add(new Constant(Long.valueOf(buff.toString())));
				if (verbose)
					System.out.println("Added constant: " + buff.toString());
			}
		
			//parse operator:
			if (nextIdx < chars.length) {
				char op = chars[nextIdx++];
				operators.add(op);
				if (verbose)
					System.out.println("Added operator: " + op);
			}
		}
		
		// if only 1 term, this is easy:
		Term term = terms.get(0);
		if (terms.size() == 1)
			return term;

		if (method == Method.PART1) { 

			//build terms left-to-right
			for (int i=1; i<terms.size(); i++) {
				char operator = operators.get(i-1);
				if (operator == '+') {
					term = new Addition(term, terms.get(i));
				} else if (operator == '*') {
					term = new Multiplication(term, terms.get(i));
				} else {
					throw new IllegalStateException("Invalid operator: " + operator);
				}
			}
			
		} else {
			
			//addition first:
			for (int i=0; i<terms.size()-1; i++) {
				if (operators.get(i) == '+') {
					terms.set(i, new Addition(terms.get(i), terms.remove(i+1)));
					operators.remove(i);
					i--;
				}
			}
			
			//multiplication next:
			for (int i=0; i<terms.size()-1; i++) {
				if (operators.get(i) == '*') {
					terms.set(i, new Multiplication(terms.get(i), terms.remove(i+1)));
					operators.remove(i);
					i--;
				}
			}
			
			term = terms.get(0);
		}
		
		return term;
	}
	
	public static void testPart1() {
		String [] tests = new String [] {
				"1 + 2 * 3 + 4 * 5 + 6", 		// 71
				"1 + (2 * 3) + (4 * (5 + 6))", 	// 51
				"2 * 3 + (4 * 5)", 				// becomes 26.
				"5 + (8 * 3 + 9 + 3 * 4 * 3)",	// becomes 437.
				"5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 		// becomes 12240.
				"((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" 	// becomes 13632
		};
		
		boolean verbose = false;
		for (String test : tests) {
			System.out.println(parse(test, verbose).value());
		}
	}
	
	public static void solvePart1() throws Exception {
		long sum = 0;
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day18/input.txt"));
		for (String line : lines) {
			long value = parse(line, false).value();
			System.out.println(value);
			sum += value;
		}
		
		System.out.println("Sum = " + sum);
	}
	
	public static void testPart2() {
		String [] tests = new String [] {
				"1 + 2 * 3 + 4 * 5 + 6", 		// 231
				"1 + (2 * 3) + (4 * (5 + 6))", 	// 51
				"2 * 3 + (4 * 5)", 				// becomes 46.
				"5 + (8 * 3 + 9 + 3 * 4 * 3)",	// becomes 1445
				"5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 		// becomes 669060
				"((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" 	// becomes 23340
		};
		
		boolean verbose = false;
		for (String test : tests) {
			System.out.println(parse(test, Method.PART2, verbose).value());
		}
	}
	
	public static void solvePart2() throws Exception {
		long sum = 0;
		
		List<String> lines = FileUtil.readLinesFromFile(new File("files/day18/input.txt"));
		for (String line : lines) {
			long value = parse(line, Method.PART2, false).value();
			System.out.println(value);
			sum += value;
		}
		
		System.out.println("Sum = " + sum);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
